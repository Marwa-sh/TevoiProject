package com.tevoi.tevoi.model;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class MainTopicFilter extends ExpandableGroup<CategoryFilter>
{
    private int iconResId;
    public int Id;
    public String Name ;
    public boolean FilterValue ;

    public MainTopicFilter(String title, List<CategoryFilter> items, int iconResId) {
        super(title, items);
        this.iconResId = iconResId;
    }

    public int getIconResId() {
        return iconResId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MainTopic)) return false;

        MainTopicFilter genre = (MainTopicFilter) o;

        return getIconResId() == genre.getIconResId();

    }

    @Override
    public int hashCode() {
        return getIconResId();
    }
}

