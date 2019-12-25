package com.tevoi.tevoi.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserProfileRequest {
    @SerializedName("UserName")
    public String UserName;
    @SerializedName("Email")
    public String Email;
    @SerializedName("Age")
    public int Age;
    @SerializedName("Gender")
    public int Gender;
    @SerializedName("Occupation")
    public String Occupation;
    @SerializedName("Country")
    public int Country;







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

    public UserProfileRequest(String userName, String email, int age, int gender, String occupation, int country) {
        UserName = userName;
        Email = email;
        Age = age;
        Gender = gender;
        Occupation = occupation;
        Country = country;
    }

    public UserProfileRequest(){


    }


}
