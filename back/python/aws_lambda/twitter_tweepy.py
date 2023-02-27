import json
import tweepy
import os
import datetime


start_time = ''

def load_tweet_api():

    api_key = 'xgadpZzhHTBNI1CH7HpCBpIT1'
    api_key_secret = 'MeUSJ5k620kziOuMVSQjvFMpb7JzAPPcuthmvVs4TPx72dKcei'
    bearer_token = 'AAAAAAAAAAAAAAAAAAAAAF2GlAEAAAAAIjpCswyJ7K2Adfh0nsm4qVrD1jc%3D6aV3vjeOYfiZKp09ScHPoQYoEM8XGHw3U0fUhBIbMGPLZaJmWi'
    access_token = '1172226929543401472-pK6uhTDi5kpQkkx9AQHVx7urbypPM6'
    access_token_secret = 'XtL6Wtv8CmS0wFjXplczqXi030esEA7TgW61FP3yu53IH'

    auth = tweepy.OAuthHandler(api_key, api_key_secret)


    auth.set_access_token(access_token, access_token_secret)

    api = tweepy.API(auth)

    client = tweepy.Client(bearer_token=bearer_token)

    api.wait_on_rate_limit = True


    line_count = {}
    query = []
    for i in range(1, 10):
        query.append(str(i) + '호선')
    
    for querys in query:
        counts = client.get_recent_tweets_count(query=querys, granularity='hour', start_time=start_time)
        for count in counts.data:
            if(count['start'] == start_time):
                line_count[querys] = count['tweet_count']
    
    ranking = sorted(line_count,key=lambda x:line_count[x], reverse=True)
    
    return ranking


def get_time():
    dt_utc = datetime.datetime.utcnow()

    #dt_utc = dt_utc - datetime.timedelta(hours=9)

    dt_utc = str(dt_utc)
    ymd, hms = map(str, dt_utc.split())
    hms = hms[:8]

    global start_time 
    start_time = ymd + 'T' + hms[:2] + ':00:00' + '.000' + 'Z'
    end_time = ymd + 'T' + hms + '.000' + 'Z'
    
    return 0
    


def respond(res):
    
    return {
        'statusCode' : '200',
        'body' : json.dumps(res, ensure_ascii=False),
        'headers' : {
            'Content-Type' : 'application/json',
        },
    }



def lambda_handler(event, context):
    # TODO implement
    
    get_time()
    
    return respond(load_tweet_api())
