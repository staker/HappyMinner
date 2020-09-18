package com.ailink.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ailink.adapter.AdapterSearchList;
import com.ailink.adapter.AdapterTradeMsg;
import com.ailink.adapter.base.BaseRecyclerAdapter;
import com.ailink.db.ApplicationData;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.pojo.TradePojo;
import com.ailink.pojo.UserPojo;
import com.ailink.util.Logg;
import com.ailink.view.LoadingDialog;
import com.ailink.view.util.SpringViewUtil;
import com.ailink.view.util.TopBarLayoutUtil;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;


/**
 *
 */
public class MessageActivity extends BaseActivity {
    private TextView txtNoticeCount;

    AdapterTradeMsg adapter;
    ArrayList<TradePojo> list;
    SpringViewUtil springViewUtil;
    TopBarLayoutUtil topBar;
    int page =1;//获取第几页的数据
    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_message);
        findViewById(R.id.layout_notice).setOnClickListener(this);
        txtNoticeCount=(TextView)findViewById(R.id.txt_msg_count);

        springViewUtil=new SpringViewUtil(this,false);
        init();
        initRecyclerView();
        refreshData();


    }

    private void init() {
        list=new ArrayList<TradePojo>();
        initTopBar();
    }
    private void initTopBar() {
        topBar = new TopBarLayoutUtil(this);
        topBar.seBottomLineColor(Color.argb(255, 234, 234, 234));
        topBar.setTitle("消息中心");
        topBar.setBackListener(new TopBarLayoutUtil.IOnClick() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(txtNoticeCount==null){
            return;
        }
        if(ApplicationData.getInstance().noticeCount>0){
            txtNoticeCount.setVisibility(View.VISIBLE);
            txtNoticeCount.setText(""+ApplicationData.getInstance().noticeCount);
        }else{
            txtNoticeCount.setVisibility(View.GONE);
        }
    }

    private void initRecyclerView() {
        adapter=new AdapterTradeMsg(this,list);
        springViewUtil.recyclerView.setAdapter(adapter);
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
        ServerUtil.getTradeMsgList(page, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                if(ApplicationData.getInstance().noticeCount>0){
                    txtNoticeCount.setVisibility(View.VISIBLE);
                    txtNoticeCount.setText(""+ApplicationData.getInstance().noticeCount);
                }else{
                    txtNoticeCount.setVisibility(View.GONE);
                }
                springViewUtil.setFinishRefresh();
                ArrayList<TradePojo> temp=(ArrayList<TradePojo>)object;
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
        ServerUtil.getTradeMsgList(page, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                springViewUtil.setFinishRefresh();
                ArrayList<TradePojo> temp=(ArrayList<TradePojo>)object;
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








































    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.layout_notice:
                JumpActivityUtil.showNormalActivity(this,NoticeActivity.class);
                break;
        }
    }
}
