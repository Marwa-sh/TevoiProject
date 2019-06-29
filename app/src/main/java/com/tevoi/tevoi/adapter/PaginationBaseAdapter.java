package com.tevoi.tevoi.adapter;

public interface PaginationBaseAdapter
{
    public static final int ITEM = 0;
    public static final int LOADING = 1;
    public boolean isLoadingAdded = false;
    public boolean retryPageLoad = false;
    public String errorMsg="";



}
