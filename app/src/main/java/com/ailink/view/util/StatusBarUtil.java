package com.ailink.view.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.ailink.activity.BaseActivity;
import com.ailink.util.Logg;
import com.ailink.view.StatusBarView;

import java.lang.reflect.Field;

import juhuiwan.ailink.android.R;


/**
 * Created by Staker on 2017/7/6.
 * 这是一个设置顶部状态栏颜色的工具类
 */

public class StatusBarUtil {

    /**
     * 设置全透明沉浸式状态栏,即去掉顶部的状态栏，activity里面的内容会全部顶上去
     *
     * 这个方法一定一定一定  要写在setContentView之前，否则无效
     */
    public static void setStatusBarFullScreem(BaseActivity activity) {
        if(activity==null){
            return;
        }
        translucentStatusBar(activity);
    }

    /**
     * 设置顶部状态栏字体的颜色，其它不做修改
     */
    public static void setStatusBarTextColor(BaseActivity activity, int color) {
        if(activity==null){
            return;
        }
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    /**
     * 设置顶部状态栏的颜色，状态栏的高度保留，只是设置它的颜色
     */
    public static void setStatusBarColor(BaseActivity activity, int color) {
        if(activity==null){
            return;
        }
        setStatusViewColor(activity,color);
    }
    //清除沉浸式状态栏
    public static  void clearFullScreem(BaseActivity activity){
        if(activity==null){
            return;
        }
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //注意要清除 FLAG_TRANSLUCENT_STATUS flag
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }




    /**
     * 设置状态栏View颜色
     * XML中根部局建议不要用RelativeLayout,否则会多出一层
     *
     * 前往要注意，这个方法一定要配合在Activity的根布局加上android:fitsSystemWindows="true"属性，否则顶部的view会没有位置，而发生重叠的现象
     */
    private static void setStatusViewColor(Activity activity, @ColorInt int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT&&Build.VERSION.SDK_INT< Build.VERSION_CODES.LOLLIPOP) { //4.4到5.0之间
            //第1步先去掉顶部的状态栏
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            //第2部 获取windowphone下的decorView
            ViewGroup windowView = (ViewGroup) activity.getWindow().getDecorView();
            int  count = windowView.getChildCount();

            //第3部 判断是否已经添加了statusBarView,因为在一个activity中可能有多个fragment切换，那么如果之前添加过的话，这里只要重新设置颜色即可
            if (count > 0 && windowView.getChildAt(count - 1) instanceof StatusBarView) {//假设已经添加过，则直接用就可以
                windowView.getChildAt(count - 1).setBackgroundColor(color);
            } else {
                //新建一个和状态栏高宽的StatusBarView
                StatusBarView statusView = createStatusBarView(activity, color);
                windowView.addView(statusView);
            }

        }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            //5.0以上设置
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //注意要清除 FLAG_TRANSLUCENT_STATUS flag  因为5.0以上就可以直接利用代码设置顶部状态栏的颜色，无需设置全透明状态栏，然后再添加高度
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
        }

    }



    private static StatusBarView createStatusBarView(Activity activity, int color) {
        // 绘制一个和状态栏一样高的矩形
        StatusBarView statusBarView = new StatusBarView(activity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(color);
        return statusBarView;
    }


    private static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

















    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void translucentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            View decorView = window.getDecorView();
            //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT); //设置navigationbar颜色为透明

            if (Build.VERSION.SDK_INT >= 24) {//这里是对7.0的额外设置
                try {
                    Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");//获取DecorView实例
                    Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");//获取特定的成员变量
                    field.setAccessible(true);//设置对此属性的可访问性
                    field.setInt(activity.getWindow().getDecorView(), Color.TRANSPARENT);  ////修改属性值 改为透明
                } catch (Exception e) {
                }
            }
        } else {
            //这个里面是对4.4到5.0之间的的手机的设置
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }
    }



























}
