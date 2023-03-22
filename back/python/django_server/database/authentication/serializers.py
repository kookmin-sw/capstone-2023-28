from .models import User
from rest_framework import serializers

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = "__all__"

class UserCheckSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = "__all__"

    def update(self, instance, validated_data):
        instance.user_definition = validated_data.get("user_definition", instance.user_definition)
        instance.user_profile_image = validated_data.get("user_profile_image", instance.user_profile_image)
        instance.save()
        return instance