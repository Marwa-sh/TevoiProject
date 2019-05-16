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
import com.ebridge.tevoi.Utils.Global;
import com.ebridge.tevoi.model.AddCommentResponse;
import com.ebridge.tevoi.model.AddCommetRequest;
import com.ebridge.tevoi.model.AddTrackToFavouriteResponse;
import com.ebridge.tevoi.model.IResponse;
import com.ebridge.tevoi.model.TrackObject;
import com.ebridge.tevoi.model.TrackResponse;
import com.ebridge.tevoi.model.TrackSerializableObject;
import com.ebridge.tevoi.model.TrackTextResponse;
import com.ebridge.tevoi.model.UserListObject;
import com.ebridge.tevoi.model.UserListResponse;
import com.ebridge.tevoi.rest.ApiClient;
import com.ebridge.tevoi.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TracksAdapter extends RecyclerView.Adapter<TracksAdapter.TrackViewHolder>
{
    private List<TrackObject> tracks;
    private Context context;
    private boolean HasPlayNextBtn;
    private boolean HasRemoveBtn;
    private boolean HasAddToListBtn = true;
    private boolean HasReadTextBtn = true;
    private boolean HasFavouriteBtn = true;

    private String fragmentName = "";

    public TracksAdapter(List<TrackObject> tracks, Context context, String fragmentName) {
        this.tracks = tracks;
        this.context = context;
        this.fragmentName = fragmentName;
    }

    public void setHasPlayNextBtn(boolean hasPlayNextBtn) {
        HasPlayNextBtn = hasPlayNextBtn;
    }

    public void setHasRemoveBtn(boolean hasRemoveBtn) {
        HasRemoveBtn = hasRemoveBtn;
    }

    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View row;
        switch (fragmentName)
        {
            case Global.HistoryFragmentName: {
                row =LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.track_row_instance_all,viewGroup,false);
                break;
            }
            case Global.FavouriteFragmentName: {
                row =LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.track_row_instance_all,viewGroup,false);
                break;
            }
            case Global.PlayNowFragmentName: {
                row =LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.track_row_instance_without_play_next,viewGroup,false);
                break;
            }
            case Global.ListTracksFragmentName: {
                row =LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.track_row_instance_without_remove,viewGroup,false);
                break;
            }
            default:
            {
                row =LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.track_row_instance_all,viewGroup,false);
                break;
            }
        }
        TrackViewHolder holder = new TrackViewHolder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder viewHolder, int i) {
        TrackObject track =  tracks.get(i);
        viewHolder.tvAuthor.setText(track.getAuthors());
        viewHolder.tvTrackName.setText(track.getName());
        viewHolder.ratingBar.setRating((float)track.getRate());
        viewHolder.tvCategories.setText(track.getCategories());
        viewHolder.tvDuration.setText(track.getDuration());
/*
        viewHolder.btnLike.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //notice I implemented onClickListener here
                // so I can associate this click with final Item item
                Toast.makeText(context, "Hi maroosh", Toast.LENGTH_SHORT).show();
            }

        });*/
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
        int id;
        LinearLayout hoverLayout;
        LinearLayout trackDetailsLayout;
        Button btnLike, btnAddToList, btnAddPlayNext,btnReadText, btnRemove;
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
            //imgBtnPlay.setOnClickListener();
            hoverLayout = itemView.findViewById(R.id.hoverButtonsLayout);
            trackDetailsLayout = itemView.findViewById(R.id.layout_track_details);

            //Hover Buttons
            btnAddPlayNext = (Button)itemView.findViewById(R.id.btn_add_play_next);
            btnAddToList = (Button)itemView.findViewById(R.id.btn_add_to_list);
            btnLike = (Button) itemView.findViewById(R.id.imgBtnLike);
            btnReadText = (Button)itemView.findViewById(R.id.btn_read_text);
            btnRemove = (Button)itemView.findViewById(R.id.btn_remove);

            /*switch (fragmentName) {
                case Global.HistoryFragmentName: {
                    btnRemove.setVisibility(View.VISIBLE);
                    btnAddPlayNext.setVisibility(View.VISIBLE);
                    break;
                }
                case Global.FavouriteFragmentName: {
                    btnRemove.setVisibility(View.VISIBLE);
                    btnAddPlayNext.setVisibility(View.VISIBLE);
                    break;
                }
                case Global.PlayNowFragmentName: {
                    btnRemove.setVisibility(View.VISIBLE);
                    btnAddPlayNext.setVisibility(View.INVISIBLE);
                    break;
                }
                case Global.ListTracksFragmentName: {
                    btnRemove.setVisibility(View.INVISIBLE);
                    btnAddPlayNext.setVisibility(View.VISIBLE);
                    break;
                }
                default:
                {
                    btnRemove.setVisibility(View.INVISIBLE);
                    btnAddPlayNext.setVisibility(View.VISIBLE);
                }
            }
*/

            //--------//

            imgBtnPlay.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int i = getAdapterPosition();
                    TrackObject selectedTrack =  tracks.get(i);
                    SideMenu activity = (SideMenu) context;
                    // if we are playing new track
                    if(activity.player != null && i != activity.trackIdPlayedNow)
                    {
                        // reset the media player
                        activity.player.resetMediaPlayer();
                        activity.isPaused =false; activity.isPlaying = false;
                        activity.serviceBound = false;
                    }
                    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                    // Replace the contents of the container with the new fragment
                    //TrackAddToList frag = new TrackAddToList();
                    activity.mediaPlayerFragment.currentTrackId = selectedTrack.getId();
                    activity.mediaPlayerFragment.currentTrack = selectedTrack;

                    ft.replace(R.id.content_frame, activity.mediaPlayerFragment);
                    // or ft.add(R.id.your_placeholder, new FooFragment());
                    // Complete the changes added above
                    ft.commit();
                }
            });

            imgBtnDrawer.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    final Context context = v.getContext();


                    if(hoverLayout.getVisibility()==View.VISIBLE)
                    {
                        hoverLayout.setVisibility(View.INVISIBLE);
                        //imgBtnPlay.setVisibility(View.VISIBLE);
                        //trackDetailsLayout.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        hoverLayout.setVisibility(View.VISIBLE);
                        //imgBtnPlay.setVisibility(View.INVISIBLE);
                        //trackDetailsLayout.setVisibility(View.INVISIBLE);
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

            if(btnRemove != null) {
                btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (fragmentName) {
                            case Global.HistoryFragmentName: {
                                Toast.makeText(context, "HistoryFragmentName", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            case Global.FavouriteFragmentName: {
                                Toast.makeText(context, "FavouriteFragmentName", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            case Global.PlayNowFragmentName: {
                                Toast.makeText(context, "PlayNowFragmentName", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                        //Show text fragment.
                        Toast.makeText(context, "btnRemove", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Like or dislike this track.
                    int i = getPosition();
                    TrackObject t = tracks.get(i);
                    ApiInterface client = ApiClient.getClient().create(ApiInterface.class);
                    Call<AddTrackToFavouriteResponse> call = client.AddTrackToFavourite(t.getId());
                    call.enqueue(new Callback<AddTrackToFavouriteResponse>() {
                        @Override
                        public void onResponse(Call<AddTrackToFavouriteResponse> call, Response<AddTrackToFavouriteResponse> response) {
                            AddTrackToFavouriteResponse res = response.body();
                            if(res.getNumber()==0)
                            {
                                Log.d("Favourite :", "onResponse: track liked ");
                                Toast.makeText(context,"Like",Toast.LENGTH_SHORT);
                            }
                            else
                            {
                                Log.d("Favourite Error", "onResponse: "+res.getMessage());
                                Toast.makeText(context,"Like",Toast.LENGTH_SHORT);
                            }
                        }

                        @Override
                        public void onFailure(Call<AddTrackToFavouriteResponse> call, Throwable t) {

                        }
                    });

                }
            });

            btnAddToList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Call<UserListResponse> call = Global.client.getUserLists(0,0);
                    call.enqueue(new Callback<UserListResponse>(){
                        public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response)
                        {
                            UserListResponse listsNames = response.body();
                            LayoutInflater li = LayoutInflater.from(context);
                            View promptsView = li.inflate(R.layout.layout_user_lists_spinner, null);

                            Spinner spinner = (Spinner) promptsView.findViewById(R.id.user_lists_spinner);
                            // TODO:  get user lists
                            // Create an ArrayAdapter using the string array and a default spinner layout
                            ArrayAdapter adapter = new ArrayAdapter(context, R.layout.spinner, listsNames.getLstUserList());

                            // Specify the layout to use when the list of choices appears
                            //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            // Apply the adapter to the spinner
                            spinner.setAdapter(adapter);

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                            // set prompts.xml to alertdialog builder
                            alertDialogBuilder.setView(promptsView);

                            // set dialog message
                            alertDialogBuilder
                                    .setCancelable(false)
                                    .setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int id) {

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
                        public void onFailure(Call<UserListResponse> call, Throwable t)
                        {
                            Toast.makeText(context, "You have to lists", Toast.LENGTH_LONG).show();
                        }
                    });
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

                    Toast.makeText(activity, "Hi add to list", Toast.LENGTH_SHORT).show();

                }
            });
            if(btnAddPlayNext!= null) {
                btnAddPlayNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = getAdapterPosition();
                        TrackObject selectedTrack = tracks.get(i);
                        SideMenu activity = (SideMenu) context;
                        TrackSerializableObject track = new TrackSerializableObject();
                        track.setId(selectedTrack.getId());
                        track.setName(selectedTrack.getName());
                        track.setDuration(selectedTrack.getDuration());
                        track.setAuthor(selectedTrack.getAuthors());
                        track.setCategories(selectedTrack.getCategories());
                        track.setRate((int) selectedTrack.getRate());
                        String result = activity.storageManager.addTrack(context, track);
                        activity.playNowListTracks = activity.storageManager.loadPlayNowTracks(context);
                        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }


    }


}
