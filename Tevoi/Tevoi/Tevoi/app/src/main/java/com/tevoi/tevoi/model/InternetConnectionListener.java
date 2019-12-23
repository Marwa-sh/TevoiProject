package com.tevoi.tevoi.model;

public interface InternetConnectionListener
{
    boolean isInternetAvailable();
    void onInternetUnavailable();
}
