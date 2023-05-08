# This code is for AWS Lambda in python. 
#
# Gets ranking for line 1 to 9 for past 2 hours in twitter.
#
# https://grwlkdonoj.execute-api.ap-northeast-1.amazonaws.com/default/twitter-line-ranking
#
#
# =========================================================================


import json
import tweepy
import os
import datetime


def load_tweet_api():
    api_key = os.environ.get('CONSUMER_KEY')
    api_key_secret = os.environ.get('CONSUMER_SECRET')
    bearer_token = os.environ.get('BEARER_TOKEN')
    access_token = os.environ.get('ACCESS_TOKEN')
    access_token_secret = os.environ.get('ACCESS_TOKEN_SECRET')

    auth = tweepy.OAuthHandler(api_key, api_key_secret)
    auth.set_access_token(access_token, access_token_secret)

    client = tweepy.Client(bearer_token=bearer_token)

    twitter_data = []
    for i in range(1, 10):
        query = f'{i}호선'
        counts = client.get_recent_tweets_count(query=query, granularity='hour', start_time=start_time)
        saved_data = {'name': query}
        for count in counts.data:
            if count['start'] == start_time:
                saved_data['count'] = count['tweet_count']
                break
        twitter_data.append(saved_data)

    ranking = sorted(twitter_data, key=lambda x: x['count'], reverse=True)
    real_rank = [{'name': rank['name'], 'rank': rank_num, 'count': rank['count']} for rank_num, rank in enumerate(ranking, start=1)]

    return {'data': real_rank}


def get_time():
    dt_utc = datetime.datetime.utcnow() - datetime.timedelta(hours=2)
    dt_utc = str(dt_utc)
    ymd, hms = dt_utc.split(' ')
    hms = hms[:8]
    global start_time 
    start_time = f'{ymd}T{hms[:2]}:00:00.000Z'


def respond(res):
    return {
        'statusCode': 200,
        'body': json.dumps(res, ensure_ascii=False),
        'headers': {
            'Content-Type': 'application/json',
        },
    }


def lambda_handler(event, context):
    get_time()
    return respond(load_tweet_api())
