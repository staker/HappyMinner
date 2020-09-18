package com.ailink.activity;

import android.graphics.Color;
import android.os.Bundle;

import com.ailink.db.Configuration;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.util.AsynTaskUtil;
import com.ailink.view.util.StatusBarUtil;

import juhuiwan.ailink.android.R;


/**
 *这是app的引导界面，只会在第一次进来app的时候展示，其他的时候不显示
 */
public class AppIntroActivity extends BaseActivity {

    @Override
    protected void initViews(Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarColor(this,Color.WHITE);
        setContentView(R.layout.activity_luancher);


    }

    private void init() {

    }




}
