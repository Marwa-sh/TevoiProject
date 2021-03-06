package com.tevoi.tevoi;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSessionManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import androidx.core.app.NotificationManagerCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
//import android.support.v4.media.app.NotificationCompat.MediaStyle;
import androidx.media.app.NotificationCompat.MediaStyle;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
//import android.support.v7.app.NotificationCompat;

import android.widget.Toast;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.model.TrackObject;
import com.tevoi.tevoi.model.UserSubscriptionInfoResponse;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomMediaPlayerService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener,
        AudioManager.OnAudioFocusChangeListener
{

    public static final String ACTION_PLAY = "com.tevoi.tevoi.audioplayer.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.tevoi.tevoi.audioplayer.ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "com.tevoi.tevoi.audioplayer.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "com.tevoi.tevoi.audioplayer.ACTION_NEXT";
    public static final String ACTION_STOP = "com.tevoi.tevoi.audioplayer.ACTION_STOP";

    public static String MAIN_ACTION = "com.tevoi.tevoi.action.main";
    public static String INIT_ACTION = "com.tevoi.tevoi.action.init";
    public static String PREV_ACTION = "com.tevoi.tevoi.action.prev";
    public static String PLAY_ACTION = "com.tevoi.tevoi.action.play";
    public static String NEXT_ACTION = "com.tevoi.tevoi.action.next";
    public static String STARTFOREGROUND_ACTION = "com.tevoi.tevoi.action.startforeground";
    public static String STOPFOREGROUND_ACTION = "com.tevoi.tevoi.action.stopforeground";

    public static int FOREGROUND_SERVICE = 101;

    MediaPlayer mMediaPlayer;

    //MediaSession
    private MediaSessionManager mediaSessionManager;
    private MediaSessionCompat mediaSession;
    private MediaControllerCompat.TransportControls transportControls;

    //AudioPlayer notification ID
    private static final int NOTIFICATION_ID = 101;

    //Used to pause/resume MediaPlayer
    private int resumePosition;

    //AudioFocus
    private AudioManager audioManager;

    // Binder given to clients
    private final IBinder iBinder = new LocalBinder();

    //List of available Audio files
    private ArrayList<TrackObject> audioList;
    private int audioIndex = -1;
    private TrackObject activeAudio; //an object on the currently playing audio

    //Handle incoming phone calls
    private boolean ongoingCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;

    String currentAudioUrl = Global.BASE_AUDIO_URL;
    //public TrackObject CurrentTrackInServicePlayer;
    String TrackName= "";
    String TrackAuthors = "";
    int TrackId = 0;
    private Handler mHandler = new Handler();
    private MyTimerRunnable mRunnable;
    int numberOfUnitsSendToServer;
    public int numberOfListenedSeconds;
    public int numberOfCurrentSecondsInTrack;

    private ServiceCallbacks serviceCallbacks;

    public void setCallbacks(ServiceCallbacks callbacks) {
        serviceCallbacks = callbacks;
    }

    /**
     * Service lifecycle methods
     */
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Perform one-time setup procedures

        // Manage incoming phone calls during playback.
        // Pause MediaPlayer on incoming call,
        // Resume on hangup.
        callStateListener();
        //ACTION_AUDIO_BECOMING_NOISY -- change in audio outputs -- BroadcastReceiver
        registerBecomingNoisyReceiver();
        //Listen for new Audio to play -- BroadcastReceiver
        register_playNewAudio();

        mRunnable = new MyTimerRunnable();

    }

    private class MyTimerRunnable implements Runnable {
        @Override
        public void run ()
        {
            if (mMediaPlayer != null)
            {
                resumePosition = mMediaPlayer.getCurrentPosition();

                if (mMediaPlayer.isPlaying())
                {
                    numberOfListenedSeconds += 1;
                    numberOfCurrentSecondsInTrack += 1;
                    //activity.numberOfTotalSeconds += activity.numberOfCurrentSeconds;
                }
                if(serviceCallbacks.isDemoUser())
                {
                    if(serviceCallbacks.getUnitsForDemoUser() <= Global.NumberOfAllowedUnits)
                    {
                        int n = serviceCallbacks.getUnitsForDemoUser() * Global.ListenUnitInSeconds + Global.ListenUnitInSeconds;
                        if (numberOfListenedSeconds >= Global.ListenUnitInSeconds)
                        {
                            int numOfUnits = numberOfListenedSeconds / Global.ListenUnitInSeconds;
                            numberOfListenedSeconds = numberOfListenedSeconds - numOfUnits * Global.ListenUnitInSeconds;
                            // update his quota
                            serviceCallbacks.addUnitsForDemoUser(numOfUnits);
                            Toast.makeText(getBaseContext(), "" + numOfUnits + " Unit consumed from your demo quota", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        mMediaPlayer.stop();
                        serviceCallbacks.setFlagUserExceedsDailyUsageListen();
                        //Toast.makeText(getBaseContext(), R.string.no_quota_for_today, Toast.LENGTH_SHORT).show();
                        Log.d("Finished Quota", "You finished for today");
                    }
                }
                else
                {
                int n = numberOfUnitsSendToServer * Global.ListenUnitInSeconds + Global.ListenUnitInSeconds;
                Log.d("Service Log", "numberOfUnitsSendToServer=" + numberOfUnitsSendToServer + ", numberOfListenedSeconds=" + numberOfListenedSeconds);
                if (numberOfListenedSeconds >= n)
                {
                    int numberOfUnRegisteredSeconds = numberOfListenedSeconds - numberOfUnitsSendToServer * Global.ListenUnitInSeconds;
                    final int numberOfConsumedUnits = numberOfUnRegisteredSeconds / Global.ListenUnitInSeconds;
                    numberOfUnitsSendToServer += numberOfConsumedUnits;


                        // send to server that we used 1 unit
                        Call<UserSubscriptionInfoResponse> call = Global.client.AddUnitUsageForUser(TrackId, numberOfConsumedUnits, numberOfCurrentSecondsInTrack);
                        call.enqueue(new Callback<UserSubscriptionInfoResponse>() {
                            public void onResponse(Call<UserSubscriptionInfoResponse> call, Response<UserSubscriptionInfoResponse> response) {
                                //generateDataList(response.body());
                                UserSubscriptionInfoResponse updatedUserUsage = response.body();
                                //numberOfUnitsSendToServer += numberOfConsumedUnits;
                                Log.d("Cs", "numberOfConsumedUnits=" + numberOfConsumedUnits);

                                Toast.makeText(getBaseContext(), "" + numberOfConsumedUnits + " Unit consumed from your quota", Toast.LENGTH_SHORT).show();
                                serviceCallbacks.updateUserUsage(updatedUserUsage);
                                numberOfCurrentSecondsInTrack = 0;
                                if (updatedUserUsage.IsFreeSubscription)
                                {
                                    if (updatedUserUsage.numberOfListenUnitsConsumed >= updatedUserUsage.FreeSubscriptionLimit.DailyListenMaxUnits) {
                                        mMediaPlayer.stop();
                                        serviceCallbacks.setFlagUserExceedsDailyUsageListen();
                                        Toast.makeText(getBaseContext(), R.string.no_quota_for_today, Toast.LENGTH_SHORT).show();
                                        Log.d("Finished Quota", "You finished for today");
                                    }
                                }
                            }

                            public void onFailure(Call<UserSubscriptionInfoResponse> call, Throwable t) {
                                numberOfUnitsSendToServer -= numberOfConsumedUnits;
                            }
                        });
                    }
                }
            }
            mHandler.postDelayed(this, 1000);

        }
    }


    //The system calls this method when an activity, requests the service be started
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try
        {
            //An audio file is passed to the service through putExtra();
            currentAudioUrl = intent.getExtras().getString("media");
            TrackName = intent.getExtras().getString("TrackName");
            TrackAuthors = intent.getExtras().getString("TrackAuthors");
            TrackId =  intent.getExtras().getInt("TrackId");
        }
        catch (NullPointerException e) {
            stopSelf();
        }

        //Request audio focus
        if (requestAudioFocus() == false)
        {
            //Could not gain focus
            stopSelf();
        }

        if (mediaSessionManager == null)
        {
            try
            {
                if (currentAudioUrl != null && currentAudioUrl != "") {

                    initMediaSession();
                    initMediaPlayer();
                    mHandler.postDelayed(mRunnable, 1000);
                    //AddListenActiityOnTrack();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
                stopSelf();
            }
            buildNotification(PlaybackStatus.PLAYING);
        }

        //Handle Intent action from MediaSession.TransportControls
        handleIncomingActions(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    /*@Override
    public boolean onUnbind(Intent intent) {
        mediaSession.release();
        removeNotification();
        return super.onUnbind(intent);
    }
*/

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (mMediaPlayer != null) {
            stopMedia();
            mMediaPlayer.release();
        }
        removeAudioFocus();
        //Disable the PhoneStateListener
        if (phoneStateListener != null) {
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }

        removeNotification();

        //unregister BroadcastReceivers
        unregisterReceiver(becomingNoisyReceiver);
        unregisterReceiver(playNewAudio);

        mHandler.removeCallbacks(mRunnable);
    }

    /**
     * Service Binder
     */
    public class LocalBinder extends Binder
    {
        public CustomMediaPlayerService getService() {
            if(serviceCallbacks != null)
                serviceCallbacks.showLoaderMediaPlayer();
            // Return this instance of LocalService so clients can call public methods
            return CustomMediaPlayerService.this;
        }
    }


    /**
     * MediaPlayer callback methods
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        //Invoked indicating buffering status of
        //a media resource being streamed over the network.

        Log.d("PlayerService", "onBufferingUpdate, percent=" + percent );

    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {

        Log.d("PlayerService", "onCompletion" );
        //Invoked when playback of a media source has completed.
        stopMedia();

        //removeNotification();
        //stop the service
        stopSelf();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        //Invoked when there has been an error during an asynchronous operation
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Log.d("MediaPlayer Error", "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.d("MediaPlayer Error", "MEDIA ERROR SERVER DIED " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.d("MediaPlayer Error", "MEDIA ERROR UNKNOWN " + extra);
                break;
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        //Invoked to communicate some info
        Log.d("PlayerService", "onInfo" + ",what=" + what + ",extra=" + extra);
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp)
    {
        //Invoked when the media source is ready for playback.
        Log.d("PlayerService", "onPrepared" );
        if(serviceCallbacks != null)
        {
            serviceCallbacks.hideLoaderMediaPlayer();
        }
        playMedia();
    }

    @Override
    public void onSeekComplete(MediaPlayer mp)
    {
        //Invoked indicating the completion of a seek operation.
        Log.d("PlayerService", "onSeekComplete" );
    }

    @Override
    public void onAudioFocusChange(int focusState) {

        //Invoked when the audio focus of the system is updated.
        switch (focusState) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (mMediaPlayer == null) initMediaPlayer();
                else if (!mMediaPlayer.isPlaying())
                {
                    // TODO : check if activity is active

                    /*if(!activityIsPaused)
                        mMediaPlayer.start();*/

                    // mMediaPlayer.start();
                }
                mMediaPlayer.setVolume(1.0f, 1.0f);
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (mMediaPlayer.isPlaying()) mMediaPlayer.pause();
                //mediaPlayer.release();
                //mediaPlayer = null;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (mMediaPlayer.isPlaying()) mMediaPlayer.pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (mMediaPlayer.isPlaying()) mMediaPlayer.setVolume(0.1f, 0.1f);
                break;
        }
    }


    /**
     * AudioFocus
     */
    private boolean requestAudioFocus()
    {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            //Focus gained
            return true;
        }
        //Could not gain focus
        return false;
    }

    private boolean removeAudioFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                audioManager.abandonAudioFocus(this);
    }


    /**
     * MediaPlayer actions
     */
    private void initMediaPlayer() {

        mMediaPlayer = new MediaPlayer();//new MediaPlayer instance

        //Set up MediaPlayer event listeners
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnSeekCompleteListener(this);
        mMediaPlayer.setOnInfoListener(this);
        //Reset so that the MediaPlayer is not pointing to another data source
        mMediaPlayer.reset();

        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try
        {
            // Set the data source to the mediaFile location
            //mediaPlayer.setDataSource(activeAudio.getData());
            Map<String,String> headers = new HashMap<>();
            headers.put("Authorization",Global.UserToken);
            headers.put("License", "TevoiMobileApp");

            mMediaPlayer.setDataSource(getBaseContext(), Uri.parse(currentAudioUrl), headers);

            //mMediaPlayer.setDataSource(currentAudioUrl);
        } catch (IOException e) {
            e.printStackTrace();
            stopSelf();
        }
        mMediaPlayer.prepareAsync();
    }

    private void playMedia()
    {
        if (!mMediaPlayer.isPlaying())
        {
            mMediaPlayer.start();
        }
    }

    private void stopMedia() {
        if (mMediaPlayer == null) return;
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

    private void pauseMedia() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            //resumePosition = mMediaPlayer.getCurrentPosition();
        }
    }

    private void resumeMedia() {
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.seekTo(resumePosition);
            mMediaPlayer.start();
        }
    }

    private void skipToNext() {

        // TODO : pass new url
        stopMedia();
        //reset mediaPlayer
        mMediaPlayer.reset();
        initMediaPlayer();
    }

    private void skipToPrevious() {

        // TODO : pass new url

        stopMedia();
        //reset mediaPlayer
        mMediaPlayer.reset();
        initMediaPlayer();
    }


    /**
     * ACTION_AUDIO_BECOMING_NOISY -- change in audio outputs
     */
    private BroadcastReceiver becomingNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //pause audio on ACTION_AUDIO_BECOMING_NOISY
            pauseMedia();
            buildNotification(PlaybackStatus.PAUSED);
        }
    };

    private void registerBecomingNoisyReceiver() {
        //register after getting audio focus
        IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(becomingNoisyReceiver, intentFilter);
    }

    /**
     * Handle PhoneState changes
     */
    private void callStateListener() {
        // Get the telephony manager
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //Starting listening for PhoneState changes
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    //if at least one call exists or the phone is ringing
                    //pause the MediaPlayer
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                    case TelephonyManager.CALL_STATE_RINGING:
                        if (mMediaPlayer != null) {
                            pauseMedia();
                            ongoingCall = true;
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        // Phone idle. Start playing.
                        if (mMediaPlayer != null) {
                            if (ongoingCall) {
                                ongoingCall = false;
                                // TODO : check if app is in background so don't resume
                                //resumeMedia();
                            }
                        }
                        break;
                }
            }
        };
        // Register the listener with the telephony manager
        // Listen for changes to the device call state.
        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_CALL_STATE);
    }

    /**
     * MediaSession and Notification actions
     */
    private void initMediaSession() throws RemoteException {
        if (mediaSessionManager != null) return; //mediaSessionManager exists

        mediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
        // Create a new MediaSession
        mediaSession = new MediaSessionCompat(getApplicationContext(), "AudioPlayer");
        //Get MediaSessions transport controls
        transportControls = mediaSession.getController().getTransportControls();
        //set MediaSession -> ready to receive media commands
        mediaSession.setActive(true);
        //indicate that the MediaSession handles transport control commands
        // through its MediaSessionCompat.Callback.
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        //Set mediaSession's MetaData
        updateMetaData();

        // Attach Callback to receive MediaSession updates
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            // Implement callbacks
            @Override
            public void onPlay() {
                super.onPlay();

                resumeMedia();
                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onPause() {
                super.onPause();

                pauseMedia();
                buildNotification(PlaybackStatus.PAUSED);
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();

                //skipToNext();
                if (serviceCallbacks != null) {
                    serviceCallbacks.playNext();
                }
                updateMetaData();
                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();

                //skipToPrevious();
                if (serviceCallbacks != null) {
                    serviceCallbacks.playPrevious();
                }
                updateMetaData();
                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onStop() {
                super.onStop();
                removeNotification();
                //Stop the service
                stopSelf();
            }

            @Override
            public void onSeekTo(long position) {
                super.onSeekTo(position);
            }
        });
    }

    private void updateMetaData()
    {
        Bitmap albumArt = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher); //replace with medias albumArt
        // Update the current metadata
        mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, TrackAuthors)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, "")
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, TrackName)
                .build());
    }

    public void buildNotification(PlaybackStatus playbackStatus)
    {

        /**
         * Notification actions -> playbackAction()
         *  0 -> Play
         *  1 -> Pause
         *  2 -> Next track
         *  3 -> Previous track
         */

        int notificationAction = android.R.drawable.ic_media_pause;//needs to be initialized
        PendingIntent play_pauseAction = null;

        //Build a new notification according to the current state of the MediaPlayer
        if (playbackStatus == PlaybackStatus.PLAYING)
        {
            notificationAction = android.R.drawable.ic_media_pause;
            //create the pause action
            play_pauseAction = playbackAction(1);
        }
        else if (playbackStatus == PlaybackStatus.PAUSED) {
            notificationAction = android.R.drawable.ic_media_play;
            //create the play action
            play_pauseAction = playbackAction(0);
        }

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                R.drawable.logo_small); //replace with your own image

/*        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews views = new RemoteViews(getPackageName(),
                R.layout.status_bar);
        RemoteViews bigViews = new RemoteViews(getPackageName(),
                R.layout.status_bar_expanded);

        // showing default album image
        views.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
        views.setViewVisibility(R.id.status_bar_album_art, View.GONE);
        bigViews.setImageViewBitmap(R.id.status_bar_album_art,
                Global.getDefaultAlbumArt(this));

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent previousIntent = new Intent(this, CustomMediaPlayerService.class);
        previousIntent.setAction(PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, CustomMediaPlayerService.class);
        playIntent.setAction(PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent nextIntent = new Intent(this, CustomMediaPlayerService.class);
        nextIntent.setAction(NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Intent closeIntent = new Intent(this, CustomMediaPlayerService.class);
        closeIntent.setAction(STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, 0);

        views.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);

        views.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);

        views.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);

        views.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);

        views.setImageViewResource(R.id.status_bar_play,
                R.drawable.apollo_holo_dark_pause);
        bigViews.setImageViewResource(R.id.status_bar_play,
                R.drawable.apollo_holo_dark_pause);

        views.setTextViewText(R.id.status_bar_track_name, "Song Title");
        bigViews.setTextViewText(R.id.status_bar_track_name, "Song Title");

        views.setTextViewText(R.id.status_bar_artist_name, "Artist Name");
        bigViews.setTextViewText(R.id.status_bar_artist_name, "Artist Name");

        bigViews.setTextViewText(R.id.status_bar_album_name, "Album Name");
        Notification status = new Notification.Builder(this).build();
        status.contentView = views;
        status.bigContentView = bigViews;
        status.flags = Notification.FLAG_ONGOING_EVENT;
        status.icon = R.drawable.ic_launcher;
        status.contentIntent = pendingIntent;
        startForeground(FOREGROUND_SERVICE, status);*/

        /*Notification.MediaStyle style = new Notification.MediaStyle();
        Notification.Builder builder = new Notification.Builder( this )
                .setSmallIcon( R.drawable.ic_launcher )
                .setContentTitle( "Media Title" )
                .setContentText( "Media Artist" )
                .setDeleteIntent( pendingIntent )
                .setStyle( style );

        builder.addAction( generateAction( android.R.drawable.ic_media_previous, "Previous", ACTION_PREVIOUS ) );
        builder.addAction( generateAction( android.R.drawable.ic_media_rew, "Rewind", ACTION_REWIND ) );
        builder.addAction( action );
        builder.addAction( generateAction( android.R.drawable.ic_media_ff, "Fast Foward", ACTION_FAST_FORWARD ) );
        builder.addAction( generateAction( android.R.drawable.ic_media_next, "Next", ACTION_NEXT ) );
        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID, status);*/
        // Create a new Notification

        Intent intent = new Intent(this, SideMenu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT|
                Intent.FLAG_ACTIVITY_SINGLE_TOP);;
        intent.setPackage(null);
        intent.setAction("OPEN_TAB_1");
        //PendingIntent pending_intent = PendingIntent.getService(this, 0, intent, 0);

        String NOTIFICATION_CHANNEL_ID = "com.tevoi.tevoi";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {

            String channelName = "My Background Service";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_LOW);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);
        }
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);


            notificationBuilder.setContentIntent(contentIntent);

            Notification notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.tevoi_logo)
                    // Hide the timestamp
                    .setShowWhen(false)
                    .setAutoCancel(true)
                    .setOngoing(false)
                    //.setContentTitle("App is running in background")
                    .setPriority(NotificationManager.IMPORTANCE_LOW)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setContentText(TrackName)
                    .setContentTitle("")
                    .setContentInfo(TrackAuthors)
                    .setOnlyAlertOnce(true)
                    .setStyle(new MediaStyle()
                            // Attach our MediaSession token
                            .setMediaSession(mediaSession.getSessionToken())
                            // Show our playback controls in the compat view
                            .setShowActionsInCompactView(0, 1, 2))
                    .setColor(ContextCompat.getColor(this,R.color.tevoiBrownLight))
                    // Set the large and small icons
                    //.setLargeIcon(largeIcon)
                    .setSmallIcon(android.R.drawable.stat_sys_headset)
                    // Add playback actions
                    .addAction(android.R.drawable.ic_menu_close_clear_cancel, "close", playbackAction(4))
                    .addAction(android.R.drawable.ic_media_previous, "previous", playbackAction(3))
                    .addAction(notificationAction, "pause", play_pauseAction)
                    .addAction(android.R.drawable.ic_media_next, "next", playbackAction(2))
                    .build();
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);



            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(NOTIFICATION_ID, notification);

            //startForeground(NOTIFICATION_ID, notification);

        /*}
        else {
            NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                    // Hide the timestamp
                    .setShowWhen(false)
                    // Set the Notification style
                    .setStyle(new MediaStyle()
                            // Attach our MediaSession token
                            .setMediaSession(mediaSession.getSessionToken())
                            // Show our playback controls in the compat view
                            .setShowActionsInCompactView(0, 1, 2))
                    // Set the Notification color
                    .setColor(getResources().getColor(R.color.tevoiBlueSecondary))
                    // Set the large and small icons
                    .setLargeIcon(largeIcon)
                    .setSmallIcon(android.R.drawable.stat_sys_headset)
                    // Set Notification content information
                    .setContentText(TrackAuthors)
                    .setContentTitle("")
                    .setContentInfo(TrackName)
                    //.setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setOnlyAlertOnce(true)
                    // Add playback actions
                    .addAction(android.R.drawable.ic_media_previous, "previous", playbackAction(3))
                    .addAction(notificationAction, "pause", play_pauseAction)
                    .addAction(android.R.drawable.ic_media_next, "next", playbackAction(2));

            //startForeground(Global.NOTIFICATION_ID.FOREGROUND_SERVICE, notificationBuilder.getNotification());
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID, notificationBuilder.build());
        }
*/
    }


    private PendingIntent playbackAction(int actionNumber)
    {
        Intent playbackAction = new Intent(this, CustomMediaPlayerService.class);
        //Toast.makeText(this, "ma" + actionNumber, Toast.LENGTH_SHORT).show();
        switch (actionNumber) {
            case 0:
                // Play
                playbackAction.setAction(ACTION_PLAY);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 1:
                // Pause
                playbackAction.setAction(ACTION_PAUSE);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 2:
                // Next track
                playbackAction.setAction(ACTION_NEXT);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 3:
                // Previous track
                playbackAction.setAction(ACTION_PREVIOUS);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 4:
            {
                playbackAction.setAction(Global.ACTION.STOPFOREGROUND_ACTION);
                //removeNotification();
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            }
            default:
                break;
        }
        return null;
    }

    public void removeNotification()
    {
        //NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //notificationManager.cancel(NOTIFICATION_ID);
        stopForeground(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancel(NOTIFICATION_ID);

        //stopForeground(true);

    }

    private void handleIncomingActions(Intent playbackAction)
    {
        if (playbackAction == null || playbackAction.getAction() == null) return;

        String actionString = playbackAction.getAction();
        if (actionString.equalsIgnoreCase(ACTION_PLAY)) {
            transportControls.play();
        } else if (actionString.equalsIgnoreCase(ACTION_PAUSE)) {
            transportControls.pause();
        } else if (actionString.equalsIgnoreCase(ACTION_NEXT)) {
            transportControls.skipToNext();
        } else if (actionString.equalsIgnoreCase(ACTION_PREVIOUS)) {
            transportControls.skipToPrevious();
        } else if (actionString.equalsIgnoreCase(ACTION_STOP)) {
            transportControls.stop();
        }
        else if(actionString.equalsIgnoreCase(Global.ACTION.STOPFOREGROUND_ACTION))
        {
            removeNotification();
        }
    }


    /**
     * Play new Audio
     */
    private BroadcastReceiver playNewAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            stopMedia();
            mMediaPlayer.reset();
            AddListenActivityOnTrack();
            try
            {
                //An audio file is passed to the service through putExtra();
                currentAudioUrl = intent.getExtras().getString("media");
                TrackName = intent.getExtras().getString("TrackName");
                TrackAuthors = intent.getExtras().getString("TrackAuthors");
                TrackId =  intent.getExtras().getInt("TrackId");

            }
            catch (NullPointerException e)
            {
                stopSelf();
            }
            initMediaPlayer();
            updateMetaData();
            buildNotification(PlaybackStatus.PLAYING);
        }
    };

    private void register_playNewAudio()
    {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter(SideMenu.Broadcast_PLAY_NEW_AUDIO);
        registerReceiver(playNewAudio, filter);
    }

    public enum PlaybackStatus {
        PLAYING,
        PAUSED
    }

    public void AddListenActivityOnTrack()
    {
        if(serviceCallbacks.isDemoUser())
        {
            if(serviceCallbacks.getUnitsForDemoUser() <= Global.NumberOfAllowedUnits)
            {
                if (numberOfListenedSeconds >= Global.ListenUnitInSeconds)
                {
                    int numOfUnits = numberOfListenedSeconds / Global.ListenUnitInSeconds;
                    numberOfListenedSeconds = numberOfListenedSeconds - numOfUnits * Global.ListenUnitInSeconds;
                    // update his quota
                    serviceCallbacks.addUnitsForDemoUser(numOfUnits);
                    Toast.makeText(getBaseContext(), "" + numOfUnits + " Unit consumed from your demo quota", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                mMediaPlayer.stop();
                serviceCallbacks.setFlagUserExceedsDailyUsageListen();
                Toast.makeText(getBaseContext(), R.string.no_quota_for_today, Toast.LENGTH_SHORT).show();
                Log.d("Finished Quota", "You finished for today");
            }
        }
        else
        {
            int numOfUnits = numberOfCurrentSecondsInTrack / Global.ListenUnitInSeconds;
            int remainingSeconds = numberOfListenedSeconds - numOfUnits * Global.ListenUnitInSeconds;

            if (remainingSeconds > 0) {
                serviceCallbacks.AddtoHistory(TrackId);
                Call<UserSubscriptionInfoResponse> call = Global.client.AddUnitUsageForUser(TrackId, 0, numberOfCurrentSecondsInTrack);
                call.enqueue(new Callback<UserSubscriptionInfoResponse>() {
                    public void onResponse(Call<UserSubscriptionInfoResponse> call, Response<UserSubscriptionInfoResponse> response) {
                        //generateDataList(response.body());
                        UserSubscriptionInfoResponse updatedUserUsage = response.body();
                        serviceCallbacks.updateUserUsage(updatedUserUsage);
                        numberOfCurrentSecondsInTrack = 0;
                        //Toast.makeText(CustomMediaPlayerService.this, "Doooooone", Toast.LENGTH_SHORT).show();
                        if (updatedUserUsage != null) {
                            if (updatedUserUsage.IsFreeSubscription) {
                                /*if(updatedUserUsage.numberOfListenUnitsConsumed >= updatedUserUsage.FreeSubscriptionLimit.DailyListenMaxUnits)
                                {
                                    mMediaPlayer.stop();
                                    serviceCallbacks.setFlagUserExceedsDailyUsageListen();
                                }*/
                            }
                        }
                    }
                    public void onFailure(Call<UserSubscriptionInfoResponse> call, Throwable t) {
                        Toast.makeText(CustomMediaPlayerService.this, "errrrrrro", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }

}


