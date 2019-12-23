package com.tevoi.tevoi;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class TrackShare extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_track_share, container, false);
    }
    public void shareClick(View v)
    {
        switch(v.getId())
        {
            // Just like you were doing
            case R.id.imgBtnShareFB :
            {


                break;
            }
            case R.id.imgBtnShareTW :
            {


                break;
            }

        }
    }
}
