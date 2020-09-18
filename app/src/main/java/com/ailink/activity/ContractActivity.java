package com.ailink.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailink.adapter.AdapterContract;
import com.ailink.adapter.AdapterTradeMsg;
import com.ailink.constants.WebUrls;
import com.ailink.db.ApplicationData;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.i.ResultListener;
import com.ailink.logic.DialogSelectQuhao;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.pojo.ContractPojo;
import com.ailink.pojo.TradePojo;
import com.ailink.util.PhoneDeviceUtil;
import com.ailink.view.util.SpringViewUtil;
import com.ailink.view.util.StatusBarUtil;
import com.ailink.view.util.TopBarLayoutUtil;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;


/**
 */
public class ContractActivity extends BaseActivity {

    private LinearLayout layoutOrder;
    private ImageView imgOrder;
    private TextView txtOrder;
    AdapterContract adapter;
    ArrayList<ContractPojo> list;
    SpringViewUtil springViewUtil;

    TopBarLayoutUtil topBar;
    int page =1;//获取第几页的数据
    int order=1;//排序 1-高到低 2-低到高
    @Override
    protected void initViews(Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarFullScreem(this);
        setContentView(R.layout.activity_contract);

                layoutOrder=(LinearLayout)findViewById(R.id.layout_order);
        imgOrder=(ImageView)findViewById(R.id.img_order);
        txtOrder=(TextView)findViewById(R.id.txt_order);
        initIdsClickLinstener(R.id.card_new_contract,R.id.card_my_contract,R.id.layout_order);
        initTopBar();
        springViewUtil=new SpringViewUtil(this,false);
        init();
        initRecyclerView();
        refreshData();
    }

    private void init() {
        list=new ArrayList<ContractPojo>();
    }
    private void initTopBar() {
        topBar = new TopBarLayoutUtil(this);
        topBar.setLeftTextColor(Color.WHITE);
        topBar.setLeftImage(R.drawable.icon_back_white);
        topBar.setTitle("合约送礼");
        topBar.setRightDrawable(R.drawable.icon_contract, new TopBarLayoutUtil.IOnClick() {
            @Override
            public void onClick() {
                JumpActivityUtil.showWebActivity(mContext,"合约规则", WebUrls.App_Contract_Rule,true);
            }
        });
        topBar.setBackListener(new TopBarLayoutUtil.IOnClick() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }
    private void initRecyclerView() {
        adapter=new AdapterContract(this,list);
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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.card_new_contract:
                JumpActivityUtil.showNormalActivity(mContext,NewContractActivity.class);
                break;
            case R.id.card_my_contract:
                JumpActivityUtil.showNormalActivity(mContext,MyContractActivity.class);
                break;

            case R.id.layout_order:
                if(order==1){
                    order=2;
                    imgOrder.setBackgroundResource(R.drawable.icon_arrow_up);
                    txtOrder.setText("蓝钻低到高");

                }else{
                    order=1;
                    imgOrder.setBackgroundResource(R.drawable.icon_arrow_down);
                    txtOrder.setText("蓝钻高到低");
                }
                refreshData();
                break;

        }

    }


    private void refreshData(){
        page =1;
        ServerUtil.getContractList(order,page, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                springViewUtil.setFinishRefresh();
                ArrayList<ContractPojo> temp=(ArrayList<ContractPojo>)object;
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
        ServerUtil.getContractList(order,page, new HttpObjectListener() {
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
