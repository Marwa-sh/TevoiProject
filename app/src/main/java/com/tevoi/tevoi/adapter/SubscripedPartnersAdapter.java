package com.tevoi.tevoi.adapter;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tevoi.tevoi.R;
import com.tevoi.tevoi.RegisterActivity;
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
        //partnerViewHolder.checkBoxFilterState.setChecked(partner.isFilterValue());
        partnerViewHolder.isChecked = partner.isFilterValue();
        if(partnerViewHolder.isChecked)
        {
            partnerViewHolder.checkBoxFilterState.setImageResource(R.mipmap.golden_button_on);
        }
        else
        {
            partnerViewHolder.checkBoxFilterState.setImageResource(R.mipmap.grey_button_off);
        }
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
        public  boolean isChecked;
        //public SwitchCompat checkBoxFilterState;
        public ImageView checkBoxFilterState;

        public SubscripedPartnersViewHolder(@NonNull final View itemView) {
            super(itemView);
            this.tvPartnerName=itemView.findViewById(R.id.tv_filter_name);
            this.checkBoxFilterState=itemView.findViewById(R.id.checkbox_filter_state);

            checkBoxFilterState.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    //change filtering state

                    int i = getPosition();
                    final SubscipedPartnersObject partner = partners.get(i);

                    isChecked = !isChecked;
                    partner.setFilterValue(isChecked);
                    if(isChecked)
                    {
                        checkBoxFilterState.setImageResource(R.mipmap.golden_button_on);
                    }
                    else
                    {
                        checkBoxFilterState.setImageResource(R.mipmap.grey_button_off);
                    }
                    //activity.mProgressDialog.setMessage(activity.getResources().getString( R.string.loader_msg));
                    //activity.mProgressDialog.show();

                    Call<IResponse> call = Global.client.UpdateFollowshipToPartner(partner.getId());
                    call.enqueue(new Callback<IResponse>() {
                        @Override
                        public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                            IResponse res = response.body();
                            activity.IsFilterChanged = true;

                            /*if(res.getNumber()==0)
                            {
                                isChecked = !isChecked;
                                partner.setFilterValue(isChecked);
                                Toast.makeText(activity, res.getMessage(),Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(activity,res.getMessage(),Toast.LENGTH_LONG).show();
                            }
                            if(isChecked)
                            {
                                checkBoxFilterState.setImageResource(R.mipmap.golden_button_on);
                            }
                            else
                            {
                                checkBoxFilterState.setImageResource(R.mipmap.grey_button_off);
                            }
                            activity.mProgressDialog.dismiss();*/
                        }
                        @Override
                        public void onFailure(Call<IResponse> call, Throwable t) {
                            //activity.mProgressDialog.dismiss();
                        }
                    });
                }
            });

            /*this.checkBoxFilterState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked)
                {
                    //change filtering state
                    int i = getPosition();
                    final SubscipedPartnersObject partner = partners.get(i);

                    Call<IResponse> call = Global.client.UpdateFollowshipToPartner(partner.getId());
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
            });*/

        }
    }
}
