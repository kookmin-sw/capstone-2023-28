from django.urls import path, include
from . import views

urlpatterns = [
    path("", views.FeedView.as_view(), name="feed_view"),
    path("comment/", views.CommentCreateView.as_view(), name="feed_comment_view"),
    path("image/", views.FeedImageView.as_view(), name="feed_image_view"),
]