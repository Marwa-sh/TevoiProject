package com.ebridge.tevoi;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ebridge.tevoi.Utils.MyStorage;
import com.ebridge.tevoi.adapter.DrawerListAdapter;
import com.ebridge.tevoi.adapter.DrawerListItemObject;
import com.ebridge.tevoi.model.IResponse;
import com.ebridge.tevoi.model.TrackObject;
import com.ebridge.tevoi.model.TrackSerializableObject;
import com.ebridge.tevoi.rest.ApiClient;
import com.ebridge.tevoi.rest.ApiInterface;

import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SideMenu extends FragmentActivity {

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
    public MyStorage storageManager = new MyStorage();
    public boolean isPlayingFromPlayNowList = false;
    public int indexCurrentTrackInPlayList;
    public static final String Broadcast_PLAY_NEW_AUDIO = "com.ebridge.tevoi.MediaPlayerService.PlayNewAudio";
    // endregion

    // region service for playing media player
    public MediaPlayerService player;
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
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;

            Toast.makeText(SideMenu.this, "Service Bound", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    // endregion

    // region properties for drawer
    // Within which the entire activity is enclosed
    DrawerLayout mDrawerLayout;
    // ListView represents Navigation Drawer
    ListView mDrawerList;
    // ActionBarDrawerToggle indicates the presence of Navigation Drawer in the action bar
    ActionBarDrawerToggle mDrawerToggle;
    // Title of the action bar
    String mTitle="";
    // endregion

    // region side menu fragments objects
    TracksList lisTracksFragment = new TracksList();
   // TracksList listTracksFargment = new TracksList();
    InterfaceLanguageFragment interfaceLanguageFragment = new InterfaceLanguageFragment();
    LoginFragment loginFragment = new LoginFragment();
    PartnersFragment partnersFragment = new PartnersFragment();
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

    //
    MenuItem searchBtn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_menu);
        trackIdPlayedNow = -1;
        searchBtn = (MenuItem)findViewById(R.id.action_search);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);


        // region load shared prefereces
        playNowListTracks  = storageManager.loadPlayNowTracks(SideMenu.this);

        // endregion

        mediaPlayerFragment = new MediaPlayerFragment();

        // region initialize drawer
        mTitle = (String) getTitle();
        // Getting reference to the DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        // Getting reference to the ActionBarDrawerToggle
        mDrawerToggle = new ActionBarDrawerToggle( this,
                mDrawerLayout,
                R.drawable.outline_reorder_rotated_24,
                R.string.drawer_open,
                R.string.drawer_close){

            /** Called when drawer is closed */
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            /** Called when a drawer is opened */
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle("Select a river");
                invalidateOptionsMenu();
            }
        };
        // Setting DrawerToggle on DrawerLayout
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        /*ArrayList<DrawerListItemObject> objs = new ArrayList<>();
        for (int i =0 ; i< getResources().getStringArray(R.array.rivers).length; i++)
        {
            DrawerListItemObject temp = new DrawerListItemObject();
            temp.setName(getResources().getStringArray(R.array.rivers)[i]);
            objs.add(temp);
        }
        mDrawerList.setAdapter(new DrawerListAdapter(this, R.layout.drawer_list_item, objs));*/

        // Creating an ArrayAdapter to add items to the listview mDrawerList
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getBaseContext(),
                R.layout.drawer_list_item ,
                getResources().getStringArray(R.array.rivers)
        );
        // Setting the adapter on mDrawerList
        mDrawerList.setAdapter(adapter);
        // endregion

        // Enabling Home button
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle("Tevoi");
        getActionBar().setSubtitle("subtitle");
        //getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.launcher_background));

        // Enabling Up navigation
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Setting item click listener for the listview mDrawerList
        mDrawerList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {
                if(isPlaying)
                {
                    isPlaying = false; isPaused = true;
                    player.mMediaPlayer.pause();
                }
                // Getting an array of rivers
                String[] rivers = getResources().getStringArray(R.array.rivers);

                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                //Currently selected river
                mTitle = rivers[position];
                switch(mTitle)
                {
                    case "History" :
                    {
                        fragmentTransaction.replace(R.id.content_frame, historyListFragment);
                        fragmentTransaction.commit();
                        break;
                    }
                    case "Play Next" :
                    {
                        fragmentTransaction.replace(R.id.content_frame, playingNowFragment);
                        fragmentTransaction.commit();
                        break;
                    }
                    case "Favourite" :
                    {
                        fragmentTransaction.replace(R.id.content_frame, favouriteFragment);
                        fragmentTransaction.commit();
                        break;
                    }
                    case "My Lists" :
                    {
                        fragmentTransaction.replace(R.id.content_frame, userListsFragment);
                        fragmentTransaction.commit();
                        break;
                    }
                    case "Interface Language" :
                    {
                        fragmentTransaction.replace(R.id.content_frame, interfaceLanguageFragment);
                        fragmentTransaction.commit();
                        break;
                    }
                    case "Partners" :
                    {
                        fragmentTransaction.replace(R.id.content_frame, partnersFragment);
                        fragmentTransaction.commit();
                        break;
                    }
                    case "Notifications" :
                    {
                        fragmentTransaction.replace(R.id.content_frame, notificationFragment);
                        fragmentTransaction.commit();
                        break;
                    }
                    case "Download lismits" :
                    {
                        fragmentTransaction.replace(R.id.content_frame, downloadFragment);
                        fragmentTransaction.commit();
                        break;
                    }
                    case "About Us" :
                    {
                        fragmentTransaction.replace(R.id.content_frame, aboutUsFragment);
                        fragmentTransaction.commit();
                        break;
                    }
                    case "Feedback and Contact" :
                    {
                        fragmentTransaction.replace(R.id.content_frame, feedbackFragment);
                        fragmentTransaction.commit();
                        break;
                    }
                    case "Follow Us" :
                    {
                        fragmentTransaction.replace(R.id.content_frame, followUsFragment);
                        fragmentTransaction.commit();
                        break;
                    }
                    case "List Tracks" :
                    {
                        fragmentTransaction.replace(R.id.content_frame, lisTracksFragment);
                        fragmentTransaction.commit();
                        break;
                    }
                    case "Filters" :
                    {
                        fragmentTransaction.replace(R.id.content_frame, filterFragment);
                        fragmentTransaction.commit();
                        break;
                    }
                };
                // Creating a fragment object
                SideMenuFragment rFragment = new SideMenuFragment();

                // Creating a Bundle object
                Bundle data = new Bundle();

                // Setting the index of the currently selected item of mDrawerList
                data.putInt("position", position);

                // Setting the position to the fragment
                rFragment.setArguments(data);

                // Getting reference to the FragmentManager
                FragmentManager fragmentManager = getFragmentManager();

                // Creating a fragment transaction
                FragmentTransaction ft = fragmentManager.beginTransaction();

                // Adding a fragment to the fragment transaction
                ft.replace(R.id.content_frame, rFragment);

                // Committing the transaction
                ft.commit();

                // Closing the drawer
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        //TrackShare frag = new TrackShare();
        ft.replace(R.id.content_frame, lisTracksFragment);

        // Committing the transaction
        ft.commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    /** Handling the touch event of app icon */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();

        if (id == R.id.action_search)
        {
            //SideMenu activity = (SideMenu)getBaseContext();
            LinearLayout layout = findViewById(R.id.linearLayoutSearch);
            if(layout!=null)
            {
                if(layout.getVisibility() == View.INVISIBLE)
                    layout.setVisibility(View.VISIBLE);
                else
                    layout.setVisibility(View.INVISIBLE);
            }else
            {
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, lisTracksFragment);
                fragmentTransaction.commit();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);

        menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

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

    public void playAudio(String media)
    {
        //Check is service is active
        //SideMenu activity = (SideMenu)getActivity();
        if (!serviceBound)
        {
            //ServiceConnection serviceConnection = serviceConnection;
            Intent playerIntent = new Intent(SideMenu.this, MediaPlayerService.class);
            playerIntent.putExtra("media", media);
            getBaseContext().startService(playerIntent);
            getBaseContext().bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        } else
        {
            //Service is active
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
            broadcastIntent.putExtra("media", media);
            sendBroadcast(broadcastIntent);
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
    // endregion


}
