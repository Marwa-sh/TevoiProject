
package com.tevoi.tevoi;

import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.Utils.HelperFunctions;
import com.tevoi.tevoi.adapter.TracksAdapter;
import com.tevoi.tevoi.model.IResponse;
import com.tevoi.tevoi.model.PaginationScrollListener;
import com.tevoi.tevoi.model.RecyclerViewEmptySupport;
import com.tevoi.tevoi.model.TrackFilter;
import com.tevoi.tevoi.model.TrackObject;
import com.tevoi.tevoi.model.TrackResponseList;
import com.tevoi.tevoi.model.UserListObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener
{

    // region pagination properties
    LinearLayoutManager linearLayoutManager;
    ProgressBar progressBar;
    LinearLayout errorLayout;
    TextView txtError;
    Button btnRetry;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 0;
    private int currentPage = 0;
    int PAGE_SIZE = Global.PAGE_SIZE;
    // endregion
    List<TrackObject> lstFavouriteTracks = new ArrayList<TrackObject>();
    List<TrackObject> lstTracks = new ArrayList<TrackObject>();

    TracksAdapter adapter ;
    RecyclerViewEmptySupport recyclerView;
    SideMenu activity;
    View rootView;
    ImageButton btnClearFavourite;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_favourite_list, container, false);
        activity = (SideMenu) getActivity();
        View emptyView = rootView.findViewById(R.id.favourite_list_empty);

        recyclerView = rootView.findViewById(R.id.favourite_tracks_recycler_View);
        initiatePagination();

        swipeRefreshLayout = rootView.findViewById(R.id.main_swiperefresh_favorite_tracks);
        swipeRefreshLayout.setOnRefreshListener(this);

        lstFavouriteTracks = activity.storageManager.loadFavoriteListTracks(activity);
        TOTAL_PAGES = lstFavouriteTracks.size()/ PAGE_SIZE;

        btnClearFavourite =rootView.findViewById(R.id.btn_clear_favourite);

        //abd
        btnClearFavourite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int size = lstFavouriteTracks.size();
               //activity.mProgressDialog.setMessage(getResources().getString( R.string.loader_msg)); activity.mProgressDialog.show();

                //adapter.notifyDataSetChanged();
                adapter.getTracksList().clear();
                View vw = rootView.findViewById(R.id.favourite_list_empty);
                vw.setVisibility(View.VISIBLE);
                recyclerView.setEmptyView(vw);

                adapter.notifyDataSetChanged();
                activity.storageManager.clearFavoriteListTracks(activity);
/*                adapter.notifyDataSetChanged();
                adapter.notifyItemRangeRemoved(0, size);*/

               /* Call<IResponse> call = Global.client.RemoveAllFavourite();
                call.enqueue(new Callback <IResponse>(){
                    public void onResponse(Call<IResponse> call, Response<IResponse> response) {
//                        generateDataList(response.body());
                        SideMenu activity = (SideMenu) getActivity();
                        IResponse result = response.body();
                        Toast.makeText(activity, activity.getResources().getString(R.string.cleared_successfully), Toast.LENGTH_SHORT).show();
                        activity.mProgressDialog.dismiss();
                        // TODO ask marwa
*//*                        adapter.getTracksList().clear();
                        adapter.clear();
                        adapter.notifyDataSetChanged();
                        adapter.notifyItemRangeRemoved(0, size);*//*
                        //activity.storageManager.clearFavoriteListTracks(activity);

//                        doRefresh();
                    }
                    public void onFailure(Call<IResponse> call, Throwable t)
                    {
                        activity.mProgressDialog.dismiss();
                        Toast.makeText(getContext(),"something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });*/


            }
        });

        //abd

        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setEmptyView(emptyView);

        List<TrackObject> trs = new ArrayList<>();
        adapter = new TracksAdapter(trs, activity, Global.FavouriteFragmentName, recyclerView);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);
        emptyView.setVisibility(View.INVISIBLE);

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager)
        {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                loadNextPage();
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
        loadFirstPage();
        /*// mocking network delay for API call
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadFirstPage();
            }
        }, 1000);
*/

        /*activity.mProgressDialog.setMessage("Loading"); activity.mProgressDialog.show();

        Call<TrackResponseList> call = Global.client.getFavouriteList(0, 10);
        call.enqueue(new Callback <TrackResponseList>(){
            public void onResponse(Call<TrackResponseList> call, Response<TrackResponseList> response) {
                //generateDataList(response.body());
                SideMenu activity = (SideMenu) getActivity();
                TrackResponseList tracks=response.body();
                int x=tracks.getTrack().size();
                adapter = new TracksAdapter(tracks.getTrack(), activity, Global.FavouriteFragmentName);
                //recyclerView.setAdapter(adapter);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                activity.mProgressDialog.dismiss();
                Toast.makeText(getContext(),"tracks:"+x, Toast.LENGTH_SHORT);
            }
            public void onFailure(Call<TrackResponseList> call, Throwable t)
            {
                activity.mProgressDialog.dismiss();
                Toast.makeText(getContext(),"something went wrong", Toast.LENGTH_SHORT);
            }
        });
*/
        return  rootView;
    }

    public void changeTabToNewFavourite(View view)
    {
        SideMenu activity = (SideMenu) getActivity();
        activity.lisTracksFragment.defaultTab = 0;
        activity.updateSubTite("List Tracks");
        androidx.fragment.app.FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, activity.lisTracksFragment);
        ft.addToBackStack( "List Tracks" );
        ft.commit();

    }

    public void changeTabToTopRatedFavourite(View view)
    {
        SideMenu activity = (SideMenu) getActivity();
        //  update actionbar
        //String[] menuItems = getResources().getStringArray(R.array.rivers);
        // Updating the action bar title
        //getActivity().getActionBar().setTitle(menuItems[0]);
        activity.updateSubTite("List Tracks");

        activity.lisTracksFragment.defaultTab = 1;
        androidx.fragment.app.FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, activity.lisTracksFragment);
        ft.addToBackStack( "List Tracks" );
        ft.commit();
    }

    public void changeToPopularFavourite(View view)
    {
        SideMenu activity = (SideMenu) getActivity();
        activity.updateSubTite("List Tracks");

        activity.lisTracksFragment.defaultTab = 2;
        androidx.fragment.app.FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, activity.lisTracksFragment);
        ft.addToBackStack( "List Tracks" );
        ft.commit();
    }

    public void notifyFavouriteListAdapter()
    {
        adapter.notifyDataSetChanged();
    }

    public  void initiatePagination()
    {
        errorLayout = (LinearLayout) rootView.findViewById(R.id.error_layout);
        txtError = (TextView) rootView.findViewById(R.id.error_txt_cause);
        btnRetry = (Button) rootView.findViewById(R.id.error_btn_retry);

        isLastPage = false;
        progressBar = (ProgressBar) rootView.findViewById(R.id.main_progress_list_tracks);
        currentPage = 0;

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFirstPage();
            }
        });
    }

    private void loadFirstPage()
    {

        progressBar.setVisibility(View.GONE);
        List<TrackObject> lstFirstPage =  HelperFunctions.getPage(lstFavouriteTracks, 0 , PAGE_SIZE );
        adapter.addAll(lstFirstPage);

        if(lstFirstPage.size() == 0) {
            View v = rootView.findViewById(R.id.favourite_list_empty);
            v.setVisibility(View.VISIBLE);
            recyclerView.setEmptyView(v);
        }
        //adapter.addAll(lstTracks);

        /*if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
        else isLastPage = true;*/

       /* currentPage = 0;
        //activity.mProgressDialog.setMessage("Loading1");
        //activity.mProgressDialog.show();

        Call<TrackResponseList> call = Global.client.getFavouriteList(currentPage, PAGE_SIZE);
        call.enqueue(new Callback <TrackResponseList>(){
            public void onResponse(Call<TrackResponseList> call, Response<TrackResponseList> response) {
                //generateDataList(response.body());
                SideMenu activity = (SideMenu) getActivity();
                TrackResponseList tracks=response.body();

                TOTAL_PAGES = tracks.getTotalRowCount() / PAGE_SIZE;

                if(tracks.getLstTrack().size() == 0) {
                    View v = rootView.findViewById(R.id.favourite_list_empty);
                    recyclerView.setEmptyView(v);
                }
                progressBar.setVisibility(View.GONE);
                adapter.addAll(tracks.getLstTrack());

                if (currentPage != 0 && currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;

                // add these tracks to the  list in Side Menu
                activity.lstTracks = new ArrayList<>();
                activity.lstTracks.addAll(tracks.getLstTrack());

                //activity.mProgressDialog.dismiss();
                adapter.notifyDataSetChanged();
            }
            public void onFailure(Call<TrackResponseList> call, Throwable t)
            {
                //activity.mProgressDialog.dismiss();
                //t.printStackTrace();
                //showErrorView(t);
                //Toast.makeText(getContext(),"something went wrong", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    private void loadNextPage()
    {
        //activity.mProgressDialog.setMessage("Loading");
        //activity.mProgressDialog.show();


        adapter.removeLoadingFooter();
        isLoading = false;

        List<TrackObject> lstNextPage = HelperFunctions.getPage(lstFavouriteTracks, currentPage , PAGE_SIZE );
        adapter.addAll(lstNextPage);
      /*  adapter.addAll(lstTracks);*/
        /*if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
        else isLastPage = true;*/

/*

        Call<TrackResponseList> call = Global.client.getFavouriteList(currentPage, PAGE_SIZE);
        call.enqueue(new Callback <TrackResponseList>(){
            public void onResponse(Call<TrackResponseList> call, Response<TrackResponseList> response) {
                //generateDataList(response.body());
                SideMenu activity = (SideMenu) getActivity();
                TrackResponseList tracks=response.body();

                TOTAL_PAGES = tracks.getTotalRowCount() / PAGE_SIZE;
                adapter.removeLoadingFooter();
                isLoading = false;

                adapter.addAll(tracks.getLstTrack());
               */
/* if(tracks.getTrack().size() == 0)
                {
                    currentPage --;
                }*//*

                if (TOTAL_PAGES != 0 && currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;

                // add these tracks to the  list in Side Menu
                activity.lstTracks.addAll(tracks.getLstTrack());

                //activity.mProgressDialog.dismiss();
                adapter.notifyDataSetChanged();
            }
            public void onFailure(Call<TrackResponseList> call, Throwable t)
            {
                //activity.mProgressDialog.dismiss(); currentPage --;
                t.printStackTrace();
                showErrorView(t);
                Toast.makeText(getContext(),"something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
*/

    }

    private void showErrorView(Throwable throwable) {
        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            txtError.setText(fetchErrorMessage(throwable));
        }
    }

    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getResources().getString(R.string.error_msg_unknown);

        if (!HelperFunctions.isNetworkConnected(activity)) {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }

    public void doRefresh() {
        progressBar.setVisibility(View.VISIBLE);
        //  Execute network request if cache is expired; otherwise do not update data.
        adapter.getTracksList().clear();
        adapter.notifyDataSetChanged();
        loadFirstPage();
    }
    @Override
    public void onRefresh() {
        progressBar.setVisibility(View.VISIBLE);

        getRefreshListHistoryTrack();

        // TODO: Check if data is stale.
        //  Execute network request if cache is expired; otherwise do not update data.
        //adapter.clear();
        //adapter.notifyDataSetChanged();
        //TODO loadfirstpage
//        loadFirstPage(k);
        swipeRefreshLayout.setRefreshing(false);
    }
    private void getRefreshListHistoryTrack()
    {
        EditText txtFilter = rootView.findViewById(R.id.txt_search_filter_value);
        CheckBox chkIsLocationEnabled = rootView.findViewById(R.id.checkBoxLocationEnable);


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
        filter.TrackTypeId =1;
        filter.Index = 0; filter.Size = 0;
        Log.d("ResultTracks Next ", filter.getStringFilter());

        //Call<TrackResponseList> call = ((CustomApp) activity.getApplication()).getApiService().getListMainTrack(filter);
        Call<TrackResponseList> call = Global.client.getListMainTrack(filter);
        call.enqueue(new Callback<TrackResponseList>() {
            public void onResponse(Call<TrackResponseList> call, Response<TrackResponseList> response)
            {
                // replace old list tracks with new one from server
                TrackResponseList tracks = response.body();

                adapter.clear();
                adapter.notifyDataSetChanged();
                lstTracks = tracks.getLstTrack();
                activity.storageManager.storeListTracks(activity, lstTracks);
                lstFavouriteTracks = activity.storageManager.loadFavoriteListTracks(activity);
                TOTAL_PAGES = lstFavouriteTracks.size()/ PAGE_SIZE;
                // TODO order by active tab
                loadFirstPage();
            }
            public void onFailure(Call<TrackResponseList> call, Throwable t)
            {
                //activity.mProgressDialog.dismiss();
            }
        });
    }
}