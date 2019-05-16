package com.ebridge.tevoi.model;

import com.ebridge.tevoi.adapter.Category;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrackResponseList {
    private List<TrackObject> lstTrack;

    public void setTrack(List<TrackObject> track) {
        this.lstTrack = track;
    }

    public List<TrackObject> getTrack() {
        return lstTrack;
    }
}
