from rest_framework.views import APIView
from .serializers import *
from authentication.models import User
from rest_framework.filters import SearchFilter
from rest_framework.response import Response
from rest_framework import permissions, status, generics
from API.s3 import S3ImageUploader
# Create your views here.
class FeedView(generics.ListAPIView):
    serializer_class = FeedSerializer
    filter_backends = [SearchFilter]
    search_fields = ['user_id__exact', 'feedhashtag__hash_tag__exact']
    def list(self, request, *args, **kwargs):
        payload = request.auth.payload
        queryset = self.get_queryset()
        serializer = self.get_serializer(queryset, many=True,  context={"user_id":payload["user_id"]} )
        data = {}
        data["status"] = "OK"
        data["res"] = serializer.data
        return Response(data, status=status.HTTP_200_OK)
    def get_queryset(self):
        queryset = Feed.objects.all()
        user_id = self.request.query_params.get('user_id', None)
        user_nickname = self.request.query_params.get('user_nickname', None)
        hash_tags = self.request.query_params.get('hash_tags', None)
        if user_id is not None:
            queryset = queryset.filter(user_id=user_id)
        if user_nickname is not None:
            try:
                user = User.objects.get(user_nickname=user_nickname)
            except User.DoesNotExist:
                return []
            queryset = queryset.filter(user_id = user.id)
        if hash_tags is not None:
            hash_tag_list = hash_tags.split(',')
            try:
                for hash_tag in hash_tag_list:
                    hash_tag_feeds = FeedHashTag.objects.filter(hash_tag=hash_tag)
                    queryset = queryset.filter(feed_id__in=map(lambda x: x.feed_id_id, hash_tag_feeds))
            except FeedHashTag.DoesNotExist:
                return []
        page_index = int(self.request.query_params.get('page_index', 0))
        page_num = int(self.request.query_params.get('page_num', 3))

        offset = page_num * page_index
        limit = offset + page_num
        return queryset.order_by("-created_at")[offset:limit]
    def post(self, request):
        payload = request.auth.payload
        serializer = FeedSerializer(data=request.data, context={"user_id":payload["user_id"], "hash_tags":request.data["hash_tags"]})
        data = {}
        if serializer.is_valid():
            serializer.save()
            data["status"] = "OK"
            data["res"] = {"feed_id": serializer.data["feed_id"]}
            return Response(data, status=status.HTTP_200_OK)
        else:
            data["status"] = "ERROR"
            data["res"] = serializer.errors
            return Response(data, status=status.HTTP_400_BAD_REQUEST)
    def delete(self, request):
        data = {}
        try:
            feed = Feed.objects.get(feed_id=request.data["feed_id"])
        except Feed.DoesNotExist:
            data["status"] = "ERROR"
            data["res"] = {"error_name": "존재하지 않는 피드", "error_id": 1}
            return Response(data, status=status.HTTP_400_BAD_REQUEST)
        else:
            feed.delete()
            data["status"] = "OK"
            data["res"] = {}
            return Response(data, status=status.HTTP_200_OK)


class FeedImageView(APIView):
    def post(self, request):
        data = {}
        feed_id = request.data["feed_id"]
        image = request.FILES["image"]
        imageUploader = S3ImageUploader(image)
        image_name = imageUploader.upload()
        serializer = FeedImageSerializer(data={"feed_id":feed_id, "image": image_name})
        if serializer.is_valid():
            serializer.save()
            data["status"] = "OK"
            data["res"] = {}
            return Response(data, status=status.HTTP_200_OK)
        else:
            data["status"] = "ERROR"
            data["res"] = serializer.errors
            return Response(data, status=status.HTTP_400_BAD_REQUEST)

class CommentView(APIView):
    def get(self, request):
        data = {}
        queryset = Comment.objects.filter(feed_id=request.query_params["feed_id"])
        serializer = CommentSerializer(queryset, many=True)
        data["status"] = "OK"
        data["res"] = serializer.data
        return Response(data, status=status.HTTP_200_OK)

    def post(self, request):
        payload = request.auth.payload

        serializer = CommentSerializer(data=request.data, context={"user_id":payload["user_id"]})
        data = {}
        if serializer.is_valid():
            serializer.save()
            data["status"] = "OK"
            data["res"] = {}
            return Response(data, status=status.HTTP_200_OK)
        else:
            data["status"] = "ERROR"
            data["res"] = serializer.errors
            return Response(data, status=status.HTTP_400_BAD_REQUEST)

class LikeView(generics.ListAPIView):
    serializer_class = LikeSerializer
    def list(self, request, *args, **kwargs):
        queryset = self.get_queryset()
        serializer = self.get_serializer(queryset, many=True)
        data = {}
        data["status"] = "OK"
        data["res"] = serializer.data
        return Response(data, status=status.HTTP_200_OK)

    def get_queryset(self):
        user_id = self.request.query_params.get('user_id', None)
        user_nickname = self.request.query_params.get('user_nickname', None)
        feed_id = self.request.query_params.get('feed_id', None)
        queryset = Like.objects.all()
        if user_id is not None:
            queryset = queryset.filter(user_id = user_id)
        if user_nickname is not None:
            queryset = queryset.filter(user_id__user_nickname=user_nickname)
        if feed_id is not None:
            queryset = queryset.filter(feed_id=feed_id)
        return queryset
    def post(self, request):
        payload = request.auth.payload
        serializer = LikeSerializer(data=request.data, context={"user_id" : payload["user_id"]})
        data = {}
        if serializer.is_valid():
            serializer.save()
            data["status"] = "OK"
            data["res"] = {}
            return Response(data, status=status.HTTP_200_OK)
        else:
            data["status"] = "ERROR"
            data["res"] = serializer.errors
            return Response(data, status=status.HTTP_400_BAD_REQUEST)
    def delete(self, request):
        data = {}
        feed_id = request.data.get("feed_id")
        payload = request.auth.payload
        try:
            like = Like.objects.filter(user_id=payload["user_id"], feed_id=feed_id)
        except Like.DoesNotExist:
            data["status"] = "ERROR"
            data["res"] = {"error_name": "존재하지 않는 좋아요", "error_id": 1}
            return Response(data, status=status.HTTP_400_BAD_REQUEST)
        else:
            like.delete()
            data["status"] = "OK"
            data["res"] = {}
            return Response(data, status=status.HTTP_200_OK)
class DislikeView(generics.ListAPIView):
    serializer_class = DislikeSerializer
    def list(self, request, *args, **kwargs):
        queryset = self.get_queryset()
        serializer = self.get_serializer(queryset, many=True)
        data = {}
        data["status"] = "OK"
        data["res"] = serializer.data
        return Response(data, status=status.HTTP_200_OK)

    def get_queryset(self):
        user_id = self.request.query_params.get('user_id', None)
        user_nickname = self.request.query_params.get('user_nickname', None)
        feed_id = self.request.query_params.get('feed_id', None)
        queryset = Dislike.objects.all()
        if user_id is not None:
            queryset = queryset.filter(user_id = user_id)
        if user_nickname is not None:
            queryset = queryset.filter(user_id__user_nickname=user_nickname)
        if feed_id is not None:
            queryset = queryset.filter(feed_id=feed_id)
        return queryset
    def post(self, request):
        payload = request.auth.payload
        serializer = DislikeSerializer(data=request.data, context={"user_id" : payload["user_id"]})
        data = {}
        if serializer.is_valid():
            serializer.save()
            data["status"] = "OK"
            data["res"] = {}
            return Response(data, status=status.HTTP_200_OK)
        else:
            data["status"] = "ERROR"
            data["res"] = serializer.errors
            return Response(data, status=status.HTTP_400_BAD_REQUEST)
    def delete(self, request):
        data = {}
        feed_id = request.data.get("feed_id")
        payload = request.auth.payload
        try:
            dislike = Dislike.objects.filter(user_id=payload["user_id"], feed_id=feed_id)
        except Dislike.DoesNotExist:
            data["status"] = "ERROR"
            data["res"] = {"error_name": "존재하지 않는 싫어요", "error_id": 1}
            return Response(data, status=status.HTTP_400_BAD_REQUEST)
        else:
            dislike.delete()
            data["status"] = "OK"
            data["res"] = {}
            return Response(data, status=status.HTTP_200_OK)