package com.tevoi.tevoi.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tevoi.tevoi.R;
import com.tevoi.tevoi.model.TrackObject;

import java.util.List;
//import butterknife.BindView;
//import butterknife.ButterKnife;

public class PostRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    private List<TrackObject> tracks;

    public PostRecyclerAdapter(List<TrackObject> tracks) {
        this.tracks = tracks;
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       /* switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder( LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false));
            case VIEW_TYPE_LOADING:
                return new FooterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
            default:
                return null;
        }*/
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == tracks.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return tracks == null ? 0 : tracks.size();
    }

    public void add(TrackObject response) {
        tracks.add(response);
        notifyItemInserted(tracks.size() - 1);
    }

    public void addAll(List<TrackObject> postItems) {
        for (TrackObject response : postItems) {
            add(response);
        }
    }


    private void remove(TrackObject postItems) {
        int position = tracks.indexOf(postItems);
        if (position > -1) {
            tracks.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addLoading() {
        isLoaderVisible = true;
        add(new TrackObject());
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = tracks.size() - 1;
        TrackObject item = getItem(position);
        if (item != null) {
            tracks.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    TrackObject getItem(int position) {
        return tracks.get(position);
    }


    public class ViewHolder extends BaseViewHolder {
        //@BindView(R.id.textViewTitle)
        TextView textViewTitle;
       //@BindView(R.id.textViewDescription)
        TextView textViewDescription;


        ViewHolder(View itemView) {
            super(itemView);
            //ButterKnife.bind(this, itemView);
        }

        protected void clear() {

        }

        public void onBind(int position) {
            super.onBind(position);
            TrackObject item = tracks.get(position);


           // textViewTitle.setText(item.getTitle());
            //textViewDescription.setText(item.getDescription());
        }
    }

    public class FooterHolder extends BaseViewHolder {

        //@BindView(R.id.progressBar)
        ProgressBar mProgressBar;


        FooterHolder(View itemView) {
            super(itemView);
           // ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {

        }

    }

}
