from django.urls import path, include
from . import views
from .views import UserSignupView, UserInfoView

from rest_framework_simplejwt.views import TokenObtainPairView, TokenRefreshView
from rest_framework_simplejwt.views import TokenVerifyView

urlpatterns = [
    path('signup/', UserSignupView.as_view(), name="user_signup_view"),
    path('info/', UserInfoView.as_view(), name="user_info_view"),
    path('token/', TokenObtainPairView.as_view(), name='token_refresh'),
    path('token/refresh/', TokenRefreshView.as_view(), name='token_refresh'),
    path('token/verify/', TokenVerifyView.as_view(), name='token_verify')
]