package com.tevoi.tevoi.model;



import com.google.gson.annotations.SerializedName;

public class TransportRequest {
    @SerializedName("IsOneWay")
    private String IsOneWay;
    @SerializedName("Comments")
    private String Comments;
    @SerializedName("WithWheelChair")
    private String WithWheelChair;
    @SerializedName("WhenDate")
    private String WhenDate;
    @SerializedName("Destination")
    private String Destination;

    public String getIsOneWay ()
    {
        return IsOneWay;
    }

    public void setIsOneWay (String IsOneWay)
    {
        this.IsOneWay = IsOneWay;
    }

    public String getComments ()
    {
        return Comments;
    }

    public void setComments (String Comments)
    {
        this.Comments = Comments;
    }

    public String getWithWheelChair ()
    {
        return WithWheelChair;
    }

    public void setWithWheelChair (String WithWheelChair)
    {
        this.WithWheelChair = WithWheelChair;
    }

    public String getWhenDate ()
    {
        return WhenDate;
    }

    public void setWhenDate (String WhenDate)
    {
        this.WhenDate = WhenDate;
    }

    public String getDestination ()
    {
        return Destination;
    }

    public void setDestination (String Destination)
    {
        this.Destination = Destination;
    }
}
