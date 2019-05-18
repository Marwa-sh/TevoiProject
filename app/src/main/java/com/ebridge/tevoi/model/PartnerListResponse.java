package com.ebridge.tevoi.model;

import java.util.List;

public class PartnerListResponse extends  IResponse{
    private List<PartnerObject> Partners;

    public void setPartners(List<PartnerObject> partners) {
        this.Partners = partners;
    }

    public List<PartnerObject> getPartners() {
        return Partners;
    }
}
