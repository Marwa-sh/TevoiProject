package com.tevoi.tevoi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserProfileResponse extends IResponse{

    @Expose
    @SerializedName("UserName")
    public String UserName;
    @Expose
    @SerializedName("Email")
    public String Email;
    @Expose
    @SerializedName("Age")
    public int Age;
    @Expose
    @SerializedName("Gender")
    public int Gender;
    @Expose
    @SerializedName("Occupation")
    public String Occupation;
    @Expose
    @SerializedName("Country")
    public int Country;

    @Expose
    List<CountryObject> lstCountry;
    @Expose
    List<GenderObject> lstGender;

    public List<CountryObject> getLstCountry() {
        return lstCountry;
    }

    public List<GenderObject> getLstGender() {
        return lstGender;
    }

    public void setLstCountry(List<CountryObject> lstCountry) {
        this.lstCountry = lstCountry;
    }

    public void setLstGender(List<GenderObject> lstGender) {
        this.lstGender = lstGender;
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

    public UserProfileResponse(String userName, String email, int age, int gender, String occupation, int country) {
        UserName = userName;
        Email = email;
        Age = age;
        Gender = gender;
        Occupation = occupation;
        Country = country;
    }

    public UserProfileResponse(){


    }
}
