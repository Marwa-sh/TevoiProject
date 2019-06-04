package com.tevoi.tevoi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.Utils.MyStorage;
import com.tevoi.tevoi.model.MainSponsoreLogoResponse;

import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntroActivity extends Activity {

    ImageView imageViewIntroLogo;
    ImageView imgMainSponsoreLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        imgMainSponsoreLogo = findViewById(R.id.img_main_sponsore_logo);
        imageViewIntroLogo = findViewById(R.id.imageViewIntroLogo);

        imageViewIntroLogo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                // go to side menu activity

                // check if he has token and he checked remember me
                MyStorage storageManager = new MyStorage();
                Boolean isRememberMe = storageManager.getRememberMePreference(IntroActivity.this);
                if(isRememberMe)
                {
                    // TODO : check token date
                    Intent i = new Intent(getApplicationContext(),SideMenu.class);
                    startActivity(i);
                    setContentView(R.layout.activity_side_menu);
                }
                else
                {
                    // go to login
                    Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(i);
                    setContentView(R.layout.fragment_login);
                }

            }
        });

        Call<MainSponsoreLogoResponse> call = Global.client.GetMainSponsoreLogo();
        call.enqueue(new Callback<MainSponsoreLogoResponse>() {
            @Override
            public void onResponse(Call<MainSponsoreLogoResponse> call, Response<MainSponsoreLogoResponse> response) {
                MainSponsoreLogoResponse result = response.body();
                if(result.getNumber() == 0)
                {
                    try
                    {
                        URL newurl = new URL(Global.IMAGE_BASE_URL + result.getMainSponsoreLogo());
                        Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
                        imgMainSponsoreLogo.setImageBitmap(mIcon_val);
                    }
                    catch (Exception exc)
                    {

                    }
                }
                else
                {
                    Toast.makeText(getBaseContext(),result.Message,Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<MainSponsoreLogoResponse> call, Throwable t) {

            }
        });

    }

    public  void skipIntro(View view)
    {

    }
}
