package com.ailink.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.ailink.adapter.AdapterPool;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.i.ResultListener;
import com.ailink.pojo.PoolPojo;
import com.ailink.util.Logg;
import com.ailink.util.PhoneDeviceUtil;
import com.ailink.view.util.TopBarLayoutUtil;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;


/**
 *这是app的引导界面，只会在第一次进来app的时候展示，其他的时候不显示
 */
public class PoolActivity extends BaseActivity {
    TopBarLayoutUtil topBar;

    RecyclerView recycler;
    AdapterPool adapter;
    ArrayList<PoolPojo> listPool;
    boolean isRequesting=false;
    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pool);
        recycler=(RecyclerView)findViewById(R.id.recycler_pool);
        init();
        initTopBar();
        initRecyclerMiner();
        refreshData();
    }

    private void init() {
        listPool=new ArrayList<PoolPojo>();
    }
    private void initTopBar() {
        topBar = new TopBarLayoutUtil(this);
        topBar.seBottomLineColor(Color.argb(255, 234, 234, 234));
        topBar.setTitle("矿池");
        topBar.setBackListener(new TopBarLayoutUtil.IOnClick() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }
    private void initRecyclerMiner() {
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setHasFixedSize( true);
        recycler.setNestedScrollingEnabled(false);
        adapter =new AdapterPool(this, listPool, new ResultListener() {
            @Override
            public void onSucess(Object object) {
                refreshData();
            }
        });
        recycler.setAdapter(adapter);
    }

    private void refreshData(){
        isRequesting=true;
        ServerUtil.getPoolList(new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                ArrayList<PoolPojo> temp=(ArrayList<PoolPojo>)object;
                if(temp!=null){
                    listPool.clear();
                    listPool.addAll(temp);
                    adapter.notifyDataSetChanged();
                }
                isRequesting=false;
            }

            @Override
            public void onDataError(String dataError) {
                super.onDataError(dataError);
                isRequesting=false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isRequesting||listPool==null||recycler==null){
            return;
        }
        refreshData();
    }
}
