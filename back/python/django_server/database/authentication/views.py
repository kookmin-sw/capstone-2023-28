from rest_framework import permissions, status, viewsets
from rest_framework.response import Response
from rest_framework.views import APIView
from .serializers import UserSerializer, UserCheckSerializer
from .models import User
from django.contrib.auth import authenticate
from rest_framework.permissions import AllowAny
from rest_framework.authtoken.models import Token

class UserSignupView(APIView):
    permission_classes = [AllowAny]
    queryset = User.objects.all()
    serializer_class = UserSerializer
    def post(self, request):
        serializer = UserSerializer(data=request.data)
        data = {}
        if serializer.is_valid():
            serializer.save()
            data['user_nickname'] = serializer.data['user_nickname']
        else:
            data = serializer.errors
            return Response(data, status=status.HTTP_400_BAD_REQUEST)
        return Response(data)
class UserUpdateView(APIView):
    def post(self, request):
        data = {}
        if User.objects.filter(user_nickname=request.data["user_nickname"]):
            user = User.objects.get(user_nickname=request.data["user_nickname"])
            serializer = UserCheckSerializer(user, data=request.data)
            if serializer.is_valid():
                user_nickname = serializer.validated_data.get("user_nickname", None)
                password = serializer.validated_data.get("password", None)

                user = User.objects.get(user_nickname=user_nickname)
                if user.check_password(password):
                    updated_user = serializer.save()
                    data["user_nickname"] = serializer.validated_data["user_nickname"]
                    return Response(data)
                else:
                    data["error"] = "Wrong password"
                    return Response(data, status=status.HTTP_400_BAD_REQUEST)
            else:
                data["error"] = "Wrong Request"
                return Response(data, status=status.HTTP_400_BAD_REQUEST)
        else:
            data["error"] = "User is not exist"
            return Response(data, status=status.HTTP_400_BAD_REQUEST)

class UserInfoView(APIView):
    # Token 으로 유저의 정보를 탐색
    def post(self, request):
        user_nickname = request.data["user_nickname"]
        user_info = User.objects.filter(user_nickname=user_nickname).values().first()
        return Response(user_info)
