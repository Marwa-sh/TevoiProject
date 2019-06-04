package com.tevoi.tevoi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tevoi.tevoi.Utils.CommentFragment;
import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.Utils.HelperFunctions;
import com.tevoi.tevoi.model.IResponse;
import com.tevoi.tevoi.model.RatingResponse;
import com.tevoi.tevoi.model.TrackObject;
import com.tevoi.tevoi.model.XmlFragementClickable;

import java.net.URL;

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
    public FragmentManager fm ;

    TextView trackName;
    TextView trackDuration;
    TextView trackCategories;
    TextView trackAuthors;
    TextView currentTime;
    TextView fullTime;
    RatingBar ratingBar;
    public ScrollView scrollViewMediaPlayer;
    public LinearLayout linearLayoutMediaPlayer;
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

    boolean ratingEnabled=false;

    public String PreviousFragment = "";

    View rootView;
    private Handler mHandler = new Handler();
    //Make sure you update Seekbar on UI thread


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.activity_media_player, container, false);
        final SideMenu activity = (SideMenu)getActivity();
        activity.numberOfCurrentSecondsInTrack =0;

        fm = getActivity().getSupportFragmentManager();
        scrollViewMediaPlayer = rootView.findViewById(R.id.scrollViewMediaPlayer);
        linearLayoutMediaPlayer = rootView.findViewById(R.id.linearLayoutMediaPlayer);

        playButton = rootView.findViewById(R.id.imageButtonPlay);
        seekBar = rootView.findViewById(R.id.seekBar);
        currentTime = rootView.findViewById(R.id.currentTime);
        fullTime = rootView.findViewById(R.id.fullTime);
        currentTime.setText(GetTimeFormat(0));
        ratingBar = rootView.findViewById(R.id.ratingBar);

        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                // Toast.makeText(activity, "hi there", Toast.LENGTH_SHORT).show();
                /*final SideMenu activity = (SideMenu)getActivity();

                boolean isForground = activity.isAppInForeground(activity, "ComponentInfo{com.ebridge.tevoi/com.ebridge.tevoi.SideMenu}");
                if(isForground==true){
                    // Toast.makeText(getBaseContext(),"Activity is in foreground, active",1000).show();
                    activity.isActivityPause = false;
                }
                else
                {
                    activity.isActivityPause = true;
                }
*/
                if(activity != null)
                {
                    if (!activity.isActivityPause)
                    {
                        if (activity.serviceBound && activity.player != null && activity.player.mMediaPlayer != null)
                        {
                            if (seekBar == null)
                                seekBar = rootView.findViewById(R.id.seekBar);

                            seekBar.setMax(activity.player.mMediaPlayer.getDuration() / 1000);
                            String timeFormat2 = GetTimeFormat(activity.player.mMediaPlayer.getDuration() / 1000);
                            fullTime.setText(timeFormat2);

                            int mCurrentPosition = activity.player.mMediaPlayer.getCurrentPosition() / 1000;

                            seekBar.setProgress(mCurrentPosition);
                            String timeFormat = GetTimeFormat(mCurrentPosition);
                            currentTime.setText(timeFormat);

                            if(activity.player.mMediaPlayer.isPlaying())
                            {
                                playButton.setImageResource(R.drawable.baseline_pause_24);
                            }
                            else
                            {
                                playButton.setImageResource(R.drawable.baseline_play_arrow_24);
                            }

                        } else {
                            playButton.setImageResource(R.drawable.baseline_play_arrow_24);
                        }
                        mHandler.postDelayed(this, 1000);
                    }
                }
            }
        });
        if(activity.serviceBound)
        {
            if(seekBar == null)
                seekBar = rootView.findViewById(R.id.seekBar);

            int duration = activity.player.mMediaPlayer.getDuration()/ 1000;
            seekBar.setMax(duration);
            String timeFormat = GetTimeFormat(activity.player.mMediaPlayer.getDuration());
            fullTime.setText(timeFormat);
        }

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(!ratingEnabled)
                    return;
                int Rating  = (int)ratingBar.getRating();
                //Toast.makeText(activity, ""+Rating, Toast.LENGTH_SHORT).show();
                Call<IResponse> call=Global.client.SetTrackRating(currentTrack.getId(),Rating);
                call.enqueue(new Callback<IResponse>() {
                    @Override
                    public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                        IResponse rating = response.body();

                    }
                    @Override
                    public void onFailure(Call<IResponse> call, Throwable t) {
                    }
                });
            }
        });

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
                if(activity!= null) {
                    if (activity.serviceBound && fromUser) {
                        // TODO  : check user quota

                        activity.player.mMediaPlayer.seekTo(progress * 1000);
                        String timeFormat = GetTimeFormat(progress);
                        currentTime.setText(timeFormat);
                        int numberofMovedSeconds = progress - activity.numberOfCurrentSecondsInTrack;
                        activity.numberOfCurrentSecondsInTrack = progress;
                        activity.numberOfListenedSeconds += numberofMovedSeconds;
                        //Toast.makeText(activity, "numberofMovedSeconds=" + numberofMovedSeconds, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        if(currentTrack != null)
        {
            hasLocation = currentTrack.isHasLocation();
            hasText = currentTrack.isHasText();
            trackName = rootView.findViewById(R.id.textViewTrackName);
            trackName.setText(currentTrack.getName());

            trackDuration = rootView.findViewById(R.id.textViewDuration);
            trackDuration.setText(currentTrack.getDuration());

            trackCategories = rootView.findViewById(R.id.textViewCategories);
            trackCategories.setText(currentTrack.getCategories());

            trackAuthors = rootView.findViewById(R.id.textViewAuthors);
            trackAuthors.setText(currentTrack.getAuthors());
            commentFragment = CommentFragment.newInstance(currentTrack.getId());
            textFargment = TrackText.newInstance(currentTrack.getId(), Global.MediaPlayerFragmentName);

            Call<RatingResponse> callRating=Global.client.GetTrackRating(currentTrack.getId());
            callRating.enqueue(new Callback<RatingResponse>() {
                @Override
                public void onResponse(Call<RatingResponse> call, Response<RatingResponse> response) {
                    RatingResponse res = response.body();
                    ratingBar.setRating((float)res.getRating());
                    ratingEnabled = true;
                    //Toast.makeText(activity, res.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<RatingResponse> call, Throwable t) {
                    ratingBar.setRating(0);
                    ratingEnabled = true;

                }
            });
            partnerName = rootView.findViewById(R.id.tv_partner_name);
            partnerLogo = rootView.findViewById(R.id.img_partner_logo);

            partnerName.setText(currentTrack.getPartnerName());
            try
            {
                URL newurl = new URL(Global.IMAGE_BASE_URL + currentTrack.getPartnerLogo());
                Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream());
                partnerLogo.setImageBitmap(mIcon_val);
            }
            catch (Exception exc)
            {

            }


            //String ulrLogo = Uri.parse(currentTrack.getPartnerLogo());
            //partnerLogo.setImageURI(ulrLogo);
            url = Global.BASE_AUDIO_URL + currentTrack.getId();
            if(!PreviousFragment.equals(Global.CarPlayFragment))
            {

            }
            if(activity.serviceBound)
            {
                if(activity.player.mMediaPlayer.isPlaying())
                {
                    playButton.setImageResource(R.drawable.baseline_pause_24);
                }
                else
                {
                    playButton.setImageResource(R.drawable.baseline_play_arrow_24);
                }
            }else
            {
                activity.playAudio(url);
                activity.isPlaying = true;
                playButton.setImageResource(R.drawable.baseline_pause_24);
            }
        }
        else
        {
            Toast.makeText(activity, "Track Info are invalid", Toast.LENGTH_SHORT).show();
            hasLocation = false; hasText = false;
        }

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
        if(activity.serviceBound)
        {
            if(activity.player.mMediaPlayer.isPlaying())
            {
                activity.player.mMediaPlayer.pause();
                playButton.setImageResource(R.drawable.baseline_play_arrow_24);
            }
            else
            {
                activity.player.mMediaPlayer.start();
                playButton.setImageResource(R.drawable.baseline_pause_24);
            }
        }
        else
        {
            activity.playAudio(url);
            playButton.setImageResource(R.drawable.baseline_pause_24);
        }
       /* if(!activity.isPlaying && !activity.isPaused)
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
        }*/
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

   /* private  void refreshMediaPlayStatus()
    {
        SideMenu activity = (SideMenu)getActivity();

        if(!activity.isPlaying && !activity.isPaused)
        {
            playButton.setImageResource(R.drawable.baseline_pause_24);
            activity.isPlaying = true;
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
    }*/

    // region media player actions
    public void imgBtnForwardClick(View view)
    {
        SideMenu activity = (SideMenu)getActivity();
        if(activity.serviceBound)
        {
            int max = activity.player.mMediaPlayer.getDuration();
            int mCurrentPosition = activity.player.mMediaPlayer.getCurrentPosition() / 1000;
            if(mCurrentPosition + 10 > max)
                mCurrentPosition = max;
            else
                mCurrentPosition +=10;
            numberOfListenedSeconds += 10;

            activity.numberOfCurrentSecondsInTrack = mCurrentPosition;
            activity.numberOfListenedSeconds += 10;

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
            activity.isPlaying = true; activity.isPaused = false;
            playButton.setImageResource(R.drawable.baseline_pause_24);
        }
        refreshCurrentTrackInfo();
    }

    public  void imgBtnNextClick(View view)
    {
        SideMenu activity =  (SideMenu) getContext();
        HelperFunctions.getNextTrack(activity, currentTrack.getId());
        if(activity.player != null)
        {
            seekBar.setMax(activity.player.mMediaPlayer.getDuration()/ 1000);
            String timeFormat = GetTimeFormat(activity.player.mMediaPlayer.getDuration()/ 1000);
            fullTime.setText(timeFormat);
            activity.isPlaying = true; activity.isPaused = false;
            playButton.setImageResource(R.drawable.baseline_pause_24);
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

                scrollViewMediaPlayer.smoothScrollBy(0,(int)(view.getY() + view.getHeight()));
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
            fm.executePendingTransactions();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Linearlayout is the layout the fragments are being added to.
                    View view = linearLayoutMediaPlayer.getChildAt(linearLayoutMediaPlayer.getChildCount()-1);

                    scrollViewMediaPlayer.smoothScrollBy(0,(int)(view.getY() + view.getHeight()));
                }
            });
        }
        else
        {
            Toast.makeText(getActivity(), R.string.track_has_no_location, Toast.LENGTH_LONG).show();
        }
    }

    public void imgBtnGetTrackTextClick(View view)
    {
        if(hasText) {
            SideMenu activity = (SideMenu) getActivity();

            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            TrackText textFargment = TrackText.newInstance(activity.CurrentTrackInPlayer.getId(), Global.MediaPlayerFragmentName);
            ft.replace(R.id.content_frame, textFargment);
            ft.addToBackStack( "TrackText" );
            // or ft.add(R.id.your_placeholder, new FooFragment());
            // Complete the changes added above
            ft.commit();

            /*FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.content_frame, textFargment);

            ft.commit();*/
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
        ft.addToBackStack( "TrackComment" );
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
        fm.executePendingTransactions();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Linearlayout is the layout the fragments are being added to.
                View view = linearLayoutMediaPlayer.getChildAt(linearLayoutMediaPlayer.getChildCount()-1);

                scrollViewMediaPlayer.smoothScrollBy(0,(int)(view.getY() + view.getHeight()));
            }
        });
    }

    public  void imgBtnCarClick(View view)
    {
        // Begin the transaction
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, carPlayFargment);
        ft.addToBackStack( "CarFargment" );
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
