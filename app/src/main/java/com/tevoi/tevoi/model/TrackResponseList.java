package com.tevoi.tevoi.model;

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
