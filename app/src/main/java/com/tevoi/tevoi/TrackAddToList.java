package com.tevoi.tevoi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.model.IResponse;
import com.tevoi.tevoi.model.UserListObject;
import com.tevoi.tevoi.model.UserListResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackAddToList extends Fragment {

    SideMenu activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activity = (SideMenu) getActivity();
        return inflater.inflate(R.layout.fragment_track_add_to_list, container, false);
    }

    public void addTrackToListClick(View v)
    {
        switch(v.getId())
        {
            // Just like you were doing
            case R.id.imgBtnAddTrackToList :
            {
                final int Id = activity.mediaPlayerFragment.currentTrack.getId();
                /*// here we need to add track to play next list

                String Name = activity.mediaPlayerFragment.currentTrack.getName();
                TrackSerializableObject track = new TrackSerializableObject();
                track.setId(Id);
                track.setName(Name);
                track.setDuration("03:20");
                track.setAuthor("Authors");
                track.setCategories("Categories");
                track.setRate(2);
                String result = activity.storageManager.addTrack(getContext(), track);
                activity.playNowListTracks = activity.storageManager.loadPlayNowTracks(getContext());
                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();*/

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
                                .setPositiveButton(activity.getResources().getText(R.string.Yes),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int id) {
                                                UserListObject selectedList = (UserListObject) ( ((Spinner) promptsView.findViewById(R.id.user_lists_spinner) ).getSelectedItem());
                                                //Toast.makeText(context, selectedList.getName(), Toast.LENGTH_SHORT).show();

                                                Call<IResponse> call = Global.client.AddTrackToUserList(Id, selectedList.getId());
                                                call.enqueue(new Callback<IResponse>(){
                                                    public void onResponse(Call<IResponse> call, Response<IResponse> response)
                                                    {
                                                        IResponse result = response.body();
                                                        Toast.makeText(activity,R.string.track_added_to_list_successfully, Toast.LENGTH_SHORT).show();

                                                    }
                                                    public void onFailure(Call<IResponse> call, Throwable t)
                                                    {
                                                        Toast.makeText(activity, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        })
                                .setNegativeButton(activity.getResources().getText(R.string.cancel),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int id) {
//                                                Toast.makeText(activity, "No Select", Toast.LENGTH_SHORT).show();
                                                dialog.cancel();
                                            }
                                        });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        // show it
                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface arg0) {
                                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(activity.getResources().getColor(R.color.tevoiBrownDark));
                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.tevoiBrownDark));
                            }
                        });
                        alertDialog.show();
                    }
                    public void onFailure(Call<UserListResponse> call, Throwable t)
                    {
                        Toast.makeText(activity, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }
                });

                break;
            }
            case R.id.imgBtnCloseAddToList :
            {

                break;
            }
        }
    }
}
