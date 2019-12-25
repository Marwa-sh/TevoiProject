package com.tevoi.tevoi;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.model.TrackLocationResponse;

import java.util.List;

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
                Call<TrackLocationResponse> call = Global.client.GetTrackLocation(2);
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
