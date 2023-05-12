from django.db import models
from django.contrib.auth.models import AbstractBaseUser, BaseUserManager
from django.contrib.auth.hashers import make_password
class UserManager(BaseUserManager):
    def create_user(self, password, **extra_fields):
        if password is None:
            raise TypeError("User must have a password")

        extra_fields.setdefault("is_admin", False)
        extra_fields.setdefault("is_staff", False)
        user = self.model(
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
    user_email = models.CharField(max_length=30, unique=True)
    user_nickname = models.CharField(max_length=10, unique=True)
    user_definition = models.CharField(max_length=100, null=True, blank=True)
    user_profile_image = models.CharField(max_length=200, null=True)
    user_point_number = models.IntegerField(default=0, null=True)
    is_seller = models.BooleanField(default=False)

    is_admin = models.BooleanField(default=False)
    is_staff = models.BooleanField(default=False)
    USERNAME_FIELD = "user_email"
    REQUIRED_FIELD = ["user_email"]

    objects = UserManager()

    def save(self, *args, **kwargs):
        if self.password:
            self.password = make_password(self.password)
        super().save(*args, **kwargs)
    def save_without_password(self, *args, **kwargs):
        super().save(*args, **kwargs)

# follow unfollow
class Follow(models.Model):
    following_user_id = models.ForeignKey('User', on_delete=models.CASCADE, related_name="user_following")
    follow_user_id = models.ForeignKey('User', on_delete=models.CASCADE, related_name="user_follower")

    class Meta:
        db_table = "follow"

    def __str__(self):
        return 'User ID ' + self.follow_user_id + ' is following ' + str(self.following_user_id)