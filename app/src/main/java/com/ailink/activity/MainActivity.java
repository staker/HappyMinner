package com.ailink.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.ailink.constants.ConstantString;
import com.ailink.db.ApplicationData;
import com.ailink.fragment.BaseFragment;
import com.ailink.fragment.HomeFragment01;
import com.ailink.fragment.HomeFragment03;
import com.ailink.fragment.HomeFragment04;
import com.ailink.fragment.HomeFragment02;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.logic.DialogUtil;
import com.ailink.pojo.VersionPojo;
import com.ailink.util.BroadcastUtil;
import com.ailink.util.Logg;
import com.ailink.util.PhoneDeviceUtil;
import com.ailink.util.StakerUtil;
import com.ailink.view.util.BottomBarLayoutUtil;

import juhuiwan.ailink.android.R;

/**
 * 这是首页的主界面
 */
public class MainActivity extends BaseActivity {

    BottomBarLayoutUtil bottomBarLayoutUtil;
    HomeFragment01 homeFragment01;
    HomeFragment02 homeFragment02;
    HomeFragment03 homeFragment03;
    HomeFragment04 homeFragment04;
    private int currentFragmentIndex = 0;//当前进来显示的主界面
    BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String data01=intent.getStringExtra("data01");
            String action=intent.getAction();
            if("3".equals(data01)){
                bottomBarLayoutUtil.setClickedTab(3);
            }
            if(ConstantString.BroadcastActions.Action_Exit_App.equals(action)){
                finish();
            }

            if(ConstantString.BroadcastActions.Action_Change_Bottom_Tab_Tips.equals(action)){
                bottomBarLayoutUtil.setRedTips(0,0,0, ApplicationData.getInstance().meTipsCount);
            }

        }
    };
    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        bottomBarLayoutUtil = new BottomBarLayoutUtil(this, new BottomBarLayoutUtil.IOnClickIndex() {
            @Override
            public void onClick(int index) {
                setFragment(index);
            }
        });
        initReOncreate(savedInstanceState);
        BroadcastUtil.registerReceiver3(this,receiver, ConstantString.BroadcastActions.Action_Change_Bottom_Tab,ConstantString.BroadcastActions.Action_Exit_App,ConstantString.BroadcastActions.Action_Change_Bottom_Tab_Tips);
        ServerUtil.getAppUpdate(new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                showAppUpdateDialog(object);
            }
        });
    }


    private void initReOncreate(Bundle savedInstanceState) {
        // 第一次进来先切换到首页的fragment
        if (savedInstanceState == null) {
            setFragment(1);
            return;
        }
        currentFragmentIndex = savedInstanceState.getInt("currentFragmentIndex");
        if (currentFragmentIndex > 0) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            homeFragment01 = (HomeFragment01) getFragmentOld(1);
            homeFragment02 = (HomeFragment02) getFragmentOld(2);
            homeFragment03 = (HomeFragment03) getFragmentOld(3);
            homeFragment04 = (HomeFragment04) getFragmentOld(4);
            if (homeFragment01 != null) {
                transaction.hide(homeFragment01);
            }
            if (homeFragment02 != null) {

                transaction.hide(homeFragment02);
            }
            if (homeFragment03 != null) {
                transaction.hide(homeFragment03);
            }
            if (homeFragment04 != null) {
                transaction.hide(homeFragment04);
            }
            transaction.commitAllowingStateLoss();
            setFragment(currentFragmentIndex);
        } else {
            setFragment(1);
        }


    }

    private void showAppUpdateDialog(Object object){
        final VersionPojo versionPojo=(VersionPojo)object;
        if(versionPojo!=null){
            int appCode= PhoneDeviceUtil.getVersionCode(this);
            if(appCode>=versionPojo.versionCode){
                return;
            }
            DialogUtil.showDialogNormal(this, versionPojo.versionLog, "取消", "更新", new DialogUtil.OnIndexButtonListener() {
                @Override
                public void onClickIndex(int buttonIndex) {
                    if(buttonIndex==2){
                        StakerUtil.showWebView(mContext,versionPojo.url);
                        if(versionPojo.state==1){
                            finish();
                        }
                    }else{
                        if(versionPojo.state==1){
                            finish();
                        }
                    }
                }
            });


        }

    }

    //这是点击底部导航，对应切换fragment的方法
    private void setFragment(final int position) {
        if (currentFragmentIndex == position) {
            return;
        }
        try {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            hideAllFragment(transaction);
            BaseFragment fragment = getFragmentOld(position);
            if (fragment == null) {
                fragment = getFragmentNew(position);
                transaction.add(R.id.layout_fragment, fragment);
            } else {
                transaction.attach(fragment);
                transaction.show(fragment);
            }
            transaction.commitAllowingStateLoss();
            currentFragmentIndex = position;
        } catch (Exception e) {
        }
        refreshFragmentState(position);
    }


    private void refreshFragmentState(int fragmentIndex){
        switch (fragmentIndex){
            case 1:
                if(homeFragment01!=null&&!homeFragment01.isResumeSelf()){
                    homeFragment01.onResumeSelf();
                }
                if(homeFragment02!=null&&homeFragment02.isResumeSelf()){
                    homeFragment02.onPauseSelf();
                }
                if(homeFragment03!=null&&homeFragment03.isResumeSelf()){
                    homeFragment03.onPauseSelf();
                }
                if(homeFragment04!=null&&homeFragment04.isResumeSelf()){
                    homeFragment04.onPauseSelf();
                }
                break;
            case 2:
                if(homeFragment01!=null&&homeFragment01.isResumeSelf()){
                    homeFragment01.onPauseSelf();
                }
                if(homeFragment02!=null&&!homeFragment02.isResumeSelf()){
                    homeFragment02.onResumeSelf();
                }
                if(homeFragment03!=null&&homeFragment03.isResumeSelf()){
                    homeFragment03.onPauseSelf();
                }
                if(homeFragment04!=null&&homeFragment04.isResumeSelf()){
                    homeFragment04.onPauseSelf();
                }
                break;
            case 3:
                if(homeFragment01!=null&&homeFragment01.isResumeSelf()){
                    homeFragment01.onPauseSelf();
                }
                if(homeFragment02!=null&&homeFragment02.isResumeSelf()){
                    homeFragment02.onPauseSelf();
                }
                if(homeFragment03!=null&&!homeFragment03.isResumeSelf()){
                    homeFragment03.onResumeSelf();
                }
                if(homeFragment04!=null&&homeFragment04.isResumeSelf()){
                    homeFragment04.onPauseSelf();
                }
                break;
            case 4:
                if(homeFragment01!=null&&homeFragment01.isResumeSelf()){
                    homeFragment01.onPauseSelf();
                }
                if(homeFragment02!=null&&homeFragment02.isResumeSelf()){
                    homeFragment02.onPauseSelf();
                }
                if(homeFragment03!=null&&homeFragment03.isResumeSelf()){
                    homeFragment03.onPauseSelf();
                }
                if(homeFragment04!=null&&!homeFragment04.isResumeSelf()){
                    homeFragment04.onResumeSelf();
                }
                break;
        }
    }










    private BaseFragment getFragmentOld(int index) {
        switch (index) {
            case 1:
                return homeFragment01;
            case 2:
                return homeFragment02;
            case 3:
                return homeFragment03;
            case 4:
                return homeFragment04;
            default:
                //不处理
                break;
        }
        return homeFragment01;
    }

    private BaseFragment getFragmentNew(int index) {
        switch (index) {
            case 1:
                homeFragment01 = new HomeFragment01();
                return homeFragment01;
            case 2:
                homeFragment02 = new HomeFragment02();
                return homeFragment02;
            case 3:
                homeFragment03 = new HomeFragment03();
                return homeFragment03;
            case 4:
                homeFragment04 = new HomeFragment04();
                return homeFragment04;
            default:
                //不处理
                break;
        }
        homeFragment01 = new HomeFragment01();
        return homeFragment01;
    }
    private void hideAllFragment(FragmentTransaction transaction){
        if (homeFragment01 != null) {
            transaction.hide(homeFragment01);
        }
        if (homeFragment02 != null) {
            transaction.hide(homeFragment02);
        }
        if (homeFragment03 != null) {
            transaction.hide(homeFragment03);
        }
        if (homeFragment04 != null) {
            transaction.hide(homeFragment04);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("currentFragmentIndex", currentFragmentIndex);// 保存上次记录的fragment对应的index
        // 主要是因为当Activity重新创建的时候lastfragment会被清空，所以就记录上次lastfragment对应的tag，这样才能保证不会出错
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onPause() {
        super.onPause();

        if(homeFragment01!=null&&homeFragment01.isResumeSelf()){
            homeFragment01.onPauseSelf();
        }
        if(homeFragment02!=null&&homeFragment02.isResumeSelf()){
            homeFragment02.onPauseSelf();
        }
        if(homeFragment03!=null&&homeFragment03.isResumeSelf()){
            homeFragment03.onPauseSelf();
        }
        if(homeFragment04!=null&&homeFragment04.isResumeSelf()){
            homeFragment04.onPauseSelf();
        }
    }




    @Override
    protected void onResume() {
        super.onResume();
        switch (currentFragmentIndex){
            case 1:
                if(homeFragment01!=null&&!homeFragment01.isResumeSelf()){
                    homeFragment01.onResumeSelf();
                }
                break;
            case 2:
                if(homeFragment02!=null&&!homeFragment02.isResumeSelf()){
                    homeFragment02.onResumeSelf();
                }
                break;
            case 3:
                if(homeFragment03!=null&&!homeFragment03.isResumeSelf()){
                    homeFragment03.onResumeSelf();
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
