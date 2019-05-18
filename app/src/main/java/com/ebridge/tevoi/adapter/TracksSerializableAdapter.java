package com.ebridge.tevoi.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ebridge.tevoi.MediaPlayerActivity;
import com.ebridge.tevoi.R;
import com.ebridge.tevoi.SideMenu;
import com.ebridge.tevoi.Utils.Global;
import com.ebridge.tevoi.Utils.HelperFunctions;
import com.ebridge.tevoi.model.AddCommentResponse;
import com.ebridge.tevoi.model.AddCommetRequest;
import com.ebridge.tevoi.model.AddTrackToFavouriteResponse;
import com.ebridge.tevoi.model.IResponse;
import com.ebridge.tevoi.model.TrackObject;
import com.ebridge.tevoi.model.TrackResponse;
import com.ebridge.tevoi.model.TrackSerializableObject;
import com.ebridge.tevoi.rest.ApiClient;
import com.ebridge.tevoi.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TracksSerializableAdapter extends RecyclerView.Adapter<TracksSerializableAdapter.TrackViewHolder>
{
    private List<TrackSerializableObject> tracks;
    private Context context;

    public TracksSerializableAdapter(List<TrackSerializableObject> tracks, Context context) {
        this.tracks = tracks;
        this.context = context;
    }
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.track_row_instance_without_play_next,viewGroup,false);
        TrackViewHolder holder = new TrackViewHolder(row);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder viewHolder, int i) {
        TrackSerializableObject track =  tracks.get(i);
        viewHolder.tvAuthor.setText(track.getAuthor());
        viewHolder.tvTrackName.setText(track.getName());
        viewHolder.ratingBar.setRating((float)track.getRate());
        viewHolder.tvCategories.setText(track.getCategories());
        viewHolder.tvDuration.setText(track.getDuration());

    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }
    class TrackViewHolder extends RecyclerView.ViewHolder
    {
        public  View view;
        TextView tvTrackName;
        TextView tvDuration;
        TextView tvAuthor;
        TextView tvCategories;
        RatingBar ratingBar;
        ImageButton imgBtnPlay;
        ImageButton imgBtnDrawer;
        Button btnLike, btnAddToList,btnReadText, btnRemove;
        int id;
        LinearLayout hoverLayout;
        LinearLayout trackDetailsLayout;
        boolean hoverVisisble = false;
        public TrackViewHolder(@NonNull final View itemView) {
            super(itemView);
            //this.view=itemView.findViewWithTag(R.id.track_row_layout);
            tvTrackName = itemView.findViewById(R.id.tv_track_name);

            tvDuration  = itemView.findViewById(R.id.tv_duration);
            tvAuthor = itemView.findViewById(R.id.tv_author);
            tvCategories = itemView.findViewById(R.id.tv_categories);
            ratingBar = itemView.findViewById(R.id.rbTrack);
            imgBtnPlay = itemView.findViewById(R.id.btn_play_pause);
            imgBtnDrawer= itemView.findViewById(R.id.btn_track_drawer);

            btnAddToList = (Button)itemView.findViewById(R.id.btn_add_to_list);
            btnLike = (Button) itemView.findViewById(R.id.imgBtnLike);
            btnReadText = (Button)itemView.findViewById(R.id.btn_read_text);
            btnRemove = (Button)itemView.findViewById(R.id.btn_remove);

            btnRemove.setVisibility(View.VISIBLE);

            //imgBtnPlay.setOnClickListener();
            hoverLayout = itemView.findViewById(R.id.hoverButtonsLayout);
            trackDetailsLayout = itemView.findViewById(R.id.layout_track_details);


            imgBtnPlay.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    SideMenu activity = (SideMenu) context;
                    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                    int i  = getAdapterPosition();
                    TrackSerializableObject t = activity.playNowListTracks.get(i);
                    TrackObject track = HelperFunctions.CastTrackSerialize(t);
                    activity.mediaPlayerFragment.currentTrack = track;
                    // Replace the contents of the container with the new fragment
                    //TrackAddToList frag = new TrackAddToList();
                    ft.replace(R.id.content_frame, activity.mediaPlayerFragment);
                    // or ft.add(R.id.your_placeholder, new FooFragment());
                    // Complete the changes added above
                    ft.commit();
                }
            });

            imgBtnDrawer.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    if(hoverLayout.getVisibility()==View.VISIBLE)
                    {
                        hoverLayout.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        hoverLayout.setVisibility(View.VISIBLE);
                    }
                }
            });

            btnReadText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Show text fragment.
                    Toast.makeText(context, "Hiiiiiiii", Toast.LENGTH_SHORT).show();
                }
            });
            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Like or dislike this track.
                    int i = getPosition();
                    TrackSerializableObject t = tracks.get(i);
                    if(!t.isFavourite()) {
                        Call<IResponse> call = Global.client.AddTrackToFavourite(t.getId());
                        call.enqueue(new Callback<IResponse>() {
                            @Override
                            public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                                IResponse res = response.body();
                                if (res.getNumber() == 0) {
                                    Log.d("Favourite :", "onResponse: track liked ");
                                    Toast.makeText(context, "Like", Toast.LENGTH_SHORT);
                                } else {
                                    Log.d("Favourite Error", "onResponse: " + res.getMessage());
                                    Toast.makeText(context, "Like", Toast.LENGTH_SHORT);
                                }
                            }
                            @Override
                            public void onFailure(Call<IResponse> call, Throwable t) {

                            }
                        });

                    }
                }
            });

            btnAddToList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //add to sharedpreference list.
                    // here we need to add track to play next list
                    SideMenu activity = (SideMenu) context;
                    int Id = activity.mediaPlayerFragment.currentTrack.getId();
                    String Name = activity.mediaPlayerFragment.currentTrack.getName();

                    TrackSerializableObject track = new TrackSerializableObject();
                    track.setId(Id);
                    track.setName(Name);
                    track.setDuration("03:20");
                    track.setAuthor("Authors");
                    track.setCategories("Categories");
                    track.setRate(2);
                    Toast.makeText(activity, "Added", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }


}
