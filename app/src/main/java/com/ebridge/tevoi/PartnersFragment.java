
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
import android.widget.Spinner;
import android.widget.Toast;

import com.ebridge.tevoi.Utils.Global;
import com.ebridge.tevoi.adapter.PartnerAdapter;
import com.ebridge.tevoi.adapter.Track;
import com.ebridge.tevoi.adapter.TracksAdapter;
import com.ebridge.tevoi.model.PartnerListResponse;
import com.ebridge.tevoi.model.PartnerObject;
import com.ebridge.tevoi.model.TrackResponseList;
import com.ebridge.tevoi.rest.ApiClient;
import com.ebridge.tevoi.rest.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartnersFragment extends Fragment {
    ProgressDialog mProgressDialog;
    ArrayList<PartnerObject> lstPartners = new ArrayList<>();
    PartnerAdapter adapter ;
    RecyclerView[] recyclerViews= new RecyclerView[4];
    int active_tab=0;
    Button[] tabs =  new Button[4];
    public int defaultTab;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_partners, container, false);

        mProgressDialog = new ProgressDialog(getActivity());

        tabs[0] = rootView.findViewById(R.id.btnAlphabetOrder);
        tabs[1]= rootView.findViewById(R.id.btnNewListPartners);
        tabs[2] = rootView.findViewById(R.id.btnTopRatedListPartners);
        tabs[3] = rootView.findViewById(R.id.btnPopularListPartners);

        lstPartners = new ArrayList<>();

        if(defaultTab < 0 || defaultTab > 3)
        {
            defaultTab =0;
        }
        recyclerViews[defaultTab] = (RecyclerView) rootView.findViewById(R.id.partners_recycler_View);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViews[defaultTab].setLayoutManager(layoutManager);
        activateTab(defaultTab);
        return  rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActivity().setContentView(R.layout.activity_tracks_list);
        //defaultTab = 0;
        if(getArguments() != null)
            defaultTab = getArguments().getInt("DefaultTab");
    }

    public  void changeToAlphabetOrder(View view)
    {
        activateTab(0);
    }
    public void changeTabToNewListPartners(View view) {
        activateTab(1);
    }

    public void changeTabToTopRatedPartners(View view) {
        activateTab(2);
    }

    public void changeToPopularPartners(View view) {
        activateTab(3);
    }

    public void activateTab(int k)
    {
        mProgressDialog.setMessage("Loading");
        mProgressDialog.show();

        final int kk= k;

        for(int i=0;i<recyclerViews.length;i++)
        {
            recyclerViews[i]=null;
        }
        recyclerViews[k] = rootView.findViewById(R.id.partners_recycler_View);
        for(int i=0;i<tabs.length;i++)
        {
            tabs[i].setBackgroundColor(ContextCompat.getColor(getContext(),R.color.tevoiBlueSecondary));
            //tabs[i].refreshDrawableState();
        }

        tabs[k].setBackgroundColor(ContextCompat.getColor(getContext(),R.color.tevoiBluePrimary));
        //tabs[k].refreshDrawableState();
        Call<PartnerListResponse> call = Global.client.GetPartnersList(k, 0, 10);
        call.enqueue(new Callback<PartnerListResponse>(){
            public void onResponse(Call<PartnerListResponse> call, Response<PartnerListResponse> response) {
                //generateDataList(response.body());
                PartnerListResponse partners=response.body();
                int x=partners.getPartners().size();
                recyclerViews[kk].setAdapter(adapter);
                adapter = new PartnerAdapter(partners.getPartners(),getContext());
                recyclerViews[kk].setAdapter(adapter);
                mProgressDialog.dismiss();
                Toast.makeText(getContext(),"partners:"+x, Toast.LENGTH_SHORT);
            }
            public void onFailure(Call<PartnerListResponse> call, Throwable t)
            {
                mProgressDialog.dismiss();
                Toast.makeText(getContext(),"something went wrong", Toast.LENGTH_SHORT);
            }
        });

    }

}
