package com.tevoi.tevoi.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tevoi.tevoi.R;
import com.tevoi.tevoi.SideMenu;
import com.tevoi.tevoi.TrackText;
import com.tevoi.tevoi.Utils.FileHelper;
import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.Utils.HelperFunctions;
import com.tevoi.tevoi.model.IResponse;
import com.tevoi.tevoi.model.TrackObject;
import com.tevoi.tevoi.model.TrackSerializableObject;
import com.tevoi.tevoi.model.UserListObject;
import com.tevoi.tevoi.model.UserListResponse;

import java.io.File;
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
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
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
        public TrackViewHolder(@NonNull final View itemView)
        {
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
                    activity.CurrentFragmentName = Global.MediaPlayerFragmentName;

                    ft.replace(R.id.content_frame, activity.mediaPlayerFragment);
                    ft.addToBackStack( "mediaPlayerFragment" );
                    ft.commit();

                    String pathTrack = activity.getFilesDir().getAbsolutePath() + Global.PlayNowListDirectory + File.separator + track.getId() + ".mp3";
                    //HelperFunctions.deleteDirectory(activity.getFilesDir().getAbsolutePath() + Global.PlayNowListDirectory);
                    boolean isFound = FileHelper.isFileExist(pathTrack);
                    if(isFound)
                    {
                        Log.d("Marwa", "deleted = " + pathTrack);
                    }

                    //HelperFunctions.readFromDisk(activity);
                    String audioPath = activity.getFilesDir().getAbsolutePath() + Global.PlayNowListDirectory + File.separator + activity.CurrentTrackInPlayer.getId() + ".mp3";
                    activity.playAudio(audioPath,
                            activity.CurrentTrackInPlayer.getName(),
                            activity.CurrentTrackInPlayer.getAuthors(),
                            activity.CurrentTrackInPlayer.getId());
                }
            });

            imgBtnDrawer.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    int i = getAdapterPosition();

                    if(tracks.get(i).isFavourite())
                    {
                        btnLike.setText("Dislike");
                        btnLike.refreshDrawableState();
                    }
                    else
                    {
                        btnLike.setText("Like");
                        btnLike.refreshDrawableState();
                    }
                    refreshHoverLayoutVisibility();
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
                    refreshHoverLayoutVisibility();
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
                    String pathTrack =  activity.getFilesDir().getAbsolutePath() + Global.PlayNowListDirectory + File.separator + selectedTrack.getId() + ".mp3";
                    FileHelper.deleteFile(pathTrack);
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
                                    btnLike.setText("Dislike");
                                    btnLike.refreshDrawableState();

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
                        Call<IResponse> call = Global.client.RemoveTrackFromFavourite(t.getId());
                        call.enqueue(new Callback<IResponse>() {
                            @Override
                            public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                                IResponse res = response.body();
                                if (res.getNumber() == 0)
                                {
                                    t.setFavourite(false);
                                    btnLike.setText("Like");
                                    btnLike.refreshDrawableState();

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

        public void refreshHoverLayoutVisibility()
        {
            if(hoverLayout.getVisibility()== View.VISIBLE)
            {
                hoverLayout.setVisibility(View.INVISIBLE);
            }
            else
            {
                hoverLayout.setVisibility(View.VISIBLE);
            }
        }

        public void closeOtherDrawers(int i)
        {

        }

    }


}
