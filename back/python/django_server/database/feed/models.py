from django.db import models
from authentication.models import User
class Feed(models.Model):
    feed_id = models.AutoField(primary_key=True)
    feed_user_id = models.ForeignKey(User, on_delete=models.CASCADE)
    feed_heart = models.IntegerField(default=0)
    feed_content = models.CharField(max_length=200)
    feed_created_at = models.DateTimeField(auto_now_add=True)
    feed_updated_at = models.DateTimeField(auto_now=True)

    def updated_on(self):
        return self.updated_at.date()
class FeedHashTag(models.Model):
    feed_id = models.ForeignKey(Feed, primary_key=True)
    hash_tag = models.CharField(max_length=10)