package com.ebridge.tevoi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ebridge.tevoi.Utils.Global;
import com.ebridge.tevoi.adapter.TracksAdapter;
import com.ebridge.tevoi.model.TrackResponseList;
import com.ebridge.tevoi.rest.ApiClient;
import com.ebridge.tevoi.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryListFragment extends Fragment {
    TracksAdapter adapter ;
    RecyclerView recyclerView;
    ApiInterface client;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_history_list, container, false);
        client = ApiClient.getClient().create(ApiInterface.class);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.history_tracks_recycler_View);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        Call<TrackResponseList> call = client.getHistoryList(0, 10);
        call.enqueue(new Callback <TrackResponseList>(){
            public void onResponse(Call<TrackResponseList> call, Response<TrackResponseList> response) {
                //generateDataList(response.body());
                SideMenu activity = (SideMenu) getActivity();
                TrackResponseList tracks=response.body();
                int x=tracks.getTrack().size();
                adapter = new TracksAdapter(tracks.getTrack(), activity, Global.HistoryFragmentName);
                //recyclerView.setAdapter(adapter);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                Toast.makeText(getContext(),"tracks:"+x, Toast.LENGTH_SHORT);
            }
            public void onFailure(Call<TrackResponseList> call, Throwable t)
            {
                Toast.makeText(getContext(),"something went wrong", Toast.LENGTH_SHORT);
            }
        });

        return  rootView;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActivity().setContentView(R.layout.activity_tracks_list);

    }

    public void changeTabToNewHistory(View view)
    {
        SideMenu activity = (SideMenu) getActivity();
        activity.lisTracksFragment.defaultTab = 0;
        String[] menuItems = getResources().getStringArray(R.array.rivers);
        // Updating the action bar title
        getActivity().getActionBar().setTitle(menuItems[0]);

        android.support.v4.app.FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, activity.lisTracksFragment);
        ft.commit();

    }

    public void changeTabToTopRatedHistory(View view)
    {
        SideMenu activity = (SideMenu) getActivity();
        String[] menuItems = getResources().getStringArray(R.array.rivers);
        // Updating the action bar title
        getActivity().getActionBar().setTitle(menuItems[0]);

        activity.lisTracksFragment.defaultTab = 1;
        android.support.v4.app.FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, activity.lisTracksFragment);
        ft.commit();
    }

    public void changeToPopularHistory(View view) {
        SideMenu activity = (SideMenu) getActivity();
        String[] menuItems = getResources().getStringArray(R.array.rivers);
        // Updating the action bar title
        getActivity().getActionBar().setTitle(menuItems[0]);

        activity.lisTracksFragment.defaultTab = 2;
        android.support.v4.app.FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, activity.lisTracksFragment);
        ft.commit();
    }

    public void notifyHistoryListAdapter()
    {
        adapter.notifyDataSetChanged();
    }
}
