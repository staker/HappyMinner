package com.ailink.fragment;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.ailink.util.Logg;

import juhuiwan.ailink.android.R;


/**
 *这是交易界面，界面顶部的  交易所，下面对应切换为此交易所的产品
 */
public class FriendFragment extends BaseFragment{
    private TextView txt01,txt02,txt03,txt04,txt05,txt06;

    FriendDuFragment friendDuFragment01,friendDuFragment02,friendDuFragment03,friendDuFragment04,friendDuFragment05,friendDuFragment06;
    int currentFragmentIndex=0;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_friend;
    }
    @Override
    public void initViews(View view) {
        txt01=findViewById(R.id.txt_01);
        txt02=findViewById(R.id.txt_02);
        txt03=findViewById(R.id.txt_03);
        txt04=findViewById(R.id.txt_04);
        txt05=findViewById(R.id.txt_05);
        txt06=findViewById(R.id.txt_06);
        txt01.setOnClickListener(this);
        txt02.setOnClickListener(this);
        txt03.setOnClickListener(this);
        txt04.setOnClickListener(this);
        txt05.setOnClickListener(this);
        txt06.setOnClickListener(this);
        setFragment(1);
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
                transaction.add(R.id.frame_friend, fragment);
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
        if (friendDuFragment01 != null) {
            transaction.hide(friendDuFragment01);
        }
        if (friendDuFragment02 != null) {
            transaction.hide(friendDuFragment02);
        }
        if (friendDuFragment03 != null) {
            transaction.hide(friendDuFragment03);
        }
        if (friendDuFragment04 != null) {
            transaction.hide(friendDuFragment04);
        }
        if (friendDuFragment05 != null) {
            transaction.hide(friendDuFragment05);
        }
        if (friendDuFragment06 != null) {
            transaction.hide(friendDuFragment06);
        }
    }



    private BaseFragment getNewFragment(int index) {
        switch (index) {
            case 1:
                friendDuFragment01  = new FriendDuFragment();
                friendDuFragment01.setFriendDu(1);
                return friendDuFragment01;
            case 2:
                friendDuFragment02  = new FriendDuFragment();
                friendDuFragment02.setFriendDu(2);
                return friendDuFragment02;
            case 3:
                friendDuFragment03  = new FriendDuFragment();
                friendDuFragment03.setFriendDu(3);
                return friendDuFragment03;
            case 4:
                friendDuFragment04  = new FriendDuFragment();
                friendDuFragment04.setFriendDu(4);
                return friendDuFragment04;
            case 5:
                friendDuFragment05  = new FriendDuFragment();
                friendDuFragment05.setFriendDu(5);
                return friendDuFragment05;
            case 6:
                friendDuFragment06  = new FriendDuFragment();
                friendDuFragment06.setFriendDu(6);
                return friendDuFragment06;
            default:
                //不处理
                break;
        }
        friendDuFragment01  = new FriendDuFragment();
        friendDuFragment01.setFriendDu(1);
        return new FriendDuFragment();
    }
    private BaseFragment getOldFragment(int index) {
        switch (index) {
            case 1:
                return friendDuFragment01;
            case 2:
                return friendDuFragment02;
            case 3:
                return friendDuFragment03;
            case 4:
                return friendDuFragment04;
            case 5:
                return friendDuFragment05;
            case 6:
                return friendDuFragment06;
        }
        return friendDuFragment01;
    }


    private void clickDu(int du){
        if (currentFragmentIndex == du) {
            Logg.e("原样返回");
            return;
        }
        clearBackgroundAndColor();
        switch (du){
            case 1:
                txt01.setBackgroundResource(R.drawable.shape_blue_btn);
                txt01.setTextColor(Color.WHITE);
                break;
            case 2:
                txt02.setBackgroundResource(R.drawable.shape_blue_btn);
                txt02.setTextColor(Color.WHITE);
                break;
            case 3:
                txt03.setBackgroundResource(R.drawable.shape_blue_btn);
                txt03.setTextColor(Color.WHITE);
                break;
            case 4:
                txt04.setBackgroundResource(R.drawable.shape_blue_btn);
                txt04.setTextColor(Color.WHITE);
                break;
            case 5:
                txt05.setBackgroundResource(R.drawable.shape_blue_btn);
                txt05.setTextColor(Color.WHITE);
                break;
            case 6:
                txt06.setBackgroundResource(R.drawable.shape_blue_btn);
                txt06.setTextColor(Color.WHITE);
                break;
        }
        setFragment(du);
    }

    private void clearBackgroundAndColor(){
        txt01.setBackgroundColor(Color.TRANSPARENT);
        txt02.setBackgroundColor(Color.TRANSPARENT);
        txt03.setBackgroundColor(Color.TRANSPARENT);
        txt04.setBackgroundColor(Color.TRANSPARENT);
        txt05.setBackgroundColor(Color.TRANSPARENT);
        txt06.setBackgroundColor(Color.TRANSPARENT);
        txt01.setTextColor(Color.parseColor("#999999"));
        txt02.setTextColor(Color.parseColor("#999999"));
        txt03.setTextColor(Color.parseColor("#999999"));
        txt04.setTextColor(Color.parseColor("#999999"));
        txt05.setTextColor(Color.parseColor("#999999"));
        txt06.setTextColor(Color.parseColor("#999999"));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.txt_01:
                clickDu(1);
                break;
            case R.id.txt_02:
                clickDu(2);
                break;
            case R.id.txt_03:
                clickDu(3);
                break;
            case R.id.txt_04:
                clickDu(4);
                break;
            case R.id.txt_05:
                clickDu(5);
                break;
            case R.id.txt_06:
                clickDu(6);
                break;
        }
    }
}
