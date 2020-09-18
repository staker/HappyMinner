package com.ailink.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ailink.adapter.base.BaseRecyclerAdapter;
import com.ailink.pojo.UserPojo;

import com.ailink.view.RoundImageView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;
import libs.glide.GlideUtil;


/**
 这是 首页产品报价的适配器
 */
public class AdapterTradeRank extends BaseRecyclerAdapter<AdapterTradeRank.ViewHolderA> {
    private Context mContext;
    private ArrayList<UserPojo> list;


    public AdapterTradeRank(Context mContext, ArrayList<UserPojo> list) {
        this.mContext = mContext;
        this.list=list;

    }
    /**
     * 由系统来决定是否调用这个方法
     */
    @Override
    public ViewHolderA onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_trade_rank,parent, false);
        ViewHolderA holderA = new ViewHolderA(view);
        AutoUtils.auto(holderA.itemView);
        return holderA;
    }


    public static class ViewHolderA extends RecyclerView.ViewHolder {
        TextView txtName,txtTradeCount,txtRank;
        RoundImageView imgHead;
        ImageView imgRank;

        public ViewHolderA(View view){
            super(view);
            txtName = (TextView) view.findViewById(R.id.txt_name);
            txtRank = (TextView) view.findViewById(R.id.txt_rank);
            txtTradeCount = (TextView) view.findViewById(R.id.txt_count);
            imgHead= (RoundImageView) view.findViewById(R.id.img_head);
            imgRank=(ImageView) view.findViewById(R.id.img_rank);
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

        UserPojo user=list.get(position);
        holder.txtName.setText(user.userName);
        holder.txtTradeCount.setText(""+user.tradeCount+"次");
        GlideUtil.getInstance().setImage(holder.imgHead,user.avatarUrl);
        if(user.tradeRank>3){
            holder.txtRank.setVisibility(View.VISIBLE);
            holder.imgRank.setVisibility(View.GONE);
            holder.txtRank.setText(""+user.tradeRank);
        }else{
            holder.txtRank.setVisibility(View.GONE);
            holder.imgRank.setVisibility(View.VISIBLE);
            holder.imgRank.setBackgroundResource(getRankIconByRank(user.tradeRank));
        }
        super.onBindViewHolder(holder,position);
    }


    private int getRankIconByRank(int rank){
        switch (rank){
            case 1:
                return R.drawable.icon_rank01;
            case 2:
                return R.drawable.icon_rank02;
            case 3:
                return R.drawable.icon_rank03;
        }
        return R.drawable.icon_rank01;
    }
    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

}
