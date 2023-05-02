import requests
import json
import time
from typing import List
from datetime import datetime, timedelta
from utils.key import CLIENT_ID, CLIENT_SECRET

class News:
    def __init__(self):
        self.subscribers = set()

    def subscribe(self, subscriber):
        self.subscribers.add(subscriber)

    def unsubscribe(self, subscriber):
        self.subscribers.discard(subscriber)

    def notify(self, message):
        for subscriber in self.subscribers:
            subscriber.update(message)


class Subscriber:
    def update(self, message):
        pass


class NaverNewsAPI:
    def __init__(self):
        self.url = "https://openapi.naver.com/v1/search/news.json"
        self.headers = {"X-Naver-Client-Id": CLIENT_ID, "X-Naver-Client-Secret": CLIENT_SECRET}

    def search_news(self, query: str) -> List[dict]:
        end_time = datetime.now()
        start_time = end_time - timedelta(hours=24)
        params = {
            "query": query,
            "display": 3,
            "sort": "sim",
            "start": 1,
            "news_office_checked": "true",
            "start_date": start_time.strftime('%Y-%m-%d'),
            "end_date": end_time.strftime('%Y-%m-%d'),
        }
        res = requests.get(self.url, headers=self.headers, params=params)
        if res.status_code == 200:
            data = json.loads(res.text)
            if "items" in data:
                return data["items"]
        return []


class NewsWatcher(Subscriber):
    def update(self, message):
        for item in message:
            print(f"New News Found: {item['title']} ({item['link']})")


def main():
    news = News()
    api = NaverNewsAPI()
    watcher = NewsWatcher()
    news.subscribe(watcher)

    while True:
        items = api.search_news("")
        news.notify(items)
        time.sleep(60)


if __name__ == '__main__':
    main()
