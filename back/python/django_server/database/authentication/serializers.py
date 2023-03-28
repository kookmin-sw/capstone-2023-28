from .models import User
from rest_framework import serializers
from database import settings

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = "__all__"

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
