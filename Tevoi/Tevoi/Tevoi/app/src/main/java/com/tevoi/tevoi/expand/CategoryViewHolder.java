package com.tevoi.tevoi.expand;

import android.view.View;
import android.widget.TextView;

import com.tevoi.tevoi.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class CategoryViewHolder extends ChildViewHolder {

  private TextView childTextView;

  public CategoryViewHolder(View itemView) {
    super(itemView);
    childTextView = (TextView) itemView.findViewById(R.id.list_item_artist_name);
  }

  public void setCategoryName(String name) {
    childTextView.setText(name);
  }
}
