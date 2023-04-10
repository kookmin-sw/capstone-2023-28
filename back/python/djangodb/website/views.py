from django.shortcuts import render
from .models import User, Follow

def home(request):
    all_user = User.objects.all
    all_follow = Follow.objects.all
    return render(request, 'home.html', {'all_user':all_user, 'all_follow':all_follow})
