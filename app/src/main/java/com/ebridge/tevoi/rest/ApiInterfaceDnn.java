package com.ebridge.tevoi.rest;


import com.ebridge.tevoi.model.AddCommentResponse;
import com.ebridge.tevoi.model.CategoryResponseList;
import com.ebridge.tevoi.model.FeedbackRequest;
import com.ebridge.tevoi.model.GetPartnerTracksResponse;
import com.ebridge.tevoi.model.GetSubscripedPartnersResponse;
import com.ebridge.tevoi.model.GetTrackFavouriteResponse;
import com.ebridge.tevoi.model.GetUserListTracksResponse;
import com.ebridge.tevoi.model.IResponse;
import com.ebridge.tevoi.model.ListNotificationTypesResponse;
import com.ebridge.tevoi.model.LoginRequest;
import com.ebridge.tevoi.model.LoginResponse;
import com.ebridge.tevoi.model.PartnerListResponse;
import com.ebridge.tevoi.model.RatingResponse;
import com.ebridge.tevoi.model.TrackCommentResponse;
import com.ebridge.tevoi.model.TrackLocationResponse;
import com.ebridge.tevoi.model.TrackObject;
import com.ebridge.tevoi.model.TrackResponseList;
import com.ebridge.tevoi.model.TrackTextResponse;
import com.ebridge.tevoi.model.UserListResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterfaceDnn {


    @GET("API/Services/Login")
    Call<LoginResponse> Login(@Query("UserName") String UserName, @Query("Password") String Password);


}
