package com.ailink.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ailink.adapter.AdapterDailyMission;
import com.ailink.adapter.base.DividerGridItemDecoration;
import com.ailink.constants.ConstantString;
import com.ailink.db.Configuration;
import com.ailink.db.MyApplication;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.pojo.DailyMissionPojo;
import com.ailink.pojo.UserPojo;
import com.ailink.util.BroadcastUtil;
import com.ailink.util.Logg;
import com.ailink.view.util.StatusBarUtil;
import com.ailink.view.util.TopBarLayoutUtil;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;


/**
 *
 */
public class MissionActivity3 extends BaseActivity {
    private TopBarLayoutUtil topBar;
    private RecyclerView recyclerDaily,recyclerBase;
    private AdapterDailyMission adapterDaily,adapterBase;
    private TextView txtAsset;

    ArrayList<DailyMissionPojo> listDaily;
    ArrayList<DailyMissionPojo> listBase;
    boolean isRequesting=false;
    BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int size=listDaily.size();
            for(DailyMissionPojo temp : listDaily){
                if("每日分享".equals(temp.name)){
                    temp.status="yes";
                    adapterDaily.notifyDataSetChanged();
                    ServerUtil.sendDailyShare();
                }
            }
        }
    };
    @Override
    protected void initViews(Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarFullScreem(this);
        setContentView(R.layout.activity_mission3);
        recyclerDaily=(RecyclerView)findViewById(R.id.recycler_daily);
        recyclerBase=(RecyclerView)findViewById(R.id.recycler_base);
        txtAsset=(TextView)findViewById(R.id.txt_asset);
        init();
        initTopBar();
        initRecyclerDaily();
        initRecycleBase();
        refreshData();
    }
    private void initTopBar() {
        topBar = new TopBarLayoutUtil(this);
        topBar.setLeftTextColor(Color.WHITE);
        topBar.setLeftImage(R.drawable.icon_back_white);
        topBar.setTitle("任务");
        topBar.setBackListener(new TopBarLayoutUtil.IOnClick() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }
    private void init() {
        BroadcastUtil.registerReceiver1(this,receiver, ConstantString.BroadcastActions.Action_Share_Success);
        listDaily=new ArrayList<DailyMissionPojo>();
        listBase=new ArrayList<DailyMissionPojo>();
        UserPojo user= Configuration.getInstance(MyApplication.getContext()).getUserPojo();
        if(user==null){
            return;
        }
        txtAsset.setText("当前资产："+(int)user.totalAsset);
    }
    private void initRecyclerDaily() {
        GridLayoutManager layoutManager=new GridLayoutManager(this,3);
        recyclerDaily.addItemDecoration(new DividerGridItemDecoration(this));
        recyclerDaily.setLayoutManager(layoutManager);
        recyclerDaily.setHasFixedSize( true);
        recyclerDaily.setNestedScrollingEnabled(false);
        adapterDaily =new AdapterDailyMission(this, listDaily);
        recyclerDaily.setAdapter(adapterDaily);
    }

    private void initRecycleBase() {
        GridLayoutManager layoutManager=new GridLayoutManager(this,3);
        recyclerBase.setLayoutManager(layoutManager);
        recyclerBase.addItemDecoration(new DividerGridItemDecoration(this));
        recyclerBase.setHasFixedSize( true);
        recyclerBase.setNestedScrollingEnabled(false);
        adapterBase =new AdapterDailyMission(this, listBase);
        recyclerBase.setAdapter(adapterBase);
    }

    private void refreshData(){
        if(isRequesting){
            return;
        }
        isRequesting=true;
        ServerUtil.getDailyMission(listDaily,listBase, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                Logg.e("size==="+listBase.size());
                isRequesting=false;
                adapterDaily.notifyDataSetChanged();
                adapterBase.notifyDataSetChanged();
            }
            public void onDataError(String dataError) {
                isRequesting=false;
            }
        });

        ServerUtil.getMyInfo(new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                UserPojo user= Configuration.getInstance(MyApplication.getContext()).getUserPojo();
                txtAsset.setText("当前资产："+(int)user.totalAsset);
                isRequesting=false;
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
