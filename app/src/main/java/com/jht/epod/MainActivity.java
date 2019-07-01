package com.jht.epod;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jht.epod.ble.BLECommand;
import com.jht.epod.ble.BleService;
import com.jht.epod.ble.Constants;
import com.jht.epod.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";

    private UUID[] AccessoryUUID = {Utils.EPOD_BASE_UUID};

    private static final String ADDRESS = "address";
    private static final String DEVICE_NAME ="name";
    private static final String IS_CONNECTED ="isConnect";

    public static final int SERVICE_BIND = 1;
    public static final int CONNECT_CHANGE = 2;
    public static final int MSG_REQUEST_ACCESSORY_MODE = 3;
    public static final int UPDATE_CONNECT_STATUS = 4;
    public static final int UPDATE_SELECTED_NUM = 5;

    private boolean mIsBind;
    private List<Map<String, Object>> mDeviceList;
    private BleService mBleService;

    private boolean mIsConnected = false;

    private BluetoothGattCharacteristic mAccessoryReadCharacteristic;
    private BluetoothGattCharacteristic mAccessoryWriteCharacteristic;
    private BluetoothGattCharacteristic mAccessorySensorCharacteristic;

    private LinearLayout mStartClassButton;
    private AlertDialog mAlertDialog;

    protected BroadcastReceiver mBleReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.ACTION_BLUETOOTH_DEVICE)) {
                String tmpDevName = intent.getStringExtra(DEVICE_NAME);
                String tmpDevAddress = intent.getStringExtra(ADDRESS);
                Log.i(TAG, "name: " + tmpDevName + ", address: " + tmpDevAddress);
                HashMap<String, Object> deviceMap = new HashMap<>();
                deviceMap.put(DEVICE_NAME, tmpDevName);
                deviceMap.put(ADDRESS, tmpDevAddress);
                deviceMap.put(IS_CONNECTED, false);
                addDevice(deviceMap);
                //mDeviceList.add(deviceMap);
                //mDeviceAdapter.notifyDataSetChanged();
            } else if (intent.getAction().equals(Constants.ACTION_GATT_CONNECTED)) {
                Log.i(TAG, "onReceive: CONNECTED: getConnectNum() " + getConnectNum());
                if(getConnectNum() > 0) {
                    mIsConnected = true;
                }
                dismissDialog();
                updateUI(Constants.STATE_CONNECTED);
                Toast.makeText(MainActivity.this,"连接成功",Toast.LENGTH_LONG).show();
            } else if (intent.getAction().equals(Constants.ACTION_GATT_DISCONNECTED)) {
                Log.i(TAG, "onReceive: DISCONNECTED: getConnectNum() " + getConnectNum());
                if(getConnectNum() == 0){
                    mIsConnected = false;
                }
                dismissDialog();
                updateUI(Constants.STATE_DISCONNECTED);
            } else if (intent.getAction().equals(Constants.ACTION_SCAN_FINISHED)) {
                //btn_scanBle.setEnabled(true);
                Log.i(TAG,"ACTION_SCAN_FINISHED ");
                updateUI(Constants.STATE_SCAN_FINISH);
                dismissDialog();
                connectDevice();
            }
        }
    };

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG," onServiceConnected ");
            mBleService = ((BleService.LocalBinder) service).getService();
            mIsBind = true;
            if (mBleService != null) mHandler.sendEmptyMessage(SERVICE_BIND);
            if (mBleService.initialize()) {
                if (mBleService.enableBluetooth(true)) {
                    if(Utils.handleVersionPermission(MainActivity.this)){
                        List<BluetoothDevice> list = mBleService.getConnectDevices();
                        Log.e(TAG,"onServiceConnected getConnectDevices " + list.size());
                        if (list == null || list.size() == 0){
                            setScan(true);
                        }else {
                            mIsConnected = true;
                            connectChanged();
                        }
                    }
                    //Toast.makeText(BleActivity.this, "Bluetooth was opened", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e(TAG, "not support Bluetooth");
                //Toast.makeText(BleActivity.this, "not support Bluetooth", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBleService = null;
            mIsBind = false;
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SERVICE_BIND:
                    setBleServiceListener();
                    break;
                case CONNECT_CHANGE:
                    //deviceAdapter.notifyDataSetChanged();
                    Log.i(TAG, "handleMessage: " + mBleService.getConnectDevices().toString());
                    break;
                case MSG_REQUEST_ACCESSORY_MODE:
                    boolean result = getAccessoryMode();
                    Log.i(TAG,"MSG_REQUEST_ACCESSORY_MODE result " + result);
                    break;
                case UPDATE_CONNECT_STATUS:
                    break;
                case UPDATE_SELECTED_NUM:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDeviceList = new ArrayList<Map<String, Object>>();
        mStartClassButton = findViewById(R.id.start_class_button);
        mStartClassButton.setOnClickListener(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mBleReceiver, makeIntentFilter());
        //doBindService();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start_class_button:
                startClass();
                Log.i(TAG,"start class button");
                break;
            case R.id.back:
                finish();
                break;
        }
    }


    private void setBleServiceListener() {
        mBleService.setOnConnectListener(new BleService.OnConnectionStateChangeListener() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    for (int i = 0; i < mDeviceList.size(); i++) {
                        HashMap<String, Object> devMap = (HashMap<String, Object>) mDeviceList.get(i);
                        if (devMap.get(ADDRESS).toString().equals(gatt.getDevice().getAddress())) {
                            ((HashMap) mDeviceList.get(i)).put(IS_CONNECTED, false);
                            return;
                        }
                    }

                } else if (newState == BluetoothProfile.STATE_CONNECTING) {

                } else if (newState == BluetoothProfile.STATE_CONNECTED) {
                    for (int i = 0; i < mDeviceList.size(); i++) {
                        HashMap<String, Object> devMap = (HashMap<String, Object>) mDeviceList.get(i);
                        if (devMap.get(ADDRESS).toString().equals(gatt.getDevice().getAddress())) {
                            ((HashMap) mDeviceList.get(i)).put(IS_CONNECTED, true);
                            return;
                        }
                    }

                } else if (newState == BluetoothProfile.STATE_DISCONNECTING) {

                }
            }
        });

        mBleService.setOnServicesDiscoveredListener(new BleService.OnServicesDiscoveredListener() {
            @Override
            public void onServicesDiscovered(final BluetoothGatt gatt, int status){
                if (status != BluetoothGatt.GATT_SUCCESS) {
                    Log.v(TAG, "Service discovery error: " + status);
                    return;
                }
                Log.v(TAG, "Service discovery completed");
                final String address = gatt.getDevice().getAddress();
                BluetoothGattService accessoryGattService = gatt.getService(Utils.EPOD_BASE_UUID);
                if (accessoryGattService != null) {
                    Log.d(TAG,"find accessory service");
                    mAccessoryWriteCharacteristic = accessoryGattService.getCharacteristic(Utils.EPOD_WRITE_UUID);
                    if(null != mAccessoryWriteCharacteristic) {
                        Log.i(TAG,"find accessory  write characteristic ");
                        //mHandler.sendEmptyMessageDelayed(MSG_REQUEST_ACCESSORY_MODE,500);
                    }

                    mAccessoryReadCharacteristic = accessoryGattService.getCharacteristic(Utils.EPOD_READ_UUID);
                    if(null != mAccessoryReadCharacteristic) {
                        readCharacteristic(address, mAccessoryReadCharacteristic,0,200);
                        Log.i(TAG,"find accessory  read characteristic");
                    }

                }

                connectChanged();
            }
        });
       /* mBleService.setOnDataAvailableListener(new BleService.OnDataAvailableListener() {
            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                Log.i(TAG,"onCharacteristicRead");
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                byte[] data = characteristic.getValue();

                if (characteristic.equals(mAccessoryReadCharacteristic)) {
                    Log.i(TAG,"get onCharacteristicChanged");
                    logData(data);
                }

            }

            @Override
            public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                Log.d(TAG,"onDescriptorRead");
            }
        });*/
    }


    private void readCharacteristic(String addr, BluetoothGattCharacteristic characteristic,long notifyDealy, long readDealy){
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

    public void setScan(boolean scan){
        if(null == mProgressDialog || !mProgressDialog.isShowing()) {
            showDialog(getResources().getString(R.string.scanning));
        }
        if (scan && mBleService.isScanning()) {
            mBleService.scanLeDevice(false);
        }
        mBleService.scanLeDevice(scan, AccessoryUUID);

/*        if(!mBleService.isScanning()){
            dismissDialog();
            connectDevice();
        }*/
        //mConnect.setEnabled(!scan);
    }


    public void stopScan(){
        if (null != mBleService && mBleService.isScanning()) {
            mBleService.scanLeDevice(false);
        }
    }


    @Override
    public void onBackPressed() {
        if (null != mBleService && mBleService.isScanning()) {
            mBleService.scanLeDevice(false);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != mBleService && mBleService.isScanning()) {
            mBleService.scanLeDevice(false);
        }
        //doUnBindService();
        unregisterReceiver(mBleReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnBindService();
    }

    private void doBindService() {
        Intent serviceIntent = new Intent(this, BleService.class);
        boolean result = bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "doBindService result " + result);
    }

    private void doUnBindService() {
        if (mIsBind) {
            unbindService(mServiceConnection);
            mBleService = null;
            mIsBind = false;
        }
    }

    private static IntentFilter makeIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_BLUETOOTH_DEVICE);
        intentFilter.addAction(Constants.ACTION_GATT_CONNECTED);
        intentFilter.addAction(Constants.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(Constants.ACTION_SCAN_FINISHED);
        return intentFilter;
    }

    private ProgressDialog mProgressDialog;

    private void showDialog(String message) {
        if(null != mProgressDialog && mProgressDialog.isShowing()) {
            dismissDialog();
        }
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(mProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage(message);
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                Log.e(TAG,"Dialog  on BackPress");
                if (null != mBleService && mBleService.isScanning()) {
                    mBleService.scanLeDevice(false);
                    return;
                }
            }
        });
        mProgressDialog.show();
    }

    private void dismissDialog() {
        if (mProgressDialog == null) return;
        mProgressDialog.dismiss();
        mProgressDialog = null;
    }

    private void showAlertDialog(){
        if(null == mAlertDialog){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("未连接到设备");
            builder.setPositiveButton("重新连接", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    connectDevice();
                }
            });
            builder.setNegativeButton("取消",null);
            mAlertDialog = builder.create();
        }
        mAlertDialog.show();
    }
    public void setConnect(){
        int size = mDeviceList.size();
        if(size == 0) {
            setScan(true);
        }else if (size > 0){
            connectDevice();
        }
    }

    public void setDisconnect(){
        int size = mDeviceList.size();
        if(0 == size){
            dismissDialog();
            Log.i(TAG,"connectDevice make Toast");
            Toast.makeText(this,"Can not find any device ,please retry",Toast.LENGTH_LONG);
            return;
        }
        mBleService.disconnect();

    }

    private void connectDevice() {
        if (mDeviceList != null){
            int size = mDeviceList.size();
            if(0 == size){
                Log.i(TAG,"connectDevice make Toast");
                //Toast.makeText(this,"Can not find any device ,please retry",Toast.LENGTH_LONG).show();
                return;
            }
            for (int i = 0; i < size; i++) {
                HashMap<String, Object> devMap = (HashMap<String, Object>) mDeviceList.get(i);
                String address = devMap.get(ADDRESS).toString();
                Log.i(TAG,"connectDevice " + address);
                if(!(boolean)devMap.get(IS_CONNECTED)) {
                    showDialog(getResources().getString(R.string.connecting));
                    boolean result =  mBleService.connect(address);
                    Log.e(TAG,"connectDevice result " + result);
                    if(!result){
                        dismissDialog();
                        showAlertDialog();
                    }
                }
            }
            Log.i(TAG,"connectDevice " + size);
        }

    }

    private int getConnectNum(){
        int num = 0;
        for (int i = 0; i < mDeviceList.size(); i++) {
            boolean isConnect =  (Boolean) (mDeviceList.get(i)).get(IS_CONNECTED);
            if (isConnect) {
                num ++;
            }
        }
        return num;
    }

    private void addDevice(HashMap<String,Object> device) {
        String addressTemp = device.get(ADDRESS).toString();
        int size = mDeviceList.size();
        for (int i = 0; i < size; i++) {
            HashMap<String, Object> devMap = (HashMap<String, Object>) mDeviceList.get(i);
            String address = devMap.get(ADDRESS).toString();
            if(address.equals(addressTemp)){
                return;
            }
        }
        mDeviceList.add(device);
    }

    private boolean setAccessoryMode(int mode) {
        if(null != mBleService){
            byte[] data = BLECommand.setParameter(BLECommand.SET_PARAMETER_ACCESSORY_MODE,mode);
            Log.d(TAG,"setAccessoryMode ");
            logData(data);
            return mBleService.writeCharacteristic(Utils.ACCESSORY_BASE_UUID,
                    Utils.ACCESSORY_WRITE_UUID, data);
        }
        return false;
    }

    private void logData(byte[] data){
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
        Log.i(TAG,"logData ~~~~  " + logTemp.toString());
    }

    private void updateReplyMessage(int mode, byte[] data){
        if(BLECommand.INVAILD_DATA == mode || null == data){
            return;
        }
        Log.d(TAG,"updateReplyMessage mode " + mode + " length  " + data.length);
        byte [] unpacketResult = null;
        switch (mode){
            case BLECommand.GET_PARAMETER_VERSION:
                break;
            case BLECommand.GET_PARAMETER_BATTERY:
                break;
            case BLECommand.GET_PARAMETER_ACCESSORY_MODE:
                if(BLECommand.REPLY_PARAMETER_ACCESSORY_MODE_LENGTH == data.length){
                    int accessory = data[1];
                    mHandler.sendMessage(mHandler.obtainMessage(UPDATE_SELECTED_NUM, accessory));
                    Log.i(TAG,"updateReplyMessage accessory " + accessory);
                }
                break;
            case BLECommand.GET_PARAMETER_PROGRAM_DATA:
                break;

        }
    }

    private boolean getAccessoryMode() {
        if(null != mBleService){
            byte[] data = BLECommand.getParameter(BLECommand.GET_PARAMETER_ACCESSORY_MODE);
            Log.d(TAG,"getAccessoryMode ");
            logData(data);
            return mBleService.writeCharacteristic(Utils.ACCESSORY_BASE_UUID,
                    Utils.ACCESSORY_WRITE_UUID, data);
        }
        return false;
    }

    private void startClass(){
        if(null == mBleService) {
            doBindService();
        }else{
            List<BluetoothDevice> list = mBleService.getConnectDevices();
            Log.i(TAG,"startClass " + list.size());
            if (list == null || list.size() == 0){
                setScan(true);
            }else {
                openClassActivity();
            }
        }
        //openClassActivity();
    }

    private void openClassActivity() {
        startActivity(new Intent(this, CountdownActivity.class));
        //finish();
    }

    public void updateUI(int status){
        mHandler.sendEmptyMessage(UPDATE_CONNECT_STATUS);
    }

    public void connectChanged(){
        openClassActivity();
    }

}
