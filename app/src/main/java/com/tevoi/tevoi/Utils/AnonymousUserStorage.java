package com.tevoi.tevoi.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AnonymousUserStorage
{
    public static  String InstallationDate = "InstallationDate" ;
    public static  boolean isFirstDay = true;

    // region installation date storage

    public void storeInstallationDatePreference(Context context, String installationDate)
    {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(InstallationDate, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(InstallationDate, installationDate);
        editor.commit();
    }

    public String getInstallationDatePreference(Context context)
    {
        SharedPreferences settings;
        String installationDate = "";
        settings = context.getSharedPreferences(InstallationDate, Context.MODE_PRIVATE);
        if (settings.contains(InstallationDate))
        {
            installationDate = settings.getString(InstallationDate, null);
        }
        else
            installationDate = "";

        return installationDate;
    }
    // endregion

    // filter  + 


}
