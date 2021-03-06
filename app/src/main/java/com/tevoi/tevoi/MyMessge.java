package com.tevoi.tevoi;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MyMessge extends Fragment {

    String Message = "";
    public static MyMessge newInstance(String message) {
        MyMessge f = new MyMessge();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("Message", message);
        f.setArguments(args);

        return f;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Message = getArguments().getString("Message");

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_messge, container, false);
        TextView tv = rootView.findViewById(R.id.textMessage);
        tv.setText(Message);
        return  rootView;
    }
}
