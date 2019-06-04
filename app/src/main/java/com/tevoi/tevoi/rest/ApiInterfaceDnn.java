package com.tevoi.tevoi.rest;


import com.tevoi.tevoi.model.LoginResponse;
import com.tevoi.tevoi.model.RegisterRequest;
import com.tevoi.tevoi.model.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterfaceDnn {


    @GET("API/Services/Login")
    Call<LoginResponse> Login(@Query("UserName") String UserName, @Query("Password") String Password);

    @POST("API/Services/Register")
    Call<RegisterResponse> Register(@Body RegisterRequest model);

}
