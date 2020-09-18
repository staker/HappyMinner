package com.ailink.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.ailink.constants.WebUrls;
import com.ailink.db.Configuration;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.pojo.QiangInfoPojo;
import com.ailink.util.Logg;
import com.ailink.view.util.StatusBarUtil;
import com.ailink.view.util.ToastUtil;
import com.ailink.view.util.TopBarLayoutUtil;

import juhuiwan.ailink.android.R;


/**
 *
 */
public class MissionQiangGiftActivity extends BaseActivity {
    private TopBarLayoutUtil topBar;
    private TextView txtGift,txtMissionDesc,txtStatus,txtTips,txtQiangTimes;

    boolean isRequesting=false;

    QiangInfoPojo qiangInfoPojo;
    @Override
    protected void initViews(Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarFullScreem(this);
        setContentView(R.layout.activity_mission_qiang_gift);
        txtGift=(TextView)findViewById(R.id.txt_gift_lan);
        txtMissionDesc=(TextView)findViewById(R.id.txt_mission_desc);
        txtStatus=(TextView)findViewById(R.id.txt_status);
        txtTips=(TextView)findViewById(R.id.txt_tips);
        txtQiangTimes=(TextView)findViewById(R.id.txt_today_times);
        initViewsClickLinstener(txtStatus);
        initTopBar();
        init();
        refreshData();
    }
    private void initTopBar() {
        topBar = new TopBarLayoutUtil(this);
        topBar.setLeftTextColor(Color.WHITE);
        topBar.setLeftImage(R.drawable.icon_back_white);
        topBar.setTitle("每日抢矿任务");
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
        ServerUtil.getQiangInfo(new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                QiangInfoPojo temp=(QiangInfoPojo)object;
                if(temp!=null){
                    qiangInfoPojo=temp;
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

        txtMissionDesc.setText("参与抢矿"+qiangInfoPojo.need+"次");
        txtGift.setText("奖励"+qiangInfoPojo.addSpeed+"算力");

        txtStatus.setEnabled(true);
        if(qiangInfoPojo.status==1){
            txtStatus.setText("已领取");
            txtStatus.setBackgroundResource(R.drawable.shape_empty_green01_55);
            txtStatus.setTextColor(Color.parseColor("#3BCB9D"));
        }else    if(qiangInfoPojo.status==2){
            txtStatus.setText("领取奖励");
            txtStatus.setTextColor(Color.WHITE);
            txtStatus.setBackgroundResource(R.drawable.selector_color_gradient_red02_55);
        }else    if(qiangInfoPojo.status==3){
            txtStatus.setText("去完成");
            txtStatus.setBackgroundResource(R.drawable.selector_color_gradient_blue01_55);
            txtStatus.setTextColor(Color.WHITE);
        }


        String text="您今日参与抢矿"+qiangInfoPojo.now+"次";
        SpannableString ss = new SpannableString(text);
        int color=Color.parseColor("#FB4E63");
        int timesLength=(""+qiangInfoPojo.now).length();
        ss.setSpan(new ForegroundColorSpan(color), 7,timesLength+7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtQiangTimes.setText(ss);
    }
    private void refreshQiangGift(){
        txtStatus.setEnabled(false);
        ServerUtil.getQiangGift(new HttpObjectListener() {
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
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case  R.id.txt_status:
                String text=txtStatus.getText().toString();
                if("去完成".equals(text)){
                    String url03= WebUrls.App_QiangKuang+ Configuration.getInstance(mContext).getEncodeUserToken();
                    JumpActivityUtil.showWebActivity(mContext,"抢矿",url03,true);
                }else if("领取奖励".equals(text)){
                    refreshQiangGift();
                }
                break;

        }
    }

}
