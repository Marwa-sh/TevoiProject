package com.tevoi.tevoi.accordion;

import android.service.autofill.FillEventHistory;

import com.tevoi.tevoi.model.CategoryFilter;
import com.tevoi.tevoi.model.CategoryObject;
import com.thoughtbot.expandablecheckrecyclerview.models.MultiCheckExpandableGroup;
import java.util.List;

public class MultiCheckMainTopic extends MultiCheckExpandableGroup
{
  private int iconResId;
  private int id;
  private boolean filterValue;
  private List<CategoryFilter> lstChildren;

  public MultiCheckMainTopic(int id, String title, List<CategoryFilter> items, boolean filtervalue,  int iconResId)
  {
    super(title, items);
    this.iconResId = iconResId;
    this.id = id;
    this.filterValue = filtervalue;
    this.lstChildren = items;
  }

  public int getIconResId() {
    return iconResId;
  }
  public int getId() {
        return id;
    }
    public  boolean getFilterValue()
    { return  filterValue;}



  public List<CategoryFilter> getLstChildren() {
    return lstChildren;
  }
}

