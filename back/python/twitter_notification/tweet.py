import os
import requests
import json
import time
from abc import ABC, abstractmethod
from dotenv import load_dotenv
from utils.key import BEARER_TOKEN

load_dotenv()  # load environment variables from .env file

bearer_token = BEARER_TOKEN

class TwitterStreamer(ABC):
    def stream_tweets(self):
        self._setup_stream()
        while True:
            time.sleep(1)
            response = self._get_stream_response()
            if response.status_code == 200:
                for line in response.iter_lines():
                    if line:
                        tweet = json.loads(line)
                        if "data" in tweet:
                            self.handle_tweet(tweet)
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
    
    @staticmethod
    def _get_stream_response():
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
        print()


if __name__ == "__main__":
    SteelohssStreamer().stream_tweets()