package com.tevoi.tevoi;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivityOld extends FragmentActivity {

    // Within which the entire activity is enclosed
    DrawerLayout mDrawerLayout;

    // ListView represents Navigation Drawer
    ListView mDrawerList;

    // ActionBarDrawerToggle indicates the presence of Navigation Drawer in the action bar
    ActionBarDrawerToggle mDrawerToggle;

    // Title of the action bar
    String mTitle="";

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Creating an ArrayAdapter to add items to the listview mDrawerList
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getBaseContext(),
                R.layout.drawer_list_item ,
                getResources().getStringArray(R.array.rivers)
        );

        // Setting the adapter on mDrawerList
        mDrawerList.setAdapter(adapter);

        // Enabling Home button
        getActionBar().setHomeButtonEnabled(true);

        // Enabling Up navigation
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Setting item click listener for the listview mDrawerList
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {

                // Getting an array of rivers
                String[] rivers = getResources().getStringArray(R.array.rivers);

                //Currently selected river
                mTitle = rivers[position];

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
                //ft.replace(R.id.content_frame, rFragment);

                // Committing the transaction
                ft.commit();

                // Closing the drawer
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });

    }
    public void onPrepared(MediaPlayer player) {
        player.start();
    }
    public void abdPlay(View view) {
        try {
            Uri uir = Uri.parse("http://192.168.1.103/FileServer/api/Files/Get?fileName=1.mp3");
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(getApplicationContext(), uir);
            mediaPlayer.prepare();
            mediaPlayer.start();
            //this.mediaPlayer.setDataSource(MainActivity.this, uir);
            Toast.makeText(this, "Mp created", Toast.LENGTH_SHORT);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("HELLOOOOO", "play: "+e.getStackTrace());
            Toast.makeText(this, e.getStackTrace().toString(), Toast.LENGTH_LONG);

        }
    }

    public void onLooginButtonClick(View view) {

    }

    public void onLoginButtonClick(View view) {
        Toast.makeText(this,"hi",Toast.LENGTH_SHORT);
        Intent i = new Intent(this,MediaPlayerActivity.class);
        //i.putExtra("media","http://192.168.1.111/FileServer/api/Files/Get?fileName=1.mp3");
        startActivity(i);

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
}
