package com.ailink.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ailink.adapter.AdapterDailyBaseMission;
import com.ailink.adapter.base.DividerItemDecoration;
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
public class MissionActivity extends BaseActivity {
    private TopBarLayoutUtil topBar;
    private RecyclerView recyclerDaily,recyclerBase;
    private AdapterDailyBaseMission adapterDaily,adapterBase;
    private TextView txtDailyMission,txtBaseMission;
    private CardView cardViewDaily,cardViewBase,cardViewRecyclerDaily,cardViewRecyclerBase;
    private TextView txtAsset;

    ArrayList<DailyMissionPojo> listDaily;
    ArrayList<DailyMissionPojo> listBase;
    boolean isRequesting=false;
    boolean isShowDaily=true;
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
        setContentView(R.layout.activity_mission);
        recyclerDaily=(RecyclerView)findViewById(R.id.recycler_daily);
        recyclerBase=(RecyclerView)findViewById(R.id.recycler_base);
        txtAsset=(TextView)findViewById(R.id.txt_asset);
        txtDailyMission=(TextView)findViewById(R.id.txt_daily_mission);
        txtBaseMission=(TextView)findViewById(R.id.txt_base_mission);
        cardViewDaily=(CardView)findViewById(R.id.card_daily_mission);
        cardViewBase=(CardView)findViewById(R.id.card_base_mission);
        cardViewRecyclerDaily=(CardView)findViewById(R.id.card_recycler_daily);
        cardViewRecyclerBase=(CardView)findViewById(R.id.card_recycler_base);
        initViewsClickLinstener(txtDailyMission,txtBaseMission,cardViewDaily,cardViewBase);
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
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerDaily.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        recyclerDaily.setLayoutManager(layoutManager);
        recyclerDaily.setHasFixedSize( true);
        recyclerDaily.setNestedScrollingEnabled(false);
        adapterDaily =new AdapterDailyBaseMission(this, listDaily);
        recyclerDaily.setAdapter(adapterDaily);
    }

    private void initRecycleBase() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerBase.setLayoutManager(layoutManager);
        recyclerBase.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        recyclerBase.setHasFixedSize( true);
        recyclerBase.setNestedScrollingEnabled(false);
        adapterBase =new AdapterDailyBaseMission(this, listBase);
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
        switch (v.getId()){
            case R.id.txt_daily_mission:
            case  R.id.card_daily_mission:
                clickDailyMission();
                isShowDaily=true;
                break;
            case R.id.txt_base_mission:
            case  R.id.card_base_mission:
                clickBaseMission();
                isShowDaily=false;
                break;

        }
    }
    private  void clickDailyMission(){
        if(isShowDaily){
            return;
        }
        txtDailyMission.setTextColor(Color.WHITE);
        txtBaseMission.setTextColor(Color.BLACK);
        cardViewDaily.setCardBackgroundColor(Color.parseColor("#2582FF"));
        cardViewBase.setCardBackgroundColor(Color.WHITE);
//        cardViewDaily.setBackgroundResource(R.drawable.shape_gradient_full_blue02_5);
//        cardViewBase.setBackgroundResource(R.drawable.shape_full_white_5);
//        cardViewBase.
//        cardViewBase.setBackgroundColor(Color.TRANSPARENT);
//        cardViewBase.setCardBackgroundColor(Color.WHITE);
//        cardViewBase.
//        cardDailyMission.setBackgroundResource(R.drawable.shape_gradient_full_blue02_5);
//        txtBaseMission.setBackgroundResource(R.drawable.shape_full_white_5);
        cardViewRecyclerDaily.setVisibility(View.VISIBLE);
        cardViewRecyclerBase.setVisibility(View.GONE);

    }
    private  void clickBaseMission(){
        if(!isShowDaily){
            return;
        }
        txtDailyMission.setTextColor(Color.BLACK);
        txtBaseMission.setTextColor(Color.WHITE);
//        txtBaseMission.setBackgroundResource(R.drawable.shape_gradient_full_blue02_5);
//        txtDailyMission.setBackgroundResource(R.drawable.shape_full_white_5);
//        cardViewDaily.setBackgroundColor(Color.TRANSPARENT);
        cardViewDaily.setCardBackgroundColor(Color.WHITE);
        cardViewBase.setCardBackgroundColor(Color.parseColor("#2582FF"));

//        cardViewDaily.setBackgroundColor(Color.WHITE);
//        cardViewDaily.setCardBackgroundColor(Color.WHITE);
//        cardViewBase.setBackgroundResource(R.drawable.shape_gradient_full_blue02_5);

        cardViewRecyclerDaily.setVisibility(View.GONE);
        cardViewRecyclerBase.setVisibility(View.VISIBLE);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
