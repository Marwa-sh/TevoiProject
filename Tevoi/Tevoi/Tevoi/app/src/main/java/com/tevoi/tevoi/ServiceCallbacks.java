package com.tevoi.tevoi;

import com.tevoi.tevoi.model.UserSubscriptionInfoResponse;

public interface ServiceCallbacks
{

    public void playNext();
    public void playPrevious();
    public void updateUserUsage(UserSubscriptionInfoResponse response);
    public boolean isDemoUser();
    public void addUnitsForDemoUser(int n);
    public int getUnitsForDemoUser();
    public void setUserExceedsQuota();
    public boolean isExceedsQuotaDemoUser();

    public void showLoaderMediaPlayer();
    public void hideLoaderMediaPlayer();
    public boolean isLoaderVisible();

    public void setFlagUserExceedsDailyUsageListen();
    //public void playBtn();
}
