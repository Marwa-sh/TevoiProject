package com.ebridge.tevoi.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ebridge.tevoi.MediaPlayerActivity;
import com.ebridge.tevoi.R;
import com.ebridge.tevoi.SideMenu;
import com.ebridge.tevoi.TrackText;
import com.ebridge.tevoi.Utils.Global;
import com.ebridge.tevoi.Utils.HelperFunctions;
import com.ebridge.tevoi.model.AddCommentResponse;
import com.ebridge.tevoi.model.AddCommetRequest;
import com.ebridge.tevoi.model.AddTrackToFavouriteResponse;
import com.ebridge.tevoi.model.IResponse;
import com.ebridge.tevoi.model.TrackObject;
import com.ebridge.tevoi.model.TrackResponse;
import com.ebridge.tevoi.model.TrackSerializableObject;
import com.ebridge.tevoi.model.UserListObject;
import com.ebridge.tevoi.model.UserListResponse;
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
    private SideMenu activity;

    public TracksSerializableAdapter(List<TrackSerializableObject> tracks, SideMenu activity) {
        this.tracks = tracks;
        this.activity = activity;
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

            btnAddToList = itemView.findViewById(R.id.btn_add_to_list);
            btnLike = itemView.findViewById(R.id.imgBtnLike);
            btnReadText = itemView.findViewById(R.id.btn_read_text);
            btnRemove = itemView.findViewById(R.id.btn_remove);

            btnRemove.setVisibility(View.VISIBLE);

            //imgBtnPlay.setOnClickListener();
            hoverLayout = itemView.findViewById(R.id.hoverButtonsLayout);
            trackDetailsLayout = itemView.findViewById(R.id.layout_track_details);


            imgBtnPlay.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                    int i  = getAdapterPosition();
                    TrackSerializableObject t = activity.playNowListTracks.get(i);
                    TrackObject track = HelperFunctions.CastTrackSerialize(t);

                    activity.mediaPlayerFragment.currentTrackId = track.getId();
                    activity.mediaPlayerFragment.currentTrack = track;
                    activity.CurrentTrackInPlayer = track;
                    ft.replace(R.id.content_frame, activity.mediaPlayerFragment);
                    ft.addToBackStack( "mediaPlayerFragment" );
                    ft.commit();
                    activity.playAudio(Global.GetStreamURL +activity.CurrentTrackInPlayer.getId());
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
                    int i = getAdapterPosition();
                    TrackSerializableObject selectedTrack = tracks.get(i);
                    if(selectedTrack.isHasText())
                    {
                        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                        TrackText textFargment = TrackText.newInstance(selectedTrack.getId(), Global.PlayNextListFragment);
                        ft.replace(R.id.content_frame, textFargment);
                        // or ft.add(R.id.your_placeholder, new FooFragment());
                        // Complete the changes added above
                        ft.commit();
                    }
                    else
                    {
                        Toast.makeText(activity, R.string.track_has_no_text, Toast.LENGTH_LONG).show();
                    }
                }
            });

            btnRemove.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int i = getAdapterPosition();
                    TrackSerializableObject selectedTrack =  tracks.get(i);
                    activity.storageManager.removeTrack(activity, selectedTrack);
                    activity.playNowListTracks = activity.storageManager.loadPlayNowTracks(activity);
                    tracks.remove(selectedTrack);
                    activity.notifyPlayNextListAdapter();
                    Toast.makeText(activity, "Track Removed from list successfully", Toast.LENGTH_SHORT).show();
                }
            });

            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    //Like or dislike this track.
                    int i = getAdapterPosition();
                    final TrackSerializableObject t = tracks.get(i);
                    if(!t.isFavourite())
                    {
                        Call<IResponse> call = Global.client.AddTrackToFavourite(t.getId());
                        call.enqueue(new Callback<IResponse>() {
                            @Override
                            public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                                IResponse res = response.body();
                                if (res.getNumber() == 0)
                                {
                                    t.setFavourite(true);
                                    Log.d("Favourite :", "onResponse: track liked ");
                                    Toast.makeText(activity, "Like", Toast.LENGTH_LONG).show();
                                } else {
                                    Log.d("Favourite Error", "onResponse: " + res.getMessage());
                                    Toast.makeText(activity, "Error Like", Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<IResponse> call, Throwable t) {

                            }
                        });
                    }
                    else
                    {
                        // unfavourite
                        Call<IResponse> call = Global.client.RemoveTrackFromFavourite(t.getActivityId());
                        call.enqueue(new Callback<IResponse>() {
                            @Override
                            public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                                IResponse res = response.body();
                                if (res.getNumber() == 0)
                                {
                                    t.setFavourite(false);
                                    Log.d("Favourite :", "onResponse: track liked ");
                                    Toast.makeText(activity, "Remove Like", Toast.LENGTH_LONG).show();
                                } else {
                                    Log.d("Favourite Error", "onResponse: " + res.getMessage());
                                    Toast.makeText(activity, "Error Remove Like", Toast.LENGTH_LONG).show();
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

                    int i = getAdapterPosition();
                    final TrackSerializableObject trackSelected = tracks.get(i);
                    Call<UserListResponse> call = Global.client.getUserLists(0,0);
                    call.enqueue(new Callback<UserListResponse>(){
                        public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response)
                        {
                            UserListResponse listsNames = response.body();
                            LayoutInflater li = LayoutInflater.from(activity);
                            final View promptsView = li.inflate(R.layout.layout_user_lists_spinner, null);

                            Spinner spinner = promptsView.findViewById(R.id.user_lists_spinner);
                            // TODO:  get user lists
                            // Create an ArrayAdapter using the string array and a default spinner layout
                            ArrayAdapter adapter = new ArrayAdapter(activity, R.layout.spinner, listsNames.getLstUserList());

                            // Specify the layout to use when the list of choices appears
                            //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            // Apply the adapter to the spinner
                            spinner.setAdapter(adapter);

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

                            // set prompts.xml to alertdialog builder
                            alertDialogBuilder.setView(promptsView);

                            // set dialog message
                            alertDialogBuilder
                                    .setCancelable(false)
                                    .setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int id) {
                                                    UserListObject selectedList = (UserListObject) ( ((Spinner) promptsView.findViewById(R.id.user_lists_spinner) ).getSelectedItem());
                                                    //Toast.makeText(context, selectedList.getName(), Toast.LENGTH_SHORT).show();

                                                    Call<IResponse> call = Global.client.AddTrackToUserList(trackSelected.getId(), selectedList.getId());
                                                    call.enqueue(new Callback<IResponse>(){
                                                        public void onResponse(Call<IResponse> call, Response<IResponse> response)
                                                        {
                                                            IResponse result = response.body();
                                                            Toast.makeText(activity, result.getMessage(), Toast.LENGTH_SHORT).show();

                                                        }
                                                        public void onFailure(Call<IResponse> call, Throwable t)
                                                        {
                                                            Toast.makeText(activity, "You have to lists", Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                                }
                                            })
                                    .setNegativeButton("Cancel",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int id) {
                                                    Toast.makeText(activity, "No Select", Toast.LENGTH_SHORT).show();
                                                    dialog.cancel();
                                                }
                                            });

                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            // show it
                            alertDialog.show();
                        }
                        public void onFailure(Call<UserListResponse> call, Throwable t)
                        {
                            Toast.makeText(activity, "You have no lists", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            });
        }
    }


}
