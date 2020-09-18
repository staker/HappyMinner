package com.ailink.fragment;

import android.view.View;
import android.widget.TextView;

import com.ailink.adapter.AdapterFriendDu;
import com.ailink.adapter.AdapterRecommend;
import com.ailink.adapter.base.BaseRecyclerAdapter;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.i.ResultListener;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.pojo.UserPojo;
import com.ailink.util.Logg;
import com.ailink.view.util.SpringViewUtil;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;


/**
 *这是推荐矿工的界面4.23
 */
public class RecommendFragment extends BaseFragment{


    public static String recommendErrorMsg;

    private TextView txtState;
    ArrayList<UserPojo> list;
    SpringViewUtil springViewUtil;
    AdapterRecommend adapter;
    int page =1;//获取第几页的数据
    @Override
    public int getLayoutId() {
        return R.layout.fragment_recommend;
    }
    @Override
    public void initViews(View view) {
        txtState=findViewById(R.id.txt_state);
        springViewUtil=new SpringViewUtil(view,false);
        list=new ArrayList<UserPojo>();
        initRecyclerView();
        refreshData();

    }
    private void initRecyclerView() {
        adapter=new AdapterRecommend(getActivity(),list);
        springViewUtil.recyclerView.setAdapter(adapter);
        springViewUtil.initDefaultHeader(getActivity());
//        springViewUtil.initDefaultFooter(getActivity());
        springViewUtil.springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
            @Override
            public void onLoadmore() {
//                loadMoreData();
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

    @Override
    public void onResumeSelf() {
        super.onResumeSelf();
        refreshData();
    }

    private void refreshData(){
        page =1;
        ServerUtil.getRecommendMiner(page, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                springViewUtil.setFinishRefresh();
                ArrayList<UserPojo> temp=(ArrayList<UserPojo>)object;
                if(temp!=null){
                    if(temp.size()==0){
                        txtState.setVisibility(View.VISIBLE);
                        txtState.setText(recommendErrorMsg==null?"":recommendErrorMsg);
                        Logg.e("rmmm="+recommendErrorMsg);
                    }else{
                        txtState.setVisibility(View.GONE);
                    }
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
        ServerUtil.getRecommendMiner(page, new HttpObjectListener() {
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
