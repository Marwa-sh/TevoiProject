
package com.tevoi.tevoi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.model.FeedbackRequest;
import com.tevoi.tevoi.model.IResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackFragment extends Fragment {

    View rootView;
    EditText txtEmail;
    EditText txtName;
    EditText txtContent;
    ImageButton btnSendFeedback;
    ImageButton btnCancel;
    SideMenu activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_feedback, container, false);
        activity = (SideMenu)getActivity();

        txtName = rootView.findViewById(R.id.txt_username);
        txtEmail = rootView.findViewById(R.id.txt_email);
        txtContent = rootView.findViewById(R.id.txt_message);

        btnSendFeedback = rootView.findViewById(R.id.btn_send_feedback);
        btnCancel = rootView.findViewById(R.id.btn_cancel);

        btnSendFeedback.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String username = txtName.getText().toString();
                String email = txtEmail.getText().toString();
                String message = txtContent.getText().toString();

                //TODO:  validate email

                if(username.equals(""))
                {
                    txtName.setError("user name can't be empty!");
                    txtName.requestFocus();
                }
                else if(email.equals(""))
                {
                    txtEmail.setError("email can't be empty!");
                    txtEmail.requestFocus();
                }
                else if(message.equals(""))
                {
                    txtContent.setError("message can't be empty!");
                    txtContent.requestFocus();
                }
                else
                {
                    FeedbackRequest request = new FeedbackRequest();
                    request.setEmail(email); request.setMessage(message); request.setName(username);
                    activity.mProgressDialog.setMessage("Loading"); activity.mProgressDialog.show();

                    Call<IResponse> call = Global.client.SendFeedback(request);
                    call.enqueue(new Callback<IResponse>(){
                        public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                            IResponse result = response.body();

                            if(result.Number == 0)
                            {
                                activity.mProgressDialog.dismiss();
                                Toast.makeText(activity, result.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                activity.mProgressDialog.dismiss();
                                Toast.makeText(activity,result.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                        public void onFailure(Call<IResponse> call, Throwable t)
                        {
                            Toast.makeText(activity,"something went wrong", Toast.LENGTH_LONG).show();
                            activity.mProgressDialog.dismiss();
                        }
                    });
                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
        return  rootView;
    }
}
