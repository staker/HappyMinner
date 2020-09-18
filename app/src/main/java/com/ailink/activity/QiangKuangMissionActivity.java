package com.ailink.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.ailink.util.PhoneDeviceUtil;
import com.ailink.view.util.TopBarLayoutUtil;

import juhuiwan.ailink.android.R;


/**
 *这是抢矿任务的展示界面
 */
public class QiangKuangMissionActivity extends BaseActivity {
    TopBarLayoutUtil topBar;
    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_qiang_kuang_mission);
        initTopBar();
    }

    private void init() {

    }
    private void initTopBar() {
        topBar = new TopBarLayoutUtil(this);
        topBar.seBottomLineColor(Color.argb(255, 234, 234, 234));
        topBar.setTitle("每日抢矿任务");
        topBar.setBackListener(new TopBarLayoutUtil.IOnClick() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }



}
