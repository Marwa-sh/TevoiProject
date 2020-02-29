package com.tevoi.tevoi.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tevoi.tevoi.PartnerNameFragment;
import com.tevoi.tevoi.R;
import com.tevoi.tevoi.SideMenu;
import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.model.IResponse;
import com.tevoi.tevoi.model.LoadingVH;
import com.tevoi.tevoi.model.PartnerObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartnerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    // region pagination
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;
    private String errorMsg;

    private PaginationAdapterCallback mCallback;
    // endregion

    private List<PartnerObject> partners;
    private SideMenu activity;

    public PartnerAdapter(List<PartnerObject> partners, SideMenu activity){
        this.partners=partners;
        this.activity=activity;
    }
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        RecyclerView.ViewHolder viewHolder2 = null;

        switch (viewType)
        {
            case ITEM:
            {
                viewHolder2 = getViewHolder(viewGroup,inflater );
                break;
            }
            case LOADING:
            {
                try
                {
                    View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_progress, viewGroup, false);
                    viewHolder2 = new LoadingVH(v);
                }
                catch (Exception exc)
                {
                    viewHolder2 = new LoadingVH(new View(activity));
                }
            }
            break;
        }
        return viewHolder2;
        /*View row=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragmet_partner_instance,viewGroup,false);
        PartnerAdapter.PartnerViewHolder holder=new PartnerAdapter.PartnerViewHolder(row);
        return holder;*/
    }
    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup viewGroup, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;

        View row;
        row = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragmet_partner_instance, viewGroup, false);

        viewHolder = new PartnerAdapter.PartnerViewHolder(row);
        return viewHolder;

    }

    @Override
    public int getItemViewType(int position) {
        return (position == partners.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i)
    {
        switch (getItemViewType(i))
        {
            case ITEM:
                PartnerAdapter.PartnerViewHolder viewHolder = (PartnerAdapter.PartnerViewHolder) holder;

                PartnerObject partner=partners.get(i);
                viewHolder.tvPartnerName.setText(partner.getName());
                viewHolder.tvDescription.setText(partner.getDescripton());
                viewHolder.tvNumOfTracks.setText(activity.getResources().getString(R.string.number_of_tracks) + " : " + partner.getNumberOfTracks());
                viewHolder.hoverLayout.setVisibility(View.INVISIBLE);
                break;
            case LOADING:
            {
                LoadingVH loadingVH = (LoadingVH) holder;
                if(loadingVH != null)
                {
                    if (retryPageLoad)
                    {
                        loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                        loadingVH.mProgressBar.setVisibility(View.GONE);

                        loadingVH.mErrorTxt.setText(
                                errorMsg != null ?
                                        errorMsg :
                                        activity.getString(R.string.error_msg_unknown));

                    } else
                    {
                        if(loadingVH.mErrorLayout!= null) loadingVH.mErrorLayout.setVisibility(View.GONE);
                        if(loadingVH.mProgressBar!= null) loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                    }
                }
                break;
            }

        }
            /*PartnerObject partner=partners.get(i);
            viewHolder.tvPartnerName.setText(partner.getName());
            viewHolder.tvDescription.setText(partner.getDescripton());
            viewHolder.tvNumOfTracks.setText("Number Of Tracks: "+ partner.getNumberOfTracks());*/

    }

    @Override
    public int getItemCount(){
            return partners.size();
            }

    class PartnerViewHolder extends RecyclerView.ViewHolder {
        public View view;
        TextView tvPartnerName;
        TextView tvDescription;
        TextView tvNumOfTracks;

        Button btnAdToFilter;
        ImageButton btnDrawer;
        LinearLayout hoverLayout;
        LinearLayout partnersDetailsLayout;

        int id;

        public PartnerViewHolder(@NonNull View itemView) {
            super(itemView);
            //this.view=itemView.findViewWithTag(R.id.track_row_layout);
            tvPartnerName = itemView.findViewById(R.id.tv_partner_name);
            tvDescription = itemView.findViewById(R.id.tv_partner_description);
            tvNumOfTracks = itemView.findViewById(R.id.tv_number_of_tracks);
            hoverLayout = itemView.findViewById(R.id.hoverButtonsLayoutPartnerList);
            partnersDetailsLayout = itemView.findViewById(R.id.layout_partner_details);

            btnAdToFilter = itemView.findViewById(R.id.btn_add_partner_to_filter);
            btnDrawer = itemView.findViewById(R.id.btn_partner_drawer);

            btnAdToFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = getAdapterPosition();
                    PartnerObject selectedpartner = partners.get(i);
                    activity.mProgressDialog.setMessage(activity.getString(R.string.loader_msg));
                    activity.mProgressDialog.show();

                    Call<IResponse> call = Global.client.AddFollowshipToPartner(selectedpartner.getId());
                    call.enqueue(new Callback<IResponse>() {
                        @Override
                        public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                            IResponse res = response.body();
                            if(res.getNumber()==0)
                            {
                                activity.mProgressDialog.dismiss();
                                Toast.makeText(activity,activity.getResources().getString( R.string.partner_add_to_filter),Toast.LENGTH_LONG).show();
                                // TODO add the partner in the list in shared prefeence

                            }
                            else
                            {
                                activity.mProgressDialog.dismiss();
                                Toast.makeText(activity,res.getMessage(),Toast.LENGTH_LONG).show();
                            }
                            hoverLayout.setVisibility(View.INVISIBLE);
                        }
                        @Override
                        public void onFailure(Call<IResponse> call, Throwable t) {
                            activity.mProgressDialog.dismiss();
                            hoverLayout.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            });

            btnDrawer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Context context = v.getContext();
                    if (hoverLayout.getVisibility() == View.VISIBLE) {
                        hoverLayout.setVisibility(View.INVISIBLE);
                    } else {
                        hoverLayout.setVisibility(View.VISIBLE);
                    }
                }
            });

            tvPartnerName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int i  = getPosition();
                    // TODO Marwa
                    PartnerObject p = partners.get(i);

                    activity.partnerNameFragment = PartnerNameFragment.newInstance(p.getId(), p.getName(), p.getDescripton(),p.getLogo());
                    androidx.fragment.app.FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, activity.partnerNameFragment);
                    fragmentTransaction.addToBackStack( "Parnter Page" );
                    fragmentTransaction.commit();

                }
            });
        }
    }


    // region helpers

    public void add(PartnerObject mc) {
        partners.add(mc);
        notifyItemInserted(partners.size() - 1);
    }

    public void addAll(List<PartnerObject> mcList)
    {
        for (PartnerObject mc : mcList) {
            add(mc);
        }
    }

    public void remove(PartnerObject city) {
        int position = partners.indexOf(city);
        if (position > -1) {
            partners.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new PartnerObject());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = partners.size() - 1;
        PartnerObject item = getItem(position);

        if (item != null) {
            partners.remove(position);
            notifyItemRemoved(position);
        }
    }

    // TODO : check availlablilty
    public PartnerObject getItem(int position)
    {
        if(partners.size() != 0)
            return partners.get(position);
        else
            return null;
    }


    // endregion
}



