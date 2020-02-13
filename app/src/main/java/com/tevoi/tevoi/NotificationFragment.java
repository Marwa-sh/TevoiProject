
package com.tevoi.tevoi;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.adapter.NotificationTypeAdapter;
import com.tevoi.tevoi.model.ListNotificationTypesResponse;
import com.tevoi.tevoi.model.NotificationTypeObject;
import com.tevoi.tevoi.model.TrackObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment {
    NotificationTypeAdapter adapter ;
    RecyclerView recyclerView;
    View rootView;
    SideMenu activity;
    SwitchCompat switchCompatAll;
    List<NotificationTypeObject> lstNotification = new ArrayList<NotificationTypeObject>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        activity = (SideMenu)getActivity();

        lstNotification = activity.storageManager.loadNotificationtList(activity);

        recyclerView = rootView.findViewById(R.id.notification_types_recycler);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        switchCompatAll = rootView.findViewById(R.id.switch_notification_type_all);
        switchCompatAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    // set all types to true and refresh adapter
                    activity.mProgressDialog.setMessage(getResources().getString( R.string.loader_msg));
                    activity.mProgressDialog.show();

                    Call<ListNotificationTypesResponse> call = Global.client.UpdateAllNotificationType(true);
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
                            Toast.makeText(getContext(),getResources().getString( R.string.something_went_wrong), Toast.LENGTH_SHORT);
                        }
                    });
                }
                else {
                    // set all types to true and refresh adapter
                    activity.mProgressDialog.setMessage(getResources().getString( R.string.loader_msg));
                    activity.mProgressDialog.show();

                    Call<ListNotificationTypesResponse> call = Global.client.UpdateAllNotificationType(false);
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
                            Toast.makeText(getContext(),getResources().getString( R.string.something_went_wrong), Toast.LENGTH_SHORT);
                        }
                    });
                }
            }
        });

       /* activity.mProgressDialog.setMessage(getResources().getString( R.string.loader_msg));
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
        });*/

        //recyclerView.setAdapter(adapter);
        SideMenu activity = (SideMenu)getActivity();
        adapter = new NotificationTypeAdapter(lstNotification,activity);
        recyclerView.setAdapter(adapter);


        return rootView;
    }
}
