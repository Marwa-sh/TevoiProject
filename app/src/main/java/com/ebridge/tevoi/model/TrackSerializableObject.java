package com.ebridge.tevoi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TrackSerializableObject implements Serializable {
    //@Expose annotation used for Gson
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
    @SerializedName("IsFavourite")
    private boolean IsFavourite;

    @Expose
    @SerializedName("ActivityId")
    private int ActivityId;


    public void setFavourite(boolean favourite) {
        IsFavourite = favourite;
    }

    public boolean isFavourite() {
        return IsFavourite;
    }

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

    public void setAuthor(String author) {
        Authors = author;
    }

    public void setCategories(String categories) {
        Categories = categories;
    }

    public void setAuthors(String authors) {
        Authors = authors;
    }

    public void setHasLocation(boolean hasLocation) {
        HasLocation = hasLocation;
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

    public void setPartnerLogo(String partnerLogo) {
        PartnerLogo = partnerLogo;
    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getDuration() {
        return Duration;
    }

    public int getRate() {
        return Rate;
    }

    public String getAuthor() {
        return Authors;
    }

    public String getCategories() {
        return Categories;
    }

    public String getAuthors() {
        return Authors;
    }

    public boolean isHasLocation() {
        return HasLocation;
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

    public String getPartnerLogo() {
        return PartnerLogo;
    }

    public int getActivityId() {
        return ActivityId;
    }

    public void setActivityId(int activityId) {
        ActivityId = activityId;
    }
}