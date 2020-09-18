package com.ailink.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ailink.adapter.base.BaseRecyclerAdapter;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.logic.TextColorSetting;
import com.ailink.pojo.IncomePojo;
import com.ailink.pojo.InvitePojo;
import com.ailink.util.Logg;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;


/**
 这是 首页产品报价的适配器
 */
public class AdapterIncomeRecord extends BaseRecyclerAdapter<AdapterIncomeRecord.ViewHolderA> {
    private Activity mContext;
    private ArrayList<IncomePojo> list;

    public AdapterIncomeRecord(Context mContext, ArrayList<IncomePojo> list) {
        this.mContext = (Activity) mContext;
        this.list=list;

    }
    /**
     * 由系统来决定是否调用这个方法
     */
    @Override
    public ViewHolderA onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_income_record, parent, false);
        ViewHolderA holderA = new ViewHolderA(view);
        return holderA;
    }


    public static class ViewHolderA extends RecyclerView.ViewHolder {
        TextView txtTime,txtContent,txtCount;

        public ViewHolderA(View view){
            super(view);
            txtTime = (TextView) view.findViewById(R.id.txt_time);
            txtContent = (TextView) view.findViewById(R.id.txt_content);
            txtCount = (TextView) view.findViewById(R.id.txt_count);

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
        if(position==getItemCount()-1){
            holder.itemView.setBackgroundResource(R.drawable.shape_bottom_left_right_full_white_5);
        }else{
            holder.itemView.setBackgroundColor(Color.WHITE);
        }
        IncomePojo incomePojo=list.get(position);
        holder.txtTime.setText(incomePojo.time);
        holder.txtCount.setText("+"+incomePojo.speedAdd);
//        TextColorSetting.addTextColor2String(holder.txtSingleReward,invitePojo.inviteSingleReward+"","蓝钻/人", Color.parseColor("#333333"),Color.parseColor("#999999"));





        String text01=incomePojo.userName;

        if(incomePojo.type==1){
            text01=text01+" 购买了你,提升算力";
        }else  if(incomePojo.type==3){
            text01=text01+" 赠送礼物,提升算力";
        }else if(incomePojo.type==10){
            holder.txtContent.setText("完成"+incomePojo.info+",系统提升算力");
            super.onBindViewHolder(holder,position);
            return;
        }

        SpannableString ss01 = new SpannableString(text01);
        // 可以设置字体前两个字符的颜色
        ss01.setSpan(new AdapterIncomeRecord.MyClickableSpan(Color.parseColor("#1E5AF1"),incomePojo.userId,incomePojo.userName),0,incomePojo.userName.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txtContent.setText(ss01);
        holder.txtContent.setMovementMethod(LinkMovementMethod.getInstance());


    }



    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }
    private class MyClickableSpan extends ClickableSpan {
        int color;
        long userId;
        String userName;
        public MyClickableSpan(int color,long userId,String userName){
            this.color=color;
            this.userId=userId;
            this.userName=userName;
        }
        public void onClick(View widget) {
            JumpActivityUtil.showMinerHomeActivity(mContext,userId,userName,null);
        }
        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(color);       //设置文件颜色
            ds.setUnderlineText(true);
        }
    }

}
