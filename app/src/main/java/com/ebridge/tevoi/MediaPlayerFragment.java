package com.ebridge.tevoi;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ebridge.tevoi.Utils.CommentFragment;
import com.ebridge.tevoi.Utils.Global;
import com.ebridge.tevoi.Utils.HelperFunctions;
import com.ebridge.tevoi.adapter.PartnerAdapter;
import com.ebridge.tevoi.adapter.Track;
import com.ebridge.tevoi.adapter.TracksAdapter;
import com.ebridge.tevoi.model.AudioDataSource;
import com.ebridge.tevoi.model.IResponse;
import com.ebridge.tevoi.model.PartnerListResponse;
import com.ebridge.tevoi.model.TrackObject;
import com.ebridge.tevoi.model.TrackResponseList;
import com.ebridge.tevoi.model.TrackSerializableObject;
import com.ebridge.tevoi.model.XmlFragementClickable;
import com.ebridge.tevoi.rest.ApiClient;
import com.ebridge.tevoi.rest.ApiInterface;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MediaPlayerFragment extends Fragment {

    //private MediaPlayerService player;
    //boolean serviceBound = false;

    //boolean isPlaying = false;
    //boolean isPaused = false;

    public String url= "http://192.168.1.100/TevoiAPIEmulator/api/Services/GetStreamAudio?id=";

            //"http://h2817272.stratoserver.net/tevoi/Portals/0/Tevoi_Files/Audio/Track/3.mp3";//"http://192.168.1.105/TevoiAPI/api/Files/SoundFile?fileName=1.mp3";
    ImageButton playButton;
    private SeekBar seekBar;

    XmlFragementClickable someFragment;
    FragmentManager fm ;

    TextView trackName;
    TextView trackDuration;
    TextView trackCategories;
    TextView trackAuthors;
    TextView currentTime;
    TextView fullTime;
    RatingBar ratingBar;
    ScrollView scrollViewMediaPlayer;
    LinearLayout linearLayoutMediaPlayer;
    TextView partnerName;
    ImageView partnerLogo;


    boolean initailizeFullTime = false;

    TrackLocation locationFragment = new TrackLocation();
    TrackAddToList addToListFragment = new TrackAddToList();
    TrackShare trackShareFragment = new TrackShare();
    CommentFragment commentFragment = new CommentFragment();
    TrackText textFargment;
    CarPlayFragment carPlayFargment = new CarPlayFragment();

    public int currentTrackId;
    public TrackObject currentTrack = new TrackObject();

    boolean hasLocation;
    boolean hasText;
    int numberOfListenedSeconds;

    View rootView;
    private Handler mHandler = new Handler();
    //Make sure you update Seekbar on UI thread


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.activity_media_player, container, false);
        final SideMenu activity = (SideMenu)getActivity();

        /*Call<IResponse> callUrlAudio = Global.client.AddListenTrackActivity(currentTrackId);
        callUrlAudio.enqueue(new Callback<IResponse>(){
            public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                IResponse reponse = response.body();
                // access response code with response.code()
                // access string of the response with response.body().string()
                if (response != null && response.body() != null) {
                }
            }
            public void onFailure(Call<IResponse> call, Throwable t)
            {
            }
        });*/

        //serviceBound = activity.serviceBound;
        //player = activity.player;

        fm = getActivity().getSupportFragmentManager();
        scrollViewMediaPlayer = (ScrollView) rootView.findViewById(R.id.scrollViewMediaPlayer);
        linearLayoutMediaPlayer = (LinearLayout) rootView.findViewById(R.id.linearLayoutMediaPlayer);

        playButton = (ImageButton) rootView.findViewById(R.id.imageButtonPlay);
        seekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
        currentTime = (TextView) rootView.findViewById(R.id.currentTime);
        fullTime = (TextView) rootView.findViewById(R.id.fullTime);
        currentTime.setText(GetTimeFormat(0));
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run() {
                final SideMenu activity = (SideMenu)getActivity();
                if(activity != null)
                {
                    if(activity.serviceBound)
                    {
                        if(activity.isPlaying)
                        {
                            activity.numberOfListenedSeconds += 1;
                            //activity.numberOfTotalSeconds += activity.numberOfCurrentSeconds;
                        }
                        int n = activity.numberOfUnitsSendToServer * Global.ListenUnitInSeconds + Global.ListenUnitInSeconds;
                        if(activity.numberOfListenedSeconds >= n)
                        {
                            // send to server that we used 1 unit
                            Call<IResponse> call = Global.client.AddUnitUsageForUser(currentTrack.getId());
                            call.enqueue(new Callback<IResponse>(){
                                public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                                    //generateDataList(response.body());
                                    IResponse partners=response.body();
                                    activity.numberOfUnitsSendToServer +=1;

                                    Toast.makeText(activity, "One Unit consumed from your quota", Toast.LENGTH_SHORT).show();
                                }
                                public void onFailure(Call<IResponse> call, Throwable t)
                                {

                                }
                            });
                        }
                        seekBar.setMax(activity.player.mMediaPlayer.getDuration()/ 1000);
                        String timeFormat2 = GetTimeFormat(activity.player.mMediaPlayer.getDuration()/ 1000);
                        fullTime.setText(timeFormat2);

                        //player = activity.player;
                        int mCurrentPosition = activity.player.mMediaPlayer.getCurrentPosition() / 1000;

                        seekBar.setProgress(mCurrentPosition);
                        String timeFormat = GetTimeFormat(mCurrentPosition);
                        currentTime.setText(timeFormat);
                    }
                    mHandler.postDelayed(this, 1000);
                }
            }
        });
        if(activity.serviceBound)
        {
            int duration = activity.player.mMediaPlayer.getDuration()/ 1000;
            seekBar.setMax(duration);
            String timeFormat = GetTimeFormat(activity.player.mMediaPlayer.getDuration());
            fullTime.setText(timeFormat);
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SideMenu activity = (SideMenu)getActivity();
                if(activity.serviceBound && fromUser){
                    activity.player.mMediaPlayer.seekTo(progress * 1000);
                    String timeFormat = GetTimeFormat(progress);
                    currentTime.setText(timeFormat);
                }
            }
        });
        if(currentTrackId == 0)
        {

        }
        if(currentTrack != null)
        {
            url = Global.BASE_AUDIO_URL + currentTrack.getId();

            hasLocation = currentTrack.isHasLocation();
            hasText = currentTrack.isHasText();
            trackName = (TextView) rootView.findViewById(R.id.textViewTrackName);
            trackName.setText(currentTrack.getName());

            trackDuration = (TextView) rootView.findViewById(R.id.textViewDuration);
            trackDuration.setText(currentTrack.getDuration());

            trackCategories = (TextView) rootView.findViewById(R.id.textViewCategories);
            trackCategories.setText(currentTrack.getCategories());

            trackAuthors = (TextView) rootView.findViewById(R.id.textViewAuthors);
            trackAuthors.setText(currentTrack.getAuthors());
            commentFragment = CommentFragment.newInstance(currentTrack.getId());
            textFargment = TrackText.newInstance(currentTrack.getId(), Global.MediaPlayerFragmentName);

            ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);
            ratingBar.setRating(currentTrack.getRate());

            partnerName = (TextView) rootView.findViewById(R.id.tv_partner_name);
            partnerLogo = (ImageView) rootView.findViewById(R.id.img_partner_logo);

            partnerName.setText(currentTrack.getPartnerName());
            //String ulrLogo = Uri.parse(currentTrack.getPartnerLogo());
            //partnerLogo.setImageURI(ulrLogo);

        }
        else
        {
            hasLocation = false;
        }
/*
        Call<ResponseBody> callUrlAudio = client.GetStreamAudio(currentTrackId);
        callUrlAudio.enqueue(new Callback<ResponseBody>(){
            @TargetApi(23)
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //generateDataList(response.body());
                ResponseBody reponse = response.body();
                // access response code with response.code()
                // access string of the response with response.body().string()
                if (response != null && response.body() != null) {
                    //response.body() will contains the downloaded file details
                    try
                    {
                        BufferedSource source = response.body().source();
                        source.request(Long.MAX_VALUE); // request the entire body.
                        Buffer buffer = source.buffer();
                        InputStream inputStream = new ByteArrayInputStream(buffer.readByteArray());
                        MediaPlayer.createPlayer(inputStream,"");

                        AudioDataSource ds = new AudioDataSource(inputStream);
                        MediaPlayer mp = new MediaPlayer();

                        mp.setDataSource(ds);
                        mp.start();
                       // activity.player.mMediaPlayer=mp;

                    }
                    catch (IOException exc)
                    {

                    }

                    //String Body = response.body().string();
                }

            }
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {

            }
        });
*/
        /*
        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment

        ft.replace(R.id.ActionsFragment, new com.ebridge.tevoi.TrackLocation());
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
        */

        return  rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        SideMenu activity = (SideMenu)getActivity();
        savedInstanceState.putBoolean("ServiceState", activity.serviceBound);
        super.onSaveInstanceState(savedInstanceState);
    }

    /*
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        serviceBound = savedInstanceState.getBoolean("ServiceState");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceBound) {
            unbindService(serviceConnection);
            //service is active
            player.stopSelf();
        }
    }
*/
    public void playTrack(View view)
    {
        SideMenu activity = (SideMenu)getActivity();
        if(!activity.isPlaying && !activity.isPaused)
        {
            playButton.setImageResource(R.drawable.baseline_pause_24);
            activity.isPlaying = true;
            activity.playAudio(url);
        }
        else
        {
            if(activity.isPlaying && !activity.isPaused)
            {
                activity.player.mMediaPlayer.pause();
                playButton.setImageResource(R.drawable.baseline_play_arrow_24);
                activity.isPlaying = false; activity.isPaused = true;
            }
            else if(activity.isPaused)
            {
                activity.player.mMediaPlayer.start();
                playButton.setImageResource(R.drawable.baseline_pause_24);
                activity.isPlaying = true; activity.isPaused = false;
            }
        }
    }

    /*
    public void onTextBtnClick(View view)
    {
        Toast.makeText(this,"hi",Toast.LENGTH_SHORT);
        Intent i = new Intent(this,MediaPlayerActivity.class);
        //i.putExtra("media","http://192.168.1.111/FileServer/api/Files/Get?fileName=1.mp3");
        startActivity(i);

    }
    */

    // region media player actions
    public void imgBtnForwardClick(View view)
    {
        SideMenu activity = (SideMenu)getActivity();
        if(activity.serviceBound){
            int max = activity.player.mMediaPlayer.getDuration();
            int mCurrentPosition = activity.player.mMediaPlayer.getCurrentPosition() / 1000;
            if(mCurrentPosition + 10 > max)
                mCurrentPosition = max;
            else
                mCurrentPosition +=10;
            numberOfListenedSeconds += 10;
            seekBar.setProgress(mCurrentPosition);
            String timeFormat = GetTimeFormat(mCurrentPosition);
            currentTime.setText(timeFormat);
            activity.player.mMediaPlayer.seekTo(mCurrentPosition*1000);
        }
    }
    public void imgBtnReplayClick(View view)
    {
        SideMenu activity = (SideMenu)getActivity();
        if(activity.serviceBound){
            int max = activity.player.mMediaPlayer.getDuration();
            int mCurrentPosition = activity.player.mMediaPlayer.getCurrentPosition() / 1000;
            if(mCurrentPosition - 10 < 0)
                mCurrentPosition = 0;
            else
                mCurrentPosition -= 10;
            seekBar.setProgress(mCurrentPosition);
            String timeFormat = GetTimeFormat(mCurrentPosition);
            currentTime.setText(timeFormat);
            activity.player.mMediaPlayer.seekTo(mCurrentPosition*1000);
        }
    }
    // endregion

    // region play next , previous and shuffle buttons actions

    public  void imgBtnPreviuosClick(View view)
    {
        SideMenu activity =  (SideMenu) getContext();
        HelperFunctions.getPreviousTrack(activity, currentTrack.getId());
        if(activity.player!= null)
        {
            seekBar.setMax(activity.player.mMediaPlayer.getDuration()/ 1000);
            String timeFormat = GetTimeFormat(activity.player.mMediaPlayer.getDuration()/ 1000);
            fullTime.setText(timeFormat);
        }
        refreshCurrentTrackInfo();
    }

    public  void imgBtnNextClick(View view)
    {
        SideMenu activity =  (SideMenu) getContext();
        HelperFunctions.getNextTrack(activity, currentTrack.getId());
        if(activity.player!= null)
        {
            seekBar.setMax(activity.player.mMediaPlayer.getDuration()/ 1000);
            String timeFormat = GetTimeFormat(activity.player.mMediaPlayer.getDuration()/ 1000);
            fullTime.setText(timeFormat);
        }
        refreshCurrentTrackInfo();
    }
    //endregion


    //region actions to buttons
    public  void imgBtnAddtoListClick(View view)
    {
        // Begin the transaction
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        //TrackAddToList frag = new TrackAddToList();
        ft.replace(R.id.ActionsFragment, addToListFragment);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
        fm.executePendingTransactions();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
            //Linearlayout is the layout the fragments are being added to.
                View view = linearLayoutMediaPlayer.getChildAt(linearLayoutMediaPlayer.getChildCount()-1);

                scrollViewMediaPlayer.scrollTo(0,(int)(view.getY() + view.getHeight()));
            }
        });

    }

    public void imgBtnLocationClick(View view) {

        // CHECK IF THIS TRACK HAS LOCATION
        if(hasLocation)
        {
            // Begin the transaction
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            // Replace the contents of the container with the new fragment
            //TrackLocation frag = new TrackLocation();
            ft.replace(R.id.ActionsFragment, locationFragment);
            // or ft.add(R.id.your_placeholder, new FooFragment());
            // Complete the changes added above
            ft.commit();
        }
        else
        {
            Toast.makeText(getActivity(), R.string.track_has_no_location, Toast.LENGTH_LONG).show();
        }
    }

    public void imgBtnGetTrackTextClick(View view)
    {
        if(hasText) {

            //TrackText dFragment = new TrackText();
            // Show DialogFragment
            //textFargment.show(fm, "Text");
            // Begin the transaction
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            // Replace the contents of the container with the new fragment
            //TrackLocation frag = new TrackLocation();
            ft.replace(R.id.content_frame, textFargment);
            // or ft.add(R.id.your_placeholder, new FooFragment());
            // Complete the changes added above
            ft.commit();
        }
        else
        {
            Toast.makeText(getActivity(), R.string.track_has_no_text, Toast.LENGTH_LONG).show();
        }
    }

    public  void imgBtnCommentClick(View view)
    {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        //TrackShare frag = new TrackShare();
        ft.replace(R.id.content_frame, commentFragment);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
        //CommentFragment dFragment = CommentFragment.newInstance(1);
        // Show DialogFragment
        //commentFragment.show(fm, "Dialog Fragment");

        //CustomCommentFragment alertdFragment = new CustomCommentFragment();
        // Show Alert DialogFragment
        //alertdFragment.show(fm, "Alert Dialog Fragment");

    }

    public  void  imgBtnShareClick(View view)
    {
        // Begin the transaction
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        //TrackShare frag = new TrackShare();
        ft.replace(R.id.ActionsFragment, trackShareFragment);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
    }

    public  void imgBtnCarClick(View view)
    {
        // Begin the transaction
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        //TrackShare frag = new TrackShare();
        ft.replace(R.id.content_frame, carPlayFargment);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
        //carPlayFargment.show(fm, "Dialog Fragment");
    }

    //endregion


    public void locationClick(View v)
    {
        locationFragment.locationClick(v);
    }
    public void shareClick(View v)
    {
        trackShareFragment.shareClick(v);
    }
    public void addTrackToListClick(View v)
    {
        addToListFragment.addTrackToListClick(v);
    }
    public  void addCommentBtn(View v)
    {
        commentFragment.addCommentClick(v);
    }

    // region helpers
    private  String GetTimeFormat(int pTime)
    {
        String result ="" ;
        final int min = pTime/60;
        final int sec = pTime-(min*60);

        final String strMin = placeZeroIfNeede(min);
        final String strSec = placeZeroIfNeede(sec);
        result =  String.format("%s:%s",strMin,strSec);

        return  result;
    }
    private String placeZeroIfNeede(int number) {
        return (number >=10)? Integer.toString(number):String.format("0%s",Integer.toString(number));
    }

    public void refreshCurrentTrackInfo()
    {
        hasLocation = currentTrack.isHasLocation();
        hasText = currentTrack.isHasText();
        trackName.setText(currentTrack.getName());
        trackDuration.setText(currentTrack.getDuration());
        trackCategories.setText(currentTrack.getCategories());
        trackAuthors.setText(currentTrack.getAuthors());
        //commentFragment = CommentFragment.newInstance(currentTrack.getId());
        ratingBar.setRating(currentTrack.getRate());
        partnerName.setText(currentTrack.getPartnerName());
        //String ulrLogo = Uri.parse(currentTrack.getPartnerLogo());
        //partnerLogo.setImageURI(ulrLogo);
    }
    // endregion
}
