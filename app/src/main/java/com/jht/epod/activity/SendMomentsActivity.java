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

import java.io.File;
import java.util.ArrayList;

public class SendMomentsActivity extends Activity implements SelecteImageAdapter.CameraListener{

    private static final String TAG = "SendMomentsActivity";

    private static final int CHOOSE_PHOTO = 0;
    private static final int TAKE_PHOTO = 1;

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

    View.OnClickListener mListener = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            switch (view.getId()){
                case R.id.back:
                    finish();
                    break;
                case R.id.send:
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
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, CHOOSE_PHOTO);
    }

    private void takePhoto() {
        Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File fileDir = new File(Environment.getExternalStorageDirectory() + File.separator + "Epod" + File.separator);
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
                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                String imagePath = c.getString(columnIndex);
                c.close();
                updateGridView(imagePath);
            }else if (requestCode == TAKE_PHOTO) {
                updateGridView(mTempPath);
                Log.i(TAG,"onActivityResult + " + TAKE_PHOTO);

            }
        }
    }
    private void updateGridView(String path){
        mPath.add(mTemp,path);
        mTemp ++;
        mAdapter.setData(mPath);
        mAdapter.notifyDataSetChanged();
    }
}
