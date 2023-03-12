from django.urls import path, include
from . import views
from rest_framework import urls
from .views import user_signup_view

urlpatterns = [
    path('signup/', user_signup_view, name="user_signup_view"),
    path('api-auth/', include('rest_framework.urls')),

]