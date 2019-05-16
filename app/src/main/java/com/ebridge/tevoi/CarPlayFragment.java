package com.ebridge.tevoi;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ebridge.tevoi.Utils.Global;
import com.ebridge.tevoi.Utils.HelperFunctions;
import com.ebridge.tevoi.Utils.HelperFunctions;
import com.ebridge.tevoi.model.TrackObject;

public class CarPlayFragment extends Fragment {

    String url= "http://192.168.1.100/TevoiAPIEmulator/api/Services/GetStreamAudio?id=";

    ImageButton imgBtnClose;
    TextView txtTrackName;
    ImageButton imgBtnPlay;
    ImageButton imgBtnPrevious;
    ImageButton imgBtnNext;
    ImageButton imgBtnShuffle;
    SeekBar seekBar;
    TextView txtCurentTime;
    TextView txtFullTime;
    TextView txtPartnerName;
    ImageView imgPartnerLogo;

    private Handler mHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_car_play, container, false);


        imgBtnClose = (ImageButton) rootView.findViewById(R.id.imgBtnCloseCarPlayer);
        imgBtnClose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Back to Media Player Fargment
                SideMenu activity = (SideMenu) getActivity();
                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                // Replace the contents of the container with the new fragment
                //TrackAddToList frag = new TrackAddToList();
                ft.replace(R.id.content_frame, activity.mediaPlayerFragment);
                // or ft.add(R.id.your_placeholder, new FooFragment());
                // Complete the changes added above
                ft.commit();
            }
        });
        seekBar = (SeekBar) rootView.findViewById(R.id.seekBarCar);
        txtCurentTime = (TextView) rootView.findViewById(R.id.txtcurrentTimeCar);
        txtFullTime = (TextView) rootView.findViewById(R.id.txtfullTimeCar);
        txtPartnerName = (TextView) rootView.findViewById(R.id.txtPartnerNameCar);
        imgPartnerLogo = (ImageView) rootView.findViewById(R.id.imgPartnerLogoCar);

        imgBtnPlay = (ImageButton) rootView.findViewById((R.id.imgBtnPlayCar));
        imgBtnPrevious = (ImageButton) rootView.findViewById((R.id.imgBtnCarPrevious));
        imgBtnNext = (ImageButton) rootView.findViewById((R.id.imgBtnCarNext));
        imgBtnShuffle = (ImageButton) rootView.findViewById((R.id.imgBtnCarShuffle));


        final SideMenu activity = (SideMenu) getActivity();
        //  INITIATE URL
        url = Global.BASE_AUDIO_URL + activity.mediaPlayerFragment.currentTrack.getId();
        TrackObject currentTrack = activity.mediaPlayerFragment.currentTrack;
        if(currentTrack!= null)
        {
            txtTrackName = (TextView) rootView.findViewById(R.id.txtTrackNameCar);
            txtTrackName.setText(currentTrack.getName());
            txtPartnerName.setText(currentTrack.getPartnerName());
            //imgPartnerLogo.setImageURI(Uri.parse(currentTrack.getPartnerLogo()));
        }

        if(activity.serviceBound)
        {
            if(activity.player != null)
            {
                int mCurrentPosition = activity.player.mMediaPlayer.getCurrentPosition() / 1000;
                activity.numberOfListenedSeconds = mCurrentPosition;
                seekBar.setProgress(mCurrentPosition);
                String timeFormat = HelperFunctions.GetTimeFormat(mCurrentPosition);
                txtCurentTime.setText(timeFormat);
                int duration = activity.player.mMediaPlayer.getDuration()/ 1000;
                seekBar.setMax(duration);
                String timeFormatFull = HelperFunctions.GetTimeFormat(duration);
                txtFullTime.setText(timeFormatFull);
                if(!activity.isPlaying && !activity.isPaused) {
                    imgBtnPlay.setImageResource(R.drawable.baseline_play_arrow_24);
                }
                else
                {
                    if(activity.isPlaying && !activity.isPaused) {
                        imgBtnPlay.setImageResource(R.drawable.baseline_pause_24);
                    }
                    else if(activity.isPaused)
                    {
                        imgBtnPlay.setImageResource(R.drawable.baseline_play_arrow_24);
                    }
                }
            }
            else
            {
                // player is null
            }
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
                    String timeFormat = HelperFunctions.GetTimeFormat(progress);
                    txtCurentTime.setText(timeFormat);
                }
            }
        });


        // region add actions on media player buttons

        imgBtnPlay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SideMenu activity = (SideMenu)getActivity();
                if(!activity.isPlaying && !activity.isPaused)
                {
                    imgBtnPlay.setImageResource(R.drawable.baseline_pause_24);
                    activity.isPlaying = true;
                    playAudio(url);
                    if(activity.player!= null)
                    {
                        seekBar.setMax(activity.player.mMediaPlayer.getDuration()/ 1000);
                        String timeFormat = HelperFunctions.GetTimeFormat(activity.player.mMediaPlayer.getDuration()/ 1000);
                        txtFullTime.setText(timeFormat);
                    }
                    else {
                        activity.player = activity.player;
                    }
                }
                else
                {
                    if(activity.player!= null) {
                        seekBar.setMax(activity.player.mMediaPlayer.getDuration()/ 1000);
                        String timeFormat = HelperFunctions.GetTimeFormat(activity.player.mMediaPlayer.getDuration()/ 1000);
                        txtFullTime.setText(timeFormat);
                    }
                    if(activity.isPlaying && !activity.isPaused) {
                        activity.player.mMediaPlayer.pause();
                        imgBtnPlay.setImageResource(R.drawable.baseline_play_arrow_24);
                        activity.isPlaying = false; activity.isPaused = true;
                    }
                    else if(activity.isPaused)
                    {
                        activity.player.mMediaPlayer.start();
                        imgBtnPlay.setImageResource(R.drawable.baseline_pause_24);
                        activity.isPlaying = true; activity.isPaused = false;
                    }
                }
            }
        });
        imgBtnPrevious.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SideMenu activity =  (SideMenu) getContext();
                HelperFunctions.getPreviousTrack(activity, activity.mediaPlayerFragment.currentTrack.getId());
                activity.mediaPlayerFragment.refreshCurrentTrackInfo();
            }
        });
        imgBtnNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SideMenu activity =  (SideMenu) getContext();
                HelperFunctions.getNextTrack(activity, activity.mediaPlayerFragment.currentTrack.getId());
                activity.mediaPlayerFragment.refreshCurrentTrackInfo();
            }
        });
        imgBtnShuffle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        // endregion

        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run() {
                SideMenu activity = (SideMenu)getActivity();
                if(activity != null)
                {
                    if(activity.serviceBound)
                    {
                        //player = activity.player;
                        int mCurrentPosition = activity.player.mMediaPlayer.getCurrentPosition() / 1000;
                        activity.numberOfListenedSeconds = mCurrentPosition;
                        seekBar.setProgress(mCurrentPosition);
                        String timeFormat = HelperFunctions.GetTimeFormat(mCurrentPosition);
                        txtCurentTime.setText(timeFormat);
                    }
                    mHandler.postDelayed(this, 1000);
                }
            }
        });


        return rootView;
    }

    private void playAudio(String media) {
        //Check is service is active
        SideMenu activity = (SideMenu)getActivity();
        if (!activity.serviceBound)
        {
            ServiceConnection serviceConnection = activity.serviceConnection;
            Intent playerIntent = new Intent(activity, MediaPlayerService.class);
            playerIntent.putExtra("media", media);
            getContext().startService(playerIntent);
            getContext().bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else
        {
            //Service is active
            //Send media with BroadcastReceiver
        }
    }
}
