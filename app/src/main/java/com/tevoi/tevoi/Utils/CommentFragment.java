package com.tevoi.tevoi.Utils;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tevoi.tevoi.R;
import com.tevoi.tevoi.SideMenu;
import com.tevoi.tevoi.adapter.CommentsAdapter;
import com.tevoi.tevoi.model.CommentObject;
import com.tevoi.tevoi.model.IResponse;
import com.tevoi.tevoi.model.TrackCommentRequest;
import com.tevoi.tevoi.model.TrackCommentResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CommentFragment extends Fragment
{
    ArrayList<CommentObject> TrackComments= new ArrayList<CommentObject>();
    private  int TrackId;
    CommentsAdapter adapter ;
    RecyclerView recyclerView;
    View rootView;
    ProgressBar progressBar;

      public static CommentFragment newInstance(int trackId) {
          CommentFragment f = new CommentFragment();
          // Supply num input as an argument.
          Bundle args = new Bundle();
          args.putInt("TrackId", trackId);
          f.setArguments(args);

          return f;
      }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
            TrackId = getArguments().getInt("TrackId");
        else
            TrackId = 0;


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_comment, container,
                false);
        final SideMenu activity = (SideMenu)getActivity();
        progressBar = rootView.findViewById(R.id.loader_comments);
        progressBar.setVisibility(View.VISIBLE);
        ImageButton ib =  rootView.findViewById(R.id.addCommentBtn);
        if(activity.storageManager.getLanguageUIPreference(activity).equals("ar"))
        {
            ib.setScaleX(-1);
        }

        ImageView iv =  rootView.findViewById(R.id.imageView_close);
        iv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Do something else
                SideMenu activity = (SideMenu) getActivity();
                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                // Replace the contents of the container with the new fragment
                //TrackAddToList frag = new TrackAddToList();
                ft.replace(R.id.content_frame, activity.mediaPlayerFragment);
                // or ft.add(R.id.your_placeholder, new FooFragment());
                // Complete the changes added above
                activity.BackBtnAction();
                ft.commit();

            }
        });


        ib.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // check comment if not empty
                EditText et = rootView.findViewById(R.id.textComment);
                String comment = et.getText().toString();
                if(comment.equals(""))
                {
                    et.setError("Comment can't be empty!");
                    et.requestFocus();
                }
                else
                {
                    // call method in api to add comment then dimiss
                    Call<IResponse> call = Global.client.AddComment(TrackId, comment);
                    call.enqueue(new Callback<IResponse>(){
                        public void onResponse(Call<IResponse> call, Response<IResponse> response)
                        {
                            IResponse result = response.body();
                           //getDialog().dismiss();
                            Toast.makeText( getContext(), "Comment Added", Toast.LENGTH_SHORT).show();
                            ImageView iv = rootView.findViewById(R.id.imageView_close);
                            iv.callOnClick();
                        }
                        public void onFailure(Call<IResponse> call, Throwable t)
                        {
                            Toast.makeText( getContext(), "Error in comment add", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        recyclerView = rootView.findViewById(R.id.comments_recycler_View);
        TextView tv = rootView.findViewById(R.id.textComment);
        //tv.setText("TrackId = " + TrackId);

        //recyclerView = (RecyclerView) rootView.findViewById(R.id.comments_recycler_View);
        //recyclerView.setAdapter(adapter);

        /*TrackCommentRequest requestData = new TrackCommentRequest();
        requestData.TrackId = TrackId; requestData.Language = "ar-SY";
        requestData.UserId = 1;*/

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        Call<TrackCommentResponse> call = Global.client.GetTrackComments(TrackId);
        call.enqueue(new Callback<TrackCommentResponse>(){
            public void onResponse(Call<TrackCommentResponse> call, Response<TrackCommentResponse> response)
            {
                TrackCommentResponse comments = response.body();
                if(comments.Comments != null)
                {
                    TrackComments = comments.Comments;
                    adapter = new CommentsAdapter(TrackComments, rootView.getContext());
                    //recyclerView.setAdapter(adapter);
                    recyclerView = rootView.findViewById(R.id.comments_recycler_View);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            }
            public void onFailure(Call<TrackCommentResponse> call, Throwable t)
            {
                progressBar.setVisibility(View.GONE);
            }
        });

        //getDialog().setTitle("Comments");

        //GetComments

        return rootView;
    }
    public void addCommentClick(View v)
    {
        switch(v.getId())
        {
            // Just like you were doing
            case R.id.imgBtnOpenMapsApp :
            {
                // here we need to open maps app



                break;
            }
            case R.id.imgBtnCloseLocationFragment :
            {

                break;
            }
        }
    }
}
