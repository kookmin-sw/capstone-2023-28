import requests
import datetime
import json
import os
from html import unescape

def strip_tags(text):
    return unescape(text).replace("<b>", "").replace("</b>", "")

def lambda_handler(event, context):
    client_id = os.environ['CLIENT_ID']
    client_secret = os.environ['CLIENT_SECRET']
    keywords = ['지연', '무정차','시위', '지하철', '장애인']
    base_url = 'https://openapi.naver.com/v1/search/news.json'
    headers = {
        'X-Naver-Client-Id': client_id,
        'X-Naver-Client-Secret': client_secret
    }
    
    # Get the current date and time
    now = datetime.datetime.now()
    yesterday = now - datetime.timedelta(days=1)
    start_date = yesterday.strftime('%Y-%m-%dT00:00:00')
    end_date = now.strftime('%Y-%m-%dT%H:%M:%S')
    
    # Build the query parameters
    query_params = {
        'query': ' '.join(keywords),
        'display': 10,
        'start': 1,
        'sort': 'date',
        'startDate': start_date,
        'endDate': end_date
    }
    
    # Send GET request to Naver API
    response = requests.get(base_url, headers=headers, params=query_params)
    
    # Check if the request was successful
    if response.status_code == 200:
        news_data = response.json()
        news_list = news_data['items']
        
        # Process the retrieved news data
        filtered_news = []
        for news in news_list:
            title = strip_tags(news['title'])
            link = news['link']
            
            # Check if the title contains any of the keywords
            if any(keyword in title for keyword in keywords):
                filtered_news.append({'news': title, 'url': link})
        
        # Create the response data
        response_data = {'data': filtered_news}
        
        return {
            'statusCode': 200,
            'body': json.dumps(response_data, ensure_ascii=False)
        }
    else:
        return {
            'statusCode': response.status_code,
            'body': 'News retrieval failed'
        }
