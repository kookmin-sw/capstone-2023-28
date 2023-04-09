"""Get a tweet info from SeoulMetro when they posts a new tweets.""" 


import tweepy

class SeoulMetroTwitterSubject:
    def __init__(self):
        self.observers = []
        self.auth = tweepy.OAuthHandler(CONSUMER_KEY, CONSUMER_SECRET)
        self.auth.set_access_token(ACCESS_TOKEN, ACCESS_TOKEN_SECRET)
        self.api = tweepy.API(self.auth)
        self.last_tweet_id = None

    def register_observer(self, observer):
        self.observers.append(observer)

    def remove_observer(self, observer):
        self.observers.remove(observer)

    def notify_observers(self, tweet):
        for observer in self.observers:
            observer.update(tweet)

    def check_for_new_tweets(self):
        tweet = None
        if self.last_tweet_id is None:
            # If last_tweet_id is None, fetch the latest tweet
            tweet = self.api.user_timeline(id='SeoulMetro')[0]
        else:
            # Fetch only the tweets posted after the last_tweet_id
            tweets = self.api.user_timeline(id='SeoulMetro', since_id=self.last_tweet_id)
            if len(tweets) > 0:
                tweet = tweets[0]

        if tweet is not None:
            self.last_tweet_id = tweet.id
            self.notify_observers(tweet)

class SeoulMetroTwitterObserver:
    def __init__(self):
        self.alarm_message = "New tweet from Seoul Metro: {}"
    
    def update(self, tweet):
        # Send the alarm message
        print(self.alarm_message.format(tweet.text))

# Instantiate the subject and observer objects
seoul_metro_twitter_subject = SeoulMetroTwitterSubject()
seoul_metro_twitter_observer = SeoulMetroTwitterObserver()

# Register the observer with the subject
seoul_metro_twitter_subject.register_observer(seoul_metro_twitter_observer)

# Check for new tweets every 60 seconds
while True:
    seoul_metro_twitter_subject.check_for_new_tweets()
    time.sleep(60)
