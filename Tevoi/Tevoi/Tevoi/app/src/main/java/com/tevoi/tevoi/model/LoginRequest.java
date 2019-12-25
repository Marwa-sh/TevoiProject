package com.tevoi.tevoi.model;


import com.google.gson.annotations.SerializedName;


public class LoginRequest
{
    @SerializedName("UserName")
    private String userName;
    @SerializedName("Password")
    private String password;
    @SerializedName("RememberMe")
    private Boolean rememberMe;

    public LoginRequest(String userName, String password, Boolean remeberMe){
        this.userName = userName;
        this.password = password;
        this.rememberMe= rememberMe;
    }
    public String getEmail() {
        return userName;
    }

    public void setEmail(String email) {
        this.userName = email;
    }

    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }

    public Boolean getRememberMe(){
        return rememberMe;
    }
    public void setRememberMe(Boolean rememberMe){
        this.rememberMe= rememberMe;
    }
}
