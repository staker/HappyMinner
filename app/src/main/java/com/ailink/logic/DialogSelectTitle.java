package com.ailink.logic;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ailink.adapter.AdapterMyMiner;
import com.ailink.adapter.AdapterTitle;
import com.ailink.adapter.base.BaseRecyclerAdapter;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.i.ResultListener;
import com.ailink.pojo.MissionPojo;
import com.ailink.pojo.TitlePojo;
import com.ailink.pojo.UserPojo;
import com.ailink.util.Logg;
import com.ailink.util.NumberUtil;
import com.ailink.view.LoadingDialog;
import com.ailink.view.util.ToastUtil;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;


/**
 * 这是平仓的对话框，里面包含逻辑的
 */
public class DialogSelectTitle {
    private RecyclerView recycler;
    Activity activity;
    private Dialog dialog;
    private ResultListener resultListener;
    private LoadingDialog loadingDialog;
    ArrayList<TitlePojo> listTitle=new ArrayList<TitlePojo>();
    AdapterTitle adapter;

    public DialogSelectTitle(Activity context, ResultListener resultListener) {
        this.resultListener=resultListener;
        loadingDialog=LoadingDialog.getInstance();
        activity = context;
        startRefreshData();
    }
    public void showDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.dialog_view_title_list, null);
        recycler = (RecyclerView)v.findViewById(R.id.recycler_title);
        initRecycler();

        dialog = new Dialog(activity, R.style.help_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(v);
        if (activity.isFinishing()) {
            dialog = null;
            return;
        }
        dialog.show();
        loadingDialog.showDialogLoading(true,activity,null);
    }

    private void initRecycler() {
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setHasFixedSize( true);
        recycler.setNestedScrollingEnabled(false);
        adapter =new AdapterTitle(activity, listTitle);
        recycler.setAdapter(adapter);
        adapter.setOnItemClickLitener(new BaseRecyclerAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                TitlePojo titlePojo=listTitle.get(position);
                sendSelectedTitle(titlePojo);
                dialog.dismiss();
            }
            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }



    private void startRefreshData(){

        ServerUtil.getTitleList(new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                loadingDialog.showDialogLoading(false,activity,null);
                ArrayList<TitlePojo> temp=(ArrayList<TitlePojo>)object;
                if(temp!=null){
                    listTitle.clear();
                    listTitle.addAll(temp);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onDataError(String dataError) {
                super.onDataError(dataError);
                loadingDialog.showDialogLoading(false,activity,null);
            }
        });
    }
    private void sendSelectedTitle(final TitlePojo titlePojo){
        loadingDialog.showDialogLoading(true,activity,null);

        ServerUtil.sendSelectedTitle(titlePojo.id, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                loadingDialog.showDialogLoading(false,activity,null);
                String response=(String)object;
                if(resultListener!=null){
                    resultListener.onSucess(titlePojo.titleName);
                }
                Logg.e("选择称号的返回  reseponse==-"+response);
            }
            @Override
            public void onDataError(String dataError) {
                super.onDataError(dataError);
                loadingDialog.showDialogLoading(false,activity,null);

            }
        });
    }

}
