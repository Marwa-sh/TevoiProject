package com.tevoi.tevoi.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.tevoi.tevoi.R;
import com.tevoi.tevoi.SideMenu;
import com.tevoi.tevoi.model.PartnerObject;
import com.tevoi.tevoi.model.TrackObject;
import com.tevoi.tevoi.model.TrackSerializableObject;
import com.tevoi.tevoi.model.UserListObject;

import java.util.ArrayList;
import java.util.List;

public class HelperFunctions
{
    public static List<TrackObject> getPage(List<TrackObject> lst , int index, int size)
    {
        int currentIndex = (index * size);
        if( currentIndex + size > lst.size())
            return lst.subList(index, lst.size());
        else
            return lst.subList(currentIndex, currentIndex + size);
    }
    public static List<PartnerObject> getPagePartners(List<PartnerObject> lst , int index, int size)
    {
        int currentIndex = (index * size);
        List<PartnerObject> l = new ArrayList<>();
        if(lst.size() == 0)
            return l;
        if(lst.size() == 1)
            return  lst;
        if( currentIndex + size > lst.size())
        {
            if(currentIndex > lst.size()-1)
                return  l;
            else
                return lst.subList(currentIndex, lst.size()-1);
        }
        else
            return lst.subList(currentIndex, currentIndex + size);
    }

    public static List<UserListObject> getPageUserList(List<UserListObject> lst , int index, int size)
    {
        int currentIndex = (index * size);
        List<UserListObject> l = new ArrayList<>();
        if(lst.size() == 0)
            return l;
        if( currentIndex + size > lst.size())
            return lst.subList(index, lst.size()-1);
        else
            return lst.subList(currentIndex, currentIndex + size);
    }


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
        int listSize = activity.lstTracks.size();
        if(listSize == 0)
        {
            Toast.makeText(activity, R.string.play_now_list_isempty, Toast.LENGTH_SHORT).show();
        }
        else
        {
                //activity.isPlayingFromPlayNowList = true;
                activity.indexCurrentTrackInPlayList --;
                if(activity.indexCurrentTrackInPlayList < 0)
                    activity.indexCurrentTrackInPlayList = listSize -1;

                if(activity.indexCurrentTrackInPlayList >= 0 )
                {
                    TrackObject t = activity.lstTracks.get(activity.indexCurrentTrackInPlayList);
                    activity.mediaPlayerFragment.currentTrack = t;
                    activity.CurrentTrackInPlayer = activity.mediaPlayerFragment.currentTrack;
                    activity.mediaPlayerFragment.url = Global.BASE_AUDIO_URL + activity.lstTracks.get(activity.indexCurrentTrackInPlayList).getId();
                    activity.playAudio(activity.mediaPlayerFragment.url,
                            activity.CurrentTrackInPlayer.getName(),
                            activity.CurrentTrackInPlayer.getAuthors(),
                            activity.CurrentTrackInPlayer.getId());
                }
                else
                {
                    Toast.makeText(activity, R.string.no_track_to_play, Toast.LENGTH_SHORT).show();
                }
        }
    }

    public  static  void  getNextTrack(SideMenu activity, int currentTrackId)
    {
        int listSize = activity.lstTracks.size();
        if(listSize == 0)
        {
            Toast.makeText(activity, R.string.play_now_list_isempty, Toast.LENGTH_SHORT).show();
        }
        else
        {
                //activity.isPlayingFromPlayNowList = true;
                activity.indexCurrentTrackInPlayList ++;
                if(activity.indexCurrentTrackInPlayList >= listSize)
                    activity.indexCurrentTrackInPlayList = 0;

                if(activity.indexCurrentTrackInPlayList < listSize )
                {
                    TrackObject t = activity.lstTracks.get(activity.indexCurrentTrackInPlayList);
                    activity.mediaPlayerFragment.currentTrack = t;
                    activity.CurrentTrackInPlayer = activity.mediaPlayerFragment.currentTrack;
                    activity.mediaPlayerFragment.url = Global.BASE_AUDIO_URL + activity.lstTracks.get(activity.indexCurrentTrackInPlayList).getId();
                    activity.playAudio(activity.mediaPlayerFragment.url,
                            activity.CurrentTrackInPlayer.getName(),
                            activity.CurrentTrackInPlayer.getAuthors(),
                            activity.CurrentTrackInPlayer.getId());
                    //if(activity.isPlaying && !activity.isPaused)
                    //    playAudio(activity.mediaPlayerFragment.url, activity);
                }
                else
                {
                    Toast.makeText(activity, R.string.no_track_to_play, Toast.LENGTH_SHORT).show();
                }
                //activity.player.updateStatusBarInfo(activity.CurrentTrackInPlayer.getName(),activity.CurrentTrackInPlayer.getAuthors());
        }
    }

   /* public  static  void getPreviousTrack(SideMenu activity, int currentTrackId)
    {
        int listSize = activity.playNowListTracks.size();
        if(listSize == 0)
        {
            Toast.makeText(activity, R.string.play_now_list_isempty, Toast.LENGTH_SHORT).show();
        }
        else
        {
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
                activity.playAudio(activity.mediaPlayerFragment.url,
                        activity.CurrentTrackInPlayer.getName(),
                        activity.CurrentTrackInPlayer.getAuthors(),
                        activity.CurrentTrackInPlayer.getId());

                //if(activity.isPlaying && !activity.isPaused)
                //    activity.player.playAudio(activity.mediaPlayerFragment.url, activity);
            }
            else
            {
                Toast.makeText(activity, R.string.no_track_to_play, Toast.LENGTH_SHORT).show();
            }


        }
    }

    public  static  void  getNextTrack(SideMenu activity, int currentTrackId)
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
                activity.playAudio(activity.mediaPlayerFragment.url,
                        activity.CurrentTrackInPlayer.getName(),
                        activity.CurrentTrackInPlayer.getAuthors(),
                        activity.CurrentTrackInPlayer.getId());
                //if(activity.isPlaying && !activity.isPaused)
                //    playAudio(activity.mediaPlayerFragment.url, activity);
            }
            else
            {
                Toast.makeText(activity, R.string.no_track_to_play, Toast.LENGTH_SHORT).show();
            }
            //activity.player.updateStatusBarInfo(activity.CurrentTrackInPlayer.getName(),activity.CurrentTrackInPlayer.getAuthors());
        }
    }*/



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


    /*public static boolean isNetworkConnected(SideMenu activity)
    {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    public static boolean isNetworkConnected(LoginActivity activity)
    {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }*/
    public static boolean isNetworkConnected(AppCompatActivity activity)
    {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


}
