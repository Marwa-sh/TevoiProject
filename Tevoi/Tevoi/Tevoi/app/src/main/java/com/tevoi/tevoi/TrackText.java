package com.tevoi.tevoi;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.model.TrackTextResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TrackText extends Fragment {

    int TrackId;
    String PreviousFragmentName;
    ProgressBar progressBar;
    LinearLayout linearLayout;

    public static TrackText newInstance(int trackId, String previousFragmentName )
    {
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
        ImageView iv = rootView.findViewById(R.id.imgBtnCloseText);
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
                if(PreviousFragmentName.equals(Global.PartnerNameFragment))
                    ft.replace(R.id.content_frame, activity.partnersFragment);
                else if(PreviousFragmentName.equals(Global.ListTracksFragmentName))
                    ft.replace(R.id.content_frame, activity.lisTracksFragment);
                else if(PreviousFragmentName.equals(Global.UserListTracksFragment))
                    ft.replace(R.id.content_frame, activity.userListsFragment);
                else if(PreviousFragmentName.equals(Global.PlayNextListFragment))
                    ft.replace(R.id.content_frame, activity.playingNowFragment);
                else
                    ft.replace(R.id.content_frame, activity.mediaPlayerFragment);
                // or ft.add(R.id.your_placeholder, new FooFragment());
                // Complete the changes added above
                activity.BackBtnAction();
                ft.commit();
            }
        });

        progressBar = rootView.findViewById(R.id.loader_text);
        linearLayout = rootView.findViewById(R.id.linear_layout_text);


        SideMenu a = ((SideMenu) getActivity());
        // here we need to open maps app
        progressBar.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.INVISIBLE);

        Call<TrackTextResponse> call = Global.client.GetTrackText(TrackId);
        call.enqueue(new Callback<TrackTextResponse>(){
            public void onResponse(Call<TrackTextResponse> call, Response<TrackTextResponse> response)
            {
                TrackTextResponse text = response.body();
                if(text.TrackText != null)
                {
                    //
                    TextView tv = rootView.findViewById(R.id.text_track);
                    tv.setText(text.TrackText);
                }
                progressBar.setVisibility(View.INVISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
            }
            public void onFailure(Call<TrackTextResponse> call, Throwable t)
            {
                TextView tv = rootView.findViewById(R.id.text_track);
                tv.setText("No Text");
                progressBar.setVisibility(View.INVISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        });

        return  rootView;

    }

}
