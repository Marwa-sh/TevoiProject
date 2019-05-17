package com.ebridge.tevoi.model;

public class PartnerObject
{
    private int id;
    private String Name;
    private String Descripton;
    private int NumberOfTracks;

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
        return id;
    }

    public String getName() {
        return Name;
    }

    public String getLogo() {
        return Logo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }
}
