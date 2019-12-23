package com.tevoi.tevoi.adapter;

public class Category {
    int Id;
    private String name;
    private Category parent;

    public Category()
    {
        name="name";
        parent = null;
    }

    public Category(int id, String name, Category parent) {
        Id = id;
        this.name = name;
        this.parent = parent;
    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    public Category getParent() {
        return parent;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }
}
