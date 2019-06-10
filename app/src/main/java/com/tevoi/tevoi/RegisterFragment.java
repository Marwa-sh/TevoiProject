package com.tevoi.tevoi;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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

public class RegisterFragment  extends Fragment implements SpinnerAdapter, AdapterView.OnItemSelectedListener {

    View rootView;
    Spinner spnrCountries, spnrGender;
    List<CountryObject> countries;
    List<GenderObject> genders;
    SideMenu activity;
    ArrayAdapter spnrCountriesAdapter,spnrGendersAdapter;
    RegisterRequest registerRequest = new RegisterRequest();
    EditText etUserName, etPassword, etPasswordConfirmation, etOccupation, etAge, etEmail;
    ImageButton imgbtnRegister,imgbtnRegisterCancel;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.activity_register,container,false);
        activity=(SideMenu) getActivity();

        etUserName=rootView.findViewById(R.id.et_register_user_name);
        etPassword= rootView.findViewById(R.id.et_register_password);
        etPasswordConfirmation=rootView.findViewById(R.id.et_register_confirm_password);
        etEmail=rootView.findViewById(R.id.et_register_email);
        etAge=rootView.findViewById(R.id.et_register_age);
        etOccupation =rootView.findViewById(R.id.et_register_occupation);

        imgbtnRegister=rootView.findViewById(R.id.imgbtn_register);
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


                Call<RegisterResponse> call=Global.client.Register(request);
                call.enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        RegisterResponse res = response.body();
                        if(res.Number==0)
                            Toast.makeText(getContext(),res.getMessage(),Toast.LENGTH_SHORT).show();
                        else
                        {
                            Toast.makeText(getContext(),res.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    }
                });


            }
        });
        imgbtnRegisterCancel = rootView.findViewById(R.id.imgbtn_register_cancel);



        spnrCountries = rootView.findViewById(R.id.spnrCountries);


        //spnrCountries.setAdapter(this);
        spnrCountries.setOnItemSelectedListener(this);


        spnrGender=rootView.findViewById(R.id.spnrGender);

        //spnrGender.setAdapter(this);
        spnrGender.setOnItemSelectedListener(this);

        Call<RegisterDataResponse> call = Global.client.GetRegisterInformation();
        call.enqueue(new Callback<RegisterDataResponse>() {
            @Override
            public void onResponse(Call<RegisterDataResponse> call, Response<RegisterDataResponse> response) {
                RegisterDataResponse registerData = response.body();
                countries = registerData.getLstCountry();
                genders = registerData.getLstGender();

                spnrCountriesAdapter = new ArrayAdapter(activity,R.layout.spinner,countries);
                spnrGendersAdapter = new ArrayAdapter(activity, R.layout.spinner,genders);
                spnrCountries.setAdapter(spnrCountriesAdapter);
                spnrGender.setAdapter(spnrGendersAdapter);

                spnrGender.setSelection(0);
                spnrCountries.setSelection(0);
            }

            @Override
            public void onFailure(Call<RegisterDataResponse> call, Throwable t) {
                Toast.makeText(getContext(),"Connection Failed",Toast.LENGTH_SHORT).show();


            }
        });


        // Inflate the layout for this fragment
        return  rootView;
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

