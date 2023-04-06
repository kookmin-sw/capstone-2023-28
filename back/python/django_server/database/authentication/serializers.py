from .models import User
from rest_framework import serializers
from database import settings
class UserSerializer(serializers.Serializer):
    user_nickname = serializers.CharField(max_length=10)
    user_email = serializers.CharField(max_length=30)
    user_definition = serializers.CharField(max_length=100)
    password = serializers.CharField()
    user_profile_image = serializers.CharField(max_length=200)
    def validate_user_nickname(self, value):
        if User.objects.filter(user_nickname=value).exists():
            raise serializers.ValidationError(
                {"error_name": "닉네임 중복", "error_id": 1}
            )
        return value
    def validate_user_email(self, value):
        if User.objects.filter(user_email=value).exists():
            raise serializers.ValidationError(
                {"error_name": "이메일 중복", "error_id": 2}
            )
    def validate(self, data):
        self.validate_user_nickname(data['user_nickname'])
        return data
    def create(self, validated_data):
        return User(**validated_data)
    def update(self, instance, validated_data):
        instance.user_definition = validated_data.get("user_definition", instance.user_definition)
        instance.user_profile_image = validated_data.get("user_profile_image", instance.user_profile_image)
        instance.save()
        return instance




class UserProfileUploadSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ["user_nickname", "user_profile_image"]
    def update(self, instance, validated_data):
        instance.user_profile_image = validated_data["user_profile_image"]
        instance.save()
        return instance
