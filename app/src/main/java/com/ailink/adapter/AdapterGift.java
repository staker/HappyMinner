package com.ailink.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ailink.adapter.base.BaseRecyclerAdapter;
import com.ailink.i.ResultListener;
import com.ailink.pojo.GiftPojo;
import com.ailink.pojo.IncomePojo;
import com.ailink.view.RoundImageView;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;
import libs.glide.GlideUtil;


/**
 这是 首页产品报价的适配器
 */
public class AdapterGift extends BaseRecyclerAdapter<AdapterGift.ViewHolderA> {
    private Context mContext;
    private ArrayList<GiftPojo> list;
    ResultListener resultListener;
    public AdapterGift(Context mContext, ArrayList<GiftPojo> list,ResultListener resultListener) {
        this.mContext = mContext;
        this.list=list;
        this.resultListener=resultListener;

    }
    /**
     * 由系统来决定是否调用这个方法
     */
    @Override
    public ViewHolderA onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_gift, parent, false);
        ViewHolderA holderA = new ViewHolderA(view);
        return holderA;
    }


    public static class ViewHolderA extends RecyclerView.ViewHolder {
        TextView txtName,txtPrice,txtCount,txtDesc;
        Button btnPlus,btnSub;
        RoundImageView imgGift;

        public ViewHolderA(View view){
            super(view);
            txtName = (TextView) view.findViewById(R.id.txt_name);
            txtPrice = (TextView) view.findViewById(R.id.txt_price);
            txtCount = (TextView) view.findViewById(R.id.txt_count);
            txtDesc = (TextView) view.findViewById(R.id.txt_desc);

            imgGift= (RoundImageView) view.findViewById(R.id.img_gift);
            btnPlus= (Button) view.findViewById(R.id.btn_plus);
            btnSub= (Button) view.findViewById(R.id.btn_sub);

        }
    }


    /**
     * 相当于以前的getview，只是这个方法已经是系统判断好了，系统会重用已经写好的holder，来决定
     * 创建新的holder还是用已经存在的holder
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ViewHolderA holder, int position) {
        if(position==getItemCount()-1){
            holder.itemView.setBackgroundResource(R.drawable.shape_bottom_left_right_full_white_5);
        }else{
            holder.itemView.setBackgroundColor(Color.WHITE);
        }
        final GiftPojo giftPojo=list.get(position);
        holder.txtName.setText(giftPojo.giftName);
        holder.txtPrice.setText(giftPojo.price+"蓝钻");
        holder.txtDesc.setText("功效:"+giftPojo.desc);
        GlideUtil.getInstance().setImage(holder.imgGift,giftPojo.iconUrl);
        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPlus(holder.btnSub,holder.txtCount,giftPojo);
            }
        });
        holder.btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSub(holder.btnSub,holder.txtCount,giftPojo);
            }
        });
        if(giftPojo.count<1){
            holder.btnSub.setVisibility(View.GONE);
        }else {
            holder.btnSub.setVisibility(View.VISIBLE);
        }
        super.onBindViewHolder(holder,position);
    }



    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }










    private void clickPlus(Button btnSub,TextView txtCount,GiftPojo giftPojo){
        try {
            String sCount=txtCount.getText().toString();
            int count=Integer.parseInt(sCount);
            if(count<99){
                count++;
                btnSub.setVisibility(View.VISIBLE);
                giftPojo.count=count;
                txtCount.setText(count+"");
                if(resultListener!=null){
                    resultListener.onSucess(null);
                }
            }
        }catch (Exception e){

        }
    }
    private void clickSub(Button btnSub,TextView txtCount,GiftPojo giftPojo){
        try {
            String sCount=txtCount.getText().toString();
            int count=Integer.parseInt(sCount);
            if(count>0){
                count--;
                giftPojo.count=count;
                txtCount.setText(count+"");
                if(resultListener!=null){
                    resultListener.onSucess(null);
                }
            }
            if(count<=0){
                btnSub.setVisibility(View.GONE);
            }
        }catch (Exception e){

        }
    }


}
