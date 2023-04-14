from django.shortcuts import render
from rest_framework.views import APIView
from .models import Feed
from .serializers import *
from rest_framework.response import Response
from rest_framework import permissions, status
# Create your views here.
class FeedCreateView(APIView):
    def post(self, request):
        payload = request.auth.payload
        serializer = FeedSerializer(data=request.data, context={"user_id":payload["user_id"]})
        data = {}
        if serializer.is_valid():
            serializer.save()
            data["status"] = "OK"
            data["res"] = {}
            return Response(data)
        else:
            data["status"] = "ERROR"
            data["res"] = serializer.errors
            return Response(data, status=status.HTTP_400_BAD_REQUEST)
