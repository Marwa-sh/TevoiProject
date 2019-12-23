package com.tevoi.tevoi;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tevoi.tevoi.Utils.Global;


public class ErrorFragment extends Fragment
{
    String PreviousFragmentName;

    public static ErrorFragment newInstance(String previousFragmentName )
    {
        ErrorFragment f = new ErrorFragment();
        Bundle args = new Bundle();
        args.putString("PreviousFragmentName", previousFragmentName);
        f.setArguments(args);
        return f;
    }

    public void setPreviousFragmentName(String previousFragmentName) {
        PreviousFragmentName = previousFragmentName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
        {
            PreviousFragmentName = getArguments().getString("PreviousFragmentName");
        }
        else
        {
            PreviousFragmentName = Global.MediaPlayerFragmentName;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
      final View rootView = inflater.inflate(R.layout.error_layout, container, false);
        Button btnRetry = rootView.findViewById(R.id.error_btn_retry);
        btnRetry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SideMenu activity = (SideMenu) getActivity();

                //Toast.makeText(activity, "Ho here the previous page : " + PreviousFragmentName, Toast.LENGTH_SHORT).show();
                switch (PreviousFragmentName)
                {
                    case Global.UpgradeToPremiumFragmentName :
                    {
                        activity.navigateToMenuItem(R.id.upgrade_to_premium);
                        break;
                    }
                    case Global.HistoryFragmentName :
                    {
                        activity.navigateToMenuItem(R.id.list_history);
                        break;
                    }
                    case Global.PlayNextListFragment :
                    {
                        activity.navigateToMenuItem(R.id.play_next);
                        break;
                    }
                    case Global.FavouriteFragmentName :
                    {
                        activity.navigateToMenuItem(R.id.list_favourite);
                        break;
                    }
                    case Global.UserListsFragment :
                    {
                        activity.navigateToMenuItem(R.id.my_list);
                        break;
                    }
                    case Global.InterfaceLanguageFragmentName :
                    {
                        activity.navigateToMenuItem(R.id.interface_language);
                        break;
                    }
                    case Global.PartnersListFragmentName :
                    {
                        activity.navigateToMenuItem(R.id.list_partners);
                        break;
                    }
                    case Global.NotificationFragmentName :
                    {
                        activity.navigateToMenuItem(R.id.notifications);
                        break;
                    }
                    case Global.DownloadFragmentName :
                    {
                        activity.navigateToMenuItem(R.id.download_limits);
                        break;
                    }
                    case Global.AboutUsFragmentName :
                    {
                        activity.navigateToMenuItem(R.id.about_us);
                        break;
                    }
                    case Global.FeedbackFragmentName :
                    {
                        activity.navigateToMenuItem(R.id.feedback_contact);
                        break;
                    }
                    case Global.FollowUsFragmentName :
                    {
                        activity.navigateToMenuItem(R.id.follow_us);
                        break;
                    }
                    case Global.ListTracksFragmentName :
                    {
                        activity.navigateToMenuItem(R.id.list_tracks);
                        break;
                    }
                }
                /* FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                // Replace the contents of the container with the new fragment
                if(PreviousFragmentName.equals(Global.PartnerNameFragment))
                    ft.replace(R.id.content_frame, activity.partnersFragment);
                else if(PreviousFragmentName.equals(Global.ListTracksFragmentName))
                    ft.replace(R.id.content_frame, activity.lisTracksFragment);
                else if(PreviousFragmentName.equals(Global.UserListTracksFragment))
                    ft.replace(R.id.content_frame, activity.userListsFragment);
                else if(PreviousFragmentName.equals(Global.PlayNextListFragment))
                    ft.replace(R.id.content_frame, activity.playingNowFragment);
                else
                    ft.replace(R.id.content_frame, activity.mediaPlayerFragment);

                activity.BackBtnAction();
                ft.commit();*/
            }
        });

        SideMenu a = ((SideMenu) getActivity());
       /* Call<TrackTextResponse> call = Global.client.GetTrackText(TrackId);
        call.enqueue(new Callback<TrackTextResponse>(){
            public void onResponse(Call<TrackTextResponse> call, Response<TrackTextResponse> response)
            {
                TrackTextResponse text = response.body();
                if(text.TrackText != null)
                {
                    //
                    TextView tv = rootView.findViewById(R.id.text_track);
                    tv.setText(text.TrackText);
                }
            }
            public void onFailure(Call<TrackTextResponse> call, Throwable t)
            {
                TextView tv = rootView.findViewById(R.id.text_track);
                tv.setText("No Text");
            }
        });*/

        return  rootView;

    }
}
