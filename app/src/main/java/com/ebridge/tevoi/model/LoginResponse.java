package com.ebridge.tevoi.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
public class LoginResponse  extends IResponse{

    @SerializedName("Token")
    private String message;

    public String getMessage(){
        return message;
    }
    public void setMessage(String message){
        this.message = message;
    }
}
