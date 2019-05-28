package com.tevoi.tevoi.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tevoi.tevoi.R;
import com.tevoi.tevoi.model.NotificationTypeObject;

import java.util.List;

public class NotificationTypeAdapter extends RecyclerView.Adapter<NotificationTypeAdapter.NotificationTypeViewHolder>
{
    private List<NotificationTypeObject> notificationTypes;
    private Context context;

    public NotificationTypeAdapter(List<NotificationTypeObject> notificationTypes, Context context) {
        this.notificationTypes = notificationTypes;
        this.context = context;
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
    }

    @Override
    public int getItemCount() {
        return notificationTypes.size();
    }
    class NotificationTypeViewHolder extends RecyclerView.ViewHolder {
        public View view;
        TextView tvNotificationTypeName;

        SwitchCompat switchCompatIsActive;

        public NotificationTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            //this.view=itemView.findViewWithTag(R.id.track_row_layout);
            tvNotificationTypeName = itemView.findViewById(R.id.notification_type_name);

            switchCompatIsActive = itemView.findViewById(R.id.switch_notification_type);

            switchCompatIsActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    //Toast.makeText(context, "switch changes to " + isChecked, Toast.LENGTH_LONG).show();
                }
            });

        }

    }
}
