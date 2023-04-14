from rest_framework import serializers
from .models import *
from authentication.models import User
class CommentSerializer(serializers.ModelSerializer):
    class Meta:
        model = Comment
        fields = ["feed", "content", "created_at", "updated_at"]

class FeedSerializer(serializers.ModelSerializer):
    comments = CommentSerializer(many=True, read_only=True)
    class Meta:
        model = Feed
        fields = ("content", "created_at", "updated_at", "comments")
    def create(self, validated_data):
        user_id = self.context.get("user_id")
        validated_data["user_id"] = User.objects.get(id=user_id)
        feed = Feed.objects.create(**validated_data)
        return feed


