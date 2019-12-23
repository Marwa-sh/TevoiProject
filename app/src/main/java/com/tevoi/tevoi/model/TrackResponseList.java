package com.tevoi.tevoi.model;

import java.util.List;

public class TrackResponseList {
    private List<TrackObject> lstTrack;
    private int TotalRowCount;
    private ListBannerResponse Banner;

    public int getTotalRowCount() {
        return TotalRowCount;
    }
    public void setTotalRowCount(int totalRowCount) {
        TotalRowCount = totalRowCount;
    }


    public void setLstTrack(List<TrackObject> lstTrack) {
        this.lstTrack = lstTrack;
    }

    public void setBanner(ListBannerResponse banner) {
        Banner = banner;
    }

    public List<TrackObject> getLstTrack() {
        return lstTrack;
    }

    public ListBannerResponse getBanner() {
        return Banner;
    }
}
