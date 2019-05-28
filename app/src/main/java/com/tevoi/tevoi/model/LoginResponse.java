package com.tevoi.tevoi.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse  extends IResponse{

    @SerializedName("Token")
    private String Token;

    public String getToken(){
        return Token;
    }
    public void setToken(String token){
        this.Token = token;
    }
}
