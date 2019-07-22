package com.jht.epod.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;

import com.jht.epod.R;
import com.jht.epod.adapter.SuggestedFollowsAdapter;
import com.jht.epod.model.SuggestedFollower;
import com.jht.epod.ui.MeasureListView;

import java.util.ArrayList;

public class SuggestedFollowsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_follows);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        MeasureListView listView = findViewById(R.id.suggested_follows_list);
        ArrayList<SuggestedFollower> listValue = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            SuggestedFollower follower = new SuggestedFollower(1l,
                    "NICE-",
                    Integer.toString(R.drawable.suggested_follow_head),
                    1,
                    Integer.toString(R.drawable.follows_pic1),
                    Integer.toString(R.drawable.follows_pic2),
                    Integer.toString(R.drawable.follows_pic3),
                    Integer.toString(R.drawable.follows_pic4));
            listValue.add(follower);
        }
        SuggestedFollowsAdapter adapter = new SuggestedFollowsAdapter(this, listValue);
        listView.setAdapter(adapter);
    }
}
