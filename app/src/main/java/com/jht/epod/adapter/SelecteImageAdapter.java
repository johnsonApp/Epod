package com.jht.epod.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jht.epod.R;

import java.util.ArrayList;

public class SelecteImageAdapter extends BaseAdapter {

    private static final String TAG = "SelecteImageAdapter";

    private static final int MAX_NUM = 9;
    private static final int TAG_CAMERA = 0;
    private static final int TAG_PHOTO = 1;

    private ArrayList<String> mFilePath;
    private Context mContext;
    private CameraListener mListener;
    private int mSize = 0;


    public SelecteImageAdapter(Context context, ArrayList<String> path){
        mContext = context;
        setData(path);
        setLastData();
    }

    public void setData(ArrayList<String> path) {
        mFilePath = path;
        if(path != null) {
            mSize = path.size();
        }else {
            mFilePath = new ArrayList<>();
        }
    }

    private void setLastData(){
        mFilePath.add(R.drawable.camera + "");
    }

    public void setListener(CameraListener listener){
        mListener = listener;
    }

    @Override
    public int getCount() {
        int count = mFilePath.size();
        if(count > MAX_NUM){
            count = MAX_NUM;
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        return mFilePath.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(null == convertView){
            View view= LayoutInflater.from(mContext).inflate(R.layout.list_select_image_item, null);
            imageView = view.findViewById(R.id.select_image);
        }else{
            imageView = (ImageView) convertView;
        }


        if(mSize <= MAX_NUM && position == (getCount() - 1)) {
            int resId = Integer.parseInt(mFilePath.get(position));
            imageView.setImageResource(resId);
            imageView.setTag(TAG_CAMERA);
            imageView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    int tag = (int)view.getTag();
                    if(TAG_CAMERA == tag){
                        Log.i(TAG," imageView.setOnClickListener tag camera");
                        if(mListener != null){
                            mListener.onCameraClick();
                        }
                    }else if(TAG_PHOTO == tag) {
                        Log.i(TAG," imageView.setOnClickListener tag photo");
                    }

                }
            });
        }else {
            String path = mFilePath.get(position);
            Bitmap bitmap = decodeSampledBitmapFromPath(path,8);
            imageView.setImageBitmap(bitmap);
            imageView.setTag(TAG_PHOTO);
        }
        return imageView;
    }

    private static Bitmap decodeSampledBitmapFromPath(String path, int size) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = size;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path,options);
    }

    public interface CameraListener{
        void onCameraClick();
    }
}
