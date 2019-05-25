package com.ebridge.tevoi.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ebridge.tevoi.PartnerNameFragment;
import com.ebridge.tevoi.R;
import com.ebridge.tevoi.SideMenu;
import com.ebridge.tevoi.Utils.Global;
import com.ebridge.tevoi.model.IResponse;
import com.ebridge.tevoi.model.PartnerObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartnerAdapter extends RecyclerView.Adapter<PartnerAdapter.PartnerViewHolder>
{
private List<PartnerObject> partners;
private SideMenu activity;

public PartnerAdapter(List<PartnerObject> partners, SideMenu activity){
        this.partners=partners;
        this.activity=activity;
        }
public PartnerAdapter.PartnerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View row=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragmet_partner_instance,viewGroup,false);
        PartnerAdapter.PartnerViewHolder holder=new PartnerAdapter.PartnerViewHolder(row);

        return holder;
        }

@Override
public void onBindViewHolder(@NonNull PartnerAdapter.PartnerViewHolder viewHolder,int i){
        PartnerObject partner=partners.get(i);
        viewHolder.tvPartnerName.setText(partner.getName());
        viewHolder.tvDescription.setText(partner.getDescripton());
        viewHolder.tvNumOfTracks.setText("Number Of Tracks: "+ partner.getNumberOfTracks());

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
                 Call<IResponse> call = Global.client.AddFollowshipToPartner(selectedpartner.getId());
                call.enqueue(new Callback<IResponse>() {
                    @Override
                    public void onResponse(Call<IResponse> call, Response<IResponse> response) {
                        IResponse res = response.body();
                        if(res.getNumber()==0)
                        {
                            Toast.makeText(activity,res.getMessage(),Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(activity,res.getMessage(),Toast.LENGTH_LONG).show();
                        }
                        hoverLayout.setVisibility(View.INVISIBLE);
                    }
                    @Override
                    public void onFailure(Call<IResponse> call, Throwable t) {
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
                PartnerObject p = partners.get(i);

                activity.partnerNameFragment = PartnerNameFragment.newInstance(p.getId(), p.getName(), p.getDescripton());
                android.support.v4.app.FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, activity.partnerNameFragment);
                fragmentTransaction.commit();

            }
        });
    }
}

}



