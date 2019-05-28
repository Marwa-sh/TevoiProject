package com.tevoi.tevoi.rest;


import com.tevoi.tevoi.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterfaceDnn {


    @GET("API/Services/Login")
    Call<LoginResponse> Login(@Query("UserName") String UserName, @Query("Password") String Password);


}
