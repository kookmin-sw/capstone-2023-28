from django.db import models
from authentication.models import User
class Feed(models.Model):
    feed_id = models.AutoField(primary_key=True)
    user_id = models.ForeignKey(User, on_delete=models.CASCADE)
    content = models.CharField(max_length=200)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

class FeedHashTag(models.Model):
    feed_id = models.ForeignKey(Feed, related_name="hash_tags", on_delete=models.CASCADE)
    hash_tag = models.CharField(max_length=10)

class Comment(models.Model):
    user_id = models.ForeignKey(User, related_name='comment_user', on_delete=models.CASCADE)
    feed_id = models.ForeignKey(Feed, related_name='comments', on_delete=models.CASCADE)
    content = models.CharField(max_length=50)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

class FeedImage(models.Model):
    feed_id = models.ForeignKey(Feed, related_name='images', on_delete=models.CASCADE)
    image = models.CharField(max_length=255)

class Like(models.Model):
    user_id = models.ForeignKey(User, related_name='like_user', on_delete=models.CASCADE)
    feed_id = models.ForeignKey(Feed, related_name='like_feed', on_delete=models.CASCADE)

    class Meta:
        unique_together=('user_id', 'feed_id')
class Dislike(models.Model):
    user_id = models.ForeignKey(User, related_name='dislike_user', on_delete=models.CASCADE)
    feed_id = models.ForeignKey(Feed, related_name='dislike_feed', on_delete=models.CASCADE)

    class Meta:
        unique_together=('user_id', 'feed_id')