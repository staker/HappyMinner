package com.ailink.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.zhy.autolayout.AutoLayoutActivity;

import juhuiwan.ailink.android.R;


/**
 *
 */
public class ShowLiveActivity extends AutoLayoutActivity implements View.OnClickListener{

    private ImageView imgLivePic;
    private Button btnView;
    private TextView txtTips;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_live);
        imgLivePic=(ImageView)findViewById(R.id.img_live_pic);
        btnView=(Button)findViewById(R.id.btn_view);
        txtTips=(TextView)findViewById(R.id.txt_live_tips);
        btnView.setOnClickListener(this);
        findViewById(R.id.mainLayout).setOnClickListener(this);
        refreshData();
    }
    private void refreshData() {
        ServerUtil.getCheckToken(new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                init();
            }
        });
    }
    private void init() {
//        LiveContentPojo liveContentPojo=ApplicationData.getInstance().liveContentPojo;
//       txtTips.setText(""+liveContentPojo.liveTips+"\n每次直播期间，观看直播可领取一次蓝钻奖励");
//        Bitmap bitmap = ZXingUtils.createQRImage(liveContentPojo.liveUrl, 400, 400);
//        imgLivePic.setImageBitmap(bitmap);
//        if(liveContentPojo.isOnline){
//            btnView.setText("观看直播,领取100蓝钻");
//        }else {
//            btnView.setText("观看直播");
//        }
    }



    @Override
    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.btn_view:
//                ServerUtil.sendGeneralValue("getOnlinePrize", null, new HttpObjectListener() {
//                    @Override
//                    public void onSucess(Object object) {
//                    }
//                });
//                StakerUtil.showWebView(this,ApplicationData.getInstance().liveContentPojo.liveUrl);
//                finish();
//                break;
//            case R.id.mainLayout:
//                finish();
//                break;
//        }

    }
}
