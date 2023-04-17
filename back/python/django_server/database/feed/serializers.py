from rest_framework import serializers
from .models import *
from authentication.models import User
from API.s3 import S3ImageUploader
from database import settings
import boto3
class CommentSerializer(serializers.ModelSerializer):
    class Meta:
        model = Comment
        fields = ["feed_id", "content", "created_at", "updated_at"]
    def to_representation(self, instance):
        ret = super().to_representation(instance)
        del ret['feed_id']
        return ret
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
        url = s3_client.generate_presigned_url(
            'get_object',
            Params={
                'Bucket': settings.AWS_STORAGE_BUCKET_NAME,
                'Key': ret['image'],
            }
        )
        ret['image'] = url
        del ret['feed_id']
        return ret

class FeedSerializer(serializers.ModelSerializer):
    comments = CommentSerializer(many=True, read_only=True)
    images = FeedImageSerializer(many=True, read_only=True)
    class Meta:
        model = Feed
        fields = ("feed_id", "user_id", "content", "created_at", "updated_at", "comments", "images")
        extra_kwargs = {"user_id": {"required": False}}
    def create(self, validated_data):
        user_id = self.context.get("user_id")
        validated_data["user_id"] = User.objects.get(id=user_id)
        feed = Feed.objects.create(**validated_data)
        return feed



