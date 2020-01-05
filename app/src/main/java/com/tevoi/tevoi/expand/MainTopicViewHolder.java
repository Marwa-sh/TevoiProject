package com.tevoi.tevoi.expand;

import android.content.Intent;
import androidx.appcompat.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tevoi.tevoi.R;
import com.tevoi.tevoi.RegisterActivity;
import com.tevoi.tevoi.SideMenu;
import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.accordion.MultiCheckMainTopic;
import com.tevoi.tevoi.model.CategoryFilter;
import com.tevoi.tevoi.model.IResponse;
import com.tevoi.tevoi.model.MainTopic;
import com.tevoi.tevoi.model.MainTopicFilter;
import com.tevoi.tevoi.model.SubscipedPartnersObject;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class MainTopicViewHolder extends GroupViewHolder
{
  private TextView genreName;
  private ImageView arrow;
  private ImageView icon;
  //private SwitchCompat sw;
  private  ImageView sw;
  private boolean isChecked;
  private SideMenu activity;

  public MainTopicViewHolder(View itemView, final List<MultiCheckMainTopic> groups) {
    super(itemView);
    genreName = (TextView) itemView.findViewById(R.id.list_item_genre_name);
    arrow = (ImageView) itemView.findViewById(R.id.list_item_genre_arrow);
    sw = (ImageView) itemView.findViewById(R.id.list_item_genre_switch);
    sw.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              //change filtering state
              activity.mProgressDialog.setMessage(activity.getResources().getString( R.string.loader_msg));
              activity.mProgressDialog.show();
              activity.mProgressDialog.setCancelable(false);
              int i = getPosition();
              final MultiCheckMainTopic mainTopic = groups.get(i);

              Log.println(Log.DEBUG, "mainTopic i = ", ""+ mainTopic.getId() +","+ mainTopic.getTitle());
              Call<IResponse> call = Global.client.UpdateCategoryPreference(mainTopic.getId());
              call.enqueue(new Callback<IResponse>()
              {
                  @Override
                  public void onResponse(Call<IResponse> call, Response<IResponse> response)
                  {
                      IResponse res = response.body();
                      isChecked = !isChecked;
                      if(isChecked)
                      {
                          sw.setImageResource(R.mipmap.golden_button_on);
                      }
                      else
                      {
                          sw.setImageResource(R.mipmap.grey_button_off);
                      }
                      Log.println(Log.DEBUG, "Result i = ", "Done");

                      /*if(!isChecked)
                      {
                          for (int i = 0; i < mainTopic.getItems().size(); i++)
                          {
                              CategoryFilter t = (CategoryFilter) mainTopic.getItems().get(i);
                              t.setFavorite(false);

                          }

                      }*/

                      if(activity != null)
                      {
                          activity.userFilterFragment.reloadFilter();
                          Log.println(Log.DEBUG, "Refresh ", "Refresh");

                      }
                      activity.mProgressDialog.dismiss();
                  }
                  @Override
                  public void onFailure(Call<IResponse> call, Throwable t) {
                      Log.println(Log.DEBUG, "Result i = ", "fail");
                      activity.mProgressDialog.dismiss();
                  }
              });


          }
      });
      /*this.sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked)
          {
              //change filtering state
              int i = getPosition();
              final MultiCheckMainTopic mainTopic = groups.get(i);

              Log.println(Log.DEBUG, "Test i = ", ""+ mainTopic.getId() +","+ mainTopic.getTitle());
              Call<IResponse> call = Global.client.UpdateCategoryPreference(mainTopic.getId());
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
    //icon = (ImageView) itemView.findViewById(R.id.list_item_genre_icon);
  }

  public  void initaiteMainTopic(ExpandableGroup genre, SideMenu activity)
  {
      this.activity = activity;
     /* if (genre instanceof MainTopicFilter) {
          genreName.setText(genre.getTitle());
          sw.setChecked(((MultiCheckMainTopic) genre).getFilterValue());
      }*/
      if (genre instanceof MultiCheckMainTopic)
      {
          genreName.setText(genre.getTitle());
          isChecked = ((MultiCheckMainTopic) genre).getFilterValue();
          if(isChecked)
          {
              sw.setImageResource(R.mipmap.golden_button_on);
          }
          else
          {
              sw.setImageResource(R.mipmap.grey_button_off);
          }
          //sw.setChecked(((MultiCheckMainTopic) genre).getFilterValue());
      }
  }
  public void setSwitchStatus(ExpandableGroup genre)
  {
      /*if (genre instanceof MainTopicFilter) {
          sw.setText(genre.());
          //icon.setBackgroundResource(((MainTopic) genre).getIconResId());
      }*/
     /* if (genre instanceof MultiCheckMainTopic) {
          sw.setChecked(((MultiCheckMainTopic) genre).getFilterValue());
          //icon.setBackgroundResource(((MultiCheckMainTopic) genre).getIconResId());
      }*/

  }

  public void setGenreTitle(ExpandableGroup genre) {
    if (genre instanceof MainTopicFilter) {
      genreName.setText(genre.getTitle());
      //icon.setBackgroundResource(((MainTopic) genre).getIconResId());
    }
    if (genre instanceof MultiCheckMainTopic) {
      genreName.setText(genre.getTitle());
      //icon.setBackgroundResource(((MultiCheckMainTopic) genre).getIconResId());
    }
    /*if (genre instanceof SingleCheckGenre) {
      genreName.setText(genre.getTitle());
      icon.setBackgroundResource(((SingleCheckGenre) genre).getIconResId());
    }*/
  }

  @Override
  public void expand() {
    animateExpand();
  }

  @Override
  public void collapse() {
    animateCollapse();
  }

  private void animateExpand() {
    RotateAnimation rotate =
        new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
    rotate.setDuration(300);
    rotate.setFillAfter(true);
    arrow.startAnimation(rotate);

  }

  private void animateCollapse() {
    RotateAnimation rotate =
        new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
    rotate.setDuration(300);
    rotate.setFillAfter(true);
    arrow.startAnimation(rotate);
  }
}
