package com.ailink.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ailink.constants.WebUrls;
import com.ailink.db.Configuration;
import com.ailink.http.ServerWishUtil;
import com.ailink.http.ServerWolfUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.util.Logg;
import com.ailink.view.util.ToastUtil;
import com.ailink.view.util.TopBarLayoutUtil;

import java.util.Timer;
import java.util.TimerTask;

import juhuiwan.ailink.android.R;


/**
 *
 */
public class BuyWolfPoolActivity extends BaseActivity {
    private TextView txtSure;
    TopBarLayoutUtil topBar;

    int times=2000;

    private Timer timer;
    private TimerTask task;




    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_buy_buy_buy);
        txtSure=(TextView)findViewById(R.id.txt_sure);
        initViewsClickLinstener(txtSure);
        initIdsClickLinstener(R.id.txt_stop);
        init();
        initTopBar();
    }

    private void initTopBar() {
        topBar = new TopBarLayoutUtil(this);
        topBar.setTitle("狼人杀买金水");
        topBar.setBackListener(new TopBarLayoutUtil.IOnClick() {
            @Override
            public void onClick() {
                finish();
            }
        });

    }
    private void init() {

    }
    private void initTimerTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new Timer(true);
        if (task != null) {
            task.cancel();
            task = null;
        }
        task = new TimerTask() {
            @Override
            public void run() {
                if (times>0){
                    times--;
                    startBuyWater();
                }else{
                    Logg.e("准备停止购买");
                    stopTimer();
                }
            }
        };
        timer.schedule(task, 0, 150);
    }
    public  void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.txt_sure:
                sendData();
                break;
            case R.id.txt_stop:
                ToastUtil.showToast("已经停止，可以从新开始！",mContext);
                times=1;
                stopTimer();
                break;
        }
    }
    private void sendData(){
        stopTimer();
        initTimerTask();
    }



    private void startBuyWater(){
        //team 1-狼人 2-预言家 3-村民
        //第二个是数量
        //第三个是价格

        Configuration.getInstance(this).setUserToken(WebUrls.joinTokens[2]);
        ServerWolfUtil.sendBuyWolfWater(1, 500, 100, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {

            }
        });
        Configuration.getInstance(this).setUserToken(WebUrls.joinTokens[1]);
        ServerWolfUtil.sendBuyWolfWater(1, 500, 100, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {

            }
        });

        Configuration.getInstance(this).setUserToken(WebUrls.joinTokens[0]);
        ServerWolfUtil.sendBuyWolfWater(3, 500, 100, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {

            }
        });


    }

    @Override
    public void finish() {
        times=1;
        stopTimer();
        super.finish();
    }
}
