import requests
import json
import time
from typing import List
from utils.key import CLIENT_ID, CLIENT_SECRET
from datetime import datetime, timedelta

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
        end_date = datetime.now().strftime('%Y-%m-%d')
        start_date = (datetime.now() - timedelta(days=1)).strftime('%Y-%m-%d')
        params = {"query": query, "display": 3, "sort": "date", "start_date": start_date, "end_date": end_date}
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




def main2():
    news = News()
    api = NaverNewsAPI()
    watchers = [NewsWatcher("호선"), NewsWatcher("지하철")]
    for watcher in watchers:
        news.subscribe(watcher)

    while True:
        for watcher in watchers:
            items = api.search_news(watcher.keyword)
            news.notify(items)

        time.sleep(60)


if __name__ == '__main__':
    main()
