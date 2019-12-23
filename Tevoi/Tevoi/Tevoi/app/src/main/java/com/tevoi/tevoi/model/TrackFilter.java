package com.tevoi.tevoi.model;

public class TrackFilter {
    public String SearchKey ;
    public boolean IsLocationEnabled ;
    public double Latitude ;
    public double Longitude ;
    public int TrackTypeId ;
    ///// <summary>
    ///// must be news or articles
    ///// </summary>
    //public bool WithNews { get; set; }
    ///// <summary>
    ///// must be news or articles
    ///// </summary>
    //public bool WithArticles { get; set; }
    public boolean WithoutHistory ;
    public int ListTypeEnum ;
    public int Index ;
    public int Size ;


    public String getStringFilter()
    {
        return  "SearchKey=" + SearchKey + ",IsLocationEnabled=" + IsLocationEnabled + ",WithoutHistory="+ WithoutHistory +
                ",ListTypeEnum=" + ListTypeEnum + ",Index="+ Index +",Size=" + Size
                +",TrackTypeId=" +TrackTypeId + ",Latitude=" + Latitude + ",Longitude=" +Longitude;
    }

}
