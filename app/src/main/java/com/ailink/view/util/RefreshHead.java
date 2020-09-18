package com.ailink.view.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liaoinstan.springview.container.BaseHeader;

/**
 * 这个是顶部的刷新布局，专门给SpringView使用.
 */
public class RefreshHead extends BaseHeader {
    private Context context;
    private int rotationSrc;
    private int arrowSrc;
    private int logoSrc;
    private boolean isShowText;

    private final int ROTATE_ANIM_DURATION = 180;
    private RotateAnimation mRotateUpAnim;
    private RotateAnimation mRotateDownAnim;

    private TextView headerTitle;
    private ImageView headerArrow;
    private ImageView headerLogo;
    private ProgressBar headerProgressbar;
    private View frame;

    private String startRefreshText;
    private String refreshIngText;
    private String dropDownIngText;

    public RefreshHead(Context context){
        this(context, 0, com.liaoinstan.springview.R.drawable.arrow,0,false);
    }

    public RefreshHead(Context context,boolean isShowText){
        this(context, 0, com.liaoinstan.springview.R.drawable.arrow,0,isShowText);
    }

    public RefreshHead(Context context,int logoSrc){
        this(context, 0, com.liaoinstan.springview.R.drawable.arrow,logoSrc,false);
    }

    public RefreshHead(Context context,int logoSrc,boolean isShowText){
        this(context, 0, com.liaoinstan.springview.R.drawable.arrow,logoSrc,isShowText);
    }

    public RefreshHead(Context context,int rotationSrc,int arrowSrc,int logoSrc,boolean isShowText){
        this.context = context;
        this.rotationSrc = rotationSrc;
        this.arrowSrc = arrowSrc;
        this.logoSrc = logoSrc;
        this.isShowText = isShowText;
        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
        startRefreshText = "下拉刷新";
        refreshIngText = "正在刷新";
        dropDownIngText = "松开刷新";
    }

    @Override
    public View getView(LayoutInflater inflater,ViewGroup viewGroup) {
        View view = inflater.inflate(com.liaoinstan.springview.R.layout.ali_header, viewGroup, true);
        headerTitle = (TextView) view.findViewById(com.liaoinstan.springview.R.id.ali_header_text);
        headerArrow = (ImageView) view.findViewById(com.liaoinstan.springview.R.id.ali_header_arrow);
        headerLogo = (ImageView) view.findViewById(com.liaoinstan.springview.R.id.ali_header_logo);
        headerProgressbar = (ProgressBar) view.findViewById(com.liaoinstan.springview.R.id.ali_header_progressbar);
        frame = view.findViewById(com.liaoinstan.springview.R.id.ali_frame);
        if(logoSrc!=0) headerLogo.setImageResource(logoSrc);
        if(!isShowText) headerTitle.setVisibility(View.GONE);
        if(rotationSrc!=0) headerProgressbar.setIndeterminateDrawable(ContextCompat.getDrawable(context, rotationSrc));
        headerArrow.setImageResource(arrowSrc);
        return view;
    }

    @Override
    public int getDragSpringHeight(View rootView) {
        return frame.getMeasuredHeight();
    }

    @Override
    public int getDragLimitHeight(View rootView) {
        return frame.getMeasuredHeight();
    }

    @Override
    public void onPreDrag(View rootView) {
    }

    /**
     * 这是下拉过程中调用的方法，可以在这个里面根据下拉的位置。设置一些动画。
     * 总之 只有头部在下拉，这个方法就会一直触发
     * @param rootView
     * @param dy  这是下拉的距离
     */
    @Override
    public void onDropAnim(View rootView, int dy) {
    }

    /**
     *
     * @param rootView
     * @param upORdown  true代表已经到了临界点可以刷新了，而false代表从临界点又回到原来的位置（即还没达到刷新的点）
     */
    @Override
    public void onLimitDes(View rootView, boolean upORdown) {
        //大小临界点会调用这个方法
        if (!upORdown){
            headerTitle.setText(dropDownIngText);
            if (headerArrow.getVisibility()==View.VISIBLE)
                headerArrow.startAnimation(mRotateUpAnim);
        }
        else {

            headerTitle.setText(startRefreshText);
            if (headerArrow.getVisibility()==View.VISIBLE)
                headerArrow.startAnimation(mRotateDownAnim);
        }
    }

    /**
     * 正在刷新会调用这个方法
     * 即松手让头部弹回去了
     */
    @Override
    public void onStartAnim() {
        headerTitle.setText(refreshIngText);
        headerArrow.setVisibility(View.INVISIBLE);
        headerArrow.clearAnimation();
        headerProgressbar.setVisibility(View.VISIBLE);
    }

    /**
     * 刷新结束，会调用这个方法。可以在里面回复一些初始设置
     */
    @Override
    public void onFinishAnim() {
        headerTitle.setText(startRefreshText);
        headerArrow.setVisibility(View.VISIBLE);
        headerProgressbar.setVisibility(View.INVISIBLE);
    }

    public void setRefreshIngText(String refreshIngText) {
        this.refreshIngText = refreshIngText;
    }

    public void setStartRefreshText(String startRefreshText) {
        this.startRefreshText = startRefreshText;
    }

    public void setDropDownIngText(String dropDownIngText) {
        this.dropDownIngText = dropDownIngText;
    }
}