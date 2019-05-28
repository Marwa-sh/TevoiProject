package com.tevoi.tevoi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tevoi.tevoi.R;

import java.util.ArrayList;

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
        TextView textView = v.findViewById(R.id.txtListItemName);

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
