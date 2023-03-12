from django.urls import path, include
from . import views
from .views import UserSignupView, UserLoginView

urlpatterns = [
    path('signup/', UserSignupView.as_view(), name="user_signup_view"),
    path('login/', UserLoginView.as_view(), name="user_login_view")
]