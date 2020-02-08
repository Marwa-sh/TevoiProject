package com.tevoi.tevoi;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.adapter.NotificationTypeAdapter;
import com.tevoi.tevoi.adapter.SubscripedPartnersAdapter;
import com.tevoi.tevoi.model.CountryObject;
import com.tevoi.tevoi.model.GenderObject;
import com.tevoi.tevoi.model.IResponse;
import com.tevoi.tevoi.model.ListNotificationTypesResponse;
import com.tevoi.tevoi.model.RegisterDataResponse;
import com.tevoi.tevoi.model.RegisterRequest;
import com.tevoi.tevoi.model.ResetPasswordRequest;
import com.tevoi.tevoi.model.UserFiltersResponse;
import com.tevoi.tevoi.model.UserProfileRequest;
import com.tevoi.tevoi.model.UserProfileResponse;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

public class MyProfileFragment extends Fragment {

    View rootView;
    EditText etUserNameMyprofile, etChangePassword, etChangePasswordConfirmation, etOccupation, etAge, etEmail;
    ImageButton imgbtnEditUserName, imgbtnResetPassword, imgbtnEditOccupation,
            imgbtnEditAge, imgbtnEditEmail, imgbtnEditCountry, imgbtnEditGender, imgbtnSaveChanges;
    ArrayAdapter spnrCountriesAdapter, spnrGendersAdapter;
    Spinner spnrCountries, spnrGender;
    SideMenu activity;
    List<CountryObject> countries;
    List<GenderObject> genders;


    boolean allRequiredFieldsFilled() {
        boolean res = false;
        if (etUserNameMyprofile.getText().toString().equals("")) {
            etUserNameMyprofile.setError(getResources().getString(R.string.user_name_is_required));
            etUserNameMyprofile.requestFocus();
            return false;
        }
        if (etEmail.getText().toString().equals("")) {
            etEmail.setError(getResources().getString(R.string.e_mail_is_required));
            etEmail.requestFocus();
            return false;

        }

 /*       if (etAge.getText().toString().equals("")) {
            etAge.setError(getResources().getString(R.string.age_is_required));
            etAge.requestFocus();
            return false;
        }
        if (etOccupation.getText().toString().equals("")) {
            etOccupation.setError(getResources().getString(R.string.occupation_is_required));
            etOccupation.requestFocus();
            return false;
        }*/
        return true;
    }
    boolean PasswordFieldFilled() {
        if (etChangePassword.getText().toString().equals("")) {
            etChangePassword.setError(getResources().getString(R.string.password_should_not_be_empty));
            etChangePassword.requestFocus();
            return false;
        }
        else
        return true;
    }
    boolean checkPasswordsMatching() {
        if (etChangePassword.getText().toString().equals(etChangePasswordConfirmation.getText().toString()))
            return true;
        else {
            etChangePasswordConfirmation.setError(getResources().getString(R.string.passwords_dont_match));
            etChangePasswordConfirmation.requestFocus();
            return false;
        }
    }

    boolean validateEmail() {
        String email = etEmail.getText().toString();
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if (email.matches(regex))
            return true;
        else {
            etEmail.setError(getResources().getString(R.string.invalide_email_format));
            etEmail.requestFocus();
            return false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_profile, container, false);
        activity = (SideMenu) getActivity();

        etUserNameMyprofile = rootView.findViewById(R.id.et_myprofile_user_name);
        etEmail = rootView.findViewById(R.id.et_myprofile_email);
        etOccupation = rootView.findViewById(R.id.et_myprofile_occupation);
        etChangePassword = rootView.findViewById(R.id.et_myprofile_change_password);
        etChangePasswordConfirmation = rootView.findViewById(R.id.et_myprofile_confirm_change_password);
        etAge = rootView.findViewById(R.id.et_myprofile_age);

        spnrCountries = rootView.findViewById(R.id.spnr_myprofile_countries);
        spnrGender = rootView.findViewById(R.id.spnr_myprofile_Gender);

//        imgbtnEditUserName = rootView.findViewById(R.id.imgbtn_myprofile_edit_username);
//        imgbtnEditEmail = rootView.findViewById(R.id.imgbtn_myprofile_edit_email);
//        imgbtnEditAge = rootView.findViewById(R.id.imgbtn_myprofile_edit_age);
//        imgbtnEditGender = rootView.findViewById(R.id.imgbtn_myprofile_edit_gender);
//        imgbtnEditOccupation = rootView.findViewById(R.id.imgbtn_myprofile_edit_occupation);
//        imgbtnEditCountry = rootView.findViewById(R.id.imgbtn_myprofile_edit_country);
        imgbtnSaveChanges = rootView.findViewById(R.id.imgbtn_myprofile_save_changes);
        imgbtnResetPassword = rootView.findViewById(R.id.imgbtn_myprofile_reset_password);

        activity.mProgressDialog.setMessage(getResources().getString( R.string.loader_msg));
        activity.mProgressDialog.show();

        Call<UserProfileResponse> call = Global.client.GetUserProfile();
        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {

                UserProfileResponse profile = response.body();

                etUserNameMyprofile.setText(profile.UserName);
                etEmail.setText(profile.Email);
                etOccupation.setText(profile.Occupation);
                etAge.setText(""+profile.Age);
                //ask marwa
                countries = profile.getLstCountry();
                genders = profile.getLstGender();

                spnrCountriesAdapter = new ArrayAdapter(activity,R.layout.spinner,countries);
                spnrGendersAdapter = new ArrayAdapter(activity, R.layout.spinner,genders);

                spnrCountries.setAdapter(spnrCountriesAdapter);
                spnrGender.setAdapter(spnrGendersAdapter);

               /// int posiotion = spnrCountriesAdapter.getPosition(profile.Country);
                int indexOfCountry = 0;
                for (int i =0; i< countries.size(); i++)
                {
                    if(countries.get(i).getId() == profile.getCountry())
                    {
                        indexOfCountry = i;
                        break;
                    }
                }
                int indexOfGender = 0;
                for (int i =0; i< genders.size(); i++)
                {
                    if(genders.get(i).getId() == profile.getGender())
                    {
                        indexOfGender = i;
                        break;
                    }
                }
                spnrCountries.setSelection(indexOfCountry);
                spnrGender.setSelection(indexOfGender);

                activity.mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                activity.mProgressDialog.dismiss();
            }
        });

        imgbtnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileRequest newprofile = new UserProfileRequest();

                GenderObject genderObject = genders.get(spnrGender.getSelectedItemPosition());
                CountryObject countryObject = countries.get(spnrCountries.getSelectedItemPosition());


                boolean status = allRequiredFieldsFilled();
                if (status == true) {
                    if (validateEmail()) {
                        newprofile.setCountry(countryObject.getId());
                        newprofile.setGender(genderObject.getId());
                        int age = Integer.parseInt(etAge.getText().toString());
                        newprofile.setAge(age);
                        newprofile.setEmail(etEmail.getText().toString());
                        newprofile.setUserName(etUserNameMyprofile.getText().toString());
                        newprofile.setOccupation(etOccupation.getText().toString());

                        activity.mProgressDialog.setMessage(getResources().getString(R.string.loader_msg));
                        activity.mProgressDialog.setCancelable(false);
                        activity.mProgressDialog.show();

                        Call<IResponse> call = Global.client.UpdateUserProfile(newprofile);
                        call.enqueue(new Callback<IResponse>() {
                            @Override
                            public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                                IResponse res = response.body();
                                if (res.Number == 0) {

                                  Toast.makeText(activity, R.string.success_update_profile, Toast.LENGTH_SHORT).show();

                                    activity.mProgressDialog.dismiss();
                                } else {
                                    Toast.makeText(activity, res.getMessage(), Toast.LENGTH_LONG).show();
                                    activity.mProgressDialog.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<IResponse> call, Throwable t) {

                                activity.mProgressDialog.dismiss();
                            }
                        });
                    }
                }
            }
        });

         imgbtnResetPassword.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 ResetPasswordRequest resetpassword = new ResetPasswordRequest();
                 if (checkPasswordsMatching() && PasswordFieldFilled()) {
                     resetpassword.setPassword(etChangePassword.getText().toString());

                     activity.mProgressDialog.setMessage(getResources().getString(R.string.loader_msg));
                     activity.mProgressDialog.setCancelable(false);
                     activity.mProgressDialog.show();

                     Call<IResponse> call = Global.clientDnn.ResetPassword(resetpassword);
                     call.enqueue(new Callback<IResponse>() {
                         @Override
                         public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                             IResponse res = response.body();
                             if (res.Number == 0) {

                                 Toast.makeText(activity, R.string.success_reset_password, Toast.LENGTH_SHORT).show();

                                 activity.mProgressDialog.dismiss();
                             } else {
                                 Toast.makeText(activity, res.getMessage(), Toast.LENGTH_LONG).show();
                                 activity.mProgressDialog.dismiss();
                             }
                         }

                         @Override
                         public void onFailure(Call<IResponse> call, Throwable t) {
                             activity.mProgressDialog.dismiss();
                         }
                     });
                 }

                 }

         });


                    return rootView;

                }
            }

