package com.ailink.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ailink.db.Configuration;
import com.ailink.db.MyApplication;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.pojo.UserPojo;
import com.ailink.util.PhoneDeviceUtil;
import com.ailink.view.util.ToastUtil;
import com.ailink.view.util.TopBarLayoutUtil;

import juhuiwan.ailink.android.R;


/**
 *这是app的引导界面，只会在第一次进来app的时候展示，其他的时候不显示
 */
public class NewContractActivity extends BaseActivity {

    private TextView txtCanUse;
    private TextView txtLanCount,txtSpeedCount;
    private TextView txtAdd01,txtAdd02,txtAdd03;
    private TextView txtSure;




    TopBarLayoutUtil topBar;
    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_new_contract);
        txtCanUse=(TextView)findViewById(R.id.txt_can_use);
        txtLanCount=(TextView)findViewById(R.id.txt_lan_count);
        txtSpeedCount=(TextView)findViewById(R.id.txt_speed_count);
        txtAdd01=(TextView)findViewById(R.id.txt_add01);
        txtAdd02=(TextView)findViewById(R.id.txt_add02);
        txtAdd03=(TextView)findViewById(R.id.txt_add03);
        txtSure=(TextView)findViewById(R.id.txt_sure);
        initViewsClickLinstener(txtAdd01,txtAdd02,txtAdd03,txtSure);
        init();
        initTopBar();
    }

    private void init() {
        UserPojo user= Configuration.getInstance(MyApplication.getContext()).getUserPojo();
        if(user==null){
            return;
        }
        txtCanUse.setText("可用："+(int)user.totalAsset);
        ServerUtil.getMyInfo(new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                UserPojo user= Configuration.getInstance(MyApplication.getContext()).getUserPojo();
                txtCanUse.setText("可用："+(int)user.totalAsset);
            }
        });
    }
    private void initTopBar() {
        topBar = new TopBarLayoutUtil(this);
        topBar.seBottomLineColor(Color.argb(255, 234, 234, 234));
        topBar.setTitle("新合约");
        topBar.setBackListener(new TopBarLayoutUtil.IOnClick() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }
    private void addLanCount(int count){
        String text=txtLanCount.getText().toString();
        int lanCount=Integer.parseInt(text);
        int totalCount=lanCount+count;
        int speedCount=totalCount/50;
        txtLanCount.setText(""+totalCount);
        txtSpeedCount.setText(""+speedCount);
    }
    private void sendContract(){
        String text=txtLanCount.getText().toString();
        int lanCount=Integer.parseInt(text);
        ServerUtil.sendNewContract(lanCount, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                ToastUtil.showToast("合约创建成功！",mContext);
                finish();
            }
            @Override
            public void onDataError(String dataError) {
                super.onDataError(dataError);
                ToastUtil.showToast(dataError,mContext);
            }
        });

    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.txt_add01:
                addLanCount(1000);
                break;
            case R.id.txt_add02:
                addLanCount(5000);
                break;
            case R.id.txt_add03:
                addLanCount(10000);
                break;
            case R.id.txt_sure:
                sendContract();
                break;

        }
    }
}
