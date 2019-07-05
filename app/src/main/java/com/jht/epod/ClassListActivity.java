package com.jht.epod;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ClassListActivity extends Activity {

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
                case R.id.class_arm:
                case R.id.class_hip:
                case R.id.class_junior:
                case R.id.class_medium:
                case R.id.class_senior:
                    startClassActivity();
                    break;
            }
        }
    };

    private void startClassActivity() {
        startActivity(new Intent(this, ClassClassifyActivity.class));
    }
}
