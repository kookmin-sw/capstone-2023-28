from rest_framework.authtoken.models import Token
from rest_framework import permissions, status, viewsets
from rest_framework.decorators import action
from rest_framework.response import Response
from rest_framework.views import APIView
from rest_framework.authentication import TokenAuthentication
from .serializers import UserSerializer
from .models import User
from django.contrib.auth import authenticate

class UserSignupView(APIView):
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

class UserLoginView(APIView):
    queryset = User.objects.all()
    serializer_class = UserSerializer

    def post(self, request, *args, **kwargs):
        user_nickname = request.data["user_nickname"]
        password = request.data["password"]
        user = authenticate(user_nickname=user_nickname, password=password)
        if user:
            token, created = Token.objects.get_or_create(user=user)
            return Response({"token": token.key})
        else:
            return Response(status=status.HTTP_400_BAD_REQUEST)

class UserInfoView(APIView):
    # Token 으로 유저의 정보를 탐색
    def post(self, request):
        token = request.data["token"]
        user_id = Token.objects.filter(key=token).values().first()["user_id"]
        user_info = User.objects.filter(id=user_id).values().first()
        return Response(user_info)
# @api_view(['POST',])
# @permission_classes((permissions.AllowAny, ))
# def user_signup_view(request):
#     serializer = UserSerializer(data=request.data)
#     data = {}
#     if serializer.is_valid():
#         user = serializer.save()
#         data['user_nickname'] = serializer.data['user_nickname']
#         #token = Token.objects.get(user=user).key
#         #data['token'] = token
#     else:
#         data = serializer.errors
#         return Response(data, status=status.HTTP_400_BAD_REQUEST)
#     return Response(data)

# @api_view(['GET',])
# @permission_classes((permissions.AllowAny, ))
# def user_login_view(request):
#     data = {}

# Create your views here.