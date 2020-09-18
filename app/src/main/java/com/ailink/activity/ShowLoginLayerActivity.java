package com.ailink.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhy.autolayout.AutoLayoutActivity;

import juhuiwan.ailink.android.R;


/**
 *
 */
public class ShowLoginLayerActivity extends AutoLayoutActivity implements View.OnClickListener{

    private Button btnView;
    private TextView txtTips,txtContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_login_layer);
        btnView=(Button)findViewById(R.id.btn_view);
        txtTips=(TextView)findViewById(R.id.txt_live_tips);
        txtContent=(TextView)findViewById(R.id.txt_content);
        btnView.setOnClickListener(this);
        findViewById(R.id.mainLayout).setOnClickListener(this);
        init();
    }
    private void init() {
//        LoginContentPojo loginContentPojo=ApplicationData.getInstance().loginContentPojo;
//        btnView.setText(loginContentPojo.btnName);
//
//
//        String text="领取 400ALI + 1918 蓝钻";
//        TextColorSetting.addTextColor2String(txtContent,"领取"," 400ALI + 1918 蓝钻", Color.BLACK,Color.parseColor("#FF1D38"));

    }



    @Override
    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.btn_view:
//                StakerUtil.showWebView(this,ApplicationData.getInstance().loginContentPojo.url);
//                finish();
//                break;
//            case R.id.mainLayout:
//                finish();
//                break;
//        }

    }
}
