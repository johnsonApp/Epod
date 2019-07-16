package com.jht.epod.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jht.epod.R;
import com.jht.epod.model.HotViewItemData;

import java.util.ArrayList;

public class HotGridViewAdapter extends BaseAdapter {
    private static final String TAG = "HotGridViewAdapter";

    private ArrayList<HotViewItemData> mData;

    public HotGridViewAdapter(ArrayList<HotViewItemData> data){
        mData = data;
    }

    @Override
    public int getCount() {
        if (mData != null) return mData.size();
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        if(mData != null) return mData.get(position);
        else return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(null == convertView){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hot_view, null);
            holder.picture = convertView.findViewById(R.id.picture);
            holder.title = convertView.findViewById(R.id.title);
            holder.userIcon = convertView.findViewById(R.id.user_icon);
            holder.userName = convertView.findViewById(R.id.user_name);
            holder.likeNum = convertView.findViewById(R.id.like_num);
            holder.commentNum = convertView.findViewById(R.id.comment_num);
            holder.likeIcon = convertView.findViewById(R.id.like_icon);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        HotViewItemData data = mData.get(position);
        if(null != data){
            final boolean isLiked = data.getIsLiked();
            holder.picture.setImageResource(data.getPictures());
            holder.title.setText(data.getTitle());
            holder.userIcon.setImageResource(data.getUserIcon());
            holder.userName.setText(data.getUserName());
            holder.likeNum.setText(data.getLikeNum() + "");
            holder.commentNum.setText(data.getCommentNum() + "");
            if(isLiked){
                holder.likeIcon.setImageResource(R.drawable.hot_item_liked);
            }
            holder.likeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    if(v instanceof ImageView){
                        if(getIsLiked(position)) {
                            ((ImageView) v).setImageResource(R.drawable.hot_item_like);
                            updateIsLiked(position, false);
                        }else{
                            ((ImageView) v).setImageResource(R.drawable.hot_item_liked);
                            updateIsLiked(position, true);
                        }
                    }
                }
            });
        }
        Log.i(TAG, "getView " + position + " 2222222222222");
        return convertView;
    }

    private void updateIsLiked(int position, boolean isLiked){
        int num = mData.get(position).getLikeNum();
        if(isLiked){
            num ++;
        }else {
            num --;
        }
        mData.get(position).setLikeNum(num);
        mData.get(position).setIsLiked(isLiked);
    }

    private boolean getIsLiked(int position){
        return mData.get(position).getIsLiked();
    }

    private class ViewHolder {
        ImageView picture;
        TextView title;
        ImageView userIcon;
        TextView userName;
        TextView likeNum;
        TextView commentNum;
        ImageView likeIcon;
    }
}
