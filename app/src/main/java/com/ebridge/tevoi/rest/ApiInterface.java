package com.ebridge.tevoi.rest;



import com.ebridge.tevoi.adapter.Track;
import com.ebridge.tevoi.model.AddCommentResponse;
import com.ebridge.tevoi.model.AddCommetRequest;
import com.ebridge.tevoi.model.AddTrackToFavouriteResponse;
import com.ebridge.tevoi.model.GeneralResponse;
import com.ebridge.tevoi.model.GetTrackFavouriteResponse;
import com.ebridge.tevoi.model.IResponse;
import com.ebridge.tevoi.model.LoginRequest;
import com.ebridge.tevoi.model.LoginResponse;
import com.ebridge.tevoi.model.PartnerListResponse;
import com.ebridge.tevoi.model.TokenRequest;
import com.ebridge.tevoi.model.TokenResponse;
import com.ebridge.tevoi.model.TrackCommentRequest;
import com.ebridge.tevoi.model.TrackCommentResponse;
import com.ebridge.tevoi.model.TrackLocationResponse;
import com.ebridge.tevoi.model.TrackObject;
import com.ebridge.tevoi.model.TrackResponse;
import com.ebridge.tevoi.model.TrackResponseList;
import com.ebridge.tevoi.model.TrackTextResponse;
import com.ebridge.tevoi.model.TransportRequest;
import com.ebridge.tevoi.model.UserListResponse;


import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @Headers({"Content-Type:application/json","Authorization:TevoiTokenSample"})
    @GET("api/Services/ListMainTrack")
    Call<TrackResponseList> getListMainTrack(@Query("ListTypeEnum") int ListTypeEnum,@Query("index") int index,@Query("size") int size);

    @Headers({"Content-Type:application/json","Authorization:TevoiTokenSample"})
    @GET("api/Services/GetHistoryList")
    Call<TrackResponseList> getHistoryList(@Query("index") int index,@Query("size") int size);

    @Headers({"Content-Type:application/json","Authorization:TevoiTokenSample"})
    @GET("api/Services/GetFavouriteList")
    Call<TrackResponseList> getFavouriteList(@Query("index") int index,@Query("size") int size);

    @Headers({"Content-Type:application/json","Authorization:TevoiTokenSample"})
    @GET("api/Services/GetUserLists")
    Call<UserListResponse> getUserLists(@Query("index") int index, @Query("size") int size);

    @GET("api/Services/GetStreamAudio")
    Call<ResponseBody> GetStreamAudio(@Query("id") int id);

    @GET("api/Services/GetTrackFullDetails")
    Call<TrackObject> getTrackFullDetails(@Query("TrackId") int TrackId);

    @GET("api/Services/GetTrackComments")
    Call<TrackCommentResponse> GetTrackComments(@Query("TrackId") int TrackId);

    @GET("api/Services/GetTrackLocation")
    Call<TrackLocationResponse> GetTrackLocation(@Query("TrackId") int TrackId);

    @GET("api/Services/GetTrackText")
    Call<TrackTextResponse> GetTrackText(@Query("TrackId") int TrackId);


    @FormUrlEncoded()
    @POST("api/Services/AddComment")
    Call<AddCommentResponse> AddComment(@Field("TrackId") int TrackId, @Field("CommentText") String CommentText);

    @GET("api/Services/RemoveFromHistory")
    Call<IResponse> RemoveFromHistory(@Query("ActivityId") int ActivityId);

    @GET("api/Services/RemoveTrackFromFavourite")
    Call<IResponse> RemoveTrackFromFavourite(@Query("ActivityId") int ActivityId);

    @GET("api/Services/AddListenTrackActivity")
    Call<IResponse> AddListenTrackActivity(@Query("TrackId") int TrackId);


    @GET("api/Services/AddTrackToFavourite")
    Call<IResponse> AddTrackToFavourite(@Query("TrackId") int TrackId);

    @POST("api/Services/GetTrackFavouritionState")
    Call<GetTrackFavouriteResponse> GetTrackFavouritionState(@Query("TrackId") int TrackId, @Query("UserId") int UserId);

    @GET("api/Services/AddTrackToUserList")
    Call<IResponse> AddTrackToUserList(@Query("TrackId") int TrackId, @Query("ListId") int ListId);


    @GET("api/Services/AddUserList")
    Call<IResponse> AddUserList(@Query("ListName") String ListName);


    @GET("api/Services/EditUserList")
    Call<IResponse> EditUserList(@Query("ListId") int ListId, @Query("NewListName") String NewListName);


    @GET("api/Services/DeleteUserList")
    Call<IResponse> DeleteUserList(@Query("ListId") int ListId);


    @Headers({"Content-Type:application/json","Authorization:TevoiTokenSample"})
    @GET("api/Services/GetPartnersList")
    Call<PartnerListResponse> GetPartnersList(@Query("TypeOfOrder") int TypeOfOrder, @Query("index") int index, @Query("size") int size);


}
