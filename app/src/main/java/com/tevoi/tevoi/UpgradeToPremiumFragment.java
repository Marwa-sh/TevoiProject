
package com.tevoi.tevoi;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

public class UpgradeToPremiumFragment extends Fragment {

    EditText txtCoupon;
    ImageButton btnAccept;

    CheckBox chk12Month;
    CheckBox chk3Month;
    CheckBox chk1Month;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_upgrade_to_premium, container, false);

        txtCoupon = rootView.findViewById(R.id.txtCoupon);
        btnAccept = rootView.findViewById(R.id.btn_accept_coupon);
        chk1Month = rootView.findViewById(R.id.chk_1_month_subscraption_upgrade);
        chk3Month = rootView.findViewById(R.id.chk_3_month_subscraption_upgrade);
        chk12Month = rootView.findViewById(R.id.chk_12_month_subscraption_upgrade);



        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coupon = txtCoupon.getText().toString();
                if(coupon.equals(""))
                {
                    txtCoupon.setError("Coupon missing");
                    txtCoupon.requestFocus();
                }
                else
                {
                    // check coupon validity

                }
            }
        });

        chk12Month.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                // you might keep a reference to the CheckBox to avoid this class cast
                boolean checked = ((CheckBox)v).isChecked();
                chk1Month.setChecked(!checked);
                chk3Month.setChecked(!checked);
            }

        });
        chk3Month.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                // you might keep a reference to the CheckBox to avoid this class cast
                boolean checked = ((CheckBox)v).isChecked();
                chk1Month.setChecked(!checked);
                chk12Month.setChecked(!checked);
            }

        });

        chk1Month.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                // you might keep a reference to the CheckBox to avoid this class cast
                boolean checked = ((CheckBox)v).isChecked();
                chk12Month.setChecked(!checked);
                chk3Month.setChecked(!checked);
            }

        });




        return  rootView;
    }
}

