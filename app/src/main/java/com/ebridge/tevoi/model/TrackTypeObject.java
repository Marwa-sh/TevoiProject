package com.ebridge.tevoi.model;

public class TrackTypeObject {

    private int Id;
    private String Name;
    private boolean TrackTypeFilter;

    //Region Setters getters

    public TrackTypeObject(int id, String name, boolean trackTypeFilter) {
        Id = id;
        Name = name;
        TrackTypeFilter = trackTypeFilter;
    }

    public TrackTypeObject() {

    }

    public void setId(int id) {
        Id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setTrackTypeFilter(boolean trackTypeFilter) {
        TrackTypeFilter = trackTypeFilter;
    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public boolean isTrackTypeFilter() {
        return TrackTypeFilter;
    }

    //end Region

}
