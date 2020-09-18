package com.ailink.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailink.adapter.AdapterUserMiner;
import com.ailink.adapter.base.BaseRecyclerAdapter;
import com.ailink.constants.ConstantString;
import com.ailink.constants.ConstantString.OperateMinerTypes;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.i.ResultListener;
import com.ailink.logic.DialogBuyMiner;
import com.ailink.logic.DialogFireMiner;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.pojo.UserPojo;
import com.ailink.util.BroadcastUtil;
import com.ailink.util.StakerUtil;
import com.ailink.view.RoundImageView;
import com.ailink.view.util.StatusBarUtil;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;
import libs.glide.GlideUtil;
import libs.util.umeng.UmengShareUtil;


/**
 *
 */
public class MinerHomeActivity extends BaseActivity {
    RecyclerView recycler;
    AdapterUserMiner adapter;
    RoundImageView  imgOwner;
    TextView txtName,txtUserId,txtNewPrice,txtSpeed,txtTitle,txtBottom;
    ImageView imgSex,imgHeadBg;
    LinearLayout layoutBottom;


    UserPojo userSelf,userOwner;
    ArrayList<UserPojo> listMiner;//用户的矿工列表

    BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data01=intent.getStringExtra("data01");
            String desUserId=userSelf.userId+"";
            if(desUserId.equals(data01)){
                refreshData();
            }
        }
    };


    @Override
    protected void initViews(Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarFullScreem(this);
        setContentView(R.layout.activity_miner_home);
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_share).setOnClickListener(this);
        initIdsClickLinstener(R.id.layout_suanli,R.id.txt_send_gift);
        imgHeadBg=(ImageView)findViewById(R.id.img_head_bg);
        imgOwner=(RoundImageView)findViewById(R.id.img_owner);
        imgOwner.setOnClickListener(this);
        txtName=(TextView)findViewById(R.id.txt_name);
        txtBottom=(TextView)findViewById(R.id.txt_buy_fire);
        txtBottom.setOnClickListener(this);
        layoutBottom=(LinearLayout)findViewById(R.id.layout_bottom);
        txtUserId=(TextView)findViewById(R.id.txt_id);
        txtNewPrice=(TextView)findViewById(R.id.txt_shenjia);
        txtTitle=(TextView)findViewById(R.id.txt_title);
        txtSpeed=(TextView)findViewById(R.id.txt_suanli);
        imgSex=(ImageView)findViewById(R.id.img_sex);
        recycler=(RecyclerView)findViewById(R.id.recycler_miner);

        init();
        initRecyclerMiner();

    }
    private void initRecyclerMiner() {

        GridLayoutManager layoutManager=new GridLayoutManager(this,3);
        recycler.setLayoutManager(layoutManager);
        recycler.setHasFixedSize( true);
        recycler.setNestedScrollingEnabled(false);
        adapter =new AdapterUserMiner(this, listMiner);
        recycler.setAdapter(adapter);
        adapter.setOnItemClickLitener(new BaseRecyclerAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                UserPojo userPojo=listMiner.get(position);
                JumpActivityUtil.showMinerHomeActivity(mContext,userPojo.userId,userPojo.userName,userPojo.avatarUrl);
            }
            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }
    private void init() {

        userSelf=new UserPojo();
        userOwner=new UserPojo();
        listMiner=new ArrayList<UserPojo>();
        Intent intent=getIntent();
        userSelf.userName=intent.getStringExtra("userName");
        userSelf.avatarUrl=intent.getStringExtra("avatarUrl");
        userSelf.userId=intent.getLongExtra("userId",0);
        BroadcastUtil.registerReceiver1(this,receiver, ConstantString.BroadcastActions.Action_Replace_Miner_Success);
        initUserData();
        refreshData();
    }
    private void initUserData(){
        StakerUtil.reSizeTextView(txtName,userSelf.userName,480);
//        txtName.setText(userSelf.userName);
        txtUserId.setText("ID "+userSelf.userId);
        GlideUtil.getInstance().setImage(imgHeadBg,userSelf.avatarUrl);
        GlideUtil.getInstance().setImage(imgOwner,userOwner.avatarUrl,R.drawable.null_owner);
        if(userSelf.sex==1){
            imgSex.setBackgroundResource(R.drawable.icon_sex_nan);
        }else if(userSelf.sex==2){
            imgSex.setBackgroundResource(R.drawable.icon_sex_nv);
        }

        txtNewPrice.setText("身价 "+ (int)userSelf.priceNew);


        if(userSelf.speedGift>0){
            txtSpeed.setText("算力 "+userSelf.speed+"("+userSelf.speedGift+")");
        }else{
            txtSpeed.setText("算力 "+userSelf.speed);
        }



        if(userSelf.userTitle!=null&&!userSelf.userTitle.equals("")){
        txtTitle.setText(userSelf.userTitle);
            txtTitle.setVisibility(View.VISIBLE);
        }else{
            txtTitle.setVisibility(View.GONE);
        }

         if(OperateMinerTypes.Type_Replace.equals(userSelf.canAction)){
            layoutBottom.setVisibility(View.VISIBLE);
            txtBottom.setText("替换矿工");
            if(userSelf.replaceNum>0){
                txtBottom.setBackgroundResource(R.drawable.selector_color_gradient_green_55);
            }else{
                txtBottom.setBackgroundResource(R.drawable.selector_color_gray_55);
                txtBottom.setEnabled(false);
            }
        }else if(OperateMinerTypes.Type_Buy_Miner.equals(userSelf.canAction)||OperateMinerTypes.Type_Master.equals(userSelf.canAction)
                ||OperateMinerTypes.Type_No_Money.equals(userSelf.canAction)){
            layoutBottom.setVisibility(View.VISIBLE);
            txtBottom.setText("购买矿工");
            txtBottom.setBackgroundResource(R.drawable.selector_shape_gradient_blue_55);
        }else if("fire".equals(userSelf.canAction)){
            layoutBottom.setVisibility(View.VISIBLE);
            txtBottom.setText("解雇矿工");
            txtBottom.setBackgroundResource(R.drawable.selector_color_gradient_red_55);
        }else{
            layoutBottom.setVisibility(View.GONE);
        }


    }
    private void refreshData(){
        ServerUtil.getUserInfo(userSelf.userId, userSelf, userOwner, listMiner, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                initUserData();
                adapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_share:
                UmengShareUtil.share(this,userSelf.userId,userSelf.userName,(int)userSelf.priceNew,userSelf.speed);
                break;

            case R.id.txt_buy_fire:
                String sText=txtBottom.getText().toString();


                if("购买矿工".equals(sText)){
                    buyMiner();
                }else if("解雇矿工".equals(sText)){
                    fireMiner();
                }else if("替换矿工".equals(sText)){
                    //替换
                    JumpActivityUtil.showReplaceMinerActivity(this,userSelf.userId,userSelf.userName,userSelf.replaceNum);
                }else{

                }
                break;

            case R.id.img_owner:
                if(userOwner.userId<1){
                    return;
                }
                JumpActivityUtil.showMinerHomeActivity(mContext,userOwner.userId,userOwner.userName,userOwner.avatarUrl);
                break;

            case R.id.layout_suanli:
                JumpActivityUtil.showSpeedRecordActivity(this,userSelf.userId,userSelf.speed,userSelf.userName,userSelf.avatarUrl);
                break;
            case R.id.txt_send_gift:
                JumpActivityUtil.showSendGiftActivity(this,userSelf.userId,userSelf.speed,userSelf.userName,userSelf.avatarUrl);
                break;

        }
    }

    private void buyMiner(){
        new DialogBuyMiner(this,userSelf, new ResultListener() {
            @Override
            public void onSucess(Object object) {
                    userSelf.canAction="fire";
                    txtBottom.setText("解雇矿工");
                    txtBottom.setBackgroundResource(R.drawable.selector_color_gradient_red_55);
            }
        }).showDialog();
    }
    private void fireMiner(){
        new DialogFireMiner(this, userSelf.userId, new ResultListener() {
            @Override
            public void onSucess(Object object) {
                userSelf.canAction="buy";
                txtBottom.setText("购买矿工");
                txtBottom.setBackgroundResource(R.drawable.selector_shape_gradient_blue_55);
            }
        }).showDialog();
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
