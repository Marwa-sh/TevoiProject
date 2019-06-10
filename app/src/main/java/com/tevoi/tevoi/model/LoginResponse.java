package com.tevoi.tevoi.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse  extends IResponse{

    @SerializedName("Token")
    private String Token;
    @SerializedName("UserId")
    private int UserId;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getToken(){
        return Token;
    }
    public void setToken(String token){
        this.Token = token;
    }
}
