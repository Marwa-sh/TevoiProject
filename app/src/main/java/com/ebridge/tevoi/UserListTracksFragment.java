package com.ebridge.tevoi;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.ebridge.tevoi.Utils.Global;
import com.ebridge.tevoi.adapter.Track;
import com.ebridge.tevoi.adapter.TracksAdapter;
import com.ebridge.tevoi.model.TrackResponseList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListTracksFragment extends Fragment
{
    ProgressDialog mProgressDialog;
    ArrayList<Track> mTracks = new ArrayList<>();
    TracksAdapter adapter ;
    RecyclerView[] recyclerViews= new RecyclerView[3];
    int active_tab=0;
    Button[] tabs =  new Button[3];
    public int defaultTab;
    SideMenu activity;

    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_user_list_track, container, false);
        mProgressDialog = new ProgressDialog(getActivity());
        activity  = (SideMenu)getActivity();

        tabs[0] = rootView.findViewById(R.id.btnNewUserListTracks);
        tabs[1]= rootView.findViewById(R.id.btnTopRatedUserListTracks);
        tabs[2] = rootView.findViewById(R.id.btnPopularUserListTracks);

        mTracks = new ArrayList<>();
        mTracks.add(new Track());
        mTracks.add(new Track());
        mTracks.add(new Track());
        mTracks.add(new Track());
        mTracks.add(new Track());

        if(defaultTab < 0 || defaultTab > 2)
        {
            defaultTab =0;
        }
        //defaultTab = 0;

        recyclerViews[defaultTab] = (RecyclerView) rootView.findViewById(R.id.user_list_tracks_recycler_View);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViews[defaultTab].setLayoutManager(layoutManager);
        activateTab(defaultTab);

        return  rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
            defaultTab = getArguments().getInt("DefaultTab");
    }

    public void changeTabToNewUserListTracks(View view) {
        activateTab(0);
    }

    public void changeTabToTopRatedUserListTracks(View view) {
        activateTab(1);
    }

    public void changeToPopularUserListTracks(View view) {
        activateTab(2);
    }

    public void activateTab(int k)
    {
        mProgressDialog.setMessage("Loading");
        mProgressDialog.show();

        final int kk= k;

        for(int i=0;i<recyclerViews.length;i++)
        {
            recyclerViews[i]=null;
        }
        recyclerViews[k] = rootView.findViewById(R.id.user_list_tracks_recycler_View);
        for(int i=0;i<tabs.length;i++)
        {
            tabs[i].setBackgroundColor(ContextCompat.getColor(activity,R.color.tevoiBlueSecondary));
            //tabs[i].refreshDrawableState();
        }

        tabs[k].setBackgroundColor(ContextCompat.getColor(activity,R.color.tevoiBluePrimary));
        //tabs[k].refreshDrawableState();
        Call<TrackResponseList> call = Global.client.getListMainTrack(k, 0, 10);
        call.enqueue(new Callback<TrackResponseList>(){
            public void onResponse(Call<TrackResponseList> call, Response<TrackResponseList> response) {
                //generateDataList(response.body());
                TrackResponseList tracks=response.body();
                int x=tracks.getTrack().size();
                recyclerViews[kk].setAdapter(adapter);
                adapter = new TracksAdapter(tracks.getTrack(),activity, Global.ListTracksFragmentName);
                recyclerViews[kk].setAdapter(adapter);
                mProgressDialog.dismiss();
                //Toast.makeText(activity,"tracks:"+x, Toast.LENGTH_SHORT);
            }
            public void onFailure(Call<TrackResponseList> call, Throwable t)
            {
                mProgressDialog.dismiss();
                Toast.makeText(activity,"something went wrong", Toast.LENGTH_SHORT);
            }
        });

    }


}
