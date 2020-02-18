package com.tevoi.tevoi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.Utils.HelperFunctions;
import com.tevoi.tevoi.Utils.MyStorage;
import com.tevoi.tevoi.model.CountryObject;
import com.tevoi.tevoi.model.GenderObject;
import com.tevoi.tevoi.model.RegisterDataResponse;
import com.tevoi.tevoi.model.RegisterRequest;
import com.tevoi.tevoi.model.RegisterResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeoutException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    ImageButton imgbtnRegisterCancel;
    Button imgbtnRegister;
    ImageView imgProfilePicture;
    TextView txtRegisterCancel;
    byte [] profile_image;
    static int GALLERY_REQUEST_CODE = 1;

    public ProgressDialog mProgressDialog;
    TextView txtErrorMessage;
    TextView txtErrorUppderMessage;
    Button btnRetry;
    LinearLayout formFileds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //region detect language
        MyStorage storageManager = new MyStorage();
        String language = storageManager.getLanguageUIPreference(this);
        if (language == null)
            language = "en";
        //Toast.makeText(this, "Lang " + language, Toast.LENGTH_SHORT).show();
        Global.UserUILanguage = language;
        Resources res = getBaseContext().getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(language)); // API 17+ only.
        // Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);

        // endregion

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

        mProgressDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setCancelable(false);

        txtErrorMessage = findViewById(R.id.txt_register_error_message);
        txtErrorUppderMessage = findViewById(R.id.txt_register_load_error_message);
        btnRetry = findViewById(R.id.error_btn_retry);
        formFileds = findViewById(R.id.linear_form_fields);

        etUserName = findViewById(R.id.et_register_user_name);
        etPassword = findViewById(R.id.et_register_password);
        etPasswordConfirmation = findViewById(R.id.et_register_confirm_password);
        etEmail = findViewById(R.id.et_register_email);
        etAge = findViewById(R.id.et_register_age);
        etOccupation = findViewById(R.id.et_register_occupation);
        imgProfilePicture = findViewById(R.id.img_upload_profile_picture);

        imgbtnRegisterCancel = findViewById(R.id.imgbtn_register_cancel);
        txtRegisterCancel = findViewById(R.id.txt_register_cancel);


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
                txtErrorUppderMessage.setVisibility(View.VISIBLE);
                txtErrorUppderMessage.setText(R.string.title_no_internet_connection);
                formFileds.setVisibility(View.INVISIBLE);
                btnRetry.setVisibility(View.VISIBLE);
            }
        });

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });


        /*imgProfilePicture.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        pickFromGallery();
                        *//*Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);*//*

                    }

                }
        );*/

        imgbtnRegister = findViewById(R.id.btn_Register);
        imgbtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterRequest request = new RegisterRequest();

                GenderObject genderObject= genders.get(spnrGender.getSelectedItemPosition());
                CountryObject countryObject= countries.get(spnrCountries.getSelectedItemPosition());
                request.setGender(genderObject.getId());


                    boolean status = allRequiredFieldsFilled();
                    if (status == true) {
                        if(checkPasswordsMatching() && validateEmail())
                        {
                            request.setCountry(countryObject.getId());
                            int age = Integer.parseInt(etAge.getText().toString());
                            request.setAge(age);
                            request.setEmail(etEmail.getText().toString());
                            request.setUserName(etUserName.getText().toString());
                            request.setPassword(etPassword.getText().toString());
                            request.setOccupation(etOccupation.getText().toString());

                            mProgressDialog.setMessage(getResources().getString(R.string.loader_msg));
                            mProgressDialog.show();

                            Call<RegisterResponse> call = Global.clientDnn.Register(request);
                            call.enqueue(new Callback<RegisterResponse>() {
                                @Override
                                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                                    RegisterResponse res = response.body();
                                    if (res.Number == 0) {
                                        Toast.makeText(getBaseContext(), res.getMessage(), Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        //setContentView(R.layout.fragment_login);
                                        mProgressDialog.dismiss();
                                    } else {
                                        Toast.makeText(getBaseContext(), res.getMessage(), Toast.LENGTH_LONG).show();
                                        mProgressDialog.dismiss();
                                    }
                                }

                                @Override
                                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                                    showErrorView(t);
                                    mProgressDialog.dismiss();
                                }
                            });
                        }
                    }

            }
        });

        imgbtnRegisterCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        txtRegisterCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
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
            etUserName.setError(getResources().getString(R.string.user_name_is_required));
            etUserName.requestFocus();
            return false;
        }
        if(etEmail.getText().toString().equals(""))
        {
            etEmail.setError(getResources().getString(R.string.e_mail_is_required));
            etEmail.requestFocus();
            return false;

        }
        if(etPassword.getText().toString().equals(""))
        {
            etPassword.setError(getResources().getString(R.string.password_is_required));
            etPassword.requestFocus();
            return false;
        }
        if(etPasswordConfirmation.getText().toString().equals(""))
        {
            etPasswordConfirmation.setError(getResources().getString(R.string.confirm_password));
            etPasswordConfirmation.requestFocus();
            return false;
        }
        if(etAge.getText().toString().equals(""))
        {
            etAge.setError(getResources().getString(R.string.age_is_required));
            etAge.requestFocus();
            return false;
        }
        if(etOccupation.getText().toString().equals(""))
        {
            etOccupation.setError(getResources().getString(R.string.occupation_is_required));
            etOccupation.requestFocus();
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
            etPasswordConfirmation.setError(getResources().getString(R.string.passwords_dont_match));
            etPasswordConfirmation.requestFocus();
            return false;
        }
    }

    boolean validateEmail()
    {
        String email = etEmail.getText().toString();
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if(email.matches(regex))
            return  true;
        else
        {
            etEmail.setError(getResources().getString(R.string.invalide_email_format));
            etEmail.requestFocus();
            return  false;
        }
    }

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode)
            {
                case 1:
                {   //data.getData return the content URI for the selected Image
                    Uri selectedImage = data.getData();
                    imgProfilePicture.setImageURI(selectedImage);
                    break;

                }
            }

//        if (resultCode == Activity.RESULT_OK)
//        {
//            if (data == null) {
//                //Display an error
//                return;
//            }
//            try
//            {
//                InputStream inputStream = getBaseContext().getContentResolver().openInputStream(data.getData());
//                //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
//                inputStream.read(profile_image);
//
//                // input stream
//                InputStream in = new ByteArrayInputStream("Techie Delight"
//                        .getBytes(StandardCharsets.UTF_8));
//                // byte array
//                byte[]  bytes = toByteArray(inputStream);
//                System.out.println(new String(bytes));
//                //Toast.makeText(this, "Marwaaaaa" + bytes.length, Toast.LENGTH_SHORT).show();
//            }
//            catch (Exception exc)
//            {
//            }
//        }
//        if (requestCode == 1)
//            if (resultCode == Activity.RESULT_OK) {
//                Uri selectedImage = data.getData();
//
//                String filePath = getPath(selectedImage);
//                String file_extn = filePath.substring(filePath.lastIndexOf(".") + 1);
//                image_name_tv.setText(filePath);
//
//                try {
//                    if (file_extn.equals("img") || file_extn.equals("jpg") || file_extn.equals("jpeg") || file_extn.equals("gif") || file_extn.equals("png")) {
//                        //FINE
//                    } else {
//                        //NOT IN REQUIRED FORMAT
//                    }
//                } catch (FileNotFoundException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
    }*/
    /*public String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        imagePath = cursor.getString(column_index);

        return cursor.getString(column_index);
    }*/
    public static byte[] toByteArray(InputStream in) throws IOException {

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len;

        // read bytes from the input stream and store them in buffer
        while ((len = in.read(buffer)) != -1) {
            // write bytes from the buffer into output stream
            os.write(buffer, 0, len);
        }

        return os.toByteArray();
    }


    // region helper image functions
    private void pickFromGallery()
    {
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }


    private void uploadToServer(String filePath) {

        //Create a file object using file path
        File file = new File(filePath);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("upload", file.getName(), fileReqBody);
        //Create request body with text description and text media type
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
        //
        Call call = Global.client.uploadImage(part, description);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

            }
            @Override
            public void onFailure(Call call, Throwable t) {
            }
        });
    }

    // endregion


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1)
            {
                /*File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    bitmap=getResizedBitmap(bitmap, 400);
                    IDProf.setImageBitmap(bitmap);
                    BitMapToString(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                thumbnail=getResizedBitmap(thumbnail, 400);
                //Log.w("path of imgfrom gallery", picturePath+"");
                imgProfilePicture.setImageBitmap(thumbnail);
                BitMapToString(thumbnail);
            }
        }
    }
    private String Document_img1="";

    public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
        return Document_img1;
    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


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

        if (!HelperFunctions.isNetworkConnected(RegisterActivity.this))
        {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }
    //endregion
}

