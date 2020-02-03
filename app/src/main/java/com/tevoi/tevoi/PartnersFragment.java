
package com.tevoi.tevoi;

import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.Utils.HelperFunctions;
import com.tevoi.tevoi.adapter.PartnerAdapter;
import com.tevoi.tevoi.model.PaginationScrollListener;
import com.tevoi.tevoi.model.PartnerListResponse;
import com.tevoi.tevoi.model.PartnerObject;
import com.tevoi.tevoi.model.RecyclerViewEmptySupport;
import com.tevoi.tevoi.model.TrackFilter;
import com.tevoi.tevoi.model.TrackObject;
import com.tevoi.tevoi.model.TrackResponseList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartnersFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener

{
    // region pagination properties
    LinearLayoutManager linearLayoutManager;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;


    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 0;
    private int currentPage = 0;
    int PAGE_SIZE = Global.PAGE_SIZE;;
    // endregion

    List<PartnerObject> lstPartners = new ArrayList<PartnerObject>();

    PartnerAdapter adapter ;
    SideMenu activity;
    View rootView;

//    abd edit
//    ArrayList<PartnerObject> lstPartners = new ArrayList<>();



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

        lstPartners = activity.storageManager.loadListPartners(activity);
        TOTAL_PAGES = lstPartners.size()/ PAGE_SIZE;


        swipeRefreshLayout = rootView.findViewById(R.id.main_swiperefresh_partners);
        swipeRefreshLayout.setOnRefreshListener(this);

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
        Collections.sort(lstPartners, new Comparator<PartnerObject>() {
        @Override
        public int compare(PartnerObject o1, PartnerObject o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    });
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
//        activity.mProgressDialog.setMessage(getResources().getString( R.string.loader_msg)); activity.mProgressDialog.show();

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

    @Override
    public void onRefresh() {
        progressBar.setVisibility(View.VISIBLE);

        getRefreshListPartners();

        // TODO: Check if data is stale.
        //  Execute network request if cache is expired; otherwise do not update data.
        adapter.clear();
        adapter.notifyDataSetChanged();
        //TODO loadfirstpage
//        loadFirstPage(k);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void loadFirstPage(final int tabId)
    {
        progressBar.setVisibility(View.GONE);
        List<PartnerObject> lstFirstPage = HelperFunctions.getPagePartners(lstPartners, 0 , PAGE_SIZE );
        adapter.addAll(lstFirstPage);

        if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
        else isLastPage = true;

        /*currentPage = 1;
        isLastPage = false;
        isLoading = false;
        if(lstPartners.size() <= PAGE_SIZE)
        {
            TOTAL_PAGES = 1;
        }
        else
        {
            TOTAL_PAGES = lstPartners.size() / PAGE_SIZE;
        }

        if(lstPartners.size() == 0)
                {
                    View v = rootView.findViewById(R.id.partners_list_empty);
                    recyclerViews[tabId].setEmptyView(v);
                }
                progressBar.setVisibility(View.GONE);

                List<PartnerObject> lstFirstPage = HelperFunctions.getPagePartners(lstPartners, 0 , PAGE_SIZE );
                adapter.addAll(lstFirstPage);

                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
                */

    }

    private void loadNextPage(int tabId)
    {

        adapter.removeLoadingFooter();
        isLoading = false;

        List<PartnerObject> lstNextPage = HelperFunctions.getPagePartners(lstPartners, currentPage , PAGE_SIZE );
        adapter.addAll(lstNextPage);
        //adapter.addAll(lstTracks);

        if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
        else isLastPage = true;

      /*  adapter.removeLoadingFooter();
        isLoading = false;
        List<PartnerObject> lstNextPage = new ArrayList<>();

        // this means that all data in list is already exists
        // because the the array list size is less than one page size
        if(lstPartners.size() < PAGE_SIZE)
        {

        }
        else
        {
            lstNextPage = HelperFunctions.getPagePartners(lstPartners, currentPage , PAGE_SIZE );
        }
        adapter.addAll(lstNextPage);

        if ( currentPage !=  TOTAL_PAGES) adapter.addLoadingFooter();
        else isLastPage = true;*/


    }

    private void getRefreshListPartners()
    {
    /*    EditText txtFilter = rootView.findViewById(R.id.txt_search_filter_value);
        CheckBox chkIsLocationEnabled = rootView.findViewById(R.id.checkBoxLocationEnable);

        *//*activity.mProgressDialog.setMessage(getResources().getString( R.string.loader_msg));
        activity.mProgressDialog.show();
        *//*
        TrackFilter filter =  new TrackFilter();
        LinearLayout layout = activity.findViewById(R.id.test_linear);
        if (layout != null)
        {
            if (layout.getVisibility() == View.GONE)
            {
                filter.SearchKey = "";
                filter.IsLocationEnabled = false;
            }
            else {
                filter.SearchKey = txtFilter.getText().toString();
                filter.IsLocationEnabled = chkIsLocationEnabled.isChecked();
            }
        }
        else {
            filter.SearchKey = "";
            filter.IsLocationEnabled = false;
        }
        filter.TrackTypeId =1; filter.ListTypeEnum = active_tab;
        filter.Index = 0; filter.Size = 0;
        Log.d("ResultTracks Next ", filter.getStringFilter());*/

        //Call<TrackResponseList> call = ((CustomApp) activity.getApplication()).getApiService().getListMainTrack(filter);
        Call<PartnerListResponse> call = Global.client.GetPartnersList(active_tab,0,0 );
        call.enqueue(new Callback<PartnerListResponse>() {
            public void onResponse(Call<PartnerListResponse> call, Response<PartnerListResponse> response)
            {
                // replace old list tracks with new one from server
               PartnerListResponse partners = response.body();

                adapter.clear();
                adapter.notifyDataSetChanged();
                lstPartners = partners.getPartners();
                activity.storageManager.storeListPartners(activity, lstPartners);
                // TODO order by active tab
                loadFirstPage(active_tab);
            }
            public void onFailure(Call<PartnerListResponse> call, Throwable t)
            {
                //activity.mProgressDialog.dismiss();
            }
        });
    }


}
