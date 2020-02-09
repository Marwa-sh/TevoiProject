
package com.tevoi.tevoi;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.accordion.MultiCheckMainTopic;
import com.tevoi.tevoi.adapter.SubscripedPartnersAdapter;
import com.tevoi.tevoi.filter.SwitchMainTopicAdapter;
import com.tevoi.tevoi.model.CategoryFilter;
import com.tevoi.tevoi.model.CategoryObject;
import com.tevoi.tevoi.model.IResponse;
import com.tevoi.tevoi.model.MainTopic;
import com.tevoi.tevoi.model.SubscipedPartnersObject;
import com.tevoi.tevoi.model.UserFiltersResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

public class UserFilterFragment extends Fragment
{
    //private MultiCheckMainTopicAdapter adapter;
    private SwitchMainTopicAdapter adapter;
    SideMenu activity;

    View rootView;
    List<CategoryObject> categoryObjectList;
    List<SubscipedPartnersObject> subscipedPartners;

    //RecyclerView categoriesRecyclerView;
    RecyclerView mainTopicsRecyclerView;
    RecyclerView subscripedPartnersRecyclerView;

    //CategoriesAdapter adapterCategories;
    //MainTopicAdapter adapterMainTopics;
    SubscripedPartnersAdapter adapterPartners;
    //TrackTypeAdapter trackTypeAdapter;


    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<MultiCheckMainTopic> mainTopics = new ArrayList<MultiCheckMainTopic>();
    ImageButton btnClearFilter;
    ImageView btnShowHeardTracks;
    boolean isShowHeardTracks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_accordion, container, false);
        activity = (SideMenu) getActivity();

        List<MainTopic> lstMainTopics = activity.storageManager.loadListMainTopicFilter(activity);
        List<SubscipedPartnersObject> lstSubscripedPartners = activity.storageManager.loadListSubscripedPartnerFilter(activity);


        isShowHeardTracks = true;

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(activity);
        mainTopics = new ArrayList<MultiCheckMainTopic>();
        for (int i =0; i< lstMainTopics.size(); i++)
        {
            List<CategoryObject> categories = lstMainTopics.get(i).CategoriesList;
            List<CategoryFilter> ct = new ArrayList<>();
            for(int k =0; k < categories.size(); k++)
            {
                CategoryFilter f = new CategoryFilter(categories.get(k).getId(), categories.get(k).getName(), categories.get(k).isFilterValue());
                ct.add(f);
            }
            MultiCheckMainTopic temp =
                    new MultiCheckMainTopic(lstMainTopics.get(i).Id, lstMainTopics.get(i).Name, ct,
                            lstMainTopics.get(i).FilterValue , 1);
            mainTopics.add(temp);
        }
        adapter = new SwitchMainTopicAdapter(mainTopics, activity);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        btnShowHeardTracks =  (ImageView) rootView.findViewById(R.id.switch_show_history_listen);
        btnShowHeardTracks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Call<IResponse> call = Global.client.UpdateShowHeardTracks();
                call.enqueue(new Callback<IResponse>()
                {
                    @Override
                    public void onResponse(Call<IResponse> call, Response<IResponse> response)
                    {
                        IResponse result = response.body();
                        if(result.getNumber() ==0) {
                            isShowHeardTracks = !isShowHeardTracks;
                            activateDeatcivateShowHeardTracks(isShowHeardTracks);
                        }else
                        {
                            Toast.makeText(activity, result.getMessage(), Toast.LENGTH_SHORT).show();
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

        // region Subscriped Partners
        subscripedPartnersRecyclerView = (RecyclerView) rootView.findViewById(R.id.subscriped_partners);
        final LinearLayoutManager layoutManagerPartners = new LinearLayoutManager(getContext());
        layoutManagerPartners.setOrientation(LinearLayoutManager.VERTICAL);
        subscripedPartnersRecyclerView.setLayoutManager(layoutManagerPartners);

        adapterPartners = new SubscripedPartnersAdapter(lstSubscripedPartners, activity);
        subscripedPartnersRecyclerView.setAdapter(adapterPartners);
        DividerItemDecoration itemDecorPartner = new DividerItemDecoration(getContext(), VERTICAL);
        subscripedPartnersRecyclerView.addItemDecoration(itemDecorPartner);

        //endregion


        ImageButton clear = (ImageButton) rootView.findViewById(R.id.clear_button);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearFilters();
                //adapter.notifyDataSetChanged();
            }
        });

        /*
        Button check = (Button) rootView.findViewById(R.id.check_first_child);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.checkChild(true, 0, 3);
            }
        });
        */


        /*activity.mProgressDialog.setMessage(getResources().getString( R.string.loader_msg)); activity.mProgressDialog.show();

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
                        CategoryFilter f = new CategoryFilter(categories.get(k).getId(), categories.get(k).getName(), categories.get(k).isFilterValue());
                        ct.add(f);
                    }
                    MultiCheckMainTopic temp = new MultiCheckMainTopic(filters.getMainTopicList().get(i).Id, filters.getMainTopicList().get(i).Name, ct,filters.getMainTopicList().get(i).FilterValue , 1);
                    mainTopics.add(temp);
                }

                adapter = new SwitchMainTopicAdapter(mainTopics, activity);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

               *//* adapterCategories = new CategoriesAdapter(filters.getMainTopicList(), activity);
                categoriesRecyclerView.setAdapter(adapterCategories);*//*
                DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), VERTICAL);
                //categoriesRecyclerView.addItemDecoration(itemDecor);

                adapterPartners = new SubscripedPartnersAdapter(filters.getSubscripedPartners(),activity);
                subscripedPartnersRecyclerView.setAdapter(adapterPartners);
                DividerItemDecoration itemDecorPartner = new DividerItemDecoration(getContext(), VERTICAL);
                subscripedPartnersRecyclerView.addItemDecoration(itemDecorPartner);

                activity.mProgressDialog.dismiss();

                isShowHeardTracks = filters.IsShowHearedTracks;
                activateDeatcivateShowHeardTracks(filters.IsShowHearedTracks);

            }

            @Override
            public void onFailure(Call<UserFiltersResponse> call, Throwable t) {
                activity.mProgressDialog.dismiss();
            }
        });*/

        //Animation slide = AnimationUtils.loadAnimation(activity, R.anim.slide_down_from_top);
        //rootView.startAnimation(slide);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        //Animation slide = AnimationUtils.loadAnimation(activity, R.anim.slide_down_from_top);
        //view.startAnimation(slide);
    }


    public void clearFilters()
    {
        mainTopics = new ArrayList<MultiCheckMainTopic>();
        activity.mProgressDialog.setMessage(getResources().getString( R.string.loader_msg)); activity.mProgressDialog.show();

        Call<UserFiltersResponse> call = Global.client.ClearAndLoadUserFilters();
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
                        CategoryFilter f = new CategoryFilter(categories.get(k).getId(), categories.get(k).getName(), categories.get(k).isFilterValue());
                        ct.add(f);
                    }
                    MultiCheckMainTopic temp = new MultiCheckMainTopic(filters.getMainTopicList().get(i).Id, filters.getMainTopicList().get(i).Name, ct,filters.getMainTopicList().get(i).FilterValue , 1);
                    mainTopics.add(temp);
                }

                adapter = new SwitchMainTopicAdapter(mainTopics, activity);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

               /* adapterCategories = new CategoriesAdapter(filters.getMainTopicList(), activity);
                categoriesRecyclerView.setAdapter(adapterCategories);*/
                DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), VERTICAL);
                //categoriesRecyclerView.addItemDecoration(itemDecor);

                adapterPartners = new SubscripedPartnersAdapter(filters.getSubscripedPartners(),activity);
                subscripedPartnersRecyclerView.setAdapter(adapterPartners);
                DividerItemDecoration itemDecorPartner = new DividerItemDecoration(getContext(), VERTICAL);
                subscripedPartnersRecyclerView.addItemDecoration(itemDecorPartner);

                activity.mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserFiltersResponse> call, Throwable t) {
                activity.mProgressDialog.dismiss();
            }
        });

    }

    public  void reloadFilter()
    {
        mainTopics = new ArrayList<MultiCheckMainTopic>();
        activity.mProgressDialog.setMessage(getResources().getString( R.string.loader_msg)); activity.mProgressDialog.show();

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
                        CategoryFilter f = new CategoryFilter(categories.get(k).getId(), categories.get(k).getName(), categories.get(k).isFilterValue());
                        ct.add(f);
                    }
                    MultiCheckMainTopic temp = new MultiCheckMainTopic(filters.getMainTopicList().get(i).Id, filters.getMainTopicList().get(i).Name, ct,filters.getMainTopicList().get(i).FilterValue , 1);
                    mainTopics.add(temp);
                }

                adapter = new SwitchMainTopicAdapter(mainTopics, activity);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

               /* adapterCategories = new CategoriesAdapter(filters.getMainTopicList(), activity);
                categoriesRecyclerView.setAdapter(adapterCategories);*/
                DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), VERTICAL);
                //categoriesRecyclerView.addItemDecoration(itemDecor);

                adapterPartners = new SubscripedPartnersAdapter(filters.getSubscripedPartners(),activity);
                subscripedPartnersRecyclerView.setAdapter(adapterPartners);
                DividerItemDecoration itemDecorPartner = new DividerItemDecoration(getContext(), VERTICAL);
                subscripedPartnersRecyclerView.addItemDecoration(itemDecorPartner);

                activity.mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserFiltersResponse> call, Throwable t) {
                activity.mProgressDialog.dismiss();
            }
        });
    }

    public void activateDeatcivateShowHeardTracks(boolean value)
    {
        if(value)
        {
            btnShowHeardTracks.setImageResource(R.mipmap.golden_button_on);
        }
        else
        {
            btnShowHeardTracks.setImageResource(R.mipmap.grey_button_off);
        }
    }

}