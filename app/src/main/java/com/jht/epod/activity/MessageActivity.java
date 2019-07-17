package com.jht.epod.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.jht.epod.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageActivity extends Activity {
    private TextView mSystemNotification;
    private TextView mFriendMessage;
    private ListView mMessageListView;

    private ArrayList<HashMap<String, Object>> mSystemListData;
    private ArrayList<HashMap<String, Object>> mFriendListData;

    private SimpleAdapter mSystemAdapter;
    private SimpleAdapter mFriendAdapter;

    private boolean isSystemTab = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        findViewById(R.id.back).setOnClickListener(listener);

        mSystemNotification = findViewById(R.id.system_notification);
        mFriendMessage = findViewById(R.id.friend_message);
        mMessageListView = findViewById(R.id.message_list);

        mSystemNotification.setOnClickListener(listener);
        mFriendMessage.setOnClickListener(listener);

        initAdapter();
        changeTab();
    }

    private void initAdapter () {
        mSystemListData = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("messageTitle", "系统消息");
            data.put("messageTime", "05-03 18:00");
            data.put("messageDetail", "消息消息消息消息消息消息消息消息消息消息");
            mSystemListData.add(data);
        }
        mSystemAdapter = new SimpleAdapter(this,
                mSystemListData, R.layout.message_list_item,
                new String[]{"messageTitle", "messageTime", "messageDetail"},
                new int[]{R.id.message_title, R.id.message_time, R.id.message_detail});

        mFriendListData = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("messageTitle", "不瘦不换头像");
            data.put("messageTime", "10-09 07:00");
            data.put("messageDetail", "好友好友好友好友好友好友好友好友好友好友");
            mFriendListData.add(data);
        }
        mFriendAdapter = new SimpleAdapter(this,
                mFriendListData, R.layout.message_list_item,
                new String[]{"messageTitle", "messageTime", "messageDetail"},
                new int[]{R.id.message_title, R.id.message_time, R.id.message_detail});
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.system_notification:
                    if (isSystemTab) {
                        return;
                    }
                    isSystemTab = true;
                    changeTab();
                    break;
                case R.id.friend_message:
                    if (!isSystemTab) {
                        return;
                    }
                    isSystemTab = false;
                    changeTab();
                    break;
            }
        }
    };

    private void changeTab () {
        if (isSystemTab) {
            mSystemNotification.setBackgroundResource(R.drawable.system_notification_select);
            mSystemNotification.setTextColor(getResources().getColor(R.color.colorWhite));
            mFriendMessage.setBackgroundResource(R.drawable.friend_message_unselect);
            mFriendMessage.setTextColor(getResources().getColor(R.color.colorButtonBg));
            mMessageListView.setAdapter(mSystemAdapter);
        } else {
            mSystemNotification.setBackgroundResource(R.drawable.system_notification_unselect);
            mSystemNotification.setTextColor(getResources().getColor(R.color.colorButtonBg));
            mFriendMessage.setBackgroundResource(R.drawable.friend_message_select);
            mFriendMessage.setTextColor(getResources().getColor(R.color.colorWhite));
            mMessageListView.setAdapter(mFriendAdapter);
        }
    }
}
