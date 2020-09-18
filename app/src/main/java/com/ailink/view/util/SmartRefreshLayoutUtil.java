package com.ailink.view.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.scwang.smartrefresh.header.WaveSwipeHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


import juhuiwan.ailink.android.R;

import static com.scwang.smartrefresh.layout.SmartRefreshLayout.setDefaultRefreshFooterCreater;
import static com.scwang.smartrefresh.layout.SmartRefreshLayout.setDefaultRefreshFooterCreator;


/**
 * Created by Staker on 2017/7/13.
 * 这是一个包含下拉刷新和上拉加载更多的工具类  当作一个可以下拉刷新的ScrollView来用
 * 可以选择添加头部Header和底部Footer
 * 此外头部和底部的布局需要自己定义
 *
 *
 * 它的功能比springview更强大，默认大一些更炫酷的下拉刷新效果，具体使用可以百度或者看有道笔记
 *
 *  * 里面只能包含多种控件  比如List View  RecycleView之类的滑动控件
 *这是我自己尝试出来可以用的方法，可以上下滑动。 不嵌套NestedScrollView的暂时还没有尝试
 *  <SmartRefreshLayout>
 *
                <android.support.v4.widget.NestedScrollView>

                        <LinearLayout>
                         xml布局代码写在这里
                        <LinearLayout>
                <android.support.v4.widget.NestedScrollView>

 <SmartRefreshLayout>
 *
 *
 * sSmartRefreshLayout可以使用默认的头部和底部刷新  当然也可以自定义 继承BaseHeader或者BaseFooter就可以
 */

public class SmartRefreshLayoutUtil {

    public SmartRefreshLayout refreshLayout;

    //static 代码段可以防止内存泄露  需要的时候再打开
    static {
        //设置全局的 Header 构建器 这种方式 设置的 Header 和 Footer 的优先级是最低的
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
               layout.setPrimaryColorsId(R.color.translucent, R.color.gray_33);//全局设置主题颜色  第一个是背景的颜色，默认透明比较好，第二个是文字和箭头的颜色
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);//指定为经典 Header，默认是 贝塞尔雷达 Header
            }
        });
        //设置全局的 Footer 构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public com.scwang.smartrefresh.layout.api.RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });

    }


    public SmartRefreshLayoutUtil(View view,int id) {
        refreshLayout = (SmartRefreshLayout)view.findViewById(id);

    }

    public SmartRefreshLayoutUtil(Activity activity,int id) {
        refreshLayout = (SmartRefreshLayout)activity.findViewById(id);
    }

    public void initDefaultHeader(Activity activity){
        WaveSwipeHeader header=new WaveSwipeHeader(activity);
        header .setPrimaryColors(Color.parseColor("#df3118"),Color.parseColor("#ffffff"));
        refreshLayout.setRefreshHeader(header);//设置头部的布局
    }
    public void initDefaultFooter(Activity activity){
        refreshLayout.setRefreshFooter(new BallPulseFooter(activity).setSpinnerStyle(SpinnerStyle.Scale));;//设置底部的布局
    }

    public void  setHeaderView(RefreshHeader headerView){
        refreshLayout.setRefreshHeader(headerView);//设置头部的布局
    }
    public void  setFooterView(com.scwang.smartrefresh.layout.api.RefreshFooter footerView){
        refreshLayout.setRefreshFooter(footerView);//设置顶部的布局
    }

    public void setFinishRefresh(){
        refreshLayout.finishRefresh();//把头部或者顶部弹回来
    }
    public void setFinishLoadMore(){
        refreshLayout.finishLoadMore();//把底部加载更多的view弹回去
    }
    public void setOnRefreshLisetener(OnRefreshListener onRefreshLisetener){
        refreshLayout.setOnRefreshListener(onRefreshLisetener);
    }
    public void setOnLoadMoreLisetener(OnLoadMoreListener onLoadMoreLisetener){
        refreshLayout.setOnLoadMoreListener(onLoadMoreLisetener);
    }








//        SmartRefreshLayoutUtil smart=new SmartRefreshLayoutUtil(getActivity(),R.id.smartlayout);
//        smart.initDefaultHeader(getActivity());
//        smart.setOnRefreshLisetener(new com.scwang.smartrefresh.layout.listener.OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshlayout) {
//
//            }
//        });
//        smart.refreshLayout.setEnableLoadmore(false);
//        smart.setOnLoadMoreLisetener(new OnLoadmoreListener() {
//            @Override
//            public void onLoadmore(RefreshLayout refreshlayout) {
//
//            }
//        });

}
