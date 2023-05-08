# If a user makes a new tweet, it sends info and notify user through firebase.
#
# Requires utils folder to run this file.
#
# ===========================================================================




import os
import requests
import json
import time
from abc import ABC, abstractmethod
from dotenv import load_dotenv
from firebase_admin import credentials, firestore, initialize_app, messaging
from utils.key import BEARER_TOKEN

import os
print(os.getcwd())

load_dotenv()  # load environment variables from .env file

bearer_token = BEARER_TOKEN

# Initialize Firebase
cred = credentials.Certificate("utils/serviceAccountKey.json")
initialize_app(cred)
db = firestore.client()


class TwitterStreamer(ABC):
    def __init__(self):
        self.retry_time = 60
        self.retry_count = 0
    
    def stream_tweets(self):
        self._setup_stream()
        while True:
            time.sleep(5)
            response = self._get_stream_response()
            if response.status_code == 200:
                self.retry_count = 0
                for line in response.iter_lines():
                    if line:
                        tweet = json.loads(line)
                        if "data" in tweet:
                            self.handle_tweet(tweet)
            elif response.status_code == 429:
                self.retry_count += 1
                print(
                    f"Rate limit reached: {response.text}\nRetrying in {self.retry_time} seconds (attempt {self.retry_count})"
                )
                time.sleep(self.retry_time)
                self.retry_time += 60
            else:
                print(
                    f"Error occurred: Cannot stream tweets (HTTP {response.status_code}): {response.text}"
                )
                print("Retrying in 30 seconds...")
                time.sleep(30)

    def _setup_stream(self):
        rules = self.get_rules()
        payload = {"add": rules}
        headers = {
            "Content-Type": "application/json",
            "Authorization": f"Bearer {bearer_token}"
        }
        response = requests.post(
            "https://api.twitter.com/2/tweets/search/stream/rules",
            data=json.dumps(payload),
            headers=headers
        )
        if response.status_code != 201:
            raise Exception(
                f"Error occurred: Cannot set up stream rules (HTTP {response.status_code}): {response.text}"
            )

    @abstractmethod
    def get_rules(self):
        pass

    @abstractmethod
    def handle_tweet(self, tweet):
        pass

    def _get_stream_response(self):
        stream_url = "https://api.twitter.com/2/tweets/search/stream"
        headers = {"Authorization": f"Bearer {bearer_token}"}
        return requests.get(stream_url, headers=headers, stream=True)



class SteelohssStreamer(TwitterStreamer):
    def get_rules(self):
        return [
            {"value": "from:steelohss", "tag": "steelohss-tweets"}
        ]
    
    def handle_tweet(self, tweet):
        print("New tweet from @steelohss!")
        print(tweet["data"]["text"])
        
        # Send data to Firebase
        doc_ref = db.collection("tweets").document(tweet["data"]["id"])
        doc_ref.set(tweet["data"])
        
        # Send notification to mobile device
        message = messaging.Message(
            topic="steelohss-tweets",
            notification=messaging.Notification(
                title="New tweet from @steelohss!",
                body=tweet["data"]["text"]
            )
        )
        response = messaging.send(message)
        print("Notification sent to mobile device")
        print()


if __name__ == "__main__":
    SteelohssStreamer().stream_tweets()