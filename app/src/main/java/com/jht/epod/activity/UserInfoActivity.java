package com.jht.epod.activity;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jht.epod.R;
import com.jht.epod.adapter.MomentsViewAdapter;
import com.jht.epod.model.MomentsViewItemData;
import com.jht.epod.model.UserInfoData;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class UserInfoActivity extends Activity {

    private static final String TAG = "UserInfoActivity";

    private MomentsViewAdapter mAdapter;
    private UserInfoData mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initUserData();
        initView(mData);
    }

    private void initUserData(){
        if(null == mData){
            mData = new UserInfoData();
        }
        int icon = R.drawable.user_info_icon;
        String name = "轻的像阵风";
        mData.setUserIcon(icon);
        mData.setUserName(name);
        mData.setUserLevel("运动达人，职业健身教练证书10项");
        mData.setFollowing((int)(Math.random() * 100));
        mData.setFollower((int)(Math.random() * 100));
        mData.setExerciseDay((int)(Math.random() * 100 + 20));
        mData.setExerciserMin((int)(Math.random() * 1000 + 50));
        mData.setBurnCalories((int)(Math.random() * 10000 + 200));
        initListViewData(icon, name);
    }


    private void initListViewData(int icon, String name) {
        int length = 4;
        ArrayList<MomentsViewItemData> datas = new ArrayList<>(length);
        MomentsViewItemData data = new MomentsViewItemData();
        String body = "#如何健身#" +  "\n" +
                "健身是一种体育项目，如各种徒手健美操、韵律操、形体操以及各种自抗力动作，体操可以增强力量、柔韧性，" +
                "增加耐力，提高协调，控制身体各部分的能力，从而使身体强健。如果要达到缓解压力的目的，至少一周锻炼3次 \n" +
                "想要锻炼肌肉，可以练举重、做体操以及其他重复伸、屈肌肉的运动。肌肉锻炼可以燃烧热量、增强骨密度、减少受伤，" +
                "尤其是关节受伤的几率，还能预防骨质疏松。";
        int[] pictures = {R.drawable.hot_item_pic1,R.drawable.hot_item_pic2,R.drawable.hot_item_pic3};
        data.setUserIcon(icon);
        data.setUserName(name);
        data.setTime("05-03 18:08");
        data.setPictures(pictures);
        data.setTextBody(body);
        for (int i = 0; i < length; i ++){
            MomentsViewItemData newData = new MomentsViewItemData(data);
            newData.setLikeNum((int)(Math.random() * 150 + 15));
            newData.setCommentNum((int)(Math.random() * 50 + 15));
            newData.setIsLiked((int)(Math.random() * 100)%2 == 0);
            datas.add(i,newData);
        }
        mData.setMoments(datas);
    }

    private void initView(UserInfoData data) {
        initTitleBackground();
        ImageView icon = findViewById(R.id.icon);
        icon.setImageResource(data.getUserIcon());
        TextView name = findViewById(R.id.name);
        name.setText(data.getUserName());
        TextView following = findViewById(R.id.following);
        following.setText(data.getFollowing() + " " + getResources().getString(R.string.following));
        TextView follower = findViewById(R.id.follower);
        follower.setText(data.getFollower() + " " + getResources().getString(R.string.follower));
        TextView level = findViewById(R.id.level);
        level.setText(data.getUserLevel());
        TextView day = findViewById(R.id.day);
        day.setText(data.getExerciseDay() + "");
        TextView min = findViewById(R.id.min);
        min.setText(data.getExerciserMin() + "");
        TextView cal = findViewById(R.id.burn);
        cal.setText(data.getBurnCalories() + "");

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        ListView listView = findViewById(R.id.listview);
        if(null == mAdapter) {
            mAdapter = new MomentsViewAdapter(this,data.getMoments(),null);
        }
        mAdapter.setAdapterType(false);
        listView.setAdapter(mAdapter);
    }

    private void initTitleBackground(){
        RelativeLayout layout = findViewById(R.id.title);
        Bitmap background = decodeSampledBitmapFromResource(getResources(),R.drawable.user_info_bg,3);
        layout.setBackground(new BitmapDrawable(getResources(), background));
    }

    private static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int size) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = size;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}
