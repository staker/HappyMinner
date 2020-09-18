package com.ailink.fragment;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ailink.activity.BaseActivity;
import com.ailink.adapter.AdapterPool;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.i.ResultListener;
import com.ailink.pojo.PoolPojo;
import com.ailink.view.util.StatusBarUtil;
import com.ailink.view.util.TopBarLayoutUtil;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;

public class AKuangChi001 extends BaseFragment{
    RecyclerView recycler;
    AdapterPool adapter;
    TopBarLayoutUtil topBar;
;   ArrayList<PoolPojo> listPool;
    boolean isRequesting=false;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_home02;
    }

    @Override
    public void initViews(View view) {
        recycler=findViewById(R.id.recycler_pool);
        topBar = new TopBarLayoutUtil(view);
        init();
        initTopBar();
        initRecyclerMiner();
        refreshData();
    }
    @Override
    public void onResumeSelf() {
        super.onResumeSelf();
        StatusBarUtil.clearFullScreem((BaseActivity) getActivity());
        if(isRequesting){
            return;
        }
        refreshData();
    }

    private void init(){
        listPool=new ArrayList<PoolPojo>();
    }
    private void initTopBar() {

        topBar.seBottomLineColor(Color.argb(255, 234, 234, 234));
        topBar.hideLeft();
        topBar.setTitle("矿池");

    }
    private void initRecyclerMiner() {

        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setHasFixedSize( true);
        recycler.setNestedScrollingEnabled(false);
        adapter =new AdapterPool(getActivity(), listPool, new ResultListener() {
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


}
