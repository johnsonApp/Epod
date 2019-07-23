package com.jht.epod.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;

import com.jht.epod.R;
import com.jht.epod.adapter.SelecteImageAdapter;
import com.jht.epod.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class SendMomentsActivity extends Activity implements SelecteImageAdapter.CameraListener{

    private static final String TAG = "SendMomentsActivity";

    private static final int CHOOSE_PHOTO = 0;
    private static final int TAKE_PHOTO = 1;

    private static final String DIR_STRING = Environment.getExternalStorageDirectory() + File.separator + "Epod" + File.separator;

    private ArrayList<String>  mPath;
    private SelecteImageAdapter mAdapter;
    private EditText mText;
    private GridView mImage;

    private String mTempPath;
    private int mTemp = 0;

    private AlertDialog.Builder mBuilder;
    private int mWhich = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_moments);
        mText = findViewById(R.id.moment_text);
        mImage = findViewById(R.id.moment_select_image);

        findViewById(R.id.back).setOnClickListener(mListener);
        findViewById(R.id.send).setOnClickListener(mListener);

        mPath = new ArrayList<>();
        mAdapter = new SelecteImageAdapter(this,mPath);
        mAdapter.setListener(this);
        mImage.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteTempPhoto();
    }

    View.OnClickListener mListener = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            switch (view.getId()){
                case R.id.back:
                    finish();
                    break;
                case R.id.send:
                    deleteTempPhoto();
                    break;
            }
        }
    };

    public void onCameraClick(){
        showPickOrCameraDialog();
    }

    private void showPickOrCameraDialog(){
        if(null == mBuilder) {
            mBuilder = new AlertDialog.Builder(this);
            mBuilder.setPositiveButton(R.string.confirm, onClickListener);
            mBuilder.setNegativeButton(R.string.cancel, null);
        }
        mBuilder.setSingleChoiceItems(R.array.pick_camera, mWhich, onClickListener);
        mBuilder.show();
    }

    private DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener(){
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(which >= 0) {
                mWhich = which;
            }else if(which == DialogInterface.BUTTON_POSITIVE) {
                startGetPhotoActivity();
            }
        }
    };

    private void startGetPhotoActivity() {
        if(0 == mWhich) {
            takePhoto();
        }else if(1 == mWhich) {
            choosePhoto();
        }
    }

    private void choosePhoto() {
        /*Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, CHOOSE_PHOTO);*/

        MultiImageSelector.create(this)
                .showCamera(true)
                .count(Utils.SELECT_IMAGE_MAX_NUM)
                .multi()
                .origin(mPath)
                .start(this,CHOOSE_PHOTO);
    }

    private void takePhoto() {
        Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File fileDir = new File(DIR_STRING);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        File photoFile = new File(fileDir, "temp" + mTemp + ".png");
        mTempPath = photoFile.getAbsolutePath();
        Uri uri = Uri.fromFile(photoFile);
        intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intentToTakePhoto, TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if(requestCode == CHOOSE_PHOTO){
                Log.i(TAG,"onActivityResult + " + CHOOSE_PHOTO);
                List<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                for(String path : paths) {
                    if(!mPath.contains(path)) {
                        mPath.add(mTemp++,path);
                    }
                }
                mAdapter.setData(mPath);
                mAdapter.notifyDataSetChanged();
            }else if (requestCode == TAKE_PHOTO) {

                updateGridView(mTempPath);
                Log.i(TAG,"onActivityResult + " + TAKE_PHOTO);

            }
        }
    }
    private void updateGridView(String path){
        mPath.add(mTemp,path);
        mTemp ++;
        //mAdapter.updateData(path);
        mAdapter.notifyDataSetChanged();
    }

    private void deleteTempPhoto(){
        for(int i = 0; i < mTemp; i++){
            File fileDir = new File(DIR_STRING);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }

            File photoFile = new File(fileDir, "temp" + i + ".png");
            if(photoFile.exists()) {
                photoFile.delete();
            }
        }
    }
}
