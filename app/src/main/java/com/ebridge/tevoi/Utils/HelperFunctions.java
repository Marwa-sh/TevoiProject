package com.ebridge.tevoi.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.ebridge.tevoi.MediaPlayerService;
import com.ebridge.tevoi.R;
import com.ebridge.tevoi.SideMenu;
import com.ebridge.tevoi.adapter.TracksAdapter;
import com.ebridge.tevoi.model.IResponse;
import com.ebridge.tevoi.model.TrackObject;
import com.ebridge.tevoi.model.TrackResponseList;
import com.ebridge.tevoi.model.TrackSerializableObject;
import com.ebridge.tevoi.rest.ApiClient;
import com.ebridge.tevoi.rest.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelperFunctions
{
    public  static String GetTimeFormat(int pTime)
    {
        String result ="" ;
        final int min = pTime/60;
        final int sec = pTime-(min*60);

        final String strMin = placeZeroIfNeede(min);
        final String strSec = placeZeroIfNeede(sec);
        result =  String.format("%s:%s",strMin,strSec);

        return  result;
    }
    public static String placeZeroIfNeede(int number) {
        return (number >=10)? Integer.toString(number):String.format("0%s",Integer.toString(number));
    }

    public  static  void getPreviousTrack(SideMenu activity, int currentTrackId)
    {
        int listSize = activity.playNowListTracks.size();
        if(listSize == 0)
        {
            Toast.makeText(activity, R.string.play_now_list_isempty, Toast.LENGTH_SHORT).show();
        }
        else {

                activity.isPlayingFromPlayNowList = true;
                activity.indexCurrentTrackInPlayList --;
                if(activity.indexCurrentTrackInPlayList < 0)
                    activity.indexCurrentTrackInPlayList = listSize -1;

                if(activity.indexCurrentTrackInPlayList >= 0 )
                {
                    //resetMediaPlayer(activity);
                    // reset the media player
                    //if(activity.serviceBound)
                    //{
                    //    activity.player.resetMediaPlayer();
                    //}
                    //activity.serviceBound = false;
                    //activity.isPaused =false; activity.isPlaying = false;
                    //activity.serviceBound = false;

                    TrackSerializableObject t = activity.playNowListTracks.get(activity.indexCurrentTrackInPlayList);
                    activity.mediaPlayerFragment.currentTrack = CastTrackSerialize(t);
                    activity.CurrentTrackInPlayer = activity.mediaPlayerFragment.currentTrack;
                    activity.mediaPlayerFragment.url = Global.BASE_AUDIO_URL + activity.playNowListTracks.get(activity.indexCurrentTrackInPlayList).getId();
                    activity.playAudio(activity.mediaPlayerFragment.url);

                    //if(activity.isPlaying && !activity.isPaused)
                    //    activity.player.playAudio(activity.mediaPlayerFragment.url, activity);
                }
                else
                {
                    Toast.makeText(activity, R.string.no_track_to_play, Toast.LENGTH_SHORT).show();
                }


        }
    }

    public  static  void getNextTrack(SideMenu activity, int currentTrackId)
    {
        int listSize = activity.playNowListTracks.size();
        if(listSize == 0)
        {
            Toast.makeText(activity, R.string.play_now_list_isempty, Toast.LENGTH_SHORT).show();
        }
        else
        {
                activity.isPlayingFromPlayNowList = true;
                activity.indexCurrentTrackInPlayList ++;
                if(activity.indexCurrentTrackInPlayList >= listSize)
                    activity.indexCurrentTrackInPlayList = 0;

                if(activity.indexCurrentTrackInPlayList < listSize )
                {
                    TrackSerializableObject t = activity.playNowListTracks.get(activity.indexCurrentTrackInPlayList);
                    activity.mediaPlayerFragment.currentTrack = CastTrackSerialize(t);
                    activity.CurrentTrackInPlayer = activity.mediaPlayerFragment.currentTrack;
                    activity.mediaPlayerFragment.url = Global.BASE_AUDIO_URL + activity.playNowListTracks.get(activity.indexCurrentTrackInPlayList).getId();
                    activity.playAudio(activity.mediaPlayerFragment.url);
                    //if(activity.isPlaying && !activity.isPaused)
                    //    playAudio(activity.mediaPlayerFragment.url, activity);
                }
                else
                {
                    Toast.makeText(activity, R.string.no_track_to_play, Toast.LENGTH_SHORT).show();
                }
        }
    }

   /* public static void resetMediaPlayer(SideMenu activity)
    {
        // reset the media player
        if(activity.serviceBound)
            activity.player.resetMediaPlayer();
        activity.isPaused =false; activity.isPlaying = false;
        activity.serviceBound = false;
    }*/
    public static TrackObject CastTrackSerialize(TrackSerializableObject obj)
    {
        TrackObject result = new TrackObject();
        result.setId(obj.getId());
        result.setName(obj.getName());
        result.setCategories(obj.getCategories());
        result.setAuthors(obj.getAuthors());
        result.setRate(obj.getRate());
        result.setHasLocation(obj.isHasLocation());
        result.setHasText(obj.isHasText());
        result.setPartnerId(obj.getPartnerId());
        result.setPartnerName(obj.getPartnerName());
        result.setPartnerLogo(obj.getPartnerLogo());

        return result;
    }



}
