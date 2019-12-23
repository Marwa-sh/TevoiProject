
package com.tevoi.tevoi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.Utils.MyStorage;
import com.tevoi.tevoi.model.IResponse;
import com.tevoi.tevoi.model.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity
{
    Button btnRequestNewPassword;
    EditText etEmail;

    public ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);

        etEmail = findViewById(R.id.et_enter_email);
        btnRequestNewPassword = findViewById(R.id.btn_Request_new_password);

        btnRequestNewPassword.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(isUserEmailFieldEmpty())
                 {
                     etEmail.setError("email missing");
                 }
                 else
                 {
                     mProgressDialog.setMessage(getResources().getString( R.string.loader_msg)); mProgressDialog.show();

                     //Login and get token then save it in shared preference
                     Call<IResponse> call =Global.client.RequestNewPassword(etEmail.getText().toString());
                     call.enqueue(new Callback<IResponse>() {
                         @Override
                         public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                             IResponse result = response.body();
                             if(result.getNumber() == 0)
                             {
                                 Toast.makeText(ForgetPasswordActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                             }
                             else
                             {
                                 Toast.makeText(ForgetPasswordActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                             }
                         }
                         @Override
                         public void onFailure(Call<IResponse> call, Throwable t)
                         {
                             Toast.makeText(ForgetPasswordActivity.this, "", Toast.LENGTH_SHORT).show();
                         }
                     });
                 }
             }
        });

    }
    //Region Helping Checkers
    boolean isUserEmailFieldEmpty()
    {
        String email;
        email=etEmail.getText().toString();
        return (email.equals(""));
    }
    //end Region



}
