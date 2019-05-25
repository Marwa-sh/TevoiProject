package com.ebridge.tevoi.model;

public class RegisterRequest {
    public String UserName;
    public String Email;
    public String Password;
    public int Age;
    public int Gender;
    public String Occupation;
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
