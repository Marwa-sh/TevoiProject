package com.tevoi.tevoi.model;

import java.util.List;

public class GetPartnerTracksResponse extends  IResponse
{
    private List<TrackObject> PartnerTracks;

    public List<TrackObject> getPartnerTracks() {
        return PartnerTracks;
    }

    public void setPartnerTracks(List<TrackObject> partnerTracks) {
        PartnerTracks = partnerTracks;
    }
}
