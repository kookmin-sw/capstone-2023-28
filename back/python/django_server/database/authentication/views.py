import uuid

from rest_framework import status
from rest_framework.response import Response
from rest_framework.views import APIView
from .serializers import *
from .models import User
from rest_framework.permissions import AllowAny
from database import settings
from django.contrib.auth.hashers import check_password
import boto3
class UserSignupView(APIView):
    permission_classes = [AllowAny]
    def post(self, request):
        serializer = UserSerializer(data=request.data, partial=True)
        data = {}
        if serializer.is_valid():
            serializer.save()
        else:
            data = {"errors": serializer.errors}
            return Response(data, status=status.HTTP_400_BAD_REQUEST)
        return Response(data)
class UserUpdateView(APIView):
    def post(self, request):
        data = {}
        try:
            user = User.objects.get(user_email=request.data["user_email"])
        except User.DoesNotExist:
            data["error"] = "User is not exist"
            return Response(data, status=status.HTTP_400_BAD_REQUEST)
        else:
            password = request.data["password"]
            if user.check_password(password):
                del request.data["password"]
                serializer = UserSerializer(user, data=request.data, partial=True)
                if serializer.is_valid():
                    updated_user = serializer.save()
                    return Response(data)
                else:
                    data["error"] = serializer.errors
                    return Response(data, status=status.HTTP_400_BAD_REQUEST)

            else:
                data["error_name"] = "비밀번호 불일치"
                data["error_id"] = 3
                return Response(data, status=status.HTTP_400_BAD_REQUEST)


class UserInfoView(APIView):
    # Token 으로 유저의 정보를 탐색
    def get(self, request):
        user_nickname = request.data["user_nickname"]
        user_info = User.objects.filter(user_nickname=user_nickname).values().first()
        return Response(user_info)
class UserUploadImageView(APIView):
    def post(self, request):
        image = request.FILES["image"]
        user_nickname = request.data["user_nickname"]
        data = {}
        if User.objects.filter(user_nickname=user_nickname):
            user = User.objects.get(user_nickname=user_nickname)
            url = S3ImageUploader(image).upload()
            serializer = UserProfileUploadSerializer(user,{"user_nickname":user_nickname, "user_profile_image":url})
            if serializer.is_valid():
                serializer.save()
                data["url"] = url
                return Response(data)
            else:
                data["error"] = "Wrong Request"
                return Response(data, status=status.HTTP_400_BAD_REQUEST)
        else:
            data["error"] = "User is not exist"
            return Response(data, status=status.HTTP_400_BAD_REQUEST)


class S3ImageUploader:
    def __init__(self, file):
        self.file = file

    def upload(self):
        s3_client = boto3.client(
            's3',
            aws_access_key_id=settings.AWS_ACCESS_KEY_ID,
            aws_secret_access_key=settings.AWS_SECRET_ACCESS_KEY,
            region_name=settings.AWS_S3_REGION_NAME
        )
        i = str(uuid.uuid4())
        response = s3_client.upload_fileobj(self.file, settings.AWS_STORAGE_BUCKET_NAME, i)
        return f'https://{settings.AWS_STORAGE_BUCKET_NAME}.s3.{settings.AWS_S3_REGION_NAME}.amazonaws.com/{i}'

