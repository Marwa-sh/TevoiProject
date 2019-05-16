package com.ebridge.tevoi.model;
import com.google.gson.annotations.SerializedName;

public class GeneralResponse {
    @SerializedName("IsOk")
    private String IsOk;
    @SerializedName("Message")
    private String Message;

    public String getIsOk ()
    {
        return IsOk;
    }

    public void setIsOk (String IsOk)
    {
        this.IsOk = IsOk;
    }

    public String getMessage ()
    {
        return Message;
    }

    public void setMessage (String Message)
    {
        this.Message = Message;
    }
}
