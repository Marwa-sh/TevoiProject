
package com.tevoi.tevoi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.Utils.MyStorage;
import com.tevoi.tevoi.model.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity
{
    TextView txtLogin;
    ImageButton btnLogin, btnRegister, btnRequestNewPassword;
    EditText etUserName,etPassword,etEmail;
    CheckBox checkBoxRememberMe;
    public ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);

        /*Window window = this.getWindow();
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

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);

        txtLogin = findViewById(R.id.txtLogin);

        btnLogin = findViewById(R.id.btn_Login);
        btnRegister = findViewById(R.id.btn_register);
        btnRequestNewPassword = findViewById(R.id.btn_Request_new_password);

        etUserName = findViewById(R.id.et_User_Name);
        etPassword = findViewById(R.id.et_Password);
        etEmail = findViewById(R.id.et_enter_email);

        checkBoxRememberMe=findViewById(R.id.checkBox_remember_me);

        txtLogin.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(isUserNameFieldEmpty())
                 {
                     etUserName.setError("user name missing");
                     etUserName.requestFocus();
                 }
                 else if(isPasswordFieldEmpty())
                 {
                     etPassword.setError("password missing");
                     etPassword.requestFocus();
                 }
                 else
                 {
                     mProgressDialog.setMessage("Loading"); mProgressDialog.show();

                     //Login and get token then save it in shared preference
                     final boolean isRememberMe =checkBoxRememberMe.isChecked();
                     Call<LoginResponse> call =Global.clientDnn.Login(etUserName.getText().toString(),etPassword.getText().toString());
                     call.enqueue(new Callback<LoginResponse>() {
                         @Override
                         public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                             LoginResponse login = response.body();
                             if(login.getNumber() == 0)
                             {
                                 Global.CurrentUserId = login.getUserId();
                                 //Toast.makeText(LoginActivity.this, ""+Global.CurrentUserId, Toast.LENGTH_SHORT).show();
                                 MyStorage storageManager = new MyStorage(login.getUserId());

                                 storageManager.storeCurrentUserId(LoginActivity.this,login.getUserId());
                                 storageManager.storeTokenPreference(LoginActivity.this, login.getToken());
                                 Global.UserToken = storageManager.getTokenPreference(LoginActivity.this);
                                 //storageManager.storeRememberMePreference(LoginActivity.this, isRememberMe);

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
                             Toast.makeText(LoginActivity.this, "", Toast.LENGTH_SHORT).show();
                         }
                     });
                 }
             }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isUserNameFieldEmpty())
                {
                    etUserName.setError("user name missing");
                    etUserName.requestFocus();
                }
                else if(isPasswordFieldEmpty())
                {
                    etPassword.setError("password missing");
                    etPassword.requestFocus();
                }
                else
                    {
                        mProgressDialog.setMessage("Loading"); mProgressDialog.show();

                    //Login and get token then save it in shared preference
                    final boolean isRememberMe =checkBoxRememberMe.isChecked();
                    Call<LoginResponse> call =Global.clientDnn.Login(etUserName.getText().toString(),etPassword.getText().toString());
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
                                //storageManager.storeRememberMePreference(LoginActivity.this, isRememberMe);

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
                            Toast.makeText(LoginActivity.this, "", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(i);
                setContentView(R.layout.activity_register);
            }
        });


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



}
