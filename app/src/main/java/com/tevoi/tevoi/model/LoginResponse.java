package com.tevoi.tevoi.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginResponse  extends IResponse{

    @SerializedName("Token")
    private String Token;
    @SerializedName("AboutUS")
    private String AboutUS;
    @SerializedName("UserId")
    private int UserId;
    @SerializedName("lstTracks")
    private List<TrackObject> lstTracks;
    @SerializedName("lstPartners")
    private List<PartnerObject> lstPartners;
    @SerializedName("lstUserLists")
    private List<PartnerObject> Userlst;
    @SerializedName("lstNotification")
    private List<PartnerObject> lstNotification;
    @SerializedName("NumberOfMinutes")
    private int NumberOfMinutes;

    public int getNumberOfMinutes() {
        return NumberOfMinutes;
    }

    public void setNumberOfMinutes(int numberOfMinutes) {
        NumberOfMinutes = numberOfMinutes;
    }

    public void setAboutUS(String aboutUS) {
        AboutUS = aboutUS;
    }

    public String getAboutUS() {
        return AboutUS;
    }

    public List<PartnerObject> getLstNotification() {
        return lstNotification;
    }

    public void setLstNotification(List<PartnerObject> lstNotification) {
        this.lstNotification = lstNotification;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getToken(){
        return Token;
    }
    public void setToken(String token){
        this.Token = token;
    }

    public void setLstTracks(List<TrackObject> lstTracks) {
        this.lstTracks = lstTracks;
    }

    public List<TrackObject> getLstTracks() {
        return lstTracks;
    }

    public void setLstPartners(List<PartnerObject> lstPartners) {
        this.lstPartners = lstPartners;
    }

    public List<PartnerObject> getLstPartners() {
        return lstPartners;
    }
    public List<PartnerObject> getUserlst() {
        return Userlst;
    }

    public void setUserlst(List<PartnerObject> userlst) {
        Userlst = userlst;
    }

}
