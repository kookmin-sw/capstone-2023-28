import uuid

from rest_framework import status
from rest_framework.response import Response
from rest_framework.views import APIView
from rest_framework_simplejwt.views import TokenObtainPairView
from .serializers import *
from .models import User
from rest_framework.permissions import AllowAny
from database import settings
import boto3
class UserSignupView(APIView):
    permission_classes = [AllowAny]
    def post(self, request):
        serializer = UserSerializer(data=request.data)
        data = {}
        if serializer.is_valid():
            data["status"] = "OK"
            data["res"] = {}
            serializer.save()
        else:
            data["status"] = "ERROR"
            data["res"] = serializer.errors
            return Response(data, status=status.HTTP_400_BAD_REQUEST)
        return Response(data)
class UserUpdateView(APIView):
    def post(self, request):
        data = {}
        payload = request.auth.payload
        user = User.objects.get(user_email=payload["user_email"])
        serializer = UserSerializer(user, data=request.data, partial=True)
        if serializer.is_valid():
            updated_user = serializer.save()
            data["status"] = "OK"
            data["res"] = {}
            return Response(data)
        else:
            data["status"] = "ERROR"
            data["res"] = serializer.errors
            return Response(data, status=status.HTTP_400_BAD_REQUEST)

class UserDeleteView(APIView):
    def delete(self, request):
        data = {}
        payload = request.auth.payload
        try:
            user = User.objects.get(user_email=payload["user_email"])
        except User.DoesNotExist:
            data = {"status": "ERROR",
                    "res": {"error_name": "이메일 없음", "error_id": 1}
                    }
            return Response(data, status=status.HTTP_400_BAD_REQUEST)
        else:
            user.delete()
            data["status"] = "OK"
            data["res"] = {}
            return Response(data, status=status.HTTP_200_OK)
class UserInfoView(APIView):
    # Token 으로 유저의 정보를 탐색
    def get(self, request):
        try:
            user = User.objects.get(user_email=request.GET["user_email"])
        except User.DoesNotExist:
            data = {"status": "ERROR",
                 "res": {"error_name": "이메일 없음", "error_id": 1}
                 }
            return Response(data, status = status.HTTP_400_BAD_REQUEST)
        else:
            data = {}
            data["status"] = "OK"
            data["res"] = {"user_email":user.user_email, "user_nickname":user.user_nickname, "user_definition":user.user_definition, "user_point_number":user.user_point_number, "user_profile_image": user.user_profile_image}
            return Response(data, status=status.HTTP_200_OK)
class UserUploadImageView(APIView):
    def post(self, request):
        data = {}
        user_email = request.auth.payload["user_email"]
        image = request.FILES["image"]
        user = User.objects.get(user_email=user_email)

        imageUploader = S3ImageUploader(image)
        file_name = imageUploader.get_file_name()
        serializer = UserProfileUploadSerializer(user,{"user_email":user_email, "user_profile_image":file_name})
        if serializer.is_valid():
            # 만약 이미 프로필 이미지가 있을 시 삭제
            if user.user_profile_image is not None:
                imageUploader.delete(user.user_profile_image)
            imageUploader.upload()

            serializer.save()
            data["status"] = "OK"
            data["res"] = {"file_name": file_name}
            return Response(data, status=status.HTTP_200_OK)
        else:
            data = {"status": "ERROR",
                    "res": {"error_name": "요청 에러", "error_id": 1}
                    }
            return Response(data, status=status.HTTP_400_BAD_REQUEST)


class S3ImageUploader:
    def __init__(self, file):
        self.file = file
        self.file_name = str(uuid.uuid4())
        #self.url = f'https://{settings.AWS_STORAGE_BUCKET_NAME}.s3.{settings.AWS_S3_REGION_NAME}.amazonaws.com/{self.file_name}'
        self.s3_client = boto3.client(
            's3',
            aws_access_key_id=settings.AWS_ACCESS_KEY_ID,
            aws_secret_access_key=settings.AWS_SECRET_ACCESS_KEY,
            region_name=settings.AWS_S3_REGION_NAME
        )
    def get_file_name(self):
        return self.file_name
    def upload(self):
        response = self.s3_client.upload_fileobj(self.file, settings.AWS_STORAGE_BUCKET_NAME, self.file_name)
        return self.file_name
    def delete(self, file_name):
        self.s3_client.delete_object(Bucket=settings.AWS_STORAGE_BUCKET_NAME, Key=file_name)
class CustomTokenObtainPairView(TokenObtainPairView):
    serializer_class = CustomTokenObtainPairSerializer
    def post(self, request, *args, **kwargs):
        response = super().post(request, *args, **kwargs)
        return Response({
            "status": "OK",
            "res": response.data
        })