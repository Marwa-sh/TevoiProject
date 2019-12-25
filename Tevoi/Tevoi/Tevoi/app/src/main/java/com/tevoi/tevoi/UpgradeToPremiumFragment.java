
package com.tevoi.tevoi;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

public class UpgradeToPremiumFragment extends Fragment {

    EditText txtCoupon;
    ImageButton btnAccept;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_upgrade_to_premium, container, false);

        txtCoupon = rootView.findViewById(R.id.txtCoupon);
        btnAccept = rootView.findViewById(R.id.btn_accept_coupon);

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





        return  rootView;
    }
}

