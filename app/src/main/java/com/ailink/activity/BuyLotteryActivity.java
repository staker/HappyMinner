package com.ailink.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ailink.http.ServerWishUtil;
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
public class BuyLotteryActivity extends BaseActivity {
    private TextView txtSure;
    TopBarLayoutUtil topBar;

    int times = 300000;

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
        topBar.setTitle("抢矿");
        topBar.setBackListener(new TopBarLayoutUtil.IOnClick() {
            @Override
            public void onClick() {
                onBackPressed();
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
                if (times >0) {
                    times--;
                    startBuyStone();
                }else{
                    stopTimer();
                }
            }
        };
        timer.schedule(task, 0, 300);
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
                ToastUtil.showToast("已经停止，可以从新开始！", mContext);
                times = 1;
                stopTimer();
                break;
        }
    }
    private void sendData(){
        stopTimer();
        initTimerTask();
    }



    private void startBuyStone(){
        //96
        ServerWishUtil.sendJoinLottery(new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                Logg.e("");
            }
        });
    }

    @Override
    public void onBackPressed() {
        times=0;
        stopTimer();
        super.onBackPressed();
    }
    @Override
    public void finish() {
        times=1;
        stopTimer();
        super.finish();
    }
}
