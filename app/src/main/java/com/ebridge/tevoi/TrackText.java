package com.ebridge.tevoi;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebridge.tevoi.Utils.CommentFragment;
import com.ebridge.tevoi.Utils.Global;
import com.ebridge.tevoi.model.TrackLocationResponse;
import com.ebridge.tevoi.model.TrackTextResponse;
import com.ebridge.tevoi.rest.ApiClient;
import com.ebridge.tevoi.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TrackText extends Fragment {

    int TrackId;
    String PreviousFragmentName;
    public static TrackText newInstance(int trackId, String previousFragmentName ) {
        TrackText f = new TrackText();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("TrackId", trackId);
        args.putString("PreviousFragmentName", previousFragmentName);
        f.setArguments(args);

        return f;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
        {
            TrackId = getArguments().getInt("TrackId");
            PreviousFragmentName = getArguments().getString("PreviousFragmentName"); }
        else
        {  TrackId = 0;  PreviousFragmentName = Global.MediaPlayerFragmentName; }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      final View rootView = inflater.inflate(R.layout.fragment_track_text, container, false);
        ImageView iv = (ImageView) rootView.findViewById(R.id.imgBtnCloseText);
        iv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Do something else
                SideMenu activity = (SideMenu) getActivity();
                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                // Replace the contents of the container with the new fragment
                //TrackAddToList frag = new TrackAddToList();
                if(PreviousFragmentName.equals(Global.ListTracksFragmentName))
                    ft.replace(R.id.content_frame, activity.lisTracksFragment);
                else if(PreviousFragmentName.equals(Global.UserListTracksFragment))
                    ft.replace(R.id.content_frame, activity.userListsFragment);
                else if(PreviousFragmentName.equals(Global.PlayNextListFragment))
                    ft.replace(R.id.content_frame, activity.playingNowFragment);
                else
                    ft.replace(R.id.content_frame, activity.mediaPlayerFragment);
                // or ft.add(R.id.your_placeholder, new FooFragment());
                // Complete the changes added above
                //activity.getSupportFragmentManager().popBackStackImmediate();
                ft.commit();
            }
        });

        SideMenu a = ((SideMenu) getActivity());
        // here we need to open maps app
        Call<TrackTextResponse> call = Global.client.GetTrackText(TrackId);
        call.enqueue(new Callback<TrackTextResponse>(){
            public void onResponse(Call<TrackTextResponse> call, Response<TrackTextResponse> response)
            {
                TrackTextResponse text = response.body();
                if(text.TrackText != null)
                {
                    //
                    TextView tv = (TextView) rootView.findViewById(R.id.text_track);
                    tv.setText(text.TrackText);
                }
            }
            public void onFailure(Call<TrackTextResponse> call, Throwable t)
            {
                TextView tv = (TextView) rootView.findViewById(R.id.text_track);
                tv.setText("No Text");
            }
        });

        return  rootView;

    }

}
