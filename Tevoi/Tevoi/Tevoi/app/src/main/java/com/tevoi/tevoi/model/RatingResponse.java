package com.tevoi.tevoi.model;

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
