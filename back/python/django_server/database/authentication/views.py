from rest_framework import status
from rest_framework.response import Response
from rest_framework.views import APIView
from rest_framework import generics
from rest_framework_simplejwt.views import TokenObtainPairView
from .serializers import *
from .models import User
from rest_framework.permissions import AllowAny
from API.s3 import S3ImageUploader
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
            data["res"] = serializer.data
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
class UserInfoView(generics.ListAPIView):
    permission_classes = [AllowAny]
    serializer_class = UserSerializer
    def list(self, request, *args, **kwargs):
        queryset = self.get_queryset()
        serializer = self.get_serializer(queryset, many=True)
        data = {}
        data["status"] = "OK"
        data["res"] = serializer.data
        return Response(data, status=status.HTTP_200_OK)
    def get_queryset(self):
        user_nickname = self.request.query_params.get('user_nickname', None)
        user_email = self.request.query_params.get('user_email', None)
        page_index = int(self.request.query_params.get('page_index', 0))
        page_num = int(self.request.query_params.get('page_num', 4))

        offset = page_num * page_index
        limit = offset + page_num
        if user_email is not None:
            queryset = User.objects.filter(user_email__startswith=user_email)[offset:limit]
            return queryset
        if user_nickname is not None:
            queryset = User.objects.filter(user_nickname__startswith=user_nickname)[offset:limit]
            return queryset
        return User.objects.none()

class UserUploadImageView(APIView):
    def post(self, request):
        data = {}
        user_email = request.auth.payload["user_email"]
        image = request.FILES["image"]
        user = User.objects.get(user_email=user_email)

        imageUploader = S3ImageUploader(image)
        image_name = imageUploader.get_image_name()
        serializer = UserProfileUploadSerializer(user,{"user_email":user_email, "user_profile_image":image_name})
        if serializer.is_valid():
            # 만약 이미 프로필 이미지가 있을 시 삭제
            if user.user_profile_image is not None:
                imageUploader.delete(user.user_profile_image)
            imageUploader.upload()
            serializer.save()
            data["status"] = "OK"
            data["res"] = {}
            return Response(data, status=status.HTTP_200_OK)
        else:
            data = {"status": "ERROR",
                    "res": {"error_name": "요청 에러", "error_id": 1}
                    }
            return Response(data, status=status.HTTP_400_BAD_REQUEST)



class CustomTokenObtainPairView(TokenObtainPairView):
    serializer_class = CustomTokenObtainPairSerializer
    def post(self, request, *args, **kwargs):
        response = super().post(request, *args, **kwargs)
        return Response({
            "status": "OK",
            "res": response.data
        })