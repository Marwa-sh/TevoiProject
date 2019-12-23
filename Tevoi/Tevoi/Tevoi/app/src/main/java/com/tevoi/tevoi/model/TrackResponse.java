package com.tevoi.tevoi.model;

import com.tevoi.tevoi.adapter.Category;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrackResponse {


    @SerializedName("Id")
    private  int Id;
    @SerializedName("Name")
    private  String Name;
    @SerializedName("Duration")
    private  String Duration;
    @SerializedName("Rating")
    private float Rating;
    @SerializedName("Author")
    private  String Author;
    @SerializedName("Categories")
    private List<Category> Categories;

    public void setId(int id) {
        Id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public void setRating(float rating) {
        Rating = rating;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public void setCategories(List<Category> categories) {
        Categories = categories;
    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getDuration() {
        return Duration;
    }

    public float getRating() {
        return Rating;
    }

    public String getAuthor() {
        return Author;
    }

    public List<Category> getCategories() {
        return Categories;
    }
}
