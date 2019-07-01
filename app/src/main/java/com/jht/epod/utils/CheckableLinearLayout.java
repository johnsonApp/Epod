package com.jht.epod.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;

import android.util.AttributeSet;

public class CheckableLinearLayout extends LinearLayout implements Checkable{
    private final String TAG = "CheckableLinearLayout";

    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};
    private boolean mChecked = false;
    private OnClickListener mListener;

    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context,attrs);
        init();
    }

    public void init(){
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"onClick mChecked " + mChecked);
                toggle();
                if(mListener != null){
                    mListener.onClick(v);
                }
            }
        });
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        mListener = l;
    }

    public boolean isChecked() {
        return  mChecked;
    }

    public void setChecked(boolean isChecked){
        if(isChecked != mChecked){
            mChecked = isChecked;
            refreshDrawableState();
        }
    }

    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if(isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }
}
