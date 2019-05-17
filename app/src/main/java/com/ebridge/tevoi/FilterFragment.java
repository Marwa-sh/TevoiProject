
package com.ebridge.tevoi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ebridge.tevoi.adapter.CategoriesAdapter;
import com.ebridge.tevoi.model.CategoryObject;
import com.ebridge.tevoi.model.CategoryResponseList;
import com.ebridge.tevoi.rest.ApiClient;
import com.ebridge.tevoi.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v7.widget.RecyclerView.HORIZONTAL;
import static android.support.v7.widget.RecyclerView.VERTICAL;

public class FilterFragment extends Fragment {

    View rootView;
    List<CategoryObject> categoryObjectList;
    ApiInterface client;
    RecyclerView categoriesRecyclerView;
    RecyclerView trackTypeRecyclerView;
    CategoriesAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_filter, container, false);
        categoriesRecyclerView= rootView.findViewById(R.id.categories_recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        final LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        categoriesRecyclerView.setLayoutManager(layoutManager2);

        trackTypeRecyclerView = rootView.findViewById(R.id.track_type_recycler_view);
        trackTypeRecyclerView.setLayoutManager(layoutManager);

        client = ApiClient.getClient().create(ApiInterface.class);
        Call<CategoryResponseList> call = client.GetCategoriesFilters();
        call.enqueue(new Callback<CategoryResponseList>() {
            @Override
            public void onResponse(Call<CategoryResponseList> call, Response<CategoryResponseList> response) {
                CategoryResponseList categories = response.body();
                adapter = new CategoriesAdapter(categories.getCategoriesList(),getContext());
                categoriesRecyclerView.setAdapter(adapter);
                DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), VERTICAL);
                categoriesRecyclerView.addItemDecoration(itemDecor);
            }

            @Override
            public void onFailure(Call<CategoryResponseList> call, Throwable t) {

            }
        });


        return rootView;
    }
}