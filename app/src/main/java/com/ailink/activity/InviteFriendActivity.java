package com.ailink.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ailink.adapter.AdapterInviteReward;
import com.ailink.adapter.base.DividerItemDecoration;
import com.ailink.db.Configuration;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.pojo.InvitePojo;
import com.ailink.pojo.UserPojo;
import com.ailink.view.util.SmartRefreshLayoutUtil;
import com.ailink.view.util.StatusBarUtil;
import com.ailink.view.util.TopBarLayoutUtil;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;
import libs.util.umeng.UmengShareUtil;


/**
 *
 */
public class InviteFriendActivity extends BaseActivity {
    private RecyclerView recycler;
    private TextView txtTotalReward;
    TopBarLayoutUtil topBar;
    ArrayList<InvitePojo> listInvite;
    AdapterInviteReward adapter;
    SmartRefreshLayoutUtil smartRefreshLayoutUtil;
    @Override
    protected void initViews(Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarColor(this,Color.parseColor("#fe5340"));
        setContentView(R.layout.activity_invite_friend);
         recycler = (RecyclerView)findViewById(R.id.recycler_invite);
        txtTotalReward = (TextView)findViewById(R.id.txt_total_reward);
        findViewById(R.id.txt_invite_friend).setOnClickListener(this);

        init();
        initRecyclerInvite();
        initTopBar();
        refreshData();

    }
    private void init() {
        listInvite=new ArrayList<InvitePojo>();
    }

    private void initTopBar() {
        topBar = new TopBarLayoutUtil(this);
       topBar.setTopBarColor(Color.parseColor("#fe5340"));
        topBar.setTitle("邀请好友",Color.WHITE);
        topBar.setLeftTextColor(Color.WHITE);
        topBar.setLeftImage(R.drawable.icon_back_white);
        topBar.setBackListener(new TopBarLayoutUtil.IOnClick() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }

    private void refreshData(){
        ServerUtil.getMyInviteList(new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                ArrayList<InvitePojo> temp=(ArrayList<InvitePojo>)object;
                if(temp!=null){
                    listInvite.clear();
                    listInvite.addAll(temp);
                    adapter.notifyDataSetChanged();

                    int totalReward=0;
                    int size=temp.size();
                    for(int i=0;i<size;i++){
                        InvitePojo invitePojo=temp.get(i);
                        if(invitePojo.inviteCount>100){
                            totalReward=totalReward+100*invitePojo.inviteSingleReward;
                        }else {
                            totalReward=totalReward+invitePojo.inviteCount*invitePojo.inviteSingleReward;
                        }

                    }
                    txtTotalReward.setText(""+totalReward);
                }
            }
        });

    }


    private void initRecyclerInvite() {
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration itemDecoration=new DividerItemDecoration(this,1);
        recycler.addItemDecoration(itemDecoration);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setHasFixedSize( true);
        recycler.setNestedScrollingEnabled(false);
        adapter =new AdapterInviteReward(this, listInvite);
        recycler.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.txt_invite_friend:
                UserPojo userPojo=Configuration.getInstance(this).getUserPojo();
                UmengShareUtil.share(this,userPojo.userId,userPojo.userName,(int)userPojo.priceNew,userPojo.speed);
                break;
        }
    }
}
