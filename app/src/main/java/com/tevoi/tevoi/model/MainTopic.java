package com.tevoi.tevoi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class MainTopic
{
    @Expose
    @SerializedName("Id")
    public int Id;
    @Expose
    @SerializedName("Name")
    public String Name ;
    @Expose
    @SerializedName("FilterValue")
    public boolean FilterValue ;
    @Expose
    @SerializedName("CategoriesList")
    public List<CategoryObject> CategoriesList ;
}
