package com.ebridge.tevoi;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SideMenuFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            // Retrieving the currently selected item number
            int position = getArguments().getInt("position");

            // List of rivers
            String[] rivers = getResources().getStringArray(R.array.rivers);

            // Creating view correspoding to the fragment
            View v = inflater.inflate(R.layout.fragment_side_menu, container, false);

            // Getting reference to the TextView of the Fragment
            TextView tv = (TextView) v.findViewById(R.id.tv_content);

            // Setting currently selected river name in the TextView
            //if(tv!= null)
            //    tv.setText(rivers[position]);

            // Updating the action bar title
            //getActivity().getActionBar().setTitle(rivers[position]);
            //getActivity().getActionBar().setSubtitle("subtitle");
            return v;
        }
    }

