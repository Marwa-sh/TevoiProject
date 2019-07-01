package com.tevoi.tevoi.model;

public class UserSubscriptionInfoResponse extends IResponse
{
    int numberOfListenUnitsConsumed;
    int numberOfReadUnitsConsumed ;
    int SubscriptionType ;
    //todo: add UnitInSeconds as int
    //todo: IsFreeSubscription
    boolean IsFreeSubscriotion ;

    public UserSubscriptionInfoResponse()
    {
        FreeSubscriptionLimit = new FreeSubscriptionLimits();
    }

    FreeSubscriptionLimits FreeSubscriptionLimit;

    public class FreeSubscriptionLimits
    {
        public int DailyListenMaxUnits ;
        public int DailyReadMaxUnits ;
        public int MonthlyListenMaxUnits ;
        public int MonthlyReadMaxUnits;
    }

}
