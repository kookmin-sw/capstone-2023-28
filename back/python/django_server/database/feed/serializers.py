from rest_framework import serializers
from .models import Feed
class FeedSerializer(serializers.ModelSerializer):
    class Meta:
        model = Feed
        fields = ("feed_user_id", "feed_content")
