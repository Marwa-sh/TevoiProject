
package com.ebridge.tevoi;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ebridge.tevoi.Utils.CommentFragment;
import com.ebridge.tevoi.Utils.Global;
import com.ebridge.tevoi.adapter.PartnerAdapter;
import com.ebridge.tevoi.adapter.TracksAdapter;
import com.ebridge.tevoi.model.GetPartnerTracksResponse;
import com.ebridge.tevoi.model.PartnerListResponse;
import com.ebridge.tevoi.model.PartnerObject;
import com.ebridge.tevoi.model.TrackObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartnerNameFragment extends Fragment {

    int PartnerId;
    String PartnerName;
    String PartnerDesciption;


    ArrayList<TrackObject> lstPartnerTracks = new ArrayList<>();
    TracksAdapter adapter ;
    RecyclerView[] recyclerViews= new RecyclerView[3];
    int active_tab = 0;
    Button[] tabs =  new Button[3];
    public int defaultTab;
    View rootView;
    SideMenu activity;
    TextView partnerName;
    TextView partnerDescription;

    public static PartnerNameFragment newInstance(int partnerId, String partnerName, String partnerDescrition) {
        PartnerNameFragment f = new PartnerNameFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("PartnerId", partnerId);
        args.putString("PartnerName", partnerName);
        args.putString("PartnerDescrition", partnerDescrition);
        f.setArguments(args);

        return f;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            PartnerId = getArguments().getInt("PartnerId");
            PartnerName =getArguments().getString("PartnerName");
            PartnerDesciption = getArguments().getString("PartnerDescrition");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_partner_name, container, false);
        partnerName = rootView.findViewById(R.id.txt_parter_name);
        partnerDescription = rootView.findViewById(R.id.txt_partner_description);
        activity = (SideMenu)getActivity();

        partnerName.setText(PartnerName);
        partnerDescription.setText(PartnerDesciption);

        tabs[0] = rootView.findViewById(R.id.btnNewListPartnerTracks);
        tabs[1]= rootView.findViewById(R.id.btnTopRatedListPartnerTracks);
        tabs[2] = rootView.findViewById(R.id.btnPopularListPartnerTracks);

        if(defaultTab < 0 || defaultTab > 2)
        {
            defaultTab =0;
        }
        recyclerViews[defaultTab] = (RecyclerView) rootView.findViewById(R.id.partner_tracks_recycler_View);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViews[defaultTab].setLayoutManager(layoutManager);
        activateTab(defaultTab);

        return rootView;
    }

    public void changeTabToNewPartnerTracks(View view) {
        activateTab(0);
    }

    public void changeTabToTopRatedPartnerTracks(View view) {
        activateTab(1);
    }

    public void changeToPopularPartnerTracks(View view) {
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
        recyclerViews[k] = rootView.findViewById(R.id.partner_tracks_recycler_View);
        for(int i=0;i<tabs.length;i++)
        {
            tabs[i].setBackgroundColor(ContextCompat.getColor(getContext(),R.color.tevoiBlueSecondary));
            //tabs[i].refreshDrawableState();
        }
        tabs[k].setBackgroundColor(ContextCompat.getColor(getContext(),R.color.tevoiBluePrimary));
        //tabs[k].refreshDrawableState();
        Call<GetPartnerTracksResponse> call = Global.client.GetPartnerTracks(PartnerId, 0, 10);
        call.enqueue(new Callback<GetPartnerTracksResponse>(){
            public void onResponse(Call<GetPartnerTracksResponse> call, Response<GetPartnerTracksResponse> response) {
                //generateDataList(response.body());
                GetPartnerTracksResponse tracks=response.body();
                int x=tracks.getPartnerTracks().size();
                recyclerViews[kk].setAdapter(adapter);
                adapter = new TracksAdapter(tracks.getPartnerTracks(),activity, Global.PartnerNameFragment);
                recyclerViews[kk].setAdapter(adapter);
                activity.mProgressDialog.dismiss();
            }
            public void onFailure(Call<GetPartnerTracksResponse> call, Throwable t)
            {
                activity.mProgressDialog.dismiss();
                Toast.makeText(getContext(),"something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
