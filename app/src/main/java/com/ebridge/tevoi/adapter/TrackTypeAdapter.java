package com.ebridge.tevoi.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ebridge.tevoi.R;
import com.ebridge.tevoi.model.TrackTypeObject;

public class TrackTypeAdapter extends RecyclerView.Adapter<TrackTypeAdapter.TrackTypeViewHolder>
{
    public TrackTypeObject[] trackTypeObjects;
    private Context context;

    public TrackTypeAdapter(TrackTypeObject[] trackTypeObjects, Context context) {
        this.trackTypeObjects = trackTypeObjects;
        this.context = context;
    }

    public TrackTypeAdapter() {

    }

    public TrackTypeAdapter(Context context){
        this.context=context;
        this.trackTypeObjects= new TrackTypeObject[3];
        trackTypeObjects[0]=new TrackTypeObject();
        trackTypeObjects[1]=new TrackTypeObject();
        trackTypeObjects[2]=new TrackTypeObject();

        trackTypeObjects[0].setName("Don't Show Heard Tracks");
        trackTypeObjects[0].setTrackTypeFilter(false);

        trackTypeObjects[1].setName("News");
        trackTypeObjects[1].setTrackTypeFilter(false);

        trackTypeObjects[2].setName("Articles");
        trackTypeObjects[2].setTrackTypeFilter(false);
    }

    @NonNull
    @Override
    public TrackTypeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_filter_instance,viewGroup,false);
        TrackTypeAdapter.TrackTypeViewHolder holder = new TrackTypeViewHolder(row);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull TrackTypeViewHolder trackTypeViewHolder, int i) {

        trackTypeViewHolder.tvTrackType.setText(trackTypeObjects[i].getName());
        trackTypeViewHolder.checkBoxFilterState.setChecked(trackTypeObjects[i].isTrackTypeFilter());

        


    }

    @Override
    public int getItemCount() {
        return 3;
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
