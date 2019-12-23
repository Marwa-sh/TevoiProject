
package com.tevoi.tevoi;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.adapter.TracksAdapter;
import com.tevoi.tevoi.model.GetPartnerTracksResponse;
import com.tevoi.tevoi.model.RecyclerViewEmptySupport;
import com.tevoi.tevoi.model.TrackObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartnerNameFragment extends Fragment {

    int PartnerId;
    String PartnerName;
    String PartnerDesciption;
    String PartnerLogoPath;

    ImageView imgPartnerLogo;

    ArrayList<TrackObject> lstPartnerTracks = new ArrayList<>();
    TracksAdapter adapter ;
    RecyclerViewEmptySupport[] recyclerViews= new RecyclerViewEmptySupport[3];
    int active_tab = 0;
    Button[] tabs =  new Button[3];
    public int defaultTab;
    View rootView;
    SideMenu activity;
    TextView partnerName;
    TextView partnerDescription;

    public static PartnerNameFragment newInstance(int partnerId, String partnerName, String partnerDescrition, String logo) {
        PartnerNameFragment f = new PartnerNameFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("PartnerId", partnerId);
        args.putString("PartnerName", partnerName);
        args.putString("PartnerDescrition", partnerDescrition);
        args.putString("PartnerLogoPath", logo);
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
            PartnerLogoPath = getArguments().getString("PartnerLogoPath");

        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_partner_name, container, false);
        partnerName = rootView.findViewById(R.id.txt_parter_name);
        partnerDescription = rootView.findViewById(R.id.txt_partner_description);
        imgPartnerLogo = rootView.findViewById(R.id.img_partner_logo);

        activity = (SideMenu)getActivity();

        partnerName.setText(PartnerName);
        partnerDescription.setText(PartnerDesciption);

        if(PartnerLogoPath!= "")
        {
            Picasso.with(activity)  //Here, this is context.
                    .load(Global.IMAGE_BASE_URL + PartnerLogoPath)  //Url of the image to load.
                    .into(imgPartnerLogo);
        }

        /*tabs[0] = rootView.findViewById(R.id.btnNewListPartnerTracks);
        tabs[1]= rootView.findViewById(R.id.btnTopRatedListPartnerTracks);
        tabs[2] = rootView.findViewById(R.id.btnPopularListPartnerTracks);
*/
        if(defaultTab < 0 || defaultTab > 2)
        {
            defaultTab =0;
        }
        recyclerViews[defaultTab] = rootView.findViewById(R.id.partner_tracks_recycler_View);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViews[defaultTab].setLayoutManager(layoutManager);
        recyclerViews[active_tab].setEmptyView(rootView.findViewById(R.id.partner_tracks_list_empty));
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

        activity.mProgressDialog.setMessage(getResources().getString( R.string.loader_msg));
        activity.mProgressDialog.show();

        final int kk= k;

        for(int i=0;i<recyclerViews.length;i++)
        {
            recyclerViews[i]=null;
        }
        recyclerViews[k] = rootView.findViewById(R.id.partner_tracks_recycler_View);

        Call<GetPartnerTracksResponse> call = Global.client.GetPartnerTracks(PartnerId,defaultTab, 0, 10);
        call.enqueue(new Callback<GetPartnerTracksResponse>(){
            public void onResponse(Call<GetPartnerTracksResponse> call, Response<GetPartnerTracksResponse> response) {
                //generateDataList(response.body());
                GetPartnerTracksResponse tracks=response.body();
                int x=tracks.getPartnerTracks().size();
                //recyclerViews[kk].setAdapter(adapter);
                adapter = new TracksAdapter(tracks.getPartnerTracks(),activity, Global.PartnerNameFragment, recyclerViews[kk]);
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
