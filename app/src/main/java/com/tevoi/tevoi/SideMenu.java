package com.tevoi.tevoi;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
/*import android.support.v7.app.ActionBar;*/
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.Utils.HelperFunctions;
import com.tevoi.tevoi.Utils.MyStorage;
import com.tevoi.tevoi.model.IResponse;
import com.tevoi.tevoi.model.InternetConnectionListener;
import com.tevoi.tevoi.model.TrackObject;
import com.tevoi.tevoi.model.TrackSerializableObject;
import com.tevoi.tevoi.model.UserSubscriptionInfoResponse;
import com.tevoi.tevoi.rest.ApiClient;
import com.tevoi.tevoi.rest.ApiInterface;

import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SideMenu extends AppCompatActivity
        implements  ServiceCallbacks , NavigationView.OnNavigationItemSelectedListener, InternetConnectionListener {

    // region Guest Information
    public boolean isDemoUser = false;
    public boolean isExceedsQuotaDemoUser = false;

    //endregion

    // region subscription Information
    public UserSubscriptionInfoResponse userSubscriptionInfo = new UserSubscriptionInfoResponse();
    public boolean IsListenDailyLimitsExceeded = false;
    public boolean IsReadDailyLimitsExceeded = false;
    public int numberOfTextUnitsConsumed = 0;
    // endregion

    public ProgressDialog mProgressDialog;

    // region user usage from quota
    int numberOfTotalSeconds;
    int numberOfCurrentSeconds;
    public int numberOfUnitsSendToServer;
    public int numberOfReadUnitsSendToServer;
    int userUsageFromServer;


    //endregion

    // region Play From User List
    public ArrayList<TrackObject> tracksFromUserList = new ArrayList<TrackObject>();
    public boolean isPlayingFromUserList = false;
    public int indexCurrentTrackInUserList;

    // endregion

    // region Play Now List Tracks
    public ArrayList<TrackSerializableObject> playNowListTracks = new ArrayList<TrackSerializableObject>();
    public MyStorage storageManager;
    public boolean isPlayingFromPlayNowList = false;
    public int indexCurrentTrackInPlayList;
    public static final String Broadcast_PLAY_NEW_AUDIO = "com.tevoi.tevoi.CustomMediaPlayerService.PlayNewAudio";
    // endregion

    // region service for playing media player
    public CustomMediaPlayerService player;
    public boolean serviceBound = false;
    public boolean isPlaying = false;
    public boolean isPaused = false;
    public int numberOfListenedSeconds;
    public int numberOfCurrentSecondsInTrack;
    public int trackIdPlayedNow;

    private Handler mHandler = new Handler();

    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            CustomMediaPlayerService.LocalBinder binder = (CustomMediaPlayerService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;
            player.setCallbacks(SideMenu.this); // register
//            Toast.makeText(SideMenu.this, "Service Bound", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    // endregion

    // region main player
    public LinearLayout mainPlayerLayout;
    public TrackObject CurrentTrackInPlayer;

    SeekBar seekBarMainPlayer;
    TextView txtCurrentTime;
    TextView txtFinishTime;
    public TextView txtTrackName;
    ImageButton btnPausePlayMainMediaPlayer;
    ProgressBar playerLoader;

    public boolean isActivityPause = false;


    // endregion

    // region banner
    public LinearLayout linearLayoutBanner;
    public ImageView imgBanner;
    public TextView txtLink;

    //endregion

    // region properties for drawer

    /* DrawerLayout mDrawerLayout;
     // ListView represents Navigation Drawer
     ListView mDrawerList;
     // ActionBarDrawerToggle indicates the presence of Navigation Drawer in the action bar
     ActionBarDrawerToggle mDrawerToggle;*/
    private DrawerLayout drawer;
    private NavigationView navigationView;

    // endregion

    // region side menu fragments objects
    public TracksList lisTracksFragment = new TracksList();
    InterfaceLanguageFragment interfaceLanguageFragment = new InterfaceLanguageFragment();

    PartnersFragment partnersFragment = new PartnersFragment();
    public PartnerNameFragment partnerNameFragment = new PartnerNameFragment();
    NotificationFragment notificationFragment = new NotificationFragment();
    DownloadFragment downloadFragment = new DownloadFragment();
    AboutUsFragment aboutUsFragment = new AboutUsFragment();
    FeedbackFragment feedbackFragment = new FeedbackFragment();
    FollowUsFragment followUsFragment = new FollowUsFragment();

    //Abd edit
    public MyProfileFragment myProfileFragment = new MyProfileFragment();
    //Abd edit

    HistoryListFragment historyListFragment = new HistoryListFragment();
    PlayingNowFragment playingNowFragment = new PlayingNowFragment();
    FavouriteFragment favouriteFragment = new FavouriteFragment();
    //MyListFragment myListFragment = new MyListFragment();
    UserListFragment userListsFragment = new UserListFragment();
    //FilterFragment filterFragment = new FilterFragment();
    public UserFilterFragment userFilterFragment = new UserFilterFragment();

    UpgradeToPremiumFragment upgradeToPremiumFragment = new UpgradeToPremiumFragment();

    public UserListTracksFragment userListTracksFragment = new UserListTracksFragment();
    public MediaPlayerFragment mediaPlayerFragment;
    ErrorFragment errorFragment = ErrorFragment.newInstance(Global.ListTracksFragmentName);
    public String CurrentFragmentName = Global.ListTracksFragmentName;
    public String PreviousFragmentName = "";
    // endregion

    // region handle List of track for next and previous buttons

    public List<TrackObject> lstTracks = new ArrayList<>();


    // endregion

    SlidingUpPanelLayout mLayout;
    public boolean IsFilterChanged = false;
    //ListTracksFragment listTracksFragment = new ListTracksFragment();

    //
    MenuItem searchBtn;

    public void initActionBar(String subTitle)
    {
        final ActionBar abar = getActionBar();
        //abar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));//line under the action bar
        View viewActionBar = getLayoutInflater().inflate(R.layout.nav_header_main, null);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);

        TextView textviewTitle = viewActionBar.findViewById(R.id.tvTitle);
        textviewTitle.setText("Tevoi");
        TextView textviewSubTitle = viewActionBar.findViewById(R.id.tvSubTitle);
        textviewSubTitle.setText(subTitle);

        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setIcon(R.color.colorAccent);
        abar.setHomeButtonEnabled(true);

        abar.setBackgroundDrawable(getResources().getDrawable(R.drawable.launcher_background));
        //abar.setElevation(0);
        //abar.setElevation(0);
    }

    private void setfullwidth()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        int width = (displayMetrics.widthPixels * 80)/100;
        params.width = width;//displayMetrics.widthPixels;
        navigationView.setLayoutParams(params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        storageManager = new MyStorage(Global.CurrentUserId);
        //region detect language

        String language = storageManager.getLanguageUIPreference(this);
        if (language == null)
            language = "en";
        Global.UserUILanguage = language;
        Resources res = getBaseContext().getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(language)); // API 17+ only.
        // Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);

        // endregion

        setContentView(R.layout.activity_main);

        Bundle b = getIntent().getExtras();
        if(b != null)
            isDemoUser = b.getBoolean("IsDemoUser");

        if(isDemoUser)
            Toast.makeText(getApplicationContext(), "You're guest", Toast.LENGTH_SHORT).show();
        //HelperFunctions.downloadAudioFile(SideMenu.this, 14);

        final androidx.fragment.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final TextView mSubTitle = (TextView) toolbar.findViewById(R.id.toolbar_subtitle);
        ImageView btnTevoiLogo = toolbar.findViewById(R.id.tevoi_logo_img);
        /*btnTevoiLogo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(!CurrentFragmentName.equals(Global.ListTracksFragmentName))
                {
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    if (drawer.isDrawerOpen(GravityCompat.START))
                    {
                        drawer.closeDrawer(GravityCompat.START);
                    }
                    mSubTitle.setText(R.string.title_list_tracks);
                    fragmentTransaction.replace(R.id.content_frame, lisTracksFragment);
                    fragmentTransaction.addToBackStack(getResources().getString(R.string.title_list_tracks));
                    CurrentFragmentName = Global.ListTracksFragmentName;
                    try
                    {
                        fragmentTransaction.commit();
                    }
                    catch (Exception exc)
                    {
                    }
                }
            }
        });*/

        ImageView imgHome = toolbar.findViewById(R.id.img_home);

        imgHome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                androidx.fragment.app.FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();

                if(!CurrentFragmentName.equals(Global.ListTracksFragmentName))
                {
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    if (drawer.isDrawerOpen(GravityCompat.START))
                    {
                        drawer.closeDrawer(GravityCompat.START);
                    }
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    mSubTitle.setText(R.string.title_list_tracks);
                    fragTransaction.replace(R.id.content_frame, lisTracksFragment);
                    fragTransaction.addToBackStack(getResources().getString(R.string.title_list_tracks));
                    CurrentFragmentName = Global.ListTracksFragmentName;
                    try
                    {
                        fragTransaction.commit();
                    }
                    catch (Exception exc)
                    {
                        Log.d("Marwa-test", exc.getMessage());
                    }
                }
            }
        });



        ImageView imgFilter = findViewById(R.id.tevoi_filter_img);
        imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(isDemoUser)
                {
                    Toast.makeText(getApplicationContext(), R.string.demo_user_need_to_register, Toast.LENGTH_SHORT).show();
                }
                else
                {

                /*//Load animation
                Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_down);

                Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_up);

                // Start animation
                RelativeLayout vf = findViewById(R.id.rl_test);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View row = inflater.inflate(R.layout.fragment_filter, vf, false);
                View v = View.inflate(getBaseContext(), R.layout.fragment_filter, null);
                vf.startAnimation(slide_down);*/
                    androidx.fragment.app.FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();

                    if (!CurrentFragmentName.equals(Global.FilterFragmentName))
                    {
                        PreviousFragmentName = CurrentFragmentName;
                        mSubTitle.setText(R.string.title_filter);
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        if (drawer.isDrawerOpen(GravityCompat.START))
                        {
                            drawer.closeDrawer(GravityCompat.START);
                        }
                        CurrentFragmentName = Global.FilterFragmentName;
                        //fragTransaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);

                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                        fragTransaction.replace(R.id.frameLayoutFilter, userFilterFragment);
                        //fragTransaction.addToBackStack(mSubTitle.getText().toString());
                        try {
                            fragTransaction.commit();
                        } catch (Exception exc) {
                        }
                    }
                    else
                    {
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        CurrentFragmentName = PreviousFragmentName;
                        mSubTitle.setText(CurrentFragmentName);
                        fragTransaction.replace(R.id.frameLayoutFilter, new Fragment());
                        //fragTransaction.addToBackStack(mSubTitle.getText().toString());
                        try {
                            fragTransaction.commit();
                        } catch (Exception exc) {
                        }
                    }
                }
            }
        });
        /* setActionBar(toolbar);
        getActionBar().setDisplayShowTitleEnabled(false);*/

        //TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        //TextView mSubTitle = (TextView) toolbar.findViewById(R.id.toolbar_subtitle);


        setSupportActionBar(toolbar);
        //mTitle.setText("Tevoi");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //toolbar.setNavigationIcon(R.mipmap.settings_menu_hover);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setfullwidth();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close)
        {
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);

                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                //TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
                TextView mSubTitle = (TextView) toolbar.findViewById(R.id.toolbar_subtitle);
                mSubTitle.setText(R.string.title_setting);

                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.mipmap.arrow_back_normal);

            }
           /* @Override
            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_SETTLING) {
                    if (!isDrawerOpen()) {
                        // starts opening
                        getActionBar()
                                .setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                    } else {
                        // closing drawer
                        getActionBar()
                                .setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
                    }
                    invalidateOptionsMenu();
                }
            }*/

            @Override
            public void onDrawerClosed(View drawerView)
            {
                // Called when a drawer has settled in a completely closed state.
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.mipmap.settings_menu_normal);
            }

        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemBackground(getDrawable(R.drawable.divider));

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.settings_menu_normal);


        //region code for changing menu item text style
        Menu menuNav = navigationView.getMenu();
        MenuItem menuUpgradeToPremium = menuNav.findItem(R.id.upgrade_to_premium);
        MenuItem menuLists = menuNav.findItem((R.id.list_tracks));
        MenuItem menuInterfaceLanguage = menuNav.findItem(R.id.interface_language);
        MenuItem menuNotifications = menuNav.findItem(R.id.notifications);
        MenuItem menuDownloadLimits = menuNav.findItem(R.id.download_limits);
        MenuItem menuMyProfile = menuNav.findItem(R.id.my_profile);
        MenuItem menuHistory = menuNav.findItem(R.id.list_history);
        MenuItem menuPlayNext = menuNav.findItem(R.id.play_next);
        MenuItem menuFavourite = menuNav.findItem(R.id.list_favourite);
        MenuItem menuMyList = menuNav.findItem(R.id.my_list);
        MenuItem menuPartners = menuNav.findItem(R.id.list_partners);
        MenuItem menuAboutUs = menuNav.findItem(R.id.about_us);
        MenuItem menuContactUS = menuNav.findItem(R.id.feedback_contact);
        MenuItem menuFollowUS = menuNav.findItem(R.id.follow_us);
        MenuItem menuLogOut = menuNav.findItem(R.id.logout);
        MenuItem menuMyAccount = menuNav.findItem(R.id.my_account);
        MenuItem menuGeneral = menuNav.findItem(R.id.general);
        MenuItem menuListsTitle = menuNav.findItem(R.id.lists);


        menuUpgradeToPremium.setTitle(Html.fromHtml("<b><font color='#84754B'>" + getResources().getString(R.string.title_upgrade_to_premium) +  "</font></b>"));
        menuLists.setTitle(Html.fromHtml("<b><font color='#535353'>" + getResources().getString(R.string.title_lists) +  "</font></b>"));
        menuInterfaceLanguage.setTitle(Html.fromHtml("<font color='#535353'>" + getResources().getString(R.string.title_interface_language) +  "</font></b>"));
        menuNotifications.setTitle(Html.fromHtml("<font color='#535353'>" + getResources().getString(R.string.title_notifications) +  "</font>"));
        menuDownloadLimits.setTitle(Html.fromHtml("<font color='#535353'>" + getResources().getString(R.string.title_donwload_limits) +  "</font>"));
        menuMyProfile.setTitle(Html.fromHtml("<font color='#535353'>" + getResources().getString(R.string.title_my_profile) +  "</font>"));
        menuHistory.setTitle(Html.fromHtml("<font color='#535353'>" + getResources().getString(R.string.title_history) +  "</font>"));
        menuPlayNext.setTitle(Html.fromHtml("<font color='#535353'>" + getResources().getString(R.string.title_play_next) +  "</font>"));
        menuFavourite.setTitle(Html.fromHtml("<font color='#535353'>" + getResources().getString(R.string.title_favourite) +  "</font>"));
        menuMyList.setTitle(Html.fromHtml("<font color='#535353'>" + getResources().getString(R.string.title_my_list) +  "</font>"));
        menuPartners.setTitle(Html.fromHtml("<font color='#535353'>" + getResources().getString(R.string.title_partners) +  "</font>"));
        menuAboutUs.setTitle(Html.fromHtml("<font color='#535353'>" + getResources().getString(R.string.title_about_us) +  "</font>"));
        menuContactUS.setTitle(Html.fromHtml("<font color='#535353'>" + getResources().getString(R.string.title_contact_us) +  "</font>"));
        menuFollowUS.setTitle(Html.fromHtml("<font color='#535353'>" + getResources().getString(R.string.title_follow_us) +  "</font>"));
        menuLogOut.setTitle(Html.fromHtml("<font color='#535353'>" + getResources().getString(R.string.logout) +  "</font>"));
        menuMyAccount.setTitle(Html.fromHtml("<b><font color='#535353'><big>" + getResources().getString(R.string.title_my_account) + "</big></font></b>"));
        menuGeneral.setTitle(Html.fromHtml("<b><font color='#535353'><big>" + getResources().getString(R.string.title_general) + "</big></font></b>"));
        menuListsTitle.setTitle(Html.fromHtml("<b><font color='#535353'><big>" + getResources().getString(R.string.title_lists) + "</big></font></b>"));





        // CHANGE ACTION VIEW FOR MENU ITEMS
        String lang = storageManager.getLanguageUIPreference(this);
        if(lang.equals("ar"))
        {
            MenuItem upgrade_to_premium = menuNav.findItem(R.id.upgrade_to_premium);
            setActionLayout(upgrade_to_premium);

            menuLists = menuNav.findItem((R.id.list_tracks));
            setActionLayout(menuLists);

            MenuItem interface_language = menuNav.findItem(R.id.interface_language);
            setActionLayout(interface_language);

            MenuItem list_partners = menuNav.findItem((R.id.list_partners));
            setActionLayout(list_partners);

            MenuItem notifications = menuNav.findItem(R.id.notifications);
            setActionLayout(notifications);

            MenuItem download_limits = menuNav.findItem((R.id.download_limits));
            setActionLayout(download_limits);

            MenuItem about_us = menuNav.findItem(R.id.about_us);
            setActionLayout(about_us);

            MenuItem feedback_contact = menuNav.findItem((R.id.feedback_contact));
            setActionLayout(feedback_contact);

            MenuItem follow_us = menuNav.findItem(R.id.follow_us);
            setActionLayout(follow_us);

            MenuItem list_history = menuNav.findItem((R.id.list_history));
            setActionLayout(list_history);

            MenuItem play_next = menuNav.findItem(R.id.play_next);
            setActionLayout(play_next);

            MenuItem list_favourite = menuNav.findItem((R.id.list_favourite));
            setActionLayout(list_favourite);

            MenuItem my_list = menuNav.findItem(R.id.my_list);
            setActionLayout(my_list);

            MenuItem logout = menuNav.findItem((R.id.logout));
            setActionLayout(logout);

            //Abd edit
            MenuItem my_profile = menuNav.findItem((R.id.my_profile));
            setActionLayout(my_profile);
            //Abd edit

            /*interface_language.setActionView(view);list_partners.setActionView(view);
            notifications.setActionView(view);download_limits.setActionView(view);
            about_us.setActionView(view);feedback_contact.setActionView(view);
            follow_us.setActionView(view);list_history.setActionView(view);
            play_next.setActionView(view); list_favourite.setActionView(view);
            my_list.setActionView(view); logout.setActionView(view);*/
        }
        if(isDemoUser)
        {
            MenuItem logout = menuNav.findItem((R.id.logout));
            logout.setTitle(Html.fromHtml(""+getResources().getString(R.string.sign_in)));
        }

        /*View v = menuTest.getActionView();
        TextView gg =  v.findViewById(R.id.hhhhhh);
        gg.setBackgroundColor(getResources().getColor(R.color.tevoiBrownDark));
        menuTest.setActionView(v);*/
        //SpannableString spanString = new SpannableString(menuTest.getTitle().toString());
        //spanString.setSpan(new ForegroundColorSpan(Color.RED), 0,     spanString.length(), 0); //fix the color to white
        //dd.setTitle(Html.fromHtml("<font color='#ff3824'>Settings</font>"));

        /*View v = findViewById(R.id.testLis);
        dd.setActionView(v);*/
        //menuTest.setActionView(v);

        //endregion


        //region section for silde panel

        mLayout =(SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i("LogPanel", "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i("LogPanel", "onPanelStateChanged " + newState);
                if(newState == SlidingUpPanelLayout.PanelState.COLLAPSED)
                {
                    CurrentFragmentName = PreviousFragmentName;
                    mSubTitle.setText(CurrentFragmentName);
                    if(CurrentFragmentName.equals(Global.ListTracksFragmentName))
                    {
                        if(IsFilterChanged)
                        {
                            lisTracksFragment.getRefreshListTrack();
                        }
                    }
                }
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                Log.i("LogPanel", "setFadeOnClickListener " );
            }
        });

        //endregion



        //setContentView(R.layout.activity_main);

        // region media player runnable action
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isActivityPause)
                {
                    if (serviceBound && player != null && player.mMediaPlayer != null)
                    {
                        txtTrackName.setText(CurrentTrackInPlayer.getName().toString());

                        if(IsListenDailyLimitsExceeded)
                        {

                        }
                        else
                        {

                        }

                        if (!player.mMediaPlayer.isPlaying())
                        {
                            if (btnPausePlayMainMediaPlayer != null)
                                btnPausePlayMainMediaPlayer.setImageResource(R.mipmap.play_white_normal);
                            mProgressDialog.dismiss();
                        }
                        if (player.mMediaPlayer.isPlaying()) {
                            mainPlayerLayout.setVisibility(View.VISIBLE);

                            if (btnPausePlayMainMediaPlayer != null)
                                btnPausePlayMainMediaPlayer.setImageResource(R.mipmap.pause_normal_white);
                            numberOfListenedSeconds += 1;
                            numberOfCurrentSecondsInTrack += 1;
                            //activity.numberOfTotalSeconds += activity.numberOfCurrentSeconds;
                        }
                        int n = numberOfUnitsSendToServer * Global.ListenUnitInSeconds + Global.ListenUnitInSeconds;
                        if (numberOfListenedSeconds >= n)
                        {
                            int numberOfUnRegisteredSeconds = numberOfListenedSeconds - numberOfUnitsSendToServer * Global.ListenUnitInSeconds;
                            final int numberOfConsumedUnits = numberOfUnRegisteredSeconds / Global.ListenUnitInSeconds;

                        }
                        if (seekBarMainPlayer == null)
                            seekBarMainPlayer = findViewById(R.id.seekBar_main_player);

                        seekBarMainPlayer.setMax(player.mMediaPlayer.getDuration() / 1000);
                        String timeFormat2 = HelperFunctions.GetTimeFormat(player.mMediaPlayer.getDuration() / 1000);
                        txtFinishTime.setText(timeFormat2);
                        int mCurrentPosition = player.mMediaPlayer.getCurrentPosition() / 1000;

                        seekBarMainPlayer.setProgress(mCurrentPosition);
                        String timeFormat = HelperFunctions.GetTimeFormat(mCurrentPosition);
                        txtCurrentTime.setText(timeFormat);
                    } else
                    {
                        if (serviceBound && player != null && player.mMediaPlayer != null)
                            player.mMediaPlayer.pause();
                        if (btnPausePlayMainMediaPlayer != null)
                            btnPausePlayMainMediaPlayer.setImageResource(R.mipmap.play_white_normal);
                    }
                    mHandler.postDelayed(this, 1000);
                }
            }
        });


        // endregion

        Global.client = ApiClient.getClient(this).create(ApiInterface.class);

        //initActionBar("Start");



        trackIdPlayedNow = -1;
        searchBtn = findViewById(R.id.action_search);
        mProgressDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setCancelable(false);


        // region load shared prefereces
        playNowListTracks = storageManager.loadPlayNowTracks(SideMenu.this);

        // endregion


        // get User subscription Info

        getUserSubscriptionInfo();

        mediaPlayerFragment = new MediaPlayerFragment();

       /*
       // region initialize drawer
        mTitle = (String) getTitle();
        // Getting reference to the DrawerLayout
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.drawer_list);
        // Getting reference to the ActionBarDrawerToggle
        mDrawerToggle = new ActionBarDrawerToggle( this,
                mDrawerLayout,
                R.drawable.outline_reorder_rotated_24,
                R.string.drawer_open,
                R.string.drawer_close){

            *//** Called when drawer is closed *//*
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            *//** Called when a drawer is opened *//*
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle("Select");
                invalidateOptionsMenu();
            }
        };
        // Setting DrawerToggle on DrawerLayout
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        *//*ArrayList<DrawerListItemObject> objs = new ArrayList<>();
        for (int i =0 ; i< getResources().getStringArray(R.array.rivers).length; i++)
        {
            DrawerListItemObject temp = new DrawerListItemObject();
            temp.setName(getResources().getStringArray(R.array.rivers)[i]);
            objs.add(temp);
        }
        mDrawerList.setAdapter(new DrawerListAdapter(this, R.layout.drawer_list_item, objs));*//*

        // Creating an ArrayAdapter to add items to the listview mDrawerList
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getBaseContext(),
                R.layout.drawer_list_item ,
                getResources().getStringArray(R.array.rivers)
        );
        // Setting the adapter on mDrawerList
        mDrawerList.setAdapter(adapter);
        // endregion*/


        // Enabling Home button
        //getActionBar().setHomeButtonEnabled(true);
        //getActionBar().setTitle("Tevoi");
        //getActionBar().setSubtitle("subtitle");
        //
        // Enabling Up navigation
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        // Setting item click listener for the listview mDrawerList
       /* mDrawerList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {
                if(serviceBound)
                {
                    if (!player.mMediaPlayer.isPlaying()) {
                        isPlaying = false;
                        isPaused = true;
                        player.mMediaPlayer.pause();
                    }
                }

                // Getting an array of rivers
                String[] rivers = getResources().getStringArray(R.array.rivers);

                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                //Currently selected river
                mTitle = rivers[position]; initActionBar(mTitle);
                switch(mTitle)
                {
                    case "Test Pagination":
                    {
                        fragmentTransaction.replace(R.id.content_frame, listTracksFragment);
                        fragmentTransaction.addToBackStack( "History" );
                        fragmentTransaction.commit();
                        break;
                    }
                    case "History" :
                    {
                        fragmentTransaction.replace(R.id.content_frame, historyListFragment);
                        fragmentTransaction.addToBackStack( "History" );
                        fragmentTransaction.commit();
                        break;
                    }
                    case "Play Next" :
                    {
                        fragmentTransaction.replace(R.id.content_frame, playingNowFragment);
                        fragmentTransaction.addToBackStack( "Play Next" );
                        fragmentTransaction.commit();
                        break;
                    }
                    case "Favourite" :
                    {
                        fragmentTransaction.replace(R.id.content_frame, favouriteFragment);
                        fragmentTransaction.addToBackStack( "Favourite" );
                        fragmentTransaction.commit();
                        break;
                    }
                    case "My Lists" :
                    {
                        fragmentTransaction.replace(R.id.content_frame, userListsFragment);
                        fragmentTransaction.addToBackStack( "My Lists" );
                        fragmentTransaction.commit();
                        break;
                    }
                    case "Interface Language" :
                    {
                        fragmentTransaction.replace(R.id.content_frame, interfaceLanguageFragment);
                        fragmentTransaction.addToBackStack( "Interface Language" );
                        fragmentTransaction.commit();
                        break;
                    }
                    case "Partners" :
                    {
                        fragmentTransaction.replace(R.id.content_frame, partnersFragment);
                        fragmentTransaction.addToBackStack( "Partners" );
                        fragmentTransaction.commit();
                        break;
                    }
                    case "Notifications" :
                    {
                        fragmentTransaction.replace(R.id.content_frame, notificationFragment);
                        fragmentTransaction.addToBackStack( "Notifications" );
                        fragmentTransaction.commit();
                        break;
                    }
                    case "Download limits" :
                    {
                        fragmentTransaction.replace(R.id.content_frame, downloadFragment);
                        fragmentTransaction.addToBackStack( "Download limits" );
                        fragmentTransaction.commit();
                        break;
                    }
                    case "About Us" :
                    {
                        fragmentTransaction.replace(R.id.content_frame, aboutUsFragment);
                        fragmentTransaction.addToBackStack( "About Us" );
                        fragmentTransaction.commit();
                        break;
                    }
                    case "Feedback and Contact" :
                    {
                        fragmentTransaction.replace(R.id.content_frame, feedbackFragment);
                        fragmentTransaction.addToBackStack( "Feedback and Contact" );
                        fragmentTransaction.commit();
                        break;
                    }
                    case "Follow Us" :
                    {
                        fragmentTransaction.replace(R.id.content_frame, followUsFragment);
                        fragmentTransaction.addToBackStack( "Follow Us" );
                        fragmentTransaction.commit();
                        break;
                    }
                    case "List Tracks" :
                    {
                        fragmentTransaction.replace(R.id.content_frame, lisTracksFragment);
                        fragmentTransaction.addToBackStack( "List Tracks" );
                        fragmentTransaction.commit();
                        break;
                    }
                    case "Filters" :
                    {
                        fragmentTransaction.replace(R.id.content_frame, filterFragment);
                        fragmentTransaction.addToBackStack( "Filters" );
                        fragmentTransaction.commit();
                        break;
                    }
                    case "Login/Register":
                    {
                        fragmentTransaction.replace(R.id.content_frame, loginFragment);
                        fragmentTransaction.addToBackStack( "Login/Register" );
                        fragmentTransaction.commit();
                        break;
                    }
                    default:
                    {
                        // Creating a fragment object
                        SideMenuFragment rFragment = new SideMenuFragment();

                        // Creating a Bundle object
                        Bundle data = new Bundle();

                        // Setting the index of the currently selected item of mDrawerList
                        data.putInt("position", position);

                        // Setting the position to the fragment
                        rFragment.setArguments(data);

                        //FragmentManager fragmentManager = getFragmentManager();
                        //FragmentTransaction ft = fragmentManager.beginTransaction();

                        // Adding a fragment to the fragment transaction
                        fragmentTransaction.replace(R.id.content_frame, rFragment);
                        fragmentTransaction.addToBackStack( "rFragment" );

                        // Committing the transaction
                        fragmentTransaction.commit();
                        break;
                    }
                }


                // Closing the drawer
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });*/



        androidx.fragment.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        //TrackShare frag = new TrackShare();
        mSubTitle.setText(R.string.title_list_tracks);
        ft.replace(R.id.content_frame, lisTracksFragment);
        String mSubTitleCurrent = getResources().getString(R.string.title_list_tracks);
        ft.addToBackStack(mSubTitleCurrent);
        // Committing the transaction
        ft.commit();


        /*Intent intent = getIntent();
        try
        {
            // open activity from notification
            String action = intent.getAction().toUpperCase();
            String f  ="";

            int g = player.TrackId;

            if(CurrentTrackInPlayer != null)
            {
                mediaPlayerFragment.currentTrackId = CurrentTrackInPlayer.getId();
                mediaPlayerFragment.currentTrack = CurrentTrackInPlayer;
                ft.replace(R.id.content_frame, mediaPlayerFragment);
                String mSubTitleCurrent = getResources().getString(R.string.title_list_tracks);
                ft.addToBackStack(mSubTitleCurrent);
            }
            else
            {
                ft.replace(R.id.content_frame, lisTracksFragment);
                String mSubTitleCurrent = getResources().getString(R.string.title_list_tracks);
                ft.addToBackStack(mSubTitleCurrent);
            }
        }
        catch (Exception exc)
        {
            ft.replace(R.id.content_frame, lisTracksFragment);
            String mSubTitleCurrent = getResources().getString(R.string.title_list_tracks);
            ft.addToBackStack(mSubTitleCurrent);
        }
*/

    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            BackBtnAction();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem m = menu.getItem(0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_search)
        {
            //SideMenu activity = (SideMenu)getBaseContext();
            LinearLayout layout = findViewById(R.id.test_linear);
            RelativeLayout rlayout = findViewById(R.id.relativeLayoutSearch);
            if (layout != null)
            {
                if (layout.getVisibility() == View.GONE)
                    layout.setVisibility(View.VISIBLE);
                else
                    layout.setVisibility(View.GONE);
            }
            else
            {
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                //TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
                TextView mSubTitle = (TextView) toolbar.findViewById(R.id.toolbar_subtitle);
                mSubTitle.setText(R.string.title_list_tracks);

                androidx.fragment.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, lisTracksFragment);
                fragmentTransaction.addToBackStack(mSubTitle.getText().toString());
                fragmentTransaction.commit();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        navigateToMenuItem(id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void navigateToMenuItem(int id)
    {
        androidx.fragment.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        TextView mSubTitle = (TextView) toolbar.findViewById(R.id.toolbar_subtitle);

        switch (id)
        {
            case R.id.upgrade_to_premium:
            {
                if(isDemoUser)
                {
                    Toast.makeText(getApplicationContext(), R.string.demo_user_need_to_register, Toast.LENGTH_SHORT).show();
                    break;
                }
                else {
                    CurrentFragmentName = Global.UpgradeToPremiumFragmentName;
                    mSubTitle.setText(R.string.title_upgrade_to_premium);
                    fragmentTransaction.replace(R.id.content_frame, upgradeToPremiumFragment);
                    fragmentTransaction.addToBackStack(mSubTitle.getText().toString());
                    fragmentTransaction.commit();
                    break;
                }
            }
            case R.id.list_history: {
                if (isDemoUser) {
                    Toast.makeText(getApplicationContext(), R.string.demo_user_need_to_register, Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    CurrentFragmentName = Global.HistoryFragmentName;
                    mSubTitle.setText(R.string.title_history);
                    fragmentTransaction.replace(R.id.content_frame, historyListFragment);
                    fragmentTransaction.addToBackStack(mSubTitle.getText().toString());
                    fragmentTransaction.commit();
                    break;
                }
            }
            case R.id.play_next: {
                if(isDemoUser)
                {
                    Toast.makeText(getApplicationContext(), R.string.demo_user_need_to_register, Toast.LENGTH_SHORT).show();
                    break;
                }
                else {
                    CurrentFragmentName = Global.PlayNextListFragment;
                    mSubTitle.setText(R.string.title_play_next);
                    fragmentTransaction.replace(R.id.content_frame, playingNowFragment);
                    fragmentTransaction.addToBackStack(mSubTitle.getText().toString());
                    fragmentTransaction.commit();
                    break;
                }
            }
            case R.id.list_favourite: {
                if(isDemoUser)
                {
                    Toast.makeText(getApplicationContext(), R.string.demo_user_need_to_register, Toast.LENGTH_SHORT).show();
                    break;
                }
                else {
                    CurrentFragmentName = Global.FavouriteFragmentName;
                    mSubTitle.setText(R.string.title_favourite);
                    fragmentTransaction.replace(R.id.content_frame, favouriteFragment);
                    fragmentTransaction.addToBackStack(mSubTitle.getText().toString());
                    fragmentTransaction.commit();
                    break;
                }
            }
            case R.id.my_list: {
                if(isDemoUser)
                {
                    Toast.makeText(getApplicationContext(), R.string.demo_user_need_to_register, Toast.LENGTH_SHORT).show();
                    break;
                }
                else {
                    CurrentFragmentName = Global.UserListsFragment;
                    mSubTitle.setText(R.string.title_my_list);
                    fragmentTransaction.replace(R.id.content_frame, userListsFragment);
                    fragmentTransaction.addToBackStack(mSubTitle.getText().toString());
                    fragmentTransaction.commit();
                    break;
                }
            }
            case R.id.interface_language:
                {
                    if(isDemoUser)
                    {
                        Toast.makeText(getApplicationContext(), R.string.demo_user_need_to_register, Toast.LENGTH_SHORT).show();
                        break;
                    }
                    else {
                        CurrentFragmentName = Global.InterfaceLanguageFragmentName;
                        mSubTitle.setText(R.string.title_interface_language);
                        fragmentTransaction.replace(R.id.content_frame, interfaceLanguageFragment);
                        fragmentTransaction.addToBackStack(mSubTitle.getText().toString());
                        fragmentTransaction.commit();
                        break;
                    }
            }
            case R.id.list_partners: {
                if(isDemoUser)
                {
                    Toast.makeText(getApplicationContext(), R.string.demo_user_need_to_register, Toast.LENGTH_SHORT).show();
                    break;
                }
                else {
                    CurrentFragmentName = Global.PartnersListFragmentName;
                    mSubTitle.setText(R.string.title_partners);
                    fragmentTransaction.replace(R.id.content_frame, partnersFragment);
                    fragmentTransaction.addToBackStack(mSubTitle.getText().toString());
                    fragmentTransaction.commit();
                    break;
                }
            }
            case R.id.notifications: {
                if(isDemoUser)
                {
                    Toast.makeText(getApplicationContext(), R.string.demo_user_need_to_register, Toast.LENGTH_SHORT).show();
                    break;
                }
                else {
                    CurrentFragmentName = Global.NotificationFragmentName;
                    mSubTitle.setText(R.string.title_notifications);
                    fragmentTransaction.replace(R.id.content_frame, notificationFragment);
                    fragmentTransaction.addToBackStack(mSubTitle.getText().toString());
                    fragmentTransaction.commit();
                    break;
                }
            }
            case R.id.download_limits:
            {
                if(isDemoUser)
                {
                    Toast.makeText(getApplicationContext(), R.string.demo_user_need_to_register, Toast.LENGTH_SHORT).show();
                    break;
                }
                else {
                    CurrentFragmentName = Global.DownloadFragmentName;
                    mSubTitle.setText(R.string.title_donwload_limits);
                    fragmentTransaction.replace(R.id.content_frame, downloadFragment);
                    fragmentTransaction.addToBackStack(mSubTitle.getText().toString());
                    fragmentTransaction.commit();
                    break;
                }
            }
            case R.id.about_us: {
                CurrentFragmentName = Global.AboutUsFragmentName;
                mSubTitle.setText(R.string.title_about_us);
                fragmentTransaction.replace(R.id.content_frame, aboutUsFragment);
                fragmentTransaction.addToBackStack(mSubTitle.getText().toString());
                fragmentTransaction.commit();
                break;
            }
            case R.id.feedback_contact: {
                if(isDemoUser)
                {
                    Toast.makeText(getApplicationContext(), R.string.demo_user_need_to_register, Toast.LENGTH_SHORT).show();
                    break;
                }
                else {
                    CurrentFragmentName = Global.FeedbackFragmentName;
                    mSubTitle.setText(R.string.title_contact_us);
                    fragmentTransaction.replace(R.id.content_frame, feedbackFragment);
                    fragmentTransaction.addToBackStack(mSubTitle.getText().toString());
                    fragmentTransaction.commit();
                    break;
                }
            }
            //Abd edit
            case R.id.my_profile: {
                if(isDemoUser)
                {
                    Toast.makeText(getApplicationContext(), R.string.demo_user_need_to_register, Toast.LENGTH_SHORT).show();
                    break;
                }
                else {
                    CurrentFragmentName = Global.MyProfileFragmentName;
                    mSubTitle.setText(R.string.title_my_profile);
                    fragmentTransaction.replace(R.id.content_frame, myProfileFragment);
                    fragmentTransaction.addToBackStack(mSubTitle.getText().toString());
                    fragmentTransaction.commit();
                    break;
                }
            }
            //Abd edit
            case R.id.follow_us: {
                CurrentFragmentName = Global.FollowUsFragmentName;
                mSubTitle.setText(R.string.title_follow_us);
                fragmentTransaction.replace(R.id.content_frame, followUsFragment);
                fragmentTransaction.addToBackStack(mSubTitle.getText().toString());
                fragmentTransaction.commit();
                break;
            }
            case R.id.list_tracks: {

                CurrentFragmentName = Global.ListTracksFragmentName;
                mSubTitle.setText(R.string.title_list_tracks);
                fragmentTransaction.replace(R.id.content_frame, lisTracksFragment);
                fragmentTransaction.addToBackStack(mSubTitle.getText().toString());
                fragmentTransaction.commit();
                break;

            }
            case R.id.logout :
            {
                // TODO : logout
                // delete token
                storageManager.storeTokenPreference(this, "");
                // go to login
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                getApplicationContext().startActivity(i);
                finish(); // finish the current activity

                break;
            }
            default: {
                /*// Creating a fragment object
                SideMenuFragment rFragment = new SideMenuFragment();

                // Creating a Bundle object
                Bundle data = new Bundle();

                // Setting the index of the currently selected item of mDrawerList
                data.putInt("position", position);

                // Setting the position to the fragment
                rFragment.setArguments(data);

                //FragmentManager fragmentManager = getFragmentManager();
                //FragmentTransaction ft = fragmentManager.beginTransaction();

                // Adding a fragment to the fragment transaction
                fragmentTransaction.replace(R.id.content_frame, rFragment);
                fragmentTransaction.addToBackStack( "rFragment" );

                // Committing the transaction
                fragmentTransaction.commit();*/
                break;
            }
        }
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }


    /**
     * Called whenever we call invalidateOptionsMenu()
     *//*
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);

        menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }*/

    // region List Tracks actions
    public void changeTabToNew(View view) {
        lisTracksFragment.changeTabToNew(view);
    }

    public void changeTabToTopRated(View view) {
        lisTracksFragment.changeTabToTopRated(view);
    }

    public void changeToPopular(View view) {
        lisTracksFragment.changeToPopular(view);
    }
    // endregion

    // region media player actions
    public void playTrack(View view) {
        mediaPlayerFragment.playTrack(view);
    }

    public void imgBtnForwardClick(View view) {
        mediaPlayerFragment.imgBtnForwardClick(view);
    }

    public void imgBtnReplayClick(View view) {
        mediaPlayerFragment.imgBtnReplayClick(view);
    }

    public void imgBtnPreviuosClick(View view) {
        mediaPlayerFragment.imgBtnPreviuosClick(view);
    }

    public void imgBtnNextClick(View view) {
        mediaPlayerFragment.imgBtnNextClick(view);
    }

    public void imgBtnShuffleClick(View view) {
        mediaPlayerFragment.imgBtnShuffleClick(view);
    }


    public void imgBtnAddtoListClick(View view) {
        mediaPlayerFragment.imgBtnAddtoListClick(view);
    }

    public void imgBtnLocationClick(View view) {
        mediaPlayerFragment.imgBtnLocationClick(view);
    }

    public void imgBtnGetTrackTextClick(View view) {
        mediaPlayerFragment.imgBtnGetTrackTextClick(view);
    }

    public void imgBtnCommentClick(View view) {
        mediaPlayerFragment.imgBtnCommentClick(view);
    }

    public void imgBtnShareClick(View view) {
        mediaPlayerFragment.imgBtnShareClick(view);
    }

    public void imgBtnCarClick(View view) {
        mediaPlayerFragment.imgBtnCarClick(view);
    }

    // region actions for car play

    // endregion


    // other fragments actions
    public void locationClick(View v) {
        mediaPlayerFragment.locationClick(v);
    }

    public void shareClick(View v) {
        mediaPlayerFragment.shareClick(v);
    }

    public void addTrackToListClick(View v) {
        mediaPlayerFragment.addTrackToListClick(v);
    }

    public void addCommentBtn(View v) {
        mediaPlayerFragment.addCommentBtn(v);
    }


    // endregion


    // region actions in play now list fragment
    public void changeTabToNewPlayNow(View view) {
        playingNowFragment.changeTabToNewPlayNow(view);
    }

    public void changeTabToTopRatedPlayNow(View view) {
        playingNowFragment.changeTabToTopRatedPlayNow(view);
    }

    public void changeToPopularPlayNow(View view) {
        playingNowFragment.changeToPopularPlayNow(view);
    }
    // endregion

    // region actions in history list fragment
    public void changeTabToNewHistory(View view) {
        historyListFragment.changeTabToNewHistory(view);
    }

    public void changeTabToTopRatedHistory(View view) {
        historyListFragment.changeTabToTopRatedHistory(view);
    }

    public void changeToPopularHistory(View view) {
        historyListFragment.changeToPopularHistory(view);
    }
    // endregion

    // region actions in favourite list fragment
    public void changeTabToNewFavourite(View view) {
        favouriteFragment.changeTabToNewFavourite(view);
    }

    public void changeTabToTopRatedFavourite(View view) {
        favouriteFragment.changeTabToTopRatedFavourite(view);
    }

    public void changeToPopularFavourite(View view) {
        favouriteFragment.changeToPopularFavourite(view);
    }
    // endregion

    // region Helper functions
    public void AddTrackToList(int TrackId, SideMenu activity) {
        Call<IResponse> call = Global.client.AddTrackToUserList(TrackId, 1);
        call.enqueue(new Callback<IResponse>() {
            public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                //generateDataList(response.body());
                IResponse result = response.body();
                Toast.makeText(SideMenu.this, R.string.track_added_to_list_successfully, Toast.LENGTH_SHORT).show();
            }

            public void onFailure(Call<IResponse> call, Throwable t) {
                IResponse r = new IResponse();
                r.Message = t.getMessage();
                r.Number = -1;
                Toast.makeText(SideMenu.this, R.string.general_error, Toast.LENGTH_SHORT).show();
            }
        });

    }

    //endregion

    // region Parter fragment actions
    public void changeToAlphabetOrder(View view) {
        partnersFragment.activateTab(0);
    }

    public void changeTabToNewListPartners(View view) {
        partnersFragment.activateTab(1);
    }

    public void changeTabToTopRatedPartners(View view) {
        partnersFragment.activateTab(2);
    }

    public void changeToPopularPartners(View view) {
        partnersFragment.activateTab(3);
    }
    // endregion

    public void playAudio(String media, String Name, String Authors, int TrackId) {
        //Check is service is active
        //SideMenu activity = (SideMenu)getActivity();
        btnPausePlayMainMediaPlayer.setVisibility(View.GONE);
        playerLoader.setVisibility(View.VISIBLE);
        if (!serviceBound)
        {
            //ServiceConnection serviceConnection = serviceConnection;
            Intent playerIntent = new Intent(SideMenu.this, CustomMediaPlayerService.class);
            playerIntent.putExtra("media", media);
            playerIntent.putExtra("TrackName", Name);
            playerIntent.putExtra("TrackAuthors", Authors);
            playerIntent.putExtra("TrackId", TrackId);
            //playerIntent.setAction(Global.ACTION.STARTFOREGROUND_ACTION);
            //playerIntent.putExtra("activityStatus", isActivityPause);
            getBaseContext().startService(playerIntent);
            getBaseContext().bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
            //mProgressDialog.show(); mProgressDialog.setCancelable(false);

        } else {
            //Service is active
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
            broadcastIntent.putExtra("media", media);
            broadcastIntent.putExtra("TrackName", Name);
            broadcastIntent.putExtra("TrackAuthors", Authors);
            broadcastIntent.putExtra("TrackId", TrackId);

            sendBroadcast(broadcastIntent);
            //player.updateStatusBarInfo(CurrentTrackInPlayer.getName(), CurrentTrackInPlayer.getAuthors());
        }
    }

    // region add notify functins for adapters
    public void notifyUserListAdapter() {
        userListsFragment.notifyUserListAdapter();
    }

    public void notifyHistoryListAdapter() {
        historyListFragment.notifyHistoryListAdapter();
    }

    public void doRefreshFavouriteList() {
        favouriteFragment.doRefresh();
    }

    public void notifyFavouriteListAdapter() {
        favouriteFragment.notifyFavouriteListAdapter();
    }

    public void notifyPlayNextListAdapter() {
        playingNowFragment.notifyPlayNextListAdapter();
    }

    public void notifyTarcksListAdapter() {
        lisTracksFragment.notifyTarcksListAdapter();
    }

    public void notifyUserListTracksAdapter() {
        userListTracksFragment.notifyUserListTracksAdapter();
    }
    // endregion

   /* @Override
    protected void onPause() {
        super.onPause();
       isActivityPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityPause = false;
    }
*/

    // region Back button action handling
   /* @Override
    public void onBackPressed()
    {
        BackBtnAction();
    }*/

    public void BackBtnAction() {

        int T = getSupportFragmentManager().getBackStackEntryCount();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            moveTaskToBack(true);
            //finish();
        } else if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            //Toast.makeText(this, "hi count 1", Toast.LENGTH_SHORT).show();
            moveTaskToBack(true);
            //finish();
        } else {
            String tr = getSupportFragmentManager().getBackStackEntryAt(T - 2).getName();
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            TextView mSubTitle = (TextView) toolbar.findViewById(R.id.toolbar_subtitle);
            mSubTitle.setText(tr);
            //setTitle(tr);
            getSupportFragmentManager().popBackStackImmediate();
            //super.onBackPressed();
        }
    }
    // endregion

    // region partner tracks list actions
    public void changeTabToNewPartnerTracks(View view) {
        partnerNameFragment.changeTabToNewPartnerTracks(view);
    }

    public void changeTabToTopRatedPartnerTracks(View view) {
        partnerNameFragment.changeTabToTopRatedPartnerTracks(view);
    }

    public void changeToPopularPartnerTracks(View view) {
        partnerNameFragment.changeToPopularPartnerTracks(view);
    }

    // endregion


    public static boolean isAppInForeground(SideMenu ctx, String activityName) {
        ActivityManager activityManager = (ActivityManager) ctx
                .getApplicationContext().getSystemService(
                        Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services = activityManager
                .getRunningTasks(Integer.MAX_VALUE);

        if (services == null) {
            return false;
        }

        return services.size() > 0
                && services.get(0).topActivity
                .getPackageName()
                .toString()
                .equalsIgnoreCase(
                        activityName);

    }


    @Override
    public void playNext() {
        //Toast.makeText(this, "Clicked Next", Toast.LENGTH_SHORT).show();
        HelperFunctions.getNextTrack(this, CurrentTrackInPlayer.getId());
        //player.updateStatusBarInfo(CurrentTrackInPlayer.getName(), CurrentTrackInPlayer.getAuthors());
    }

    @Override
    public void playPrevious() {
        //Toast.makeText(this, "Clicked Previous", Toast.LENGTH_SHORT).show();
        HelperFunctions.getPreviousTrack(this, CurrentTrackInPlayer.getId());
        //player.updateStatusBarInfo(CurrentTrackInPlayer.getName(), CurrentTrackInPlayer.getAuthors());
    }

    @Override
    public  void updateUserUsage(UserSubscriptionInfoResponse response)
    {
        userSubscriptionInfo = response;
    }
    @Override
    public boolean isDemoUser()
    {
        return  isDemoUser;
    }
    @Override
    public void AddtoHistory (int Id)
    {
        storageManager.Addtohistory(this,Id);
    }
    @Override
    public boolean isExceedsQuotaDemoUser()
    {
        return  isExceedsQuotaDemoUser;
    }

    @Override
    public void addUnitsForDemoUser(int n)
    {
        int oldUnits = storageManager.getDemoUsagePreference(this);
        int newUnits = oldUnits +  n;
        storageManager.storeDemoUsagePreference(this, newUnits);
    }
    @Override
    public void setUserExceedsQuota()
    {
        isExceedsQuotaDemoUser = true;
    }




    @Override
    public int getUnitsForDemoUser()
    {
        int n =storageManager.getDemoUsagePreference(this);
        return n;
    }

    @Override
    public void setFlagUserExceedsDailyUsageListen()
    {
        IsListenDailyLimitsExceeded = true;
    }

    /*@Override
    public void playBtn() {
        //player.updateStatusBarInfo(CurrentTrackInPlayer.getName(), CurrentTrackInPlayer.getAuthors());
    }
*/
    @Override
    protected void onDestroy() {
        if (serviceBound) {
            unbindService(serviceConnection);
            //service is active
            player.stopSelf();
            player.removeNotification();
            serviceBound = false;
        }
        super.onDestroy();
    }


    public void updateSubTite(String subTitle) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mSubTitle = (TextView) toolbar.findViewById(R.id.toolbar_subtitle);
        mSubTitle.setText(subTitle);
    }


    public void getUserSubscriptionInfo()
    {
        Call<UserSubscriptionInfoResponse> call = Global.client.GetUserSubscriptionInfo();
            call.enqueue(new Callback<UserSubscriptionInfoResponse>()
            {
                @Override
                public void onResponse
                (Call < UserSubscriptionInfoResponse > call, Response < UserSubscriptionInfoResponse > response) {
                    userSubscriptionInfo = response.body();
                }

                @Override
                public void onFailure (Call < UserSubscriptionInfoResponse > call, Throwable t){
                       // TODO :
                    userSubscriptionInfo = new UserSubscriptionInfoResponse();
                }
            });
    }


    @Override
    public boolean isInternetAvailable()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public void onInternetUnavailable()
    {
        // TODO: show error page with retry button
        Log.d(  "Maaa" , CurrentFragmentName);

        if(CurrentFragmentName == Global.MediaPlayerFragmentName)
        {
            mediaPlayerFragment.ratingBar.setRating(0);
            mediaPlayerFragment.ratingBar.setIsIndicator(true);
        }
        else
        {
            androidx.fragment.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            //TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            TextView mSubTitle = (TextView) toolbar.findViewById(R.id.toolbar_subtitle);
            String noInternetConnection = getResources().getString(R.string.title_no_internet_connection);

            errorFragment.PreviousFragmentName = CurrentFragmentName;
            // mSubTitle.setText(noInternetConnection);
            fragmentTransaction.replace(R.id.content_frame, errorFragment);
            fragmentTransaction.addToBackStack(noInternetConnection);
            fragmentTransaction.commit();

        }
        Log.d("MyTagGoesHere", "This is my log message at the debug level here");
    }


    public void deleteTrackFromPlayNowList(int trackId)
    {
        storageManager.removeTrackById(SideMenu.this, trackId);
        playNowListTracks = storageManager.loadPlayNowTracks(SideMenu.this);

    }
    public  void AddListenTrack(int trackId)
    {
        Call<UserSubscriptionInfoResponse> call = Global.client.AddListenTrackWithQuota(trackId);
        call.enqueue(new Callback<UserSubscriptionInfoResponse>()
        {
            @Override
            public void onResponse
                    (Call < UserSubscriptionInfoResponse > call, Response < UserSubscriptionInfoResponse > response) {
                userSubscriptionInfo = response.body();
            }

            @Override
            public void onFailure (Call < UserSubscriptionInfoResponse > call, Throwable t){
                // TODO :
                userSubscriptionInfo = new UserSubscriptionInfoResponse();
            }
        });
    }

    @Override
    public void showLoaderMediaPlayer()
    {
        mProgressDialog.setMessage(getResources().getString( R.string.loader_msg));
        mProgressDialog.show();
        mProgressDialog.setCancelable(false);
        isLoaderVisible = true;
    }

    @Override
    public void hideLoaderMediaPlayer()
    {
        btnPausePlayMainMediaPlayer.setVisibility(View.VISIBLE);
        playerLoader.setVisibility(View.GONE);
        //mProgressDialog.dismiss();
        //isLoaderVisible =  false;
    }
    boolean isLoaderVisible = false;
    @Override
    public boolean isLoaderVisible()
    {
        return isLoaderVisible;
    }


    private void initiateArabicMenu(Menu menuNav)
    {
        MenuItem upgrade_to_premium = menuNav.findItem(R.id.upgrade_to_premium);
        MenuItem menuLists = menuNav.findItem((R.id.list_tracks));
        MenuItem interface_language = menuNav.findItem(R.id.interface_language);
        MenuItem list_partners = menuNav.findItem((R.id.list_partners));
        MenuItem notifications = menuNav.findItem(R.id.notifications);
        MenuItem download_limits = menuNav.findItem((R.id.download_limits));
        MenuItem about_us = menuNav.findItem(R.id.about_us);
        MenuItem feedback_contact = menuNav.findItem((R.id.feedback_contact));
        MenuItem follow_us = menuNav.findItem(R.id.follow_us);
        MenuItem list_history = menuNav.findItem((R.id.list_history));
        MenuItem play_next = menuNav.findItem(R.id.play_next);
        MenuItem list_favourite = menuNav.findItem((R.id.list_favourite));
        MenuItem my_list = menuNav.findItem(R.id.my_list);
        MenuItem logout = menuNav.findItem((R.id.logout));

        //Abd edit
        MenuItem my_profile = menuNav.findItem((R.id.my_profile));
        //Abd edit


        View view = View.inflate(this, R.layout.drawer_list_item_ar, null);

        upgrade_to_premium.setActionView(view);menuLists.setActionView(view);
        interface_language.setActionView(view);list_partners.setActionView(view);
        notifications.setActionView(view);download_limits.setActionView(view);
        about_us.setActionView(view);feedback_contact.setActionView(view);
        follow_us.setActionView(view);list_history.setActionView(view);
        play_next.setActionView(view); list_favourite.setActionView(view);
        my_list.setActionView(view); logout.setActionView(view);


    }

    private  void setActionLayout(MenuItem m)
    {
        View view = View.inflate(this, R.layout.drawer_list_item_ar, null);
        m.setActionView(view);
    }

    public  boolean isPlay()
    {
        return player.mMediaPlayer.isPlaying();
    }
    public void pausePlayer()
    {
        player.mMediaPlayer.pause();
    }
}
