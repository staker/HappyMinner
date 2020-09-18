package com.ailink.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ailink.adapter.base.BaseRecyclerAdapter;
import com.ailink.i.ResultListener;
import com.ailink.logic.DialogDeleteContract;
import com.ailink.pojo.ContractPojo;
import com.ailink.view.RoundImageView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;
import libs.glide.GlideUtil;


/**
 这是 首页产品报价的适配器
 */
public class AdapterMyContract extends BaseRecyclerAdapter<AdapterMyContract.ViewHolderA> {
    private Context mContext;
    private ArrayList<ContractPojo> list;

    public AdapterMyContract(Context mContext, ArrayList<ContractPojo> list) {
        this.mContext = mContext;
        this.list=list;

    }
    /**
     * 由系统来决定是否调用这个方法
     */
    @Override
    public ViewHolderA onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_my_contract, parent, false);
        ViewHolderA holderA = new ViewHolderA(view);
        AutoUtils.auto(holderA.itemView);
        return holderA;
    }


    public static class ViewHolderA extends RecyclerView.ViewHolder {
        TextView txtCancel,txtLan,txtSpeed,txtStatusGoing,txtStatusFinish,txtName;
        RoundImageView imgHead;

        public ViewHolderA(View view){
            super(view);
            txtCancel = (TextView) view.findViewById(R.id.txt_cancel);
            txtStatusGoing = (TextView) view.findViewById(R.id.txt_status_going);
            txtStatusFinish = (TextView) view.findViewById(R.id.txt_status_finish);
            txtLan = (TextView) view.findViewById(R.id.txt_lan);
            txtSpeed = (TextView) view.findViewById(R.id.txt_speed);
            txtName = (TextView) view.findViewById(R.id.txt_name);
            imgHead = (RoundImageView) view.findViewById(R.id.img_head);
            imgHead.setRectAdius(15);
        }
    }


    /**
     * 相当于以前的getview，只是这个方法已经是系统判断好了，系统会重用已经写好的holder，来决定
     * 创建新的holder还是用已经存在的holder
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolderA holder, final int position) {

        final ContractPojo contractPojo=list.get(position);
        holder.txtLan.setText(""+contractPojo.lan);
        holder.txtSpeed.setText(""+contractPojo.speed);
        if(contractPojo.status==1){

            holder.txtStatusGoing.setVisibility(View.VISIBLE);
            holder.txtStatusFinish.setVisibility(View.GONE);

            holder.txtCancel.setVisibility(View.VISIBLE);
            holder.txtCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteContract(contractPojo,position);
                }
            });
            holder.txtName.setVisibility(View.GONE);
            holder.imgHead.setVisibility(View.GONE);
        }else{
            holder.txtStatusGoing.setVisibility(View.GONE);
            holder.txtStatusFinish.setVisibility(View.VISIBLE);

            holder.txtCancel.setVisibility(View.GONE);
            holder.txtName.setVisibility(View.VISIBLE);
            holder.imgHead.setVisibility(View.VISIBLE);
            holder.txtName.setText(contractPojo.userName);
            GlideUtil.getInstance().setImage(holder.imgHead,contractPojo.avatarUrl);
        }

        super.onBindViewHolder(holder,position);
    }
    private void deleteContract(ContractPojo contractPojo, final int index){
        new DialogDeleteContract((Activity) mContext, contractPojo.lan, contractPojo.contractId, new ResultListener() {
            @Override
            public void onSucess(Object object) {
                list.remove(index);
                notifyDataSetChanged();
            }
        }).showDialog();
    }


    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

}
