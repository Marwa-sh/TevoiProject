package com.tevoi.tevoi.accordion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tevoi.tevoi.R;
import com.tevoi.tevoi.expand.MainTopicViewHolder;
import com.tevoi.tevoi.model.CategoryFilter;
import com.tevoi.tevoi.model.CategoryObject;
import com.thoughtbot.expandablecheckrecyclerview.CheckableChildRecyclerViewAdapter;
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import java.util.List;

public class MultiCheckMainTopicAdapter extends
    CheckableChildRecyclerViewAdapter<MainTopicViewHolder, MultiCheckArtistViewHolder> {

  public MultiCheckMainTopicAdapter(List<MultiCheckMainTopic> groups)
  {
    super(groups);
  }

  @Override
  public MultiCheckArtistViewHolder onCreateCheckChildViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_multicheck_artist, parent, false);
    return new MultiCheckArtistViewHolder(view);
  }

  @Override
  public void onBindCheckChildViewHolder(MultiCheckArtistViewHolder holder, int position,
      CheckedExpandableGroup group, int childIndex) {
    final CategoryFilter artist = (CategoryFilter) group.getItems().get(childIndex);
    holder.setArtistName(artist.getName());
  }

  @Override
  public MainTopicViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_genre, parent, false);
    return new MainTopicViewHolder(view, (List<MultiCheckMainTopic>)this.getGroups());
  }

  @Override
  public void onBindGroupViewHolder(MainTopicViewHolder holder, int flatPosition,
      ExpandableGroup group) {
    holder.setGenreTitle(group);
  }
}
