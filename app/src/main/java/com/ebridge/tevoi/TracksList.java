package com.ebridge.tevoi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.ebridge.tevoi.Utils.Global;
import com.ebridge.tevoi.adapter.Category;
import com.ebridge.tevoi.adapter.Track;
import com.ebridge.tevoi.adapter.TracksAdapter;
import com.ebridge.tevoi.model.IResponse;
import com.ebridge.tevoi.model.TrackResponse;
import com.ebridge.tevoi.model.TrackResponseList;
import com.ebridge.tevoi.model.UserListObject;
import com.ebridge.tevoi.rest.ApiClient;
import com.ebridge.tevoi.rest.ApiInterface;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TracksList extends Fragment implements AdapterView.OnItemSelectedListener {
    ArrayList<Track> mTracks = new ArrayList<>();
    TracksAdapter adapter ;
    RecyclerView[] recyclerViews= new RecyclerView[3];
    int active_tab=0;
    Button[] tabs =  new Button[3];
    public int defaultTab;
    SideMenu activity;
    ImageButton btnSearch;

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tracks_list, container, false);
        activity  = (SideMenu)getActivity();
        active_tab = defaultTab;
        btnSearch = rootView.findViewById(R.id.btn_search);
        if(btnSearch != null)
        {
            btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtFilter = rootView.findViewById(R.id.txt_search_filter_value);
                CheckBox chkIsLocationEnabled = rootView.findViewById(R.id.checkBoxLocationEnable);

                if(!txtFilter.getText().equals(""))
                {
                    activity.mProgressDialog.setMessage("Loading"); activity.mProgressDialog.show();

                    Call<TrackResponseList> call = Global.client.ListMainTrackWithFilter(txtFilter.getText().toString(), chkIsLocationEnabled.isChecked(), defaultTab, 0 , 10);
                    call.enqueue(new Callback<TrackResponseList>(){
                        public void onResponse(Call<TrackResponseList> call, Response<TrackResponseList> response) {
                            TrackResponseList tracks=response.body();
                            int x=tracks.getTrack().size();
                            recyclerViews[active_tab].setAdapter(adapter);
                            adapter = new TracksAdapter(tracks.getTrack(),activity, Global.ListTracksFragmentName);
                            recyclerViews[active_tab].setAdapter(adapter);
                            activity.mProgressDialog.dismiss();
                        }
                        public void onFailure(Call<TrackResponseList> call, Throwable t)
                        {
                            Toast.makeText(activity,"something went wrong", Toast.LENGTH_LONG).show();;
                            activity.mProgressDialog.dismiss();
                        }
                    });
                }

            }
        });
        }
        Spinner spinner = (Spinner) rootView.findViewById(R.id.user_lists_spinner);
        if(spinner != null)
            spinner.setOnItemSelectedListener(this);

        tabs[0] = rootView.findViewById(R.id.btnNewList);
        tabs[1]= rootView.findViewById(R.id.btnTopRatedList);
        tabs[2] = rootView.findViewById(R.id.btnPopularList);

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

        recyclerViews[defaultTab] = (RecyclerView) rootView.findViewById(R.id.tracks_recycler_View);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViews[defaultTab].setLayoutManager(layoutManager);
        activateTab(defaultTab);
/*

        Call<TrackResponseList> call = client.getListMainTrack(0,0, 10);
        call.enqueue(new Callback <TrackResponseList>(){
            public void onResponse(Call<TrackResponseList> call, Response<TrackResponseList> response) {
                //generateDataList(response.body());
                TrackResponseList tracks=response.body();
                int x=tracks.getTrack().size();
                adapter = new TracksAdapter(tracks.getTrack(),getContext());
                recyclerViews[defaultTab].setAdapter(adapter);

                Toast.makeText(getContext(),"tracks:"+x, Toast.LENGTH_SHORT);

            }
            public void onFailure(Call<TrackResponseList> call, Throwable t)
            {
                Toast.makeText(getContext(),"something went wrong", Toast.LENGTH_SHORT);
            }
        });
*/
        return  rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActivity().setContentView(R.layout.activity_tracks_list);
        //defaultTab = 0;
        if(getArguments() != null)
            defaultTab = getArguments().getInt("DefaultTab");
    }

    public void changeTabToNew(View view) {
        active_tab = 0;
        activateTab(0);
    }

    public void changeTabToTopRated(View view) {
        active_tab = 1;
        activateTab(1);
    }

    public void changeToPopular(View view) {
        active_tab = 2;
        activateTab(2);
    }

    public void activateTab(int k)
    {
        activity.mProgressDialog.setMessage("Loading");
        activity.mProgressDialog.show();

        final int kk= k;

        for(int i=0;i<recyclerViews.length;i++)
        {
            recyclerViews[i]=null;
        }
        recyclerViews[k] = rootView.findViewById(R.id.tracks_recycler_View);
        for(int i=0;i<tabs.length;i++)
        {
            tabs[i].setBackgroundColor(ContextCompat.getColor(activity,R.color.tevoiBlueSecondary));
            //tabs[i].refreshDrawableState();
        }

        tabs[k].setBackgroundColor(ContextCompat.getColor(activity,R.color.tevoiBluePrimary));
        //tabs[k].refreshDrawableState();
        Call<TrackResponseList> call = Global.client.getListMainTrack(k, 0, 10);
        call.enqueue(new Callback <TrackResponseList>(){
            public void onResponse(Call<TrackResponseList> call, Response<TrackResponseList> response) {
                //generateDataList(response.body());
                TrackResponseList tracks=response.body();
                int x=tracks.getTrack().size();
                recyclerViews[kk].setAdapter(adapter);
                adapter = new TracksAdapter(tracks.getTrack(),activity, Global.ListTracksFragmentName);
                recyclerViews[kk].setAdapter(adapter);
                activity.mProgressDialog.dismiss();
                Toast.makeText(activity,"tracks:"+x, Toast.LENGTH_SHORT);
            }
            public void onFailure(Call<TrackResponseList> call, Throwable t)
            {
                activity.mProgressDialog.dismiss();
                Toast.makeText(activity,"something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }



    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        Toast.makeText(getContext(), "Hiii there I'm marwa", Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void notifyTarcksListAdapter()
    {
        adapter.notifyDataSetChanged();
    }
}
