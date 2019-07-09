package com.jht.epod.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jht.epod.R;
import com.jht.epod.utils.Utils;

public class ClassListActivity extends Activity {

    private boolean mClassType;
    private int mSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        findViewById(R.id.back).setOnClickListener(mViewListener);
        findViewById(R.id.class_core).setOnClickListener(mViewListener);
        findViewById(R.id.class_arm).setOnClickListener(mViewListener);
        findViewById(R.id.class_hip).setOnClickListener(mViewListener);
        findViewById(R.id.class_junior).setOnClickListener(mViewListener);
        findViewById(R.id.class_medium).setOnClickListener(mViewListener);
        findViewById(R.id.class_senior).setOnClickListener(mViewListener);
    }

    View.OnClickListener mViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    finish();
                    break;
                case R.id.class_core:
                    mClassType = true;
                    mSelected = Utils.TYPE_CORE;
                    startClassActivity();
                    break;
                case R.id.class_arm:
                    mClassType = true;
                    mSelected = Utils.TYPE_ARM;
                    startClassActivity();
                    break;
                case R.id.class_hip:
                    mClassType = true;
                    mSelected = Utils.TYPE_HIP;
                    startClassActivity();
                    break;
                case R.id.class_junior:
                    mClassType = false;
                    mSelected = Utils.DEGREE_JUNIOR;
                    startClassActivity();
                    break;
                case R.id.class_medium:
                    mClassType = false;
                    mSelected = Utils.DEGREE_MEDIUM;
                    startClassActivity();
                    break;
                case R.id.class_senior:
                    mClassType = false;
                    mSelected = Utils.DEGREE_SENIOR;
                    startClassActivity();
                    break;
            }
        }
    };

    private void startClassActivity() {
        Intent  intent = new Intent(this, ClassClassifyActivity.class);
        intent.putExtra(Utils.CLASSTYPE,mClassType);
        intent.putExtra(Utils.DEGREE, mSelected);
        startActivity(intent);
    }
}
