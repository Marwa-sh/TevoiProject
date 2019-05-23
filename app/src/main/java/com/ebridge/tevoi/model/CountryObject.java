package com.ebridge.tevoi.model;

public class CountryObject {
    public int Id;
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
