from django.db import models


class User(models.Model):
    user_id = models.IntegerField()
    user_nickname = models.CharField(max_length=45)
    user_definition = models.CharField(max_length=45, blank=True, null=True)
    user_profile_image = models.CharField(max_length=45)
    user_point_number = models.CharField(max_length=45)
    is_seller = models.BigIntegerField()

    class Meta:
        db_table = "user"
    
    def __str__(self):
        return self.user_nickname
    
class Follow(models.Model):
    following_user_id = models.ForeignKey('User', on_delete=models.CASCADE)
    follow_user_id = models.CharField(max_length=45)

    class Meta:
        db_table = "follow"

    def __str__(self):
        return 'User ID ' + self.follow_user_id + ' is following ' + str(self.following_user_id)