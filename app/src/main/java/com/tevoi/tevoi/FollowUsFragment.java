
package com.tevoi.tevoi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class FollowUsFragment extends Fragment {
        ImageButton btnfollowUsFb,btnfollowUsIns,btnfollowUsYouTube,btnfollowUsWhatsUp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_follow_us, container, false);

        btnfollowUsFb = rootView.findViewById(R.id.imgBtnfollowUsFb);
        btnfollowUsIns = rootView.findViewById(R.id.imgBtnfollowUsIN);
        btnfollowUsYouTube = rootView.findViewById(R.id.imgBtnfollowUsYoutube);
        btnfollowUsWhatsUp = rootView.findViewById(R.id.imgBtnfollowUsWhatsup);

        btnfollowUsFb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.facebook.com/TeVoiServices"));
                startActivity(intent);
            } });
        btnfollowUsIns.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://tevoi.com/"));
                startActivity(intent);
            } });
        btnfollowUsYouTube.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://tevoi.com/"));
                startActivity(intent);
            } });
        btnfollowUsWhatsUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    String headerReceiver = "";// Replace with your message.
                    String bodyMessageFormal = "";// Replace with your message.
                    String whatsappContain = headerReceiver + bodyMessageFormal;
                    String trimToNumner = "+971 56 360 1717"; //10 digit number
                    Intent intent = new Intent ( Intent.ACTION_VIEW );
                    intent.setData ( Uri.parse ( "https://wa.me/" + trimToNumner + "/?text=" + "" ) );
                    startActivity ( intent );
                } catch (Exception e) {
                    e.printStackTrace ();
                }
            } });

        return rootView;
    }
}
