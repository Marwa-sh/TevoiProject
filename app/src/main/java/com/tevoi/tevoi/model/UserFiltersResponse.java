package com.tevoi.tevoi.model;

import java.util.List;

public class UserFiltersResponse
{
    public List<MainTopic> MainTopicList;
    //public List<CategoryObject> CategoriesList;
    public List<SubscipedPartnersObject> SubscripedPartners;
    //public List<TrackTypeObject> TrackTypesList;


    public void setSubscripedPartners(List<SubscipedPartnersObject> subscripedPartners) {
        SubscripedPartners = subscripedPartners;
    }
    public List<SubscipedPartnersObject> getSubscripedPartners() {
        return SubscripedPartners;
    }

    public List<MainTopic> getMainTopicList() {
        return MainTopicList;
    }

    public void setMainTopicList(List<MainTopic> mainTopicList) {
        MainTopicList = mainTopicList;
    }
}
