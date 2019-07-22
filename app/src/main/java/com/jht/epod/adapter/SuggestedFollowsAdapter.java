package com.jht.epod.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jht.epod.R;
import com.jht.epod.model.SuggestedFollower;

import java.util.ArrayList;

public class SuggestedFollowsAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<SuggestedFollower> mList;

    public SuggestedFollowsAdapter(Context mContext, ArrayList<SuggestedFollower> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.suggested_follow_list_item, null);
            viewHolder.followName = convertView.findViewById(R.id.follow_name);
            viewHolder.followHead = convertView.findViewById(R.id.follow_head);
            viewHolder.followButton = convertView.findViewById(R.id.follow_button);
            viewHolder.followPic1 = convertView.findViewById(R.id.follow_pic1);
            viewHolder.followPic2 = convertView.findViewById(R.id.follow_pic2);
            viewHolder.followPic3 = convertView.findViewById(R.id.follow_pic3);
            viewHolder.followPic4 = convertView.findViewById(R.id.follow_pic4);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.followName.setText(mList.get(position).getName());
        viewHolder.followHead.setImageResource(Integer.parseInt(mList.get(position).getIconName()));
        viewHolder.followHead.setTag(position);
        if (mList.get(position).getState() == 0) {
            viewHolder.followButton.setText(R.string.follow);
            viewHolder.followButton.setBackgroundResource(R.drawable.suggested_follows_button_background);
        } else {
            viewHolder.followButton.setText(R.string.followed);
            viewHolder.followButton.setBackgroundResource(R.drawable.suggested_follows_button_unselected);
        }
        viewHolder.followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mList.get(position).getState() == 0) {
                    ((Button) v).setText(R.string.followed);
                    v.setBackgroundResource(R.drawable.suggested_follows_button_unselected);
                    mList.get(position).setState(1);
                } else {
                    ((Button) v).setText(R.string.follow);
                    v.setBackgroundResource(R.drawable.suggested_follows_button_background);
                    mList.get(position).setState(0);
                }
            }
        });
        viewHolder.followButton.setTag(position);
        viewHolder.followPic1.setImageResource(R.drawable.follows_pic1);
        viewHolder.followPic2.setImageResource(R.drawable.follows_pic2);
        viewHolder.followPic3.setImageResource(R.drawable.follows_pic3);
        viewHolder.followPic4.setImageResource(R.drawable.follows_pic4);

        return convertView;
    }

    class ViewHolder {
        ImageView followHead;
        TextView followName;
        Button followButton;
        ImageView followPic1;
        ImageView followPic2;
        ImageView followPic3;
        ImageView followPic4;
    }
}
