
package com.tevoi.tevoi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.adapter.MainTopicAdapter;
import com.tevoi.tevoi.adapter.SubscripedPartnersAdapter;
import com.tevoi.tevoi.adapter.TrackTypeAdapter;
import com.tevoi.tevoi.model.CategoryObject;
import com.tevoi.tevoi.model.CategoryResponseList;
import com.tevoi.tevoi.model.GetSubscripedPartnersResponse;
import com.tevoi.tevoi.model.MainTopic;
import com.tevoi.tevoi.model.SubscipedPartnersObject;
import com.tevoi.tevoi.model.TrackTypeObject;
import com.tevoi.tevoi.model.UserFiltersResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v7.widget.RecyclerView.VERTICAL;

public class FilterFragment extends Fragment {
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

    ImageButton btnClearFilter;

    SideMenu activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_filter, container, false);
        activity = (SideMenu) getActivity();
        btnClearFilter = rootView.findViewById(R.id.btn_clear_filter);


        //categoriesRecyclerView = rootView.findViewById(R.id.categories_recycler_view);
        subscripedPartnersRecyclerView = rootView.findViewById(R.id.subscriped_partners);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        final LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        //categoriesRecyclerView.setLayoutManager(layoutManager2);

        final LinearLayoutManager layoutManagerPartners = new LinearLayoutManager(getContext());
        layoutManagerPartners.setOrientation(LinearLayoutManager.VERTICAL);
        subscripedPartnersRecyclerView.setLayoutManager(layoutManagerPartners);

        //trackTypeRecyclerView = rootView.findViewById(R.id.track_type_recycler_view);
        //trackTypeRecyclerView.setLayoutManager(layoutManager);
        //trackTypeAdapter = new TrackTypeAdapter(getContext());
        //trackTypeRecyclerView.setAdapter(trackTypeAdapter);
        DividerItemDecoration itemDecorTrackType = new DividerItemDecoration(getContext(), VERTICAL);
        //trackTypeRecyclerView.addItemDecoration(itemDecorTrackType);

        //TrackTypeObject[] trackTypeObjects = trackTypeAdapter.getTrackTypeObjects();

        btnClearFilter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

            }
        });

        final SideMenu activity = (SideMenu) getActivity();

        activity.mProgressDialog.setMessage("Loading"); activity.mProgressDialog.show();

        Call<UserFiltersResponse> call = Global.client.GetUserFilters();
        call.enqueue(new Callback<UserFiltersResponse>()
        {
            @Override
            public void onResponse(Call<UserFiltersResponse> call, Response<UserFiltersResponse> response)
            {
                UserFiltersResponse filters = response.body();

               /* adapterCategories = new CategoriesAdapter(filters.getMainTopicList(), activity);
                categoriesRecyclerView.setAdapter(adapterCategories);*/
                DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), VERTICAL);
                //categoriesRecyclerView.addItemDecoration(itemDecor);

                adapterPartners = new SubscripedPartnersAdapter(filters.getSubscripedPartners(),activity);
                subscripedPartnersRecyclerView.setAdapter(adapterPartners);
                DividerItemDecoration itemDecorPartner = new DividerItemDecoration(getContext(), VERTICAL);
                subscripedPartnersRecyclerView.addItemDecoration(itemDecor);

                activity.mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserFiltersResponse> call, Throwable t) {
                activity.mProgressDialog.dismiss();
            }
        });


        /*Call<CategoryResponseList> call = Global.client.GetCategoriesFilters();
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
        });*/
       /* activity.mProgressDialog.setMessage("Loading"); activity.mProgressDialog.show();

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
        });*/

        //mProgressDialog.dismiss();
        return rootView;
    }
}