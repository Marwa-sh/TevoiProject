package com.tevoi.tevoi.model;

import java.util.List;

public class TrackResponseList {
    private List<TrackObject> lstTrack;
    private int TotalRowCount;


    public int getTotalRowCount() {
        return TotalRowCount;
    }
    public void setTotalRowCount(int totalRowCount) {
        TotalRowCount = totalRowCount;
    }

    public void setTrack(List<TrackObject> track) {
        this.lstTrack = track;
    }

    public List<TrackObject> getTrack() {
        return lstTrack;
    }
}
