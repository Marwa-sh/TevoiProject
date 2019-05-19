
package com.ebridge.tevoi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ebridge.tevoi.Utils.Global;
import com.ebridge.tevoi.adapter.NotificationTypeAdapter;
import com.ebridge.tevoi.adapter.PartnerAdapter;
import com.ebridge.tevoi.adapter.UserListAdapter;
import com.ebridge.tevoi.model.ListNotificationTypesResponse;
import com.ebridge.tevoi.model.NotificationTypeObject;
import com.ebridge.tevoi.model.PartnerListResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment {
    NotificationTypeAdapter adapter ;
    RecyclerView recyclerView;
    View rootView;
    SideMenu activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        activity = (SideMenu)getActivity();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.notification_types_recycler);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        activity.mProgressDialog.setMessage("Loading");
        activity.mProgressDialog.show();

        Call<ListNotificationTypesResponse> call = Global.client.GetNotificationTypesList();
        call.enqueue(new Callback<ListNotificationTypesResponse>(){
            public void onResponse(Call<ListNotificationTypesResponse> call, Response<ListNotificationTypesResponse> response) {
                //generateDataList(response.body());
                ListNotificationTypesResponse notificationTypes =response.body();

                recyclerView.setAdapter(adapter);
                SideMenu activity = (SideMenu)getActivity();
                adapter = new NotificationTypeAdapter(notificationTypes.getLstNotiicationTypes(),activity);
                recyclerView.setAdapter(adapter);
                activity.mProgressDialog.dismiss();

            }
            public void onFailure(Call<ListNotificationTypesResponse> call, Throwable t)
            {
                activity.mProgressDialog.dismiss();
                Toast.makeText(getContext(),"something went wrong", Toast.LENGTH_SHORT);
            }
        });

        return rootView;
    }
}
