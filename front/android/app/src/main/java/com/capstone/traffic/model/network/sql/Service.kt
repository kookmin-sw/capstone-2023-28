package com.capstone.traffic.model.network.sql

import com.capstone.traffic.model.network.sql.dataclass.DefaultRes
import com.capstone.traffic.model.network.sql.dataclass.getfeed.FeedResSuc
import com.capstone.traffic.model.network.sql.dataclass.ImageUpload
import com.capstone.traffic.model.network.sql.dataclass.comment.ComResSuc
import com.capstone.traffic.model.network.sql.dataclass.info.InfoRecSuc
import com.capstone.traffic.model.network.sql.dataclass.login.LoginResSuc
import com.capstone.traffic.model.network.sql.dataclass.postfeed.response
import com.capstone.traffic.model.network.sql.dataclass.sign.SignResSuc
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Service {
    @POST("user/signup/")
    fun getSign(@Body param: RequestBody) : Call<SignResSuc>

    @POST("user/token/")
    fun getLogin(@Body param: RequestBody) : Call<LoginResSuc>

    @GET("user/info/")
    fun getInfo(@Query("user_email", encoded = true) userEmail : String?, @Query("user_nickname", encoded = true) userNickname : String?, @Query("page_num", encoded = true) pageNum : String?) : Call<InfoRecSuc>

    // 텍스트 업로드
    @POST("feed/")
    fun getPostingText(@Body param: RequestBody) : Call<response>
    
    // 피드 전체 불러오기
    @GET("feed/")
    fun getFeed(@Query("hash_tags", encoded = true) hashTag : String?, @Query("user_id", encoded = true) userId : String? , @Query("user_nickname", encoded = true) userNickname : String?  , @Query("page_num", encoded = true) pageNum : String? , @Query("page_index", encoded = true) pageIndex : String? ) : Call<FeedResSuc>

    // 피드 필터링 (해시 태그) 해서 가져오기
    @GET("feed/")
    fun getFeedByHashTag(@Query("hash_tags", encoded = true) hashTag : String) : Call<FeedResSuc>


    // 유저 정보를 통해 피드 가져오기
    @GET("feed/")
    fun getFeedByUserId(@Query("user_id", encoded = true) userId : String) : Call<FeedResSuc>

    // 프로필 이미지 업데이트
    @Multipart
    @POST("user/update/profile/")
    fun updateProfile(@Part image : MultipartBody.Part) : Call<DefaultRes>

    // 이미지 업로드
    @Multipart
    @POST("feed/image/")
    fun uploadImage(@PartMap feed_id : HashMap<String, RequestBody>, @Part image : MultipartBody.Part) : Call<ImageUpload>


    // 댓글 달기
    @POST("feed/comment/")
    fun uploadComments(@Body param : RequestBody) : Call<ImageUpload>

    // 댓글 가져오기
    @GET("feed/comment/")
    fun getComments(@Query("feed_id", encoded = true) feedId : String) : Call<ComResSuc>

    // 유저 정보 수정
    @POST("user/update/")
    fun updateProfile(@Body param : RequestBody) : Call<DefaultRes>

    // 좋아요 기능
    @POST("feed/like/")
    fun updateLike(@Body param : RequestBody) : Call<DefaultRes>

    // 좋아요 취소 기능
    @HTTP(method = "DELETE", path = "feed/like/", hasBody = true)
    fun updateCancleLike(@Body param: RequestBody) : Call<DefaultRes>

    // 팔로잉 기능
    @POST("user/follow/")
    fun makeFollowing(@Body param: RequestBody) : Call<DefaultRes>

    // 언팔
    @HTTP(method = "DELETE", path = "user/follow/", hasBody = true)
    fun makeUnfollowing(@Body param: RequestBody) : Call<DefaultRes>

    // 피드 삭제
    @HTTP(method = "DELETE", path = "feed/", hasBody = true)
    fun deleteFeed(@Body param: RequestBody) : Call<DefaultRes>
}