
package com.tevoi.tevoi;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.tevoi.tevoi.Utils.FileHelper;
import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.adapter.TracksSerializableAdapter;
import com.tevoi.tevoi.model.RecyclerViewEmptySupport;
import com.tevoi.tevoi.model.TrackSerializableObject;

import java.util.ArrayList;

public class PlayingNowFragment extends Fragment
{
    TracksSerializableAdapter adapter ;
    RecyclerViewEmptySupport recyclerView;
    SideMenu activity;

    ImageButton btnClearPlayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_playing_now_list, container, false);
        activity = (SideMenu) getActivity();
        btnClearPlayList = rootView.findViewById(R.id.btn_clear_play_now_list);
        View emptyView = rootView.findViewById(R.id.txt_play_now_empty);

        btnClearPlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String path = activity.getFilesDir().getAbsolutePath() + Global.PlayNowListDirectory;
                FileHelper.deleteDirectory(path);
                // delete shared preference list
                activity.storageManager.deletePlayNowList(activity);
                activity.playNowListTracks = activity.storageManager.loadPlayNowTracks(activity);
                notifyPlayNextListAdapter();
                Toast.makeText(activity, getResources().getString(R.string.msg_play_now_deleted_successfully), Toast.LENGTH_SHORT).show();
            }
        });
        // get list of play now tracks
        ArrayList<TrackSerializableObject> lstTracks = new ArrayList<>();

        activity.playNowListTracks =  activity.storageManager.loadPlayNowTracks(activity);
        lstTracks = activity.playNowListTracks;

        recyclerView = rootView.findViewById(R.id.tracks_play_now_recycler_View);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setEmptyView(emptyView);

        emptyView.setVisibility(View.INVISIBLE);

        adapter = new TracksSerializableAdapter(lstTracks, activity);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return rootView;
    }

    public void changeTabToNewPlayNow(View view)
    {
        SideMenu activity = (SideMenu) getActivity();
        activity.lisTracksFragment.defaultTab = 0;
        String mSubTitle = getResources().getString(R.string.title_list_tracks);;
        activity.updateSubTite(mSubTitle);

        androidx.fragment.app.FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, activity.lisTracksFragment);
        ft.addToBackStack( mSubTitle);
        ft.commit();

    }

    public void changeTabToTopRatedPlayNow(View view)
    {
        SideMenu activity = (SideMenu) getActivity();
        String mSubTitle = getResources().getString(R.string.title_list_tracks);;
        activity.updateSubTite(mSubTitle);

        activity.lisTracksFragment.defaultTab = 1;
        androidx.fragment.app.FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, activity.lisTracksFragment);
        ft.addToBackStack( mSubTitle );
        ft.commit();
    }

    public void changeToPopularPlayNow(View view) {
        SideMenu activity = (SideMenu) getActivity();
        String mSubTitle = getResources().getString(R.string.title_list_tracks);;
        activity.updateSubTite(mSubTitle);

        activity.lisTracksFragment.defaultTab = 2;
        androidx.fragment.app.FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, activity.lisTracksFragment);
        ft.addToBackStack( mSubTitle );
        ft.commit();
    }

    public void notifyPlayNextListAdapter()
    {
        adapter.notifyDataSetChanged();
    }

}
