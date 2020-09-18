package com.ailink.activity;

import android.graphics.Color;
import android.os.Bundle;

import com.ailink.adapter.AdapterContract;
import com.ailink.adapter.AdapterMyContract;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.pojo.ContractPojo;
import com.ailink.util.Logg;
import com.ailink.view.util.SpringViewUtil;
import com.ailink.view.util.TopBarLayoutUtil;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;


/**
 *这是app的引导界面，只会在第一次进来app的时候展示，其他的时候不显示
 */
public class MyContractActivity extends BaseActivity {
    AdapterMyContract adapter;
    ArrayList<ContractPojo> list;
    SpringViewUtil springViewUtil;



    TopBarLayoutUtil topBar;
    int page =1;//获取第几页的数据
    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_contract);
        springViewUtil=new SpringViewUtil(this,false);
        initTopBar();
        init();
        initRecyclerView();
        refreshData();
    }

    private void init() {
        list=new ArrayList<ContractPojo>();
    }
    private void initTopBar() {
        topBar = new TopBarLayoutUtil(this);
        topBar.seBottomLineColor(Color.argb(255, 234, 234, 234));
        topBar.setTitle("我的合约");
        topBar.setBackListener(new TopBarLayoutUtil.IOnClick() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }
    private void initRecyclerView() {
        adapter=new AdapterMyContract(this,list);
        springViewUtil.recyclerView.setAdapter(adapter);
        springViewUtil.setDividerNone();
        springViewUtil.initDefaultHeader(this);
        springViewUtil.initDefaultFooter(this);
        springViewUtil.springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
            @Override
            public void onLoadmore() {
                loadMoreData();
            }
        });
    }
    private void refreshData(){
        page =1;
        ServerUtil.getMyContractList(page, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                springViewUtil.setFinishRefresh();
                ArrayList<ContractPojo> temp=(ArrayList<ContractPojo>)object;
                Logg.e("size="+temp.size());
                if(temp!=null){
                    list.clear();
                    list.addAll(temp);
                    adapter.notifyDataSetChanged();
                }
            }
            public void onDataError(String dataError) {
                springViewUtil.setFinishRefresh();
            }
            public void onException(Exception e) {
                springViewUtil.setFinishRefresh();
            }
        });
    }








    private void loadMoreData() {
        page++;
        ServerUtil.getMyContractList(page, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                springViewUtil.setFinishRefresh();
                ArrayList<ContractPojo> temp=(ArrayList<ContractPojo>)object;
                if(temp==null||temp.size()==0){
                    page--;
                    return;
                }
                list.addAll(temp);
                adapter.notifyDataSetChanged();
            }
            public void onDataError(String dataError)
            {
                page--;
                springViewUtil.setFinishRefresh();
            }
            public void onException(Exception e) {
                page--;
                springViewUtil.setFinishRefresh();
            }
        });
    }

}
