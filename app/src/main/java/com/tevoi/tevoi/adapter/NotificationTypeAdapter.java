package com.tevoi.tevoi.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tevoi.tevoi.R;
import com.tevoi.tevoi.SideMenu;
import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.model.IResponse;
import com.tevoi.tevoi.model.ListNotificationTypesResponse;
import com.tevoi.tevoi.model.NotificationTypeObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationTypeAdapter extends RecyclerView.Adapter<NotificationTypeAdapter.NotificationTypeViewHolder>
{
    private List<NotificationTypeObject> notificationTypes;
    private SideMenu activity;

    public NotificationTypeAdapter(List<NotificationTypeObject> notificationTypes, SideMenu activity) {
        this.notificationTypes = notificationTypes;
        this.activity = activity;
    }
    public NotificationTypeAdapter.NotificationTypeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_notification_instance,viewGroup,false);
        NotificationTypeAdapter.NotificationTypeViewHolder holder = new NotificationTypeAdapter.NotificationTypeViewHolder(row);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationTypeAdapter.NotificationTypeViewHolder viewHolder, int i) {
        NotificationTypeObject nt =  notificationTypes.get(i);
        viewHolder.tvNotificationTypeName.setText(nt.getName());
        viewHolder.switchCompatIsActive.setChecked(nt.isActive());
    }

    @Override
    public int getItemCount() {
        return notificationTypes.size();
    }
    class NotificationTypeViewHolder extends RecyclerView.ViewHolder {
        public View view;
        TextView tvNotificationTypeName;

        SwitchCompat switchCompatIsActive;

        public NotificationTypeViewHolder(@NonNull View itemView)
        {
            super(itemView);
            //this.view=itemView.findViewWithTag(R.id.track_row_layout);
            tvNotificationTypeName = itemView.findViewById(R.id.notification_type_name);

            switchCompatIsActive = itemView.findViewById(R.id.switch_notification_type);

            switchCompatIsActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    int i = getAdapterPosition();

/*                    activity.mProgressDialog.setMessage(activity.getResources().getString( R.string.loader_msg));
                    activity.mProgressDialog.show();*/

                    Call<IResponse> call = Global.client.UpdateNotificationType(notificationTypes.get(i).getId());
                    call.enqueue(new Callback<IResponse>(){
                        public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                            //generateDataList(response.body());
                            IResponse notificationTypes =response.body();

//                            activity.mProgressDialog.dismiss();

                        }
                        public void onFailure(Call<IResponse> call, Throwable t)
                        {
//                            activity.mProgressDialog.dismiss();
                        }
                    });

                }
            });

        }

    }
}
