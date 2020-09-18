package com.ailink.view.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ailink.adapter.base.DividerItemDecoration;
import com.liaoinstan.springview.container.BaseFooter;
import com.liaoinstan.springview.container.BaseHeader;
import com.liaoinstan.springview.widget.SpringView;

import juhuiwan.ailink.android.R;


/**
 * Created by Staker on 2017/7/13.
 * 这是一个包含下拉刷新和上拉加载更多的工具类
 * 可以选择添加头部Header和底部Footer
 * 此外头部和底部的布局需要自己定义
 *
 *  * 里面只能包含一种List View  RecycleView之类的滑动控件
 *
 *
 * 要配合布局文件 layout_sprint_view
 *
 * springView可以使用默认的头部和底部刷新  当然也可以自定义 继承BaseHeader或者BaseFooter就可以
 * 此外springView如果不设置监听器的话，拉出来头部和底部之后会自动弹回去。这种效果在微信的网页界面就有用到
 */

public class SpringViewUtil {
    public SpringView springView;;
    public RecyclerView recyclerView;
    private DividerItemDecoration divider;



    public SpringViewUtil(View view,boolean isGrid) {
        springView = (SpringView) view.findViewById(R.id.spring_view);
        springView.setType(SpringView.Type.FOLLOW);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        if(isGrid){
            GridLayoutManager layoutManager=new GridLayoutManager(view.getContext(),3);
            recyclerView.setLayoutManager(layoutManager);
        }else{
            LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            divider = new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL);//使用默认的分割线
            recyclerView.addItemDecoration(divider);
        }

    }

    public SpringViewUtil(Activity activity,boolean isGrid) {
        springView = (SpringView) activity.findViewById(R.id.spring_view);
        springView.setType(SpringView.Type.FOLLOW);

        recyclerView = (RecyclerView) activity.findViewById(R.id.recycler_view);

        if(isGrid){
            GridLayoutManager layoutManager=new GridLayoutManager(activity,3);
            recyclerView.setLayoutManager(layoutManager);
        }else{
            LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
        }
        divider = new DividerItemDecoration(activity, LinearLayoutManager.VERTICAL);//使用默认的分割线
        recyclerView.addItemDecoration(divider);
    }







    public void initDefaultHeader(Activity activity){
        springView.setHeader(new RefreshHead(activity, 0, true));//设置头部的布局
    }
    public void initDefaultFooter(Activity activity){
        springView.setFooter(new RefreshFooter(activity, 0, true));//设置顶部的布局
    }

    public void  setHeaderView(BaseHeader headerView){
        springView.setHeader(headerView);//设置头部的布局
    }
    public void  setFooterView(BaseFooter footerView){
        springView.setFooter(footerView);//设置顶部的布局
    }
    /**
     * 给RecyclerView设置适配器
     * @param adapter
     */
    public void setRecyclerViewAdapter(RecyclerView.Adapter adapter){
        recyclerView.setAdapter(adapter);
    }
    /**
     * 设置RecyclerView的高度和图片
     */
    public void setDivider(Drawable drawable, int height) {
        divider.setDivider(drawable, height);
        recyclerView.addItemDecoration(divider);
    }
    /**
     * 设置RecyclerView的高度和图片
     */
    public void setDivider(Context context, int drawableResId, int height) {
        divider.setDivider(context,drawableResId, height);
        recyclerView.addItemDecoration(divider);
    }
    public void setDividerNone() {
        divider.setDividerHeight(0);
    }
    public void setFinishRefresh(){
        springView.onFinishFreshAndLoad();//把头部或者顶部弹回来
    }
    public void setDividerHeight(int height) {
        divider.setDividerHeight(height);
    }

}
/**
 * SpringView的一些方法如下

        springView.onFinishFreshAndLoad();//把头部或者顶部弹回来


                springView.setType(SpringView.Type.FOLLOW);
                springView.setHeader(new RefreshHead(mActivity, 0, true));//设置头部的布局
                springView.setFooter(new RefreshFooter(mActivity, 0, true));//设置顶部的布局
        //设置监听
         springView.setListener(new SpringView.OnFreshListener() {
                @Override
                public void onRefresh() {

                }

                @Override
                public void onLoadmore() {

                }
        });



 */