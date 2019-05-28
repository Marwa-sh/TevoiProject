
package com.tevoi.tevoi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.adapter.CategoriesAdapter;
import com.tevoi.tevoi.adapter.SubscripedPartnersAdapter;
import com.tevoi.tevoi.adapter.TrackTypeAdapter;
import com.tevoi.tevoi.model.CategoryObject;
import com.tevoi.tevoi.model.CategoryResponseList;
import com.tevoi.tevoi.model.GetSubscripedPartnersResponse;
import com.tevoi.tevoi.model.SubscipedPartnersObject;
import com.tevoi.tevoi.model.TrackTypeObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v7.widget.RecyclerView.VERTICAL;

public class FilterFragment extends Fragment {
    View rootView;
    List<CategoryObject> categoryObjectList;
    List<SubscipedPartnersObject> subscipedPartners;

    RecyclerView trackTypeRecyclerView;
    RecyclerView categoriesRecyclerView;
    RecyclerView subscripedPartnersRecyclerView;

    CategoriesAdapter adapterCategories;
    SubscripedPartnersAdapter adapterPartners;
    TrackTypeAdapter trackTypeAdapter;

    SideMenu activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_filter, container, false);
        activity = (SideMenu) getActivity();

        categoriesRecyclerView= rootView.findViewById(R.id.categories_recycler_view);
        subscripedPartnersRecyclerView = rootView.findViewById(R.id.subscriped_partners);

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
        trackTypeAdapter = new TrackTypeAdapter(getContext());
        trackTypeRecyclerView.setAdapter(trackTypeAdapter);
        DividerItemDecoration itemDecorTrackType = new DividerItemDecoration(getContext(), VERTICAL);
        trackTypeRecyclerView.addItemDecoration(itemDecorTrackType);

        TrackTypeObject[] trackTypeObjects = trackTypeAdapter.getTrackTypeObjects();




        activity.mProgressDialog.setMessage("Loading"); activity.mProgressDialog.show();

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

                activity.mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CategoryResponseList> call, Throwable t) {
                activity.mProgressDialog.dismiss();
            }
        });
        activity.mProgressDialog.setMessage("Loading"); activity.mProgressDialog.show();

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
                activity.mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetSubscripedPartnersResponse> call, Throwable t) {
                activity.mProgressDialog.dismiss();
            }
        });

        //mProgressDialog.dismiss();
        return rootView;
    }
}