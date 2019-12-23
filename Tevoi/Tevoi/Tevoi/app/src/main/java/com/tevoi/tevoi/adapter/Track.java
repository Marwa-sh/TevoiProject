package com.tevoi.tevoi.adapter;

import java.util.ArrayList;

public class Track {

    int id;
    private String Name;
    private String Duration;
    private String Url;
    private ArrayList<Category> Categories;
    private double Rating;
    private String Author;

    public Track()
    {
        Name="name";
        Duration="duration";
        Url="URL";
        Categories=new ArrayList<>();
        getCategories().add(new Category());
        getCategories().add(new Category());
        Rating = 5;
        Author = "author";
        id=1;

    }

    public Track(int id, String name, String duration, String url, ArrayList<Category> categories, double rating) {
        this.id = id;
        this.Name = name;
        this.Duration = duration;
        this.Url = url;
        this.Categories = categories;
        this.Rating = rating;
    }

    public Track(int id, String name, String duration, String url, ArrayList<Category> categories, double rating, String author) {
        this.id = id;
        this.Name = name;
        this.Duration = duration;
        this.Url = url;
        this.Categories = categories;
        this.Rating = rating;
        this.Author = author;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        this.Author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Track(String name, String duration, String url, ArrayList<Category> categories, double rating) {
        this.Name = name;
        this.Duration = duration;
        this.Url = url;
        this.Categories = categories;
        this.Rating = rating;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        this.Duration = duration;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        this.Url = url;
    }

    public ArrayList<Category> getCategories() {
        return Categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.Categories = categories;
    }

    public double getRating() {
        return Rating;
    }

    public void setRating(double rating) {
        this.Rating = rating;
    }
}
