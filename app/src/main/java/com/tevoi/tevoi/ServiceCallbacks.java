package com.tevoi.tevoi;

import com.tevoi.tevoi.model.UserSubscriptionInfoResponse;

public interface ServiceCallbacks
{

    public void playNext();
    public  void playPrevious();
    public void updateUserUsage(UserSubscriptionInfoResponse response);
    public void setFlagUserExceedsDailyUsageListen();
    //public void playBtn();
}
