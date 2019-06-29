package com.tevoi.tevoi;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.print.PrintAttributes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
/*import android.support.v7.app.ActionBar;*/
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.Utils.HelperFunctions;
import com.tevoi.tevoi.Utils.MyStorage;
import com.tevoi.tevoi.adapter.PaginationAdapterCallback;
import com.tevoi.tevoi.model.IResponse;
import com.tevoi.tevoi.model.TrackObject;
import com.tevoi.tevoi.model.TrackSerializableObject;
import com.tevoi.tevoi.rest.ApiClient;
import com.tevoi.tevoi.rest.ApiInterface;

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
        implements  ServiceCallbacks , NavigationView.OnNavigationItemSelectedListener {

    public ProgressDialog mProgressDialog;

    // region user usage from quota
    int numberOfTotalSeconds;
    int numberOfCurrentSeconds;
    int numberOfUnitsSendToServer;
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
            Toast.makeText(SideMenu.this, "Service Bound", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    // endregion

    // region
    // main player
    public LinearLayout mainPlayerLayout;
    public TrackObject CurrentTrackInPlayer;

    SeekBar seekBarMainPlayer;
    TextView txtCurrentTime;
    TextView txtFinishTime;
    public TextView txtTrackName;
    ImageButton btnPausePlayMainMediaPlayer;

    public boolean  isActivityPause  = false;


    // endregion

    // region properties for drawer

   /* DrawerLayout mDrawerLayout;
    // ListView represents Navigation Drawer
    ListView mDrawerList;
    // ActionBarDrawerToggle indicates the presence of Navigation Drawer in the action bar
    ActionBarDrawerToggle mDrawerToggle;*/
    private DrawerLayout drawer;
    private NavigationView navigationView;

    // Title of the action bar
    String mTitle="";
    // endregion

    // region side menu fragments objects
    public TracksList lisTracksFragment = new TracksList();
   // TracksList listTracksFargment = new TracksList();
    InterfaceLanguageFragment interfaceLanguageFragment = new InterfaceLanguageFragment();
    LoginFragment loginFragment = new LoginFragment();
    RegisterFragment registerFragment = new RegisterFragment();
    PartnersFragment partnersFragment = new PartnersFragment();
    public PartnerNameFragment partnerNameFragment = new PartnerNameFragment();
    NotificationFragment notificationFragment = new NotificationFragment();
    DownloadFragment downloadFragment = new DownloadFragment();
    AboutUsFragment aboutUsFragment = new AboutUsFragment();
    FeedbackFragment feedbackFragment = new FeedbackFragment();
    FollowUsFragment followUsFragment = new FollowUsFragment();

    HistoryListFragment historyListFragment = new HistoryListFragment();
    PlayingNowFragment playingNowFragment = new PlayingNowFragment();
    FavouriteFragment favouriteFragment = new FavouriteFragment();
    MyListFragment myListFragment = new MyListFragment();
    UserListFragment userListsFragment = new UserListFragment();
    FilterFragment filterFragment = new FilterFragment();

    public UserListTracksFragment userListTracksFragment = new UserListTracksFragment();
    public MediaPlayerFragment mediaPlayerFragment;


    // endregion

    ListTracksFragment listTracksFragment = new ListTracksFragment();

    //
    MenuItem searchBtn ;

    public void initActionBar(String subTitle)
    {
        final ActionBar abar =  getActionBar();
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
    private void setfullwidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        params.width = displayMetrics.widthPixels;
        navigationView.setLayoutParams(params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        /* setActionBar(toolbar);
        getActionBar().setDisplayShowTitleEnabled(false);*/

        //TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        //TextView mSubTitle = (TextView) toolbar.findViewById(R.id.toolbar_subtitle);

        setSupportActionBar(toolbar);
        //mTitle.setText("Tevoi");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setfullwidth();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemBackground(getDrawable(R.drawable.divider));


        storageManager = new MyStorage(Global.CurrentUserId);
        //setContentView(R.layout.activity_main);

        // region media player runnable action
        this.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                /*boolean isForground = isAppInForeground(SideMenu.this, "ComponentInfo{com.ebridge.tevoi/com.ebridge.tevoi.SideMenu}");
                if(isForground==true){
                   // Toast.makeText(getBaseContext(),"Activity is in foreground, active",1000).show();
                    isActivityPause = false;
                }
                else
                {
                    isActivityPause = true;
                }*/

                if (!isActivityPause)
                {
                    if (serviceBound && player != null && player.mMediaPlayer != null)
                    {
                        txtTrackName.setText(CurrentTrackInPlayer.getName().toString());
                        mainPlayerLayout.setVisibility(View.VISIBLE);
                        //player.updateStatusBarInfo(CurrentTrackInPlayer.getName(), CurrentTrackInPlayer.getAuthors());
                        if (!player.mMediaPlayer.isPlaying())
                        {
                            if(btnPausePlayMainMediaPlayer != null)
                                btnPausePlayMainMediaPlayer.setImageResource(R.drawable.baseline_play_arrow_24);
                            mProgressDialog.dismiss();
                        }
                        if (player.mMediaPlayer.isPlaying())
                        {
                            if(btnPausePlayMainMediaPlayer != null)
                                btnPausePlayMainMediaPlayer.setImageResource(R.drawable.baseline_pause_24);
                            numberOfListenedSeconds += 1;
                            numberOfCurrentSecondsInTrack += 1;
                            //activity.numberOfTotalSeconds += activity.numberOfCurrentSeconds;
                        }
                        int n = numberOfUnitsSendToServer * Global.ListenUnitInSeconds + Global.ListenUnitInSeconds;
                        if (numberOfListenedSeconds >= n) {
                            int numberOfUnRegisteredSeconds = numberOfListenedSeconds - numberOfUnitsSendToServer * Global.ListenUnitInSeconds;
                            final int numberOfConsumedUnits = numberOfUnRegisteredSeconds / Global.ListenUnitInSeconds;
                            // send to server that we used 1 unit
                            /*Call<IResponse> call = Global.client.AddUnitUsageForUser(CurrentTrackInPlayer.getId(), numberOfConsumedUnits);
                            call.enqueue(new Callback<IResponse>() {
                                public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                                    //generateDataList(response.body());
                                    IResponse partners = response.body();
                                    numberOfUnitsSendToServer += numberOfConsumedUnits;
                                    Toast.makeText(getBaseContext(), "" + numberOfConsumedUnits + " Unit consumed from your quota", Toast.LENGTH_SHORT).show();
                                }

                                public void onFailure(Call<IResponse> call, Throwable t) {

                                }
                            });*/
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
                    }
                    else
                    {
                        if (serviceBound && player != null && player.mMediaPlayer != null)
                            player.mMediaPlayer.pause();
                        if(btnPausePlayMainMediaPlayer != null)
                            btnPausePlayMainMediaPlayer.setImageResource(R.drawable.baseline_play_arrow_24);
                    }
                    mHandler.postDelayed(this, 1000);
                }
            }
        });


        // endregion


        //initActionBar("Start");

        //region detect language

        String language = storageManager.getLanguagePreference(this);
        if(language == null)
            language = "en";
        Resources res = getBaseContext().getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(language)); // API 17+ only.
        // Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);
        // endregion

        trackIdPlayedNow = -1;
        searchBtn = findViewById(R.id.action_search);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);


        // region load shared prefereces
        playNowListTracks  = storageManager.loadPlayNowTracks(SideMenu.this);

        // endregion

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

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        //TrackShare frag = new TrackShare();
        ft.replace(R.id.content_frame, lisTracksFragment);
        ft.addToBackStack( "List Tracks" );
        // Committing the transaction
        ft.commit();
        // init media player in main page elements

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            BackBtnAction();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_search)
        {
            //SideMenu activity = (SideMenu)getBaseContext();
            LinearLayout layout = findViewById(R.id.test_linear);
            RelativeLayout rlayout = findViewById(R.id.relativeLayoutSearch);
            if(layout!=null)
            {
                if(layout.getVisibility() == View.GONE)
                    layout.setVisibility(View.VISIBLE);
                else
                    layout.setVisibility(View.GONE);
            }
            else
            {
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, lisTracksFragment);
                fragmentTransaction.commit();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        TextView mSubTitle = (TextView) toolbar.findViewById(R.id.toolbar_subtitle);

        //mTitle.setText("Tevoi");
        switch(id)
        {
            /*case R.id.test_pagination:
            {
                fragmentTransaction.replace(R.id.content_frame, listTracksFragment);
                fragmentTransaction.addToBackStack( "History" );
                fragmentTransaction.commit();
                break;
            }*/
            case R.id.list_history :
            {
                mSubTitle.setText("History");
                fragmentTransaction.replace(R.id.content_frame, historyListFragment);
                fragmentTransaction.addToBackStack( "History" );
                fragmentTransaction.commit();
                break;
            }
            case R.id.play_next :
            {
                mSubTitle.setText("Play Next");
                fragmentTransaction.replace(R.id.content_frame, playingNowFragment);
                fragmentTransaction.addToBackStack( "Play Next" );
                fragmentTransaction.commit();
                break;
            }
            case R.id.list_favourite :
            {
                mSubTitle.setText("Favourite");
                fragmentTransaction.replace(R.id.content_frame, favouriteFragment);
                fragmentTransaction.addToBackStack( "Favourite" );
                fragmentTransaction.commit();
                break;
            }
            case R.id.my_list :
            {
                mSubTitle.setText("My Lists");
                fragmentTransaction.replace(R.id.content_frame, userListsFragment);
                fragmentTransaction.addToBackStack( "My Lists" );
                fragmentTransaction.commit();
                break;
            }
            case R.id.interface_language :
            {
                mSubTitle.setText("Interface Language");
                fragmentTransaction.replace(R.id.content_frame, interfaceLanguageFragment);
                fragmentTransaction.addToBackStack( "Interface Language" );
                fragmentTransaction.commit();
                break;
            }
            case R.id.list_partners :
            {
                mSubTitle.setText("Partners");
                fragmentTransaction.replace(R.id.content_frame, partnersFragment);
                fragmentTransaction.addToBackStack( "Partners" );
                fragmentTransaction.commit();
                break;
            }
            case R.id.notifications :
            {
                mSubTitle.setText("Notifications");
                fragmentTransaction.replace(R.id.content_frame, notificationFragment);
                fragmentTransaction.addToBackStack( "Notifications" );
                fragmentTransaction.commit();
                break;
            }
            case R.id.download_limits :
            {
                mSubTitle.setText("Download limits");
                fragmentTransaction.replace(R.id.content_frame, downloadFragment);
                fragmentTransaction.addToBackStack( "Download limits" );
                fragmentTransaction.commit();
                break;
            }
            case R.id.about_us :
            {
                mSubTitle.setText("About Us");
                fragmentTransaction.replace(R.id.content_frame, aboutUsFragment);
                fragmentTransaction.addToBackStack( "About Us" );
                fragmentTransaction.commit();
                break;
            }
            case R.id.feedback_contact :
            {
                mSubTitle.setText("Feedback and Contact");
                fragmentTransaction.replace(R.id.content_frame, feedbackFragment);
                fragmentTransaction.addToBackStack( "Feedback and Contact" );
                fragmentTransaction.commit();
                break;
            }
            case R.id.follow_us :
            {
                mSubTitle.setText("Follow Us");
                fragmentTransaction.replace(R.id.content_frame, followUsFragment);
                fragmentTransaction.addToBackStack( "Follow Us" );
                fragmentTransaction.commit();
                break;
            }
            case R.id.list_tracks :
            {
                mSubTitle.setText("List Tracks");
                fragmentTransaction.replace(R.id.content_frame, lisTracksFragment);
                fragmentTransaction.addToBackStack( "List Tracks" );
                fragmentTransaction.commit();
                break;
            }
            case R.id.filters :
            {
                mSubTitle.setText("Filters");
                fragmentTransaction.replace(R.id.content_frame, filterFragment);
                fragmentTransaction.addToBackStack( "Filters" );
                fragmentTransaction.commit();
                break;
            }
            default:
            {
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /** Called whenever we call invalidateOptionsMenu() *//*
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);

        menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }*/


    // region List Tracks actions

    public void changeTabToNew(View view)
    {
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
    public void playTrack(View view)
    {
        mediaPlayerFragment.playTrack(view);
    }
    public void imgBtnForwardClick(View view)
    {
        mediaPlayerFragment.imgBtnForwardClick(view);
    }
    public void imgBtnReplayClick(View view)
    {
        mediaPlayerFragment.imgBtnReplayClick(view);
    }

    public  void imgBtnPreviuosClick(View view)
    {
        mediaPlayerFragment.imgBtnPreviuosClick(view);
    }

    public  void imgBtnNextClick(View view)
    {
        mediaPlayerFragment.imgBtnNextClick(view);
    }

    public void imgBtnAddtoListClick(View view)
    {
        mediaPlayerFragment.imgBtnAddtoListClick(view);
    }
    public void imgBtnLocationClick(View view)
    {
        mediaPlayerFragment.imgBtnLocationClick(view);
    }
    public void imgBtnGetTrackTextClick(View view) {
        mediaPlayerFragment.imgBtnGetTrackTextClick(view);
    }
    public void imgBtnCommentClick(View view)
    {
        mediaPlayerFragment.imgBtnCommentClick(view);
    }
    public void imgBtnShareClick(View view)
    {
        mediaPlayerFragment.imgBtnShareClick(view);
    }

    public void imgBtnCarClick(View view)
    {
        mediaPlayerFragment.imgBtnCarClick(view);
    }

    // region actions for car play

    // endregion


    // other fragments actions
    public void locationClick(View v)
    {
        mediaPlayerFragment.locationClick(v);
    }
    public void shareClick(View v)
    {
        mediaPlayerFragment.shareClick(v);
    }
    public void addTrackToListClick(View v)
    {
        mediaPlayerFragment.addTrackToListClick(v);
    }
    public void addCommentBtn(View v)
    {
        mediaPlayerFragment.addCommentBtn(v);
    }


    // endregion


    // region actions in play now list fragment
    public void changeTabToNewPlayNow(View view)
    {
        playingNowFragment.changeTabToNewPlayNow(view);
    }
    public void changeTabToTopRatedPlayNow(View view)
    {
        playingNowFragment.changeTabToTopRatedPlayNow(view);
    }
    public void changeToPopularPlayNow(View view) {
        playingNowFragment.changeToPopularPlayNow(view);
    }
    // endregion

    // region actions in history list fragment
    public void changeTabToNewHistory(View view)
    {
        historyListFragment.changeTabToNewHistory(view);
    }
    public void changeTabToTopRatedHistory(View view)
    {
        historyListFragment.changeTabToTopRatedHistory(view);
    }
    public void changeToPopularHistory(View view) {
        historyListFragment.changeToPopularHistory(view);
    }
    // endregion

    // region actions in favourite list fragment
    public void changeTabToNewFavourite(View view)
    {
        favouriteFragment.changeTabToNewFavourite(view);
    }
    public void changeTabToTopRatedFavourite(View view)
    {
        favouriteFragment.changeTabToTopRatedFavourite(view);
    }
    public void changeToPopularFavourite(View view) {
        favouriteFragment.changeToPopularFavourite(view);
    }
    // endregion
    
    // region Helper functions
    public void AddTrackToList(int TrackId, SideMenu activity )
    {
        ApiInterface client = ApiClient.getClient().create(ApiInterface.class);
        Call<IResponse> call = client.AddTrackToUserList(TrackId, 1);
        call.enqueue(new Callback<IResponse>(){
            public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                //generateDataList(response.body());
                IResponse result  =response.body();
                Toast.makeText(SideMenu.this, R.string.track_added_to_list_successfully, Toast.LENGTH_SHORT).show();
            }
            public void onFailure(Call<IResponse> call, Throwable t)
            {
                IResponse r =  new IResponse();
                r.Message= t.getMessage();
                r.Number = -1;
                Toast.makeText(SideMenu.this, R.string.general_error, Toast.LENGTH_SHORT).show();
            }
        });

    }
    
    //endregion

    // region Parter fragment actions
    public  void changeToAlphabetOrder(View view)
    {
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

    public void playAudio(String media, String Name , String Authors, int TrackId)
    {
        //Check is service is active
        //SideMenu activity = (SideMenu)getActivity();
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

        }
        else
        {
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
    public void notifyUserListAdapter()
    {
        userListsFragment.notifyUserListAdapter();
    }
    public void notifyHistoryListAdapter()
    {
        historyListFragment.notifyHistoryListAdapter();
    }
    public void notifyFavouriteListAdapter()
    {
        favouriteFragment.notifyFavouriteListAdapter();
    }
    public void notifyPlayNextListAdapter()
    {
        playingNowFragment.notifyPlayNextListAdapter();
    }
    public void notifyTarcksListAdapter()
    {
        lisTracksFragment.notifyTarcksListAdapter();
    }
    public  void notifyUserListTracksAdapter()
    {
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

    public  void BackBtnAction()
    {

        int T= getSupportFragmentManager().getBackStackEntryCount();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            moveTaskToBack(true);
            //finish();
        }
        else if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            //Toast.makeText(this, "hi count 1", Toast.LENGTH_SHORT).show();
            moveTaskToBack(true);
            //finish();
        }
        else
        {
            String tr = getSupportFragmentManager().getBackStackEntryAt(T-2).getName();
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


    /*@Override
    public void playBtn() {
        //player.updateStatusBarInfo(CurrentTrackInPlayer.getName(), CurrentTrackInPlayer.getAuthors());
    }
*/
    @Override
    protected void onDestroy() {
        if (serviceBound)
        {
            unbindService(serviceConnection);
            //service is active
            player.stopSelf();
            player.removeNotification();
            serviceBound = false;
        }
        super.onDestroy();
    }



    public  void updateSubTite(String subTitle)
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mSubTitle = (TextView) toolbar.findViewById(R.id.toolbar_subtitle);
        mSubTitle.setText(subTitle);
    }
}
