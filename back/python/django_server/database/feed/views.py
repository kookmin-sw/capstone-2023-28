from django.shortcuts import render
from rest_framework.views import APIView
from .models import Feed
from .serializers import *
from rest_framework.filters import SearchFilter
from rest_framework.response import Response
from rest_framework import permissions, status, generics
from API.s3 import S3ImageUploader
# Create your views here.
class FeedView(generics.ListAPIView):
    serializer_class = FeedSerializer
    filter_backends = [SearchFilter]
    search_fields = ['user_id__exact', 'feedhashtag__hash_tag__exact']
    def list(self, request, *args, **kwargs):
        queryset = self.get_queryset()
        serializer = self.get_serializer(queryset, many=True)
        data = {}
        data["status"] = "OK"
        data["res"] = serializer.data
        return Response(data, status=status.HTTP_200_OK)
    def get_queryset(self):
        queryset = Feed.objects.all()
        user_id = self.request.query_params.get('user_id', None)
        hash_tags = self.request.query_params.get('hash_tags', None)
        if user_id is not None:
            queryset = queryset.filter(user_id=user_id)
        if hash_tags is not None:
            hash_tag_list = hash_tags.split(',')
            for hash_tag in hash_tag_list:
                hash_tag_feeds = FeedHashTag.objects.filter(hash_tag=hash_tag)
                queryset = queryset.filter(feed_id__in=map(lambda x: x.feed_id_id, hash_tag_feeds))
        return queryset.order_by("-created_at")
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
    def delete(self, request):
        data = {}
        try:
            feed = Feed.objects.get(feed_id=request.data["feed_id"])
        except Feed.DoesNotExist:
            data["status"] = "ERROR"
            data["res"] = {"error_name": "존재하지 않는 필드", "error_id": 1}
            return Response(data, status=status.HTTP_400_BAD_REQUEST)
        else:
            feed.delete()
            data["status"] = "OK"
            data["res"] = {}
            return Response(data, status=status.HTTP_200_OK)


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

