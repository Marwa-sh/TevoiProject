
package com.tevoi.tevoi;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.model.IResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InterfaceLanguageFragment extends Fragment {

    View rootView;
    CheckBox chkArabic;
    CheckBox chkEnglish;

    CheckBox chkTrackArabic;
    CheckBox chkTrackEnglish;

    CheckBox chkShowWithDefaultTrackLanguage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_interface_language, container, false);
        chkArabic = rootView.findViewById(R.id.chk_arabic);
        chkEnglish = rootView.findViewById(R.id.chk_english);

        chkTrackArabic = rootView.findViewById(R.id.chk_track_arabic);
        chkTrackEnglish = rootView.findViewById(R.id.chk_track_english);

        chkShowWithDefaultTrackLanguage = rootView.findViewById(R.id.chk_show_with_default_audio_languae);

        SideMenu activity = (SideMenu) getActivity();
        String language = activity.storageManager.getLanguageUIPreference(activity);

        if(language.equals("ar"))
        {
            chkArabic.setChecked(true); chkEnglish.setChecked(false);
        }
        else {
            chkArabic.setChecked(false); chkEnglish.setChecked(true);
        }

        String languageTrack = activity.storageManager.getLanguageTrackPreference(activity);

        if(languageTrack.equals("ar"))
        {
            chkTrackArabic.setChecked(true); chkTrackEnglish.setChecked(false);
        }
        else {
            chkTrackArabic.setChecked(false); chkTrackEnglish.setChecked(true);
        }

        chkEnglish.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                // you might keep a reference to the CheckBox to avoid this class cast
                boolean checked = ((CheckBox)v).isChecked();
                chkArabic.setChecked(!checked);
                chkTrackArabic.setChecked(!checked);
                chkTrackEnglish.setChecked(checked);
                int LanguageId;

                SideMenu activity = (SideMenu) getActivity();
                if(checked)
                {
                    activity.storageManager.storeLanguageUIPreference(activity, "en");
                    activity.storageManager.storeLanguageTrackPreference(activity, "en");
                    LanguageId = Global.English;
                }
                else
                {
                    activity.storageManager.storeLanguageUIPreference(activity, "ar");
                    activity.storageManager.storeLanguageTrackPreference(activity, "ar");
                    LanguageId = Global.Arabic;
                }
                Call<IResponse> call = Global.client.UpdateUIDefaultLanguage(LanguageId);
                call.enqueue(new Callback<IResponse>() {
                    @Override
                    public void onResponse(Call<IResponse> call, Response<IResponse> response) {

                    }
                    @Override
                    public void onFailure(Call<IResponse> call, Throwable t) {

                    }
                });
                Toast.makeText(activity, R.string.restart_app_for_apply_language, Toast.LENGTH_SHORT).show();
            }

        });

        chkArabic.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // you might keep a reference to the CheckBox to avoid this class cast
                boolean checked = ((CheckBox)v).isChecked();
                chkEnglish.setChecked(!checked);
                chkTrackArabic.setChecked(checked);
                chkTrackEnglish.setChecked(!checked);
                int LanguageId;
                SideMenu activity = (SideMenu) getActivity();
                if(checked)
                {
                    activity.storageManager.storeLanguageUIPreference(activity, "ar");
                    activity.storageManager.storeLanguageTrackPreference(activity, "ar");
                    LanguageId = Global.Arabic;
                }
                else
                {
                    activity.storageManager.storeLanguageUIPreference(activity, "en");
                    activity.storageManager.storeLanguageTrackPreference(activity, "en");
                    LanguageId = Global.English;
                }

                Call<IResponse> call = Global.client.UpdateUIDefaultLanguage(LanguageId);
                call.enqueue(new Callback<IResponse>() {
                    @Override
                    public void onResponse(Call<IResponse> call, Response<IResponse> response) {

                    }
                    @Override
                    public void onFailure(Call<IResponse> call, Throwable t) {

                    }
                });
            }

        });


        chkTrackEnglish.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // you might keep a reference to the CheckBox to avoid this class cast
                boolean checked = ((CheckBox)v).isChecked();
                int LanguageId;
                chkTrackArabic.setChecked(!checked);
                chkArabic.setChecked(!checked);
                chkEnglish.setChecked(checked);
                SideMenu activity = (SideMenu) getActivity();
                if(checked)
                {
                    activity.storageManager.storeLanguageTrackPreference(activity, "en");
                    activity.storageManager.storeLanguageUIPreference(activity, "en");
                    LanguageId = Global.English;
                }
                else
                {
                    activity.storageManager.storeLanguageTrackPreference(activity, "ar");
                    activity.storageManager.storeLanguageUIPreference(activity, "ar");
                    LanguageId = Global.Arabic;
                }

                Call<IResponse> call = Global.client.UpdateTrackDefaultLanguage(LanguageId);
                call.enqueue(new Callback<IResponse>() {
                    @Override
                    public void onResponse(Call<IResponse> call, Response<IResponse> response) {

                    }
                    @Override
                    public void onFailure(Call<IResponse> call, Throwable t) {

                    }
                });

            }

        });

        chkTrackArabic.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // you might keep a reference to the CheckBox to avoid this class cast
                boolean checked = ((CheckBox)v).isChecked();
                chkTrackEnglish.setChecked(!checked);
                chkArabic.setChecked(checked);
                chkEnglish.setChecked(!checked);
                int LanguageId;
                SideMenu activity = (SideMenu) getActivity();
                if(checked)
                {
                    activity.storageManager.storeLanguageTrackPreference(activity, "ar");
                    activity.storageManager.storeLanguageUIPreference(activity, "ar");
                    LanguageId = Global.Arabic;
                }
                else
                {
                    activity.storageManager.storeLanguageTrackPreference(activity, "en");
                    activity.storageManager.storeLanguageUIPreference(activity, "en");
                    LanguageId = Global.English;
                }

                Call<IResponse> call = Global.client.UpdateTrackDefaultLanguage(LanguageId);
                call.enqueue(new Callback<IResponse>() {
                    @Override
                    public void onResponse(Call<IResponse> call, Response<IResponse> response) {

                    }
                    @Override
                    public void onFailure(Call<IResponse> call, Throwable t) {

                    }
                });
            }

        });

        return rootView;
    }
}