package com.tevoi.tevoi.model;

import com.google.gson.annotations.SerializedName;

public class TrackObject {

    @SerializedName("Id")
    private  int Id;
    @SerializedName("Name")
    private  String Name;
    @SerializedName("Duration")
    private  String Duration;
    @SerializedName("Rate")
    private int Rate;
    @SerializedName("Authors")
    private  String Authors;
    @SerializedName("Categories")
    private String Categories;
    @SerializedName("HasLocation")
    private  boolean HasLocation;
    @SerializedName("HasText")
    private  boolean  HasText;
    @SerializedName("PartnerName")
    private String PartnerName;
    @SerializedName("PartnerId")
    private int PartnerId;
    @SerializedName("PartnerLogo")
    private String PartnerLogo;
    @SerializedName("ActivityId")
    private int ActivityId;
    private boolean IsFaourite;

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

    public boolean isFaourite() {
        return IsFaourite;
    }

    public void setFaourite(boolean faourite) {
        IsFaourite = faourite;
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

    public TrackObject( ) {

    }
    public TrackObject( String name) {

        Name = name;
    }
}

