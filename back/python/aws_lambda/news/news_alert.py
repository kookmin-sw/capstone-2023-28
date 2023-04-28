import requests
import time
from typing import List
from utils.key import CLIENT_SECRET, CLIENT_ID
import threading

class Observer:
    def update(self, *args, **kwargs):
        pass

class NaverNewsObserver(Observer):
    def __init__(self):
        self.last_news = None
    
    def update(self, news):
        if news != self.last_news:
            print(f"New news article found: {news['title']} - {news['originallink']}")
            self.last_news = news

class NaverBlogObserver(Observer):
    def __init__(self):
        self.last_blog = None
    
    def update(self, blog):
        if blog != self.last_blog:
            print(f"New blog article found: {blog['title']} - {blog['link']}")
            self.last_blog = blog

class NaverCafeObserver(Observer):
    def __init__(self):
        self.last_cafe = None
    
    def update(self, cafe):
        if cafe != self.last_cafe:
            print(f"New cafe article found: {cafe['title']} - {cafe['link']}")
            self.last_cafe = cafe

class NaverSearchAPI:
    def __init__(self, keyword: str):
        self.observers = []
        self.keyword = keyword
    
    def attach(self, observer: Observer):
        self.observers.append(observer)
    
    def detach(self, observer: Observer):
        self.observers.remove(observer)
    
    def notify_observers(self, article, article_type):
        for observer in self.observers:
            if article_type == "news":
                observer.update(article)
            elif article_type == "blog":
                observer.update(article)
            elif article_type == "cafe":
                observer.update(article)
    
    def search_news(self):
        while True:
            url = "https://openapi.naver.com/v1/search/news.json"
            headers = {
                "X-Naver-Client-Id": CLIENT_ID,
                "X-Naver-Client-Secret": CLIENT_SECRET
            }
            params = {"query": self.keyword}
            response = requests.get(url, headers=headers, params=params)
            if response.status_code == 200:
                for article in response.json()["items"]:
                    if "호선" in article["title"]:
                        self.notify_observers(article, "news")
            time.sleep(10)
    
    def search_blog(self):
        while True:
            url = "https://openapi.naver.com/v1/search/blog.json"
            headers = {
                "X-Naver-Client-Id": CLIENT_ID,
                "X-Naver-Client-Secret": CLIENT_SECRET
            }
            params = {"query": self.keyword}
            response = requests.get(url, headers=headers, params=params)
            if response.status_code == 200:
                for article in response.json()["items"]:
                    if "호선" in article["title"]:
                        self.notify_observers(article, "blog")
            time.sleep(10)
    
    def search_cafe(self):
        while True:
            url = "https://openapi.naver.com/v1/search/cafearticle.json"
            headers = {
                "X-Naver-Client-Id": CLIENT_ID,
                "X-Naver-Client-Secret": CLIENT_SECRET
            }
            params = {"query": self.keyword}
            response = requests.get(url, headers=headers, params=params)
            if response.status_code == 200:
                for article in response.json()["items"]:
                    if "호선" in article["title"]:
                        self.notify_observers(article, "cafe")
            time.sleep(10)

if __name__ == "__main__":
    observer1 = NaverNewsObserver()
    observer2 = NaverBlogObserver()
    observer3 = NaverCafeObserver()
    naver_api = NaverSearchAPI("호선")
    naver_api.attach(observer1)
    naver_api.attach(observer2)
    naver_api.attach(observer3)
    thread_news = threading.Thread(target=naver_api.search_news)
    thread_blog = threading.Thread(target=naver_api.search_blog)
    thread_cafe = threading.Thread(target=naver_api.search_cafe)
    thread_news.start()
    thread_blog.start()
    thread_cafe.start()
