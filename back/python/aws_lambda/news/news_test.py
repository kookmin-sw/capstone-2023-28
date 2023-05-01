import requests
import json
import time
from datetime import datetime, timedelta
from typing import List
from utils.key import CLIENT_ID, CLIENT_SECRET

class Singleton(type):
    """
    This is a Singleton metaclass that ensures only one instance of a class is created.
    """
    _instances = {}

    def __call__(cls, *args, **kwargs):
        if cls not in cls._instances:
            cls._instances[cls] = super().__call__(*args, **kwargs)
        return cls._instances[cls]


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


class NaverNewsAPI(metaclass=Singleton):
    def __init__(self):
        self.url = "https://openapi.naver.com/v1/search/news.json"
        self.headers = {"X-Naver-Client-Id": CLIENT_ID, "X-Naver-Client-Secret": CLIENT_SECRET}

    def search_news(self, query: str) -> List[dict]:
        params = {"query": query, "display": 100}
        res = requests.get(self.url, headers=self.headers, params=params)
        if res.status_code == 200:
            data = json.loads(res.text)
            if "items" in data:
                return data["items"]
        return []

    def get_recent_news(self, query: str, hours: int = 24, count: int = 3) -> List[dict]:
        now = datetime.now()
        target_time = now - timedelta(hours=hours)
        items = []
        page = 1
        while len(items) < count:
            params = {"query": query, "display": 100, "sort": "date", "start": (page - 1) * 100 + 1}
            res = requests.get(self.url, headers=self.headers, params=params)
            if res.status_code == 200:
                data = json.loads(res.text)
                if "items" in data:
                    for item in data["items"]:
                        pub_date = datetime.strptime(item["pubDate"], "%a, %d %b %Y %H:%M:%S +0900")
                        if pub_date < target_time:
                            # articles are sorted by date, so we can break once we reach an old article
                            break
                        items.append(item)
                        if len(items) == count:
                            break
            page += 1
        return items


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
        items = api.get_recent_news("", hours=24, count=3)
        news.notify(items)
        time.sleep(60)


if __name__ == '__main__':
    main()
