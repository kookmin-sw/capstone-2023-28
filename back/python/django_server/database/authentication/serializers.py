from .models import User
from rest_framework import serializers

class UserSerializer(serializers.ModelSerializer):
    def create(self, validated_data):
        user = User.objects.create_user(
            user_email=validated_data["user_email"],
            user_nickname = validated_data["user_nickname"],
            user_definition = validated_data["user_definition"],
            user_profile_image = validated_data["user_profile_image"],
            user_point_number = validated_data["user_point_number"],
            password = validated_data["password"]
        )
        return user

    class Meta:
        model = User
        fields = "__all__"