from django.shortcuts import render
from rest_framework.views import APIView
from .models import Feed
from .serializers import FeedCreateSerializer, FeedLoadSerializer
from rest_framework.response import Response
from rest_framework import permissions, status
# Create your views here.
class FeedCreateView(APIView):
    def post(self, request):
        serializer = FeedCreateSerializer(data=request.data)
        data = {}
        if serializer.is_valid():
            feed = serializer.save()
            data["feed_id"] = feed.feed_id
            return Response(data)
        else:
            data = serializer.errors
            return Response(data, status=status.HTTP_400_BAD_REQUEST)
class FeedLoadView(APIView):
    def post(self, request):
        serializer = FeedLoadSerializer(data=request.data)
        data = {}
        if serializer.is_valid():
            validated_data = serializer.validated_data
            feed_user_id = validated_data["feed_user_id"]
            num_of_feeds = validated_data["num_of_feeds"]
            tag_names = validated_data["tag_names"]
            feeds = Feed.objects.filter(feed_user_id=feed_user_id).values()
        else:
            data = serializer.errors
            return Response(data, status=status.HTTP_400_BAD_REQUEST)