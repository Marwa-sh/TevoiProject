package com.ebridge.tevoi.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ebridge.tevoi.R;
import com.ebridge.tevoi.Utils.CommentFragment;
import com.ebridge.tevoi.model.CommentObject;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private List<CommentObject> comments;
    private Context context;

    public CommentsAdapter(List<CommentObject> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }
    public CommentsAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_instance,viewGroup,false);
        CommentsAdapter.CommentViewHolder holder = new CommentsAdapter.CommentViewHolder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.CommentViewHolder viewHolder, int i) {
        CommentObject comment =  comments.get(i);
        viewHolder.tvCommentText.setText(comment.Text);
        viewHolder.id = comment.Id;

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
    class CommentViewHolder extends RecyclerView.ViewHolder
    {
        public  View view;
        TextView tvCommentText;
        long id;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            //this.view=itemView.findViewWithTag(R.id.track_row_layout);
            tvCommentText = itemView.findViewById(R.id.comment_text);
            //imgBtnPlay.setOnClickListener();
        }
    }
}
