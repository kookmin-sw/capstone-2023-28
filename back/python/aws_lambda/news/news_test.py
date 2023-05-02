import requests
import json
import time
from datetime import datetime, timedelta
from typing import List
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
        res = requests.get(self.url, headers=self.headers, params=query)
        if res.status_code == 200:
            data = json.loads(res.text)
            if "items" in data:
                return data["items"]
        return []


class NewsWatcher(Subscriber):
    def __init__(self, keyword):
        self.keyword = keyword

    def update(self, message):
        
        for item in message:
            print(f"New News Found: {item['title']} ({item['link']})")
            print("\n")


def main():
    news = News()
    api = NaverNewsAPI()

    # Get latest 3 articles from last 24 hours
    now = datetime.now()
    query = {"query": "", "display": 100, "sort": "date"}
    query["query"] = f"datetime:{(now - timedelta(days=1)).strftime('%Y-%m-%d')}:{now.strftime('%Y-%m-%d')}"
    items = api.search_news(query)[:3]
    news.notify(items)

    # Watch for new articles
    watchers = [NewsWatcher("호선"), NewsWatcher("지하철")]
    for watcher in watchers:
        news.subscribe(watcher)

    while True:
        for watcher in watchers:
            query = {"query": watcher.keyword, "display": 1, "sort": "date"}
            items = api.search_news(query)
            news.notify(items)

        print("Wainting for 60 seconds... \n")
        time.sleep(60)
        

if __name__ == '__main__':
    main()
