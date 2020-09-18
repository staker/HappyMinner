package com.ailink.logic;


import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.ailink.adapter.AdapterTitle;
import com.ailink.adapter.base.BaseRecyclerAdapter;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.i.ResultListener;
import com.ailink.pojo.TitlePojo;
import com.ailink.util.Logg;
import com.ailink.view.LoadingDialog;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;


/**
 * 这是平仓的对话框，里面包含逻辑的
 */
public class DialogChooseSex {
    Activity activity;
    private Dialog dialog;
    private ResultListener resultListener;
    Button btnNan,btnNv;
    int currentSex=0;//0代表没有选中  1代表男性  2 代表女性

    public DialogChooseSex(Activity context, int currentSex,ResultListener resultListener) {
        this.resultListener=resultListener;
        activity = context;
        this.currentSex=currentSex;
    }
    public void showDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.dialog_view_choose_sex, null);



        btnNan = (Button)v.findViewById(R.id.btn_nan);
        btnNv = (Button)v.findViewById(R.id.btn_nv);
        v.findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resultListener!=null){
                    resultListener.onSucess(""+currentSex);
                }
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
            }
        });
        setChoosedSex(currentSex);
        btnNan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChoosedSex(1);
            }
        });
        btnNv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChoosedSex(2);
            }
        });

        dialog = new Dialog(activity, R.style.help_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(v);
        if (activity.isFinishing()) {
            dialog = null;
            return;
        }
        dialog.show();
    }



    private void setChoosedSex(int sex){
        switch (sex){
            case 0:
                btnNan.setBackgroundResource(R.drawable.sex_nan_unpress);
                btnNv.setBackgroundResource(R.drawable.sex_nv_unpress);
                break;
            case 1:
                btnNan.setBackgroundResource(R.drawable.sex_nan_press);
                btnNv.setBackgroundResource(R.drawable.sex_nv_unpress);
                break;
            case 2:
                btnNan.setBackgroundResource(R.drawable.sex_nan_unpress);
                btnNv.setBackgroundResource(R.drawable.sex_nv_press);
                break;
        }

        currentSex=sex;
    }




}
