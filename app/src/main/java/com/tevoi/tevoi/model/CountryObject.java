package com.tevoi.tevoi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountryObject {
    @Expose
    @SerializedName("Id")
    public int Id;
    @Expose
    @SerializedName("Name")
    public String Name;

    @Override
    public String toString() {
        return Name;
    }

    public CountryObject(int id, String name) {
        Id = id;
        Name = name;
    }

    public int getId() {
        return Id;
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
