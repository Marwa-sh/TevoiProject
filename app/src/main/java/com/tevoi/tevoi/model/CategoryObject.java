package com.tevoi.tevoi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryObject
{
    @Expose
    @SerializedName("Id")
    private int Id;
    @Expose
    @SerializedName("Name")
    private String Name;
    @Expose
    @SerializedName("FilterValue")
    private boolean FilterValue;

    //region Constructors setters and getters

    public CategoryObject(int id, String name) {
        Id = id;
        Name = name;

    }
    public void setId(int id) {
        Id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean isFilterValue() {
        return FilterValue;
    }

    public CategoryObject(int id, String name, boolean filterValue) {
        Id = id;
        Name = name;
        FilterValue = filterValue;
    }

    public void setFilterValue(boolean filterValue) {
        FilterValue = filterValue;
    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }
    //end region
}
