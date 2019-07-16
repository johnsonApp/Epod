package com.jht.epod.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jht.epod.R;
import com.jht.epod.model.RecommendItemData;

import java.util.ArrayList;

public class RecommendViewAdapter extends BaseAdapter {

    private static final int SHOW_NUM = 3;
    private ArrayList<RecommendItemData> mData;

    public RecommendViewAdapter(ArrayList<RecommendItemData> data) {
        mData = data;
    }

    public void updateData(ArrayList<RecommendItemData> data){
        mData = data;
    }

    @Override
    public int getCount() {
        return SHOW_NUM;
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
        ViewHolder holder;
        if(null == convertView){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggest_follow, null);
            holder.userIcon = convertView.findViewById(R.id.user_icon);
            holder.userName = convertView.findViewById(R.id.user_name);
            holder.userLevel = convertView.findViewById(R.id.user_level);
            holder.follow = convertView.findViewById(R.id.follow);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        RecommendItemData data = mData.get(position);
        holder.userIcon.setImageResource(data.getUserIcon());
        holder.userName.setText(data.getUserName());
        holder.userLevel.setText(data.getUserLevel());
        final boolean isFollowed = data.getIsFollowed();

        updateBackground(holder.follow,isFollowed);
        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check;
                if(getIsFollowed(position)){
                    check = false;
                }else {
                    check = true;
                }
                updateBackground((TextView) v,check);
                mData.get(position).setIsFollowed(check);
            }
        });

        return convertView;
    }

    private void updateBackground(TextView tx, boolean isFollow) {
        if(isFollow){
            tx.setBackgroundResource(R.drawable.recommend_button_followed_bg);
            tx.setText(R.string.followed);
        }else {
            tx.setBackgroundResource(R.drawable.recommend_button_bg);
            tx.setText(R.string.follow);
        }
    }

    private boolean getIsFollowed(int position){
        return mData.get(position).getIsFollowed();
    }

    private class ViewHolder{
        ImageView userIcon;
        TextView userName;
        TextView userLevel;
        TextView follow;
    }
}
