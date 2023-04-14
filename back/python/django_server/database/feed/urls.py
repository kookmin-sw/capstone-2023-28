from django.urls import path, include
from . import views

urlpatterns = [
    path("create/", views.FeedCreateView.as_view(), name="feed_create_view"),
    path("comment/create/", views.CommentCreateView.as_view(), name="comment_create_view")
]