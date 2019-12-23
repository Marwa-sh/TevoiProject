package com.tevoi.tevoi.model;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tevoi.tevoi.R;

public  class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener
{
        public ProgressBar mProgressBar;
        public ImageButton mRetryBtn;
        public TextView mErrorTxt;
        public LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);

            mProgressBar = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);
            mRetryBtn = (ImageButton) itemView.findViewById(R.id.loadmore_retry);
            mErrorTxt = (TextView) itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = (LinearLayout) itemView.findViewById(R.id.loadmore_errorlayout);

            if(mRetryBtn != null) {
                mRetryBtn.setOnClickListener(this);
                mErrorLayout.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loadmore_retry:
                case R.id.loadmore_errorlayout:

                    //showRetry(false, null);
                    //mCallback.retryPageLoad();

                    break;
            }
        }
    }

