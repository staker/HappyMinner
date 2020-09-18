package com.ailink.view.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.ailink.adapter.base.DividerItemDecoration;

import juhuiwan.ailink.android.R;


/**
 * Created by Staker on 2017/7/10.
 * 这是一个下拉刷新的工具类,要配合布局文件 layout_swipe_refresh_layout
 * 这个布局文件是需要直接写在Activity里面的
 * 里面只能包含一种List View  RecycleView之类的滑动控件
 */

public class SwipeRefreshUtil {
    public SwipeRefreshLayout refreshLayout;
    public RecyclerView recyclerView;
    private DividerItemDecoration divider;


    public SwipeRefreshUtil(View view) {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        refreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);//设置圆圈的背景颜色
        refreshLayout.setColorSchemeResources(R.color.black,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);//第一个颜色是 下拉时候的颜色。后面三个是转动时候切换的颜色

        refreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, view.getContext().getResources()
                        .getDisplayMetrics()));


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        divider = new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL);//使用默认的分割线
        recyclerView.addItemDecoration(divider);

    }

    public SwipeRefreshUtil(Activity activity) {
        refreshLayout = (SwipeRefreshLayout) activity.findViewById(R.id.swipe_refresh_layout);
        refreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);//设置圆圈的背景颜色
        refreshLayout.setColorSchemeResources(R.color.black,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);//第一个颜色是 下拉时候的颜色。后面三个是转动时候切换的颜色

        refreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, activity.getResources()
                        .getDisplayMetrics()));


        recyclerView = (RecyclerView) activity.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        divider = new DividerItemDecoration(activity, LinearLayoutManager.VERTICAL);//使用默认的分割线
        recyclerView.addItemDecoration(divider);
    }

    /**
     * 给RecyclerView设置适配器
     * @param adapter
     */
    public void setRecyclerViewAdapter(RecyclerView.Adapter adapter){
        recyclerView.setAdapter(adapter);
    }

    public void setSwipeBackground(int color) {
        refreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);//设置圆圈的背景颜色
    }

    /**
     * 设置RecyclerView的高度和图片
     */
    public void setDivider(Drawable drawable, int height) {
        divider.setDivider(drawable, height);
        recyclerView.addItemDecoration(divider);
    }
    public void setDividerHeight(int height) {
        divider.setDividerHeight(height);
    }
    /**
     * 设置RecyclerView的高度和图片
     */
    public void setDivider(Context context,int drawableResId, int height) {
        divider.setDivider(context,drawableResId, height);
        recyclerView.addItemDecoration(divider);
    }
    public void setSwipeColorSchemeResources(int color01, int color02, int color03, int color04) {
        refreshLayout.setColorSchemeResources(color01, color02, color03, color04);
        //第一个颜色是 下拉时候的颜色。后面三个是转动时候切换的颜色
    }

    public void setSwipeProgressViewOffset(boolean scale, int start, int end) {
        refreshLayout.setProgressViewOffset(scale, start, end);
    }

//    private Handler handler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if(mOnRefreshListener!=null){
//                mOnRefreshListener.onComplete();
//                refreshLayout.setRefreshing(false);
//            }
//        }
//    };
//    private OnRefreshListener mOnRefreshListener;
//    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
//        if(onRefreshListener==null){
//            return;
//        }
//        mOnRefreshListener=onRefreshListener;
//        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mOnRefreshListener.onRefreshing();
//                handler.sendEmptyMessage(0);
//            }
//        });
//    }


    //        refreshLayout=(SwipeRefreshLayout)this.findViewById(R.id.swiperefreshlayout);
//        refreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);//设置圆圈的背景颜色
//
//        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
//                android.R.color.holo_red_light,android.R.color.holo_orange_light,
//                android.R.color.holo_green_light);//第一个颜色是 下拉时候的颜色。后面三个是转动时候切换的颜色
//
//        refreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
//                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
//                        .getDisplayMetrics()));
//

}
