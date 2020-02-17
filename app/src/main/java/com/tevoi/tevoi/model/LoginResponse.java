package com.tevoi.tevoi.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginResponse  extends IResponse{

    @SerializedName("Token")
    private String Token;
    @SerializedName("AboutUs")
    private String AboutUs;
    @SerializedName("UserId")
    private int UserId;
    @SerializedName("NumberOfMinutes")
    private int NumberOfMinutes;
    @SerializedName("lstTracks")
    private List<TrackObject> lstTracks;
    @SerializedName("lstHistoryTracks")
    private List<TrackObject> lstHistoryTracks;
    @SerializedName("lstFavouriteTracks")
    private List<TrackObject> lstFavouriteTracks;
    @SerializedName("lstPartners")
    private List<PartnerObject> lstPartners;
    @SerializedName("lstUserLists")
    private List<UserListObject> Userlst;
    @SerializedName("lstNotificationTypes")
    private List<PartnerObject> lstNotificationTypes;
    @SerializedName("lstMainTopic")
    public List<MainTopic> lstMainTopic;
    @SerializedName("lstSubscripedPartners")
    public List<SubscipedPartnersObject> lstSubscripedPartners;
    @SerializedName("isShowHeardTracks")
    public boolean isShowHeardTracks;
    @SerializedName("Banner")
    private ListBannerResponse Banner;
    @SerializedName("UserInfo")
    private UserProfileResponse UserInfo;
    @SerializedName("UserName")
    public String UserName;
    @SerializedName("Email")
    public String Email;

    public void setUserInfo(UserProfileResponse userInfo) {
        UserInfo = userInfo;
    }

    public UserProfileResponse getUserInfo() {
        return UserInfo;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setAge(int age) {
        Age = age;
    }

    public void setGender(int gender) {
        Gender = gender;
    }

    public void setOccupation(String occupation) {
        Occupation = occupation;
    }

    public void setCountry(int country) {
        Country = country;
    }

    public String getUserName() {
        return UserName;
    }

    public String getEmail() {
        return Email;
    }

    public int getAge() {
        return Age;
    }

    public int getGender() {
        return Gender;
    }

    public String getOccupation() {
        return Occupation;
    }

    public int getCountry() {
        return Country;
    }

    @SerializedName("Age")
    public int Age;
    @SerializedName("Gender")
    public int Gender;
    @SerializedName("Occupation")
    public String Occupation;
    @SerializedName("Country")
    public int Country;


    public void setLstHistoryTracks(List<TrackObject> lstHistoryTracks) {
        this.lstHistoryTracks = lstHistoryTracks;
    }

    public ListBannerResponse getBanner() {
        return Banner;
    }

    public void setBanner(ListBannerResponse banner) {
        Banner = banner;
    }

    public List<TrackObject> getLstHistoryTracks() {
        return lstHistoryTracks;
    }

    public void setAboutUs(String aboutUs) {
        AboutUs = aboutUs;
    }

    public void setNumberOfMinutes(int numberOfMinutes) {
        NumberOfMinutes = numberOfMinutes;
    }

    public boolean isShowHeardTracks() {
        return isShowHeardTracks;
    }

    public void setShowHeardTracks(boolean showHeardTracks) {
        isShowHeardTracks = showHeardTracks;
    }

    public String getAboutUs() {
        return AboutUs;
    }

    public int getNumberOfMinutes() {
        return NumberOfMinutes;
    }

    public void setAboutUS(String aboutUs) {
        AboutUs = aboutUs;
    }

    public String getAboutUS() {
        return AboutUs;
    }

    public List<TrackObject> getLstFavouriteTracks() {
        return lstFavouriteTracks;
    }

    public void setLstFavouriteTracks(List<TrackObject> lstFavouriteTracks) {
        this.lstFavouriteTracks = lstFavouriteTracks;
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
    public List<UserListObject> getUserlst() {
        return Userlst;
    }

    public void setUserlst(List<UserListObject> userlst) {
        Userlst = userlst;
    }

    public List<PartnerObject> getLstNotificationTypes() {
        return lstNotificationTypes;
    }

    public List<MainTopic> getLstMainTopic() {
        return lstMainTopic;
    }

    public List<SubscipedPartnersObject> getLstSubscripedPartners() {
        return lstSubscripedPartners;
    }

    public void setLstNotificationTypes(List<PartnerObject> lstNotificationTypes) {
        this.lstNotificationTypes = lstNotificationTypes;
    }

    public void setLstMainTopic(List<MainTopic> lstMainTopic) {
        this.lstMainTopic = lstMainTopic;
    }

    public void setLstSubscripedPartners(List<SubscipedPartnersObject> lstSubscripedPartners) {
        this.lstSubscripedPartners = lstSubscripedPartners;
    }
}
