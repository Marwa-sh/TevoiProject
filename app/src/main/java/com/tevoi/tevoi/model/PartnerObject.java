package com.tevoi.tevoi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PartnerObject
{
    @Expose
    @SerializedName("Id")
    private int Id;
    @Expose
    @SerializedName("Name")
    private String Name;
    @Expose
    @SerializedName("Descripton")
    private String Descripton;
    @Expose
    @SerializedName("NumberOfTracks")
    private int NumberOfTracks;
    @Expose
    @SerializedName("Logo")
    private String Logo;


    public int getNumberOfTracks() {
        return NumberOfTracks;
    }

    public void setNumberOfTracks(int numberOfTracks) {
        NumberOfTracks = numberOfTracks;
    }

    public void setDescripton(String descripton) {
        Descripton = descripton;
    }

    public String getDescripton() {
        return Descripton;
    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getLogo() {
        return Logo;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }
}
