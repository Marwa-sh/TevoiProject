package com.tevoi.tevoi.rest;



import com.tevoi.tevoi.model.AboutUsResponse;
import com.tevoi.tevoi.model.AddUserListResponse;
import com.tevoi.tevoi.model.CategoryResponseList;
import com.tevoi.tevoi.model.FeedbackRequest;
import com.tevoi.tevoi.model.GetDownloadLimitResponse;
import com.tevoi.tevoi.model.GetPartnerTracksResponse;
import com.tevoi.tevoi.model.GetSubscripedPartnersResponse;
import com.tevoi.tevoi.model.GetUserListTracksResponse;
import com.tevoi.tevoi.model.IResponse;
import com.tevoi.tevoi.model.ListNotificationTypesResponse;
import com.tevoi.tevoi.model.MainSponsoreLogoResponse;
import com.tevoi.tevoi.model.PartnerListResponse;
import com.tevoi.tevoi.model.RatingResponse;
import com.tevoi.tevoi.model.RegisterDataResponse;
import com.tevoi.tevoi.model.RegisterRequest;
import com.tevoi.tevoi.model.RegisterResponse;
import com.tevoi.tevoi.model.ResetPasswordRequest;
import com.tevoi.tevoi.model.TrackCommentResponse;
import com.tevoi.tevoi.model.TrackFilter;
import com.tevoi.tevoi.model.TrackLocationResponse;
import com.tevoi.tevoi.model.TrackObject;
import com.tevoi.tevoi.model.TrackResponseList;
import com.tevoi.tevoi.model.TrackTextResponse;
import com.tevoi.tevoi.model.UserFiltersResponse;
import com.tevoi.tevoi.model.UserListResponse;
import com.tevoi.tevoi.model.UserProfileRequest;
import com.tevoi.tevoi.model.UserProfileResponse;
import com.tevoi.tevoi.model.UserSubscriptionInfoResponse;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

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
    Call<IResponse> AddComment(@Query("TrackId") int TrackId, @Query("CommentText") String CommentText);

    @GET("api/Services/RemoveFromHistory")
    Call<IResponse> RemoveFromHistory(@Query("TrackId") int TrackId);

    @GET("api/Services/RemoveTrackFromFavourite")
    Call<IResponse> RemoveTrackFromFavourite(@Query("TrackId") int TrackId);

    /*@GET("api/Services/AddListenTrackActivity")
    Call<IResponse> AddListenTrackActivity(@Query("TrackId") int TrackId, @Query("numberOfSeconds") int numberOfSeconds);
*/
    @GET("api/Services/AddTrackToFavourite")
    Call<IResponse> AddTrackToFavourite(@Query("TrackId") int TrackId);

    @GET("api/Services/AddTrackToUserList")
    Call<IResponse> AddTrackToUserList(@Query("TrackId") int TrackId, @Query("ListId") int ListId);

    @GET("api/Services/AddTrackToNewUserList")
    Call<IResponse> AddTrackToNewUserList(@Query("TrackId") int TrackId, @Query("ListName") String ListName);


    @GET("api/Services/AddUserList")
    Call<IResponse> AddUserList(@Query("ListName") String ListName);

    @GET("api/Services/AddGetUserList")
    Call<AddUserListResponse> AddGetUserList(@Query("ListName") String ListName);

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

    @GET("api/Services/UpdateShowHeardTracks")
    Call<IResponse> UpdateShowHeardTracks();

    @GET("api/Services/GetCategoriesFilters")
    Call<CategoryResponseList> GetCategoriesFilters();

    @GET("api/Services/GetSubscripedPartners")
    Call<GetSubscripedPartnersResponse> GetSubscripedPartners();

    @GET("api/Services/AddUnitUsageForUser")
    Call<UserSubscriptionInfoResponse> AddUnitUsageForUser(@Query("TrackId") int TrackId, @Query("numberOfUnits") int numberOfUnits, @Query("NumberOfSeconds") int NumberOfSeconds);

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

    @GET("api/Services/UpdateNotificationType")
    Call<IResponse> UpdateNotificationType(@Query("notificationFilterId") int notificationFilterId);

    @GET("api/Services/UpdateAllNotificationType")
    Call<ListNotificationTypesResponse> UpdateAllNotificationType(@Query("allnotificationStatus") boolean allnotificationStatus);

    @GET("api/User/GetRegisterInformation")
    Call<RegisterDataResponse> GetRegisterInformation();

    @POST("api/User/Register")
    Call<RegisterResponse> Register(@Query("RegisterRequest") RegisterRequest model);

    @GET("api/Services/GetAboutUs")
    Call<AboutUsResponse> GetAboutUs();

    @GET("api/Services/GetUserSubscriptionInfo")
    Call<UserSubscriptionInfoResponse> GetUserSubscriptionInfo();

    @GET("api/Services/GetUserFilters")
    Call<UserFiltersResponse> GetUserFilters();

    @GET("api/Services/ClearAndLoadUserFilters")
    Call<UserFiltersResponse> ClearAndLoadUserFilters();

    @GET("api/Services/ClearUserList")
    Call<IResponse> ClearUserList();

    @GET("api/Services/RemoveAllHistory")
    Call<IResponse> RemoveAllHistory();

    //abd
    @GET("api/Services/RemoveAllFavourite")
    Call<IResponse> RemoveAllFavourite();

    @GET("api/Services/ClearUserListTracks")
    Call<IResponse> ClearUserListTracks(@Query("UserListId") int UserListId);
    //abd


    @GET("api/Services/UpdateUIDefaultLanguage")
    Call<IResponse> UpdateUIDefaultLanguage(@Query("LanguageId") int LanguageId);

    @GET("api/Services/UpdateTrackDefaultLanguage")
    Call<IResponse> UpdateTrackDefaultLanguage(@Query("LanguageId") int LanguageId);


    @GET("api/User/RequestNewPassword")
    Call<IResponse> RequestNewPassword(@Query("Email") String Email);


    @Multipart
    @POST("/upload")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file, @Part("name") RequestBody requestBody);


    @Streaming
    @GET("api/Services/GetAudioDownload")
    Call<ResponseBody> downloadFileByUrl(@Query("id") int id);

    @GET("api/Services/AddListenTrackWithQuota")
    Call<UserSubscriptionInfoResponse> AddListenTrackWithQuota(@Query("TrackId") int TrackId);


    @GET("api/Services/UpdateDownloadLimit")
    Call<IResponse> UpdateDownloadLimit(@Query("numberOfMinutes") int numberOfMinutes);

    @GET("api/Services/GetDownloadLimit")
    Call<GetDownloadLimitResponse> GetDownloadLimit();

    @GET("api/Services/GetUserProfile")
    Call<UserProfileResponse> GetUserProfile();

    @POST("api/Services/UpdateUserProfile")
    Call<IResponse> UpdateUserProfile(@Body UserProfileRequest model);


}
