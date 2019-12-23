package com.tevoi.tevoi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.Utils.MyStorage;
import com.tevoi.tevoi.model.MainSponsoreLogoResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntroActivity  extends AppCompatActivity {

    ImageView imageViewIntroLogo;
    ImageView imgMainSponsoreLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

       /* Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(this.getResources().getColor(R.color.tevoiBluePrimary));*/

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        TextView mSubTitle = (TextView) toolbar.findViewById(R.id.toolbar_subtitle);

        setSupportActionBar(toolbar);*/
        //mTitle.setText("Tevoi");
        //mSubTitle.setText("First Page");
        //getSupportActionBar().setDisplayShowTitleEnabled(false);


        imgMainSponsoreLogo = findViewById(R.id.img_main_sponsore_logo);
        imageViewIntroLogo = findViewById(R.id.imageViewIntroLogo);

        imageViewIntroLogo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // go to side menu activity

                // check if he has token and he checked remember me
                MyStorage storageManager = new MyStorage(0);
                Boolean isRememberMe = storageManager.getRememberMePreference(IntroActivity.this);
                if(isRememberMe)
                {
                    // TODO : check token date
                    Intent i = new Intent(getApplicationContext(),SideMenu.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    startActivity(i);
                    //setContentView(R.layout.activity_side_menu);
                }
                else
                {
                    // go to login
                    Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    startActivity(i);
                    //setContentView(R.layout.fragment_login);
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
                        Picasso.with(IntroActivity.this)  //Here, this is context.
                                .load(Global.IMAGE_BASE_URL + result.getMainSponsoreLogo())  //Url of the image to load.
                                .into(imgMainSponsoreLogo);
                    }
                    catch (Exception exc)
                    {
                        //Toast.makeText(IntroActivity.this,"l="+ exc.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getBaseContext(),result.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<MainSponsoreLogoResponse> call, Throwable t) {

            }
        });

        Thread timer= new Thread()
        {
            public void run()
            {
                try
                {
                    //Display for 5 seconds
                    sleep(2000);
                }
                catch (InterruptedException e)
                {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                finally
                {
                    // check if he has token and he checked remember me
                    MyStorage storageManager = new MyStorage(0);
                    Boolean isRememberMe = storageManager.getRememberMePreference(IntroActivity.this);
                    if(isRememberMe)
                    {
                        // TODO : check token date
                        Intent i = new Intent(getApplicationContext(),SideMenu.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                        startActivity(i);
                    }
                    else
                    {
                        // go to login
                        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                        startActivity(i);
                    }
                }
            }
        };
        timer.start();

    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public  void skipIntro(View view)
    {

    }

}
