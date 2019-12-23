package com.tevoi.tevoi.expand;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tevoi.tevoi.R;
import com.tevoi.tevoi.accordion.MultiCheckMainTopic;
import com.tevoi.tevoi.expand.CategoryViewHolder;
import com.tevoi.tevoi.expand.MainTopicViewHolder;
import com.tevoi.tevoi.model.CategoryFilter;
import com.tevoi.tevoi.model.CategoryObject;
import com.tevoi.tevoi.model.MainTopic;
import com.tevoi.tevoi.model.MainTopicFilter;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class GenreAdapter extends ExpandableRecyclerViewAdapter<MainTopicViewHolder, CategoryViewHolder> {

  public GenreAdapter(List<? extends ExpandableGroup> groups) {
    super(groups);
  }

  @Override
  public MainTopicViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_genre, parent, false);
    return new MainTopicViewHolder(view, (List<MultiCheckMainTopic>)this.getGroups());
  }

  @Override
  public CategoryViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_artist, parent, false);
    return new CategoryViewHolder(view);
  }

  @Override
  public void onBindChildViewHolder(CategoryViewHolder holder, int flatPosition,
      ExpandableGroup group, int childIndex) {

    final CategoryFilter artist = ((MainTopicFilter) group).getItems().get(childIndex);
    holder.setCategoryName(artist.getName());
  }

  @Override
  public void onBindGroupViewHolder(MainTopicViewHolder holder, int flatPosition,
      ExpandableGroup group) {

    holder.setGenreTitle(group);
  }
}
