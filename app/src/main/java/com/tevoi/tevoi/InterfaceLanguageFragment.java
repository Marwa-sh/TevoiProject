
package com.tevoi.tevoi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

public class InterfaceLanguageFragment extends Fragment {

    View rootView;
    CheckBox chkArabic;
    CheckBox chkEnglish;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_interface_language, container, false);
        chkArabic = rootView.findViewById(R.id.chk_arabic);
        chkEnglish = rootView.findViewById(R.id.chk_english);

        SideMenu activity = (SideMenu) getActivity();
        String language = activity.storageManager.getLanguagePreference(activity);

        if(language.equals("ar"))
        {
            chkArabic.setChecked(true); chkEnglish.setChecked(false);
        }
        else {
            chkArabic.setChecked(false); chkEnglish.setChecked(true);
        }


        chkEnglish.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // you might keep a reference to the CheckBox to avoid this class cast
                boolean checked = ((CheckBox)v).isChecked();
                chkArabic.setChecked(!checked);

                SideMenu activity = (SideMenu) getActivity();
                if(checked)
                    activity.storageManager.storeLanguagePreference(activity, "en");
                else
                    activity.storageManager.storeLanguagePreference(activity, "ar");
            }

        });

        chkArabic.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // you might keep a reference to the CheckBox to avoid this class cast
                boolean checked = ((CheckBox)v).isChecked();
                chkEnglish.setChecked(!checked);
                SideMenu activity = (SideMenu) getActivity();
                if(checked)
                    activity.storageManager.storeLanguagePreference(activity, "ar");
                else
                    activity.storageManager.storeLanguagePreference(activity, "en");
            }

        });


        return rootView;
    }
}