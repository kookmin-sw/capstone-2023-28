import requests
import json
import time
from datetime import datetime, timedelta
from typing import List
import os


class News:
    def __init__(self):
        self.subscribers = set()
        self.notified_items = set()

    def subscribe(self, subscriber):
        self.subscribers.add(subscriber)

    def unsubscribe(self, subscriber):
        self.subscribers.discard(subscriber)

    def notify(self, message):
        new_items = []
        for item in message:
            if item['title'] not in self.notified_items:
                new_items.append(item)
                self.notified_items.add(item['title'])
        
        for subscriber in self.subscribers:
            subscriber.update(new_items)


class Subscriber:
    def update(self, message):
        pass


class NaverNewsAPI:
    def __init__(self):
        CLIENT_ID = 'lWeNQNNKurUXDWlvG2rs'
        CLIENT_SECRET = 'vLiUueWfql'
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
        relevant_items = [item for item in message if self.keyword in item['title']]
        for item in relevant_items:
            print(f"New News Found: {item['title']}  ({item['link']})")
        print("\n")


def lambda_handler(event, context):
    news = News()
    api = NaverNewsAPI()

    # Get latest 3 articles from last 24 hours
    now = datetime.now()
    query = {"query": "", "display": 100, "sort": "date"}
    query["query"] = f"datetime:{(now - timedelta(days=1)).strftime('%Y-%m-%d')}:{now.strftime('%Y-%m-%d')}"
    items = api.search_news(query)[:3]
    news.notify(items)

    # Watch for new articles
    watchers = [NewsWatcher("호선"), NewsWatcher("지하철 호선 지연"), NewsWatcher("열차 지연"), ]
    for watcher in watchers:
        news.subscribe(watcher)

    while True:
        new_items = []
        for watcher in watchers:
            query = {"query": watcher.keyword, "display": 10, "sort": "date"}
            items = api.search_news(query)
            new_items += [item for item in items if item not in new_items]
        
        if new_items:
            news_data =[]
            news.notify(new_items)
            for item in new_items:
                if '시위' in item['title'] or '지하철' in item['title']:
                    news_data.append({"name": item['title'], "url": item['link']})
            
            print(json.dumps({"data": news_data}, ensure_ascii=False))
            return {'statusCode': 200,
                'body' : json.dumps({"data": news_data}, ensure_ascii=False)
                }
        break
