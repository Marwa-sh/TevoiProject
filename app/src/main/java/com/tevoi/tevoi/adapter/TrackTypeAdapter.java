package com.tevoi.tevoi.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tevoi.tevoi.R;
import com.tevoi.tevoi.model.TrackTypeObject;

import java.util.List;

public class TrackTypeAdapter extends RecyclerView.Adapter<TrackTypeAdapter.TrackTypeViewHolder>
{
    //public TrackTypeObject[] trackTypeObjects;
    private Context context;
    public List<TrackTypeObject> trackTypeObjects;

    public List<TrackTypeObject> getTrackTypeObjects() {
        return trackTypeObjects;
    }

    public void setTrackTypeObjects(List<TrackTypeObject> trackTypeObjects) {
        this.trackTypeObjects = trackTypeObjects;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public TrackTypeAdapter(List<TrackTypeObject> trackTypeObjects, Context context) {
        this.trackTypeObjects = trackTypeObjects;
        this.context = context;
    }

    public TrackTypeAdapter() {

    }

    public TrackTypeAdapter(Context context, List<TrackTypeObject> trackTypeObjects)
    {
        this.context=context;
        this.trackTypeObjects= trackTypeObjects;

    }

    @NonNull
    @Override
    public TrackTypeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View row= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_filter_instance,viewGroup,false);
        TrackTypeAdapter.TrackTypeViewHolder holder = new TrackTypeViewHolder(row);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull TrackTypeViewHolder trackTypeViewHolder, int i) {

        trackTypeViewHolder.tvTrackType.setText(trackTypeObjects.get(i).getName());
        trackTypeViewHolder.checkBoxFilterState.setChecked(trackTypeObjects.get(i).isTrackTypeFilter());
    }

    @Override
    public int getItemCount() {
        return trackTypeObjects.size();
    }

    public class TrackTypeViewHolder extends RecyclerView.ViewHolder
    {
        public View view;
        public TextView tvTrackType;
        public int Id;
        public SwitchCompat checkBoxFilterState;

        public TrackTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTrackType=itemView.findViewById(R.id.tv_filter_name);
            checkBoxFilterState=itemView.findViewById(R.id.checkbox_filter_state);
        }
    }

}
