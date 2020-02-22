package com.tevoi.tevoi.TextPagination;

import android.text.Layout;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.tevoi.tevoi.SideMenu;
import com.tevoi.tevoi.Utils.Global;

import java.util.HashMap;

import androidx.annotation.NonNull;

public class PaginationController {

    SideMenu activity;

    private static final String TAG = PaginationController.class.getSimpleName();

    private final TextView mTextView;
    private final TextView mPageNumber;

    private int mPageIndex;
    private String mText;
    private HashMap<Integer, Boundary> mBoundaries;
    private int mLastPageIndex;

    int remainingWordsCount;
    int totalUnitsInText;
    int localUnitsConsumed;

    boolean isNextClick = false;

    public PaginationController(@NonNull TextView textView,@NonNull TextView textPageNumber, SideMenu activity) {
        mTextView = textView;
        mBoundaries = new HashMap<>();
        mLastPageIndex = -1;
        this.activity = activity;
        mPageNumber = textPageNumber;
        remainingWordsCount = 0;
        totalUnitsInText =0;
        localUnitsConsumed = 0;
    }

    public void onTextLoaded(@NonNull String text, @NonNull final OnInitializedListener listener) {
        mPageIndex = 0;
        mText = text;

        int wordsCountInText = countWords(text);
        totalUnitsInText = wordsCountInText / Global.ReadUnitInWords;

        // check if there are remaining words
        int r = wordsCountInText - totalUnitsInText * Global.ReadUnitInWords;
        if(r > 0)
        {
            totalUnitsInText++;
        }
        Log.d("TotatlUnits", totalUnitsInText + "");
        if (mTextView.getLayout() == null) {
            mTextView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ViewTreeObserver obs = mTextView.getViewTreeObserver();
                    obs.removeOnGlobalLayoutListener(this);
                    setTextWithCaching(mPageIndex, 0);
                    listener.onInitialized();
                }
            });
        } else {
            setTextWithCaching(mPageIndex, 0);
            listener.onInitialized();
        }
    }

    /**
     * Assume, that page index can be only next or previous. For other cases
     *
     * @param pageIndex index of selected page
     */
    private void selectPage(int pageIndex) {
        Log.d(TAG, "selectPage=" + pageIndex);

        String displayedText;
        if (mBoundaries.containsKey(pageIndex)) {
            // use existing boundaries
            Boundary boundary = mBoundaries.get(pageIndex);
            displayedText = mText.substring(boundary.start, boundary.end);

            // TODO: get Words Count
            int countNumber = countWords(displayedText);
            countNumber += remainingWordsCount;
            int numOfUnitsInPage = countNumber / Global.ReadUnitInWords;

            Log.d(TAG, "ccMMc Units In:[" + pageIndex + "]: " + numOfUnitsInPage
                    + ", Number Of words" + countNumber
                    + "remaining=" + remainingWordsCount);
            if(activity.numberOfReadUnitsSendToServer > activity.userSubscriptionInfo.FreeSubscriptionLimit.DailyReadMaxUnits)
            {
                activity.IsReadDailyLimitsExceeded = true;
            }
            else
            {
                activity.numberOfTextUnitsConsumed += numOfUnitsInPage;
                localUnitsConsumed += numOfUnitsInPage;
                remainingWordsCount = countNumber - numOfUnitsInPage*Global.ReadUnitInWords;

            }

            if(mLastPageIndex != -1)
            {
                if(remainingWordsCount != 0)
                {
                    activity.numberOfTextUnitsConsumed += 1;
                    localUnitsConsumed += 1;
                    numOfUnitsInPage += 1;
                }
            }
            // todo: send to server
            Log.d(TAG, "UnitsSendToServer" + numOfUnitsInPage);

            mTextView.setText(displayedText);
            mPageNumber.setText("" + (pageIndex +1));
            Log.v(TAG, "Existing[" + pageIndex + "]: " + displayedText);

        } else if (mBoundaries.containsKey(pageIndex - 1)) {
            //calculate boundaries for new page (previous exists)
            Boundary previous = mBoundaries.get(pageIndex - 1);
            setTextWithCaching(pageIndex, previous.end);
        } else {
            Log.v(TAG, "selectPage(" + pageIndex + "), values=[" + mBoundaries.keySet());
            // TODO implement selectPage(n), n - random int
        }
    }

    public static int countWords(String s){

        int wordCount = 0;

        boolean word = false;
        int endOfLine = s.length() - 1;

        for (int i = 0; i < s.length(); i++) {
            // if the char is a letter, word = true.
            if (Character.isLetter(s.charAt(i)) && i != endOfLine) {
                word = true;
                // if char isn't a letter and there have been letters before,
                // counter goes up.
            } else if (!Character.isLetter(s.charAt(i)) && word) {
                wordCount++;
                word = false;
                // last word of String; if it doesn't end with a non letter, it
                // wouldn't count without this.
            } else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
                wordCount++;
            }
        }
        return wordCount;
    }

    private void setTextWithCaching(int pageIndex, int pageStartSymbol) {
        String restText = mText.substring(pageStartSymbol);

        mTextView.setText(restText);

        int height = mTextView.getHeight();
        int scrollY = mTextView.getScrollY();
        Layout layout = mTextView.getLayout();
        int firstVisibleLineNumber = layout.getLineForVertical(scrollY);
        int lastVisibleLineNumber = layout.getLineForVertical(height + scrollY);

        //check is latest line fully visible
        if (mTextView.getHeight() < layout.getLineBottom(lastVisibleLineNumber)) {
            lastVisibleLineNumber--;
        }

        int start = pageStartSymbol + mTextView.getLayout().getLineStart(firstVisibleLineNumber);
        int end = pageStartSymbol + mTextView.getLayout().getLineEnd(lastVisibleLineNumber);

        if (end == mText.length()) {
            mLastPageIndex = pageIndex;
        }
        String displayedText = mText.substring(start, end);
        Log.v(TAG, "Added to Cache[" + pageIndex + "](symbols={" + start + "," + end + "}): " + displayedText);

        if(isNextClick) {

            int countNumber = countWords(displayedText);
            countNumber += remainingWordsCount;
            int numOfUnitsInPage = countNumber / Global.ReadUnitInWords;

            Log.d(TAG, "Units In:[" + pageIndex + "]: " + numOfUnitsInPage
                    + ", Number Of words" + countNumber
                    + "remaining=" + remainingWordsCount);

            Log.d(TAG, "Last Index=" + mLastPageIndex);
            if (activity.numberOfReadUnitsSendToServer > activity.userSubscriptionInfo.FreeSubscriptionLimit.DailyReadMaxUnits) {
                activity.IsReadDailyLimitsExceeded = true;
            } else {
                activity.numberOfTextUnitsConsumed += numOfUnitsInPage;
                remainingWordsCount = countNumber - numOfUnitsInPage * Global.ReadUnitInWords;
                // todo: send to server
            }
        }
        //correct visible text
        mTextView.setText(displayedText);
        mPageNumber.setText("" + (pageIndex +1));

        mBoundaries.put(pageIndex, new Boundary(start, end));
    }

    public boolean next() {
        isNextClick = true;
        throwIfNotInitialized();
        if (isNextEnabled()) {
            selectPage(++mPageIndex);
            return true;
        }
        return false;
    }

    public boolean previous() {
        isNextClick = false;
        throwIfNotInitialized();
        if (isPreviousEnabled()) {
            selectPage(--mPageIndex);
            return true;
        }
        return false;
    }

    public boolean isNextEnabled() {
        throwIfNotInitialized();
        return mPageIndex < mLastPageIndex || mLastPageIndex < 0;
    }

    public boolean isPreviousEnabled() {
        throwIfNotInitialized();
        return mPageIndex > 0;
    }

    void throwIfNotInitialized() {
        if (mText == null) {
            throw new IllegalStateException("Call onTextLoaded(String) first");
        }
    }

    private class Boundary {

        final int start;
        final int end;

        private Boundary(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    public interface OnInitializedListener {
        void onInitialized();
    }
}
