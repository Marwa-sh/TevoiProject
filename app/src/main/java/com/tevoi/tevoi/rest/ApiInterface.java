package com.tevoi.tevoi.rest;



import com.tevoi.tevoi.model.AddCommentResponse;
import com.tevoi.tevoi.model.CategoryResponseList;
import com.tevoi.tevoi.model.FeedbackRequest;
import com.tevoi.tevoi.model.GetPartnerTracksResponse;
import com.tevoi.tevoi.model.GetSubscripedPartnersResponse;
import com.tevoi.tevoi.model.GetTrackFavouriteResponse;
import com.tevoi.tevoi.model.GetUserListTracksResponse;
import com.tevoi.tevoi.model.IResponse;
import com.tevoi.tevoi.model.ListNotificationTypesResponse;
import com.tevoi.tevoi.model.LoginRequest;
import com.tevoi.tevoi.model.LoginResponse;
import com.tevoi.tevoi.model.MainSponsoreLogoResponse;
import com.tevoi.tevoi.model.PartnerListResponse;
import com.tevoi.tevoi.model.RatingResponse;
import com.tevoi.tevoi.model.RegisterDataResponse;
import com.tevoi.tevoi.model.RegisterRequest;
import com.tevoi.tevoi.model.RegisterResponse;
import com.tevoi.tevoi.model.TrackCommentResponse;
import com.tevoi.tevoi.model.TrackFilter;
import com.tevoi.tevoi.model.TrackLocationResponse;
import com.tevoi.tevoi.model.TrackObject;
import com.tevoi.tevoi.model.TrackResponseList;
import com.tevoi.tevoi.model.TrackTextResponse;
import com.tevoi.tevoi.model.UserListResponse;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    /*@Headers({"Content-Type:application/json","Authorization:TevoiTokenSample", "LicenseTevoiMobileApp:"})*/
    @GET("api/Services/GetMainSponsoreLogo")
    Call<MainSponsoreLogoResponse> GetMainSponsoreLogo();


    @POST("api/Services/ListMainTrack")
    Call<TrackResponseList> getListMainTrack(@Body TrackFilter model);

    @POST("api/Services/ListMainTrackWithFilter")
    Call<TrackResponseList> ListMainTrackWithFilter(@Body TrackFilter model);

    @GET("api/Services/GetHistoryList")
    Call<TrackResponseList> getHistoryList(@Query("index") int index,@Query("size") int size);

    @GET("api/Services/GetFavouriteList")
    Call<TrackResponseList> getFavouriteList(@Query("index") int index,@Query("size") int size);

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

    @GET("api/Services/AddComment")
    Call<AddCommentResponse> AddComment(@Query("TrackId") int TrackId, @Query("CommentText") String CommentText);

    @GET("api/Services/RemoveFromHistory")
    Call<IResponse> RemoveFromHistory(@Query("ActivityId") int ActivityId);

    @GET("api/Services/RemoveTrackFromFavourite")
    Call<IResponse> RemoveTrackFromFavourite(@Query("ActivityId") int ActivityId);

    /*@GET("api/Services/AddListenTrackActivity")
    Call<IResponse> AddListenTrackActivity(@Query("TrackId") int TrackId, @Query("numberOfSeconds") int numberOfSeconds);
*/
    @GET("api/Services/AddTrackToFavourite")
    Call<IResponse> AddTrackToFavourite(@Query("TrackId") int TrackId);

    @GET("api/Services/AddTrackToUserList")
    Call<IResponse> AddTrackToUserList(@Query("TrackId") int TrackId, @Query("ListId") int ListId);

    @GET("api/Services/AddUserList")
    Call<IResponse> AddUserList(@Query("ListName") String ListName);

    @GET("api/Services/EditUserList")
    Call<IResponse> EditUserList(@Query("ListId") int ListId, @Query("NewListName") String NewListName);

    @GET("api/Services/DeleteUserList")
    Call<IResponse> DeleteUserList(@Query("ListId") int ListId);

    @GET("api/Services/GetPartnersList")
    Call<PartnerListResponse> GetPartnersList(@Query("TypeOfOrder") int TypeOfOrder, @Query("index") int index, @Query("size") int size);

    @GET("api/Services/AddFollowshipToPartner")
    Call<IResponse> AddFollowshipToPartner(@Query("PartnerId") int PartnerId);

    @GET("api/Services/UpdateFollowshipToPartner")
    Call<IResponse> UpdateFollowshipToPartner(@Query("FollowshipId") int FollowshipId);

    @GET("api/Services/UpdateCategoryPreference")
    Call<IResponse> UpdateCategoryPreference(@Query("CategoryId") int CategoryId);

    @GET("api/Services/GetCategoriesFilters")
    Call<CategoryResponseList> GetCategoriesFilters();

    @GET("api/Services/GetSubscripedPartners")
    Call<GetSubscripedPartnersResponse> GetSubscripedPartners();

    @GET("api/Services/AddUnitUsageForUser")
    Call<IResponse> AddUnitUsageForUser(@Query("TrackId") int TrackId,@Query("NumberOfUnits") int NumberOfSeconds);

    @GET("api/Services/GetPartnerTracks")
    Call<GetPartnerTracksResponse> GetPartnerTracks(@Query("PartnerId") int PartnerId,@Query("ListTypeEnum") int ListTypeEnum, @Query("index") int index, @Query("size") int size);

    @GET("api/Services/GetTracksForUserList")
    Call<GetUserListTracksResponse> GetTracksForUserList(@Query("ListId") int ListId, @Query("index") int index, @Query("size") int size);

    @GET("api/Services/DeleteTrackFromUserList")
    Call<IResponse> DeleteTrackFromUserList(@Query("ListId") int ListId, @Query("TrackId") int TrackId);

    @POST("api/Services/SendFeedback")
    Call<IResponse> SendFeedback(@Body FeedbackRequest request);

    @GET("api/Services/GetTrackRating")
    Call<RatingResponse> GetTrackRating(@Query("TrackId") int TrackId);

    @GET("api/Services/SetTrackRating")
    Call<IResponse> SetTrackRating(@Query("TrackId") int TrackId, @Query("Rating") int Rating);

    @GET("api/Services/GetNotificationTypesList")
    Call<ListNotificationTypesResponse> GetNotificationTypesList();

    @GET("api/User/GetRegisterInformation")
    Call<RegisterDataResponse> GetRegisterInformation();

    @POST("api/User/Register")
    Call<RegisterResponse> Register(@Query("RegisterRequest") RegisterRequest model);

}
