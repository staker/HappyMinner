package com.ailink.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ailink.adapter.AdapterReplaceMiner;
import com.ailink.db.Configuration;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.logic.TextColorSetting;
import com.ailink.pojo.UserPojo;
import com.ailink.util.Logg;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;


/**
 *
 */
public class ReplaceMinerActivity extends Activity {
    private RecyclerView recycler;
    private TextView txtReplaceTimes;

    UserPojo userSelf;
    ArrayList<UserPojo> listMiner;
    AdapterReplaceMiner adapter;

    int replaceTimes;
    long userId;
    String userName;

//    @Override
//    protected void initViews(Bundle savedInstanceState) {
//        setContentView(R.layout.activity_replace_miner);
//        recycler = (RecyclerView)findViewById(R.id.recycler_my_miner);
//        init();
//        initRecyclerMyMiner();
//
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace_miner);
        recycler = (RecyclerView)findViewById(R.id.recycler_my_miner);
        txtReplaceTimes=(TextView) findViewById(R.id.txt_times);
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
        userName=intent.getStringExtra("userName");
        userId=intent.getLongExtra("userId",0);
        replaceTimes=intent.getIntExtra("replaceTimes",0);

        Logg.e("name="+userName);
        Logg.e("userId="+userId);
        Logg.e("replaceTimes="+replaceTimes);
        TextColorSetting.addTextColor3String(txtReplaceTimes,"今日还可替换 ",replaceTimes+"","次",Color.parseColor("#333333"),Color.parseColor("#dd0000"),Color.parseColor("#333333"));

        refreshData();
    }


    private void refreshData(){
        ServerUtil.getHomeMinerList(userSelf, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                ArrayList<UserPojo> temp=(ArrayList<UserPojo>)object;
                if(temp!=null&&temp.size()!=0){
                    listMiner.clear();
                    listMiner.addAll(temp);
                    adapter.notifyDataSetChanged();
                }
            }
        });


    }


    private void initRecyclerMyMiner() {
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setHasFixedSize( true);
        recycler.setNestedScrollingEnabled(false);
        adapter =new AdapterReplaceMiner(this, userId,listMiner);
        recycler.setAdapter(adapter);
//        adapter.setOnItemClickLitener(new BaseRecyclerAdapter.OnItemClickLitener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                UserPojo userPojo=listMiner.get(position);
//                JumpActivityUtil.showMinerHomeActivity(mContext,userPojo.userId,userPojo.userName,userPojo.avatarUrl);
//            }
//
//            @Override
//            public void onItemLongClick(View view, int position) {
//
//            }
//        });
    }

}
