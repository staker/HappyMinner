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
import com.ailink.db.ApplicationData;
import com.ailink.i.ResultListener;
import com.ailink.logic.DialogBuyMiner;
import com.ailink.pojo.UserPojo;
import com.ailink.view.RoundImageView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;
import libs.glide.GlideUtil;


/**
 这是 首页产品报价的适配器
 */
public class AdapterRecommend extends BaseRecyclerAdapter<AdapterRecommend.ViewHolderA> {
    private Context mContext;
    private ArrayList<UserPojo> list;

    public AdapterRecommend(Context mContext, ArrayList<UserPojo> list) {
        this.mContext = mContext;
        this.list=list;
    }
    /**
     * 由系统来决定是否调用这个方法
     */
    @Override
    public ViewHolderA onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_recommend ,parent, false);
        ViewHolderA holderA = new ViewHolderA(view);
        AutoUtils.auto(holderA.itemView);
        return holderA;
    }


    public static class ViewHolderA extends RecyclerView.ViewHolder {
        TextView txtName,txtNewPrice,txtSpeed,txtBuy;
        RoundImageView imgHead;
        public ViewHolderA(View view){
            super(view);
            txtName = (TextView) view.findViewById(R.id.txt_name);
            txtNewPrice = (TextView) view.findViewById(R.id.txt_shenjia);
            txtSpeed = (TextView) view.findViewById(R.id.txt_suanli);
            txtBuy = (TextView) view.findViewById(R.id.txt_buy_miner);
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
        if(position==0){
            holder.itemView.setBackgroundResource(R.drawable.shape_top_left_right_full_white_8);
        }else{
            holder.itemView.setBackgroundColor(Color.WHITE);
        }
        final UserPojo user=list.get(position);
        holder.txtName.setText(user.userName);
        GlideUtil.getInstance().setImage(holder.imgHead,user.avatarUrl);

        holder.txtNewPrice.setText("身价 "+ (int)user.priceNew);

//        holder.txtSpeed.setText("算力 "+user.speed);
        if(user.speedGift!=0){
            holder. txtSpeed.setText("算力 " + user.speed+"("+user.speedGift+")");
        }else{
            holder.txtSpeed.setText("算力 " + user.speed);
        }

        if(ApplicationData.getInstance().replaceFlag){
            holder.txtBuy.setVisibility(View.GONE);
        }else{
            holder.txtBuy.setVisibility(View.VISIBLE);
            setBuyClickListener(holder.txtBuy,user,position);
        }

        super.onBindViewHolder(holder,position);
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    private void setBuyClickListener(TextView textView,final UserPojo user,final int index){
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogBuyMiner((Activity) mContext, user, new ResultListener() {
                    @Override
                    public void onSucess(Object object) {

                    }
                }).showDialog();;
            }
        });

    }












}
