from django.db import models
from django.contrib.auth.models import AbstractUser
# Create your models here.

class User(AbstractUser):
    user_definition = models.CharField(max_length=100, blank=True)
    user_nickname = models.CharField(max_length=15)
    user_profile_image = model.CharField(max_length=200)
    user_point_number = model.IntegerField(min_length=0)


