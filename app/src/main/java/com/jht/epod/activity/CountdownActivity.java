package com.jht.epod.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jht.epod.R;
import com.jht.epod.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

public class CountdownActivity extends Activity {
    private ImageView mBack;
    private TextView mCountDown;
    private int mCurValue = 3;
    private TimerTask mTimerTask;
    private Timer mTimer = new Timer();
    private MyHandler mHandler = new MyHandler(this);
    private Intent mIntent;
    private long mId = 1;

    class MyHandler extends Handler {
        WeakReference<CountdownActivity> mActivity;

        MyHandler(CountdownActivity activity){
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CountdownActivity activity = mActivity.get();
            if (activity != null){
                if (msg.what == 0){
                    activity.mCountDown.setText(String.valueOf(--activity.mCurValue));
                    if (activity.mCurValue == 0){
                        CountdownActivity.this.finish();
                        //倒计时结束进入锻炼页面
                        Intent intent = new Intent(CountdownActivity.this, TrainingActivity.class);
                        intent.putExtra(Utils.ID, mId);
                        startActivity(intent);
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        mIntent = getIntent();
        Bundle extras = mIntent.getExtras();
        if(null != extras) {
            String title = extras.getString(Utils.NAME,"");
            if(!isStringNull(title)){
                TextView textView = findViewById(R.id.title);
                textView.setText(title);
            }
            mId = extras.getLong(Utils.ID);
        }

        mBack = findViewById(R.id.back);
        mBack.setOnClickListener(listener);
        mCountDown = findViewById(R.id.count_down);
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        };
        mTimer.schedule(mTimerTask, 1000, 1000);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.back){
                CountdownActivity.this.finish();
            }
        }
    };

    private boolean isStringNull(String string) {
        boolean result = false;
        if(null == string || string.equals("") || string.equals(" ")) {
            result = true;
        }
        return result;
    }

    @Override
    public void finish() {
        mTimer.cancel();
        mTimer.purge();
        super.finish();
    }
}
