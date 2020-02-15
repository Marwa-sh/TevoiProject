package com.tevoi.tevoi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListBannerResponse extends IResponse
{
    @Expose
    @SerializedName("BannerLink")
    public String BannerLink;
    @Expose
    @SerializedName("BannerImagePath")
    public String BannerImagePath;

    public void setBannerLink(String bannerLink) {
        BannerLink = bannerLink;
    }

    public void setBannerImagePath(String bannerImagePath) {
        BannerImagePath = bannerImagePath;
    }

    public String getBannerLink() {
        return BannerLink;
    }

    public String getBannerImagePath() {
        return BannerImagePath;
    }
}
