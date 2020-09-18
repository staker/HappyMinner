package com.ailink.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.ailink.adapter.AdapterIncomeRecord;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.pojo.IncomePojo;
import com.ailink.util.Logg;
import com.ailink.view.util.SpringViewUtil;
import com.ailink.view.util.TopBarLayoutUtil;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;


/**
 *
 */
public class SpeedRecordActivity extends BaseActivity {

    public static int speedAll;
    public static int speedGift;
    private TopBarLayoutUtil topBar;
    private TextView txtSpeed;
    ArrayList<IncomePojo> list;
    SpringViewUtil springViewUtil;
    AdapterIncomeRecord adapter;
    int page =1;//获取第几页的数据
    long userId;
    int speed;
    String userAvatar,userName;
    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_speed_record);
        springViewUtil=new SpringViewUtil(this,false);
        txtSpeed=(TextView)findViewById(R.id.txt_speed);
        init();
        initRecyclerView();
        refreshData();

    }

    private void init() {
        Intent intent=getIntent();
        userName=intent.getStringExtra("userName");
        userAvatar=intent.getStringExtra("avatarUrl");
        userId=intent.getLongExtra("userId",0);
        speed=intent.getIntExtra("speed",0);
        txtSpeed.setText(""+speed);
        list=new ArrayList<IncomePojo>();

        topBar = new TopBarLayoutUtil(this);
        topBar.seBottomLineColor(Color.argb(255, 234, 234, 234));
        topBar.setTitle("算力记录");
        topBar.setBackListener(new TopBarLayoutUtil.IOnClick() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }


    private void initRecyclerView() {
        adapter=new AdapterIncomeRecord(this,list);
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
        ServerUtil.getIncomeRecord(page, userId,new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                springViewUtil.setFinishRefresh();
                ArrayList<IncomePojo> temp=(ArrayList<IncomePojo>)object;
//                Logg.e("size="+temp.size());
                if(temp!=null){
                    list.clear();
                    list.addAll(temp);
                    adapter.notifyDataSetChanged();
                }
                if(speedGift!=0){
                    txtSpeed.setText(speedAll+"("+speedGift+")");
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
        ServerUtil.getIncomeRecord(page, userId,new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                springViewUtil.setFinishRefresh();
                ArrayList<IncomePojo> temp=(ArrayList<IncomePojo>)object;
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
