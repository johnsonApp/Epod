package com.jht.epod.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.jht.epod.R;
import com.jht.epod.adapter.HotGridViewAdapter;
import com.jht.epod.model.HotViewItemData;

import java.util.ArrayList;

public class HotViewFragment extends Fragment {

    private GridView mGridView;
    private HotGridViewAdapter mAdapter;

    public HotViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null == mAdapter) {
            initGridViewData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.fragment_hotview, container, false);
        mGridView = view.findViewById(R.id.gridview);
        if(null == mAdapter) {
            initGridViewData();
        }
        mGridView.setAdapter(mAdapter);
        return view;
    }

    private void initGridViewData() {
        final int length = 4;
        ArrayList<HotViewItemData> data = new ArrayList<>(length);
        int[] pic = {R.drawable.hot_item_pic1, R.drawable.hot_item_pic2, R.drawable.hot_item_pic3, R.drawable.hot_item_pic4};
        String[] title = {"#如何健身# \n 健身是一种体育项目，如各种徒手健美操", "#打卡#坚持手臂肌肉训练第12天！！！好累啊，求鼓励，求支持。",
                "#打卡#坚持手臂肌肉训练第12天！！！好累啊，求鼓励，求支持。", "#打卡#坚持手臂肌肉训练第12天！！！好累啊，求鼓励，求支持。"};
        int[] userIcon = {R.drawable.hot_user_icon1, R.drawable.hot_user_icon2, R.drawable.hot_user_icon3, R.drawable.hot_user_icon4};
        String[] userName = {"轻的像阵风","miyaaaa", "UR", "miyaaaa"};
        for(int i = 0; i < length; i++) {
            HotViewItemData itemData = new HotViewItemData();
            itemData.setPictures(pic[i]);
            itemData.setTitle(title[i]);
            itemData.setUserIcon(userIcon[i]);
            itemData.setUserName(userName[i]);
            itemData.setLikeNum((int)(Math.random() * 150 + 15));
            itemData.setCommentNum((int)(Math.random() * 50 + 15));
            itemData.setIsLiked((int)(Math.random() * 100)%2 == 0);
            data.add(i, itemData);
        }
        mAdapter = new HotGridViewAdapter(data);
    }
}
