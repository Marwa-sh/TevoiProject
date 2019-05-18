
package com.ebridge.tevoi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class LoginFragment extends Fragment {


    View rootView;
    Button btnLogin, btnCancel,btnRequestNewPassword;
    EditText etUserName,etPassword,etEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_tracks_list, container, false);
        btnLogin=rootView.findViewById(R.id.btn_Login);
        btnCancel=rootView.findViewById(R.id.btn_Calcel_Login);
        btnRequestNewPassword=rootView.findViewById(R.id.btn_Request_new_password);

        etUserName=rootView.findViewById(R.id.et_User_Name);
        etPassword=rootView.findViewById(R.id.et_Password);
        etEmail=rootView.findViewById(R.id.et_enter_email);





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

                }


            }
        });


        return inflater.inflate(R.layout.fragment_login, container, false);


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
