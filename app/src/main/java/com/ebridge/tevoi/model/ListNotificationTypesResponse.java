package com.ebridge.tevoi.model;

import java.util.List;

public class ListNotificationTypesResponse extends  IResponse
{
    private List<NotificationTypeObject> lstNotiicationTypes;

    public List<NotificationTypeObject> getLstNotiicationTypes() {
        return lstNotiicationTypes;
    }

    public void setLstNotiicationTypes(List<NotificationTypeObject> lstNotiicationTypes) {
        this.lstNotiicationTypes = lstNotiicationTypes;
    }
}
