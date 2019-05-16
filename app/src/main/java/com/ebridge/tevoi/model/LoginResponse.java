package com.ebridge.tevoi.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
public class LoginResponse {
    @SerializedName("isOk")
    private Boolean isOk;
    @SerializedName("Message")
    private String message;

    public Boolean getStatus(){
        return isOk;
    }
    public void setStatus(Boolean isOk){
        this.isOk = isOk;
    }

    public String getMessage(){
        return message;
    }
    public void setMessage(String message){
        this.message = message;
    }
}
