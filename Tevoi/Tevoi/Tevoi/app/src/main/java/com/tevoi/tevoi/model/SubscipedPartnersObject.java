package com.tevoi.tevoi.model;

import com.google.gson.annotations.SerializedName;

public class SubscipedPartnersObject
{
    @SerializedName("Id")
    private int Id;
    @SerializedName("PartnerId")
    private  int PartnerId;
    @SerializedName("Name")
    private String Name;
    @SerializedName("FilterValue")
    private boolean FilterValue;

    //region Constructors setters and getters

    public SubscipedPartnersObject(int id, String name) {
        Id = id;
        Name = name;

    }

    public int getPartnerId() {
        return PartnerId;
    }

    public void setPartnerId(int partnerId) {
        PartnerId = partnerId;
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

    public SubscipedPartnersObject(int id, String name, boolean filterValue) {
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
