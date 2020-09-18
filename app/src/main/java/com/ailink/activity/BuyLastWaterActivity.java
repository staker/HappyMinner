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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import juhuiwan.ailink.android.R;


/**
 *
 */
public class BuyLastWaterActivity extends BaseActivity {
    private TextView txtSure;
    TopBarLayoutUtil topBar;
    public static long leaveTime = 100;
    int times = 99999999;

    private Timer timer;
    private TimerTask task;



    private String[] joinNames = {"西红柿", "叮当娃娃","小韭菜"};

    private HashMap<String, String> nameTokenMap;
    private int joinCount = 3;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_buy_buy_buy);
        txtSure = (TextView) findViewById(R.id.txt_sure);
        initViewsClickLinstener(txtSure);
        initIdsClickLinstener(R.id.txt_stop);
        init();
        initTopBar();
    }

    private void initTopBar() {
        topBar = new TopBarLayoutUtil(this);
        topBar.setTitle("最后一杀");
        topBar.setBackListener(new TopBarLayoutUtil.IOnClick() {
            @Override
            public void onClick() {
                finish();
            }
        });

    }

    private void init() {
        joinCount = joinNames.length;
        nameTokenMap = new HashMap<String, String>();
        for (int k = 0; k < joinCount; k++) {
            nameTokenMap.put(joinNames[k], WebUrls.joinTokens[k]);
        }

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
                    getWolfPoolInfo();
                } else {
                    stopTimer();
                }
            }
        };
        timer.schedule(task, 0, 1000);
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
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

    private void sendData() {
        stopTimer();
        initTimerTask();
    }


    private void getWolfPoolInfo() {
        ServerWolfUtil.getWolfPool(new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                ArrayList<String> listNames = (ArrayList<String>) object;
                startBuyStone(listNames);
            }
        });
    }


    private void startBuyStone(ArrayList<String> listNames) {
        if (listNames == null) {
            return;
        }
        Logg.e("剩余时间：" + BuyLastWaterActivity.leaveTime);
        if (leaveTime >= 3) {
            Logg.e("剩余时间太长，不购买");
            return;
        }
        int nameCount = listNames.size();
        String lastName = joinNames[2];//默认用第一个token进行购买
        ArrayList<String> listNeedNames = new ArrayList<String>();
        for (int k = 0; k < joinCount; k++) {
            listNeedNames.add(joinNames[k]);
        }


        for (int i = 0; i < nameCount; i++) {
            //获取排名最靠后的一个人
            String rankName = listNames.get(i);
            for (int k = 0; k < joinCount; k++) {
                if (joinNames[k].equals(rankName)) {
                    Logg.e("排" + (k + 1) + "名中：" + rankName);
                    listNeedNames.remove(rankName);
                    break;
                }
            }
        }

        int nowLeftCount = listNeedNames.size();
        if (nowLeftCount == 0) {
            Logg.e("参与的人都在10个人当中，不买了");
            return;
        } else {
            lastName = listNeedNames.get(nowLeftCount - 1);
            Logg.e("用" + lastName + "准备加入购买");
        }


        boolean isRankFirst = false;
        for (int i = 0; i < 4; i++) {
            String rankName = listNames.get(i);
            for (int k = 0; k < joinCount; k++) {
                if (joinNames[k].equals(rankName)) {
                    isRankFirst = true;
                    break;
                }
            }
        }
        if (isRankFirst) {
            Logg.e("已经有人排在前四，不买了");
            return;
        }
        String buyToken = nameTokenMap.get(lastName);
        Configuration.getInstance(this).setUserToken(buyToken);

        Logg.e("用" + lastName + "购买");
        //team 1-狼人 2-预言家 3-村民
        ServerWolfUtil.sendBuyWolfWater(1, 1, 100, new HttpObjectListener() {
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
