package com.tevoi.tevoi.model;

public class MainSponsoreLogoResponse extends  IResponse
{
    private String MainSponsoreLogo;
    private String MainSponsoreName;

    public String getMainSponsoreLogo() {
        return MainSponsoreLogo;
    }

    public String getMainSponsoreName() {
        return MainSponsoreName;
    }

    public void setMainSponsoreLogo(String mainSponsoreLogo) {
        MainSponsoreLogo = mainSponsoreLogo;
    }

    public void setMainSponsoreName(String mainSponsoreName) {
        MainSponsoreName = mainSponsoreName;
    }
}
