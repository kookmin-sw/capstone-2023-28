from rest_framework.authtoken.models import Token
from rest_framework import permissions, status
from rest_framework.decorators import api_view, permission_classes
from rest_framework.response import Response
from .serializers import UserSerializer
@api_view(['POST',])
@permission_classes((permissions.AllowAny, ))
def user_signup_view(request):
    serializer = UserSerializer(data=request.data)
    data = {}
    if serializer.is_valid():
        user = serializer.save()
        data['user_nickname'] = serializer.data['user_nickname']
        #token = Token.objects.get(user=user).key
        #data['token'] = token
    else:
        data = serializer.errors
        return Response(data, status=status.HTTP_400_BAD_REQUEST)
    return Response(data)
# Create your views here.