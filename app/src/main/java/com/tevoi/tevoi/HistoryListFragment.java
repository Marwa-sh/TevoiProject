package com.tevoi.tevoi;

import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.tevoi.tevoi.model.TrackObject;
import com.tevoi.tevoi.model.TrackResponseList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryListFragment extends Fragment
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
    int PAGE_SIZE = Global.PAGE_SIZE;;
    // endregion
    List<TrackObject> lstHistoryTracks = new ArrayList<TrackObject>();

    TracksAdapter adapter ;
    RecyclerViewEmptySupport recyclerView;
    //RecyclerView recyclerView;
    View rootView;
    SideMenu activity;

    ImageButton btnClearHistory;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_history_list, container, false);
        activity = (SideMenu) getActivity();

        btnClearHistory = rootView.findViewById(R.id.btn_clear_history);

        recyclerView = rootView.findViewById(R.id.history_tracks_recycler_View);
        initiatePagination();

        lstHistoryTracks = activity.storageManager.loadHistoryListTracks(activity);
        TOTAL_PAGES = lstHistoryTracks.size()/ PAGE_SIZE;

        btnClearHistory.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                adapter.getTracksList().clear();
                View vw = rootView.findViewById(R.id.history_list_empty);
                vw.setVisibility(View.VISIBLE);
                recyclerView.setEmptyView(vw);

                adapter.notifyDataSetChanged();
                activity.storageManager.clearHistoryListTracks(activity);
/*
                activity.mProgressDialog.setMessage(getResources().getString( R.string.loader_msg)); activity.mProgressDialog.show();

                Call<IResponse> call = Global.client.RemoveAllHistory();
                call.enqueue(new Callback <IResponse>(){
                    public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                        //generateDataList(response.body());
                        SideMenu activity = (SideMenu) getActivity();
                        IResponse result = response.body();
                        Toast.makeText(activity, result.getMessage(), Toast.LENGTH_SHORT).show();
                        activity.mProgressDialog.dismiss();
                        doRefresh();
                    }
                    public void onFailure(Call<IResponse> call, Throwable t)
                    {
                        activity.mProgressDialog.dismiss();
                        Toast.makeText(getContext(),"something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });*/
            }
        });
        View emptyView = rootView.findViewById(R.id.history_list_empty);

        //recyclerView = rootView.findViewById(R.id.history_tracks_recycler_View);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setEmptyView(emptyView);

        List<TrackObject> trs = new ArrayList<>();
        adapter = new TracksAdapter(trs, activity, Global.HistoryFragmentName, recyclerView);

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

        // mocking network delay for API call
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadFirstPage();
            }
        }, 1000);


        /*activity.mProgressDialog.setMessage("Loading"); activity.mProgressDialog.show();

        Call<TrackResponseList> call = Global.client.getHistoryList(0, 10);
        call.enqueue(new Callback <TrackResponseList>(){
            public void onResponse(Call<TrackResponseList> call, Response<TrackResponseList> response) {
                //generateDataList(response.body());
                SideMenu activity = (SideMenu) getActivity();
                TrackResponseList tracks=response.body();
                int x=tracks.getTrack().size();
                adapter = new TracksAdapter(tracks.getTrack(), activity, Global.HistoryFragmentName);
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
        });*/

        return  rootView;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActivity().setContentView(R.layout.activity_tracks_list);

    }

    public void changeTabToNewHistory(View view)
    {
        SideMenu activity = (SideMenu) getActivity();
        activity.lisTracksFragment.defaultTab = 0;
        String mSubTitle = getResources().getString(R.string.title_list_tracks);;
        activity.updateSubTite(mSubTitle);

        androidx.fragment.app.FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, activity.lisTracksFragment);
        ft.addToBackStack( mSubTitle);
        ft.commit();

    }

    public void changeTabToTopRatedHistory(View view)
    {
        SideMenu activity = (SideMenu) getActivity();
        String mSubTitle = getResources().getString(R.string.title_list_tracks);;
        activity.updateSubTite(mSubTitle);

        activity.lisTracksFragment.defaultTab = 1;
        androidx.fragment.app.FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, activity.lisTracksFragment);
        ft.addToBackStack( mSubTitle );
        ft.commit();
    }

    public void changeToPopularHistory(View view)
    {
        SideMenu activity = (SideMenu) getActivity();
        String mSubTitle = getResources().getString(R.string.title_list_tracks);;
        activity.updateSubTite(mSubTitle);

        activity.lisTracksFragment.defaultTab = 2;
        androidx.fragment.app.FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, activity.lisTracksFragment);
        ft.addToBackStack( mSubTitle );
        ft.commit();
    }

    public void notifyHistoryListAdapter()
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
    private List<TrackObject> getPage(List<TrackObject> lst , int pageIndex, int size)
    {
        int currentIndex = (pageIndex * PAGE_SIZE);
        List<TrackObject> l = new ArrayList<>();
        if(lst.size() == 0)
            return l;
        if( currentIndex + size > lst.size()) {
            if(currentIndex > lst.size()-1)
                return  l;
            else
                return lst.subList(currentIndex, lst.size());
        }
        else
            return lst.subList(currentIndex, currentIndex + size);
    }

    private void loadFirstPage()
    {

        currentPage = 0;
        progressBar.setVisibility(View.GONE);
        List<TrackObject> lstFirstPage = getPage(lstHistoryTracks, 0 , PAGE_SIZE );
        adapter.addAll(lstFirstPage);
        //activity.mProgressDialog.setMessage("Loading1");
        //activity.mProgressDialog.show();
      /*  currentPage = 0;
        Call<TrackResponseList> call = Global.client.getHistoryList(currentPage, PAGE_SIZE);
        call.enqueue(new Callback <TrackResponseList>(){
            public void onResponse(Call<TrackResponseList> call, Response<TrackResponseList> response) {
                //generateDataList(response.body());
                SideMenu activity = (SideMenu) getActivity();
                TrackResponseList tracks=response.body();

                TOTAL_PAGES = tracks.getTotalRowCount() / PAGE_SIZE;

                if(tracks.getLstTrack().size() == 0) {
                    View v = rootView.findViewById(R.id.history_list_empty);
                    v.setVisibility(View.INVISIBLE);
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
                t.printStackTrace();
                showErrorView(t);
                Toast.makeText(getContext(),"something went wrong", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    private void loadNextPage() {

        //activity.mProgressDialog.setMessage("Loading");
        //activity.mProgressDialog.show();

        progressBar.setVisibility(View.VISIBLE);

        List<TrackObject> lstNextPage = getPage(lstHistoryTracks, currentPage , PAGE_SIZE );
        adapter.addAll(lstNextPage);

        progressBar.setVisibility(View.GONE);

        /*Call<TrackResponseList> call = Global.client.getHistoryList(currentPage, PAGE_SIZE);
        call.enqueue(new Callback <TrackResponseList>(){
            public void onResponse(Call<TrackResponseList> call, Response<TrackResponseList> response) {
                //generateDataList(response.body());
                SideMenu activity = (SideMenu) getActivity();
                TrackResponseList tracks=response.body();

                TOTAL_PAGES = tracks.getTotalRowCount() / PAGE_SIZE;
                adapter.removeLoadingFooter();
                isLoading = false;

                adapter.addAll(tracks.getLstTrack());
                *//*if(tracks.getTrack().size() == 0)
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
        });*/

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

    private void doRefresh()
    {
        progressBar.setVisibility(View.VISIBLE);
        //  Execute network request if cache is expired; otherwise do not update data.
        adapter.getTracksList().clear();
        adapter.notifyDataSetChanged();
        loadFirstPage();
    }
}
