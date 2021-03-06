
package com.tevoi.tevoi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.Utils.HelperFunctions;
import com.tevoi.tevoi.adapter.UserListAdapter;
import com.tevoi.tevoi.model.AddUserListResponse;
import com.tevoi.tevoi.model.IResponse;
import com.tevoi.tevoi.model.PaginationScrollListener;
import com.tevoi.tevoi.model.PartnerListResponse;
import com.tevoi.tevoi.model.PartnerObject;
import com.tevoi.tevoi.model.RecyclerViewEmptySupport;
import com.tevoi.tevoi.model.TrackObject;
import com.tevoi.tevoi.model.UserListObject;
import com.tevoi.tevoi.model.UserListResponse;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener

{
    // region pagination properties
    LinearLayoutManager linearLayoutManager;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout mEmptyViewContainer;

    LinearLayout errorLayout;
    TextView txtError;
    Button btnRetry;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 0;
    private int currentPage = 0;
    int PAGE_SIZE = Global.PAGE_SIZE;
    // endregion
    List<UserListObject> Userlst = new ArrayList<UserListObject>();


    UserListAdapter adapter ;
    //RecyclerView recyclerView;
    RecyclerViewEmptySupport recyclerView;
    SideMenu activity;
    ImageButton btn_clear_user_lists;
    ImageButton imgBtnAddUserList;
    boolean isFirtsTime = true;


    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_user_list, container, false);
        activity = (SideMenu)getActivity();
        btn_clear_user_lists = rootView.findViewById(R.id.btn_clear_user_lists);

        Userlst = activity.storageManager.loadUserList(activity);
        TOTAL_PAGES = Userlst.size()/ PAGE_SIZE;


        mEmptyViewContainer = rootView.findViewById(R.id.swipeRefreshLayout_emptyView);
        onCreateSwipeToRefresh(mEmptyViewContainer);

        swipeRefreshLayout = rootView.findViewById(R.id.main_swiperefresh_userlist);
        swipeRefreshLayout.setOnRefreshListener(this);


        initiatePagination();

        btn_clear_user_lists.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(adapter.getUserLists().size() == 0)
                {
                    Toast.makeText(activity, R.string.no_lists_to_delete, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    LayoutInflater layoutInflater = LayoutInflater.from(activity);
                    View promptView = layoutInflater.inflate(R.layout.layout_user_list_clear_lists, null);

                    final AlertDialog alertD = new AlertDialog.Builder(activity).create();

                    ImageButton btnYes = (ImageButton) promptView.findViewById(R.id.imgBtnYes);

                    ImageButton btnNo = (ImageButton) promptView.findViewById(R.id.imgBtnNo);

                    btnYes.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            alertD.cancel();
                            activity.mProgressDialog.setMessage(activity.getResources().getString( R.string.loader_msg));
                            activity.mProgressDialog.show();

                            Call<IResponse> call = Global.client.ClearUserList();
                            call.enqueue(new Callback<IResponse>() {
                                public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                                    //generateDataList(response.body());
                                    SideMenu activity = (SideMenu) getActivity();
                                    IResponse result = response.body();
                                    if(result.getNumber()==0)
                                    {
                                        activity.storageManager.deleteUserList(activity);
                                        adapter.clear();
                                        recyclerView.triggerObserver();
                                        Toast.makeText(activity,activity.getResources().getString(R.string.cleared_successfully), Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(activity,activity.getResources().getString(R.string.general_error), Toast.LENGTH_SHORT).show();
                                    }
                                    activity.mProgressDialog.dismiss();
                            }

                                public void onFailure(Call<IResponse> call, Throwable t) {
                                    activity.mProgressDialog.dismiss();
                                    Toast.makeText(getContext(), "something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    btnNo.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            // btnAdd2 has been clicked
                            alertD.cancel();
                        }
                    });
                    alertD.setView(promptView);
                    alertD.show();
                }
            }
        });

        imgBtnAddUserList = rootView.findViewById(R.id.imgBtnAddUserList);
        imgBtnAddUserList.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(getContext());
                View promptsView = li.inflate(R.layout.layout_user_list_edittext, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getContext());

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = promptsView
                        .findViewById(R.id.edittxtUserListName);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(activity.getResources().getText(R.string.Yes),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        String listName = userInput.getText().toString();
                                        if (listName.equals(""))
                                        {
                                            Toast.makeText(getContext(), R.string.list_name_required, Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            activity.mProgressDialog.setMessage(getResources().getString(R.string.loader_msg));
                                            activity.mProgressDialog.show();
                                            Call<AddUserListResponse> call = Global.client.AddGetUserList(listName);
                                            call.enqueue(new Callback<AddUserListResponse>(){
                                                public void onResponse(Call<AddUserListResponse> call, Response<AddUserListResponse> response) {
                                                    AddUserListResponse result = response.body();

                                                    if (result.getNumber() == 0)
                                                    {
                                                        Toast.makeText(getContext(),R.string.user_list_added_successfully, Toast.LENGTH_LONG).show();
                                                        adapter.add(result.getUserList());

                                                        Collections.sort(adapter.getUserLists(), new Comparator<UserListObject>() {
                                                            @Override
                                                            public int compare(UserListObject o1, UserListObject o2)
                                                            {
                                                                if(o1.getCreationDate()== null && o2.getCreationDate() == null )
                                                                    return  0;
                                                                else {
                                                                    Date d1 = new Date(o1.getCreationDate());
                                                                    Date d2 = new Date(o2.getCreationDate());
                                                                    int d =  d1.compareTo(d2);
                                                                    return d;
                                                                }
                                                                //Integer.compare((int)o1.getRate(), (int)o2.getRate());
                                                            }

                                                        });
                                                        adapter.notifyDataSetChanged();
                                                        recyclerView.triggerObserver();

                                                        Log.d("UserListId=", "New=" + result.getUserList().getId());
                                                        //adapter.notifyDataSetChanged();
                                                        activity.mProgressDialog.dismiss();
                                                    } else {
                                                        Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();
                                                        activity.mProgressDialog.dismiss();
                                                    }
//                                                    doRefresh();
                                                }
                                                public void onFailure(Call<AddUserListResponse> call, Throwable t)
                                                {
                                                    Toast.makeText(getContext(),R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                                                    activity.mProgressDialog.dismiss();
                                                }
                                            });



                                        }
                                    }
                                })
                        .setNegativeButton(activity.getResources().getText(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(activity.getResources().getColor(R.color.tevoiBrownDark));
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.tevoiBrownDark));

                    }
                });
                alertDialog.show();
            }
        });

        View emptyView = rootView.findViewById(R.id.user_lists_empty);

        recyclerView = rootView.findViewById(R.id.user_list_recycler_View);

        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setEmptyView(mEmptyViewContainer);
        //emptyView.setVisibility(View.INVISIBLE);

        List<UserListObject> lists = new ArrayList<>();


        //List<UserListObject> lstFirstPage =  HelperFunctions.getPageUserList(Userlst, 0 , PAGE_SIZE );

        adapter = new UserListAdapter(lists, activity, recyclerView);


        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        //emptyView.setVisibility(View.INVISIBLE);


        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager)
        {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                if(currentPage <TOTAL_PAGES)
                { currentPage += 1;

                loadNextPage();}
                /*else if(currentPage == TOTAL_PAGES)
                {
                    loadNextPage();
                }*/
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

        /*// mocking network delay for API call
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadFirstPage();
            }
        }, 1000);*/
        loadFirstPage();
        isFirtsTime = false;
       /* activity.mProgressDialog.setMessage("Loading");
        activity.mProgressDialog.show();

        Call<UserListResponse> call = Global.client.getUserLists(0, 10);
        call.enqueue(new Callback<UserListResponse>(){
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                //generateDataList(response.body());
                SideMenu activity = (SideMenu) getActivity();
                UserListResponse userLists =response.body();
                int x = userLists.getLstUserList().size();
                adapter = new UserListAdapter(userLists.getLstUserList(), activity);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                activity.mProgressDialog.dismiss();
                //Toast.makeText(activity,userLists.Message, Toast.LENGTH_LONG).show();
            }
            public void onFailure(Call<UserListResponse> call, Throwable t)
            {
                activity.mProgressDialog.dismiss();
                Toast.makeText(getContext(),"something went wrong", Toast.LENGTH_SHORT);
            }
        });*/

        //Toast.makeText(activity, "token=" + Global.UserToken, Toast.LENGTH_LONG).show();

        return rootView;
    }
    private void onCreateSwipeToRefresh(SwipeRefreshLayout refreshLayout) {

        refreshLayout.setOnRefreshListener(this);

        /*refreshLayout.setColorScheme(
                android.R.color.holo_blue_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light);
*/
    }

    public  void GetUserLists()
    {
        activity.mProgressDialog.setMessage(getResources().getString( R.string.loader_msg));
        activity.mProgressDialog.show();

        Call<UserListResponse> call = Global.client.getUserLists(0, 10);
        call.enqueue(new Callback<UserListResponse>(){
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                //generateDataList(response.body());
                UserListResponse userLists =response.body();
                int x=userLists.getLstUserList().size();
                adapter = new UserListAdapter(userLists.getLstUserList(), activity,recyclerView);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                activity.mProgressDialog.dismiss();
            }
            public void onFailure(Call<UserListResponse> call, Throwable t)
            {
                activity.mProgressDialog.dismiss();
                Toast.makeText(getContext(),"something went wrong", Toast.LENGTH_SHORT);
            }
        });

    }

    public void notifyUserListAdapter()
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

    @Override
    public void onRefresh() {
        progressBar.setVisibility(View.VISIBLE);

        getRefreshUserList();

        // TODO: Check if data is stale.
        //  Execute network request if cache is expired; otherwise do not update data.

    }


    private void loadFirstPage()
    {
        currentPage = 0;
        progressBar.setVisibility(View.GONE);
        List<UserListObject> lstFirstPage =  HelperFunctions.getPageUserList(Userlst, 0 , PAGE_SIZE );
        adapter.addAll(lstFirstPage);
        recyclerView.triggerObserver();

        //adapter.addAll(lstTracks);
        /*if(lstFirstPage.size() == 0) {
            recyclerView.setEmptyView(mEmptyViewContainer);
        }*/
        //mEmptyViewContainer.setRefreshing(false);
        /*if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
        else isLastPage = true;*/
    }

    private void loadNextPage() {
        //adapter.removeLoadingFooter();
        isLoading = false;

        List<UserListObject> lstNextPage = HelperFunctions.getPageUserList(Userlst, currentPage , PAGE_SIZE );
        adapter.addAll(lstNextPage);
        //adapter.addAll(lstTracks);

        /*if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
        else isLastPage = true;*/

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

    // TODO what to do with this function Marwa
    private void doRefresh()
    {
        progressBar.setVisibility(View.VISIBLE);
        //  Execute network request if cache is expired; otherwise do not update data.
        adapter.getUserLists().clear();
        adapter.notifyDataSetChanged();
        View v = rootView.findViewById(R.id.user_lists_empty);
        recyclerView.setEmptyView(v);
        v.setVisibility(View.INVISIBLE);
        loadFirstPage();
    }

    private void getRefreshUserList()
    {

        Call<UserListResponse> call = Global.client.getUserLists(0,0 );
        call.enqueue(new Callback<UserListResponse>() {
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response)
            {
                // replace old list tracks with new one from server
                UserListResponse UserList = response.body();

                adapter.clear();
                //adapter.notifyDataSetChanged();
                Userlst = UserList.getLstUserList();
                activity.storageManager.storeUsetList(activity, Userlst);
                TOTAL_PAGES = Userlst.size()/ PAGE_SIZE;

                loadFirstPage();

                swipeRefreshLayout.setRefreshing(false);
                mEmptyViewContainer.setRefreshing(false);
            }
            public void onFailure(Call<UserListResponse> call, Throwable t)
            {
                swipeRefreshLayout.setRefreshing(false);
                mEmptyViewContainer.setRefreshing(false);
                //activity.mProgressDialog.dismiss();
            }
        });
    }
}