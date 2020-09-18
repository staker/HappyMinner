package com.ailink.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.pojo.QianDaoPojo;
import com.ailink.view.LoadingDialog;
import com.ailink.view.util.ToastUtil;

import juhuiwan.ailink.android.R;


/**
 *
 */
public class QianDaoActivity extends BaseActivity{
    private TextView txtTime01,txtTime02,txtTime03;

    private TextView txtDay01,txtDay02,txtDay03,txtDay04,txtDay05,txtDay06,txtDay07;
    private ImageView imgDay01,imgDay02,imgDay03,imgDay04,imgDay05,imgDay06,imgDay07;
    private TextView txtGiftTips;
    private LinearLayout layoutGiftLan,layoutGiftSpeed;
    private TextView txtGiftLan,txtGiftSpeed;
    private TextView txtSure;

    QianDaoPojo qianDaoPojo;
    LoadingDialog loadingDialog;
    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_qian_dao);
        txtTime01=(TextView)findViewById(R.id.txt_time01);
        txtTime02=(TextView)findViewById(R.id.txt_time02);
        txtTime03=(TextView)findViewById(R.id.txt_time03);

        txtDay01=(TextView)findViewById(R.id.txt_day01);
        txtDay02=(TextView)findViewById(R.id.txt_day02);
        txtDay03=(TextView)findViewById(R.id.txt_day03);
        txtDay04=(TextView)findViewById(R.id.txt_day04);
        txtDay05=(TextView)findViewById(R.id.txt_day05);
        txtDay06=(TextView)findViewById(R.id.txt_day06);
        txtDay07=(TextView)findViewById(R.id.txt_day07);

        imgDay01=(ImageView) findViewById(R.id.img_day01);
        imgDay02=(ImageView) findViewById(R.id.img_day02);
        imgDay03=(ImageView) findViewById(R.id.img_day03);
        imgDay04=(ImageView) findViewById(R.id.img_day04);
        imgDay05=(ImageView) findViewById(R.id.img_day05);
        imgDay06=(ImageView) findViewById(R.id.img_day06);
        imgDay07=(ImageView) findViewById(R.id.img_day07);

        txtGiftTips=(TextView)findViewById(R.id.txt_gift_tips);
        txtGiftLan=(TextView)findViewById(R.id.txt_gift_lan);
        txtGiftSpeed=(TextView)findViewById(R.id.txt_gift_speed);

        layoutGiftLan=(LinearLayout)findViewById(R.id.layout_gift_lan);
        layoutGiftSpeed=(LinearLayout)findViewById(R.id.layout_gift_speed);

        txtSure=(TextView)findViewById(R.id.txt_sure);
        initViewsClickLinstener(txtSure);
        initIdsClickLinstener(R.id.mainLayout);
        init();
        refreshData();
    }
    private void init() {
        loadingDialog=LoadingDialog.getInstance();
    }
    private void refreshData() {
        ServerUtil.getSignInfo(new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                QianDaoPojo temp=(QianDaoPojo)object;
                if(temp!=null){
                    qianDaoPojo=temp;
                    initData();
                }
            }
        });
    }
    private void initData() {
        int dayBai,dayShi,dayGe;
        dayBai=qianDaoPojo.continueDay/100;
        dayShi=qianDaoPojo.continueDay%100/10;
        dayGe=qianDaoPojo.continueDay%100%10;
        txtTime01.setText(""+dayBai);
        txtTime02.setText(""+dayShi);
        txtTime03.setText(""+dayGe);
        initSignDay(qianDaoPojo.day);

        if(qianDaoPojo.token>0){
            layoutGiftLan.setVisibility(View.VISIBLE);
            txtGiftLan.setText(qianDaoPojo.token+"蓝钻");
        }else {
            layoutGiftLan.setVisibility(View.GONE);
        }

        if(qianDaoPojo.speed>0){
            layoutGiftSpeed.setVisibility(View.VISIBLE);
            txtGiftSpeed.setText(qianDaoPojo.speed+"算力");
        }else {
            layoutGiftSpeed.setVisibility(View.GONE);
        }

        showCanSign();

    }
    private void showCanSign(){
        boolean canSign="yes".equals(qianDaoPojo.status);
        if (!canSign){
            txtGiftTips.setText("签到成功，已领取奖励");
            txtSure.setText("确定");
            txtSure.setTextColor(Color.WHITE);
            txtSure.setBackgroundResource(R.drawable.shape_gradient_full_red02_55);
        }
    }
    private void initSignDay(int day){

        txtDay01.setTextColor(Color.parseColor("#999999"));
        txtDay02.setTextColor(Color.parseColor("#999999"));
        txtDay03.setTextColor(Color.parseColor("#999999"));
        txtDay04.setTextColor(Color.parseColor("#999999"));
        txtDay05.setTextColor(Color.parseColor("#999999"));
        txtDay06.setTextColor(Color.parseColor("#999999"));
        txtDay07.setTextColor(Color.parseColor("#999999"));
        txtDay01.setText("第1天");
        txtDay02.setText("第2天");
        txtDay03.setText("第3天");
        txtDay04.setText("第4天");
        txtDay05.setText("第5天");
        txtDay06.setText("第6天");
        txtDay07.setText("第7天");
        imgDay01.setBackgroundResource(R.drawable.icon_qian_no);
        imgDay02.setBackgroundResource(R.drawable.icon_qian_no);
        imgDay03.setBackgroundResource(R.drawable.icon_qian_no);
        imgDay04.setBackgroundResource(R.drawable.icon_qian_no);
        imgDay05.setBackgroundResource(R.drawable.icon_qian_no);
        imgDay06.setBackgroundResource(R.drawable.icon_qian_no);
        imgDay07.setBackgroundResource(R.drawable.icon_qian_no);
        boolean canSign="yes".equals(qianDaoPojo.status);
        switch (day){
            case 1:
                txtDay01.setText("今天");
                txtDay01.setTextColor(Color.parseColor("#FB4E63"));
                if(canSign){
                    imgDay01.setBackgroundResource(R.drawable.icon_qian_today);
                }else {
                    imgDay01.setBackgroundResource(R.drawable.icon_qian_yes);
                }
                break;
            case 2:
                txtDay01.setText("第1天");
                txtDay02.setText("今天");
                txtDay01.setTextColor(Color.parseColor("#FB4E63"));
                txtDay02.setTextColor(Color.parseColor("#FB4E63"));
                imgDay01.setBackgroundResource(R.drawable.icon_qian_yes);

                if(canSign){
                    imgDay02.setBackgroundResource(R.drawable.icon_qian_today);
                }else {
                    imgDay02.setBackgroundResource(R.drawable.icon_qian_yes);
                }
                break;
            case 3:
                txtDay01.setText("第1天");
                txtDay02.setText("第2天");
                txtDay03.setText("今天");

                txtDay01.setTextColor(Color.parseColor("#FB4E63"));
                txtDay02.setTextColor(Color.parseColor("#FB4E63"));
                txtDay03.setTextColor(Color.parseColor("#FB4E63"));
                imgDay01.setBackgroundResource(R.drawable.icon_qian_yes);
                imgDay02.setBackgroundResource(R.drawable.icon_qian_yes);
                if(canSign){
                    imgDay03.setBackgroundResource(R.drawable.icon_qian_today);
                }else {
                    imgDay03.setBackgroundResource(R.drawable.icon_qian_yes);
                }
                break;
            case 4:
                txtDay01.setText("第1天");
                txtDay02.setText("第2天");
                txtDay03.setText("第3天");
                txtDay04.setText("今天");

                txtDay01.setTextColor(Color.parseColor("#FB4E63"));
                txtDay02.setTextColor(Color.parseColor("#FB4E63"));
                txtDay03.setTextColor(Color.parseColor("#FB4E63"));
                txtDay04.setTextColor(Color.parseColor("#FB4E63"));
                imgDay01.setBackgroundResource(R.drawable.icon_qian_yes);
                imgDay02.setBackgroundResource(R.drawable.icon_qian_yes);
                imgDay03.setBackgroundResource(R.drawable.icon_qian_yes);
                if(canSign){
                    imgDay04.setBackgroundResource(R.drawable.icon_qian_today);
                }else {
                    imgDay04.setBackgroundResource(R.drawable.icon_qian_yes);
                }
                break;
            case 5:
                txtDay01.setText("第1天");
                txtDay02.setText("第2天");
                txtDay03.setText("第3天");
                txtDay04.setText("第4天");
                txtDay05.setText("今天");

                txtDay01.setTextColor(Color.parseColor("#FB4E63"));
                txtDay02.setTextColor(Color.parseColor("#FB4E63"));
                txtDay03.setTextColor(Color.parseColor("#FB4E63"));
                txtDay04.setTextColor(Color.parseColor("#FB4E63"));
                txtDay05.setTextColor(Color.parseColor("#FB4E63"));

                imgDay01.setBackgroundResource(R.drawable.icon_qian_yes);
                imgDay02.setBackgroundResource(R.drawable.icon_qian_yes);
                imgDay03.setBackgroundResource(R.drawable.icon_qian_yes);
                imgDay04.setBackgroundResource(R.drawable.icon_qian_yes);
                if(canSign){
                    imgDay05.setBackgroundResource(R.drawable.icon_qian_today);
                }else {
                    imgDay05.setBackgroundResource(R.drawable.icon_qian_yes);
                }
                break;

            case 6:
                txtDay01.setText("第1天");
                txtDay02.setText("第2天");
                txtDay03.setText("第3天");
                txtDay04.setText("第4天");
                txtDay05.setText("第5天");
                txtDay06.setText("今天");

                txtDay01.setTextColor(Color.parseColor("#FB4E63"));
                txtDay02.setTextColor(Color.parseColor("#FB4E63"));
                txtDay03.setTextColor(Color.parseColor("#FB4E63"));
                txtDay04.setTextColor(Color.parseColor("#FB4E63"));
                txtDay05.setTextColor(Color.parseColor("#FB4E63"));
                txtDay06.setTextColor(Color.parseColor("#FB4E63"));

                imgDay01.setBackgroundResource(R.drawable.icon_qian_yes);
                imgDay02.setBackgroundResource(R.drawable.icon_qian_yes);
                imgDay03.setBackgroundResource(R.drawable.icon_qian_yes);
                imgDay04.setBackgroundResource(R.drawable.icon_qian_yes);
                imgDay05.setBackgroundResource(R.drawable.icon_qian_yes);
                if(canSign){
                    imgDay06.setBackgroundResource(R.drawable.icon_qian_today);
                }else {
                    imgDay06.setBackgroundResource(R.drawable.icon_qian_yes);
                }
                break;
            case 7:
                txtDay01.setText("第1天");
                txtDay02.setText("第2天");
                txtDay03.setText("第3天");
                txtDay04.setText("第4天");
                txtDay05.setText("第5天");
                txtDay06.setText("第6天");
                txtDay07.setText("今天");

                txtDay01.setTextColor(Color.parseColor("#FB4E63"));
                txtDay02.setTextColor(Color.parseColor("#FB4E63"));
                txtDay03.setTextColor(Color.parseColor("#FB4E63"));
                txtDay04.setTextColor(Color.parseColor("#FB4E63"));
                txtDay05.setTextColor(Color.parseColor("#FB4E63"));
                txtDay06.setTextColor(Color.parseColor("#FB4E63"));
                txtDay07.setTextColor(Color.parseColor("#FB4E63"));

                imgDay01.setBackgroundResource(R.drawable.icon_qian_yes);
                imgDay02.setBackgroundResource(R.drawable.icon_qian_yes);
                imgDay03.setBackgroundResource(R.drawable.icon_qian_yes);
                imgDay04.setBackgroundResource(R.drawable.icon_qian_yes);
                imgDay05.setBackgroundResource(R.drawable.icon_qian_yes);
                imgDay06.setBackgroundResource(R.drawable.icon_qian_yes);
                if(canSign){
                    imgDay07.setBackgroundResource(R.drawable.icon_qian_today);
                }else {
                    imgDay07.setBackgroundResource(R.drawable.icon_qian_yes);
                }
                break;
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_sure:
                if("确定".equals(txtSure.getText().toString())){
                    finish();
                }else{
                    loadingDialog.showDialogLoading(true,mContext,null);
                    ServerUtil.sendSignIn(new HttpObjectListener() {
                        @Override
                        public void onSucess(Object object) {
                            ToastUtil.showToast("签到成功",mContext);
                            loadingDialog.showDialogLoading(false,mContext,null);
                            refreshData();
                        }
                        @Override
                        public void onDataError(String dataError) {
                            super.onDataError(dataError);
                            loadingDialog.showDialogLoading(false,mContext,null);
                            ToastUtil.showToast(dataError,mContext);
                        }
                    });
                }
                break;
            case R.id.mainLayout:
                finish();
                break;

        }

    }
}
