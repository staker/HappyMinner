package com.ailink.view.util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gc.materialdesign.views.LayoutRipple;

import juhuiwan.ailink.android.R;


/**
 * Created by Staker on 2017/6/23.
 * 这个类主要是用来控制底部导航
 */

public class BottomBarLayoutUtil {
    public static int unSelectedTextColor = Color.rgb(102, 102, 102);//为选中底部文字颜色
    public static int selectedTextColor = Color.rgb(3, 74, 254);//选中菜单  底部文字的颜色

    LayoutRipple layout01;
    LayoutRipple layout02;
    LayoutRipple layout03;
    LayoutRipple layout04;
    TextView txt01,txt02,txt03,txt04;
    ImageView img01,img02,img03,img04;

    TextView txtTips01,txtTips02,txtTips03,txtTips04;





    private Activity activity;
    private IOnClickIndex iOnClickIndex;

    /**
     * 设置点击导航之后返回对应的index数字
     */
    public interface IOnClickIndex {
        public abstract void onClick(int index);
    }







    public BottomBarLayoutUtil(Activity activity,IOnClickIndex iOnClickIndex) {
        this.activity = activity;
        this.iOnClickIndex=iOnClickIndex;
        findViewById();
        changeBackgroundBySelected(1);
    }


    /**
     * 这个方法可以用来给外部调用，比如需要设置当前选中哪个底部tab，就是说可以手动来切换选中的状态
     * @param clickedTab
     */
    public void setClickedTab(int clickedTab){
        switch (clickedTab){
            case 1:
                if(iOnClickIndex!=null){
                    changeBackgroundBySelected(1);iOnClickIndex.onClick(1);
                }
                break;
            case 2:
                if(iOnClickIndex!=null){
                    changeBackgroundBySelected(2);iOnClickIndex.onClick(2);
                }
                break;
            case 3:
                if(iOnClickIndex!=null){
                    changeBackgroundBySelected(3);iOnClickIndex.onClick(3);
                }
                break;
            case 4:
                if(iOnClickIndex!=null){
                    changeBackgroundBySelected(4);iOnClickIndex.onClick(4);
                }
                break;
        }
    }
    private void findViewById() {
        img01 = (ImageView) activity.findViewById(R.id.img_01);
        img02 = (ImageView) activity.findViewById(R.id.img_02);
        img03 = (ImageView) activity.findViewById(R.id.img_03);
        img04 = (ImageView) activity.findViewById(R.id.img_04);


        txt01 = (TextView) activity.findViewById(R.id.txt_01);
        txt02 = (TextView) activity.findViewById(R.id.txt_02);
        txt03 = (TextView) activity.findViewById(R.id.txt_03);
        txt04 = (TextView) activity.findViewById(R.id.txt_04);


        txtTips01= (TextView) activity.findViewById(R.id.txt_tips01);
        txtTips02= (TextView) activity.findViewById(R.id.txt_tips02);
        txtTips03= (TextView) activity.findViewById(R.id.txt_tips03);
        txtTips04= (TextView) activity.findViewById(R.id.txt_tips04);

        activity.findViewById(R.id.layout_01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(iOnClickIndex!=null){
                    changeBackgroundBySelected(1);iOnClickIndex.onClick(1);
                }
            }
        });
        activity.findViewById(R.id.layout_02).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(iOnClickIndex!=null){
                    changeBackgroundBySelected(2);
                    iOnClickIndex.onClick(2);
                }
            }
        });
        activity.findViewById(R.id.layout_03).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(iOnClickIndex!=null){
                    changeBackgroundBySelected(3);
                    iOnClickIndex.onClick(3);
                }
            }
        });
        activity.findViewById(R.id.layout_04).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(iOnClickIndex!=null){
                    changeBackgroundBySelected(4);
                    iOnClickIndex.onClick(4);
                }
            }
        });
    }


    public void setRedTips(int tips01,int tips02,int tips03,int tips04){
        if(tips01<=0){
            txtTips01.setVisibility(View.GONE);
        }else if(tips01==1){
            txtTips01.setVisibility(View.VISIBLE);
            txtTips01.setText("");
        }else{
            txtTips01.setText((tips01-1)+"");
        }


        if(tips02<=0){
            txtTips02.setVisibility(View.GONE);
        }else if(tips02==1){
            txtTips02.setVisibility(View.VISIBLE);
            txtTips02.setText("");
        }else{
            txtTips02.setText((tips02-1)+"");
        }

        if(tips03<=0){
            txtTips03.setVisibility(View.GONE);
        }else if(tips03==1){
            txtTips03.setVisibility(View.VISIBLE);
            txtTips03.setText("");
        }else{
            txtTips03.setText((tips03-1)+"");
        }

        if(tips04<=0){
            txtTips04.setVisibility(View.GONE);
        }else if(tips04==1){
            txtTips04.setVisibility(View.VISIBLE);
            txtTips04.setText("");
        }else{
            txtTips04.setText((tips04-1)+"");
        }

    }

        /**
         * 改变选中之后的颜色背景
         *
         * @param index
         *            菜单的下标值
         */
        private void changeBackgroundBySelected(int index) {
            switch (index) {
                case 1:
                    img01.setBackgroundResource(R.mipmap.home_bottombar01_pressed);
                    img02.setBackgroundResource(R.mipmap.home_bottombar02_unpressed);
                    img03.setBackgroundResource(R.mipmap.home_bottombar03_unpressed);
                    img04.setBackgroundResource(R.mipmap.home_bottombar04_unpressed);

                    txt01.setTextColor(selectedTextColor);
                    txt02.setTextColor(unSelectedTextColor);
                    txt03.setTextColor(unSelectedTextColor);
                    txt04.setTextColor(unSelectedTextColor);
                    break;
                case 2:

                    img01.setBackgroundResource(R.mipmap.home_bottombar01_unpressed);
                    img02.setBackgroundResource(R.mipmap.home_bottombar02_pressed);
                    img03.setBackgroundResource(R.mipmap.home_bottombar03_unpressed);
                    img04.setBackgroundResource(R.mipmap.home_bottombar04_unpressed);

                    txt01.setTextColor(unSelectedTextColor);
                    txt02.setTextColor(selectedTextColor);
                    txt03.setTextColor(unSelectedTextColor);
                    txt04.setTextColor(unSelectedTextColor);
                    break;
                case 3:

                    img01.setBackgroundResource(R.mipmap.home_bottombar01_unpressed);
                    img02.setBackgroundResource(R.mipmap.home_bottombar02_unpressed);
                    img03.setBackgroundResource(R.mipmap.home_bottombar03_pressed);
                    img04.setBackgroundResource(R.mipmap.home_bottombar04_unpressed);
//
                    txt01.setTextColor(unSelectedTextColor);
                    txt02.setTextColor(unSelectedTextColor);
                    txt03.setTextColor(selectedTextColor);
                    txt04.setTextColor(unSelectedTextColor);
                    break;
                case 4:
                    img01.setBackgroundResource(R.mipmap.home_bottombar01_unpressed);
                    img02.setBackgroundResource(R.mipmap.home_bottombar02_unpressed);
                    img03.setBackgroundResource(R.mipmap.home_bottombar03_unpressed);
                    img04.setBackgroundResource(R.mipmap.home_bottombar04_pressed);

                    txt01.setTextColor(unSelectedTextColor);
                    txt02.setTextColor(unSelectedTextColor);
                    txt03.setTextColor(unSelectedTextColor);
                    txt04.setTextColor(selectedTextColor);
                    break;

                default:
                    break;
            }
        }


    }
