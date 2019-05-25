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
import com.ebridge.tevoi.Utils.MyStorage;
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
    private SideMenu activity;
    private boolean HasPlayNextBtn;
    private boolean HasRemoveBtn;
    private boolean HasAddToListBtn = true;
    private boolean HasReadTextBtn = true;
    private boolean HasFavouriteBtn = true;

    private String fragmentName = "";

    public TracksAdapter(List<TrackObject> tracks, SideMenu activity, String fragmentName) {
        this.tracks = tracks;
        this.activity = activity;
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
            case Global.UserListTracksFragment:
            case Global.FavouriteFragmentName: {
                row =LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.track_row_instance_all,viewGroup,false);
                break;
            }
            case Global.PlayNowFragmentName: {
                row =LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.track_row_instance_without_play_next,viewGroup,false);
                break;
            }
            case Global.PartnerNameFragment:
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
        viewHolder.ratingBar.setRating(track.getRate());
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
            btnAddPlayNext = itemView.findViewById(R.id.btn_add_play_next);
            btnAddToList = itemView.findViewById(R.id.btn_add_to_list);
            btnLike = itemView.findViewById(R.id.imgBtnLike);
            btnReadText = itemView.findViewById(R.id.btn_read_text);
            btnRemove = itemView.findViewById(R.id.btn_remove);

            //--------//


            imgBtnPlay.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int i = getAdapterPosition();
                    TrackObject selectedTrack =  tracks.get(i);
                    activity.CurrentTrackInPlayer = tracks.get(i);
                    if(fragmentName.equals(Global.ListTracksFragmentName))
                    {
                        activity.playAudio(Global.GetStreamURL +activity.CurrentTrackInPlayer.getId());
                        activity.txtTrackName.setText(selectedTrack.getName().toString());

                        if(activity.mainPlayerLayout.getVisibility()==View.INVISIBLE)
                        {
                            activity.mainPlayerLayout.setVisibility(View.VISIBLE);
                        }
                        activity.lisTracksFragment.fm.executePendingTransactions();
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Linearlayout is the layout the fragments are being added to.
                                View view = activity.lisTracksFragment.linearLayoutListTracks.getChildAt(activity.lisTracksFragment.linearLayoutListTracks.getChildCount()-1);

                                activity.lisTracksFragment.scrollViewListTracks.smoothScrollBy(0,(int)(view.getY() + view.getHeight()));
                            }
                        });
                    }
                    else
                    {
                        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                        activity.mediaPlayerFragment.currentTrackId = selectedTrack.getId();
                        activity.mediaPlayerFragment.currentTrack = selectedTrack;
                        activity.CurrentTrackInPlayer = selectedTrack;
                        ft.replace(R.id.content_frame, activity.mediaPlayerFragment);
                        ft.addToBackStack( "mediaPlayerFragment" );
                        ft.commit();
                        activity.playAudio(Global.GetStreamURL +activity.CurrentTrackInPlayer.getId());
                    }
                    // if we are playing new track
                    /*if(activity.player != null && i != activity.trackIdPlayedNow)
                    {
                        // reset the media player
                        activity.player.resetMediaPlayer();
                        activity.isPaused =false; activity.isPlaying = false;
                        activity.serviceBound = false;
                    }*/
                    /*FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                    // Replace the contents of the container with the new fragment
                    //TrackAddToList frag = new TrackAddToList();
                    activity.mediaPlayerFragment.currentTrackId = selectedTrack.getId();
                    activity.mediaPlayerFragment.currentTrack = selectedTrack;
                    activity.CurrentTrackInPlayer = selectedTrack;

                    ft.replace(R.id.content_frame, activity.mediaPlayerFragment);
                    // or ft.add(R.id.your_placeholder, new FooFragment());
                    // Complete the changes added above
                    ft.commit();*/
                }
            });

            imgBtnDrawer.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    final Context context = v.getContext();
                    int i = getAdapterPosition();

                    if(tracks.get(i).isFaourite())
                    {
                        btnLike.setText("Dislike");
                        btnLike.refreshDrawableState();
                    }
                    else
                    {
                        btnLike.setText("Like");
                        btnLike.refreshDrawableState();
                    }
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
                    int i = getPosition();
                    TrackObject selectedTrack = tracks.get(i);
                    if(selectedTrack.isHasText())
                    {
                        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                        TrackText textFargment = TrackText.newInstance(selectedTrack.getId(), fragmentName);
                        ft.replace(R.id.content_frame, textFargment);
                        ft.addToBackStack( "TrackText" );
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

            if(btnRemove != null)
            {
                btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = getAdapterPosition();
                        final TrackObject selectedTrack = tracks.get(i);
                        switch (fragmentName)
                        {
                            case Global.HistoryFragmentName:
                                {
                                Toast.makeText(activity, "HistoryFragmentName", Toast.LENGTH_SHORT).show();
                                Call<IResponse> call = Global.client.RemoveFromHistory(selectedTrack.getActivityId());
                                call.enqueue(new Callback<IResponse>() {
                                    @Override
                                    public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                                        IResponse res = response.body();
                                        if(res.getNumber()==0)
                                        {
                                            tracks.remove(selectedTrack);
                                            activity.notifyHistoryListAdapter();
                                            Toast.makeText(activity,"Remove From History successfully",Toast.LENGTH_LONG).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(activity,"Error Remove From History",Toast.LENGTH_LONG).show();
                                        }

                                    }
                                    @Override
                                    public void onFailure(Call<IResponse> call, Throwable t) {

                                    }
                                });
                                break;
                            }
                            case Global.FavouriteFragmentName: {
                                //Toast.makeText(context, "FavouriteFragmentName", Toast.LENGTH_SHORT).show();
                                Call<IResponse> call = Global.client.RemoveTrackFromFavourite(selectedTrack.getActivityId());
                                call.enqueue(new Callback<IResponse>() {
                                    @Override
                                    public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                                        IResponse res = response.body();
                                        if(res.getNumber()==0)
                                        {
                                            tracks.remove(selectedTrack);
                                            activity.notifyFavouriteListAdapter();
                                            Toast.makeText(activity,"Remove From Favourite successfully",Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(activity,"Error Remove From Favourite",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<IResponse> call, Throwable t) {

                                    }
                                });
                                break;
                            }
                            case Global.PlayNowFragmentName:
                                {
                                Toast.makeText(activity, "PlayNowFragmentName remove", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            case Global.UserListTracksFragment:
                            {
                                activity.mProgressDialog.setMessage("Loading");
                                activity.mProgressDialog.show();

                                Call<IResponse> call = Global.client.DeleteTrackFromUserList(activity.userListTracksFragment.currenUsertListId, selectedTrack.getId());
                                call.enqueue(new Callback<IResponse>() {
                                    @Override
                                    public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                                        IResponse res = response.body();
                                        if(res.getNumber()==0)
                                        {
                                            tracks.remove(selectedTrack);
                                            activity.notifyUserListTracksAdapter();
                                            activity.mProgressDialog.dismiss();
                                            Toast.makeText(activity,res.getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                        else
                                        {
                                            activity.mProgressDialog.dismiss();
                                            Toast.makeText(activity,res.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<IResponse> call, Throwable t) {

                                    }
                                });
                                break;
                            }
                        }
                        //hoverLayout.setVisibility(View.INVISIBLE);
                    }
                });
            }
            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Like or dislike this track.
                    int i = getPosition();
                   final TrackObject t = tracks.get(i);
                    if(!t.isFaourite())
                    {
                        Call<IResponse> call = Global.client.AddTrackToFavourite(t.getId());
                        call.enqueue(new Callback<IResponse>() {
                            @Override
                            public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                                IResponse res = response.body();
                                if (res.getNumber() == 0)
                                {
                                    t.setFaourite(true);
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
                        Call<IResponse> call = Global.client.RemoveTrackFromFavourite(t.getActivityId());
                        call.enqueue(new Callback<IResponse>() {
                            @Override
                            public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                                IResponse res = response.body();
                                if (res.getNumber() == 0)
                                {
                                    t.setFaourite(false);
                                    btnLike.setText("Like");
                                    btnLike.refreshDrawableState();

                                    Log.d("Favourite :", "onResponse: track liked ");
                                    //Toast.makeText(activity, "Remove Like", Toast.LENGTH_LONG).show();
                                } else {
                                    Log.d("Favourite Error", "onResponse: " + res.getMessage());
                                    //Toast.makeText(activity, "Error Remove Like", Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<IResponse> call, Throwable t) {

                            }
                        });
                    }
                    if(fragmentName.equals(Global.FavouriteFragmentName))
                    {
                        activity.notifyFavouriteListAdapter();
                    }
                    else if(fragmentName.equals(Global.HistoryFragmentName))
                    {
                        activity.notifyHistoryListAdapter();
                    }
                    else if(fragmentName.equals(Global.ListTracksFragmentName))
                    {
                        activity.notifyTarcksListAdapter();
                    }
                    //hoverLayout.setVisibility(View.INVISIBLE);
                }
            });
            btnAddToList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = getPosition();
                    final TrackObject trackSelected =  tracks.get(i);

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
                            Toast.makeText(activity, "You have to lists", Toast.LENGTH_LONG).show();
                        }
                    });
                  /*  // here we need to add track to play next list
                    int Id = activity.mediaPlayerFragment.currentTrack.getId();
                    String Name = activity.mediaPlayerFragment.currentTrack.getName();
                    TrackSerializableObject track = new TrackSerializableObject();
                    track.setId(Id);
                    track.setName(Name);
                    track.setDuration("03:20");
                    track.setAuthor("Authors");
                    track.setCategories("Categories");
                    track.setRate(2);

                    Toast.makeText(activity, "Hi add to list", Toast.LENGTH_SHORT).show();*/

                }
            });
            if(btnAddPlayNext!= null) {
                btnAddPlayNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = getAdapterPosition();
                        TrackObject selectedTrack = tracks.get(i);
                        TrackSerializableObject track = new TrackSerializableObject();
                        track.setId(selectedTrack.getId());
                        track.setName(selectedTrack.getName());
                        track.setDuration(selectedTrack.getDuration());
                        track.setAuthor(selectedTrack.getAuthors());
                        track.setCategories(selectedTrack.getCategories());
                        track.setRate((int) selectedTrack.getRate());
                        track.setFavourite(selectedTrack.isFaourite());
                        track.setActivityId(selectedTrack.getActivityId());
                        track.setHasLocation(selectedTrack.isHasLocation());
                        track.setHasText(selectedTrack.isHasText());
                        track.setPartnerId(selectedTrack.getPartnerId());
                        track.setPartnerName(selectedTrack.getPartnerName());
                        track.setPartnerLogo(selectedTrack.getPartnerLogo());
                        String result = activity.storageManager.addTrack(activity, track);
                        activity.playNowListTracks = activity.storageManager.loadPlayNowTracks(activity);
                        Toast.makeText(activity, result, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }


    }


}
