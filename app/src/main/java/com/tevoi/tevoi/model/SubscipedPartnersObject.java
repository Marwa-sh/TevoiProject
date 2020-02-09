package com.tevoi.tevoi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubscipedPartnersObject
{
    @Expose
    @SerializedName("Id")
    private int Id;
    @Expose
    @SerializedName("PartnerId")
    private  int PartnerId;
    @Expose
    @SerializedName("Name")
    private String Name;
    @Expose
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
