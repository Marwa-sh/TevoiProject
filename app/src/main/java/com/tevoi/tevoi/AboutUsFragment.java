
package com.tevoi.tevoi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.model.AboutUsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutUsFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_about_us, container, false);

        final TextView txt = rootView.findViewById(R.id.txtAboutUs);
        final SideMenu activity = (SideMenu) getActivity();

/*        activity.mProgressDialog.setMessage(getResources().getString(R.string.loader_msg));
        activity.mProgressDialog.setCancelable(false);
        activity.mProgressDialog.show();*/

        String loadtxt;
        loadtxt = activity.storageManager.loadAboutUs(activity);

        if(loadtxt.equals(""))
        {
            txt.setText(R.string.visit_website);
        }
        else
            txt.setText(loadtxt);
/*
        Call<AboutUsResponse> call = Global.client.GetAboutUs();
        call.enqueue(new Callback<AboutUsResponse>() {
            @Override
            public void onResponse(Call<AboutUsResponse> call, Response<AboutUsResponse> response) {
                AboutUsResponse txtResonse = response.body();
                txt.setText(txtResonse.getText());
                activity.mProgressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<AboutUsResponse> call, Throwable t) {

                txt.setText(R.string.visit_website);
                activity.mProgressDialog.dismiss();
            }
        });
*/

        return rootView;
    }
}
