package com.tevoi.tevoi.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tevoi.tevoi.R;
import com.tevoi.tevoi.SideMenu;
import com.tevoi.tevoi.UserListTracksFragment;
import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.model.IResponse;
import com.tevoi.tevoi.model.UserListObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder>
{
    private List<UserListObject> userLists;
    private SideMenu activity;

    public UserListAdapter(List<UserListObject> userLists, SideMenu activity) {
        this.userLists = userLists;
        this.activity = activity;
    }
    public UserListAdapter.UserListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_user_list_instance,viewGroup,false);
        UserListAdapter.UserListViewHolder holder = new UserListAdapter.UserListViewHolder(row);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.UserListViewHolder viewHolder, int i) {
        UserListObject userList =  userLists.get(i);
        viewHolder.tvUserListName.setText(userList.getName());
        viewHolder.tvCreationDate.setText(userList.getCreationDate());
        viewHolder.tvUserListDuration.setText(userList.getDuration());

    }

    @Override
    public int getItemCount() {
        return userLists.size();
    }
    class UserListViewHolder extends RecyclerView.ViewHolder
    {
        public  View view;
        TextView tvUserListName;
        TextView tvUserListDuration;
        TextView tvCreationDate;
        ImageButton btnPlay;
        ImageButton btnDrawer;
        Button btnRename;
        Button btnRemove;
        LinearLayout hoverLayout;
        LinearLayout userListDetailsLayout;

        int id;
        public UserListViewHolder(@NonNull View itemView) {
            super(itemView);
            //this.view=itemView.findViewWithTag(R.id.track_row_layout);
            tvUserListName = itemView.findViewById(R.id.tv_user_list_name);
            tvUserListDuration  = itemView.findViewById(R.id.tv_duration_user_list);
            tvCreationDate = itemView.findViewById(R.id.tv_creation_date);
            hoverLayout = itemView.findViewById(R.id.hoverButtonsLayoutUserList);
            userListDetailsLayout = itemView.findViewById(R.id.layout_user_list_details);

            btnPlay = itemView.findViewById(R.id.btn_play_pause_user_list);
            btnDrawer= itemView.findViewById(R.id.btn_user_list_drawer);

            btnRename = itemView.findViewById(R.id.btn_rename_user_list);
            btnRemove= itemView.findViewById(R.id.btn_delete_user_list);

            btnPlay.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int i = getAdapterPosition();
                    UserListObject selectedList =  userLists.get(i);
                    // TODO: we need to add tracks related to this list to the current list played

                   /* FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                    // Replace the contents of the container with the new fragment
                    //TrackAddToList frag = new TrackAddToList();
                    activity.mediaPlayerFragment.currentTrackId = selectedTrack.getId();
                    activity.mediaPlayerFragment.currentTrack = selectedTrack;

                    ft.replace(R.id.content_frame, activity.mediaPlayerFragment);
                    // or ft.add(R.id.your_placeholder, new FooFragment());
                    // Complete the changes added above
                    ft.commit();
                */
                }
            });

            btnDrawer.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    final Context context = v.getContext();
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

            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int i = getAdapterPosition();

                    Call<IResponse> call = Global.client.DeleteUserList(userLists.get(i).getId());
                    call.enqueue(new Callback<IResponse>(){
                        public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                            IResponse result = response.body();
                            userLists.remove(i);
                            activity.notifyUserListAdapter();
                            if(result.Number == 0)
                            {
                                Toast.makeText(activity, result.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(activity,result.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            hoverLayout.setVisibility(View.INVISIBLE);
                            //mProgressDialog.dismiss();
                        }
                        public void onFailure(Call<IResponse> call, Throwable t)
                        {
                            Toast.makeText(activity,"something went wrong", Toast.LENGTH_LONG).show();
                            //mProgressDialog.dismiss();
                        }
                    });
                }
            });

            tvUserListName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i  = getPosition();
                    UserListObject p = userLists.get(i);
                    activity.userListTracksFragment = UserListTracksFragment.newInstance(0, p.getId());
                    //UserListTracksFragment fragment = UserListTracksFragment.newInstance(0, p.getId());
                    android.support.v4.app.FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, activity.userListTracksFragment);
                    fragmentTransaction.commit();
                    //Toast.makeText(activity, "tvUserListName", Toast.LENGTH_SHORT).show();
                }
            });

            btnRename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i  = getPosition();
                    final UserListObject objUserList = userLists.get(i);

                    // get prompts.xml view
                    LayoutInflater li = LayoutInflater.from(activity);
                    View promptsView = li.inflate(R.layout.layout_user_list_edittext, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            activity);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);

                    final EditText userInput = promptsView
                            .findViewById(R.id.edittxtUserListName);
                    userInput.setText(objUserList.getName());
                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            // get user input and set it to result
                                            // edit text
                                            final String newListName = userInput.getText().toString();
                                            if (newListName.equals(""))
                                            {
                                                Toast.makeText(activity, "Name cann't be empty", Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                //mProgressDialog.setMessage("Loading");
                                                //mProgressDialog.show();
                                                // edit user list
                                                Call<IResponse> call = Global.client.EditUserList(objUserList.getId(), newListName);
                                                call.enqueue(new Callback<IResponse>(){
                                                    public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                                                        IResponse result = response.body();
                                                        objUserList.setName(newListName);

                                                        if(result.Number == 0)
                                                        {
                                                            Toast.makeText(activity,"List Edit Successfully", Toast.LENGTH_LONG).show();
                                                        }
                                                        else
                                                        {
                                                            Toast.makeText(activity,result.Message, Toast.LENGTH_LONG).show();
                                                        }
                                                        hoverLayout.setVisibility(View.INVISIBLE);
                                                        activity.notifyUserListAdapter();
                                                        //mProgressDialog.dismiss();
                                                    }
                                                    public void onFailure(Call<IResponse> call, Throwable t)
                                                    {
                                                        Toast.makeText(activity,"something went wrong", Toast.LENGTH_LONG).show();
                                                        //mProgressDialog.dismiss();
                                                    }
                                                });
                                            }
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
            });
        }
    }


}

