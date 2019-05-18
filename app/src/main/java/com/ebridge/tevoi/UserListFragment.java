
package com.ebridge.tevoi;

import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ebridge.tevoi.Utils.Global;
import com.ebridge.tevoi.adapter.TracksAdapter;
import com.ebridge.tevoi.adapter.UserListAdapter;
import com.ebridge.tevoi.model.IResponse;
import com.ebridge.tevoi.model.TrackResponseList;
import com.ebridge.tevoi.model.UserListResponse;
import com.ebridge.tevoi.rest.ApiClient;
import com.ebridge.tevoi.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListFragment extends Fragment {
    UserListAdapter adapter ;
    RecyclerView recyclerView;

    ImageButton imgBtnAddUserList;
    ProgressDialog mProgressDialog;


    ApiInterface client;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_user_list, container, false);
        mProgressDialog = new ProgressDialog(getActivity());

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

                final EditText userInput = (EditText) promptsView
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
                                            mProgressDialog.setMessage("Loading");
                                            mProgressDialog.show();
                                            // add user list
                                            Call<IResponse> call = client.AddUserList(listName);
                                            call.enqueue(new Callback<IResponse>(){
                                                public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                                                    IResponse result = response.body();
                                                    if(result.Number == 0)
                                                    {
                                                        Toast.makeText(getContext(),"List Added Successfully", Toast.LENGTH_LONG);
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(getContext(),result.Message, Toast.LENGTH_LONG);
                                                    }
                                                    mProgressDialog.dismiss();
                                                }
                                                public void onFailure(Call<IResponse> call, Throwable t)
                                                {
                                                    Toast.makeText(getContext(),"something went wrong", Toast.LENGTH_LONG);
                                                    mProgressDialog.dismiss();
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

        client = ApiClient.getClient().create(ApiInterface.class);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.user_list_recycler_View);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        Call<UserListResponse> call = client.getUserLists(0, 10);
        call.enqueue(new Callback<UserListResponse>(){
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                //generateDataList(response.body());
                SideMenu activity = (SideMenu) getActivity();
                UserListResponse userLists =response.body();
                int x=userLists.getLstUserList().size();
                adapter = new UserListAdapter(userLists.getLstUserList(), activity);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                Toast.makeText(getContext(),"tracks:"+x, Toast.LENGTH_SHORT);
            }
            public void onFailure(Call<UserListResponse> call, Throwable t)
            {
                Toast.makeText(getContext(),"something went wrong", Toast.LENGTH_SHORT);
            }
        });

        return rootView;
    }

    public void notifyUserListAdapter()
    {
        adapter.notifyDataSetChanged();
    }
}