from rest_framework import serializers
from .models import *
from authentication.models import User
from authentication.serializers import UserSerializer
from database import settings
from django.db import IntegrityError
import boto3
import base64

class LikeSerializer(serializers.ModelSerializer):
    class Meta:
        model = Like
        fields = ["feed_id"]
    def create(self, validated_data):
        user_id = self.context.get("user_id")
        validated_data["user_id"] = User.objects.get(id=user_id)
        try:
            like = Like.objects.create(**validated_data)
        except IntegrityError:
            raise serializers.ValidationError(
                {"status": "ERROR",
                 "res": {"error_name": "좋아요 중복", "error_id": 1}
                 }
            )
        return like
    def to_representation(self, instance):
        ret = super().to_representation(instance)
        ret["user_id"] = instance.user_id_id
        return ret
class DislikeSerializer(serializers.ModelSerializer):
    class Meta:
        model = Dislike
        fields = ["feed_id"]
    def create(self, validated_data):
        user_id = self.context.get("user_id")
        validated_data["user_id"] = User.objects.get(id=user_id)
        try:
            dislike = Dislike.objects.create(**validated_data)
        except IntegrityError:
            raise serializers.ValidationError(
                {"status": "ERROR",
                 "res": {"error_name": "싫어요 중복", "error_id": 1}
                 }
            )
        return dislike
    def to_representation(self, instance):
        ret = super().to_representation(instance)
        ret["user_id"] = instance.user_id_id
        return ret
class CommentSerializer(serializers.ModelSerializer):
    class Meta:
        model = Comment
        fields = ["user_id", "feed_id", "content", "created_at", "updated_at"]
        extra_kwargs = {"user_id": {"required": False}}
    def to_representation(self, instance):
        ret = super().to_representation(instance)
        user = UserSerializer(instance.user_id).data

        user_dict = {}
        user_dict["user_id"] = ret["user_id"]
        user_dict["user_email"] = user["user_email"]
        user_dict["user_profile_image"] = user["user_profile_image"]
        user_dict["user_nickname"] = user["user_nickname"]

        ret["user"] = user_dict
        del ret["user_id"]
        return ret
    def create(self, validated_data):
        user_id = self.context.get("user_id")
        validated_data["user_id"] = User.objects.get(id=user_id)
        comment = Comment.objects.create(**validated_data)
        return comment
class FeedImageSerializer(serializers.ModelSerializer):
    class Meta:
        model = FeedImage
        fields = "__all__"
    def to_representation(self, instance):
        ret = super().to_representation(instance)
        s3_client = boto3.client(
            's3',
            aws_access_key_id=settings.AWS_ACCESS_KEY_ID,
            aws_secret_access_key=settings.AWS_SECRET_ACCESS_KEY,
            region_name=settings.AWS_S3_REGION_NAME
        )
        body = s3_client.get_object(Bucket=settings.AWS_STORAGE_BUCKET_NAME ,Key=ret['image'])["Body"]
        raw_data = body.read()
        ret['image'] = base64.b64encode(raw_data).decode('utf-8')
        del ret['feed_id']
        return ret
class FeedHashTagSerializer(serializers.ModelSerializer):
    class Meta:
        model = FeedHashTag
        fields = ["hash_tag"]
class FeedSerializer(serializers.ModelSerializer):
    images = FeedImageSerializer(many=True, read_only=True)
    hash_tags = FeedHashTagSerializer(many=True, read_only=True)
    comments_num = serializers.SerializerMethodField()
    likes_num = serializers.SerializerMethodField()
    is_liked = serializers.SerializerMethodField()
    class Meta:
        model = Feed
        fields = ("feed_id", "user_id", "content", "created_at", "updated_at", "comments_num", "likes_num", "is_liked", "images", "hash_tags")
        extra_kwargs = {"user_id": {"required": False}}
    def create(self, validated_data):
        user_id = self.context.get("user_id")
        hash_tags = self.context.get("hash_tags")
        validated_data["user_id"] = User.objects.get(id=user_id)
        feed = Feed.objects.create(**validated_data)
        for hash_tag in hash_tags:
            FeedHashTag.objects.create(feed_id_id=feed.feed_id ,hash_tag=hash_tag)
        return feed
    def get_comments_num(self, obj):
        return obj.comments.count()
    def get_likes_num(self, obj):
        return obj.like_feed.count()
    def get_is_liked(self, obj):
        user_id = self.context.get("user_id")
        print(user_id)
        is_liked = obj.like_feed.filter(user_id=user_id).exists()
        return is_liked

    def to_representation(self, instance):
        ref = super().to_representation(instance)
        ref["user"] = UserSerializer(instance.user_id).data
        return ref
