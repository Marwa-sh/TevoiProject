package com.tevoi.tevoi.accordion;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.tevoi.tevoi.R;
import com.tevoi.tevoi.SideMenu;

import java.util.ArrayList;
import java.util.List;

public class MultiCheckActivity extends Fragment
{

  private MultiCheckMainTopicAdapter adapter;
  SideMenu activity;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View rootView = inflater.inflate(R.layout.fragment_accordion, container, false);
    activity = (SideMenu) getActivity();

    RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
    LinearLayoutManager layoutManager = new LinearLayoutManager(activity);

    List<MultiCheckMainTopic> mainTopics = new ArrayList<MultiCheckMainTopic>();

    adapter = new MultiCheckMainTopicAdapter(mainTopics);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter);

    ImageButton clear = (ImageButton) rootView.findViewById(R.id.clear_button);
    clear.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        adapter.clearChoices();
      }
    });

    /*Button check = (Button) rootView.findViewById(R.id.check_first_child);
    check.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        adapter.checkChild(true, 0, 3);
      }
    });*/

    return  rootView;
  }

  /*@Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    adapter.onSaveInstanceState(outState);
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    adapter.onRestoreInstanceState(savedInstanceState);
  }*/
}
