package com.ailink.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ailink.constants.WebUrls;
import com.ailink.db.Configuration;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.pojo.UserPojo;
import com.ailink.view.util.ToastUtil;
import com.ailink.view.util.TopBarLayoutUtil;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import juhuiwan.ailink.android.R;


/**
 *
 */
public class AutoOpenKuangActivity extends BaseActivity {
    private TextView txtSure;
    TopBarLayoutUtil topBar;
    int times = 99999999;
    private Timer timer;
    private TimerTask task;
    ArrayList<UserPojo>  listMyMiner;
    int startIndex=0;





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
        topBar.setTitle("自动收矿");
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
                if (times >0) {
                    times--;
                    startIndex=0;
                    startOpenKuang(startIndex);
                } else {
                    stopTimer();
                }
            }
        };
        timer.schedule(task, 0,7200000);
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

    private void startOpenKuang(int i) {
        int tokenCount= WebUrls.joinTokens.length;
        if(i>=tokenCount){
            return;
        }
        String tempToken=WebUrls.joinTokens[i];
        Configuration.getInstance(this).setUserToken(tempToken);
        getMyMinerList();
    }
    private void getMyMinerList() {
        UserPojo  userSelf= Configuration.getInstance(this).getUserPojo();
        if(userSelf==null){
            userSelf=new UserPojo();
        }

        ServerUtil.sendSignIn(new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
            }
        });


       listMyMiner=new ArrayList<UserPojo>();
        ServerUtil.getHomeMinerList(userSelf, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                ArrayList<UserPojo> temp=(ArrayList<UserPojo>)object;
                if(temp!=null){
                    listMyMiner.addAll(temp);
                    minerIndex=0;
                    openAllKuang();
                }
            }
            @Override
            public void onDataError(String dataError) {
                super.onDataError(dataError);
            }
        });
    }

    int minerIndex=0;
    private void openAllKuang() {
      int minerSize=listMyMiner.size();
        if(minerIndex>=minerSize){
         startIndex++;
            startOpenKuang(startIndex);
            return;
        }
        UserPojo userPojo=listMyMiner.get(minerIndex);
        ServerUtil.getMinerPkg(userPojo.pkgId, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                minerIndex++;
                openAllKuang();
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
