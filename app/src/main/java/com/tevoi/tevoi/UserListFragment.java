
package com.tevoi.tevoi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.tevoi.tevoi.adapter.TracksAdapter;
import com.tevoi.tevoi.adapter.UserListAdapter;
import com.tevoi.tevoi.model.IResponse;
import com.tevoi.tevoi.model.PaginationScrollListener;
import com.tevoi.tevoi.model.RecyclerViewEmptySupport;
import com.tevoi.tevoi.model.TrackObject;
import com.tevoi.tevoi.model.TrackResponseList;
import com.tevoi.tevoi.model.UserListObject;
import com.tevoi.tevoi.model.UserListResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListFragment extends Fragment
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


    UserListAdapter adapter ;
    //RecyclerView recyclerView;
    RecyclerViewEmptySupport recyclerView;
    SideMenu activity;

    ImageButton imgBtnAddUserList;

    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_user_list, container, false);
        activity = (SideMenu)getActivity();

        initiatePagination();

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
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        String listName = userInput.getText().toString();
                                        if (listName.equals(""))
                                        {
                                            Toast.makeText(getContext(), "Name can;t be empty", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            activity.mProgressDialog.setMessage("Loading");
                                            activity.mProgressDialog.show();
                                            // add user list
                                            Call<IResponse> call = Global.client.AddUserList(listName);
                                            call.enqueue(new Callback<IResponse>(){
                                                public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                                                    IResponse result = response.body();
                                                    Toast.makeText(getContext(),result.getMessage(), Toast.LENGTH_LONG).show();
                                                    activity.mProgressDialog.dismiss();
                                                    GetUserLists();
                                                }
                                                public void onFailure(Call<IResponse> call, Throwable t)
                                                {
                                                    Toast.makeText(getContext(),"something went wrong", Toast.LENGTH_LONG).show();
                                                    activity.mProgressDialog.dismiss();
                                                }
                                            });



                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        });

        recyclerView = rootView.findViewById(R.id.user_list_recycler_View);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setEmptyView(rootView.findViewById(R.id.user_lists_empty));

        List<UserListObject> lists = new ArrayList<>();
        adapter = new UserListAdapter(lists, activity);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);

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

    public  void GetUserLists()
    {
        activity.mProgressDialog.setMessage("Loading");
        activity.mProgressDialog.show();

        Call<UserListResponse> call = Global.client.getUserLists(0, 10);
        call.enqueue(new Callback<UserListResponse>(){
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                //generateDataList(response.body());
                UserListResponse userLists =response.body();
                int x=userLists.getLstUserList().size();
                adapter = new UserListAdapter(userLists.getLstUserList(), activity);
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

    private void loadFirstPage()
    {
        currentPage = 0;
        activity.mProgressDialog.setMessage("Loading1");
        activity.mProgressDialog.show();

        Call<UserListResponse> call = Global.client.getUserLists(currentPage, PAGE_SIZE);
        call.enqueue(new Callback <UserListResponse>(){
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                //generateDataList(response.body());
                SideMenu activity = (SideMenu) getActivity();
                UserListResponse userLists =response.body();

                //TOTAL_PAGES = userLists.getTotalRowCount() / PAGE_SIZE;

                if(userLists.getLstUserList().size() == 0) {
                    View v = rootView.findViewById(R.id.favourite_list_empty);
                    recyclerView.setEmptyView(v);
                }
                progressBar.setVisibility(View.GONE);
                adapter.addAll(userLists.getLstUserList());

                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
                activity.mProgressDialog.dismiss();
                adapter.notifyDataSetChanged();
            }
            public void onFailure(Call<UserListResponse> call, Throwable t)
            {
                activity.mProgressDialog.dismiss();
                t.printStackTrace();
                showErrorView(t);
                Toast.makeText(getContext(),"something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadNextPage() {

        activity.mProgressDialog.setMessage("Loading");
        activity.mProgressDialog.show();

        Call<UserListResponse> call = Global.client.getUserLists(currentPage, PAGE_SIZE);
        call.enqueue(new Callback <UserListResponse>(){
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                //generateDataList(response.body());
                SideMenu activity = (SideMenu) getActivity();
                UserListResponse userLists =response.body();

                //TOTAL_PAGES = userLists.getTotalRowCount() / PAGE_SIZE;
                adapter.removeLoadingFooter();
                isLoading = false;

                adapter.addAll(userLists.getLstUserList());
                if(userLists.getLstUserList().size() == 0)
                {
                    currentPage --;
                }
                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;

                activity.mProgressDialog.dismiss();
                adapter.notifyDataSetChanged();
            }
            public void onFailure(Call<UserListResponse> call, Throwable t)
            {
                activity.mProgressDialog.dismiss(); currentPage --;
                t.printStackTrace();
                showErrorView(t);
                Toast.makeText(getContext(),"something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

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


}