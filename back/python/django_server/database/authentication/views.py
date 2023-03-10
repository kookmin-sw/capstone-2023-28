from django.shortcuts import render

from .serializers import UserSerializer
from rest_framework import generics
from .models import User

class UserCreate(generics.CreateAPIView):
    queryset = User.objects.all()
    serializer_class = UserSerializer
# Create your views here.
