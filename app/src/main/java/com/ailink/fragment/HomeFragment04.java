package com.ailink.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ailink.activity.BaseActivity;
import com.ailink.adapter.AdapterFunction;
import com.ailink.db.ApplicationData;
import com.ailink.db.Configuration;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.i.ResultListener;
import com.ailink.logic.DialogSelectTitle;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.pojo.UserPojo;
import com.ailink.util.Logg;
import com.ailink.view.RoundImageView;
import com.ailink.view.util.StatusBarUtil;

import juhuiwan.ailink.android.R;
import libs.glide.GlideUtil;


public class HomeFragment04 extends BaseFragment {
    private android.widget.LinearLayout layoutChangeTitle;
    private TextView txtUserTitle;
    RecyclerView recyclerFunction;
    UserPojo userPojo;
    AdapterFunction adapterFunction;

    RoundImageView imgHeader;
    ImageView imgSex;
    TextView txtName, txtNewPrice, txtSpeed, txtId;
    boolean isRequesting = false;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home04;
    }

    @Override
    public void initViews(View view) {
        StatusBarUtil.clearFullScreem((BaseActivity) getActivity());
        recyclerFunction = findViewById(R.id.recycler_function);
        imgHeader = findViewById(R.id.img_head);
        imgHeader.setOnClickListener(this);
        txtName = findViewById(R.id.txt_name);
        txtNewPrice = findViewById(R.id.txt_shenjia);
        txtSpeed = findViewById(R.id.txt_suanli);
        txtSpeed.setOnClickListener(this);
        imgSex = findViewById(R.id.img_sex);
        txtUserTitle = findViewById(R.id.txt_user_title);
        layoutChangeTitle = findViewById(R.id.layout_change_title);
        layoutChangeTitle.setOnClickListener(this);
        txtId = findViewById(R.id.txt_id);
        initIdsOnClickLinsener(R.id.layout_speed,R.id.txt_suanli,R.id.layout_edit);
        initRecyclerFunction();
        init();
    }

    @Override
    public void onResumeSelf() {
        super.onResumeSelf();
        StatusBarUtil.clearFullScreem((BaseActivity) getActivity());

        Logg.e("onResumeSelf home03 准备请求数据");
        if (isRequesting) {
            return;
        }
        Logg.e("onResumeSelf home03 开始请求数据");
        refreshData();
    }

    private void initRecyclerFunction() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerFunction.setLayoutManager(layoutManager);

        adapterFunction=new AdapterFunction(getActivity());
        recyclerFunction.setAdapter(adapterFunction);
    }

    private void init() {
        userPojo = Configuration.getInstance(getActivity()).getUserPojo();
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
                JumpActivityUtil.showMeSettingActivity(getActivity(),false);
                break;
            case R.id.layout_speed:
            case R.id.txt_suanli:
                JumpActivityUtil.showSpeedRecordActivity(getActivity(),userPojo.userId,userPojo.speed,userPojo.userName,userPojo.avatarUrl);
                break;
            case R.id.img_head:
                if (userPojo == null) {
                    return;
                }
                JumpActivityUtil.showMinerHomeActivity(getActivity(), 0, userPojo.userName, userPojo.avatarUrl);
                break;
            case R.id.layout_change_title:
                new DialogSelectTitle(getActivity(), new ResultListener() {
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
