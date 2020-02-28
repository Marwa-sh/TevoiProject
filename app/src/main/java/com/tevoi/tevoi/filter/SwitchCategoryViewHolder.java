package com.tevoi.tevoi.filter;

import androidx.appcompat.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tevoi.tevoi.R;
import com.tevoi.tevoi.SideMenu;
import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.accordion.MultiCheckMainTopic;
import com.tevoi.tevoi.model.CategoryFilter;
import com.tevoi.tevoi.model.IResponse;
import com.thoughtbot.expandablecheckrecyclerview.viewholders.CheckableChildViewHolder;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SwitchCategoryViewHolder extends CheckableChildViewHolder
{
    private TextView textCategory;
    private SwitchCompat childCheckedSwitchOld;
    private ImageView childCheckedSwitch;
    private boolean isChecked;
    private SideMenu activity;

    int id;

    public SwitchCategoryViewHolder(View itemView)
    {
        super(itemView);
        textCategory = (TextView) itemView.findViewById(R.id.list_item_switch_category_name);
        childCheckedSwitchOld = (SwitchCompat) itemView.findViewById(R.id.switch_category_old);

        childCheckedSwitch = (ImageView) itemView.findViewById(R.id.switch_category);
        childCheckedSwitch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                isChecked = !isChecked;
                if(isChecked)
                {
                    childCheckedSwitch.setImageResource(R.mipmap.golden_button_on);
                }
                else
                {
                    childCheckedSwitch.setImageResource(R.mipmap.grey_button_off);
                }

                Log.println(Log.DEBUG, "childSwitch is clicked=" + isChecked, "Done");

                Call<IResponse> call = Global.client.UpdateCategoryPreference(id);
                call.enqueue(new Callback<IResponse>()
                {
                    @Override
                    public void onResponse(Call<IResponse> call, Response<IResponse> response)
                    {
                        IResponse res = response.body();
                        Log.println(Log.DEBUG, "Result i = ", "Done");
                        activity.IsFilterChanged = true;

                        activity.storageManager.updateCategoryFilter(activity, id, isChecked);
                        /*isChecked = !isChecked;
                        if(isChecked)
                        {
                            childCheckedSwitch.setImageResource(R.mipmap.golden_button_on);
                        }
                        else
                        {
                            childCheckedSwitch.setImageResource(R.mipmap.grey_button_off);
                        }*/

                        /*if(isChecked)
                        {
                            for (int i = 0; i < mainTopic.getItems().size(); i++)
                            {
                                CategoryFilter t = (CategoryFilter) mainTopic.getItems().get(i);
                                t.setFavorite(false);
                            }
                            mainTopic.notifyAll();
                        }*/
                        /*if(activity != null)
                        {
                            // TODO: update just the main list tracks
                            activity.userFilterFragment.reloadFilter();
                            Log.println(Log.DEBUG, "Refresh ", "Refresh");
                        }*/
                    }
                    @Override
                    public void onFailure(Call<IResponse> call, Throwable t) {
                        Log.println(Log.DEBUG, "Result i = ", "fail");
                    }
                });


            }
        });
        /*this.childCheckedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked)
            {
                //change filtering state
                *//*int i = getPosition();
                final MultiCheckMainTopic mainTopic = groups.get(i);*//*
                Log.println(Log.DEBUG, "Test3 i = ", ""+ id +",");
               // Log.println(Log.DEBUG, "Test i = ", ""+ mainTopic.getId() +","+ mainTopic.getTitle());
                Call<IResponse> call = Global.client.UpdateCategoryPreference(id);
                call.enqueue(new Callback<IResponse>()
                {
                    @Override
                    public void onResponse(Call<IResponse> call, Response<IResponse> response)
                    {
                        IResponse res = response.body();
                        Log.println(Log.DEBUG, "Result i = ", "Done");
                    }
                    @Override
                    public void onFailure(Call<IResponse> call, Throwable t) {
                        Log.println(Log.DEBUG, "Result i = ", "fail");
                    }
                });
            }
        });*/
    }
    public  void initaiteCategory(String name, boolean filterValue, int id, SideMenu activity)
    {
        this.activity = activity;
        textCategory.setText(name);
        //childCheckedSwitch.setChecked(filterValue);
        this.id = id;
        isChecked = filterValue;
        Log.println(Log.DEBUG, " Initaite Category = ", "id");
        if(isChecked)
        {
            childCheckedSwitch.setImageResource(R.mipmap.golden_button_on);
        }
        else
        {
            childCheckedSwitch.setImageResource(R.mipmap.grey_button_off);
        }
    }

    @Override
    public Checkable getCheckable() {
        return childCheckedSwitchOld;
    }

    public void setCategoryName(String categoryName) {
        textCategory.setText(categoryName);
    }
}
