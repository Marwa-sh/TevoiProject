package com.tevoi.tevoi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrackRateResponse extends IResponse {
    @Expose
    @SerializedName("Id")
    public int Rate;
}
