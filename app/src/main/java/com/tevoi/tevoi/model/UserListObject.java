package com.tevoi.tevoi.model;

public class UserListObject
{
    private int Id;
    private  String Name;
    private String CreationDate;
    private  String Duration;

    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getCreationDate() {
        return CreationDate;
    }

    public String getDuration() {
        return Duration;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setCreationDate(String creationDate) {
        CreationDate = creationDate;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    @Override
    public String toString() {
        return Name;
    }
}
