
package com.ebridge.tevoi;

import android.app.ProgressDialog;
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

import com.ebridge.tevoi.Utils.Global;
import com.ebridge.tevoi.adapter.CategoriesAdapter;
import com.ebridge.tevoi.adapter.SubscripedPartnersAdapter;
import com.ebridge.tevoi.model.CategoryObject;
import com.ebridge.tevoi.model.CategoryResponseList;
import com.ebridge.tevoi.model.GetSubscripedPartnersResponse;
import com.ebridge.tevoi.model.SubscipedPartnersObject;
import com.ebridge.tevoi.rest.ApiClient;
import com.ebridge.tevoi.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v7.widget.RecyclerView.HORIZONTAL;
import static android.support.v7.widget.RecyclerView.VERTICAL;

public class FilterFragment extends Fragment {
    ProgressDialog mProgressDialog;
    View rootView;
    List<CategoryObject> categoryObjectList;
    List<SubscipedPartnersObject> subscipedPartners;

    RecyclerView trackTypeRecyclerView;
    RecyclerView categoriesRecyclerView;
    RecyclerView subscripedPartnersRecyclerView;

    CategoriesAdapter adapterCategories;
    SubscripedPartnersAdapter adapterPartners;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_filter, container, false);
        categoriesRecyclerView= rootView.findViewById(R.id.categories_recycler_view);
        subscripedPartnersRecyclerView = rootView.findViewById(R.id.subscriped_partners);

        mProgressDialog = new ProgressDialog(getActivity());

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        final LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        categoriesRecyclerView.setLayoutManager(layoutManager2);

        final LinearLayoutManager layoutManagerPartners = new LinearLayoutManager(getContext());
        layoutManagerPartners.setOrientation(LinearLayoutManager.VERTICAL);
        subscripedPartnersRecyclerView.setLayoutManager(layoutManagerPartners);

        trackTypeRecyclerView = rootView.findViewById(R.id.track_type_recycler_view);
        trackTypeRecyclerView.setLayoutManager(layoutManager);

        mProgressDialog.setMessage("Loading");
        mProgressDialog.show();

        Call<CategoryResponseList> call = Global.client.GetCategoriesFilters();
        call.enqueue(new Callback<CategoryResponseList>() {
            @Override
            public void onResponse(Call<CategoryResponseList> call, Response<CategoryResponseList> response) {
                CategoryResponseList categories = response.body();
                SideMenu activity = (SideMenu) getActivity();
                adapterCategories = new CategoriesAdapter(categories.getCategoriesList(), activity);
                categoriesRecyclerView.setAdapter(adapterCategories);
                DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), VERTICAL);
                categoriesRecyclerView.addItemDecoration(itemDecor);
            }

            @Override
            public void onFailure(Call<CategoryResponseList> call, Throwable t) {

            }
        });

        Call<GetSubscripedPartnersResponse> callPartners = Global.client.GetSubscripedPartners();
        callPartners.enqueue(new Callback<GetSubscripedPartnersResponse>() {
            @Override
            public void onResponse(Call<GetSubscripedPartnersResponse> call, Response<GetSubscripedPartnersResponse> response) {
                GetSubscripedPartnersResponse partners = response.body();
                SideMenu activity = (SideMenu) getActivity();
                adapterPartners = new SubscripedPartnersAdapter(partners.getSubscripedPartners(),activity);
                subscripedPartnersRecyclerView.setAdapter(adapterPartners);
                DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), VERTICAL);
                subscripedPartnersRecyclerView.addItemDecoration(itemDecor);
            }

            @Override
            public void onFailure(Call<GetSubscripedPartnersResponse> call, Throwable t) {

            }
        });

        mProgressDialog.dismiss();
        return rootView;
    }
}