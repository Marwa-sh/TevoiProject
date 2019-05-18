package com.ebridge.tevoi.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.ebridge.tevoi.model.TrackSerializableObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyStorage {
    public static final String PREFS_NAME = "Tevoi";
    //public static final String FAVORITES = "Favorite";
    public static final String PlayNowTrack = "PlayNowTrack";


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
        return (ArrayList) playNowTracks;
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
        ArrayList playNowTracks = loadPlayNowTracks(context);
        if (playNowTracks != null) {
            playNowTracks.remove(myModel);
            storePlayNowTracks(context, playNowTracks);
        }
    }
}
