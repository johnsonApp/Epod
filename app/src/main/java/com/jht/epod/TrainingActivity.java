package com.jht.epod;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jht.epod.ble.BleService;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TrainingActivity extends Activity {

    private static final String TAG = "TrainingActivity";

    private final String SELECT_MODE = "select_mode";

    private static final int SERVICE_BIND = 1;
    private static final int UPDATE_HOMEGYM_COUNT = 2;
    private static final int UPDATE_ACCESSORY_COUNT = 3;
    private static final int MSG_DUMBBELL = 4;
    private static final int MSG_ROPE_SKIP = 5;
    private static final int TIME_CHANGE = 6;
    private static final int REST_TIME_CHANGE = 7;
    private static final int MSG_REQUEST_ACCESSORY_MODE = 8;
    private static final int MSG_SWITCH_PAUSE = 9;
    private static final int MSG_SWITCH_TRAINING = 10;
    private static final int MSG_SWITCH_STOP = 11;

    private TextView mCountDown;

    private long mStartTime = 0L;
    private long mRestTime = 0L;
    private int mTotalTime = 0;
    private Timer mTimer = new Timer();
    private TimerTask mTimerTask;

    //private Query<FreeTraining> mFreeTrainingQuery;


    private BleService mBleService;

    private BluetoothGattCharacteristic mAccessoryReadCharacteristic;
    private BluetoothGattCharacteristic mAccessoryWriteCharacteristic;
    private BluetoothGattCharacteristic mAccessorySensorCharacteristic;


    private TextView mTrainingTime;
    private ImageView mBackButton;

    private StringBuilder mLogData;

    private Handler mHandler = new Handler() {
        boolean result = false;
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SERVICE_BIND:
                    setBleServiceListener();
                    break;
                case UPDATE_HOMEGYM_COUNT:
                    break;
                case UPDATE_ACCESSORY_COUNT:

                    //mRopeSkippingValue.setText(String.valueOf(mRopeSkipExerciseCounter));
                    break;
                case MSG_DUMBBELL:

                    break;
                case MSG_ROPE_SKIP:

                    break;
                case MSG_REQUEST_ACCESSORY_MODE:
                    break;
                case TIME_CHANGE:
                    //mTrainingTime.setText(String.valueOf(msg.obj));
                    break;
                case REST_TIME_CHANGE:
                    mTrainingTime.setText(String.valueOf(msg.obj));
                    Log.i(TAG,"REST_TIME_CHANGE " + String.valueOf(msg.obj));
                    break;
                case MSG_SWITCH_PAUSE:
                    switchToPause();
                    break;
                case MSG_SWITCH_TRAINING:
                    switchToTraining();
                    break;
                case MSG_SWITCH_STOP:
                    finishTraining();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        setContentView(R.layout.activity_training);
        mCountDown = (TextView) findViewById(R.id.strength_num);
        mTrainingTime = (TextView) findViewById(R.id.trainingg_time);
        mBackButton = (ImageView) findViewById(R.id.back);
        mBackButton.setOnClickListener(listener);
        mStartTime = SystemClock.elapsedRealtime();

        switchToPause();
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    finish();
                    break;
            }
        }
    };

    SeekBar.OnSeekBarChangeListener resistanceListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void onStart(){
        super.onStart();
        doBindService();
    }

    public void onStop(){
        super.onStop();
        doUnBindService();
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBleService = ((BleService.LocalBinder) service).getService();
            Log.d(TAG,"onServiceConnected " + (mBleService == null));
            if(null != mBleService) {
                mHandler.sendEmptyMessage(SERVICE_BIND);
            }
            if (!mBleService.initialize()) {
                Toast.makeText(TrainingActivity.this, "not support Bluetooth", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBleService = null;
        }
    };

    private void doBindService() {
        Log.d(TAG,"doBindService " );
        Intent serviceIntent = new Intent(this, BleService.class);
        bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void doUnBindService() {
        unbindService(mServiceConnection);
        mBleService = null;

    }

    private void setBleServiceListener() {
        mBleService.setOnConnectListener(new BleService.OnConnectionStateChangeListener() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                if (newState == BluetoothProfile.STATE_DISCONNECTED) {

                } else if (newState == BluetoothProfile.STATE_CONNECTING) {

                } else if (newState == BluetoothProfile.STATE_CONNECTED) {

                } else if (newState == BluetoothProfile.STATE_DISCONNECTING) {

                }
            }
        });
        mBleService.setOnServicesDiscoveredListener(new BleService.OnServicesDiscoveredListener() {
            @Override
            public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
                Log.i(TAG, "Service discovery completed");

            }

        });
        mBleService.setOnDataAvailableListener(new BleService.OnDataAvailableListener() {
            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                Log.d(TAG,"onCharacteristicRead");
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                Log.d(TAG,"onCharacteristicChanged");
                String temp = logData(characteristic.getValue());
                mCountDown.setText(temp);
            }

            @Override
            public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                Log.d(TAG,"onDescriptorRead");
            }
        });
    }

    private String logData(byte[] data){
        StringBuilder logTemp = new StringBuilder("");
        int length = data.length;
        int temp = 0;
        String string;
        for(int i = 0; i< length; i++){
            temp = data[i] & 0xFF;
            string = Integer.toHexString(temp);
            if(string.length() < 2){
                logTemp.append(0);
            }
            logTemp.append(string);
        }
        Log.d(TAG,"logData ~~~~  " + logTemp.toString());
        return logTemp.toString();
    }
    private void readCharacteristic(String addr, BluetoothGattCharacteristic characteristic, long notifyDealy, long readDealy){
        final String address = addr;
        final BluetoothGattCharacteristic readCharacteristic = characteristic;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(null != mBleService) {
                    mBleService.setCharacteristicNotification(readCharacteristic, true);
                }
            }
        }, notifyDealy);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(null != mBleService) {
                    mBleService.readCharacteristic(readCharacteristic);
                }
            }
        }, readDealy);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    public void onBackPressed() {
        super.onBackPressed();
        finishTraining();
    }

    private void switchToPause(){
        mTimer.cancel();
        mTimer.purge();
        mRestTime = SystemClock.elapsedRealtime();
        mHandler.sendMessage(mHandler.obtainMessage(REST_TIME_CHANGE, "00:00:00"));
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendMessage(mHandler.obtainMessage(REST_TIME_CHANGE, timeReversal(SystemClock.elapsedRealtime()- mRestTime)));
            }
        };
        mTimer.schedule(mTimerTask, 1000, 1000);
        //mTotalTime = mRestTime - mStartTime + mTotalTime;
    }

    private void switchToTraining(){
        mTimer.cancel();
        mTimer.purge();
        mStartTime = SystemClock.elapsedRealtime();
        /*mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendMessage(mHandler.obtainMessage(TIME_CHANGE, timeReversal(SystemClock.elapsedRealtime()- mStartTime + mTotalTime)));
            }
        };
        mTimer.schedule(mTimerTask, 1000, 1000);*/
    }

    private void finishTraining(){
        mTimer.cancel();
        mTimer.purge();
        /*FreeTraining freeTraining = new FreeTraining();
        freeTraining.setUserId(1);
        freeTraining.setCurTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
        freeTraining.setTotalTime((int)(mTotalTime));
        freeTraining.setDumbbellNum(mDumbbellExerciseCounter);
        freeTraining.setSkipRopeNum(mRopeSkipExerciseCounter);
        freeTraining.setPullRopeNum(mHomegymExerciseCounter);
        freeTraining.setTotalNum(mTotalExerciseCounter);
        freeTraining.setLevel(mResistanceBar.getProgress());
        long id = mFreeTrainingBox.put(freeTraining);
        Intent intent = new Intent(TrainingActivity.this, SummaryActivity.class);
        intent.putExtra("TRAINING_ID", id);
        startActivity(intent);*/
        finish();
    }

    private String timeReversal(long time){
        time = time / 1000;
        String hh = new DecimalFormat("00").format(time / 3600);
        String mm = new DecimalFormat("00").format(time % 3600 / 60);
        String ss = new DecimalFormat("00").format(time % 60);
        return hh + ":" + mm + ":" + ss;
    }

    private String timeReversal(int min, int sec){
        String hh = new DecimalFormat("00").format(min / 60);
        String mm = new DecimalFormat("00").format(min % 60);
        String ss = new DecimalFormat("00").format(sec);
        return hh + ":" + mm + ":" + ss;
    }

    @Override
    public void finish() {
        mTimer.cancel();
        super.finish();
    }

}
