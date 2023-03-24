from django.urls import path, include
from . import views

urlpatterns = [
    path("create/", views.FeedCreateView.as_view(), name="feed_create_view"),
]