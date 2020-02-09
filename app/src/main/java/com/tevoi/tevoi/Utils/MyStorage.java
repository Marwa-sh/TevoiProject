package com.tevoi.tevoi.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.BoringLayout;

import com.tevoi.tevoi.R;
import com.tevoi.tevoi.model.MainTopic;
import com.tevoi.tevoi.model.NotificationTypeObject;
import com.tevoi.tevoi.model.PartnerObject;
import com.tevoi.tevoi.model.SubscipedPartnersObject;
import com.tevoi.tevoi.model.TrackObject;
import com.tevoi.tevoi.model.TrackSerializableObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tevoi.tevoi.model.UserListObject;

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


    public static final String ArrayListTrack = "ArrayListTrack";
    public static final String ArrayListPartners = "ArrayListPartners";
    public static final String ArrayUserList = "ArrayUserList";
    public static final String ArrayListMainTopic = "ArrayListMainTopic";
    public static final String ArrayListSubscripedPartner = "ArrayListSubscripedPartner";
    public static final String ArrayNotificationList = " ArrayNotificationList";
    public static final String AboutUs = " AboutUs";
    public static final String NumberOfMinutes = "NumberOfMinutes";


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


    // region shared preference for User list
    public String addUserList(Context context, UserListObject myModel)
    {
        String result = "";
        ArrayList<UserListObject> userList = loadUserList(context);
        if (userList == null)
            userList = new ArrayList();
        Boolean isUserListExists = false;
        // check if this track already exists or not
        for (int i=0; i< userList.size(); i++ )
        {
            if(userList.get(i).getName()== myModel.getName())
            {
                result = "User List Already Exists";
                isUserListExists = true;
            }
        }
        if(!isUserListExists)
        {result = "User List added successfully";
            userList.add(myModel);
            storeUsetList(context, userList);}
        return result;
    }

    public void removeUserList(Context context, UserListObject myModel)
    {
        ArrayList<UserListObject> userList = loadUserList(context);
        if (userList != null)
        {
            for ( int i =0; i< userList.size(); i++)
            {
                if(userList.get(i).getId() == myModel.getId())
                {
                    userList.remove(i);
                    break;
                }
            }
            //playNowTracks.remove(myModel);
            storeUsetList(context, userList);
        }
    }

    public void removeUserListById(Context context, int userListId)
    {
        ArrayList<UserListObject> userList = loadUserList(context);
        if (userList != null)
        {
            for ( int i =0; i< userList.size(); i++)
            {
                if(userList.get(i).getId() == userListId)
                {
                    userList.remove(i);
                    break;
                }
            }
            //playNowTracks.remove(myModel);
            storeUsetList(context, userList);
        }
    }
    public void deleteUserList(Context context)
    {
        ArrayList<UserListObject> userList = loadUserList(context);
        for ( int i =0; i< userList.size(); i++)
        {
            userList.remove(i);
        }
        storeUsetList(context, userList);
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

    // region shared preference for List Tracks

    public void storeListTracks(Context context, List listTracks)
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
        String jsonFavorites = sExposeGson.toJson(listTracks);
        editor.putString(ArrayListTrack + Suffix, jsonFavorites);
        editor.commit();
    }

    public ArrayList<TrackObject> loadListTracks(Context context)
    {
        // used for retrieving arraylist from json formatted string
        SharedPreferences settings;
        ArrayList<TrackObject> listTracks;
        settings = context.getSharedPreferences(PREFS_NAME + Suffix, Context.MODE_PRIVATE);
        if (settings.contains(ArrayListTrack + Suffix)) {
            String jsonPlayNowTracks = settings.getString(ArrayListTrack + Suffix, null);

            GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
            builder.excludeFieldsWithoutExposeAnnotation();
            Gson sExposeGson = builder.create();
            TrackObject[] trackItems = sExposeGson.fromJson(jsonPlayNowTracks, TrackObject[].class);

            listTracks = new ArrayList( Arrays.asList(trackItems));
        } else
            return new ArrayList<>();
        return listTracks;
    }

    public ArrayList<TrackObject> loadFavoriteListTracks(Context context)
{
    // used for retrieving arraylist from json formatted string
    SharedPreferences settings;
    ArrayList<TrackObject> listTracks;
    settings = context.getSharedPreferences(PREFS_NAME + Suffix, Context.MODE_PRIVATE);
    if (settings.contains(ArrayListTrack + Suffix)) {
        String jsonPlayNowTracks = settings.getString(ArrayListTrack + Suffix, null);

        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson sExposeGson = builder.create();
        TrackObject[] trackItems = sExposeGson.fromJson(jsonPlayNowTracks, TrackObject[].class);

        listTracks = new ArrayList( Arrays.asList(trackItems));
        ArrayList<TrackObject> lstFavourite = new ArrayList<>();
        for (int j=0;j<listTracks.size();j++)
        {
            if (listTracks.get(j).isFavourite())
                lstFavourite.add(listTracks.get(j));
        }
        return lstFavourite;
    } else
        return new ArrayList<>();

}
    public void clearFavoriteListTracks(Context context)
    {
        // used for retrieving arraylist from json formatted string
        SharedPreferences settings;
        ArrayList<TrackObject> listTracks = loadListTracks(context);

        for (int j=0;j<listTracks.size();j++)
        {
                listTracks.get(j).setFavourite(false);
        }
        storeListTracks(context,listTracks);
    }
    public void LikeFunction(Context context,int Id)
    {
        ArrayList<TrackObject> listTracks = loadListTracks(context);

        for(int j=0 ; j<listTracks.size();j++)
        {
            if(listTracks.get(j).getId()== Id)
            {
                listTracks.get(j).setFavourite(!listTracks.get(j).isFavourite());
                break;
            }
        }
        storeListTracks(context,listTracks);
    }
    //endregion
    // used for retrieving History lsit

    public ArrayList<TrackObject> loadHistoryListTracks(Context context)
    {
        // used for retrieving arraylist from json formatted string
        SharedPreferences settings;
        ArrayList<TrackObject> listTracks;
        settings = context.getSharedPreferences(PREFS_NAME + Suffix, Context.MODE_PRIVATE);
        if (settings.contains(ArrayListTrack + Suffix)) {
            String jsonPlayNowTracks = settings.getString(ArrayListTrack + Suffix, null);

            GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
            builder.excludeFieldsWithoutExposeAnnotation();
            Gson sExposeGson = builder.create();
            TrackObject[] trackItems = sExposeGson.fromJson(jsonPlayNowTracks, TrackObject[].class);

            listTracks = new ArrayList( Arrays.asList(trackItems));
            ArrayList<TrackObject> lstHistory = new ArrayList<>();
            for (int j=0;j<listTracks.size();j++)
            {
                if (listTracks.get(j).isListen())
                    lstHistory.add(listTracks.get(j));
            }
            return lstHistory;
        } else
            return new ArrayList<>();

    }
    public void clearHistoryListTracks(Context context)
    {
        // used for retrieving arraylist from json formatted string
        SharedPreferences settings;
        ArrayList<TrackObject> listTracks = loadListTracks(context);

        for (int j=0;j<listTracks.size();j++)
        {
            listTracks.get(j).setListen(false);
        }
        storeListTracks(context,listTracks);
    }
    public void Addtohistory(Context context,int Id)
{
    ArrayList<TrackObject> listTracks = loadListTracks(context);

    for(int j=0 ; j<listTracks.size();j++)
    {
        if(listTracks.get(j).getId()== Id)
        {
            listTracks.get(j).setListen(true);
            break;
        }
    }
    storeListTracks(context,listTracks);
}
    public void removeFromHistory(Context context,int Id)
    {
        ArrayList<TrackObject> listTracks = loadListTracks(context);

        for(int j=0 ; j<listTracks.size();j++)
        {
            if(listTracks.get(j).getId()== Id)
            {
                listTracks.get(j).setListen(false);
                break;
            }
        }
        storeListTracks(context,listTracks);
    }
    //endregion

   /* public String addTrack(Context context, TrackSerializableObject myModel)
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
    }*/
    //endregion


    // region shared preference for List Partners

    public void storeListPartners(Context context, List listPartners)
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
        String jsonFavorites = sExposeGson.toJson(listPartners);
        editor.putString(ArrayListPartners + Suffix, jsonFavorites);
        editor.commit();
    }

    public ArrayList<PartnerObject> loadListPartners(Context context)
    {
        // used for retrieving arraylist from json formatted string
        SharedPreferences settings;
        ArrayList<PartnerObject> listPartners;
        settings = context.getSharedPreferences(PREFS_NAME + Suffix, Context.MODE_PRIVATE); ////ask marwa
        if (settings.contains(ArrayListPartners + Suffix)) {
            String jsonPlayNowTracks = settings.getString(ArrayListPartners + Suffix, null);

            GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
            builder.excludeFieldsWithoutExposeAnnotation();
            Gson sExposeGson = builder.create();
            PartnerObject[] partnerItems = sExposeGson.fromJson(jsonPlayNowTracks, PartnerObject[].class);

            listPartners = new ArrayList( Arrays.asList(partnerItems));
        } else
            return new ArrayList<>();
        return listPartners;
    }

   /* public String addTrack(Context context, TrackSerializableObject myModel)
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
    }*/
    //endregion

    // region shared preference for List Tracks

    public void storeUsetList(Context context, List userList)
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
    String jsonFavorites = sExposeGson.toJson(userList);
    editor.putString(ArrayUserList + Suffix, jsonFavorites);
    editor.commit();
}

    public ArrayList<UserListObject> loadUserList(Context context)
    {
        // used for retrieving arraylist from json formatted string
        SharedPreferences settings;
        ArrayList<UserListObject> userList;
        settings = context.getSharedPreferences(PREFS_NAME + Suffix, Context.MODE_PRIVATE);
        if (settings.contains(ArrayUserList + Suffix)) {
            String jsonPlayNowTracks = settings.getString(ArrayUserList + Suffix, null);

            GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
            builder.excludeFieldsWithoutExposeAnnotation();
            Gson sExposeGson = builder.create();
            UserListObject[] userlistItems = sExposeGson.fromJson(jsonPlayNowTracks, UserListObject[].class);

            userList = new ArrayList( Arrays.asList(userlistItems));
        } else
            return new ArrayList<>();
        return userList;
    }
    //endregion

    // region shared preference for Notification

    public void storeNotificationtList(Context context, List notificationList)
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
        String jsonFavorites = sExposeGson.toJson(notificationList);
        editor.putString(ArrayNotificationList + Suffix, jsonFavorites);
        editor.commit();
    }

    public ArrayList<NotificationTypeObject> loadNotificationtList(Context context)
    {
        // used for retrieving arraylist from json formatted string
        SharedPreferences settings;
        ArrayList<NotificationTypeObject> notificationList;
        settings = context.getSharedPreferences(PREFS_NAME + Suffix, Context.MODE_PRIVATE);
        if (settings.contains(ArrayNotificationList + Suffix)) {
            String jsonPlayNowTracks = settings.getString(ArrayNotificationList + Suffix, null);

            GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
            builder.excludeFieldsWithoutExposeAnnotation();
            Gson sExposeGson = builder.create();
            NotificationTypeObject[] notificationlistItems = sExposeGson.fromJson(jsonPlayNowTracks, NotificationTypeObject[].class);

            notificationList = new ArrayList( Arrays.asList(notificationlistItems));
        } else
            return new ArrayList<>();
        return notificationList;
    }
    //endregion

    public void storeAboutUs(Context context, String aboutUstxt)
    {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(AboutUs + Suffix, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(AboutUs + Suffix, aboutUstxt);
        editor.commit();
    }

    public String loadAboutUs(Context context)
    {
        SharedPreferences settings;
        String aboutUs = "";
        settings = context.getSharedPreferences(AboutUs+ Suffix, Context.MODE_PRIVATE);
        if (settings.contains(AboutUs+ Suffix))
        {
            aboutUs = settings.getString(AboutUs+ Suffix, null);
        }
        else
            aboutUs = "";

        return aboutUs;
    }
    // region number of minutes storage

    public void storenumberOfMinutes(Context context, int numberOfMinutes)
    {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(NumberOfMinutes, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putInt(NumberOfMinutes, numberOfMinutes);
        editor.commit();
    }

    public int getnumberOfMinutes(Context context)
    {
        SharedPreferences settings;
        int numberOfMinutes = 0;
        settings = context.getSharedPreferences(NumberOfMinutes, Context.MODE_PRIVATE);
        if (settings.contains(NumberOfMinutes))
        {
            numberOfMinutes = settings.getInt(NumberOfMinutes, 0);
        }
        else
            numberOfMinutes = 0;

        return numberOfMinutes;
    }
    // endregion


    // region shared preference for List Main Topic

    public void storeListMainTopicFilter(Context context, List listMainTopic)
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
        String jsonFavorites = sExposeGson.toJson(listMainTopic);
        editor.putString(ArrayListMainTopic + Suffix, jsonFavorites);
        editor.commit();
    }

    public ArrayList<MainTopic> loadListMainTopicFilter(Context context)
    {
        // used for retrieving arraylist from json formatted string
        SharedPreferences settings;
        ArrayList<MainTopic> listMainTopic;
        settings = context.getSharedPreferences(PREFS_NAME + Suffix, Context.MODE_PRIVATE); ////ask marwa
        if (settings.contains(ArrayListMainTopic + Suffix)) {
            String jsonPlayNowTracks = settings.getString(ArrayListMainTopic + Suffix, null);

            GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
            builder.excludeFieldsWithoutExposeAnnotation();
            Gson sExposeGson = builder.create();
            MainTopic[] mainTopicItems = sExposeGson.fromJson(jsonPlayNowTracks, MainTopic[].class);

            listMainTopic = new ArrayList( Arrays.asList(mainTopicItems));
        } else
            return new ArrayList<>();
        return listMainTopic;
    }

    //endregion

// region shared preference for List Subscriped Partners

    public void storeListSubscripedPartnerFilter(Context context, List listSubscripedPartner)
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
        String jsonFavorites = sExposeGson.toJson(listSubscripedPartner);
        editor.putString(ArrayListSubscripedPartner + Suffix, jsonFavorites);
        editor.commit();
    }

    public ArrayList<SubscipedPartnersObject> loadListSubscripedPartnerFilter(Context context)
    {
        // used for retrieving arraylist from json formatted string
        SharedPreferences settings;
        ArrayList<SubscipedPartnersObject> listSubscripedPartner;
        settings = context.getSharedPreferences(PREFS_NAME + Suffix, Context.MODE_PRIVATE); ////ask marwa
        if (settings.contains(ArrayListSubscripedPartner + Suffix)) {
            String jsonPlayNowTracks = settings.getString(ArrayListSubscripedPartner + Suffix, null);

            GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
            builder.excludeFieldsWithoutExposeAnnotation();
            Gson sExposeGson = builder.create();
            SubscipedPartnersObject[] subscripedPartnerItems = sExposeGson.fromJson(jsonPlayNowTracks, SubscipedPartnersObject[].class);

            listSubscripedPartner = new ArrayList( Arrays.asList(subscripedPartnerItems));
        } else
            return new ArrayList<>();
        return listSubscripedPartner;
    }

    //endregion



}
