package com.ailink.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.ailink.logic.JumpActivityUtil;
import com.ailink.util.PhoneDeviceUtil;
import com.ailink.view.util.StatusBarUtil;
import com.ailink.view.util.TopBarLayoutUtil;

import juhuiwan.ailink.android.R;


/**
 *这是app的引导界面，只会在第一次进来app的时候展示，其他的时候不显示
 */
public class AboutActivity extends BaseActivity {
    private TextView txtVersion;
    TopBarLayoutUtil topBar;
    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_about);
        txtVersion=(TextView)findViewById(R.id.txt_version);
        txtVersion.setText("版本:"+ PhoneDeviceUtil.getVersionName(this));
        initTopBar();
    }

    private void init() {

    }
    private void initTopBar() {
        topBar = new TopBarLayoutUtil(this);
        topBar.seBottomLineColor(Color.argb(255, 234, 234, 234));
        topBar.setTitle("关于");
        topBar.setBackListener(new TopBarLayoutUtil.IOnClick() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }



}
