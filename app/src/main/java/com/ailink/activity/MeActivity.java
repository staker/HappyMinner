package com.ailink.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailink.adapter.AdapterFunction;
import com.ailink.db.Configuration;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.i.ResultListener;
import com.ailink.logic.DialogSelectTitle;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.pojo.UserPojo;
import com.ailink.util.Logg;
import com.ailink.util.PhoneDeviceUtil;
import com.ailink.view.RoundImageView;
import com.ailink.view.util.TopBarLayoutUtil;

import juhuiwan.ailink.android.R;
import libs.glide.GlideUtil;


/**
 *这是app的引导界面，只会在第一次进来app的时候展示，其他的时候不显示
 */
public class MeActivity extends BaseActivity {
    private android.widget.LinearLayout layoutChangeTitle;
    private TextView txtUserTitle;
    RecyclerView recyclerFunction;
    UserPojo userPojo;
    AdapterFunction adapterFunction;

    RoundImageView imgHeader;
    ImageView imgSex;
    TextView txtName, txtNewPrice, txtSpeed, txtId;
    boolean isRequesting = false;
    TopBarLayoutUtil topBar;
    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_me);
        recyclerFunction = (RecyclerView)findViewById(R.id.recycler_function);
        imgHeader = (RoundImageView)findViewById(R.id.img_head);
        imgHeader.setOnClickListener(this);
        txtName = (TextView)findViewById(R.id.txt_name);
        txtNewPrice = (TextView)findViewById(R.id.txt_shenjia);
        txtSpeed =(TextView) findViewById(R.id.txt_suanli);
        txtSpeed.setOnClickListener(this);
        imgSex = (ImageView)findViewById(R.id.img_sex);
        txtUserTitle = (TextView)findViewById(R.id.txt_user_title);
        layoutChangeTitle = (LinearLayout)findViewById(R.id.layout_change_title);
        layoutChangeTitle.setOnClickListener(this);
        txtId = (TextView)findViewById(R.id.txt_id);
        initIdsClickLinstener(R.id.layout_speed,R.id.txt_suanli,R.id.layout_edit);
        initRecyclerFunction();

        initTopBar();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRequesting) {
            return;
        }
        refreshData();
    }

    private void initTopBar() {
        topBar = new TopBarLayoutUtil(this);
        topBar.seBottomLineColor(Color.argb(255, 234, 234, 234));
        topBar.setTitle("我");
        topBar.setBackListener(new TopBarLayoutUtil.IOnClick() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }

    private void initRecyclerFunction() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerFunction.setLayoutManager(layoutManager);

        adapterFunction=new AdapterFunction(mContext);
        recyclerFunction.setAdapter(adapterFunction);
    }

    private void init() {
        userPojo = Configuration.getInstance(mContext).getUserPojo();
        initUser();
        refreshData();
    }

    private void initUser() {
        if (userPojo == null) {
            return;
        }
        GlideUtil.getInstance().setImage(imgHeader, userPojo.avatarUrl);
        txtName.setText(userPojo.userName + "");
        if (userPojo.sex == 1) {
            imgSex.setBackgroundResource(R.drawable.icon_sex_nan);
        } else if (userPojo.sex == 2) {
            imgSex.setBackgroundResource(R.drawable.icon_sex_nv);
        }
        txtId.setText("ID " + userPojo.userId);
        txtNewPrice.setText("身价 " + (int)userPojo.priceNew);


        if(userPojo.speedGift!=0){
            txtSpeed.setText("算力 " + userPojo.speed+"("+userPojo.speedGift+")");
        }else{
            txtSpeed.setText("算力 " + userPojo.speed);
        }
        initTitle();
    }

    private void initTitle() {
        if (userPojo == null) {
            return;
        }
        if (!"".equals(userPojo.userTitle) && userPojo.userTitle != null) {
            txtUserTitle.setVisibility(View.VISIBLE);
            layoutChangeTitle.setVisibility(View.VISIBLE);
            txtUserTitle.setText(userPojo.userTitle);
        } else {
            txtUserTitle.setVisibility(View.INVISIBLE);
            layoutChangeTitle.setVisibility(View.INVISIBLE);
        }
    }

    private void refreshData() {
        isRequesting = true;
        ServerUtil.getMyInfo(new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                UserPojo temp = (UserPojo) object;
                if (temp != null) {
                    userPojo = temp;
                    initUser();
                }
                adapterFunction.notifyDataSetChanged();
                isRequesting = false;
            }
        });


    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.layout_edit:
                JumpActivityUtil.showMeSettingActivity(mContext,false);
                break;
            case R.id.layout_speed:
            case R.id.txt_suanli:
                JumpActivityUtil.showSpeedRecordActivity(mContext,userPojo.userId,userPojo.speed,userPojo.userName,userPojo.avatarUrl);
                break;
            case R.id.img_head:
                if (userPojo == null) {
                    return;
                }
                JumpActivityUtil.showMinerHomeActivity(mContext, 0, userPojo.userName, userPojo.avatarUrl);
                break;
            case R.id.layout_change_title:
                new DialogSelectTitle(mContext, new ResultListener() {
                    @Override
                    public void onSucess(Object object) {
                        String title = (String) object;
                        if (userPojo != null) {
                            userPojo.userTitle = title;
                        }
                        initTitle();
                    }
                }).showDialog();
                break;

        }
    }

}
