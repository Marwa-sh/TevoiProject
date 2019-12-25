package com.tevoi.tevoi.model;

import com.google.gson.annotations.SerializedName;

public class ResetPasswordRequest {

    @SerializedName("Password")
    public String Password;

    public void setPassword(String password) {
        Password = password;
    }

    public String getPassword() {
        return Password;
    }

    public ResetPasswordRequest(String password) {
        Password = password;
    }

    public  ResetPasswordRequest(){

    }
}
