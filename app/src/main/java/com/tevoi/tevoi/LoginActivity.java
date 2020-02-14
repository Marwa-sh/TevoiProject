
package com.tevoi.tevoi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.Utils.HelperFunctions;
import com.tevoi.tevoi.Utils.MyStorage;
import com.tevoi.tevoi.model.InternetConnectionListener;
import com.tevoi.tevoi.model.LoginRequestModel;
import com.tevoi.tevoi.model.LoginResponse;
import com.tevoi.tevoi.model.TrackObject;
import com.tevoi.tevoi.rest.ApiClient;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity
{
    TextView txtRegister;
    TextView txtForgetPassword;
    Button btnLogin;
    ImageButton btnRegister;
    EditText etUserName,etPassword,etEmail;
    CheckBox checkBoxRememberMe;
    Button btnGuest;
    public ProgressDialog mProgressDialog;

    TextView txtErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);


        //region detect language
        MyStorage storageManager = new MyStorage();
        String language = storageManager.getLanguageUIPreference(this);
        if (language == null)
            language = "en";
        Global.UserUILanguage = language;
        Resources res = getBaseContext().getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(language)); // API 17+ only.
        // Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);

        // endregion


        /*
        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(this.getResources().getColor(R.color.tevoiBluePrimary));*/

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        TextView mSubTitle = (TextView) toolbar.findViewById(R.id.toolbar_subtitle);

        setSupportActionBar(toolbar);
        mTitle.setText("Tevoi");*/
        //mSubTitle.setText("First Page");
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        mProgressDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setCancelable(false);

        txtRegister = findViewById(R.id.txtRegister);
        txtForgetPassword = findViewById(R.id.txt_forget_password);

        btnLogin = findViewById(R.id.btn_Login);
        btnRegister = findViewById(R.id.btn_register);

        etUserName = findViewById(R.id.et_User_Name);
        etPassword = findViewById(R.id.et_Password);
        etEmail = findViewById(R.id.et_enter_email);

        btnGuest = (Button) findViewById(R.id.btn_continue_as_guest);

        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(),SideMenu.class);
                Bundle b = new Bundle();
                b.putBoolean("IsDemoUser", true); //Your id
                i.putExtras(b); //Put your id to your next Intent

                startActivity(i);
                setContentView(R.layout.activity_side_menu);
            }
        });


        txtErrorMessage = (TextView) findViewById(R.id.txt_error_message);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isUserNameFieldEmpty())
                {
                    etUserName.setError(getResources().getString( R.string.user_name_missing));
                    etUserName.requestFocus();
                }
                else if(isPasswordFieldEmpty())
                {
                    etPassword.setError(getResources().getString( R.string.password_missing));
                    etPassword.requestFocus();
                }
                else
                {
                    mProgressDialog.setMessage(getResources().getString( R.string.loader_msg)); mProgressDialog.show();

                    LoginRequestModel model = new LoginRequestModel();
                    model.Username = etUserName.getText().toString();
                    model.Password = etPassword.getText().toString();

                    //Login and get token then save it in shared preference
                    Call<LoginResponse> call =Global.clientDnn.Login(model);
                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            LoginResponse login = response.body();
                            if(login.getNumber() == 0)
                            {
                                Global.CurrentUserId = login.getUserId();
                                MyStorage storageManager = new MyStorage(login.getUserId());

                                storageManager.storeCurrentUserId(LoginActivity.this,login.getUserId());
                                storageManager.storeTokenPreference(LoginActivity.this, login.getToken());
                                Global.UserToken = storageManager.getTokenPreference(LoginActivity.this);
                                Global.UserUILanguage = storageManager.getLanguageUIPreference(LoginActivity.this);
                                //storageManager.storeRememberMePreference(LoginActivity.this, isRememberMe);
                                storageManager.storeListTracks(LoginActivity.this, login.getLstTracks());
                                storageManager.storeListPartners(LoginActivity.this, login.getLstPartners());
                                storageManager.storeUsetList(LoginActivity.this, login.getUserlst());
                                storageManager.storeNotificationtList(LoginActivity.this, login.getLstNotificationTypes());
                                storageManager.storeAboutUs(LoginActivity.this,login.getAboutUS());
                                storageManager.storenumberOfMinutes(LoginActivity.this,login.getNumberOfMinutes());
                                storageManager.storeListMainTopicFilter(LoginActivity.this, login.getLstMainTopic());
                                storageManager.storeListSubscripedPartnerFilter(LoginActivity.this, login.getLstSubscripedPartners());
                                storageManager.storeFavoriteListTracks(LoginActivity.this, login.getLstFavouriteTracks());
                                storageManager.storeHistoryListTracks(LoginActivity.this, login.getLstHistoryTracks());
                                Global.UserNewUILanguage = storageManager.getLanguageUIPreference(LoginActivity.this);

                                storageManager.storeBanner(LoginActivity.this, login.getBanner());

                                Intent i = new Intent(getApplicationContext(),SideMenu.class);
                                startActivity(i);
                                setContentView(R.layout.activity_side_menu);
                                mProgressDialog.dismiss();
                            }
                            else
                            {
                                Toast.makeText(getBaseContext(),login.Message,Toast.LENGTH_SHORT).show();
                                mProgressDialog.dismiss();
                            }
                        }
                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            showErrorView(t);
                            mProgressDialog.dismiss();
                        }
                    });
                }
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity(i);
                //setContentView(R.layout.activity_register);
            }
        });
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity(i);
                //setContentView(R.layout.activity_register);
            }
        });
        txtForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ForgetPasswordActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity(i);
                //setContentView(R.layout.activity_forget_password);
            }
        });


        etUserName.setText("ma");
        etPassword.setText("A@123456");
       // btnLogin.performClick();

    }

    //Region Helping Checkers
    boolean isUserNameFieldEmpty()
    {
        String userName;
        userName=etUserName.getText().toString();
        return (userName.equals(""));
    }
    boolean isPasswordFieldEmpty()
    {
        String password;
        password=etPassword.getText().toString();
        return (password.equals(""));
    }
    boolean isEmailFieldEmpty()
    {
        String email;
        email=etEmail.getText().toString();
        return (email.equals(""));
    }
    //end Region

    // region handle internet offline
    private void showErrorView(Throwable throwable)
    {
        if (txtErrorMessage.getVisibility() == View.GONE)
        {
            txtErrorMessage.setVisibility(View.VISIBLE);
            txtErrorMessage.setText(fetchErrorMessage(throwable));
        }
    }
    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getResources().getString(R.string.error_msg_unknown);

        if (!HelperFunctions.isNetworkConnected(LoginActivity.this))
        {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        }
        else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }
    //endregion

}
