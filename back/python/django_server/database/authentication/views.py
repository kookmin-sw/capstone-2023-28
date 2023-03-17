from rest_framework import permissions, status, viewsets
from rest_framework.response import Response
from rest_framework.views import APIView
from .serializers import UserSerializer
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
            user = serializer.save()
            data['user_nickname'] = serializer.data['user_nickname']
            # token = Token.objects.get(user=user).key
            # data['token'] = token
        else:
            data = serializer.errors
            return Response(data, status=status.HTTP_400_BAD_REQUEST)
        return Response(data)

# class UserLoginView(APIView):
#     queryset = User.objects.all()
#
#     def post(self, request, *args, **kwargs):
#         serializer = UserSerializer(data=request.data)
#         if serializer.is_valid():
#             user_nickname = serializer.validated_data["user_nickname"]
#             password = serializer.validated_data["password"]
#             user = authenticate(user_nickname=user_nickname, password=password)
#             if user:
#                 token, created = Token.objects.get_or_create(user=user)
#                 return Response({"token": token.key, "user_id": token.user_id})
#             else:
#                 return Response(status=status.HTTP_400_BAD_REQUEST)
#         else:
#             return Response(status=status.HTTP_400_BAD_REQUEST)
class UserInfoView(APIView):
    # Token 으로 유저의 정보를 탐색
    def post(self, request):
        user_nickname = request.data["user_nickname"]
        user_info = User.objects.filter(user_nickname=user_nickname).values().first()
        return Response(user_info)
