package com.ebridge.tevoi;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.ebridge.tevoi.adapter.CommentsAdapter;
import com.ebridge.tevoi.model.TrackCommentResponse;
import com.ebridge.tevoi.model.TrackLocationResponse;
import com.ebridge.tevoi.model.XmlFragementClickable;
import com.ebridge.tevoi.rest.ApiClient;
import com.ebridge.tevoi.rest.ApiInterface;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackLocation extends Fragment  {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_track_location, container, false);

    }

    public void locationClick(View v)
    {
        switch(v.getId())
        {
            // Just like you were doing
            case R.id.imgBtnOpenMapsApp :
            {
                SideMenu p = ((SideMenu) this.getActivity());
                int trackId = p.mediaPlayerFragment.currentTrack.getId();
                // here we need to open maps app
                ApiInterface client = ApiClient.getClient().create(ApiInterface.class);
                Call<TrackLocationResponse> call = client.GetTrackLocation(2);
                call.enqueue(new Callback<TrackLocationResponse>(){
                    public void onResponse(Call<TrackLocationResponse> call, Response<TrackLocationResponse> response)
                    {
                        TrackLocationResponse location = response.body();
                        if(location.Location != null)
                        {
                            // show google app map
                            // TODO :
                            //Uri number = Uri.parse("tel:5551234");
                            //Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                            // Build the intent
                            //Uri L = Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California");
                            Uri L = Uri.parse("geo:"+location.Location.Latitude+","+location.Location.Longitude+ "?z=14"); // z param is zoom level
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, L);
                           // startActivity(mapIntent);
                            // Verify it resolves
                            PackageManager packageManager = getActivity().getPackageManager();
                            List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
                            boolean isIntentSafe = activities.size() > 0;

                            // Start an activity if it's safe
                           if (isIntentSafe) {
                                startActivity(mapIntent);
                           }
                        }
                    }
                    public void onFailure(Call<TrackLocationResponse> call, Throwable t)
                    {

                    }
                });


                break;
            }
            case R.id.imgBtnCloseLocationFragment :
            {

                break;
            }
        }
    }

}
