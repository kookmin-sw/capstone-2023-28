import os
import requests
import json
import time
from dotenv import load_dotenv
from utils.key import BEARER_TOKEN

load_dotenv()  # load environment variables from .env file

bearer_token = BEARER_TOKEN

def stream_rules():
    """
    Set up stream rules for filtering tweets.
    """
    rules = [
        {"value": "from:steelohss", "tag": "steelohss-tweets"}
    ]
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

def stream_tweets():
    """
    Stream tweets and print a notification when a new tweet is detected.
    """
    stream_url = "https://api.twitter.com/2/tweets/search/stream"
    headers = {"Authorization": f"Bearer {bearer_token}"}
    while True:
        time.sleep(1)
        response = requests.get(stream_url, headers=headers, stream=True)
        if response.status_code == 200:
            break
        print(
            f"Error occurred: Cannot stream tweets (HTTP {response.status_code}): {response.text}"
        )
        print("Retrying in 30 seconds...")
        time.sleep(30)
    for line in response.iter_lines():
        if line:
            tweet = json.loads(line)
            if "data" in tweet:
                print("New tweet from @steelohss!")
                print(tweet["data"]["text"])
                print()

if __name__ == "__main__":
    stream_rules()
    stream_tweets()
