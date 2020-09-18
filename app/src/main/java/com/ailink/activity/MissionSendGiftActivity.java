package com.ailink.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.ailink.adapter.AdapterTradeMsg;
import com.ailink.constants.WebUrls;
import com.ailink.db.Configuration;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.pojo.QiangInfoPojo;
import com.ailink.pojo.SendInfoPojo;
import com.ailink.view.util.StatusBarUtil;
import com.ailink.view.util.ToastUtil;
import com.ailink.view.util.TopBarLayoutUtil;

import juhuiwan.ailink.android.R;


/**
 *
 */
public class MissionSendGiftActivity extends BaseActivity {
    private TopBarLayoutUtil topBar;
    private TextView txtGift,txtMissionDesc,txtStatus,txtTips,txtSendCount;

    boolean isRequesting=false;


    SendInfoPojo sendInfoPojo;
    @Override
    protected void initViews(Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarFullScreem(this);
        setContentView(R.layout.activity_mission_send_gift);
        txtGift=(TextView)findViewById(R.id.txt_gift_lan);
        txtMissionDesc=(TextView)findViewById(R.id.txt_mission_desc);
        txtStatus=(TextView)findViewById(R.id.txt_status);
        txtTips=(TextView)findViewById(R.id.txt_tips);
        txtSendCount=(TextView)findViewById(R.id.txt_send_count);
        initViewsClickLinstener(txtStatus);
        initTopBar();
        init();
        refreshData();
    }
    private void initTopBar() {
        topBar = new TopBarLayoutUtil(this);
        topBar.setLeftTextColor(Color.WHITE);
        topBar.setLeftImage(R.drawable.icon_back_white);
        topBar.setTitle("每日送礼任务");
        topBar.setBackListener(new TopBarLayoutUtil.IOnClick() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }
    private void init() {

    }


    private void refreshData(){
        if(isRequesting){
            return;
        }
        isRequesting=true;
        txtStatus.setEnabled(false);
        ServerUtil.getSendInfo(new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                SendInfoPojo temp=(SendInfoPojo)object;
                if(temp!=null){
                    sendInfoPojo=temp;
                    initRefreshData();
                }
                isRequesting=false;
            }

            @Override
            public void onDataError(String dataError) {
                super.onDataError(dataError);
                isRequesting=false;
                ToastUtil.showToast(dataError,mContext);
            }
        });
    }
    private void initRefreshData(){


        txtMissionDesc.setText("送礼金额达到\n"+sendInfoPojo.need+"蓝钻");
        String giftContent="";
        if(sendInfoPojo.addSpeed>0){
            giftContent="奖励"+sendInfoPojo.addSpeed+"算力";
        }

        if(sendInfoPojo.addToken>0){
            if(sendInfoPojo.addSpeed>0){
                giftContent=giftContent+"\n"+"奖励"+sendInfoPojo.addToken+"蓝钻";
            }else{
                giftContent="奖励"+sendInfoPojo.addToken+"蓝钻";
            }
        }

        txtGift.setText(giftContent);
        txtStatus.setEnabled(true);

        if(sendInfoPojo.status==1){
            txtStatus.setText("已领取");
            txtStatus.setBackgroundResource(R.drawable.shape_empty_green01_55);
            txtStatus.setTextColor(Color.parseColor("#3BCB9D"));
        }else    if(sendInfoPojo.status==2){
            txtStatus.setText("领取奖励");
            txtStatus.setTextColor(Color.WHITE);
            txtStatus.setBackgroundResource(R.drawable.selector_color_gradient_red02_55);
        }else    if(sendInfoPojo.status==3){
            txtStatus.setText("去完成");
            txtStatus.setBackgroundResource(R.drawable.selector_color_gradient_blue01_55);
            txtStatus.setTextColor(Color.WHITE);
        }


        String text="您今日已送"+sendInfoPojo.now+"蓝钻";
        SpannableString ss = new SpannableString(text);
        int color=Color.parseColor("#FB4E63");
        int timesLength=(""+sendInfoPojo.now).length();
        ss.setSpan(new ForegroundColorSpan(color), 5,timesLength+5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtSendCount.setText(ss);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }
    private void refreshSendGift(){
        txtStatus.setEnabled(false);
        ServerUtil.getSendGift(new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                ToastUtil.showToast("领取成功",mContext);
                refreshData();
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

            case  R.id.txt_status:
                String text=txtStatus.getText().toString();
                if("去完成".equals(text)){
//                    JumpActivityUtil.showNormalActivity((Activity)mContext, MyMinerActivity.class);
                    JumpActivityUtil.showMinerActivity((Activity)mContext,1);
                }else if("领取奖励".equals(text)){
                    refreshSendGift();
                }
                break;
        }
    }

}
