package com.ailink.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ailink.adapter.AdapterFangzhiMiner;
import com.ailink.adapter.AdapterReplaceMiner;
import com.ailink.constants.ConstantString;
import com.ailink.db.ApplicationData;
import com.ailink.db.Configuration;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.logic.TextColorSetting;
import com.ailink.pojo.UserPojo;
import com.ailink.util.BroadcastUtil;
import com.ailink.util.Logg;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;


/**
 *
 */
public class FangzhiMinerActivity extends Activity implements  View.OnClickListener{
    private RecyclerView recycler;
    private CardView cardViewZhaomu01,cardViewZhaomu02,cardViewZhaomu03,cardViewZhaomu04,cardViewZhaomu05,cardViewZhaomu06;

    UserPojo userSelf;
    ArrayList<UserPojo> listMiner;
    AdapterFangzhiMiner adapter;

    long removeUserId;
    int poolId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fangzhi_miner);
        recycler = (RecyclerView)findViewById(R.id.recycler_my_miner);
        cardViewZhaomu01=(CardView)findViewById(R.id.card_zhaomu_01);
        cardViewZhaomu02=(CardView)findViewById(R.id.card_zhaomu_02);
        cardViewZhaomu03=(CardView)findViewById(R.id.card_zhaomu_03);
        cardViewZhaomu04=(CardView)findViewById(R.id.card_zhaomu_04);
        cardViewZhaomu05=(CardView)findViewById(R.id.card_zhaomu_05);
        cardViewZhaomu06=(CardView)findViewById(R.id.card_zhaomu_06);
        findViewById(R.id.txt_find_miner01).setOnClickListener(this);
        findViewById(R.id.txt_find_miner02).setOnClickListener(this);
        findViewById(R.id.txt_find_miner03).setOnClickListener(this);
        findViewById(R.id.txt_find_miner04).setOnClickListener(this);
        findViewById(R.id.txt_find_miner05).setOnClickListener(this);
        findViewById(R.id.txt_find_miner06).setOnClickListener(this);
        findViewById(R.id.layout_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        init();
        initRecyclerMyMiner();
    }

    private void init() {
        userSelf= Configuration.getInstance(this).getUserPojo();
        if(userSelf==null){
            userSelf=new UserPojo();
        }
        listMiner=new ArrayList<UserPojo>();

        Intent intent=getIntent();
        removeUserId=intent.getLongExtra("removeUserId",0);
        poolId=intent.getIntExtra("poolId",0);

        refreshData();
    }


    private void refreshData(){
        ServerUtil.getMyMinerListForPool(poolId,userSelf, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                ArrayList<UserPojo> temp=(ArrayList<UserPojo>)object;
                if(temp!=null&&temp.size()!=0){
                    listMiner.clear();
                    listMiner.addAll(temp);
                    adapter.notifyDataSetChanged();
                }
                updateZhaoMuCard();
            }
        });


    }


    private void initRecyclerMyMiner() {
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setHasFixedSize( true);
        recycler.setNestedScrollingEnabled(false);
        adapter =new AdapterFangzhiMiner(this, removeUserId,poolId,listMiner);
        recycler.setAdapter(adapter);
    }
    public void updateZhaoMuCard(){
        int size=listMiner.size();
        int count=userSelf.minerPkgCount-size;
        switch (count){
            case 1:
                cardViewZhaomu01.setVisibility(View.VISIBLE);
                cardViewZhaomu02.setVisibility(View.GONE);
                cardViewZhaomu03.setVisibility(View.GONE);
                cardViewZhaomu04.setVisibility(View.GONE);
                cardViewZhaomu05.setVisibility(View.GONE);
                cardViewZhaomu06.setVisibility(View.GONE);
                break;
            case 2:
                cardViewZhaomu01.setVisibility(View.VISIBLE);
                cardViewZhaomu02.setVisibility(View.VISIBLE);
                cardViewZhaomu03.setVisibility(View.GONE);
                cardViewZhaomu04.setVisibility(View.GONE);
                cardViewZhaomu05.setVisibility(View.GONE);
                cardViewZhaomu06.setVisibility(View.GONE);
                break;
            case 3:
                cardViewZhaomu01.setVisibility(View.VISIBLE);
                cardViewZhaomu02.setVisibility(View.VISIBLE);
                cardViewZhaomu03.setVisibility(View.VISIBLE);
                cardViewZhaomu04.setVisibility(View.GONE);
                cardViewZhaomu05.setVisibility(View.GONE);
                cardViewZhaomu06.setVisibility(View.GONE);
                break;
            case 4:
                cardViewZhaomu01.setVisibility(View.VISIBLE);
                cardViewZhaomu02.setVisibility(View.VISIBLE);
                cardViewZhaomu03.setVisibility(View.VISIBLE);
                cardViewZhaomu04.setVisibility(View.VISIBLE);
                cardViewZhaomu05.setVisibility(View.GONE);
                cardViewZhaomu06.setVisibility(View.GONE);
                break;
            case 5:
                cardViewZhaomu01.setVisibility(View.VISIBLE);
                cardViewZhaomu02.setVisibility(View.VISIBLE);
                cardViewZhaomu03.setVisibility(View.VISIBLE);
                cardViewZhaomu04.setVisibility(View.VISIBLE);
                cardViewZhaomu05.setVisibility(View.VISIBLE);
                cardViewZhaomu06.setVisibility(View.GONE);
                break;
            case 6:
                cardViewZhaomu01.setVisibility(View.VISIBLE);
                cardViewZhaomu02.setVisibility(View.VISIBLE);
                cardViewZhaomu03.setVisibility(View.VISIBLE);
                cardViewZhaomu04.setVisibility(View.VISIBLE);
                cardViewZhaomu05.setVisibility(View.VISIBLE);
                cardViewZhaomu06.setVisibility(View.VISIBLE);
                break;

        }


    }
    @Override
    public void onClick(View v) {
                switch (v.getId()){
            case R.id.txt_find_miner01:
            case R.id.txt_find_miner02:
            case R.id.txt_find_miner03:
            case R.id.txt_find_miner04:
            case R.id.txt_find_miner05:
            case R.id.txt_find_miner06:
            case R.id.txt_mission01:
                ApplicationData.getInstance().poolId=poolId;
                JumpActivityUtil.showMinerActivity(this,2);
//                BroadcastUtil.sendBroadcast(this,ConstantString.BroadcastActions.Action_Home_Jump_To_Buy_Miner);
//                ApplicationData.getInstance().isShowRecommendMiner=true;
//                BroadcastUtil.sendBroadcast1(this,"3", ConstantString.BroadcastActions.Action_Change_Bottom_Tab);
                finish();
                break;
        }
    }
}
