from .models import User
from rest_framework import serializers
from database import settings
class UserSerializer(serializers.Serializer):
    user_nickname = serializers.CharField(max_length=10)
    user_email = serializers.CharField(max_length=30)
    user_definition = serializers.CharField(max_length=100)
    password = serializers.CharField()
    user_profile_image = serializers.CharField(max_length=200)
    def validate_unique_user_nickname(self, value):
        if User.objects.filter(user_nickname=value).exists():
            raise serializers.ValidationError(
                {"error_name": "닉네임 중복", "error_id": 1}
            )
        return value
    def validate_unique_user_email(self, value):
        if User.objects.filter(user_email=value).exists():
            raise serializers.ValidationError(
                {"error_name": "이메일 중복", "error_id": 2}
            )
        return value
    def create(self, validated_data):
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




class UserProfileUploadSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ["user_nickname", "user_profile_image"]
    def update(self, instance, validated_data):
        instance.user_profile_image = validated_data["user_profile_image"]
        instance.save()
        return instance
