package com.tevoi.tevoi.model;

public class UserSubscriptionInfoResponse extends IResponse
{
    public int numberOfListenUnitsConsumed;
    public int numberOfReadUnitsConsumed ;
    public int SubscriptionType ;
    //todo: add UnitInSeconds as int
    //todo: IsFreeSubscription
    public boolean IsFreeSubscription ;

    public UserSubscriptionInfoResponse()
    {
        FreeSubscriptionLimit = new FreeSubscriptionLimits();
    }

    public FreeSubscriptionLimits FreeSubscriptionLimit;

    public class FreeSubscriptionLimits
    {
        public int DailyListenMaxUnits ;
        public int DailyReadMaxUnits ;
        //public int MonthlyListenMaxUnits ;
        //public int MonthlyReadMaxUnits;
    }

}
