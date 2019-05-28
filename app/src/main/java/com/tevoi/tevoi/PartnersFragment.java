
package com.tevoi.tevoi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.adapter.PartnerAdapter;
import com.tevoi.tevoi.model.PartnerListResponse;
import com.tevoi.tevoi.model.PartnerObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartnersFragment extends Fragment {
    ArrayList<PartnerObject> lstPartners = new ArrayList<>();
    PartnerAdapter adapter ;
    RecyclerView[] recyclerViews= new RecyclerView[4];
    int active_tab=0;
    Button[] tabs =  new Button[4];
    public int defaultTab;
    View rootView;
    SideMenu activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_partners, container, false);
        activity = (SideMenu) getActivity();

        tabs[0] = rootView.findViewById(R.id.btnAlphabetOrder);
        tabs[1]= rootView.findViewById(R.id.btnNewListPartners);
        tabs[2] = rootView.findViewById(R.id.btnTopRatedListPartners);
        tabs[3] = rootView.findViewById(R.id.btnPopularListPartners);

        lstPartners = new ArrayList<>();

        if(defaultTab < 0 || defaultTab > 3)
        {
            defaultTab =0;
        }
        recyclerViews[defaultTab] = rootView.findViewById(R.id.partners_recycler_View);
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
        activity.mProgressDialog.setMessage("Loading"); activity.mProgressDialog.show();

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
                SideMenu activity = (SideMenu)getActivity();
                adapter = new PartnerAdapter(partners.getPartners(),activity);
                recyclerViews[kk].setAdapter(adapter);
                activity.mProgressDialog.dismiss();
                Toast.makeText(getContext(),"partners:"+x, Toast.LENGTH_SHORT);
            }
            public void onFailure(Call<PartnerListResponse> call, Throwable t)
            {
                activity.mProgressDialog.dismiss();
                Toast.makeText(getContext(),"something went wrong", Toast.LENGTH_SHORT);
            }
        });

    }

}
