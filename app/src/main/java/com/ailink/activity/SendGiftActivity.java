package com.ailink.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ailink.adapter.AdapterGift;
import com.ailink.adapter.AdapterIncomeRecord;
import com.ailink.adapter.AdapterInviteReward;
import com.ailink.adapter.base.DividerItemDecoration;
import com.ailink.constants.ConstantString;
import com.ailink.db.Configuration;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.i.ResultListener;
import com.ailink.logic.TextColorSetting;
import com.ailink.pojo.GiftPojo;
import com.ailink.pojo.IncomePojo;
import com.ailink.pojo.InvitePojo;
import com.ailink.pojo.UserPojo;
import com.ailink.util.BroadcastUtil;
import com.ailink.util.Logg;
import com.ailink.view.LoadingDialog;
import com.ailink.view.RoundImageView;
import com.ailink.view.util.SpringViewUtil;
import com.ailink.view.util.StatusBarUtil;
import com.ailink.view.util.ToastUtil;
import com.ailink.view.util.TopBarLayoutUtil;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;
import libs.glide.GlideUtil;


/**
 *
 */
public class SendGiftActivity extends BaseActivity {
    private TextView txtSend,txtTotalPrice,txtAsset,txtName,txtSpeed,txtIntro;
    private RoundImageView imgHead;
    private TopBarLayoutUtil topBar;
    private RecyclerView recycler;
    LoadingDialog loadingDialog;
    ArrayList<GiftPojo> list;
    AdapterGift adapter;
    long userId;
    int speed;
    int totalPrice=0;//礼物总价格
    String userAvatar,userName;
    boolean isRequesting=false;
    @Override
    protected void initViews(Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarFullScreem(this);
        setContentView(R.layout.activity_send_gift);
        recycler = (RecyclerView)findViewById(R.id.recycler_gift);
        txtSend=(TextView) findViewById(R.id.txt_send);
        txtTotalPrice=(TextView) findViewById(R.id.txt_total_price);
        txtAsset=(TextView) findViewById(R.id.txt_asset);
        txtName=(TextView) findViewById(R.id.txt_name);
        txtSpeed=(TextView) findViewById(R.id.txt_speed);
        txtIntro=(TextView) findViewById(R.id.txt_intro);
        imgHead=(RoundImageView) findViewById(R.id.img_head);
        initViewsClickLinstener(txtSend);
        init();
        initTopBar();
        initRecyclerView();
        refreshData();

    }

    private void init() {
        loadingDialog=LoadingDialog.getInstance();
        Intent intent=getIntent();
        userName=intent.getStringExtra("userName");
        userAvatar=intent.getStringExtra("avatarUrl");
        userId=intent.getLongExtra("userId",0);
        speed=intent.getIntExtra("speed",0);
        txtName.setText(userName);
        txtSpeed.setText(""+speed);
        GlideUtil.getInstance().setImage(imgHead,userAvatar);
        list=new ArrayList<GiftPojo>();
        String sIntro="通过赠送礼物，可以提高对方的算力";
        TextColorSetting.addTextColor2String(txtIntro,"规则说明:",sIntro,"#FF2F2F","#333333");
        TextColorSetting.addTextColor2String(txtTotalPrice,"礼物总价 ","0","#333333","#F23249");

        initMyAsset();
        ServerUtil.getMyInfo(new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                initMyAsset();
            }
        });
    }
    private void initMyAsset(){
        UserPojo userPojo= Configuration.getInstance(this).getUserPojo();
        if(userPojo!=null){
            txtAsset.setText("蓝钻:"+(int)userPojo.totalAsset);
        }

    }
    private void initTopBar() {
        topBar = new TopBarLayoutUtil(this);
        topBar.setLeftTextColor(Color.WHITE);
        topBar.setLeftImage(R.drawable.icon_back_white);
        topBar.setTitle("赠送礼物");
        topBar.setBackListener(new TopBarLayoutUtil.IOnClick() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration itemDecoration=new DividerItemDecoration(this,1);
        recycler.addItemDecoration(itemDecoration);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setHasFixedSize( true);
        recycler.setNestedScrollingEnabled(false);
        adapter =new AdapterGift(this, list, new ResultListener() {
            @Override
            public void onSucess(Object object) {
                totalPrice=0;
                for(GiftPojo giftPojo : list){
                    totalPrice=totalPrice+giftPojo.price*giftPojo.count;
                }
                TextColorSetting.addTextColor2String(txtTotalPrice,"礼物总价 ",""+totalPrice,"#333333","#F23249");
                Logg.e("数量有变动="+totalPrice);
            }
        });
        recycler.setAdapter(adapter);
    }


    private void refreshData(){
        ServerUtil.getGiftList(new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                ArrayList<GiftPojo> temp=(ArrayList<GiftPojo>)object;
                if(temp!=null){
                    list.clear();
                    list.addAll(temp);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
    private void sendData(){
        if(isRequesting){
            return;
        }
        isRequesting=true;
        loadingDialog.showDialogLoading(true,this,null);
        ServerUtil.sendGift(userId, list, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                isRequesting=false;
                loadingDialog.showDialogLoading(false,mContext,null);
                BroadcastUtil.sendBroadcast1(mContext,""+userId, ConstantString.BroadcastActions.Action_Replace_Miner_Success);
                ToastUtil.showToast("礼物赠送成功",mContext);
                finish();
            }
            @Override
            public void onDataError(String dataError) {
                super.onDataError(dataError);
                isRequesting=false;
                loadingDialog.showDialogLoading(false,mContext,null);
                ToastUtil.showToast(dataError,mContext);
            }

            @Override
            public void onException(Exception e) {
                super.onException(e);
                isRequesting=false;
                loadingDialog.showDialogLoading(false,mContext,null);
            }
        });
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.txt_send:
                if(totalPrice<=0){
                    ToastUtil.showToast("还没有选择礼物",this);
                    return;
                }
                sendData();
                break;
        }
    }
}
