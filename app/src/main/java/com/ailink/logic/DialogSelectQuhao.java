package com.ailink.logic;


import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.ailink.adapter.AdapterQuhao;
import com.ailink.adapter.base.BaseRecyclerAdapter;
import com.ailink.constants.QuHao;
import com.ailink.i.ResultListener;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;


/**
 * 这是平仓的对话框，里面包含逻辑的
 */
public class DialogSelectQuhao {
    private RecyclerView recycler;
    Activity activity;
    private Dialog dialog;
    private ResultListener resultListener;
    ArrayList<String> listQuhao;
    AdapterQuhao adapter;

    public DialogSelectQuhao(Activity context, ResultListener resultListener) {
        this.resultListener=resultListener;
        listQuhao= QuHao.getAllQuHao();
        activity = context;
    }
    public void showDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.dialog_view_quhao_list, null);
        recycler = (RecyclerView)v.findViewById(R.id.recycler_quhao);
        initRecycler();
        dialog = new Dialog(activity, R.style.help_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(v);
        if (activity.isFinishing()) {
            dialog = null;
            return;
        }
        dialog.show();
    }

    private void initRecycler() {
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setHasFixedSize( true);
        recycler.setNestedScrollingEnabled(false);
        adapter =new AdapterQuhao(activity,listQuhao);
        recycler.setAdapter(adapter);
        adapter.setOnItemClickLitener(new BaseRecyclerAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if(resultListener!=null){
                    resultListener.onSucess(listQuhao.get(position));
                }
                dialog.dismiss();
            }
            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }


}
