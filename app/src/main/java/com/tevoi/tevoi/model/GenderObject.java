package com.tevoi.tevoi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenderObject {
    @Expose
    @SerializedName("Id")
    public int Id;
    @Expose
    @SerializedName("Name")
    public String Name;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    @Override
    public String toString() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public GenderObject(int id, String name) {
        Id = id;
        Name = name;
    }
}
