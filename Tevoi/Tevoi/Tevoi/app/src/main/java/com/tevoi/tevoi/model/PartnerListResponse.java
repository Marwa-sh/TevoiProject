package com.tevoi.tevoi.model;

import java.util.List;

public class PartnerListResponse extends  IResponse
{
    private  int TotalRowCount;
    private List<PartnerObject> Partners;

    public int getTotalRowCount() {
        return TotalRowCount;
    }

    public List<PartnerObject> getPartners() {
        return Partners;
    }
}
