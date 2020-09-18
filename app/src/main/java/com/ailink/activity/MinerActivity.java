package com.ailink.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.ailink.constants.ConstantString;
import com.ailink.db.ApplicationData;
import com.ailink.fragment.BaseFragment;
import com.ailink.fragment.FriendFragment;
import com.ailink.fragment.MyMinerFragment;
import com.ailink.fragment.RecommendFragment;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.pojo.UserPojo;
import com.ailink.util.BroadcastUtil;
import com.ailink.util.Logg;
import com.ailink.util.PhoneDeviceUtil;
import com.ailink.view.util.TopBarLayoutUtil;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;


/**
 *这是app的引导界面，只会在第一次进来app的时候展示，其他的时候不显示
 */
public class MinerActivity extends BaseActivity {

    private TextView txtTab01,txtTab02,txtTab03;

    private MyMinerFragment fragmentTab01;
    private RecommendFragment fragmentTab02;
    private FriendFragment fragmentTab03;


    int currentFragmentIndex=0;
    UserPojo userSelf=new UserPojo();
    TopBarLayoutUtil topBar;


    BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            clickTab(2);
            setFragment(2);
        }
    };

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_miner);
        txtTab01=(TextView) findViewById(R.id.txt_tab01);
        txtTab02=(TextView) findViewById(R.id.txt_tab02);
        txtTab03=(TextView) findViewById(R.id.txt_tab03);
        initViewsClickLinstener(txtTab01,txtTab02,txtTab03);
        initTopBar();
        init();
    }

    private void init() {
        BroadcastUtil.registerReceiver1(mContext,receiver, ConstantString.BroadcastActions.Action_Home_Jump_To_Buy_Miner);
        int startIndex=getIntent().getIntExtra("tab",1);
        clickTab(startIndex);
        setFragment(startIndex);
        ServerUtil.getHomeMinerList(userSelf, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                ArrayList<UserPojo> temp=(ArrayList<UserPojo>)object;
                if(temp!=null){
                    int size=temp.size();
                    topBar.setTitle("我的矿工位"+size+"/7");
                }
            }
        });
    }
    private void initTopBar() {
        topBar = new TopBarLayoutUtil(this);
        topBar.seBottomLineColor(Color.argb(255, 234, 234, 234));
        topBar.setTitle("我的矿工");
        topBar.setBackListener(new TopBarLayoutUtil.IOnClick() {
            @Override
            public void onClick() {
                finish();
            }
        });
        topBar.setRightDrawable(R.drawable.icon_search, new TopBarLayoutUtil.IOnClick() {
            @Override
            public void onClick() {
                JumpActivityUtil.showNormalActivity(mContext, SearchMinerActivity.class);
            }
        });
    }


    private void setFragment(final int position) {
        if (currentFragmentIndex == position) {
            return;
        }
        try {
            FragmentManager manager =getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            hideAllFragment(transaction);
            BaseFragment fragment = getOldFragment(position);
            if (fragment == null) {
                fragment = getNewFragment(position);
                transaction.add(R.id.frame_miner, fragment);
            } else {
                transaction.attach(fragment);
                transaction.show(fragment);
            }
            transaction.commitAllowingStateLoss();
            currentFragmentIndex = position;
        } catch (Exception e) {
        }
    }

    private void hideAllFragment(FragmentTransaction transaction){

        if (fragmentTab01 != null) {
            transaction.hide(fragmentTab01);
        }
        if (fragmentTab02 != null) {
            transaction.hide(fragmentTab02);
        }
        if (fragmentTab03 != null) {
            transaction.hide(fragmentTab03);
        }
    }
    private BaseFragment getNewFragment(int index) {
        switch (index) {
            case 1:
                fragmentTab01 =  new MyMinerFragment();
                return fragmentTab01;
            case 2:
                fragmentTab02 =  new RecommendFragment();
                return fragmentTab02;
            case 3:
                fragmentTab03 =  new FriendFragment();
                return fragmentTab03;
            default:
                //不处理
                break;
        }
        fragmentTab01 =  new MyMinerFragment();
        return fragmentTab01;
    }

    private BaseFragment getOldFragment(int index) {
        switch (index) {
            case 1:
                return fragmentTab01;
            case 2:
                return fragmentTab02;
            case 3:
                return fragmentTab03;
        }
        return fragmentTab01;
    }

    private void clickTab(int index){
        switch (index){
            case 1:
                txtTab01.setTextColor(Color.BLACK);
                txtTab02.setTextColor(Color.parseColor("#999999"));
                txtTab03.setTextColor(Color.parseColor("#999999"));
                setFragment(1);
                break;
            case 2:
                txtTab01.setTextColor(Color.parseColor("#999999"));
                txtTab02.setTextColor(Color.BLACK);
                txtTab03.setTextColor(Color.parseColor("#999999"));
                setFragment(2);
                break;
            case 3:
                txtTab01.setTextColor(Color.parseColor("#999999"));
                txtTab02.setTextColor(Color.parseColor("#999999"));
                txtTab03.setTextColor(Color.BLACK);
                setFragment(3);
                break;
        }
    }
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.txt_tab01:
                clickTab(1);
                break;
            case R.id.txt_tab02:
                clickTab(2);
                break;
            case R.id.txt_tab03:
                clickTab(3);
                break;
            case R.id.img_search:
                JumpActivityUtil.showNormalActivity(mContext, SearchMinerActivity.class);
                break;



        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ApplicationData.getInstance().poolId=0;
        unregisterReceiver(receiver);
    }
}
