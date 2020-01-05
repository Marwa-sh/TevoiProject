package com.tevoi.tevoi;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.adapter.Track;
import com.tevoi.tevoi.adapter.TracksAdapter;
import com.tevoi.tevoi.model.GetUserListTracksResponse;
import com.tevoi.tevoi.model.IResponse;
import com.tevoi.tevoi.model.RecyclerViewEmptySupport;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListTracksFragment extends Fragment
{
    ArrayList<Track> mTracks = new ArrayList<>();
    TracksAdapter adapter ;
    //RecyclerView[] recyclerViews= new RecyclerView[3];
    RecyclerViewEmptySupport[] recyclerViews= new RecyclerViewEmptySupport[3];
    int active_tab=0;
    Button[] tabs =  new Button[3];
    public int defaultTab;
    public  int currenUsertListId;
    public  String ListName ="";
    SideMenu activity;
    ImageButton btnClearUserListTrack;
    View rootView;

    public static UserListTracksFragment newInstance(int defaultTab, int listId, String listName) {
        UserListTracksFragment f = new UserListTracksFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("DefaultTab", defaultTab);
        args.putInt("CurrenUsertListId", listId);
        args.putString("ListName", listName);
        f.setArguments(args);

        return f;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            defaultTab = getArguments().getInt("DefaultTab");
            currenUsertListId = getArguments().getInt("CurrenUsertListId");
            ListName = getArguments().getString("ListName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_user_list_track, container, false);
        activity  = (SideMenu)getActivity();
        btnClearUserListTrack = rootView.findViewById(R.id.btn_clear_user_list_track);

        btnClearUserListTrack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                activity.mProgressDialog.setMessage(getResources().getString( R.string.loader_msg)); activity.mProgressDialog.show();

                Call<IResponse> call = Global.client.ClearUserListTrack();
                call.enqueue(new Callback <IResponse>(){
                    public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                        //generateDataList(response.body());
                        SideMenu activity = (SideMenu) getActivity();
                        IResponse result = response.body();
                        Toast.makeText(activity, result.getMessage(), Toast.LENGTH_SHORT).show();
                        activity.mProgressDialog.dismiss();
                        // TODO doRefrech error ask marwa
//                        doRefrech();
                    }
                    public void onFailure(Call<IResponse> call, Throwable t)
                    {
                        activity.mProgressDialog.dismiss();
                        Toast.makeText(getContext(),"something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        tabs[0] = rootView.findViewById(R.id.btnNewUserListTracks);
        tabs[1] = rootView.findViewById(R.id.btnTopRatedUserListTracks);
        tabs[2] = rootView.findViewById(R.id.btnPopularUserListTracks);

        if(defaultTab < 0 || defaultTab > 2)
        {
            defaultTab =0;
        }
        //defaultTab = 0;

        activity.updateSubTite(ListName);

        recyclerViews[defaultTab] = rootView.findViewById(R.id.user_list_tracks_recycler_View);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViews[defaultTab].setLayoutManager(layoutManager);
        recyclerViews[active_tab].setEmptyView(rootView.findViewById(R.id.user_tracks_list_empty));

        activateTab(defaultTab);

        return  rootView;
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
        activity.mProgressDialog.setMessage(getResources().getString( R.string.loader_msg));
        activity.mProgressDialog.show();

        final int kk= k;

        for(int i=0;i<recyclerViews.length;i++)
        {
            recyclerViews[i]=null;
        }
        recyclerViews[k] = rootView.findViewById(R.id.user_list_tracks_recycler_View);
        for(int i=0;i<tabs.length;i++)
        {
            tabs[i].setBackgroundColor(ContextCompat.getColor(activity,R.color.tevoiBrownDark));
            tabs[i].setTextColor(ContextCompat.getColor(activity,R.color.white));
        }

        tabs[k].setBackgroundColor(ContextCompat.getColor(activity,R.color.white));
        tabs[k].setTextColor(ContextCompat.getColor(activity,R.color.tevoiSwitchBlackLight));
        //tabs[k].refreshDrawableState();
        Call<GetUserListTracksResponse> call = Global.client.GetTracksForUserList(currenUsertListId, 0, 10);
        call.enqueue(new Callback<GetUserListTracksResponse>(){
            public void onResponse(Call<GetUserListTracksResponse> call, Response<GetUserListTracksResponse> response) {
                //generateDataList(response.body());
                GetUserListTracksResponse tracks=response.body();
                int x=tracks.getLstTrack().size();
                recyclerViews[kk].setEmptyView(rootView.findViewById(R.id.user_tracks_list_empty));
                recyclerViews[kk].setAdapter(adapter);
                adapter = new TracksAdapter(tracks.getLstTrack(),activity, Global.UserListTracksFragment,recyclerViews[kk]);
                recyclerViews[kk].setAdapter(adapter);

                activity.mProgressDialog.dismiss();
                //Toast.makeText(activity,"tracks:"+x, Toast.LENGTH_SHORT);
            }
            public void onFailure(Call<GetUserListTracksResponse> call, Throwable t)
            {
                activity.mProgressDialog.dismiss();
                Toast.makeText(activity,"something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public  void notifyUserListTracksAdapter()
    {
        adapter.notifyDataSetChanged();
    }
}
