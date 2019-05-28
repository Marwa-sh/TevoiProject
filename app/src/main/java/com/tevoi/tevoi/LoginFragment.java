
package com.tevoi.tevoi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.model.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {


    View rootView;
    ImageButton btnLogin, btnRegister, btnRequestNewPassword;
    EditText etUserName,etPassword,etEmail;
    CheckBox checkBoxRememberMe;
    SideMenu activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        activity = (SideMenu) getActivity();

        btnLogin=rootView.findViewById(R.id.btn_Login);
        btnRegister=rootView.findViewById(R.id.btn_register);
        btnRequestNewPassword=rootView.findViewById(R.id.btn_Request_new_password);

        etUserName=rootView.findViewById(R.id.et_User_Name);
        etPassword=rootView.findViewById(R.id.et_Password);
        etEmail=rootView.findViewById(R.id.et_enter_email);

        checkBoxRememberMe=rootView.findViewById(R.id.checkBox_remember_me);

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
                else {
                    //Login and get token then save it in shared preference
                    boolean isRememberMe =checkBoxRememberMe.isChecked();
                    Call<LoginResponse> call =Global.clientDnn.Login(etUserName.getText().toString(),etPassword.getText().toString());
                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            LoginResponse login = response.body();
                            if(login.getNumber() == 0)
                            {
                                activity.storageManager.storeTokenPreference(activity, login.getToken());
                                Global.UserToken = activity.storageManager.getTokenPreference(activity);

                                android.support.v4.app.FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.content_frame, activity.lisTracksFragment);
                                fragmentTransaction.addToBackStack( "lisTracksFragment" );
                                fragmentTransaction.commit();
                            }
                            else
                            {
                                Toast.makeText(rootView.getContext(),login.Message,Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {

                        }
                    });
                }
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.content_frame, activity.registerFragment);
                fragmentTransaction.addToBackStack( "registerFragment" );
                fragmentTransaction.commit();
            }
        });

        return rootView;


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
