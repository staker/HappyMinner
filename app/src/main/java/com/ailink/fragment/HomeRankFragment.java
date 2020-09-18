package com.ailink.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailink.adapter.AdapterHomeRank;
import com.ailink.adapter.AdapterPriceRank;
import com.ailink.adapter.base.BaseRecyclerAdapter;
import com.ailink.adapter.base.DividerItemDecoration;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.pojo.UserPojo;
import com.ailink.util.Logg;
import com.ailink.view.RoundImageView;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;
import libs.glide.GlideUtil;


/**
 *这是交易界面，界面顶部的  交易所，下面对应切换为此交易所的产品
 */
public class HomeRankFragment extends BaseFragment{
    private RecyclerView recycler;
    private AdapterHomeRank adapter;
    private TextView txtName01,txtName02,txtName03;
    private TextView txtPrice01,txtPrice02,txtPrice03;
    private TextView txtDesc01,txtDesc02,txtDesc03;
    private TextView txtNoData;
    private LinearLayout layoutTop3;

    private RoundImageView imgHead01,imgHead02,imgHead03;
    private UserPojo user01,user02,user03;
    private ImageView imgHomeBang;



    ArrayList<UserPojo> list=new ArrayList<UserPojo>();
    boolean isRequesting=false;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_bang;
    }
    @Override
    public void initViews(View view) {
        recycler = findViewById(R.id.recycler_home_rank);
        imgHomeBang=(ImageView) findViewById(R.id.img_home_bang);
        txtName01=findViewById(R.id.txt_name01);
        txtName02=findViewById(R.id.txt_name02);
        txtName03=findViewById(R.id.txt_name03);
        txtPrice01=findViewById(R.id.txt_price01);
        txtPrice02=findViewById(R.id.txt_price02);
        txtPrice03=findViewById(R.id.txt_price03);
        txtNoData=findViewById(R.id.txt_no_data);
        txtDesc01=findViewById(R.id.txt_desc01);
        txtDesc02=findViewById(R.id.txt_desc02);
        txtDesc03=findViewById(R.id.txt_desc03);
        layoutTop3=findViewById(R.id.layout_top3);
        imgHead01=findViewById(R.id.img_head01);
        imgHead02=findViewById(R.id.img_head02);
        imgHead03=findViewById(R.id.img_head03);
        imgHead01.setOnClickListener(this);
        imgHead02.setOnClickListener(this);
        imgHead03.setOnClickListener(this);
        initRecyclerPriceRank();
        refreshData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isRequesting){
            return;
        }
        refreshData();
    }

    private void initRecyclerPriceRank() {
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration  divider = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);//使用默认的分割线
        recycler.addItemDecoration(divider);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setHasFixedSize( true);
        recycler.setNestedScrollingEnabled(false);
        adapter =new AdapterHomeRank(getContext(), list);
        recycler.setAdapter(adapter);
        adapter.setOnItemClickLitener(new BaseRecyclerAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                UserPojo userPojo=list.get(position);
                JumpActivityUtil.showMinerHomeActivity(getActivity(),userPojo.userId,userPojo.userName,userPojo.avatarUrl);
            }
            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }
    public void refreshData(){
        isRequesting=true;
        ServerUtil.getHomeRank(imgHomeBang,new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                isRequesting=false;
                ArrayList<UserPojo> temp=(ArrayList<UserPojo>)object;
                if(temp!=null){
                    int size=temp.size();
                    if(size==0){
                        txtNoData.setVisibility(View.VISIBLE);
                        layoutTop3.setVisibility(View.GONE);
                        return;
                    }
                    txtNoData.setVisibility(View.GONE);
                    layoutTop3.setVisibility(View.VISIBLE);

                    if(size<=3){
                        for(int i=0;i<size;i++){
                            UserPojo userPojo=temp.get(i);
                            switch (i){
                                case 0:
                                    initPriceRank01(userPojo);
                                    user01=userPojo;
                                    break;
                                case 1:
                                    initPriceRank02(userPojo);
                                    user02=userPojo;
                                    break;
                                case 2:
                                    initPriceRank03(userPojo);
                                    user03=userPojo;
                                    break;
                            }
                        }
                    }else{
                        for(int i=0;i<3;i++){
                            UserPojo userPojo=temp.get(i);
                            switch (i){
                                case 0:
                                    initPriceRank01(userPojo);
                                    user01=userPojo;
                                    break;
                                case 1:
                                    initPriceRank02(userPojo);
                                    user02=userPojo;
                                    break;
                                case 2:
                                    initPriceRank03(userPojo);
                                    user03=userPojo;
                                    break;
                            }
                        }
                        temp.remove(0);
                        temp.remove(0);
                        temp.remove(0);
                        list.clear();
                        list.addAll(temp);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onDataError(String dataError) {
                super.onDataError(dataError);
                isRequesting=false;
            }
        });
    }



    private  void initPriceRank01(UserPojo userPojo){
        txtName01.setText(userPojo.userName);
        txtPrice01.setText(""+(int)userPojo.homeBangValue);
        txtDesc01.setText(userPojo.homeBangText);
        GlideUtil.getInstance().setImage(imgHead01,userPojo.avatarUrl);
    }

    private  void initPriceRank02(UserPojo userPojo){
        txtName02.setText(userPojo.userName);
        txtPrice02.setText(""+(int)userPojo.homeBangValue);
        txtDesc02.setText(userPojo.homeBangText);
        GlideUtil.getInstance().setImage(imgHead02,userPojo.avatarUrl);
    }

    private  void initPriceRank03(UserPojo userPojo){
        txtName03.setText(userPojo.userName);
        txtPrice03.setText(""+(int)userPojo.homeBangValue);
        txtDesc03.setText(userPojo.homeBangText);
        GlideUtil.getInstance().setImage(imgHead03,userPojo.avatarUrl);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.img_head01:
                if(user01!=null){
                    JumpActivityUtil.showMinerHomeActivity(getActivity(),user01.userId,user01.userName,user01.avatarUrl);
                }
                break;
            case R.id.img_head02:
                if(user02!=null){
                    JumpActivityUtil.showMinerHomeActivity(getActivity(),user02.userId,user02.userName,user02.avatarUrl);
                }
                break;
            case R.id.img_head03:
                if(user03!=null){
                    JumpActivityUtil.showMinerHomeActivity(getActivity(),user03.userId,user03.userName,user03.avatarUrl);
                }
                break;

        }
    }
}
