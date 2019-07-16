package com.jht.epod.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jht.epod.R;
import com.jht.epod.adapter.MomentsViewAdapter;
import com.jht.epod.model.HotViewItemData;
import com.jht.epod.model.MomentsViewItemData;
import com.jht.epod.model.RecommendItemData;

import java.util.ArrayList;

public class MomentsViewFragement extends Fragment {

    private ListView mListView;
    private MomentsViewAdapter mAdapter;

    public MomentsViewFragement() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null == mAdapter) {
            initListViewData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.fragment_momentsview, container, false);
        mListView = view.findViewById(R.id.listview);
        if(null == mAdapter) {
            initListViewData();
        }
        mListView.setAdapter(mAdapter);
        return view;
    }

    private void initListViewData() {
        int length = 4;
        ArrayList<MomentsViewItemData> datas = new ArrayList<>(length);
        ArrayList<RecommendItemData> datas1 = new ArrayList<>(length);
        MomentsViewItemData data = new MomentsViewItemData();
        String body = "#如何健身#" +  "\n" +
                "健身是一种体育项目，如各种徒手健美操、韵律操、形体操以及各种自抗力动作，体操可以增强力量、柔韧性，" +
                "增加耐力，提高协调，控制身体各部分的能力，从而使身体强健。如果要达到缓解压力的目的，至少一周锻炼3次 \n" +
                "想要锻炼肌肉，可以练举重、做体操以及其他重复伸、屈肌肉的运动。肌肉锻炼可以燃烧热量、增强骨密度、减少受伤，" +
                "尤其是关节受伤的几率，还能预防骨质疏松。";
        int[] pictures = {R.drawable.hot_item_pic1,R.drawable.hot_item_pic2,R.drawable.hot_item_pic3};
        data.setUserIcon(R.drawable.moment_user_icon);
        data.setUserName("NICE-");
        data.setTime("05-03 18:08");
        data.setPictures(pictures);
        data.setTextBody(body);
        RecommendItemData data1 = new RecommendItemData();
        data1.setUserIcon(R.drawable.hot_user_icon1);
        data1.setUserName("茜茜要瘦");
        data1.setUserLevel("社交达人");
        for (int i = 0; i < length; i ++){
            MomentsViewItemData newData = new MomentsViewItemData(data);
            newData.setLikeNum((int)(Math.random() * 150 + 15));
            newData.setCommentNum((int)(Math.random() * 50 + 15));
            newData.setIsLiked((int)(Math.random() * 100)%2 == 0);
            datas.add(i,newData);
            RecommendItemData newData1 = new RecommendItemData(data1);
            newData1.setIsFollowed((int)(Math.random() * 100)%2 == 0);
            datas1.add(i,newData1);
        }
        mAdapter = new MomentsViewAdapter(datas,datas1);
    }
}
