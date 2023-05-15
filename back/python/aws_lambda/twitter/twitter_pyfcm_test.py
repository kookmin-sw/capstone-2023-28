import os
import requests
import json
import time
from abc import ABC, abstractmethod
from dotenv import load_dotenv
from pyfcm import FCMNotification
from utils.key import BEARER_TOKEN, FIREBASE_TOKEN

APIKEY = "AAAA7cnIpUQ:APA91bGZxpASqBqs54TRTt1dlgiIE0waj-jz7VURruhsJXj5CnRilkxrYtXVghV2C8lZIYt3Zb2HeAt9XtK3HCXovJ6tXiUrxlSdB0-k5VFjVmYQZjhfBS_mxJiHr60ovP3z3eoY72RZ"
push_service = FCMNotification(APIKEY)

load_dotenv()  # load environment variables from .env file

bearer_token = BEARER_TOKEN

# Initialize Firebase
push_service = FCMNotification(api_key=FIREBASE_TOKEN)  # Replace with your Firebase Server API Key

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
        # Replace this with your own logic to store data in Firebase Firestore
        
        # Send notification to mobile device
        registration_ids = ["<device_registration_token>"]  # Replace with your device registration token
        message = {
            "title": "New tweet from @steelohss!",
            "body": tweet["data"]["text"],
        }
        result = push_service.notify_multiple_devices(registration_ids=registration_ids, data_message=message)
        print("Notification sent to mobile device")
        print()


if __name__ == "__main__":
    SteelohssStreamer().stream_tweets()
