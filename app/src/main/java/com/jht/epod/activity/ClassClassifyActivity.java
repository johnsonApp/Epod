package com.jht.epod.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.jht.epod.MeasureListView;
import com.jht.epod.R;
import com.jht.epod.model.ClassData;
import com.jht.epod.model.ClassDataManager;
import com.jht.epod.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClassClassifyActivity extends Activity {
    private static final String TAG = "ClassClassifyActivity";

    private static final int FIRST = 1;
    private static final int SECOND = 2;
    private static final int THIRD = 3;

    private int mCurrentSelect = FIRST;//1:收腹,2:雕塑手臂,3:翹臀
    private int mPreviousSelect = FIRST;//1:收腹,2:雕塑手臂,3:翹臀

    private LinearLayout mCoreTab;
    private TextView mCoreText;
    private ImageView mCoreImage;

    private LinearLayout mArmTab;
    private TextView mArmText;
    private ImageView mArmImage;

    private LinearLayout mHipTab;
    private TextView mHipText;
    private ImageView mHipImage;

    private MeasureListView mListClass;

    private ClassDataManager mDataManager;

    private ArrayList<ClassData> mFirstData;
    private ArrayList<ClassData> mSecondData;
    private ArrayList<ClassData> mThirdData;

    private SimpleAdapter mFirstAdapter;
    private SimpleAdapter mSecondAdapter;
    private SimpleAdapter mThirdAdapter;

    private boolean mClassType = true;
    private int mLastViewSelected = FIRST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            mClassType = extras.getBoolean(Utils.CLASSTYPE, true);
            mLastViewSelected = extras.getInt(Utils.DEGREE, FIRST);
        }

        mCurrentSelect = mLastViewSelected;
        initClassData();
        initView();

    }

    private void initView() {
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

        if(!mClassType) {
            mCoreText.setText(R.string.junior);
            mArmText.setText(R.string.medium);
            mHipText.setText(R.string.senior);
        }

        findViewById(R.id.back).setOnClickListener(mViewListener);
        mCoreTab.setOnClickListener(mViewListener);
        mArmTab.setOnClickListener(mViewListener);
        mHipTab.setOnClickListener(mViewListener);

        mListClass = findViewById(R.id.list_class);
        mListClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView stordId = view.findViewById(R.id.store_id);
                Log.i(TAG,"onItemClick get store id " + stordId.getText());
                int classId = Integer.parseInt(stordId.getText().toString());
                startClassActivity(classId);
            }
        });

        upDataState();
    }

    private void startClassActivity(int id) {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra(Utils.ID,id);
        startActivity(intent);
    }

    private void initClassData() {
        if(mClassType) {
            mFirstData = ClassDataManager.queryClassByType(Utils.TYPE_CORE);
            mSecondData = ClassDataManager.queryClassByType(Utils.TYPE_ARM);
            mThirdData = ClassDataManager.queryClassByType(Utils.TYPE_HIP);
        }else {
            mFirstData = ClassDataManager.queryClassByDegree(Utils.DEGREE_JUNIOR);
            mSecondData = ClassDataManager.queryClassByDegree(Utils.DEGREE_MEDIUM);
            mThirdData = ClassDataManager.queryClassByDegree(Utils.DEGREE_SENIOR);
        }
    }

    private void upDataListData() {
        mListClass.setAdapter(getAdapter());
    }

    private SimpleAdapter getAdapter() {
        SimpleAdapter adapter = null;
        switch (mCurrentSelect) {
            case Utils.TYPE_CORE:
                if(null == mFirstAdapter) {
                    mFirstAdapter = initAdapter();
                }
                adapter = mFirstAdapter;
                break;
            case Utils.TYPE_ARM:
                if(null == mSecondAdapter) {
                    mSecondAdapter = initAdapter();
                }
                adapter = mSecondAdapter;
                break;
            case Utils.TYPE_HIP:
                if(null == mThirdAdapter) {
                    mThirdAdapter = initAdapter();
                }
                adapter = mThirdAdapter;
                break;
        }
        return adapter;
    }

    private SimpleAdapter initAdapter() {
        ArrayList<HashMap<String, Object>> listClassValue = new ArrayList<HashMap<String, Object>>();
        ArrayList<ClassData> list = getDataBySelected();
        for(ClassData Data : list) {
            HashMap<String, Object> classValue = new HashMap<String, Object>();
            classValue.put("classPic", getImageResource(Data.getIconName()));
            classValue.put("classTitle", Data.getName());
            classValue.put("classSubtitle", getTextInfo(Data));
            classValue.put("storeId", Data.getId());
            listClassValue.add(classValue);
            Log.i(TAG,"initAdapter " + Data.toString());
        }
        SimpleAdapter adapter = new SimpleAdapter(this, listClassValue,
                R.layout.list_recommended_course,
                new String[]{"classPic", "classTitle", "classSubtitle", "storeId"},
                new int[]{R.id.class_pic, R.id.class_title, R.id.class_subtitle, R.id.store_id});
        return adapter;
    }

    private ArrayList<ClassData> getDataBySelected(){
        ArrayList<ClassData> list = null;
        switch (mCurrentSelect) {
            case Utils.TYPE_CORE:
                list = mFirstData;
                break;
            case Utils.TYPE_ARM:
                list = mSecondData;
                break;
            case Utils.TYPE_HIP:
                list = mThirdData;
                break;
        }
        return list;
    }

    private String getTextInfo(ClassData data){
        StringBuilder sb = new StringBuilder();
        sb.append(data.getTime())
                .append("分钟*")
                .append(data.getCalorie())
                .append("千卡*")
                .append(getStringByDegree(data.getDegree()));
        return sb.toString();
    }

    private String getStringByDegree(int degree) {
        String text = null;
        switch (degree){
            case Utils.DEGREE_JUNIOR:
                text = getResources().getString(R.string.junior);
                break;
            case Utils.DEGREE_MEDIUM:
                text = getResources().getString(R.string.medium);
                break;
            case Utils.DEGREE_SENIOR:
                text = getResources().getString(R.string.senior);
                break;
        }
        return text;
    }

    private int getImageResource(String imageName) {
        int resId = getResources().getIdentifier(imageName,"drawable",this.getPackageName());
        return resId;
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
                    mCurrentSelect = FIRST;
                    if(mCurrentSelect == mPreviousSelect){
                        return;
                    }
                    upDataState();
                    break;
                case R.id.arm_tab:
                    mPreviousSelect = mCurrentSelect;
                    mCurrentSelect = SECOND;
                    if(mCurrentSelect == mPreviousSelect){
                        return;
                    }
                    upDataState();
                    break;
                case R.id.hip_tab:
                    mPreviousSelect = mCurrentSelect;
                    mCurrentSelect = THIRD;
                    if(mCurrentSelect == mPreviousSelect){
                        return;
                    }
                    upDataState();
                    break;
            }
        }
    };

    private void upDataState(){
        //切换三种卡片功能实现
        upDataListData();
        //切花三种卡片UI实现
        upDataTab();
    }

    private void upDataTab(){
        switch (mCurrentSelect) {
            case FIRST:
                mCoreText.setTextColor(getResources().getColor(R.color.colorUpTabSelect));
                mCoreImage.setBackgroundColor(getResources().getColor(R.color.colorUpTabSelect));
                if (mPreviousSelect == SECOND) {
                    mArmText.setTextColor(getResources().getColor(R.color.colorTabUnselect));
                    mArmImage.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                } else if (mPreviousSelect == THIRD) {
                    mHipText.setTextColor(getResources().getColor(R.color.colorTabUnselect));
                    mHipImage.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                }
                break;
            case SECOND:
                mArmText.setTextColor(getResources().getColor(R.color.colorUpTabSelect));
                mArmImage.setBackgroundColor(getResources().getColor(R.color.colorUpTabSelect));
                if (mPreviousSelect == FIRST) {
                    mCoreText.setTextColor(getResources().getColor(R.color.colorTabUnselect));
                    mCoreImage.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                } else if (mPreviousSelect == THIRD) {
                    mHipText.setTextColor(getResources().getColor(R.color.colorTabUnselect));
                    mHipImage.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                }
                break;
            case THIRD:
                mHipText.setTextColor(getResources().getColor(R.color.colorUpTabSelect));
                mHipImage.setBackgroundColor(getResources().getColor(R.color.colorUpTabSelect));
                if (mPreviousSelect == FIRST) {
                    mCoreText.setTextColor(getResources().getColor(R.color.colorTabUnselect));
                    mCoreImage.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                } else if (mPreviousSelect == SECOND) {
                    mArmText.setTextColor(getResources().getColor(R.color.colorTabUnselect));
                    mArmImage.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                }
                break;
        }
    }
}
