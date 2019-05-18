package com.ebridge.tevoi.adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ebridge.tevoi.R;
import com.ebridge.tevoi.model.CategoryObject;

import java.util.ArrayList;
import java.util.List;

public class DrawerListAdapter extends ArrayAdapter<DrawerListItemObject>
{
    private final Context context;
    private final int layoutResourceId;
    private ArrayList<DrawerListItemObject> data = null;

    public DrawerListAdapter(Context context, int layoutResourceId, ArrayList<DrawerListItemObject> data)
    {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_list_item,parent,false);

        //ImageView imageView = (ImageView) v.findViewById(R.id.btnGoTo);
        TextView textView = (TextView) v.findViewById(R.id.txtListItemName);

        DrawerListItemObject choice = data.get(position);

        textView.setText(choice.getName());

        return v;
    }
}

/*public class DrawerListAdapter extends RecyclerView.Adapter<DrawerListAdapter.DrawerListItemViewHolder>
{

    private List<DrawerListItemObject> listItems;
    private Context context;

    public DrawerListAdapter(List<DrawerListItemObject> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public DrawerListAdapter.DrawerListItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.drawer_list_item,viewGroup,false);
        DrawerListAdapter.DrawerListItemViewHolder holder = new DrawerListAdapter.DrawerListItemViewHolder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DrawerListAdapter.DrawerListItemViewHolder drawerListItemViewHolder, int i) {
        DrawerListItemObject listItemObject =  listItems.get(i);
        drawerListItemViewHolder.tvListItemName.setText(listItemObject.getName());
    }
    @Override
    public int getItemCount() {
        return listItems.size();
    }
    public class DrawerListItemViewHolder extends RecyclerView.ViewHolder
    {
        public  View view;
        public TextView tvListItemName;
        public int Id;
        public ImageButton btnGoTo;

        public DrawerListItemViewHolder(@NonNull final View itemView) {
            super(itemView);
            this.tvListItemName=itemView.findViewById(R.id.txtListItemName);
            this.btnGoTo=itemView.findViewById(R.id.btnGoTo);

        }
    }


}*/
