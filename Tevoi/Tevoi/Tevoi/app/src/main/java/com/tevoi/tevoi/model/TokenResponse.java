package com.tevoi.tevoi.model;


import com.google.gson.annotations.SerializedName;

public class TokenResponse {
    @SerializedName("access_token")
    private String access_token;
    @SerializedName("token_type")
    private String token_type;
    @SerializedName("userName")
    private String userName;

    public String getAccess_token(){
        return access_token;
    }
    public void setAccess_token(String access_token){
        this.access_token = access_token;
    }

    public String getToken_type(){
        return token_type;
    }
    public void setToken_type(String message){
        this.token_type = token_type;
    }

    public String getUserName(){
        return userName;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
}
