package com.tevoi.tevoi.model;

public class GenderObject {
    public int Id;
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
