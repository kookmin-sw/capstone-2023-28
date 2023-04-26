from django.shortcuts import render
from rest_framework.views import APIView
from .models import Feed
from .serializers import *
from rest_framework.response import Response
from rest_framework import permissions, status
from API.s3 import S3ImageUploader
# Create your views here.
class FeedView(APIView):
    def get(self, request):
        data = {}
        feeds = Feed.objects.all()
        serializers = FeedSerializer(feeds, many=True)
        data["status"] = "OK"
        data["res"] = serializers.data
        return Response(data, status=status.HTTP_200_OK)
    def post(self, request):
        payload = request.auth.payload
        serializer = FeedSerializer(data=request.data, context={"user_id":payload["user_id"], "hash_tags":request.data["hash_tags"]})
        data = {}
        if serializer.is_valid():
            serializer.save()
            data["status"] = "OK"
            data["res"] = {"feed_id": serializer.data["feed_id"]}
            return Response(data, status=status.HTTP_200_OK)
        else:
            data["status"] = "ERROR"
            data["res"] = serializer.errors
            return Response(data, status=status.HTTP_400_BAD_REQUEST)
class FeedImageView(APIView):
    def post(self, request):
        data = {}
        feed_id = request.data["feed_id"]
        image = request.FILES["image"]
        imageUploader = S3ImageUploader(image)
        image_name = imageUploader.upload()
        serializer = FeedImageSerializer(data={"feed_id":feed_id, "image": image_name})
        if serializer.is_valid():
            serializer.save()
            data["status"] = "OK"
            data["res"] = {}
            return Response(data, status=status.HTTP_200_OK)
        else:
            data["status"] = "ERROR"
            data["res"] = serializer.errors
            return Response(data, status=status.HTTP_400_BAD_REQUEST)

class CommentCreateView(APIView):
    def post(self, request):
        payload = request.auth.payload

        serializer = CommentSerializer(data=request.data, context={"user_id":payload["user_id"]})
        data = {}
        if serializer.is_valid():
            serializer.save()
            data["status"] = "OK"
            data["res"] = {}
            return Response(data, status=status.HTTP_200_OK)
        else:
            data["status"] = "ERROR"
            data["res"] = serializer.errors
            return Response(data, status=status.HTTP_400_BAD_REQUEST)

