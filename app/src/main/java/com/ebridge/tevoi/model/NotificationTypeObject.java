package com.ebridge.tevoi.model;

public class NotificationTypeObject
{
    private int Id;
    private String Name;
    private boolean  IsActive;

    public int getId() {
        return Id;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
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
