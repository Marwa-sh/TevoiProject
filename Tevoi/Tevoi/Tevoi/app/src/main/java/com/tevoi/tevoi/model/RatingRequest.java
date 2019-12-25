package com.tevoi.tevoi.model;

public class RatingRequest {
    public int TrackId;
    public int Rating;

    public RatingRequest(int trackId, int rating) {
        TrackId = trackId;
        Rating = rating;
    }

    public int getTrackId() {
        return TrackId;
    }

    public int getRating() {
        return Rating;
    }

    public void setTrackId(int trackId) {
        TrackId = trackId;
    }

    public void setRating(int rating) {
        Rating = rating;
    }
}
