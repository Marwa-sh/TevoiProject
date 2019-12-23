package com.tevoi.tevoi.filter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tevoi.tevoi.R;
import com.tevoi.tevoi.SideMenu;
import com.tevoi.tevoi.accordion.MultiCheckArtistViewHolder;
import com.tevoi.tevoi.accordion.MultiCheckMainTopic;
import com.tevoi.tevoi.expand.MainTopicViewHolder;
import com.tevoi.tevoi.model.CategoryFilter;
import com.thoughtbot.expandablecheckrecyclerview.CheckableChildRecyclerViewAdapter;
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class SwitchMainTopicAdapter extends
    CheckableChildRecyclerViewAdapter<MainTopicViewHolder, SwitchCategoryViewHolder>
{
    private SideMenu activity;

    public SwitchMainTopicAdapter(List<MultiCheckMainTopic> groups, SideMenu activity)
  {
    super(groups);
    this.activity = activity;
  }

  @Override
  public SwitchCategoryViewHolder onCreateCheckChildViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_switch_category, parent, false);
    return new SwitchCategoryViewHolder(view);
  }

  @Override
  public void onBindCheckChildViewHolder(SwitchCategoryViewHolder holder, int position,
      CheckedExpandableGroup group, int childIndex)
  {
      final CategoryFilter category = (CategoryFilter) group.getItems().get(childIndex);
      //holder.setCategoryName(category.getName());
      holder.initaiteCategory(category.getName(), category.isFavorite(),category.getId(), activity);
  }

  @Override
  public MainTopicViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_genre, parent, false);
    return new MainTopicViewHolder(view, (List<MultiCheckMainTopic>)this.getGroups());
  }

  @Override
  public void onBindGroupViewHolder(MainTopicViewHolder holder, int flatPosition,
      ExpandableGroup group)
  {
      holder.initaiteMainTopic(group, activity);
    //holder.setGenreTitle(group);
    //holder.setSwitchStatus(group);
  }
}
