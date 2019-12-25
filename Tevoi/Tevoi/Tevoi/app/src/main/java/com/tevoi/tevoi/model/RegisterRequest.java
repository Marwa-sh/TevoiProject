package com.tevoi.tevoi.model;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest
{
    @SerializedName("UserName")
    public String UserName;
    @SerializedName("Email")
    public String Email;
    @SerializedName("Password")
    public String Password;
    @SerializedName("Age")
    public int Age;
    @SerializedName("Gender")
    public int Gender;
    @SerializedName("Occupation")
    public String Occupation;
    @SerializedName("Country")
    public int Country;

    public RegisterRequest() {
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public int getGender() {
        return Gender;
    }

    public void setGender(int gender) {
        Gender = gender;
    }

    public String getOccupation() {
        return Occupation;
    }

    public void setOccupation(String occupation) {
        Occupation = occupation;
    }

    public int getCountry() {
        return Country;
    }

    public void setCountry(int country) {
        Country = country;
    }

    public RegisterRequest(String userName, String email, String password, int age, int gender, String occupation, int country) {
        UserName = userName;
        Email = email;
        Password = password;
        Age = age;
        Gender = gender;
        Occupation = occupation;
        Country = country;
    }
}
