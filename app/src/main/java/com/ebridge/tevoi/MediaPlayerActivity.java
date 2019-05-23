package com.ebridge.tevoi;

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
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ebridge.tevoi.Utils.CommentFragment;
import com.ebridge.tevoi.Utils.Global;
import com.ebridge.tevoi.adapter.TracksAdapter;
import com.ebridge.tevoi.model.IResponse;
import com.ebridge.tevoi.model.RatingRequest;
import com.ebridge.tevoi.model.RatingResponse;
import com.ebridge.tevoi.model.TrackObject;
import com.ebridge.tevoi.model.TrackResponseList;
import com.ebridge.tevoi.model.XmlFragementClickable;
import com.ebridge.tevoi.rest.ApiClient;
import com.ebridge.tevoi.rest.ApiInterface;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MediaPlayerActivity extends FragmentActivity {

    private MediaPlayerService player;
    boolean serviceBound = false;

    boolean isPlaying = false;
    boolean isPaused = false;

    String url= "http://h2817272.stratoserver.net/tevoi/Portals/0/Tevoi_Files/Audio/Track/3.mp3";//"http://192.168.1.105/TevoiAPI/api/Files/SoundFile?fileName=1.mp3";
    ImageButton playButton;
    private SeekBar seekBar;
    RatingBar ratingBar;

    XmlFragementClickable someFragment;
    FragmentManager fm = getSupportFragmentManager();

    TextView trackName;
    TextView trackCategories;
    TextView trackAuthors;
    TextView currentTime;
    TextView fullTime;

    TrackLocation locationFragment = new TrackLocation();
    TrackAddToList addToListFragment = new TrackAddToList();
    TrackShare trackShareFragment = new TrackShare();
    CommentFragment commentFragment = new CommentFragment();
    TrackText textFargment = new TrackText();
    CarPlayFragment carPlayFargment = new CarPlayFragment();

    public TrackObject currentTrack = new TrackObject();

    boolean hasLocation;
    boolean hasText;
    int numberOfListenedSeconds;

    boolean ratingEnabled=false;

    private Handler mHandler = new Handler();
    //Make sure you update Seekbar on UI thread


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;

            Toast.makeText(MediaPlayerActivity.this, "Service Bound", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        playButton = (ImageButton) findViewById(R.id.imageButtonPlay);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        currentTime = (TextView) findViewById(R.id.currentTime);
        fullTime = (TextView) findViewById(R.id.fullTime);
        ratingBar = findViewById(R.id.ratingBar);
        Call<RatingResponse> callRating=Global.client.GetTrackRating(currentTrack.getId());
        callRating.enqueue(new Callback<RatingResponse>() {
            @Override
            public void onResponse(Call<RatingResponse> call, Response<RatingResponse> response) {
                RatingResponse res = response.body();
                ratingBar.setRating((float)res.getRating());
                ratingEnabled = true;
            }

            @Override
            public void onFailure(Call<RatingResponse> call, Throwable t) {
                ratingBar.setRating(0);
                ratingEnabled = true;

            }
        });


        MediaPlayerActivity.this.runOnUiThread(new Runnable()
        {
            @Override
            public void run() {
                if(serviceBound){
                    int mCurrentPosition = player.mMediaPlayer.getCurrentPosition() / 1000;
                    numberOfListenedSeconds = mCurrentPosition;
                    seekBar.setProgress(mCurrentPosition);
                    String timeFormat = GetTimeFormat(mCurrentPosition);
                    currentTime.setText(timeFormat);
                }
                mHandler.postDelayed(this, 1000);
            }
        });
        if(serviceBound)
        {
            seekBar.setMax(player.mMediaPlayer.getDuration());
            String timeFormat = GetTimeFormat(player.mMediaPlayer.getDuration());
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
                if(serviceBound && fromUser){
                    player.mMediaPlayer.seekTo(progress * 1000);
                    String timeFormat = GetTimeFormat(progress);
                    currentTime.setText(timeFormat);
                }
            }
        });

        ApiInterface client = ApiClient.getClient().create(ApiInterface.class);
        Call<TrackObject> call = client.getTrackFullDetails(1);
        call.enqueue(new Callback<TrackObject>(){
            public void onResponse(Call<TrackObject> call, Response<TrackObject> response) {
                //generateDataList(response.body());
                TrackObject trackFullDetail = response.body();
                if(trackFullDetail != null)
                {
                    currentTrack = trackFullDetail;
                    url += currentTrack.getId();

                    hasLocation = trackFullDetail.isHasLocation();
                    hasText = trackFullDetail.isHasText();

                    commentFragment = CommentFragment.newInstance(currentTrack.getId());

                }
                else
                {
                    hasLocation = false;
                }
            }
            public void onFailure(Call<TrackObject> call, Throwable t)
            {
                currentTrack = new TrackObject();
            }
        });

        /*
        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment

        ft.replace(R.id.ActionsFragment, new com.ebridge.tevoi.TrackLocation());
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
        */
    }
    private void playAudio(String media) {
        //Check is service is active
        if (!serviceBound)
        {
            Intent playerIntent = new Intent(this, MediaPlayerService.class);
            playerIntent.putExtra("media", media);
            startService(playerIntent);
            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        } else
            {
            //Service is active
            //Send media with BroadcastReceiver
        }
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("ServiceState", serviceBound);
        super.onSaveInstanceState(savedInstanceState);
    }

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


    public void playTrack(View view) {

        if(!isPlaying && !isPaused)
        {
            playButton.setImageResource(R.drawable.baseline_pause_24);
            isPlaying = true;
            playAudio(url);
            if(player!= null) {
                seekBar.setMax(player.mMediaPlayer.getDuration()/ 1000);
                String timeFormat = GetTimeFormat(player.mMediaPlayer.getDuration()/ 1000);
                fullTime.setText(timeFormat);
            }
        }
        else
        {
            if(player!= null) {
                seekBar.setMax(player.mMediaPlayer.getDuration()/ 1000);
                String timeFormat = GetTimeFormat(player.mMediaPlayer.getDuration()/ 1000);
                fullTime.setText(timeFormat);
            }
            if(isPlaying && !isPaused) {
                player.mMediaPlayer.pause();
                playButton.setImageResource(R.drawable.baseline_play_arrow_24);
                isPlaying = false; isPaused = true;
            }
            else if(isPaused)
            {
                player.mMediaPlayer.start();
                playButton.setImageResource(R.drawable.baseline_pause_24);
                isPlaying = true; isPaused = false;
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
        if(serviceBound){
            int max = player.mMediaPlayer.getDuration();
            int mCurrentPosition = player.mMediaPlayer.getCurrentPosition() / 1000;
            if(mCurrentPosition + 10 > max)
                mCurrentPosition = max;
            else
                mCurrentPosition +=10;
            numberOfListenedSeconds += 10;
            seekBar.setProgress(mCurrentPosition);
            String timeFormat = GetTimeFormat(mCurrentPosition);
            currentTime.setText(timeFormat);
            player.mMediaPlayer.seekTo(mCurrentPosition*1000);
        }
    }
    public void imgBtnReplayClick(View view)
    {
        if(serviceBound){
            int max = player.mMediaPlayer.getDuration();
            int mCurrentPosition = player.mMediaPlayer.getCurrentPosition() / 1000;
            if(mCurrentPosition - 10 < 0)
                mCurrentPosition = 0;
            else
                mCurrentPosition -= 10;
            seekBar.setProgress(mCurrentPosition);
            String timeFormat = GetTimeFormat(mCurrentPosition);
            currentTime.setText(timeFormat);
            player.mMediaPlayer.seekTo(mCurrentPosition*1000);
        }
    }
    // endregion


    //region actions to buttons
    public  void imgBtnAddtoListClick(View view)
    {
        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        //TrackAddToList frag = new TrackAddToList();
        ft.replace(R.id.ActionsFragment, addToListFragment);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
    }

    public void imgBtnLocationClick(View view) {

        // CHECK IF THIS TRACK HAS LOCATION
        if(hasLocation)
        {
            // Begin the transaction
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // Replace the contents of the container with the new fragment
            //TrackLocation frag = new TrackLocation();
            ft.replace(R.id.ActionsFragment, locationFragment);
            // or ft.add(R.id.your_placeholder, new FooFragment());
            // Complete the changes added above
            ft.commit();
        }
        else
        {
            // Begin the transaction
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // Replace the contents of the container with the new fragment
            MyMessge frag = MyMessge.newInstance("Track has No Location");
            ft.replace(R.id.ActionsFragment, frag);
            // or ft.add(R.id.your_placeholder, new FooFragment());
            // Complete the changes added above
            ft.commit();
        }
    }

    public void imgBtnGetTrackTextClick(View view)
    {
        if(hasText) {

            //TrackText dFragment = new TrackText();
            // Show DialogFragment
            //textFargment.show(fm, "Text");
        }
    }

    public  void imgBtnCommentClick(View view)
    {
        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
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
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        //TrackShare frag = new TrackShare();
        ft.replace(R.id.ActionsFragment, trackShareFragment);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
    }

    public  void imgBtnCarClick(View view)
    {
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


    // endredion
}
