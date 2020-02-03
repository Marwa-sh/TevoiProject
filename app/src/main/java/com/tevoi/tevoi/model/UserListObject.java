package com.tevoi.tevoi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserListObject
{
    @Expose
    @SerializedName("Id")
    private int Id;
    @Expose
    @SerializedName("Name")
    private  String Name;
    @Expose
    @SerializedName("CreationDate")
    private String CreationDate;
    @Expose
    @SerializedName("Duration")
    private  String Duration;

    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getCreationDate() {
        return CreationDate;
    }

    public String getDuration() {
        return Duration;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setCreationDate(String creationDate) {
        CreationDate = creationDate;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    @Override
    public String toString() {
        return Name;
    }
}
