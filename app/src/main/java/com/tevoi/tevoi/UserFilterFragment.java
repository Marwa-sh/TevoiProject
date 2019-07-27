
package com.tevoi.tevoi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.accordion.MultiCheckMainTopic;
import com.tevoi.tevoi.accordion.MultiCheckMainTopicAdapter;
import com.tevoi.tevoi.adapter.MainTopicAdapter;
import com.tevoi.tevoi.adapter.SubscripedPartnersAdapter;
import com.tevoi.tevoi.model.CategoryFilter;
import com.tevoi.tevoi.model.CategoryObject;
import com.tevoi.tevoi.model.SubscipedPartnersObject;
import com.tevoi.tevoi.model.UserFiltersResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v7.widget.RecyclerView.VERTICAL;

public class UserFilterFragment extends Fragment
{
    private MultiCheckMainTopicAdapter adapter;
    SideMenu activity;

    View rootView;
    List<CategoryObject> categoryObjectList;
    List<SubscipedPartnersObject> subscipedPartners;

    //RecyclerView categoriesRecyclerView;
    RecyclerView mainTopicsRecyclerView;
    RecyclerView subscripedPartnersRecyclerView;

    //CategoriesAdapter adapterCategories;
    MainTopicAdapter adapterMainTopics;
    SubscripedPartnersAdapter adapterPartners;
    //TrackTypeAdapter trackTypeAdapter;


    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<MultiCheckMainTopic> mainTopics = new ArrayList<MultiCheckMainTopic>();
    ImageButton btnClearFilter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_accordion, container, false);
        activity = (SideMenu) getActivity();


        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(activity);


        Button clear = (Button) rootView.findViewById(R.id.clear_button);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clearChoices();
            }
        });

        /*Button check = (Button) rootView.findViewById(R.id.check_first_child);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.checkChild(true, 0, 3);
            }
        });
*/
        mainTopics = new ArrayList<MultiCheckMainTopic>();
       activity.mProgressDialog.setMessage("Loading"); activity.mProgressDialog.show();

        Call<UserFiltersResponse> call = Global.client.GetUserFilters();
        call.enqueue(new Callback<UserFiltersResponse>()
        {
            @Override
            public void onResponse(Call<UserFiltersResponse> call, Response<UserFiltersResponse> response)
            {
                UserFiltersResponse filters = response.body();
                for (int i =0; i< filters.getMainTopicList().size(); i++)
                {
                    List<CategoryObject> categories = filters.getMainTopicList().get(i).CategoriesList;
                    List<CategoryFilter> ct = new ArrayList<>();
                    for(int k =0; k < categories.size(); k++)
                    {
                        CategoryFilter f = new CategoryFilter(categories.get(k).getName(), categories.get(k).isFilterValue());
                        ct.add(f);
                    }
                    MultiCheckMainTopic temp = new MultiCheckMainTopic(filters.getMainTopicList().get(i).Name, ct, 1);
                    mainTopics.add(temp);
                }

                adapter = new MultiCheckMainTopicAdapter(mainTopics);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);


               /* adapterCategories = new CategoriesAdapter(filters.getMainTopicList(), activity);
                categoriesRecyclerView.setAdapter(adapterCategories);*/
                DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), VERTICAL);
                //categoriesRecyclerView.addItemDecoration(itemDecor);

                /*adapterPartners = new SubscripedPartnersAdapter(filters.getSubscripedPartners(),activity);
                subscripedPartnersRecyclerView.setAdapter(adapterPartners);
                DividerItemDecoration itemDecorPartner = new DividerItemDecoration(getContext(), VERTICAL);
                subscripedPartnersRecyclerView.addItemDecoration(itemDecor);*/

                activity.mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserFiltersResponse> call, Throwable t) {
                activity.mProgressDialog.dismiss();
            }
        });
        return rootView;
    }
}