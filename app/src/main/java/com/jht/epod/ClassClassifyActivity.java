package com.jht.epod;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ClassClassifyActivity extends Activity {
    private int mCurrentSelect = 0;//0:收腹,1:雕塑手臂,2:翹臀
    private int mPreviousSelect = 0;//0:收腹,1:雕塑手臂,2:翹臀

    private LinearLayout mCoreTab;
    private TextView mCoreText;
    private ImageView mCoreImage;

    private LinearLayout mArmTab;
    private TextView mArmText;
    private ImageView mArmImage;

    private LinearLayout mHipTab;
    private TextView mHipText;
    private ImageView mHipImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_classify);
        mCoreTab = findViewById(R.id.core_tab);
        mCoreText = findViewById(R.id.core_tab_txt);
        mCoreImage = findViewById(R.id.core_tab_pic);

        mArmTab = findViewById(R.id.arm_tab);
        mArmText = findViewById(R.id.arm_tab_txt);
        mArmImage = findViewById(R.id.arm_tab_pic);

        mHipTab = findViewById(R.id.hip_tab);
        mHipText = findViewById(R.id.hip_tab_txt);
        mHipImage = findViewById(R.id.hip_tab_pic);

        findViewById(R.id.back).setOnClickListener(mViewListener);
        mCoreTab.setOnClickListener(mViewListener);
        mArmTab.setOnClickListener(mViewListener);
        mHipTab.setOnClickListener(mViewListener);
        updateState();

        MeasureListView mListClass = findViewById(R.id.list_class);
        ArrayList<HashMap<String, Object>> listClassValue = new ArrayList<HashMap<String, Object>>();
        for(int i = 0; i < 6; i++){
            HashMap<String, Object> classValue = new HashMap<String, Object>();
            classValue.put("classPic", R.drawable.list_class_pic);
            classValue.put("classTitle", getResources().getString(R.string.list_class_title));
            classValue.put("classSubtitle", getResources().getString(R.string.list_class_subtitle));
            listClassValue.add(classValue);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, listClassValue,
                R.layout.list_recommended_course,
                new String[]{"classPic", "classTitle", "classSubtitle"},
                new int[]{R.id.class_pic, R.id.class_title, R.id.class_subtitle});
        mListClass.setAdapter(adapter);
        mListClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(ClassClassifyActivity.this, MainActivity.class));
            }
        });
    }

    View.OnClickListener mViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    finish();
                    break;
                case R.id.core_tab:
                    mPreviousSelect = mCurrentSelect;
                    mCurrentSelect = 0;
                    if(mCurrentSelect == mPreviousSelect){
                        return;
                    }
                    updateState();
                    break;
                case R.id.arm_tab:
                    mPreviousSelect = mCurrentSelect;
                    mCurrentSelect = 1;
                    if(mCurrentSelect == mPreviousSelect){
                        return;
                    }
                    updateState();
                    break;
                case R.id.hip_tab:
                    mPreviousSelect = mCurrentSelect;
                    mCurrentSelect = 2;
                    if(mCurrentSelect == mPreviousSelect){
                        return;
                    }
                    updateState();
                    break;
            }
        }
    };

    private void updateState(){
        //切换三种卡片功能实现

        //切花三种卡片UI实现
        updateTab();
    }

    private void updateTab(){
        switch (mCurrentSelect) {
            case 0:
                mCoreText.setTextColor(getResources().getColor(R.color.colorUpTabSelect));
                mCoreImage.setBackgroundColor(getResources().getColor(R.color.colorUpTabSelect));
                if (mPreviousSelect == 1) {
                    mArmText.setTextColor(getResources().getColor(R.color.colorTabUnselect));
                    mArmImage.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                } else if (mPreviousSelect == 2) {
                    mHipText.setTextColor(getResources().getColor(R.color.colorTabUnselect));
                    mHipImage.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                }
                break;
            case 1:
                mArmText.setTextColor(getResources().getColor(R.color.colorUpTabSelect));
                mArmImage.setBackgroundColor(getResources().getColor(R.color.colorUpTabSelect));
                if (mPreviousSelect == 0) {
                    mCoreText.setTextColor(getResources().getColor(R.color.colorTabUnselect));
                    mCoreImage.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                } else if (mPreviousSelect == 2) {
                    mHipText.setTextColor(getResources().getColor(R.color.colorTabUnselect));
                    mHipImage.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                }
                break;
            case 2:
                mHipText.setTextColor(getResources().getColor(R.color.colorUpTabSelect));
                mHipImage.setBackgroundColor(getResources().getColor(R.color.colorUpTabSelect));
                if (mPreviousSelect == 0) {
                    mCoreText.setTextColor(getResources().getColor(R.color.colorTabUnselect));
                    mCoreImage.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                } else if (mPreviousSelect == 1) {
                    mArmText.setTextColor(getResources().getColor(R.color.colorTabUnselect));
                    mArmImage.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                }
                break;
        }
    }
}
