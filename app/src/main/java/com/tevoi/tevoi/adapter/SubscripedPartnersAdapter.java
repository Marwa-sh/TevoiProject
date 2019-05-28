package com.tevoi.tevoi.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tevoi.tevoi.R;
import com.tevoi.tevoi.SideMenu;
import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.model.IResponse;
import com.tevoi.tevoi.model.SubscipedPartnersObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscripedPartnersAdapter extends RecyclerView.Adapter<SubscripedPartnersAdapter.SubscripedPartnersViewHolder>
{
    private List<SubscipedPartnersObject> partners;
    private SideMenu activity;

    public SubscripedPartnersAdapter(List<SubscipedPartnersObject> partners, SideMenu activity) {
        this.partners = partners;
        this.activity = activity;
    }

    @NonNull
    @Override
    public SubscripedPartnersAdapter.SubscripedPartnersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_filter_instance,viewGroup,false);
        SubscripedPartnersAdapter.SubscripedPartnersViewHolder holder = new SubscripedPartnersAdapter.SubscripedPartnersViewHolder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubscripedPartnersAdapter.SubscripedPartnersViewHolder partnerViewHolder, int i) {
        SubscipedPartnersObject partner =  partners.get(i);
        partnerViewHolder.tvPartnerName.setText(partner.getName());
        partnerViewHolder.checkBoxFilterState.setChecked(partner.isFilterValue());
    }
    @Override
    public int getItemCount() {
        return partners.size();
    }
    public class SubscripedPartnersViewHolder extends RecyclerView.ViewHolder
    {
        public  View view;
        public TextView tvPartnerName;
        public int Id;
        public SwitchCompat checkBoxFilterState;

        public SubscripedPartnersViewHolder(@NonNull final View itemView) {
            super(itemView);
            this.tvPartnerName=itemView.findViewById(R.id.tv_filter_name);
            this.checkBoxFilterState=itemView.findViewById(R.id.checkbox_filter_state);

            this.checkBoxFilterState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked)
                {
                    //change filtering state
                    int i = getPosition();
                    final SubscipedPartnersObject partner = partners.get(i);

                    Call<IResponse> call = Global.client.UpdateFollowshipToPartner(partner.getId(), isChecked);
                    call.enqueue(new Callback<IResponse>() {
                        @Override
                        public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                            IResponse res = response.body();
                            if(res.getNumber()==0)
                            {
                                partner.setFilterValue(isChecked);
                                Toast.makeText(activity, res.getMessage(),Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(activity,res.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<IResponse> call, Throwable t) {

                        }
                    });
                }
            });

        }
    }
}
