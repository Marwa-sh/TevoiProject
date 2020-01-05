package com.tevoi.tevoi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

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


        return rootView;
    }

}
