from .models import User
from rest_framework import serializers
from rest_framework_simplejwt.serializers import TokenObtainPairSerializer
from rest_framework.exceptions import ValidationError
from database import settings
import boto3

class UserSerializer(serializers.Serializer):
    user_nickname = serializers.CharField(max_length=10)
    user_email = serializers.EmailField(max_length=30)
    user_definition = serializers.CharField(max_length=100, allow_null=True, required=False)
    password = serializers.CharField()
    user_profile_image = serializers.CharField(max_length=200, allow_null=True, required=False)
    def validate_unique_user_nickname(self, value):
        if User.objects.filter(user_nickname=value).exists():
            raise serializers.ValidationError(
                {"status": "ERROR",
                 "res": {"error_name": "닉네임 중복", "error_id": 1}
                 }
            )
        return value
    def validate_unique_user_email(self, value):
        if User.objects.filter(user_email=value).exists():
            raise serializers.ValidationError(
                {"status": "ERROR",
                 "res": {"error_name": "이메일 중복", "error_id": 2}
                 }
            )
        return value
    def create(self, validated_data):
        print(validated_data)
        # 여기서 email 과 nickname validation exception 둘 다 raise 하는 방법을 모르겠음.
        self.validate_unique_user_email(validated_data.get("user_email"))
        self.validate_unique_user_nickname(validated_data.get("user_nickname"))
        user = User.objects.create(**validated_data)
        return user
    def update(self, instance, validated_data):
        self.validate_unique_user_nickname(validated_data.get("user_nickname"))
        instance.user_nickname = validated_data.get("user_nickname", instance.user_nickname)
        instance.user_definition = validated_data.get("user_definition", instance.user_definition)
        instance.user_profile_image = validated_data.get("user_profile_image", instance.user_profile_image)
        instance.save_without_password()
        return instance
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
                'Key': ret['user_profile_image'],
            }
        )
        ret['user_profile_image'] = url
        del ret['password']
        return ret



class UserProfileUploadSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ["user_profile_image"]
    def update(self, instance, validated_data):
        instance.user_profile_image = validated_data["user_profile_image"]
        instance.save()
        return instance

class CustomTokenObtainPairSerializer(TokenObtainPairSerializer):
    default_error_messages = {
        "no_active_account": {"status": "ERROR",
                                   "res": {
                                       "error_name": "No active account found with the given credentials",
                                       "error_id": 0
                                   }}
    }
    @classmethod
    def get_token(cls, user):
        token = super().get_token(user)
        token["user_email"]=user.user_email
        token["user_id"]=user.id
        return token

