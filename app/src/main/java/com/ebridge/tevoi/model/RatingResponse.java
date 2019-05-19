package com.ebridge.tevoi.model;

import com.google.gson.annotations.SerializedName;

public class RatingResponse extends IResponse {

    int Rating;

    public RatingResponse(int rating) {
        Rating = rating;
    }

    public void setRating(int rating) {
        Rating = rating;
    }

    public int getRating() {
        return Rating;
    }
}
