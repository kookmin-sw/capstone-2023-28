from django.db import models
from django.contrib.auth.models import AbstractBaseUser, BaseUserManager

class UserManager(BaseUserManager):
    def create_user(self, user_email, password, **extra_fields):
        if password is None:
            raise TypeError("User must have a password")
        user = self.model(
            user_email=user_email,
            **extra_fields
        )
        user.set_password(password)
        user.save(using=self._db)
        return user
    def create_superuser(self, user_email='admin.kookmin.ac.kr', password='', **extra_fields):
        if password is None:
            raise TypeError("SuperUser must have a password")

        extra_fields.setdefault("is_admin",True)
        extra_fields.setdefault("is_staff",True)

        return self.create_user(user_email=user_email, password=password, **extra_fields)



# Create your models here.

class User(AbstractBaseUser):
    user_email = models.CharField(max_length=30)
    user_nickname = models.CharField(max_length=10, unique=True)
    user_definition = models.CharField(max_length=100, null=True)
    user_profile_image = models.CharField(max_length=200, null=True)
    user_point_number = models.IntegerField(default=0, null=True)

    is_admin = models.BooleanField(default=False)
    is_staff = models.BooleanField(default=False)
    USERNAME_FIELD = "user_nickname"
    REQUIRED_FIELD = ['user_nickname']

    objects = UserManager()

