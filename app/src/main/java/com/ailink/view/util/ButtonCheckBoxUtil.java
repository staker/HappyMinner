package com.ailink.view.util;


import android.content.Context;
import android.view.View;
import android.widget.Button;

import juhuiwan.ailink.android.R;

/**
 * 这是一个工具类，把button当作checkbox用的的工具类，里面会有很多相应方法，让设置图片之类的变得简单，而且通用
 * 2018.05.10
 */
public class ButtonCheckBoxUtil {
    private Context context;
    Button button;
    CheckLisetener checkLisetener;
    int checkedResId = R.drawable.icon_checkbox_yes;
    int unCheckedResId=R.drawable.icon_checkbox_no;
    boolean isChecked=false;
    public interface CheckLisetener{
        public void isCheck(boolean isChecked);
    }
    public ButtonCheckBoxUtil(Button button,int checkedResId,int unCheckedResId){
        this.button=button;
        setBackgroundResource(checkedResId,unCheckedResId);
        if(isChecked){
            button.setBackgroundResource(checkedResId);
        }else{
            button.setBackgroundResource(unCheckedResId);
        }
        setOnClickLisetener();
    }
    public ButtonCheckBoxUtil(Button button){
        this.button=button;
        if(isChecked){
            button.setBackgroundResource(checkedResId);
        }else{
            button.setBackgroundResource(unCheckedResId);
        }
        setOnClickLisetener();
    }
    private void setOnClickLisetener(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isChecked){
                    isChecked=false;
                    button.setBackgroundResource(unCheckedResId);
                    if(checkLisetener!=null){
                        checkLisetener.isCheck(false);
                    }
                }else{
                    isChecked=true;
                    button.setBackgroundResource(checkedResId);
                    if(checkLisetener!=null){
                        checkLisetener.isCheck(true);
                    }
                }
            }
        });
    }
    public void setCheckLisetener(CheckLisetener checkLisetener){
        this.checkLisetener=checkLisetener;
    }

    /**
     * 设置选中图片的背景和未选中图片的背景
     * @param checkedResId
     * @param unCheckedResId
     */
    public void setBackgroundResource(int checkedResId,int unCheckedResId){
        this.checkedResId=checkedResId;
        this.unCheckedResId=unCheckedResId;

    }
    public void setChecked(boolean isChecked){
        if(isChecked){
            this.isChecked=true;
            button.setBackgroundResource(checkedResId);
            if(checkLisetener!=null){
                checkLisetener.isCheck(true);
            }
        }else{
            this.isChecked=false;
            button.setBackgroundResource(unCheckedResId);
            if(checkLisetener!=null){
                checkLisetener.isCheck(false);
            }
        }
    }
    public boolean getCheckedState(){
        return  isChecked;
    }
}
