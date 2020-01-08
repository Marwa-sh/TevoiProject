package com.tevoi.tevoi;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.adapter.Track;
import com.tevoi.tevoi.adapter.TracksAdapter;
import com.tevoi.tevoi.model.GetUserListTracksResponse;
import com.tevoi.tevoi.model.IResponse;
import com.tevoi.tevoi.model.PaginationScrollListener;
import com.tevoi.tevoi.model.RecyclerViewEmptySupport;
import com.tevoi.tevoi.model.TrackFilter;
import com.tevoi.tevoi.model.TrackObject;
import com.tevoi.tevoi.model.TrackResponseList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListTracksFragment extends Fragment
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

    TracksAdapter adapter ;
    //RecyclerView[] recyclerViews= new RecyclerView[3];
    RecyclerViewEmptySupport[] recyclerViews= new RecyclerViewEmptySupport[3];
    int active_tab=0;
    Button[] tabs =  new Button[3];
    public int defaultTab;
    public  int currenUsertListId;
    public  String ListName ="";
    SideMenu activity;
    ImageButton btnClearUserListTrack;
    View rootView;

    public static UserListTracksFragment newInstance(int defaultTab, int listId, String listName) {
        UserListTracksFragment f = new UserListTracksFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("DefaultTab", defaultTab);
        args.putInt("CurrenUsertListId", listId);
        args.putString("ListName", listName);
        f.setArguments(args);

        return f;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            defaultTab = getArguments().getInt("DefaultTab");
            currenUsertListId = getArguments().getInt("CurrenUsertListId");
            ListName = getArguments().getString("ListName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_user_list_track, container, false);
        activity  = (SideMenu)getActivity();
        btnClearUserListTrack = rootView.findViewById(R.id.btn_clear_user_list_track);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_user_list_tracks);

        btnClearUserListTrack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                activity.mProgressDialog.setMessage(getResources().getString( R.string.loader_msg)); activity.mProgressDialog.show();

                Call<IResponse> call = Global.client.ClearUserListTracks(currenUsertListId);
                call.enqueue(new Callback <IResponse>(){
                    public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                        //generateDataList(response.body());
                        SideMenu activity = (SideMenu) getActivity();
                        IResponse result = response.body();
                        Toast.makeText(activity, result.getMessage(), Toast.LENGTH_SHORT).show();
                        activity.mProgressDialog.dismiss();
                        // TODO doRefrech error ask marwa
//                        doRefrech();
                    }
                    public void onFailure(Call<IResponse> call, Throwable t)
                    {
                        activity.mProgressDialog.dismiss();
                        Toast.makeText(getContext(),"something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        tabs[0] = rootView.findViewById(R.id.btnNewUserListTracks);
        tabs[1] = rootView.findViewById(R.id.btnTopRatedUserListTracks);
        tabs[2] = rootView.findViewById(R.id.btnPopularUserListTracks);

        if(defaultTab < 0 || defaultTab > 2)
        {
            defaultTab =0;
        }
        //defaultTab = 0;

        activity.updateSubTite(ListName);

        /*recyclerViews[defaultTab] = rootView.findViewById(R.id.user_list_tracks_recycler_View);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViews[defaultTab].setLayoutManager(layoutManager);
        recyclerViews[active_tab].setEmptyView(rootView.findViewById(R.id.user_tracks_list_empty));
*/
        activateTab(defaultTab);

        return  rootView;
    }

    public void changeTabToNewUserListTracks(View view) {
        active_tab = 0;
        activateTab(0);
    }

    public void changeTabToTopRatedUserListTracks(View view) {
        active_tab = 1;activateTab(1);
    }

    public void changeToPopularUserListTracks(View view) {
        active_tab = 2;activateTab(2);
    }

    public void activateTab(final int k)
    {
        for(int i=0;i<recyclerViews.length;i++)
        {
            recyclerViews[i]=null;
        }
        recyclerViews[k] = rootView.findViewById(R.id.user_list_tracks_recycler_View);

        for(int i=0;i<tabs.length;i++)
        {
            tabs[i].setBackgroundColor(ContextCompat.getColor(activity,R.color.tevoiBrownDark));
            tabs[i].setTextColor(ContextCompat.getColor(activity,R.color.white));
        }
        tabs[k].setBackgroundColor(ContextCompat.getColor(activity,R.color.white));
        tabs[k].setTextColor(ContextCompat.getColor(activity,R.color.tevoiSwitchBlackLight));

        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViews[k].setLayoutManager(linearLayoutManager);

        List<TrackObject> trs = new ArrayList<>();
        adapter = new TracksAdapter(trs, activity, Global.UserListTracksFragment, recyclerViews[active_tab]);

        recyclerViews[k].setItemAnimator(new DefaultItemAnimator());

        recyclerViews[k].setAdapter(adapter);

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
        }, 100);

        //tabs[k].refreshDrawableState();
        /*Call<GetUserListTracksResponse> call = Global.client.GetTracksForUserList(currenUsertListId, 0, 10);
        call.enqueue(new Callback<GetUserListTracksResponse>(){
            public void onResponse(Call<GetUserListTracksResponse> call, Response<GetUserListTracksResponse> response) {
                //generateDataList(response.body());
                GetUserListTracksResponse tracks=response.body();
                int x=tracks.getLstTrack().size();
                recyclerViews[kk].setEmptyView(rootView.findViewById(R.id.user_tracks_list_empty));
                recyclerViews[kk].setAdapter(adapter);
                adapter = new TracksAdapter(tracks.getLstTrack(),activity, Global.UserListTracksFragment,recyclerViews[kk]);
                recyclerViews[kk].setAdapter(adapter);

                activity.mProgressDialog.dismiss();
                //Toast.makeText(activity,"tracks:"+x, Toast.LENGTH_SHORT);
            }
            public void onFailure(Call<GetUserListTracksResponse> call, Throwable t)
            {
                activity.mProgressDialog.dismiss();
                Toast.makeText(activity,"something went wrong", Toast.LENGTH_SHORT).show();
            }
        });*/

    }

    private void loadFirstPage(final int tabId)
    {
        currentPage = 0;
        isLastPage = false;
        isLoading = false;
        activity.mProgressDialog.setMessage(getResources().getString( R.string.loader_msg));
        activity.mProgressDialog.show();

        Call<GetUserListTracksResponse> call = Global.client.GetTracksForUserList(currenUsertListId, currentPage, PAGE_SIZE);
        call.enqueue(new Callback<GetUserListTracksResponse>(){
            public void onResponse(Call<GetUserListTracksResponse> call, Response<GetUserListTracksResponse> response) {

                GetUserListTracksResponse tracks=response.body();
                Log.d("ResultTracks First", "tracksNum=" +tracks.getTotalRows());

                TOTAL_PAGES = tracks.getTotalRows() / PAGE_SIZE;

                if(tracks.getLstTrack().size() == 0) {
                    View v = rootView.findViewById(R.id.user_tracks_list_empty);
                    recyclerViews[tabId].setEmptyView(v);
                }
                progressBar.setVisibility(View.GONE);
                adapter.addAll(tracks.getLstTrack());

                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;

                // TODO : check logic if good, adding these tracks to the  list in Side Menu
                activity.lstTracks = new ArrayList<>();
                activity.lstTracks.addAll(tracks.getLstTrack());

                activity.mProgressDialog.dismiss();
            }
            public void onFailure(Call<GetUserListTracksResponse> call, Throwable t)
            {
                activity.mProgressDialog.dismiss();
                Toast.makeText(activity,"something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadNextPage(int tabId)
    {
        activity.mProgressDialog.setMessage(getResources().getString( R.string.loader_msg));
        activity.mProgressDialog.show();

        Call<GetUserListTracksResponse> call = Global.client.GetTracksForUserList(currenUsertListId, currentPage, PAGE_SIZE);
        call.enqueue(new Callback<GetUserListTracksResponse>(){
            public void onResponse(Call<GetUserListTracksResponse> call, Response<GetUserListTracksResponse> response) {

                GetUserListTracksResponse tracks=response.body();
                TOTAL_PAGES = tracks.getTotalRows() / PAGE_SIZE;
                adapter.removeLoadingFooter();
                isLoading = false;
                Log.d("ResultTracks Next ", "tracksNum=" +tracks.getTotalRows());

               /* if(tracks.getTrack().size() == 0 && currentPage != 0)
                    currentPage --;*/
                adapter.addAll(tracks.getLstTrack());

                if (TOTAL_PAGES != 0 && currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;

                // TODO: check logic add these tracks to the  list in Side Menu
                activity.lstTracks.addAll(tracks.getLstTrack());

                activity.mProgressDialog.dismiss();
            }
            public void onFailure(Call<GetUserListTracksResponse> call, Throwable t)
            {
                activity.mProgressDialog.dismiss();currentPage --;
                Toast.makeText(activity,"something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public  void notifyUserListTracksAdapter()
    {
        adapter.notifyDataSetChanged();
    }

    private void doRefresh()
    {
        progressBar.setVisibility(View.VISIBLE);
        //  Execute network request if cache is expired; otherwise do not update data.
        adapter.getTracksList().clear();
        adapter.notifyDataSetChanged();
        View v = rootView.findViewById(R.id.user_tracks_list_empty);
        recyclerViews[active_tab].setEmptyView(v);
        v.setVisibility(View.INVISIBLE);
        loadFirstPage(active_tab);
    }

}
