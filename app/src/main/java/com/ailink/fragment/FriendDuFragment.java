package com.ailink.fragment;

import android.view.View;
import android.widget.TextView;

import com.ailink.adapter.AdapterFriendDu;
import com.ailink.adapter.base.BaseRecyclerAdapter;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.pojo.UserPojo;
import com.ailink.util.Logg;
import com.ailink.view.util.SpringViewUtil;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;

public class FriendDuFragment extends BaseFragment{

    SpringViewUtil springViewUtil;
    AdapterFriendDu adapter;
    ArrayList<UserPojo> list;
    int  friendDu=1;
    int page =1;//获取第几页的数据
    @Override
    public int getLayoutId() {
        return R.layout.fragment_friend_du;
    }

    @Override
    public void initViews(View view) {
        springViewUtil=new SpringViewUtil(view,true);
        list=new ArrayList<UserPojo>();
        initRecyclerView();
        refreshData();
    }
    public void setFriendDu(int friendDu){
        this.friendDu=friendDu;
    }
    private void initRecyclerView() {
        adapter=new AdapterFriendDu(getActivity(),list);
        springViewUtil.recyclerView.setAdapter(adapter);
        springViewUtil.initDefaultHeader(getActivity());
        springViewUtil.initDefaultFooter(getActivity());
//        springViewUtil.setDividerNone();ComplexViewPagerAdapter.java
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
    private void refreshData(){
        page =1;
        ServerUtil.getMinerRelationShip(friendDu,page, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                springViewUtil.setFinishRefresh();
                ArrayList<UserPojo> temp=(ArrayList<UserPojo>)object;
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
        ServerUtil.getMinerRelationShip(friendDu,page, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                springViewUtil.setFinishRefresh();
                ArrayList<UserPojo> temp=(ArrayList<UserPojo>)object;
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
