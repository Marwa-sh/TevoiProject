
package com.tevoi.tevoi;

import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.adapter.PartnerAdapter;
import com.tevoi.tevoi.model.PaginationScrollListener;
import com.tevoi.tevoi.model.PartnerListResponse;
import com.tevoi.tevoi.model.PartnerObject;
import com.tevoi.tevoi.model.RecyclerViewEmptySupport;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartnersFragment extends Fragment
{
    // region pagination properties
    LinearLayoutManager linearLayoutManager;
    ProgressBar progressBar;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 0;
    private int currentPage = 0;
    int PAGE_SIZE = Global.PAGE_SIZE;;
    // endregion

    PartnerAdapter adapter ;
    SideMenu activity;
    View rootView;
    ArrayList<PartnerObject> lstPartners = new ArrayList<>();
    RecyclerViewEmptySupport[] recyclerViews= new RecyclerViewEmptySupport[4];

    int active_tab=0;
    Button[] tabs =  new Button[2];
    public int defaultTab;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_partners, container, false);
        activity = (SideMenu) getActivity();

        tabs[0] = rootView.findViewById(R.id.btnAlphabetOrder);
        tabs[1]= rootView.findViewById(R.id.btnNewListPartners);

        if(defaultTab < 0 || defaultTab > 1)
        {
            defaultTab =0;
        }
        /*recyclerViews[defaultTab] = rootView.findViewById(R.id.partners_recycler_View);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViews[defaultTab].setLayoutManager(layoutManager);*/
        initiatePagination();

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

    /*public void changeTabToTopRatedPartners(View view) {
        activateTab(2);
    }

    public void changeToPopularPartners(View view) {
        activateTab(3);
    }*/

    public void activateTab(final int k)
    {
        activity.mProgressDialog.setMessage(getResources().getString( R.string.loader_msg)); activity.mProgressDialog.show();

        for(int i=0;i<recyclerViews.length;i++)
        {
            recyclerViews[i]=null;
        }
        recyclerViews[k] = rootView.findViewById(R.id.partners_recycler_View);
        for(int i=0;i<tabs.length;i++)
        {
            tabs[i].setBackgroundColor(ContextCompat.getColor(activity,R.color.tevoiBrownDark));
            tabs[i].setTextColor(ContextCompat.getColor(activity,R.color.white));
        }

        tabs[k].setBackgroundColor(ContextCompat.getColor(activity,R.color.white));
        tabs[k].setTextColor(ContextCompat.getColor(activity,R.color.tevoiSwitchBlackLight));
        View emptyView = rootView.findViewById(R.id.partners_list_empty);

        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViews[k].setLayoutManager(linearLayoutManager);
        recyclerViews[k].setEmptyView(emptyView);

        List<PartnerObject> partners = new ArrayList<>();
        adapter = new PartnerAdapter(partners, activity);

        recyclerViews[k].setItemAnimator(new DefaultItemAnimator());

        recyclerViews[k].setAdapter(adapter);
        emptyView.setVisibility(View.INVISIBLE);

        recyclerViews[k].addOnScrollListener(new PaginationScrollListener(linearLayoutManager)
        {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                loadNextPage(k);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        // mocking network delay for API call
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadFirstPage(k);
            }
        }, 1000);

    }

    public  void initiatePagination()
    {
        isLastPage = false;
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_list_partners);
        currentPage = 0;
    }

    private void loadFirstPage(final int tabId)
    {
        currentPage = 0;
        isLastPage = false;
        isLoading = false;
        activity.mProgressDialog.setMessage(getResources().getString( R.string.loader_msg));
        activity.mProgressDialog.show();

        Call<PartnerListResponse> call = Global.client.GetPartnersList(tabId, currentPage, PAGE_SIZE);
        call.enqueue(new Callback<PartnerListResponse>(){
            public void onResponse(Call<PartnerListResponse> call, Response<PartnerListResponse> response) {

                PartnerListResponse partners=response.body();
                TOTAL_PAGES = partners.getTotalRowCount() / PAGE_SIZE;

                if(partners.getPartners().size() == 0)
                {
                    View v = rootView.findViewById(R.id.tracks_list_empty);
                    recyclerViews[tabId].setEmptyView(v);
                }
                progressBar.setVisibility(View.GONE);
                adapter.addAll(partners.getPartners());

                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;

                activity.mProgressDialog.dismiss();
                adapter.notifyDataSetChanged();
                /*int x=partners.getPartners().size();
                //recyclerViews[kk].setAdapter(adapter);
                SideMenu activity = (SideMenu)getActivity();
                adapter = new PartnerAdapter(partners.getPartners(),activity);
                recyclerViews[kk].setEmptyView(rootView.findViewById(R.id.partners_list_empty));

                recyclerViews[kk].setAdapter(adapter);
                activity.mProgressDialog.dismiss();
                Toast.makeText(getContext(),"partners:"+x, Toast.LENGTH_SHORT);*/
            }
            public void onFailure(Call<PartnerListResponse> call, Throwable t)
            {
                Log.d("ResultTracks", "Faaaail=");
                activity.mProgressDialog.dismiss();
                t.printStackTrace();
            }
        });

    }

    private void loadNextPage(int tabId)
    {
        activity.mProgressDialog.setMessage(getResources().getString( R.string.loader_msg));
        activity.mProgressDialog.show();

        Call<PartnerListResponse> call = Global.client.GetPartnersList(tabId, currentPage, PAGE_SIZE);
        call.enqueue(new Callback<PartnerListResponse>(){
            public void onResponse(Call<PartnerListResponse> call, Response<PartnerListResponse> response) {

                PartnerListResponse partners=response.body();
                TOTAL_PAGES = partners.getTotalRowCount() / PAGE_SIZE;
                adapter.removeLoadingFooter();
                isLoading = false;
               /* if(tracks.getTrack().size() == 0 && currentPage != 0)
                    currentPage --;*/
                adapter.addAll(partners.getPartners());

                if (TOTAL_PAGES != 0 && currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;

                activity.mProgressDialog.dismiss();

            }
            public void onFailure(Call<PartnerListResponse> call, Throwable t)
            {
                activity.mProgressDialog.dismiss();currentPage --;
                t.printStackTrace();
            }
        });

    }




}
