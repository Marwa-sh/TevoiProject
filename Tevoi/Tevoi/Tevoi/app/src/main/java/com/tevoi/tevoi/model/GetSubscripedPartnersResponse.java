package com.tevoi.tevoi.model;

import java.util.List;

public class GetSubscripedPartnersResponse extends  IResponse
{
    private List<SubscipedPartnersObject> SubscripedPartners;


    public List<SubscipedPartnersObject> getSubscripedPartners() {
        return SubscripedPartners;
    }

    public void setSubscripedPartners(List<SubscipedPartnersObject> lstSubscipedPartners) {
        this.SubscripedPartners = lstSubscipedPartners;
    }
}
