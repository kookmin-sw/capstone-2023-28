from django.shortcuts import render
from rest_framework.views import APIView
from .models import Feed
from .serializers import FeedSerializer
from rest_framework.response import Response
from rest_framework import permissions, status
# Create your views here.
class FeedCreateView(APIView):
    def post(self, request):
        serializer = FeedSerializer(data=request.data)
        data = {}
        if serializer.is_valid():
            feed = serializer.save()
            data["feed_id"] = feed.feed_id
            return Response(data)
        else:
            data = serializer.errors
            return Response(data, status=status.HTTP_400_BAD_REQUEST)
