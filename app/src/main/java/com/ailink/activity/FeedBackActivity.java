package com.ailink.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ailink.db.Configuration;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.util.AsynTaskUtil;
import com.ailink.view.util.StatusBarUtil;
import com.ailink.view.util.ToastUtil;
import com.ailink.view.util.TopBarLayoutUtil;

import juhuiwan.ailink.android.R;


/**
 *
 */
public class FeedBackActivity extends BaseActivity {
    private EditText edtContent;
    private TextView txtSure;
    TopBarLayoutUtil topBar;
    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_feedback);
        edtContent=(EditText)findViewById(R.id.edt_content);
        txtSure=(TextView)findViewById(R.id.txt_sure);
        initViewsClickLinstener(txtSure);
        initTopBar();
    }

    private void initTopBar() {
        topBar = new TopBarLayoutUtil(this);
        topBar.setTitle("意见反馈");
        topBar.setBackListener(new TopBarLayoutUtil.IOnClick() {
            @Override
            public void onClick() {
                finish();
            }
        });

    }
    private void init() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.txt_sure:
                sendData();
                break;
        }
    }
    private void sendData(){
        String content=edtContent.getText().toString().trim();
        if(content.length()==0){
            ToastUtil.showToast("请输入您要提交意见内容",this);
            return;
        }

    }
}
