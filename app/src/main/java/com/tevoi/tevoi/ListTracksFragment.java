package com.tevoi.tevoi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.Utils.HelperFunctions;
import com.tevoi.tevoi.adapter.PaginationAdapter;
import com.tevoi.tevoi.adapter.PaginationAdapterCallback;
import com.tevoi.tevoi.listener.OnSwipeTouchListener;
import com.tevoi.tevoi.model.PaginationScrollListener;
import com.tevoi.tevoi.model.RecyclerViewEmptySupport;
import com.tevoi.tevoi.model.TrackFilter;
import com.tevoi.tevoi.model.TrackObject;
import com.tevoi.tevoi.model.TrackResponseList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListTracksFragment extends Fragment
        implements AdapterView.OnItemSelectedListener, PaginationAdapterCallback
{
    PaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    ProgressBar progressBar;

    LinearLayout errorLayout;
    TextView txtError;
    Button btnRetry;

    //private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 0;
    private int currentPage = 0;
    int PAGE_SIZE =3;

    ArrayList<TrackObject> mTracks = new ArrayList<>();
    RecyclerViewEmptySupport[] recyclerViews = new RecyclerViewEmptySupport[3];
    //RecyclerView[] recyclerViews= new RecyclerView[3];
    int active_tab = 0;
    Button[] tabs = new Button[3];
    public int defaultTab;
    SideMenu activity;
    ImageButton btnSearch;

    public FragmentManager fm;
    //public ScrollView scrollViewListTracks;
    public LinearLayout linearLayoutListTracks;

    View rootView;

    public void initMediaPlayerLayout(View rootView) {
        activity.mainPlayerLayout = rootView.findViewById(R.id.layout_main_player_include);
        //activity.mainPlayerLayout = rootView.findViewById(R.id.linearLayout_media_player_main);
        activity.seekBarMainPlayer = rootView.findViewById(R.id.seekBar_main_player);
        activity.txtCurrentTime = rootView.findViewById(R.id.currentTime_main_player);
        activity.txtFinishTime = rootView.findViewById(R.id.fullTime_main_player);
        activity.txtTrackName = rootView.findViewById(R.id.txt_track_name_main_player);
        activity.btnPausePlayMainMediaPlayer = rootView.findViewById(R.id.img_btn_pause_main_player);

        /*activity.mainPlayerLayout.setOnTouchListener(new OnSwipeTouchListener(activity) {
                public void onSwipeTop() {
                    Toast.makeText(activity, "top", Toast.LENGTH_SHORT).show();
                }
                public void onSwipeRight() {
                    Toast.makeText(activity, "right", Toast.LENGTH_SHORT).show();
                }
                public void onSwipeLeft() {
                    Toast.makeText(activity, "left", Toast.LENGTH_SHORT).show();
                }
                public void onSwipeBottom() {
                    Toast.makeText(activity, "bottom", Toast.LENGTH_SHORT).show();
                }

            });*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tracks_list_test, container, false);
        activity = (SideMenu) getActivity();
        active_tab = defaultTab;

        errorLayout = (LinearLayout) rootView.findViewById(R.id.error_layout);

        txtError = (TextView) rootView.findViewById(R.id.error_txt_cause);
        btnRetry = (Button) rootView.findViewById(R.id.error_btn_retry);

        btnSearch = rootView.findViewById(R.id.btn_search);
        fm = getActivity().getSupportFragmentManager();
        //scrollViewListTracks = rootView.findViewById(R.id.scrollViewListTracks);
        linearLayoutListTracks = rootView.findViewById(R.id.linearLayoutListTracks);
        isLastPage = false;

        initMediaPlayerLayout(rootView);

        progressBar = (ProgressBar) rootView.findViewById(R.id.main_progress);


        activity.mainPlayerLayout.setOnTouchListener(new OnSwipeTouchListener(activity) {
            public void onSwipeTop() {
                Toast.makeText(activity, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Toast.makeText(activity, "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                Toast.makeText(activity, "left", Toast.LENGTH_SHORT).show();
                /*if (activity.CurrentTrackInPlayer != null) {
                    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                    // Replace the contents of the container with the new fragment
                    //TrackAddToList frag = new TrackAddToList();

                    activity.mediaPlayerFragment.currentTrackId = activity.CurrentTrackInPlayer.getId();

                    activity.mediaPlayerFragment.currentTrack = activity.CurrentTrackInPlayer;

                    ft.replace(R.id.content_frame, activity.mediaPlayerFragment);
                    ft.addToBackStack("mediaPlayerFragment");
                    // or ft.add(R.id.your_placeholder, new FooFragment());
                    // Complete the changes added above
                    ft.commit();

                } else {
                    Toast.makeText(activity, "You didn't choose a track", Toast.LENGTH_SHORT).show();
                }*/
            }
            public void onSwipeBottom() {
                Toast.makeText(activity, "bottom", Toast.LENGTH_SHORT).show();
            }

        });

        /*activity.mainPlayerLayout.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    //do something
                    if (activity.CurrentTrackInPlayer != null) {
                        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                        // Replace the contents of the container with the new fragment
                        //TrackAddToList frag = new TrackAddToList();

                        activity.mediaPlayerFragment.currentTrackId = activity.CurrentTrackInPlayer.getId();

                        activity.mediaPlayerFragment.currentTrack = activity.CurrentTrackInPlayer;

                        ft.replace(R.id.content_frame, activity.mediaPlayerFragment);
                        ft.addToBackStack("mediaPlayerFragment");
                        // or ft.add(R.id.your_placeholder, new FooFragment());
                        // Complete the changes added above
                        ft.commit();

                    } else {
                        Toast.makeText(activity, "You didn't choose a track", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });*/
        activity.seekBarMainPlayer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SideMenu activity = (SideMenu) getActivity();
                if (activity != null) {
                    if (activity.serviceBound && fromUser) {
                        // TODO  : check user quota

                        activity.player.mMediaPlayer.seekTo(progress * 1000);
                        String timeFormat = HelperFunctions.GetTimeFormat(progress);
                        activity.txtCurrentTime.setText(timeFormat);
                        int numberofMovedSeconds = progress - activity.numberOfCurrentSecondsInTrack;
                        activity.numberOfCurrentSecondsInTrack = progress;
                        activity.numberOfListenedSeconds += numberofMovedSeconds;
                        activity.player.numberOfListenedSeconds += numberofMovedSeconds;
                        //Toast.makeText(activity, "numberofMovedSeconds=" + numberofMovedSeconds, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // this case when the seek bar for main media player is playing and we moved to media playe fragment

                }
            }
        });


        activity.btnPausePlayMainMediaPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.serviceBound) {
                    if (activity.player.mMediaPlayer.isPlaying()) {
                        activity.player.mMediaPlayer.pause();
                        activity.player.buildNotification(CustomMediaPlayerService.PlaybackStatus.PAUSED);
                        activity.btnPausePlayMainMediaPlayer.setImageResource(R.mipmap.play_white_normal);
                    } else {
                        activity.player.mMediaPlayer.start();
                        activity.player.buildNotification(CustomMediaPlayerService.PlaybackStatus.PLAYING);
                        activity.btnPausePlayMainMediaPlayer.setImageResource(R.mipmap.pause_normal_white);
                    }
                } else {
                    //activity.playAudio(url);
                    //activity.btnPausePlayMainMediaPlayer.setImageResource(R.drawable.baseline_pause_24);
                }
            }
        });
        if (btnSearch != null) {

            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText txtFilter = rootView.findViewById(R.id.txt_search_filter_value);
                    CheckBox chkIsLocationEnabled = rootView.findViewById(R.id.checkBoxLocationEnable);
                    //activity.player.removeNotification();
                    if (!txtFilter.getText().equals("")) {
                        activity.mProgressDialog.setMessage(getResources().getString( R.string.loader_msg));
                        activity.mProgressDialog.show();

                        TrackFilter filter =  new TrackFilter();
                        filter.SearchKey = txtFilter.getText().toString(); filter.IsLocationEnabled = chkIsLocationEnabled.isChecked();
                        filter.ListTypeEnum = defaultTab; filter.Index = 0; filter.Size = 10;
                        Call<TrackResponseList> call = Global.client.ListMainTrackWithFilter(filter);
                        call.enqueue(new Callback<TrackResponseList>() {
                            public void onResponse(Call<TrackResponseList> call, Response<TrackResponseList> response) {
                                TrackResponseList tracks = response.body();
                                int x = tracks.getLstTrack().size();
                                View v = rootView.findViewById(R.id.tracks_list_empty);
                                recyclerViews[active_tab].setEmptyView(v);

                                adapter = new PaginationAdapter(tracks.getLstTrack(), activity, Global.ListTracksFragmentName);
                                adapter.setmCallback((PaginationAdapterCallback) getContext());

                                //recyclerViews[active_tab].setAdapter(adapter);
                                recyclerViews[active_tab].setAdapter(adapter);
                                activity.mProgressDialog.dismiss();
                            }

                            public void onFailure(Call<TrackResponseList> call, Throwable t) {
                                Toast.makeText(activity, "something went wrong", Toast.LENGTH_LONG).show();
                                activity.mProgressDialog.dismiss();
                            }
                        });
                    }

                }
            });
        }
        Spinner spinner = rootView.findViewById(R.id.user_lists_spinner);
        if (spinner != null)
            spinner.setOnItemSelectedListener(this);

        tabs[0] = rootView.findViewById(R.id.btnNewList);
        tabs[1] = rootView.findViewById(R.id.btnTopRatedList);
        tabs[2] = rootView.findViewById(R.id.btnPopularList);


        if (defaultTab < 0 || defaultTab > 2) {
            defaultTab = 0;
        }
        //defaultTab = 0;

/*
        recyclerViews[defaultTab] = rootView.findViewById(R.id.tracks_recycler_View);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViews[defaultTab].setLayoutManager(layoutManager);

        //adapter = new PaginationAdapter(mTracks, activity,Global.ListTracksFragmentName);

        recyclerViews[defaultTab].setItemAnimator(new DefaultItemAnimator());

        //recyclerViews[defaultTab].setAdapter(adapter);

        recyclerViews[defaultTab].addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
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
*/

        activateTab(defaultTab);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActivity().setContentView(R.layout.activity_tracks_list);
        //defaultTab = 0;
        if (getArguments() != null)
            defaultTab = getArguments().getInt("DefaultTab");
    }

    public void changeTabToNew(View view) {
        active_tab = 0;
        activateTab(0);
    }

    public void changeTabToTopRated(View view) {
        active_tab = 1;
        activateTab(1);
    }

    public void changeToPopular(View view) {
        active_tab = 2;
        activateTab(2);
    }

    public void activateTab(final int k)
    {

        for (int i = 0; i < recyclerViews.length; i++) {
            recyclerViews[i] = null;
        }
        recyclerViews[k] = rootView.findViewById(R.id.tracks_recycler_View);
        for (int i = 0; i < tabs.length; i++) {
            tabs[i].setBackgroundColor(ContextCompat.getColor(activity, R.color.tevoiBlueSecondary));
        }

        tabs[k].setBackgroundColor(ContextCompat.getColor(activity, R.color.tevoiBluePrimary));

        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViews[k].setLayoutManager(linearLayoutManager);

        List<TrackObject> tracks = new ArrayList<>();
        adapter = new PaginationAdapter(tracks, activity, Global.ListTracksFragmentName);
        //adapter.setmCallback((PaginationAdapterCallback) this);

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
        }, 1000);


        /*//tabs[k].refreshDrawableState();
        Call<TrackResponseList> call = Global.client.getListMainTrack(k, currentPage, 10);
        call.enqueue(new Callback<TrackResponseList>() {
            public void onResponse(Call<TrackResponseList> call, Response<TrackResponseList> response) {
                //generateDataList(response.body());
                TrackResponseList tracks = response.body();
                View v = rootView.findViewById(R.id.tracks_list_empty);
                recyclerViews[kk].setEmptyView(v);

                adapter = new PaginationAdapter(mTracks, activity, Global.ListTracksFragmentName);

                recyclerViews[kk].setItemAnimator(new DefaultItemAnimator());

                recyclerViews[kk].setAdapter(adapter);
            }

            public void onFailure(Call<TrackResponseList> call, Throwable t) {
                Toast.makeText(activity, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
*/
    }


    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        Toast.makeText(getContext(), "Hiii there I'm marwa", Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void notifyTarcksListAdapter() {
        adapter.notifyDataSetChanged();
    }

    private void loadFirstPage(final int tabId)
    {
        activity.mProgressDialog.setMessage("Loading1");
        activity.mProgressDialog.show();

        //Log.d(TAG, "loadFirstPage: ");
        TrackFilter filter =  new TrackFilter();
        filter.SearchKey = ""; filter.IsLocationEnabled = false;
        filter.TrackTypeId = 1;
        filter.ListTypeEnum = tabId; filter.Index = currentPage; filter.Size = PAGE_SIZE;
        Call<TrackResponseList> call = Global.client.getListMainTrack(filter);
        call.enqueue(new Callback<TrackResponseList>() {
            public void onResponse(Call<TrackResponseList> call, Response<TrackResponseList> response) {

                TrackResponseList tracks = response.body();
                TOTAL_PAGES = tracks.getTotalRowCount() / PAGE_SIZE;

                View v = rootView.findViewById(R.id.tracks_list_empty);
                recyclerViews[tabId].setEmptyView(v);

               /* adapter = new PaginationAdapter(tracks.getTrack(), activity, Global.ListTracksFragmentName);

                linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerViews[tabId].setLayoutManager(linearLayoutManager);

                recyclerViews[tabId].setItemAnimator(new DefaultItemAnimator());

                recyclerViews[tabId].setAdapter(adapter);*/

                progressBar.setVisibility(View.GONE);
                adapter.addAll(tracks.getLstTrack());

                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
                activity.mProgressDialog.dismiss();
            }

            public void onFailure(Call<TrackResponseList> call, Throwable t) {
                activity.mProgressDialog.dismiss();
                t.printStackTrace();
                showErrorView(t);
            }
        });

    }

    private void loadNextPage(int tabId) {

        activity.mProgressDialog.setMessage(getResources().getString( R.string.loader_msg));
        activity.mProgressDialog.show();

        TrackFilter filter =  new TrackFilter();
        filter.SearchKey = ""; filter.IsLocationEnabled = false;
        filter.TrackTypeId =1;
        filter.ListTypeEnum = tabId; filter.Index = currentPage; filter.Size = PAGE_SIZE;
        Call<TrackResponseList> call = Global.client.getListMainTrack(filter);
        call.enqueue(new Callback<TrackResponseList>() {
            public void onResponse(Call<TrackResponseList> call, Response<TrackResponseList> response) {
                //generateDataList(response.body());
                TrackResponseList tracks = response.body();
                TOTAL_PAGES = tracks.getTotalRowCount() / PAGE_SIZE;
                adapter.removeLoadingFooter();
                isLoading = false;

                if(tracks.getLstTrack().size() == 0 && currentPage != 0)
                    currentPage --;

                adapter.addAll(tracks.getLstTrack());

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;

                /*adapter = new PaginationAdapter(tracks.getTrack(), activity, Global.ListTracksFragmentName);
                View v = rootView.findViewById(R.id.tracks_list_empty);
                recyclerViews[active_tab].setEmptyView(v);
                recyclerViews[defaultTab].setAdapter(adapter);*/
                activity.mProgressDialog.dismiss();
            }

            public void onFailure(Call<TrackResponseList> call, Throwable t) {
                activity.mProgressDialog.dismiss();currentPage --;
            }
        });



    }


    @Override
    public void retryPageLoad() {
        loadNextPage(defaultTab);
    }

    private void showErrorView(Throwable throwable) {
        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            txtError.setText(fetchErrorMessage(throwable));
        }
    }

    /**
     * Remember to add android.permission.ACCESS_NETWORK_STATE permission.
     *
     * @return
     */
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @param throwable to identify the type of error
     * @return appropriate error message
     */
    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getResources().getString(R.string.error_msg_unknown);

        if (!isNetworkConnected()) {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }
}