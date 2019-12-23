package com.tevoi.tevoi.accordion;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tevoi.tevoi.R;
import com.tevoi.tevoi.SideMenu;
import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.model.CategoryObject;
import com.tevoi.tevoi.model.IResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>
{

    private List<CategoryObject> categories;
    private SideMenu activity;

    public CategoriesAdapter(List<CategoryObject> categories, SideMenu activity) {
        this.categories = categories;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_filter_instance,viewGroup,false);
        CategoriesAdapter.CategoryViewHolder holder = new CategoriesAdapter.CategoryViewHolder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int i) {
        CategoryObject category =  categories.get(i);
        categoryViewHolder.tvCategoryName.setText(category.getName());
        categoryViewHolder.checkBoxFilterState.setChecked(category.isFilterValue());
    }
    @Override
    public int getItemCount() {
        return categories.size();
    }
    public class CategoryViewHolder extends RecyclerView.ViewHolder
    {
        public  View view;
        public TextView tvCategoryName;
        public int Id;
        public SwitchCompat checkBoxFilterState;

        public CategoryViewHolder(@NonNull final View itemView) {
            super(itemView);
            this.tvCategoryName=itemView.findViewById(R.id.tv_filter_name);
            this.checkBoxFilterState=itemView.findViewById(R.id.checkbox_filter_state);

            this.checkBoxFilterState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,final boolean isChecked) {
                    activity.mProgressDialog.setMessage(activity.getResources().getString( R.string.loader_msg));
                    activity.mProgressDialog.show();
                    activity.mProgressDialog.setCancelable(false);

                    int i = getPosition();
                    final CategoryObject category = categories.get(i);

                    Call<IResponse> call = Global.client.UpdateCategoryPreference(category.getId());
                    call.enqueue(new Callback<IResponse>() {
                        @Override
                        public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                            IResponse res = response.body();
                            if(res.getNumber()==0)
                            {
                                category.setFilterValue(isChecked);
                                Toast.makeText(activity,res.getMessage(),Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(activity,res.getMessage(),Toast.LENGTH_LONG).show();
                            }
                            activity.mProgressDialog.dismiss();
                        }
                        @Override
                        public void onFailure(Call<IResponse> call, Throwable t) {
                            activity.mProgressDialog.dismiss();
                        }
                    });
                }
            });

        }
    }


}
