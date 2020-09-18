package com.ailink.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ailink.activity.MyMinerActivity;
import com.ailink.adapter.base.BaseRecyclerAdapter;
import com.ailink.constants.ConstantString;
import com.ailink.i.ResultListener;
import com.ailink.logic.DialogFireMiner;
import com.ailink.logic.DialogReplaceMiner;
import com.ailink.pojo.UserPojo;
import com.ailink.util.BroadcastUtil;
import com.ailink.util.Logg;
import com.ailink.view.RoundImageView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;
import libs.glide.GlideUtil;


/**
 这是 首页产品报价的适配器
 */
public class AdapterReplaceMiner extends BaseRecyclerAdapter<AdapterReplaceMiner.ViewHolderA> {
    private Context mContext;
    private ArrayList<UserPojo> list;
    private long destMinerCode;
    public AdapterReplaceMiner(Context mContext, long destMinerCode,ArrayList<UserPojo> list) {
        this.mContext = mContext;
        this.list=list;
        this.destMinerCode=destMinerCode;
    }
    /**
     * 由系统来决定是否调用这个方法
     */
    @Override
    public ViewHolderA onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_replace_miner ,parent, false);
        ViewHolderA holderA = new ViewHolderA(view);
        AutoUtils.auto(holderA.itemView);
        return holderA;
    }


    public static class ViewHolderA extends RecyclerView.ViewHolder {
        TextView txtName,txtNewPrice,txtSpeed,txtMinerState,txtFire;
        RoundImageView imgHead;
        public ViewHolderA(View view){
            super(view);
            txtName = (TextView) view.findViewById(R.id.txt_name);
            txtNewPrice = (TextView) view.findViewById(R.id.txt_shenjia);
            txtSpeed = (TextView) view.findViewById(R.id.txt_suanli);
            txtMinerState = (TextView) view.findViewById(R.id.txt_miner_state);
            txtFire = (TextView) view.findViewById(R.id.txt_fire_miner);
            imgHead= (RoundImageView) view.findViewById(R.id.img_head);
        }
    }

    /**
     * 相当于以前的getview，只是这个方法已经是系统判断好了，系统会重用已经写好的holder，来决定
     * 创建新的holder还是用已经存在的holder
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolderA holder, int position) {
        final UserPojo user=list.get(position);
        holder.txtName.setText(user.userName);
        GlideUtil.getInstance().setImage(holder.imgHead,user.avatarUrl);

//        if("stop".equals(user.minerStatus)){
//            holder.txtMinerState.setText("已满");
//            holder.txtMinerState.setBackgroundResource(R.drawable.shape_full_red_5);
//        }else{
//            holder.txtMinerState.setText("挖矿中");
//            holder.txtMinerState.setBackgroundResource(R.drawable.shape_yellow_bg);
//        }
        holder.txtNewPrice.setText("买入价 "+ (int)user.priceOld);
        holder.txtSpeed.setText("算力 "+user.speed);
        setReplaceClickListener(holder.txtFire,user,position);
        super.onBindViewHolder(holder,position);
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    private void setReplaceClickListener(TextView textView,final UserPojo user,final int index){
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogReplaceMiner((Activity) mContext, user.userId, destMinerCode, new ResultListener() {
                    @Override
                    public void onSucess(Object object) {
                       ((Activity) mContext).finish();
                        BroadcastUtil.sendBroadcast1(mContext,destMinerCode+"", ConstantString.BroadcastActions.Action_Replace_Miner_Success);
                    }
                }).showDialog();
            }
        });
    }












}
