package com.jht.epod.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
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

public class ClassClassifyActivity extends Activity {
    private static final String TAG = "ClassClassifyActivity";

    private int mCurrentSelect = Utils.TYPE_CORE;
    private int mPreviousSelect = 0;

    private LinearLayout mCoreTab;
    private TextView mCoreText;
    private ImageView mCoreImage;

    private LinearLayout mArmTab;
    private TextView mArmText;
    private ImageView mArmImage;

    private LinearLayout mHipTab;
    private TextView mHipText;
    private ImageView mHipImage;

    private LinearLayout mJuniorTab;
    private TextView mJuniorText;
    private ImageView mJuniorImage;

    private LinearLayout mMediumTab;
    private TextView mMediumText;
    private ImageView mMediumImage;

    private LinearLayout mSeniorTab;
    private TextView mSeniorText;
    private ImageView mSeniorImage;

    private MeasureListView mListClass;

    private ArrayList<ClassData> mCoreData;
    private ArrayList<ClassData> mArmData;
    private ArrayList<ClassData> mHipData;
    private ArrayList<ClassData> mJuniorData;
    private ArrayList<ClassData> mMediumData;
    private ArrayList<ClassData> mSeniorData;

    private SimpleAdapter mCoreAdapter;
    private SimpleAdapter mArmAdapter;
    private SimpleAdapter mHipAdapter;
    private SimpleAdapter mJuniorAdapter;
    private SimpleAdapter mMediumAdapter;
    private SimpleAdapter mSeniorAdapter;

    private int mClassification = Utils.CLASS_TYPE_BODY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            mClassification = extras.getInt(Utils.CLASSIFICATION, Utils.CLASS_TYPE_BODY);
            if (mClassification == Utils.CLASS_TYPE_LEVEL) {
                mCurrentSelect = extras.getInt(Utils.DEGREE, Utils.DEGREE_JUNIOR + 3);
            } else {
                mCurrentSelect = extras.getInt(Utils.CLASSTYPE, Utils.TYPE_CORE);
            }
        }

        initClassData();
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_class_classify);

        findViewById(R.id.back).setOnClickListener(mViewListener);

        mCoreTab = findViewById(R.id.core_tab);
        mCoreText = findViewById(R.id.core_tab_txt);
        mCoreImage = findViewById(R.id.core_tab_pic);

        mArmTab = findViewById(R.id.arm_tab);
        mArmText = findViewById(R.id.arm_tab_txt);
        mArmImage = findViewById(R.id.arm_tab_pic);

        mHipTab = findViewById(R.id.hip_tab);
        mHipText = findViewById(R.id.hip_tab_txt);
        mHipImage = findViewById(R.id.hip_tab_pic);

        mJuniorTab = findViewById(R.id.junior_tab);
        mJuniorText = findViewById(R.id.junior_tab_txt);
        mJuniorImage = findViewById(R.id.junior_tab_pic);

        mMediumTab = findViewById(R.id.medium_tab);
        mMediumText = findViewById(R.id.medium_tab_txt);
        mMediumImage = findViewById(R.id.medium_tab_pic);

        mSeniorTab = findViewById(R.id.senior_tab);
        mSeniorText = findViewById(R.id.senior_tab_txt);
        mSeniorImage = findViewById(R.id.senior_tab_pic);

        mCoreTab.setOnClickListener(mViewListener);
        mArmTab.setOnClickListener(mViewListener);
        mHipTab.setOnClickListener(mViewListener);
        mJuniorTab.setOnClickListener(mViewListener);
        mMediumTab.setOnClickListener(mViewListener);
        mSeniorTab.setOnClickListener(mViewListener);

        switch (mClassification) {
            case Utils.CLASS_TYPE_BODY:
                mCoreTab.setVisibility(View.VISIBLE);
                mArmTab.setVisibility(View.VISIBLE);
                mHipTab.setVisibility(View.VISIBLE);
                mJuniorTab.setVisibility(View.GONE);
                mMediumTab.setVisibility(View.GONE);
                mSeniorTab.setVisibility(View.GONE);
                break;
            case Utils.CLASS_TYPE_LEVEL:
                mCoreTab.setVisibility(View.GONE);
                mArmTab.setVisibility(View.GONE);
                mHipTab.setVisibility(View.GONE);
                mJuniorTab.setVisibility(View.VISIBLE);
                mMediumTab.setVisibility(View.VISIBLE);
                mSeniorTab.setVisibility(View.VISIBLE);
                break;
            case Utils.CLASS_TYPE_ALL:
                mCoreTab.setVisibility(View.VISIBLE);
                mArmTab.setVisibility(View.VISIBLE);
                mHipTab.setVisibility(View.VISIBLE);
                mJuniorTab.setVisibility(View.VISIBLE);
                mMediumTab.setVisibility(View.VISIBLE);
                mSeniorTab.setVisibility(View.VISIBLE);
                mCoreText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                mArmText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                mHipText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                mJuniorText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                mMediumText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                mSeniorText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                break;
        }

        mListClass = findViewById(R.id.list_class);
        mListClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView storeId = view.findViewById(R.id.store_id);
                Log.i(TAG,"onItemClick get store id " + storeId.getText());
                int classId = Integer.parseInt(storeId.getText().toString());
                startClassActivity(classId);
            }
        });

        upDataState();
    }

    private void startClassActivity(int id) {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra(Utils.ID,id);
        startActivity(intent);
        finish();
    }

    private void initClassData() {
        switch (mClassification) {
            case Utils.CLASS_TYPE_BODY:
                mCoreData = ClassDataManager.queryClassByType(Utils.TYPE_CORE);
                mArmData = ClassDataManager.queryClassByType(Utils.TYPE_ARM);
                mHipData = ClassDataManager.queryClassByType(Utils.TYPE_HIP);
                break;
            case Utils.CLASS_TYPE_LEVEL:
                mJuniorData = ClassDataManager.queryClassByDegree(Utils.DEGREE_JUNIOR);
                mMediumData = ClassDataManager.queryClassByDegree(Utils.DEGREE_MEDIUM);
                mSeniorData = ClassDataManager.queryClassByDegree(Utils.DEGREE_SENIOR);
                break;
            case Utils.CLASS_TYPE_ALL:
                mCoreData = ClassDataManager.queryClassByType(Utils.TYPE_CORE);
                mArmData = ClassDataManager.queryClassByType(Utils.TYPE_ARM);
                mHipData = ClassDataManager.queryClassByType(Utils.TYPE_HIP);
                mJuniorData = ClassDataManager.queryClassByDegree(Utils.DEGREE_JUNIOR);
                mMediumData = ClassDataManager.queryClassByDegree(Utils.DEGREE_MEDIUM);
                mSeniorData = ClassDataManager.queryClassByDegree(Utils.DEGREE_SENIOR);
                break;
        }
    }

    private void upDataListData() {
        mListClass.setAdapter(getAdapter());
    }

    private SimpleAdapter getAdapter() {
        SimpleAdapter adapter = null;
        switch (mCurrentSelect) {
            case Utils.TYPE_CORE:
                if(null == mCoreAdapter) {
                    mCoreAdapter = initAdapter();
                }
                adapter = mCoreAdapter;
                break;
            case Utils.TYPE_ARM:
                if(null == mArmAdapter) {
                    mArmAdapter = initAdapter();
                }
                adapter = mArmAdapter;
                break;
            case Utils.TYPE_HIP:
                if(null == mHipAdapter) {
                    mHipAdapter = initAdapter();
                }
                adapter = mHipAdapter;
                break;
            case Utils.DEGREE_JUNIOR + 3:
                if(null == mJuniorAdapter) {
                    mJuniorAdapter = initAdapter();
                }
                adapter = mJuniorAdapter;
                break;
            case Utils.DEGREE_MEDIUM + 3:
                if(null == mMediumAdapter) {
                    mMediumAdapter = initAdapter();
                }
                adapter = mMediumAdapter;
                break;
            case Utils.DEGREE_SENIOR + 3:
                if(null == mSeniorAdapter) {
                    mSeniorAdapter = initAdapter();
                }
                adapter = mSeniorAdapter;
                break;
        }
        return adapter;
    }

    private SimpleAdapter initAdapter() {
        ArrayList<HashMap<String, Object>> listClassValue = new ArrayList<>();
        ArrayList<ClassData> list = getDataBySelected();
        for(ClassData data : list) {
            HashMap<String, Object> classValue = new HashMap<>();
            classValue.put("classPic", getImageResource(data.getIconName()));
            classValue.put("classTitle", data.getName());
            classValue.put("classSubtitle1", data.getTime() + getResources().getString(R.string.minutes));
            classValue.put("classSubtitle2", data.getCalorie() + getResources().getString(R.string.calorie));
            classValue.put("classSubtitle3", getStringByDegree(data.getDegree()));
            classValue.put("storeId", data.getId());
            listClassValue.add(classValue);
            Log.i(TAG,"initAdapter " + data.toString());
        }
        return new SimpleAdapter(this, listClassValue,
                R.layout.list_recommended_course,
                new String[]{"classPic", "classTitle", "classSubtitle1",
                        "classSubtitle2", "classSubtitle3", "storeId"},
                new int[]{R.id.class_pic, R.id.class_title, R.id.class_subtitle1,
                        R.id.class_subtitle2, R.id.class_subtitle3, R.id.store_id});
    }

    private ArrayList<ClassData> getDataBySelected(){
        ArrayList<ClassData> list = null;
        switch (mCurrentSelect) {
            case Utils.TYPE_CORE:
                list = mCoreData;
                break;
            case Utils.TYPE_ARM:
                list = mArmData;
                break;
            case Utils.TYPE_HIP:
                list = mHipData;
                break;
            case Utils.DEGREE_JUNIOR + 3:
                list = mJuniorData;
                break;
            case Utils.DEGREE_MEDIUM + 3:
                list = mMediumData;
                break;
            case Utils.DEGREE_SENIOR + 3:
                list = mSeniorData;
                break;
        }
        return list;
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
        return getResources().getIdentifier(imageName,"drawable",this.getPackageName());
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
                    mCurrentSelect = Utils.TYPE_CORE;
                    if(mCurrentSelect == mPreviousSelect){
                        return;
                    }
                    upDataState();
                    break;
                case R.id.arm_tab:
                    mPreviousSelect = mCurrentSelect;
                    mCurrentSelect = Utils.TYPE_ARM;
                    if(mCurrentSelect == mPreviousSelect){
                        return;
                    }
                    upDataState();
                    break;
                case R.id.hip_tab:
                    mPreviousSelect = mCurrentSelect;
                    mCurrentSelect = Utils.TYPE_HIP;
                    if(mCurrentSelect == mPreviousSelect){
                        return;
                    }
                    upDataState();
                    break;
                case R.id.junior_tab:
                    mPreviousSelect = mCurrentSelect;
                    mCurrentSelect = Utils.DEGREE_JUNIOR + 3;
                    if(mCurrentSelect == mPreviousSelect){
                        return;
                    }
                    upDataState();
                    break;
                case R.id.medium_tab:
                    mPreviousSelect = mCurrentSelect;
                    mCurrentSelect = Utils.DEGREE_MEDIUM + 3;
                    if(mCurrentSelect == mPreviousSelect){
                        return;
                    }
                    upDataState();
                    break;
                case R.id.senior_tab:
                    mPreviousSelect = mCurrentSelect;
                    mCurrentSelect = Utils.DEGREE_SENIOR + 3;
                    if(mCurrentSelect == mPreviousSelect){
                        return;
                    }
                    upDataState();
                    break;
            }
        }
    };

    private void upDataState(){
        //切换卡片功能实现
        upDataListData();
        //切换卡片UI实现
        upDataTab();
    }

    private void upDataTab(){
        switch (mCurrentSelect) {
            case Utils.TYPE_CORE:
                mCoreText.setTextColor(getResources().getColor(R.color.colorUpTabSelect));
                mCoreImage.setBackgroundColor(getResources().getColor(R.color.colorUpTabSelect));
                unSelectedTabUIChange();
                break;
            case Utils.TYPE_ARM:
                mArmText.setTextColor(getResources().getColor(R.color.colorUpTabSelect));
                mArmImage.setBackgroundColor(getResources().getColor(R.color.colorUpTabSelect));
                unSelectedTabUIChange();
                break;
            case Utils.TYPE_HIP:
                mHipText.setTextColor(getResources().getColor(R.color.colorUpTabSelect));
                mHipImage.setBackgroundColor(getResources().getColor(R.color.colorUpTabSelect));
                unSelectedTabUIChange();
                break;
            case Utils.DEGREE_JUNIOR + 3:
                mJuniorText.setTextColor(getResources().getColor(R.color.colorUpTabSelect));
                mJuniorImage.setBackgroundColor(getResources().getColor(R.color.colorUpTabSelect));
                unSelectedTabUIChange();
                break;
            case Utils.DEGREE_MEDIUM + 3:
                mMediumText.setTextColor(getResources().getColor(R.color.colorUpTabSelect));
                mMediumImage.setBackgroundColor(getResources().getColor(R.color.colorUpTabSelect));
                unSelectedTabUIChange();
                break;
            case Utils.DEGREE_SENIOR + 3:
                mSeniorText.setTextColor(getResources().getColor(R.color.colorUpTabSelect));
                mSeniorImage.setBackgroundColor(getResources().getColor(R.color.colorUpTabSelect));
                unSelectedTabUIChange();
                break;
        }
    }

    private void unSelectedTabUIChange () {
        switch (mPreviousSelect) {
            case Utils.TYPE_CORE:
                mCoreText.setTextColor(getResources().getColor(R.color.colorTabUnselect));
                mCoreImage.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                break;
            case Utils.TYPE_ARM:
                mArmText.setTextColor(getResources().getColor(R.color.colorTabUnselect));
                mArmImage.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                break;
            case Utils.TYPE_HIP:
                mHipText.setTextColor(getResources().getColor(R.color.colorTabUnselect));
                mHipImage.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                break;
            case Utils.DEGREE_JUNIOR + 3:
                mJuniorText.setTextColor(getResources().getColor(R.color.colorTabUnselect));
                mJuniorImage.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                break;
            case Utils.DEGREE_MEDIUM + 3:
                mMediumText.setTextColor(getResources().getColor(R.color.colorTabUnselect));
                mMediumImage.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                break;
            case Utils.DEGREE_SENIOR + 3:
                mSeniorText.setTextColor(getResources().getColor(R.color.colorTabUnselect));
                mSeniorImage.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                break;
        }
    }
}
