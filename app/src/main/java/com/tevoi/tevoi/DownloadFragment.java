
package com.tevoi.tevoi;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.model.GetDownloadLimitResponse;
import com.tevoi.tevoi.model.IResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadFragment extends Fragment
{
    SeekBar seekbar;
    SideMenu activity;
    int numberOfMinutes = 0;
    ImageButton btnSaveChanges;
    TextView txtSaveChanges;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_download, container, false);
        activity = (SideMenu) getActivity();

        seekbar = rootView.findViewById(R.id.seekbar_download_limits);
        btnSaveChanges = rootView.findViewById(R.id.save_changes_download_limits);
        txtSaveChanges = rootView.findViewById(R.id.txt_save_changes_download_limits);

        int progress = activity.storageManager.getnumberOfMinutes(activity) / 60;
        seekbar.setProgress(progress);

        /*activity.mProgressDialog.setMessage(getResources().getString( R.string.loader_msg)); activity.mProgressDialog.show();

        Call<GetDownloadLimitResponse> call = Global.client.GetDownloadLimit();
        call.enqueue(new Callback<GetDownloadLimitResponse>(){
            public void onResponse(Call<GetDownloadLimitResponse> call, Response<GetDownloadLimitResponse> response)
            {
                GetDownloadLimitResponse result = response.body();
                if(result != null)
                {
                    activity.mProgressDialog.dismiss();
                    int progress = result.NumberOfMinutes / 60;
                    seekbar.setProgress(progress);
                }
                else
                {
                    activity.mProgressDialog.dismiss();
                    seekbar.setProgress(0);
                }
            }
            public void onFailure(Call<GetDownloadLimitResponse> call, Throwable t)
            {
                activity.mProgressDialog.dismiss();
                seekbar.setProgress(0);
            }
        });*/

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Toast.makeText(getContext(), "onProgressChanged=" + progress, Toast.LENGTH_LONG).show();
                switch (progress)
                {
                    case 0:
                    {
                        numberOfMinutes = 0;
                        break;
                    }
                    case 1:
                    {
                        numberOfMinutes = 30;
                        break;
                    }
                    case 2:
                    {
                        numberOfMinutes = 60;
                        break;
                    }
                    case 3:
                    {
                        numberOfMinutes = 120;
                        break;
                    }
                    case 4:
                    {
                        numberOfMinutes = 300;
                        break;
                    }
                    case 5:
                    {
                        numberOfMinutes = 480;
                        break;
                    }
                    case 6:
                    {
                        numberOfMinutes = 600;
                        break;
                    }
                }

            }
        });
        txtSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.mProgressDialog.setMessage(getResources().getString( R.string.loader_msg)); activity.mProgressDialog.show();

                Call<IResponse> call = Global.client.UpdateDownloadLimit(numberOfMinutes);
                call.enqueue(new Callback<IResponse>(){
                    public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                        IResponse result = response.body();

                        if(result.Number == 0)
                        {
                            activity.mProgressDialog.dismiss();
                            Toast.makeText(activity, getResources().getString( R.string.download_limit_update_sucessfully), Toast.LENGTH_SHORT).show();
                            activity.storageManager.storenumberOfMinutes(activity,numberOfMinutes);
                        }
                        else
                        {
                            activity.mProgressDialog.dismiss();
                            Toast.makeText(activity,result.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    public void onFailure(Call<IResponse> call, Throwable t)
                    {
                        Toast.makeText(activity,getResources().getString( R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                        activity.mProgressDialog.dismiss();
                    }
                });
            }
        });

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.mProgressDialog.setMessage(getResources().getString( R.string.loader_msg)); activity.mProgressDialog.show();

                Call<IResponse> call = Global.client.UpdateDownloadLimit(numberOfMinutes);
                call.enqueue(new Callback<IResponse>(){
                    public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                        IResponse result = response.body();

                        if(result.Number == 0)
                        {
                            activity.mProgressDialog.dismiss();
                            Toast.makeText(activity, getResources().getString( R.string.download_limit_update_sucessfully), Toast.LENGTH_SHORT).show();
                            activity.storageManager.storenumberOfMinutes(activity,numberOfMinutes);
                        }
                        else
                        {
                            activity.mProgressDialog.dismiss();
                            Toast.makeText(activity,result.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    public void onFailure(Call<IResponse> call, Throwable t)
                    {
                        Toast.makeText(activity,getResources().getString( R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                        activity.mProgressDialog.dismiss();
                    }
                });
            }
        });

        return rootView;
    }
}
