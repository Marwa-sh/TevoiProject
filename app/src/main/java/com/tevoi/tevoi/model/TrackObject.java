package com.tevoi.tevoi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TrackObject
{
    @Expose
    @SerializedName("Id")
    private  int Id;
    @Expose
    @SerializedName("Name")
    private  String Name;
    @Expose
    @SerializedName("Duration")
    private  String Duration;
    @Expose
    @SerializedName("Rate")
    private int Rate;
    @Expose
    @SerializedName("Authors")
    private  String Authors;
    @Expose
    @SerializedName("Categories")
    private String Categories;
    @Expose
    @SerializedName("HasLocation")
    private  boolean HasLocation;
    @Expose
    @SerializedName("HasText")
    private  boolean  HasText;
    @Expose
    @SerializedName("PartnerName")
    private String PartnerName;
    @Expose
    @SerializedName("PartnerId")
    private int PartnerId;
    @Expose
    @SerializedName("PartnerLogo")
    private String PartnerLogo;
    @Expose
    @SerializedName("ActivityId")
    private int ActivityId;
    @Expose
    @SerializedName("IsFavourite")
    private boolean IsFavourite;
    @Expose
    @SerializedName("ListenCount")
    private int ListenCount;
    @Expose
    @SerializedName("CreationDate")
    private String CreationDate;
    @Expose
    @SerializedName("IsListen")
    private boolean IsListen;

    //private int TypeId;
    //private int TypeName;

    public void setId(int id) {
        Id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public void setRate(int rate) {
        Rate = rate;
    }

    public void setActivityId(int activityId) {
        ActivityId = activityId;
    }

    public void setCategories(String categories) {
        Categories = categories;
    }

    public boolean isFavourite() {
        return IsFavourite;
    }

    public void setFavourite(boolean favourite) {
        IsFavourite = favourite;
    }

    public void setHasLocation(boolean hasLocation) {
        HasLocation = hasLocation;
    }

    public void setPartnerLogo(String partnerLogo) {
        PartnerLogo = partnerLogo;
    }

    public void setHasText(boolean hasText) {
        HasText = hasText;
    }

    public void setPartnerName(String partnerName) {
        PartnerName = partnerName;
    }

    public void setPartnerId(int partnerId) {
        PartnerId = partnerId;
    }

    public int getId() {
        return Id;
    }
    public String getAuthors() {
        return Authors;
    }

    public void setAuthors(String authors) {
        Authors = authors;
    }

    public String getName() {
        return Name;
    }

    public String getDuration() {
        return Duration;
    }

    public float getRate() {
        return Rate;
    }

    public String getCategories() {
        return Categories;
    }

    public boolean isHasLocation() {
        return HasLocation;
    }

    public String getPartnerLogo() {
        return PartnerLogo;
    }

    public boolean isHasText() {
        return HasText;
    }

    public String getPartnerName() {
        return PartnerName;
    }

    public int getPartnerId() {
        return PartnerId;
    }

    public int getActivityId() {
        return ActivityId;
    }

    public int getListenCount() {
        return ListenCount;
    }

    public String getCreationDate() {
        return CreationDate;
    }

    public void setListenCount(int listenCount) {
        ListenCount = listenCount;
    }

    public void setCreationDate(String creationDate) {
        CreationDate = creationDate;
    }

    public boolean isListen() {
        return IsListen;
    }

    public void setListen(boolean listen) {
        IsListen = listen;
    }

    public TrackObject( ) {

    }

    public TrackObject( String name) {

        Name = name;
    }
}

