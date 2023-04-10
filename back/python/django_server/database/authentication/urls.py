from django.urls import path
from .views import *

from rest_framework_simplejwt.views import TokenObtainPairView, TokenRefreshView
from rest_framework_simplejwt.views import TokenVerifyView

urlpatterns = [
    path('signup/', UserSignupView.as_view(), name="user_signup_view"),
    #path('delete/', )
    path('update/', UserUpdateView.as_view(), name="user_update_view"),
    path('update/profile', UserUploadImageView.as_view(), name="user_update_profile_view"),
    path('info/', UserInfoView.as_view(), name="user_info_view"),
    path('token/', CustomTokenObtainPairView.as_view(), name='token'),
    path('token/refresh/', TokenRefreshView.as_view(), name='token_refresh'),
    path('token/verify/', TokenVerifyView.as_view(), name='token_verify')
]