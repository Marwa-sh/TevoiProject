package com.tevoi.tevoi.model;

import java.util.List;

public class GetUserListTracksResponse extends  IResponse
{
    private List<TrackObject> lstTrack;
    private int TotalRows;

    public int getTotalRows() {
        return TotalRows;
    }

    public List<TrackObject> getLstTrack() {
        return lstTrack;
    }

    public void setLstTrack(List<TrackObject> lstTrack) {
        this.lstTrack = lstTrack;
    }
}
