package com.tevoi.tevoi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.model.IResponse;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentFragment extends Fragment {

    View rootView;
    CheckBox chk12Month;
    CheckBox chk3Month;
    CheckBox chk1Month;

    CheckBox chkAppleStore;
    CheckBox chkPhoneBill;
    CheckBox chkCredit;
    CheckBox chkPaypal;

    ImageButton btnPlayNow;
    ImageButton btnCancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_payment, container, false);
        btnCancel = rootView.findViewById(R.id.btn_cancel_payment);
        btnPlayNow = rootView.findViewById(R.id.btn_play_now_payment);
        chk12Month = rootView.findViewById(R.id.chk_12_month_subscraption);
        chk3Month = rootView.findViewById(R.id.chk_3_month_subscraption);
        chk1Month = rootView.findViewById(R.id.chk_1_month_subscraption);
        chkAppleStore = rootView.findViewById(R.id.chk_apple_store_google_play);
        chkPhoneBill = rootView.findViewById(R.id.chk_phone_bill);
        chkCredit = rootView.findViewById(R.id.chk_credit_debit_card);
        chkPaypal =rootView.findViewById(R.id.chk_paypal);

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

        

        return rootView;
    }

}
