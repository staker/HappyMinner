package com.ailink.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ailink.adapter.AdapterMyMiner;
import com.ailink.adapter.AdapterNotice;
import com.ailink.adapter.base.BaseRecyclerAdapter;
import com.ailink.db.ApplicationData;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.pojo.MissionPojo;
import com.ailink.pojo.NoticePojo;
import com.ailink.pojo.UserPojo;
import com.ailink.util.Logg;
import com.ailink.view.util.TopBarLayoutUtil;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;


/**
 *
 */
public class NoticeActivity extends BaseActivity {
    private RecyclerView recycler;
    TopBarLayoutUtil topBar;
    AdapterNotice adapter;
    ArrayList<NoticePojo> listNotice;
    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_notice);
        recycler = (RecyclerView)findViewById(R.id.recycler_notice);
        init();
        initRecyclerNotice();
        refreshData();

    }

    private void init() {
        listNotice=new ArrayList<NoticePojo>();
        initTopBar();
    }
    private void initTopBar() {
        topBar = new TopBarLayoutUtil(this);
        topBar.seBottomLineColor(Color.argb(255, 234, 234, 234));
        topBar.setTitle("系统公告");
        topBar.setBackListener(new TopBarLayoutUtil.IOnClick() {
            @Override
            public void onClick() {
                finish();
            }
        });

    }
    private void initRecyclerNotice() {
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setHasFixedSize( true);
        recycler.setNestedScrollingEnabled(false);
        adapter =new AdapterNotice(this, listNotice);
        recycler.setAdapter(adapter);
        adapter.setOnItemClickLitener(new BaseRecyclerAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                NoticePojo temp=listNotice.get(position);
                if("unread".equals(temp.status)){
                    temp.status="read";
                    ApplicationData.getInstance().noticeCount--;
                    adapter.notifyDataSetChanged();
                }
                JumpActivityUtil.showNoticeDetailActivity(mContext,temp.id);
            }
            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }


    private void refreshData(){
        ServerUtil.getNoticeList( new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                ArrayList<NoticePojo> temp=(ArrayList<NoticePojo>)object;
                if(temp!=null&&temp.size()!=0){
                    listNotice.clear();
                    listNotice.addAll(temp);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
