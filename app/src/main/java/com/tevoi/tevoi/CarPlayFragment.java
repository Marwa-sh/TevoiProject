package com.tevoi.tevoi;

import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.Utils.HelperFunctions;
import com.tevoi.tevoi.model.TrackObject;

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


        imgBtnClose = rootView.findViewById(R.id.imgBtnCloseCarPlayer);
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
                activity.mediaPlayerFragment.PreviousFragment= Global.CarPlayFragment;
                ft.replace(R.id.content_frame, activity.mediaPlayerFragment);
                activity.BackBtnAction();
                // or ft.add(R.id.your_placeholder, new FooFragment());
                // Complete the changes added above
                ft.commit();
            }
        });
        seekBar = rootView.findViewById(R.id.seekBarCar);
        txtCurentTime = rootView.findViewById(R.id.txtcurrentTimeCar);
        txtFullTime = rootView.findViewById(R.id.txtfullTimeCar);
        txtPartnerName = rootView.findViewById(R.id.txtPartnerNameCar);
        imgPartnerLogo = rootView.findViewById(R.id.imgPartnerLogoCar);

        imgBtnPlay = rootView.findViewById((R.id.imgBtnPlayCar));
        imgBtnPrevious = rootView.findViewById((R.id.imgBtnCarPrevious));
        imgBtnNext = rootView.findViewById((R.id.imgBtnCarNext));
        imgBtnShuffle = rootView.findViewById((R.id.imgBtnCarShuffle));

        if(Global.DefaultUILanguage.equals("ar")) {
            imgBtnPlay.setScaleX(-1);
            imgBtnPrevious.setScaleX(-1);
            imgBtnNext.setScaleX(-1);
            imgBtnShuffle.setScaleX(-1);
        }

        final SideMenu activity = (SideMenu) getActivity();
        //  INITIATE URL
        url = Global.BASE_AUDIO_URL + activity.mediaPlayerFragment.currentTrack.getId();
        TrackObject currentTrack = activity.mediaPlayerFragment.currentTrack;
        if(currentTrack!= null)
        {
            txtTrackName = rootView.findViewById(R.id.txtTrackNameCar);
            txtTrackName.setText(currentTrack.getName());
            txtPartnerName.setText(currentTrack.getPartnerName());
            //imgPartnerLogo.setImageURI(Uri.parse(currentTrack.getPartnerLogo()));
        }

        if(activity.IsListenDailyLimitsExceeded)
        {
            Toast.makeText(activity, R.string.no_quota_for_today, Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (activity.serviceBound)
            {
                if (activity.player != null) {
                    int mCurrentPosition = activity.player.mMediaPlayer.getCurrentPosition() / 1000;
                    activity.numberOfListenedSeconds = mCurrentPosition;
                    seekBar.setProgress(mCurrentPosition);
                    String timeFormat = HelperFunctions.GetTimeFormat(mCurrentPosition);
                    txtCurentTime.setText(timeFormat);
                    int duration = activity.player.mMediaPlayer.getDuration() / 1000;
                    seekBar.setMax(duration);
                    String timeFormatFull = HelperFunctions.GetTimeFormat(duration);
                    txtFullTime.setText(timeFormatFull);
                    if (activity.serviceBound) {
                        if (activity.player.mMediaPlayer.isPlaying()) {
                            imgBtnPlay.setImageResource(R.mipmap.pause_normal_white);
                        } else {
                            imgBtnPlay.setImageResource(R.mipmap.play_white_normal);
                        }
                    } else {
                        activity.playAudio(url,
                                activity.CurrentTrackInPlayer.getName(),
                                activity.CurrentTrackInPlayer.getAuthors(),
                                activity.CurrentTrackInPlayer.getId());
                        activity.isPlaying = true;
                        imgBtnPlay.setImageResource(R.mipmap.pause_normal_white);
                    }
                } else {
                    // player is null
                }
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
                    int numberofMovedSeconds = progress - activity.numberOfCurrentSecondsInTrack;
                    activity.numberOfCurrentSecondsInTrack = progress;
                    activity.numberOfListenedSeconds += numberofMovedSeconds;
                    activity.player.numberOfListenedSeconds += numberofMovedSeconds;
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
                if(activity.IsListenDailyLimitsExceeded)
                {
                    Toast.makeText(activity, R.string.no_quota_for_today, Toast.LENGTH_SHORT).show();
                }
                else {
                    if (activity.serviceBound) {
                        seekBar.setMax(activity.player.mMediaPlayer.getDuration() / 1000);
                        String timeFormat = HelperFunctions.GetTimeFormat(activity.player.mMediaPlayer.getDuration() / 1000);
                        txtFullTime.setText(timeFormat);

                        if (activity.player.mMediaPlayer.isPlaying()) {
                            activity.player.mMediaPlayer.pause();
                            activity.player.buildNotification(CustomMediaPlayerService.PlaybackStatus.PAUSED);
                            imgBtnPlay.setImageResource(R.mipmap.play_white_normal);
                        } else {
                            activity.player.mMediaPlayer.start();
                            activity.player.buildNotification(CustomMediaPlayerService.PlaybackStatus.PLAYING);
                            imgBtnPlay.setImageResource(R.mipmap.pause_normal_white);
                        }
                    } else {
                        activity.playAudio(url,
                                activity.CurrentTrackInPlayer.getName(),
                                activity.CurrentTrackInPlayer.getAuthors(),
                                activity.CurrentTrackInPlayer.getId());
                        imgBtnPlay.setImageResource(R.mipmap.pause_normal_white);
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
                if(activity.player!= null)
                {
                    seekBar.setMax(activity.player.mMediaPlayer.getDuration()/ 1000);
                    String timeFormat = HelperFunctions.GetTimeFormat(activity.player.mMediaPlayer.getDuration()/ 1000);
                    txtFullTime.setText(timeFormat);
                    activity.isPlaying = true; activity.isPaused = false;
                    imgBtnPlay.setImageResource(R.mipmap.pause_normal_white);
                }
                refreshCurrentTrackInfo(activity);
                //activity.mediaPlayerFragment.refreshCurrentTrackInfo();
            }
        });
        imgBtnNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SideMenu activity =  (SideMenu) getContext();
                HelperFunctions.getNextTrack(activity, activity.mediaPlayerFragment.currentTrack.getId());
                if(activity.player!= null)
                {
                    seekBar.setMax(activity.player.mMediaPlayer.getDuration()/ 1000);
                    String timeFormat = HelperFunctions.GetTimeFormat(activity.player.mMediaPlayer.getDuration()/ 1000);
                    txtFullTime.setText(timeFormat);
                    activity.isPlaying = true; activity.isPaused = false;
                    imgBtnPlay.setImageResource(R.mipmap.pause_normal_white);
                }
                refreshCurrentTrackInfo(activity);
                //activity.mediaPlayerFragment.refreshCurrentTrackInfo();
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
            public void run()
            {
                final SideMenu activity = (SideMenu)getActivity();
                if(activity != null)
                {
                    /*boolean isForground = activity.isAppInForeground(activity, "ComponentInfo{com.ebridge.tevoi/com.ebridge.tevoi.SideMenu}");
                    if(isForground==true){
                        // Toast.makeText(getBaseContext(),"Activity is in foreground, active",1000).show();
                        activity.isActivityPause = false;
                    }
                    else
                    {
                        activity.isActivityPause = true;
                    }*/

                    if (!activity.isActivityPause) {
                        if (activity.serviceBound)
                        {
                            //Toast.makeText(activity, "maroosh", Toast.LENGTH_SHORT).show();

                       /* if(seekBar == null)
                            seekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
*/
                            if(activity.player.mMediaPlayer.isPlaying()) {
                                imgBtnPlay.setImageResource(R.mipmap.pause_normal_white);
                            }
                            else {
                                imgBtnPlay.setImageResource(R.mipmap.play_white_normal);
                            }
                            seekBar.setMax(activity.player.mMediaPlayer.getDuration() / 1000);
                            String timeFormat2 = HelperFunctions.GetTimeFormat(activity.player.mMediaPlayer.getDuration() / 1000);
                            txtFullTime.setText(timeFormat2);
                            //Toast.makeText(activity, timeFormat2, Toast.LENGTH_SHORT).show();
                            //player = activity.player;
                            int mCurrentPosition = activity.player.mMediaPlayer.getCurrentPosition() / 1000;

                            seekBar.setProgress(mCurrentPosition);
                            String timeFormat = HelperFunctions.GetTimeFormat(mCurrentPosition);
                            txtCurentTime.setText(timeFormat);
                        } else {
                            imgBtnPlay.setImageResource(R.mipmap.play_white_normal);
                        }
                        mHandler.postDelayed(this, 1000);
                    }
                }
                /*SideMenu activity = (SideMenu)getActivity();
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
                }*/
            }
        });


        return rootView;
    }



    public void refreshCurrentTrackInfo(SideMenu activity)
    {
        activity.mediaPlayerFragment.hasLocation = activity.mediaPlayerFragment.currentTrack.isHasLocation();
        activity.mediaPlayerFragment.hasText = activity.mediaPlayerFragment.currentTrack.isHasText();
        txtTrackName.setText(activity.mediaPlayerFragment.currentTrack.getName());
        txtPartnerName.setText(activity.mediaPlayerFragment.currentTrack.getPartnerName());
        //String ulrLogo = Uri.parse(currentTrack.getPartnerLogo());
        //partnerLogo.setImageURI(ulrLogo);
    }
}
