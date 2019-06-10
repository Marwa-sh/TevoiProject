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
    public static final String PreferenceLanguage = "PreferenceLanguage";
    public static final String TrackTypeFilter = "TrackTypeFilter";
    public static final String UserToken = "UserToken";
    public  static final String RememberMe = "RememberMe";
    public static final String ShowHeardTracks="ShowHeardTracks";
    public static final String ShowNewsTracks="ShowNewsTracks";
    public static final String ShowArticlesTracks="ShowArticlesTracks";

    public void storePlayNowTracks(Context context, List playNowTracks)
    {
        // used for store arrayList in json format
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson sExposeGson = builder.create();
        String jsonFavorites = sExposeGson.toJson(playNowTracks);
        editor.putString(PlayNowTrack, jsonFavorites);
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
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (settings.contains(PlayNowTrack)) {
            String jsonPlayNowTracks = settings.getString(PlayNowTrack, null);

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

    public void removeTrack(Context context, TrackSerializableObject myModel) {
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

    // region Language storage

    public void storeLanguagePreference(Context context, String language)
    {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PreferenceLanguage, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(PreferenceLanguage, language);
        editor.commit();
    }

    public String getLanguagePreference(Context context)
    {
        SharedPreferences settings;
        String language = "";
        settings = context.getSharedPreferences(PreferenceLanguage, Context.MODE_PRIVATE);
        if (settings.contains(PreferenceLanguage))
        {
            language = settings.getString(PreferenceLanguage, null);
        }
        else
            language = Global.DefaultLanguage;

        return language;
    }

    // endregion


    public void storeTrackTypeFilter(Context context, String trackType)
    {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(TrackTypeFilter, trackType);
        editor.commit();
    }



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
            token = Global.DefaultLanguage;

        return token;
    }
    // endregion

    // Region filters
    public String getShowHeardTracksState(Context context)
    {
        SharedPreferences settings;
        String state="";
        settings = context.getSharedPreferences(ShowHeardTracks,context.MODE_PRIVATE);
        if(settings.contains(ShowHeardTracks))
        {
            state=settings.getString(ShowHeardTracks,"true");
        }
        state="true";
        return state;

    }
    public String getShowNewsTracksState(Context context)
    {
        SharedPreferences settings;
        String state="";
        settings = context.getSharedPreferences(ShowNewsTracks,context.MODE_PRIVATE);
        if(settings.contains(ShowNewsTracks))
        {
            state=settings.getString(ShowNewsTracks,"true");
        }
        state="true";
        return state;

    }
    public String getShowArticlesTracksState(Context context)
    {
        SharedPreferences settings;
        String state="";
        settings = context.getSharedPreferences(ShowArticlesTracks,context.MODE_PRIVATE);
        if(settings.contains(ShowArticlesTracks))
        {
            state=settings.getString(ShowArticlesTracks,"true");
        }
        state="true";
        return state;

    }

    public void storeSHowHeardTracksState(Context context, String state)
    {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(ShowHeardTracks, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(UserToken, state);
        editor.commit();
    }
    public void storeShowNewsTracksState(Context context, String state)
    {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(ShowNewsTracks, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(ShowNewsTracks, state);
        editor.commit();
    }

    public void storeShowArticlesTracksState(Context context, String state)
    {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(ShowArticlesTracks, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(ShowArticlesTracks, state);
        editor.commit();
    }

    // endRegion

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

}
