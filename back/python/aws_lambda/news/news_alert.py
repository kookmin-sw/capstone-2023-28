import requests
import time
from typing import List

class Observer:
    def update(self, *args, **kwargs):
        pass

class NaverNewsObserver(Observer):
    def __init__(self):
        self.last_news = None
    
    def update(self, news):
        if news != self.last_news:
            print(f"New article found: {news['title']} - {news['originallink']}")
            self.last_news = news

class NaverSearchAPI:
    def __init__(self, keyword: str):
        self.observers = []
        self.keyword = keyword
    
    def attach(self, observer: Observer):
        self.observers.append(observer)
    
    def detach(self, observer: Observer):
        self.observers.remove(observer)
    
    def notify_observers(self, news):
        for observer in self.observers:
            observer.update(news)
    
    def search_news(self):
        while True:
            url = "https://openapi.naver.com/v1/search/news.json"
            headers = {
                "X-Naver-Client-Id": "<YOUR_CLIENT_ID>",
                "X-Naver-Client-Secret": "<YOUR_CLIENT_SECRET>"
            }
            params = {"query": self.keyword}
            response = requests.get(url, headers=headers, params=params)
            if response.status_code == 200:
                for news in response.json()["items"]:
                    if "호선" in news["title"]:
                        self.notify_observers(news)
            time.sleep(10)

if __name__ == "__main__":
    naver_api = NaverSearchAPI("호선")
    observer = NaverNewsObserver()
    naver_api.attach(observer)
    naver_api.search_news()
