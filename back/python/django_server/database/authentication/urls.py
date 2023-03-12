from django.urls import path, include
from . import views
from .views import UserSignupView, UserLoginView, UserInfoView

urlpatterns = [
    path('signup/', UserSignupView.as_view(), name="user_signup_view"),
    path('login/', UserLoginView.as_view(), name="user_login_view"),
    path('info/', UserInfoView.as_view(), name="user_info_view")
]