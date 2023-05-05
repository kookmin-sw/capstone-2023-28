from django.urls import path
from . import views

urlpatterns = [
    path("", views.FeedView.as_view(), name="feed_view"),
    path("comment/", views.CommentView.as_view(), name="feed_comment_view"),
    path("image/", views.FeedImageView.as_view(), name="feed_image_view"),
    path("like/", views.LikeView.as_view(), name="feed_like_view"),
]