package com.jht.epod.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.jht.epod.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAdapter extends SimpleAdapter {
    private Context mContext;
    private ArrayList<HashMap<String, Object>> mListValue;
    private int mLayoutResource;
    private String[] mKeyValue;
    private int[] mLayoutId;

    public MyAdapter(Context context, ArrayList<HashMap<String, Object>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.mContext = context;
        mListValue = data;
        mLayoutResource = resource;
        mKeyValue = from;
        mLayoutId =to;
    }

    @Override
    public int getCount() {
        return mListValue.size();
    }

    @Override
    public Object getItem(int position) {
        return mListValue.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(mLayoutResource, null);
            viewHolder.classPic = convertView.findViewById(mLayoutId[0]);
            viewHolder.classTitle = convertView.findViewById(mLayoutId[1]);
            viewHolder.classSubtitle1 = convertView.findViewById(mLayoutId[2]);
            viewHolder.classSubtitle2 = convertView.findViewById(mLayoutId[3]);
            viewHolder.classSubtitle3 = convertView.findViewById(mLayoutId[4]);
            viewHolder.completedTime = convertView.findViewById(mLayoutId[5]);
            viewHolder.storeId = convertView.findViewById(mLayoutId[6]);
            viewHolder.deleteBtn = convertView.findViewById(R.id.delete_class_btn);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.classPic.setImageResource(Integer.parseInt(mListValue.get(position).get(mKeyValue[0]).toString()));
        viewHolder.classTitle.setText(mListValue.get(position).get(mKeyValue[1]).toString());
        viewHolder.classSubtitle1.setText(mListValue.get(position).get(mKeyValue[2]).toString());
        viewHolder.classSubtitle2.setText(mListValue.get(position).get(mKeyValue[3]).toString());
        viewHolder.classSubtitle3.setText(mListValue.get(position).get(mKeyValue[4]).toString());
        viewHolder.completedTime.setText(mListValue.get(position).get(mKeyValue[5]).toString());
        viewHolder.storeId.setText(mListValue.get(position).get(mKeyValue[6]).toString());

        viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemDeleteListener.onDeleteClick(position);
            }
        });
        return convertView;
    }

    public interface onItemDeleteListener {
        void onDeleteClick(int position);
    }

    private onItemDeleteListener mOnItemDeleteListener;

    public void setOnItemDeleteClickListener(onItemDeleteListener itemDeleteListener) {
        this.mOnItemDeleteListener = itemDeleteListener;
    }

    class ViewHolder{
        ImageView classPic;
        TextView classTitle;
        TextView classSubtitle1;
        TextView classSubtitle2;
        TextView classSubtitle3;
        TextView completedTime;
        TextView storeId;
        TextView deleteBtn;
    }
}
