package com.tevoi.tevoi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.model.CountryObject;
import com.tevoi.tevoi.model.GenderObject;
import com.tevoi.tevoi.model.RegisterDataResponse;
import com.tevoi.tevoi.model.RegisterRequest;
import com.tevoi.tevoi.model.RegisterResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements SpinnerAdapter, AdapterView.OnItemSelectedListener
{
    Spinner spnrCountries, spnrGender;
    List<CountryObject> countries;
    List<GenderObject> genders;
    ArrayAdapter spnrCountriesAdapter,spnrGendersAdapter;
    RegisterRequest registerRequest = new RegisterRequest();
    EditText etUserName, etPassword, etPasswordConfirmation, etOccupation, etAge, etEmail;
    ImageButton imgbtnRegister, imgbtnRegisterCancel;
    public ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);

        etUserName = findViewById(R.id.et_register_user_name);
        etPassword = findViewById(R.id.et_register_password);
        etPasswordConfirmation = findViewById(R.id.et_register_confirm_password);
        etEmail = findViewById(R.id.et_register_email);
        etAge = findViewById(R.id.et_register_age);
        etOccupation = findViewById(R.id.et_register_occupation);

        imgbtnRegister = findViewById(R.id.imgbtn_register);
        imgbtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterRequest request = new RegisterRequest();

                GenderObject genderObject= genders.get(spnrGender.getSelectedItemPosition());
                CountryObject countryObject= countries.get(spnrCountries.getSelectedItemPosition());
                request.setGender(genderObject.getId());

                allRequiredFieldsFilled();
                checkPasswordsMatching();

                request.setCountry(countryObject.getId());
                int age = Integer.parseInt(etAge.getText().toString());
                request.setAge(age);
                request.setEmail(etEmail.getText().toString());
                request.setUserName(etUserName.getText().toString());
                request.setPassword(etPassword.getText().toString());
                request.setOccupation(etOccupation.getText().toString());

                mProgressDialog.setMessage("Loading"); mProgressDialog.show();

                Call<RegisterResponse> call=Global.clientDnn.Register(request);
                call.enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        RegisterResponse res = response.body();
                        if (res.Number == 0)
                        {
                            Toast.makeText(getBaseContext(), res.getMessage(), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(i);
                            setContentView(R.layout.fragment_login);
                            mProgressDialog.dismiss();
                        }
                        else
                        {
                            Toast.makeText(getBaseContext(),res.getMessage(),Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();
                        }

                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t)
                    {

                    }
                });


            }
        });
        imgbtnRegisterCancel = findViewById(R.id.imgbtn_register_cancel);


        spnrCountries = findViewById(R.id.spnrCountries);


        //spnrCountries.setAdapter(this);
        spnrCountries.setOnItemSelectedListener(this);


        spnrGender = findViewById(R.id.spnrGender);

        //spnrGender.setAdapter(this);
        spnrGender.setOnItemSelectedListener(this);

        Call<RegisterDataResponse> call = Global.client.GetRegisterInformation();
        call.enqueue(new Callback<RegisterDataResponse>()
        {
            @Override
            public void onResponse(Call<RegisterDataResponse> call, Response<RegisterDataResponse> response)
            {
                RegisterDataResponse registerData = response.body();
                countries = registerData.getLstCountry();
                genders = registerData.getLstGender();

                spnrCountriesAdapter = new ArrayAdapter(RegisterActivity.this,R.layout.spinner,countries);
                spnrGendersAdapter = new ArrayAdapter(RegisterActivity.this, R.layout.spinner,genders);
                spnrCountries.setAdapter(spnrCountriesAdapter);
                spnrGender.setAdapter(spnrGendersAdapter);

                spnrGender.setSelection(0);
                spnrCountries.setSelection(0);
            }

            @Override
            public void onFailure(Call<RegisterDataResponse> call, Throwable t) {
                Toast.makeText(getBaseContext(),"Connection Failed",Toast.LENGTH_SHORT).show();


            }
        });



    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId())
        {
            case R.id.spnrCountries:
            {
                registerRequest.setCountry(countries.get(position).getId());
            }
            case R.id.spnrGender:
            {
                registerRequest.setGender(genders.get(position).getId());
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    boolean allRequiredFieldsFilled()
    {
        boolean res = false;
        if(etUserName.getText().toString().equals(""))
        {
            etUserName.setError("User name is required");
            return false;
        }
        if(etPassword.getText().toString().equals(""))
        {
            etPassword.setError("Password is required");
            return false;
        }
        if(etPasswordConfirmation.getText().toString().equals(""))
        {
            etPasswordConfirmation.setError("confirm your password");
            return false;
        }
        if(etEmail.getText().toString().equals(""))
        {
            etEmail.setError("e-mail is required");
            return false;

        }
        if(etAge.getText().toString().equals(""))
        {
            etAge.setError("Age is required");
            return false;
        }
        return true;


    }
    boolean checkPasswordsMatching()
    {
        if(etPassword.getText().toString().equals(etPasswordConfirmation.getText().toString()))
            return true;
        else
        {
            etPasswordConfirmation.setError("passwords don't match");
            return false;
        }
    }
}

