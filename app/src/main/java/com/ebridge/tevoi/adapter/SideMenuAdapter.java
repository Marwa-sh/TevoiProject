package com.ebridge.tevoi.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ebridge.tevoi.R;
import com.ebridge.tevoi.model.CommentObject;

import java.util.List;

public class SideMenuAdapter extends RecyclerView.Adapter<SideMenuAdapter.SideMenuItemViewHolder> {

    private List<String> menuItemsNames;
    private Context context;

    public SideMenuAdapter(List<String> menuItemsNames, Context context) {
        this.menuItemsNames = menuItemsNames;
        this.context = context;
    }

    public SideMenuAdapter.SideMenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.drawer_list_item, viewGroup, false);
        SideMenuAdapter.SideMenuItemViewHolder holder = new SideMenuAdapter.SideMenuItemViewHolder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SideMenuAdapter.SideMenuItemViewHolder viewHolder, int i) {
        String menuItem = menuItemsNames.get(i);
        viewHolder.textMenuItem.setText(menuItem);
        viewHolder.id = i;

    }

    @Override
    public int getItemCount() {
        return menuItemsNames.size();
    }

    class SideMenuItemViewHolder extends RecyclerView.ViewHolder {
        public View view;
        TextView textMenuItem;
        long id;

        public SideMenuItemViewHolder(@NonNull View itemView) {
            super(itemView);
            //this.view=itemView.findViewWithTag(R.id.track_row_layout);
            textMenuItem = itemView.findViewById(R.id.txtListItemName);
            //imgBtnPlay.setOnClickListener();
        }
    }
}