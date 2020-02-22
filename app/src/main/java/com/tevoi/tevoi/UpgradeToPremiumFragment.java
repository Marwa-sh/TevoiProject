
package com.tevoi.tevoi;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tevoi.tevoi.PaymentUtil.IabHelper;
import com.tevoi.tevoi.PaymentUtil.IabResult;
import com.tevoi.tevoi.PaymentUtil.Inventory;
import com.tevoi.tevoi.PaymentUtil.Purchase;

import java.util.ArrayList;
import java.util.List;

public class UpgradeToPremiumFragment extends Fragment {
    private String tag = "MainActivity";
    private ArrayList<Item> data = new ArrayList<Item>();

    static final int RC_REQUEST = 10001;

    static  final String SKU_COIN = "";

    static final String SKU_YEARLY = "one_year_subscription";
    static final String SKU_Three_Months = "three_months_subscription";
    static final String SKU_One_Month = "one_month_subscription";

    String[] mIssueSkus = {};//, SKU_ISSUE_2, SKU_ISSUE_3};

    private String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApNiregPb5uPYa9eyTN7YNwGrzgZPiSo8Y6J02PB7FmhWhpa4UVRMV3Bj6+jBj3kkDl0mRa3kYGR4VuFkSDZFNERuqQEeXHc6DAmHhiYZFKOF+Xxcsv3LOH0+OhJHCLwdoEvfx5m4WPAZxzc3Q0sckYqUuksei0GGZTYQKRemZWwWq05DIqbrc5IjblLlcp4xpVaduHIqrq7jS5hv0wiSkq9DBJFvdGFrUQVVdJFsndkXY+vP/Hg0WrheIroHb40L49ox9CLEqpXGW7QHLMNClg5ispk/mMfMahjCtW3VQGvMaojO8Fss9HmIQRXnCGfMy1HgRNrsAhjhhaS5/NXTsQIDAQAB";

    public IabHelper mHelper;
    private ListView mListView;
    private ArrayList<String> mBoughtIssues = new ArrayList<String>();

    private boolean mYearlySubscribed = false;
    private boolean mThreeMonthSubscribed = false;
    private boolean mOneMonthSubscribed = false;

    private SideMenu activity;

    EditText txtCoupon;
    ImageButton btnAccept;

    CheckBox chk12Month;
    CheckBox chk3Month;
    CheckBox chk1Month;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activity = (SideMenu)getActivity();
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


        createPurchaseHelper();


        return  rootView;
    }

    private void createPurchaseHelper(){

        mHelper = new IabHelper(activity, base64EncodedPublicKey);

        mHelper.enableDebugLogging(true);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {

            public void onIabSetupFinished(IabResult result) {

                if (!result.isSuccess()) {
                    alert("Error: Problem setting up in-app billing: " + result);
                    return;
                }

                if (mHelper == null) return;
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });
    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {

            if (mHelper == null) return;

            if (result.isFailure()) {
                alert("Failed to query inventory: " + result);
                return;
            }

            Log.d(tag, "Query inventory was successful.");

            /*Purchase coinPurchase = inventory.getPurchase(SKU_COIN);

            if (coinPurchase != null && verifyDeveloperPayload(coinPurchase)) {

                mHelper.consumeAsync(inventory.getPurchase(SKU_COIN), mConsumeFinishedListener);
            }*/

            /*for(int i = 0; i < 1; i++){

                String sku = mIssueSkus[i];

                Purchase issuePurchase = inventory.getPurchase(sku);

                if(issuePurchase != null && verifyDeveloperPayload(issuePurchase)) {
                    updateUIForIssue(sku);
                }
            }
*/
            Purchase yearlySubsPurchase = inventory.getPurchase(SKU_YEARLY);

            mYearlySubscribed = (yearlySubsPurchase != null && verifyDeveloperPayload(yearlySubsPurchase));

            Purchase three_months = inventory.getPurchase(SKU_Three_Months);

            mThreeMonthSubscribed = (three_months != null && verifyDeveloperPayload(three_months));

            Purchase one_months = inventory.getPurchase(SKU_One_Month);

            mThreeMonthSubscribed = (one_months != null && verifyDeveloperPayload(one_months));

        }
    };
    private void updateUIForIssue(String sku){

        if(!mBoughtIssues.contains(sku)){
            mBoughtIssues.add(sku);
            mListView.invalidateViews();
        }
    }

    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        //TODO: add verification
        return true;
    }

    public void generateListContent(){
        int[] drawables = {R.drawable.car_icon, R.drawable.outline_play_arrow_white_18, R.drawable.menu};
        for(int i= 0; i < 3; i++) {
            Item item = new Item("Issue " + i, drawables[i]);
            data.add(i, item);
        }
    }

    public void onClickSubscribe(View view){

       /* mHelper.launchPurchaseFlow( activity,
                SKU_YEARLY,
                IabHelper.ITEM_TYPE_SUBS,
                RC_REQUEST,
                mPurchaseFinishedListener,
                "");*/
        mHelper.launchPurchaseFlow( activity,
                SKU_Three_Months,
                IabHelper.ITEM_TYPE_SUBS,
                RC_REQUEST,
                mPurchaseFinishedListener,
                "");
    }

    public void onClickWin(View view){

        //mHelper.launchPurchaseFlow(activity, SKU_COIN, RC_REQUEST, mPurchaseFinishedListener, "");
    }

    private void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(activity);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        bld.create().show();
    }


    private class Item
    {
        private int drawable;
        private String text;
        public Item(String text, int drawable){
            this.text = text;
            this.drawable = drawable;
        }

        public int getDrawable() {
            return drawable;
        }

        public String getText() {
            return text;
        }
    }
    private class MyListAdapter extends ArrayAdapter
    {

        private int layout;
        private List listData;

        public MyListAdapter(Context context, int resource, List objects) {
            super(context, resource, objects);
            layout = resource;
            listData = objects;
        }

        public MyListAdapter(Context context, int resource, int textViewResourceId, List objects) {
            super(context, resource, textViewResourceId, objects);
            layout = resource;
            listData = objects;
        }

        public class ViewHolder{
            ImageView thumbnail;
            TextView title;
            Button button;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewHolder = null;
            Item item = (Item)getItem(position);
            String text = item.getText();
            int drawable = item.getDrawable();

            final String sku = mIssueSkus[position];

            if(convertView == null){
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                convertView = layoutInflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();

                /*viewHolder.thumbnail = (ImageView)convertView.findViewById(R.id.list_item_thumbnail);
                viewHolder.thumbnail.setImageResource(drawable);
                viewHolder.title = (TextView)convertView.findViewById(R.id.list_item_textView);
                viewHolder.title.setText(text);

                viewHolder.button = (Button)convertView.findViewById(R.id.list_item_button);
*/
                if(mBoughtIssues.contains(sku)){
                    viewHolder.button.setText("Read");
                }

                viewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "Buy clicked at position " + position, Toast.LENGTH_SHORT).show();

                        mHelper.launchPurchaseFlow(activity, sku, RC_REQUEST, mPurchaseFinishedListener, "");
                    }
                });

                convertView.setTag(viewHolder);
            }
            else{
                mainViewHolder = (ViewHolder)convertView.getTag();
                mainViewHolder.title.setText(text);
                mainViewHolder.thumbnail.setImageResource(drawable);
                if(mBoughtIssues.contains(sku)){
                    mainViewHolder.button.setText("Read");
                }
            }
            return convertView;
        }
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {

            if (mHelper == null) return;

            if (result.isFailure()) {
                alert("Error purchasing: " + result);
                return;
            }

            Log.d(tag, "Purchase successful.");

            if(purchase != null && verifyDeveloperPayload(purchase)) {

                if (purchase.getSku().equals(SKU_COIN)) {

                    mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                }
                else if (purchase.getSku().equals(SKU_YEARLY)) {

                    alert("Thank you for subscribing to infinite gas!");

                    mYearlySubscribed = true;
                }
                else if (purchase.getSku().equals(SKU_Three_Months)) {

                    alert("Thank you for subscribing to Six months!");

                    mThreeMonthSubscribed = true;
                }
                else{
                    updateUIForIssue(purchase.getSku());
                }
            }
        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener(){
        public void onConsumeFinished(Purchase purchase, IabResult result) {

            if (mHelper == null) return;

            if (result.isSuccess()) {
                gamble();
            }
            else {
                alert("Error while consuming: " + result);
            }
        }
    };

    private void gamble(){
        double randNumber = Math.random();
        int result = (int)(randNumber * 10000);
        if(result == 88){
            alert("Congrats! You win!");
            //TODO: add more logic to give out yearly subscription
        }
        else{
            alert("Thanks! Have a nice day :)");
        }
    }


    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (mHelper == null) return;

        if (mHelper.handleActivityResult(requestCode, resultCode, data)) {
            Log.d(tag, "onActivityResult handled by mHelper.");
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }*/

}

