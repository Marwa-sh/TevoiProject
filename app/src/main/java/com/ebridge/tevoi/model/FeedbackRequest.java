package com.ebridge.tevoi.model;

import com.google.gson.annotations.SerializedName;

public class FeedbackRequest
{
    @SerializedName("Name")
    private String Name ;
    @SerializedName("Email")
    private String Email ;
    @SerializedName("Message")
    private String Message;

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getMessage() {
        return Message;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
