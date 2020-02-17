package com.tevoi.tevoi.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.tevoi.tevoi.model.LoadingVH;
import com.tevoi.tevoi.model.RecyclerViewEmptySupport;
import com.tevoi.tevoi.model.UserListObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    // region pagination
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;
    private String errorMsg;

    private PaginationAdapterCallback mCallback;
    // endregion

    private List<UserListObject> userLists;
    private SideMenu activity;

    RecyclerViewEmptySupport recyclerView;

    public List<UserListObject> getUserLists()
    {
        return  userLists;
    }

    public UserListAdapter(List<UserListObject> userLists, SideMenu activity,RecyclerViewEmptySupport recyclerView) {
        this.userLists = userLists;
        this.activity = activity;
        this.recyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        RecyclerView.ViewHolder viewHolder2 = null;

        switch (viewType)
        {
            case ITEM:
            {
                viewHolder2 = getViewHolder(viewGroup,inflater );
                break;
            }
            case LOADING:
            {
                try
                {
                    View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_progress, viewGroup, false);
                    viewHolder2 = new LoadingVH(v);
                }
                catch (Exception exc)
                {
                    viewHolder2 = new LoadingVH(new View(activity));
                }
            }
            break;
        }
        return viewHolder2;
    }
    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup viewGroup, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View row= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_user_list_instance,viewGroup,false);
        viewHolder = new UserListAdapter.UserListViewHolder(row);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == userLists.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

        switch (getItemViewType(i))
        {
            case ITEM:
                UserListAdapter.UserListViewHolder viewHolder = (UserListAdapter.UserListViewHolder) holder;
                UserListObject userList =  userLists.get(i);
                viewHolder.tvUserListName.setText(userList.getName());
                viewHolder.tvCreationDate.setText(userList.getCreationDate());
                viewHolder.tvUserListDuration.setText(userList.getDuration());

                break;
            case LOADING:
            {
                LoadingVH loadingVH = (LoadingVH) holder;
                if(loadingVH != null) {

                    if (retryPageLoad)
                    {
                        loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                        loadingVH.mProgressBar.setVisibility(View.GONE);

                        loadingVH.mErrorTxt.setText(
                                errorMsg != null ?
                                        errorMsg :
                                        activity.getString(R.string.error_msg_unknown));

                    } else
                    {
                        if(loadingVH.mErrorLayout!= null) loadingVH.mErrorLayout.setVisibility(View.GONE);
                        if(loadingVH.mProgressBar!= null) loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                    }
                }
                break;
            }

        }

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

            String lang = activity.storageManager.getLanguageUIPreference(activity);
            if(lang.equals("ar"))
                btnPlay.setScaleX(-1);

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

                    // get prompts.xml view

                    // region old way to remove list
                    /*LayoutInflater li = LayoutInflater.from(activity);
                    View promptsView = li.inflate(R.layout.layout_user_list_remove, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);//, R.style.AlertDialogCustom);
                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);
                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            // to do remove list
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

                    alertDialog.show();*/
                    //endregion

                    final int i = getPosition();

                    LayoutInflater layoutInflater = LayoutInflater.from(activity);
                    View promptView = layoutInflater.inflate(R.layout.layout_user_list_remove, null);

                    final AlertDialog alertD = new AlertDialog.Builder(activity).create();

                    ImageButton btnYes = (ImageButton) promptView.findViewById(R.id.imgBtnYes);

                    ImageButton btnNo = (ImageButton) promptView.findViewById(R.id.imgBtnNo);

                    btnYes.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            alertD.cancel();
                            activity.mProgressDialog.setMessage(activity.getResources().getString( R.string.loader_msg));
                            activity.mProgressDialog.show();
                            // btnAdd1 has been clicked
                            Log.d("UserListId", "Delete=" +userLists.get(i).getId());
                            Call<IResponse> call = Global.client.DeleteUserList(userLists.get(i).getId());
                            call.enqueue(new Callback<IResponse>(){
                                public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                                    IResponse result = response.body();

                                    if(result.Number == 0)
                                    {
                                        Toast.makeText(activity,activity.getResources().getString(R.string.deleted_successfully), Toast.LENGTH_SHORT).show();
                                        userLists.remove(i);
                                        activity.storageManager.storeUsetList(activity,userLists);
                                        recyclerView.triggerObserver();
//                                        activity.storageManager.removeUserList(activity,userLists.get(i));
                                        activity.notifyUserListAdapter();

                                    }
                                    else
                                    {
                                        Toast.makeText(activity,result.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                    activity.mProgressDialog.dismiss();
                                    hoverLayout.setVisibility(View.INVISIBLE);
                                }
                                public void onFailure(Call<IResponse> call, Throwable t)
                                {
                                    Toast.makeText(activity,"something went wrong", Toast.LENGTH_LONG).show();
                                    //mProgressDialog.dismiss();
                                    hoverLayout.setVisibility(View.INVISIBLE);
                                    activity.mProgressDialog.dismiss();
                                }
                            });
                        }
                    });

                    btnNo.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            // btnAdd2 has been clicked
                            alertD.cancel();
                            hoverLayout.setVisibility(View.INVISIBLE);
                        }
                    });

                    alertD.setView(promptView);

                    alertD.show();


                }
            });

            userListDetailsLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i  = getPosition();
                    UserListObject p = userLists.get(i);
                    activity.userListTracksFragment = UserListTracksFragment.newInstance(0, p.getId(), p.getName());
                    //UserListTracksFragment fragment = UserListTracksFragment.newInstance(0, p.getId());
                    androidx.fragment.app.FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, activity.userListTracksFragment);
                    fragmentTransaction.commit();
                }
            });
            tvUserListName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i  = getPosition();
                    UserListObject p = userLists.get(i);
                    if(p.getDuration() == "00:00")
                    {
                        Toast.makeText(activity, R.string.list_has_no_tracks, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        activity.userListTracksFragment = UserListTracksFragment.newInstance(0, p.getId(), p.getName());
                        //UserListTracksFragment fragment = UserListTracksFragment.newInstance(0, p.getId());
                        androidx.fragment.app.FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, activity.userListTracksFragment);
                        fragmentTransaction.commit();
                        //Toast.makeText(activity, "tvUserListName", Toast.LENGTH_SHORT).show();
                    }
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
                            .setPositiveButton(activity.getResources().getString(R.string.Yes),
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
                                                            Toast.makeText(activity,activity.getResources().getString(R.string.list_edit_successfully), Toast.LENGTH_LONG).show();
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
                            .setNegativeButton(activity.getResources().getString(R.string.cancel),
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

    // region helpers

    public void add(UserListObject mc) {
        userLists.add(mc);
        notifyItemInserted(userLists.size() - 1);
    }

    public void addAll(List<UserListObject> mcList)
    {
        for (UserListObject mc : mcList) {
            add(mc);
        }
    }

    public void remove(UserListObject city) {
        int position = userLists.indexOf(city);
        if (position > -1) {
            userLists.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new UserListObject());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = userLists.size() - 1;
        UserListObject item = getItem(position);

        if (item != null) {
            userLists.remove(position);
            notifyItemRemoved(position);
        }
    }

    public UserListObject getItem(int position) {
        return userLists.get(position);
    }


    // endregion

}

