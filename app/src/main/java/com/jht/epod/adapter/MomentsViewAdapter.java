package com.jht.epod.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jht.epod.R;
import com.jht.epod.activity.UserInfoActivity;
import com.jht.epod.model.MomentsViewItemData;
import com.jht.epod.model.RecommendItemData;

import java.util.ArrayList;

public class MomentsViewAdapter extends BaseAdapter {
    private static final String TAG = "MomentsViewAdapter";

    private static final int TYPE_ONE = 0, TYPE_TWO = 1, TYPE_COUNT = 2;

    private ArrayList<MomentsViewItemData> mData;
    private ArrayList<RecommendItemData> mData1;
    private Context mContext;

    private boolean mNeedRecommend = true;

    public MomentsViewAdapter(Context context, ArrayList<MomentsViewItemData> data, ArrayList<RecommendItemData> data1) {
        mContext = context;
        mData = data;
        mData1 = data1;
    }

    public void setAdapterType(boolean isNeedRecommend) {
        mNeedRecommend = isNeedRecommend;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (mData != null) {
            count = mData.size();
        }
        if(mNeedRecommend) {
            count ++;
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        if(mData != null)
            return mData.get(position);
        else return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getViewTypeCount() {
        if(mNeedRecommend) return TYPE_COUNT;
        else return 1;
    }

    @Override
    public int getItemViewType(int position) {
        int type = 0;
        if(!mNeedRecommend) {
            type = TYPE_ONE;
        }else {
            if (position == TYPE_COUNT) {
                type = TYPE_TWO;
            } else {
                type = TYPE_ONE;
            }
        }
        return type;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder holder1 = null;
        RecommendViewHolder holder2 = null;
        int type=getItemViewType(position);

        Log.i(TAG,"getView position " + position + " type " + type);
        if(null == convertView) {
            switch (type){
                case TYPE_ONE:
                    holder1 = new MyViewHolder();
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moments_view_my, null);
                    holder1.userIcon = convertView.findViewById(R.id.user_icon);
                    holder1.userName = convertView.findViewById(R.id.user_name);
                    holder1.time = convertView.findViewById(R.id.time);
                    holder1.textBody = convertView.findViewById(R.id.text_body);
                    holder1.picture1 = convertView.findViewById(R.id.pic1);
                    holder1.picture2 = convertView.findViewById(R.id.pic2);
                    holder1.picture3 = convertView.findViewById(R.id.pic3);
                    holder1.like = convertView.findViewById(R.id.like);
                    holder1.likeIcon = convertView.findViewById(R.id.like_icon);
                    holder1.likeNum = convertView.findViewById(R.id.like_num);
                    holder1.commentNum = convertView.findViewById(R.id.comment_num);
                    convertView.setTag(holder1);
                    break;
                case TYPE_TWO:
                    holder2 = new RecommendViewHolder();
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moments_view_recommend, null);
                    holder2.more = convertView.findViewById(R.id.more);
                    holder2.gridView = convertView.findViewById(R.id.list);
                    convertView.setTag(holder2);
                    break;
            }
        }else {
            switch (type) {
                case TYPE_ONE:
                    holder1 = (MyViewHolder) convertView.getTag();
                    break;
                case TYPE_TWO:
                    holder2 = (RecommendViewHolder) convertView.getTag();
                    break;
            }
        }
        switch (type) {
            case TYPE_ONE:
                MomentsViewItemData data;
                final int finalPostion;
                if(!mNeedRecommend || position < TYPE_COUNT){
                    finalPostion = position;
                }else {
                    finalPostion = position - 1;
                }
                data = mData.get(finalPostion);

                int[] pic = data.getPictures();
                holder1.userIcon.setImageResource(data.getUserIcon());
                holder1.userName.setText(data.getUserName());
                holder1.time.setText(data.getTime());
                holder1.likeNum.setText(data.getLikeNum() + "");
                holder1.commentNum.setText(data.getCommentNum() + "");
                holder1.textBody.setText(data.getTextBody());
                holder1.picture1.setImageResource(pic[0]);
                holder1.picture2.setImageResource(pic[1]);
                holder1.picture3.setImageResource(pic[2]);
                updateLikeIcon(holder1.likeIcon,data.getIsLiked());

                holder1.userIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mNeedRecommend) {
                            mContext.startActivity(new Intent(mContext, UserInfoActivity.class));
                        }
                    }
                });


                holder1.like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        boolean check;
                        if(getIsLiked(finalPostion)) {
                            check = false;
                        }else {
                            check = true;
                        }
                        updateIsLiked(finalPostion,check);
                        updateLikeView(v,finalPostion);
                    }
                });

                break;
            case TYPE_TWO:
                holder2.more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG," more click");
                    }
                });
                RecommendViewAdapter adapter = new RecommendViewAdapter(mData1);
                holder2.gridView.setAdapter(adapter);
                break;
        }
        return convertView;
    }

    private void updateLikeView(View v, int position){
        if(v instanceof LinearLayout){
            ImageView icon = v.findViewById(R.id.like_icon);
            TextView tx = v.findViewById(R.id.like_num);
            MomentsViewItemData data = mData.get(position);
            updateLikeIcon(icon,data.getIsLiked());
            tx.setText(data.getLikeNum() + "");
        }
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

    private void updateLikeIcon(ImageView view, boolean isLiked){
        if(isLiked){
            view.setImageResource(R.drawable.hot_item_liked);
        }else {
            view.setImageResource(R.drawable.hot_item_like);
        }
    }

    private class MyViewHolder {
        TextView time;
        ImageView userIcon;
        TextView userName;
        TextView likeNum;
        TextView commentNum;
        ImageView likeIcon;
        TextView textBody;
        ImageView picture1;
        ImageView picture2;
        ImageView picture3;
        LinearLayout like;
    }

    private class RecommendViewHolder {
        TextView more;
        GridView gridView;
    }
}
