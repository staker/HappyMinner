package com.ailink.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.ailink.activity.BaseActivity;
import com.ailink.activity.SearchMinerActivity;
import com.ailink.constants.ConstantString;
import com.ailink.db.ApplicationData;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.pojo.UserPojo;
import com.ailink.util.BroadcastUtil;
import com.ailink.util.Logg;
import com.ailink.view.util.StatusBarUtil;

import juhuiwan.ailink.android.R;


/**
 *这是交易界面，界面顶部的  交易所，下面对应切换为此交易所的产品
 */
public class HomeFragment03 extends BaseFragment{
    private FriendFragment friendFragment;
    private RecommendFragment recommendFragment;
    private TextView txtFriend,txtRecommend;

    int currentFragmentIndex=0;
    UserPojo userSelf;

    BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            txtFriend.setTextColor(Color.parseColor("#999999"));
            txtRecommend.setTextColor(Color.BLACK);
            setFragment(2);
            ApplicationData.getInstance().isShowRecommendMiner=false;
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home03;
    }
    @Override
    public void initViews(View view) {
        StatusBarUtil.clearFullScreem((BaseActivity) getActivity());
        txtFriend=findViewById(R.id.txt_friend);
        txtRecommend=findViewById(R.id.txt_recommend);
        findViewById(R.id.img_search).setOnClickListener(this);
        txtFriend.setOnClickListener(this);
        txtRecommend.setOnClickListener(this);
        init();
        BroadcastUtil.registerReceiver1(getActivity(),receiver, ConstantString.BroadcastActions.Action_Home_Jump_To_Buy_Miner);
    }
    @Override
    public void onResumeSelf() {
        super.onResumeSelf();
        Logg.e("home02    onResumeSelf  准备请求数据");
        StatusBarUtil.clearFullScreem((BaseActivity) getActivity());
        if(recommendFragment !=null){
            recommendFragment.onResumeSelf();
        }
    }

    @Override
    public void onPauseSelf() {
        super.onPauseSelf();
        ApplicationData.getInstance().poolId=0;
    }

    private void init(){
        if(ApplicationData.getInstance().isShowRecommendMiner){
            txtFriend.setTextColor(Color.parseColor("#999999"));
            txtRecommend.setTextColor(Color.BLACK);
            setFragment(2);
            ApplicationData.getInstance().isShowRecommendMiner=false;
        }else{
            setFragment(1);
        }
    }
    private void setFragment(final int position) {
        if (currentFragmentIndex == position) {
            return;
        }
        try {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            hideAllFragment(transaction);
            BaseFragment fragment = getOldFragment(position);
            if (fragment == null) {
                fragment = getNewFragment(position);
                transaction.add(R.id.frame_home02, fragment);
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

        if (friendFragment != null) {
            transaction.hide(friendFragment);
        }
        if (recommendFragment != null) {
            transaction.hide(recommendFragment);
        }
    }
    private BaseFragment getNewFragment(int index) {
        switch (index) {
            case 1:
                friendFragment =  new FriendFragment();
                return friendFragment;
            case 2:
                recommendFragment =  new RecommendFragment();
                return recommendFragment;
            default:
                //不处理
                break;
        }
        friendFragment = new FriendFragment();
        return friendFragment;
    }

    private BaseFragment getOldFragment(int index) {
        switch (index) {
            case 1:
                return friendFragment;
            case 2:
                return recommendFragment;
        }
        return friendFragment;
    }


    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){

            case R.id.txt_friend:
                txtRecommend.setTextColor(Color.parseColor("#999999"));
                txtFriend.setTextColor(Color.BLACK);
                setFragment(1);
                break;
            case R.id.txt_recommend:
                txtFriend.setTextColor(Color.parseColor("#999999"));
                txtRecommend.setTextColor(Color.BLACK);
                setFragment(2);
                break;
            case R.id.img_search:
                JumpActivityUtil.showNormalActivity(getActivity(), SearchMinerActivity.class);
                break;



        }
    }


    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(receiver);
        super.onDestroy();
    }
}
