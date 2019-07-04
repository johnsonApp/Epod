package com.jht.epod;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;


import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class HomePageActivity extends AppCompatActivity implements MainPageFragment.OnFragmentInteractionListener,
        DynamicFragment.OnFragmentInteractionListener,UserCenterFragment.OnFragmentInteractionListener,
        MyPlanFragment.OnFragmentInteractionListener,ClassViewFragment.OnFragmentInteractionListener{

    private final static String Tag = "HomePageActivity";

    private FragmentTabHost mFragmentTabHost;
    private LayoutInflater mInflater;
    private ArrayList<TabContent> mTabs = new ArrayList<TabContent>(3);

    private MainPageFragment mMainPageFragment;
    private DynamicFragment mDynamicFragment;
    private UserCenterFragment mUserCenterFragment;


    private MyHandler mMyHandler = new MyHandler(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_homepage);


        mMainPageFragment = new MainPageFragment();
        mDynamicFragment = new DynamicFragment();
        mUserCenterFragment = new UserCenterFragment();

        initTab();
        //addFragmentToContainer(mMainPageFragment,"main_page");

    }

    public void onResume(){
        super.onResume();
    }

    private void initTab() {
        TabContent tabClass = new TabContent(R.drawable.tabcontent_class,R.string.tab_class,MainPageFragment.class);
        TabContent tabDynamic = new TabContent(R.drawable.tabcontent_dynamic,R.string.tab_dynamic,DynamicFragment.class);
        TabContent tabUser = new TabContent(R.drawable.tabcontent_user,R.string.tab_user,UserCenterFragment.class);

        mTabs.add(tabClass);
        mTabs.add(tabDynamic);
        mTabs.add(tabUser);

        mFragmentTabHost = findViewById(android.R.id.tabhost);
        mFragmentTabHost.setup(this,getSupportFragmentManager(),R.id.maincontent);
        mInflater = LayoutInflater.from(this);

        for(TabContent tab : mTabs) {
            TabHost.TabSpec spec = mFragmentTabHost.newTabSpec(String.valueOf(tab.getText()));
            spec.setIndicator(buildView(tab));
            mFragmentTabHost.addTab(spec,tab.getFragment(),null);
        }

        mFragmentTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);


    }

    private View buildView(TabContent tab){
        View view = mInflater.inflate(R.layout.tab_content,null);
        ImageView imageView = view.findViewById(R.id.tab_icon);
        TextView textView = view.findViewById(R.id.tab_text);

        imageView.setImageResource(tab.getImage());
        textView.setText(tab.getText());
        return view;
    }

    public void onFragmentInteraction(Uri uri){

    }

    private class TabContent{
        private int mImage;
        private int mText;
        private Class mFragment;

        public TabContent(int getImage, int getText, Class getFragment){
            mImage = getImage;
            mText = getText;
            mFragment = getFragment;
        }

        public int getImage(){
            return mImage;
        }

        public void setImage (int image) {
            mImage = image;
        }

        public int getText() {
            return mText;
        }

        public void setText (int text) {
            mText = text;
        }

        public Class getFragment() {
            return mFragment;
        }

        public void setFragment(Class fragment) {
            mFragment = fragment;
        }
    }
    class MyHandler extends Handler {
        WeakReference<HomePageActivity> mActivity;
        boolean  mConnectedStatus;

        MyHandler(HomePageActivity activity){
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            HomePageActivity activity = mActivity.get();
            if (activity != null){
                switch(msg.what){
                }
            }
        }
    }
}
