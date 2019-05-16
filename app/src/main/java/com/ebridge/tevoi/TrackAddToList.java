package com.ebridge.tevoi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ebridge.tevoi.model.TrackSerializableObject;

public class TrackAddToList extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_track_add_to_list, container, false);
    }

    public void addTrackToListClick(View v)
    {
        switch(v.getId())
        {
            // Just like you were doing
            case R.id.imgBtnAddTrackToList :
            {
                // here we need to add track to play next list
                SideMenu activity = (SideMenu) getActivity();
                int Id = activity.mediaPlayerFragment.currentTrack.getId();
                String Name = activity.mediaPlayerFragment.currentTrack.getName();
                TrackSerializableObject track = new TrackSerializableObject();
                track.setId(Id);
                track.setName(Name);
                track.setDuration("03:20");
                track.setAuthor("Authors");
                track.setCategories("Categories");
                track.setRate(2);
                String result = activity.storageManager.addTrack(getContext(), track);
                activity.playNowListTracks = activity.storageManager.loadPlayNowTracks(getContext());
                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.imgBtnCloseAddToList :
            {

                break;
            }
        }
    }
}
