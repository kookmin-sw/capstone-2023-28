from rest_framework import serializers
from .models import Feed

class FeedCreateSerializer(serializers.ModelSerializer):
    class Meta:
        model = Feed
        fields = ("feed_user_id", "feed_content")
class FeedLoadSerializer(serializers.ModelSerializer):
    num_of_feeds = serializers.IntegerField
    tag_names = serializers.ListField(
        child = serializers.CharField(max_length=10)
    )
    class Meta:
        model = Feed
        fields = ("feed_user_id", "num_of_feeds", "tag_names")
