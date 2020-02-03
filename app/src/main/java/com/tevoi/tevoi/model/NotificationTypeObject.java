package com.tevoi.tevoi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationTypeObject
{
    @Expose
    @SerializedName("Id")
    private int Id;
    @Expose
    @SerializedName("Name")
    private String Name;
    @Expose
    @SerializedName("IsActive")
    private boolean  IsActive;

    public int getId() {
        return Id;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public String getName() {
        return Name;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setName(String name) {
        Name = name;
    }
}
