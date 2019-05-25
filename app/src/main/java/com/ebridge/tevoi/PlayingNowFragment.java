
package com.ebridge.tevoi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ebridge.tevoi.adapter.TracksSerializableAdapter;
import com.ebridge.tevoi.model.TrackSerializableObject;

import java.util.ArrayList;

public class PlayingNowFragment extends Fragment {
    TracksSerializableAdapter adapter ;
    RecyclerView recyclerView;
    SideMenu activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_playing_now_list, container, false);
        activity = (SideMenu) getActivity();

        // get list of play now tracks
        ArrayList<TrackSerializableObject> lstTracks = new ArrayList<>();

        lstTracks = activity.playNowListTracks;

        recyclerView = (RecyclerView) rootView.findViewById(R.id.tracks_play_now_recycler_View);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new TracksSerializableAdapter(lstTracks, activity);
        //recyclerView.setAdapter(adapter);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return rootView;
    }

    public void changeTabToNewPlayNow(View view)
    {
        SideMenu activity = (SideMenu) getActivity();
        activity.lisTracksFragment.defaultTab = 0;
        String[] menuItems = getResources().getStringArray(R.array.rivers);
        // Updating the action bar title
        getActivity().getActionBar().setTitle(menuItems[0]);

        android.support.v4.app.FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, activity.lisTracksFragment);
        ft.addToBackStack( "List Tracks" );
        ft.commit();

    }

    public void changeTabToTopRatedPlayNow(View view)
    {
        SideMenu activity = (SideMenu) getActivity();
        String[] menuItems = getResources().getStringArray(R.array.rivers);
        // Updating the action bar title
        getActivity().getActionBar().setTitle(menuItems[0]);

        activity.lisTracksFragment.defaultTab = 1;
        android.support.v4.app.FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, activity.lisTracksFragment);
        ft.addToBackStack( "List Tracks" );
        ft.commit();
    }

    public void changeToPopularPlayNow(View view) {
        SideMenu activity = (SideMenu) getActivity();
        String[] menuItems = getResources().getStringArray(R.array.rivers);
        // Updating the action bar title
        getActivity().getActionBar().setTitle(menuItems[0]);

        activity.lisTracksFragment.defaultTab = 2;
        android.support.v4.app.FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, activity.lisTracksFragment);
        ft.addToBackStack( "List Tracks" );
        ft.commit();
    }

    public void notifyPlayNextListAdapter()
    {
        adapter.notifyDataSetChanged();
    }

}
