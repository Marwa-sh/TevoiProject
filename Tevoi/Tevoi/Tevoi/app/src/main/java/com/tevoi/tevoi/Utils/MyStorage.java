package com.tevoi.tevoi.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.BoringLayout;

import com.tevoi.tevoi.model.TrackSerializableObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyStorage
{
    public static final String PREFS_NAME = "Tevoi";
    //public static final String FAVORITES = "Favorite";
    public static final String PlayNowTrack = "PlayNowTrack";
    public static final String PreferenceUILanguage = "PreferenceUILanguage";
    public static final String PreferenceTrackLanguage = "PreferenceTrackLanguage";
    public static final String TrackTypeFilter = "TrackTypeFilter";
    public static final String UserToken = "UserToken";
    public static final String RememberMe = "RememberMe";
    public static final String ShowHeardTracks="ShowHeardTracks";
    public static final String ShowNewsTracks="ShowNewsTracks";
    public static final String ShowArticlesTracks="ShowArticlesTracks";
    public static final String IsFirstTime = "IsFirstTime";
    public static final String DemoUsageUnits = "DemoUsageUnits";


    private  String Suffix =  "_" + USER_ID;

    int UserId;
    public static final  String USER_ID = "UserId";

    public MyStorage(int userId)
    {
        UserId = userId;
        Suffix = "_" + UserId;
    }

    public MyStorage()
    {
        UserId = 0;
        Suffix = "_" + UserId;
    }

    // region shared preference for next play list

    public void storePlayNowTracks(Context context, List playNowTracks)
    {
        // used for store arrayList in json format
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME + Suffix, Context.MODE_PRIVATE);
        editor = settings.edit();

        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson sExposeGson = builder.create();
        String jsonFavorites = sExposeGson.toJson(playNowTracks);
        editor.putString(PlayNowTrack + Suffix, jsonFavorites);
        editor.commit();

        //Tricky- This will helps to resolve issue for :
        //1: java.lang.SecurityException: Can't make field constructor accessible(https://github.com/google/gson/issues/648)

        //GsonBuilder builder = new GsonBuilder();
        //builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
        //builder.excludeFieldsWithoutExposeAnnotation();
        //Gson sExposeGson = builder.create();

        //Store and Retrieve a class object in SharedPreference in Android
        //We can do this using gson.jar
        //Download this jar here

        //For saving object
        //Gson gson = new Gson();
        //String json = gson.toJson(TrackSerializableObject);
        //editor.putString("TrackSerializableObject", json);
        //editor.commit();

        //For getting object
        //Gson gson = new Gson();
        //String json = settings.getString("TrackSerializableObject", "");
        //TrackSerializableObject obj = gson.fromJson(json, TrackSerializableObject.class);

    }

    public ArrayList<TrackSerializableObject> loadPlayNowTracks(Context context)
    {
        // used for retrieving arraylist from json formatted string
        SharedPreferences settings;
        ArrayList<TrackSerializableObject> playNowTracks;
        settings = context.getSharedPreferences(PREFS_NAME + Suffix, Context.MODE_PRIVATE);
        if (settings.contains(PlayNowTrack + Suffix)) {
            String jsonPlayNowTracks = settings.getString(PlayNowTrack + Suffix, null);

            GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
            builder.excludeFieldsWithoutExposeAnnotation();
            Gson sExposeGson = builder.create();
            TrackSerializableObject[] trackItems = sExposeGson.fromJson(jsonPlayNowTracks, TrackSerializableObject[].class);
            //playNowTracks = Arrays.asList(trackItems);
            playNowTracks = new ArrayList( Arrays.asList(trackItems));
        } else
            return new ArrayList<>();
        return playNowTracks;
    }

    public String addTrack(Context context, TrackSerializableObject myModel)
    {
        String result = "";
        ArrayList<TrackSerializableObject> playNowTracks = loadPlayNowTracks(context);
        if (playNowTracks == null)
            playNowTracks = new ArrayList();
        Boolean isTrackExists = false;
        // check if this track already exists or not
        for (int i=0; i< playNowTracks.size(); i++ )
        {
            if(playNowTracks.get(i).getId()== myModel.getId())
            {
                result = "Track Already Exists";
                isTrackExists = true;
            }
        }
        if(!isTrackExists)
        {result = "Track added successfully";
        playNowTracks.add(myModel);
        storePlayNowTracks(context, playNowTracks);}
        return result;
    }

    public void removeTrack(Context context, TrackSerializableObject myModel)
    {
        ArrayList<TrackSerializableObject> playNowTracks = loadPlayNowTracks(context);
        if (playNowTracks != null)
        {
            for ( int i =0; i< playNowTracks.size(); i++)
            {
                if(playNowTracks.get(i).getId() == myModel.getId())
                {
                    playNowTracks.remove(i);
                    break;
                }
            }
            //playNowTracks.remove(myModel);
            storePlayNowTracks(context, playNowTracks);
        }
    }

    public void removeTrackById(Context context, int trackId)
    {
        ArrayList<TrackSerializableObject> playNowTracks = loadPlayNowTracks(context);
        if (playNowTracks != null)
        {
            for ( int i =0; i< playNowTracks.size(); i++)
            {
                if(playNowTracks.get(i).getId() == trackId)
                {
                    playNowTracks.remove(i);
                    break;
                }
            }
            //playNowTracks.remove(myModel);
            storePlayNowTracks(context, playNowTracks);
        }
    }
    public void deletePlayNowList(Context context)
    {
        ArrayList<TrackSerializableObject> playNowTracks = loadPlayNowTracks(context);
        for ( int i =0; i< playNowTracks.size(); i++)
        {
            playNowTracks.remove(i);
        }
        storePlayNowTracks(context, playNowTracks);
    }
    //endregion

    // region UI Language storage

    public void storeLanguageUIPreference(Context context, String language)
    {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PreferenceUILanguage + Suffix, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(PreferenceUILanguage + Suffix, language);
        editor.commit();
    }

    public String getLanguageUIPreference(Context context)
    {
        SharedPreferences settings;
        String language = "";
        settings = context.getSharedPreferences(PreferenceUILanguage + Suffix, Context.MODE_PRIVATE);
        if (settings.contains(PreferenceUILanguage + Suffix))
        {
            language = settings.getString(PreferenceUILanguage + Suffix, null);
        }
        else
            language = Global.DefaultUILanguage;

        return language;
    }

    // endregion

    // region Track Language storage

    public void storeLanguageTrackPreference(Context context, String language)
    {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PreferenceTrackLanguage + Suffix, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(PreferenceTrackLanguage + Suffix, language);
        editor.commit();
    }

    public String getLanguageTrackPreference(Context context)
    {
        SharedPreferences settings;
        String language = "";
        settings = context.getSharedPreferences(PreferenceTrackLanguage + Suffix, Context.MODE_PRIVATE);
        if (settings.contains(PreferenceTrackLanguage + Suffix))
        {
            language = settings.getString(PreferenceTrackLanguage + Suffix, null);
        }
        else
            language = Global.DefaultTrackLanguage;

        return language;
    }

    // endregion

    // region current user id
    public void storeCurrentUserId(Context context, int userId)
    {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(USER_ID, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putInt(USER_ID, userId);
        editor.commit();
    }

    public int getCurrentUserId(Context context)
    {
        SharedPreferences settings;
        int userId = 0;
        settings = context.getSharedPreferences(USER_ID, Context.MODE_PRIVATE);
        if (settings.contains(USER_ID))
        {
            userId = settings.getInt(USER_ID, 0);
        }

        return userId;
    }

    // endregion

    // region track types
    public void storeTrackTypeFilter(Context context, String trackType)
    {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME +Suffix, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(TrackTypeFilter + Suffix, trackType);
        editor.commit();
    }

    //endregion

    // region user token storage

    public void storeTokenPreference(Context context, String token)
    {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(UserToken, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(UserToken, token);
        editor.commit();
    }

    public String getTokenPreference(Context context)
    {
        SharedPreferences settings;
        String token = "";
        settings = context.getSharedPreferences(UserToken, Context.MODE_PRIVATE);
        if (settings.contains(UserToken))
        {
            token = settings.getString(UserToken, null);
        }
        else
            token = "";

        return token;
    }
    // endregion

    //region filters
    public String getShowHeardTracksState(Context context)
    {
        SharedPreferences settings;
        String state="";
        settings = context.getSharedPreferences(ShowHeardTracks + Suffix,context.MODE_PRIVATE);
        if(settings.contains(ShowHeardTracks + Suffix))
        {
            state=settings.getString(ShowHeardTracks + Suffix,"true");
        }
        state="true";
        return state;

    }
    public String getShowNewsTracksState(Context context)
    {
        SharedPreferences settings;
        String state="";
        settings = context.getSharedPreferences(ShowNewsTracks + Suffix,context.MODE_PRIVATE);
        if(settings.contains(ShowNewsTracks + Suffix))
        {
            state=settings.getString(ShowNewsTracks + Suffix,"true");
        }
        state="true";
        return state;

    }
    public String getShowArticlesTracksState(Context context)
    {
        SharedPreferences settings;
        String state="";
        settings = context.getSharedPreferences(ShowArticlesTracks + Suffix,context.MODE_PRIVATE);
        if(settings.contains(ShowArticlesTracks + Suffix))
        {
            state=settings.getString(ShowArticlesTracks + Suffix,"true");
        }
        state="true";
        return state;

    }

    public void storeSHowHeardTracksState(Context context, String state)
    {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(ShowHeardTracks + Suffix, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(ShowHeardTracks + Suffix, state);
        editor.commit();
    }
    public void storeShowNewsTracksState(Context context, String state)
    {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(ShowNewsTracks + Suffix, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(ShowNewsTracks +Suffix, state);
        editor.commit();
    }
    public void storeShowArticlesTracksState(Context context, String state)
    {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(ShowArticlesTracks +Suffix, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(ShowArticlesTracks +Suffix, state);
        editor.commit();
    }

    // endregion

    // region Remember Me
    public void storeRememberMePreference(Context context, Boolean isRememberMe)
    {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(RememberMe, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putBoolean(RememberMe, isRememberMe);
        editor.commit();
    }

    public Boolean getRememberMePreference(Context context)
    {
        SharedPreferences settings;
        Boolean isRememberMe = false;
        settings = context.getSharedPreferences(RememberMe, Context.MODE_PRIVATE);
        if (settings.contains(RememberMe))
        {
            isRememberMe = settings.getBoolean(RememberMe, false);
        }

        return isRememberMe;
    }
    //endregion

    // region Is First Time
    public void storeIsFirstTimePreference(Context context, Boolean isFirstTime)
    {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(IsFirstTime, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putBoolean(IsFirstTime, isFirstTime);
        editor.commit();
    }

    public Boolean getIsFirstTimePreference(Context context)
    {
        SharedPreferences settings;
        Boolean isFirstTime = false;
        settings = context.getSharedPreferences(IsFirstTime, Context.MODE_PRIVATE);
        if (settings.contains(IsFirstTime))
        {
            isFirstTime = settings.getBoolean(IsFirstTime, false);
        }

        return isFirstTime;
    }
    //endregion

    //region DemoUsageUnits
    public void storeDemoUsagePreference(Context context, int demoUsageUnits)
    {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(DemoUsageUnits, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putInt(DemoUsageUnits, demoUsageUnits);
        editor.commit();
    }
    public int  getDemoUsagePreference(Context context)
    {
        SharedPreferences settings;
        int demoUsageUnits = 0;
        settings = context.getSharedPreferences(DemoUsageUnits, Context.MODE_PRIVATE);
        if (settings.contains(DemoUsageUnits))
        {
            demoUsageUnits = settings.getInt(DemoUsageUnits, 0);
        }
        return demoUsageUnits;
    }
    //endregion
}
