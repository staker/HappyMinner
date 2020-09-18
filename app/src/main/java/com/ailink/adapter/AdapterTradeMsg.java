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
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ailink.adapter.base.BaseRecyclerAdapter;
import com.ailink.constants.ConstantString;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.pojo.TradePojo;
import com.ailink.util.NumberUtil;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;


public class AdapterTradeMsg extends BaseRecyclerAdapter<AdapterTradeMsg.ViewHolderA> {
    private Activity mContext;
    private ArrayList<TradePojo> list;
    final static int color01 =Color.parseColor("#F28C08");//黄
    final static int color02 =Color.parseColor("#1E59EF");//蓝
    final static int color03 =Color.parseColor("#ff0d24");//红

    private class MyClickableSpan extends ClickableSpan{
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

    public AdapterTradeMsg(Context mContext, ArrayList<TradePojo> list) {
        this.mContext = (Activity)mContext;
        this.list = list;
    }

    /**
     * 由系统来决定是否调用这个方法
     */
    @Override
    public ViewHolderA onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_trade_msg, parent, false);
        ViewHolderA holderA = new ViewHolderA(view);
        return holderA;
    }


    public static class ViewHolderA extends RecyclerView.ViewHolder {
        TextView txtTitle, txtTime, txtMoney,txtContent01,txtContent02,txtContent03,txtContent04;

        public ViewHolderA(View view) {
            super(view);
            txtTitle = (TextView) view.findViewById(R.id.txt_title);
            txtTime = (TextView) view.findViewById(R.id.txt_time);
            txtMoney = (TextView) view.findViewById(R.id.txt_money);
            txtContent01 = (TextView) view.findViewById(R.id.txt_content01);
            txtContent02 = (TextView) view.findViewById(R.id.txt_content02);
            txtContent03 = (TextView) view.findViewById(R.id.txt_content03);
            txtContent04 = (TextView) view.findViewById(R.id.txt_content04);
        }
    }

    /**
     * 相当于以前的getview，只是这个方法已经是系统判断好了，系统会重用已经写好的holder，来决定
     * 创建新的holder还是用已经存在的holder
     *
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
        holder.txtContent02.setVisibility(View.GONE);
        holder.txtContent03.setVisibility(View.GONE);
        holder.txtContent04.setVisibility(View.GONE);
        TradePojo tradePojo = list.get(position);
        String title="";
        int moneyColor=Color.BLACK;
        double money=0;

        if(ConstantString.TradeMsgTypes.Type_Buy.equals(tradePojo.msgType)){//买了一个无主矿工

            title="你获得了一个新矿工";
            money=-tradePojo.usedToken;
            moneyColor=Color.BLACK;
            setString03(holder,tradePojo);

        }else if(ConstantString.TradeMsgTypes.Type_ActivityGiveToken.equals(tradePojo.msgType)){
            title="欢迎来到开心矿工";
            moneyColor=Color.parseColor("#ff0d24");
            money=tradePojo.openMineAdd;
            holder.txtContent01.setText("欢迎来到开心矿工");
            holder.txtContent02.setText("快去购买一个矿工，开始游戏吧！");
            holder.txtContent02.setVisibility(View.VISIBLE);

        }else if(ConstantString.TradeMsgTypes.Type_ActivityInvite.equals(tradePojo.msgType)){
            title="邀请成功奖励";
            moneyColor=Color.parseColor("#ff0d24");
            money=tradePojo.openMineAdd;
            setString06(holder,tradePojo);


        }else if(ConstantString.TradeMsgTypes.Type_Fire.equals(tradePojo.msgType)){
            title="你解雇了一个矿工";
            moneyColor=Color.parseColor("#ff0d24");
            money=tradePojo.openMineAdd;
            setString07(holder,tradePojo);


        }else if(ConstantString.TradeMsgTypes.Type_Grab.equals(tradePojo.msgType)){//买走别人的矿工
            title="你获得了一个新矿工";
            money=-tradePojo.usedToken;
            moneyColor=Color.BLACK;
            setString04(holder,tradePojo);

        }else if(ConstantString.TradeMsgTypes.Type_Transaction.equals(tradePojo.msgType)){
            title="你被别人买走了";
            moneyColor=Color.parseColor("#ff0d24");
            money=tradePojo.getFee;
            setString02(holder,tradePojo);

        }else if(ConstantString.TradeMsgTypes.Type_Snatch.equals(tradePojo.msgType)){
            title="矿工被抢走了";
            moneyColor=Color.parseColor("#ff0d24");
            money=tradePojo.getFee+tradePojo.openMineAdd;
            setString01(holder,tradePojo);

        }else if(ConstantString.TradeMsgTypes.Type_ActivityInvitees.equals(tradePojo.msgType)){
            title="欢迎来到开心矿工";
            moneyColor=Color.parseColor("#ff0d24");
            money=tradePojo.openMineAdd;
            holder.txtContent01.setText("欢迎来到开心矿工");
            holder.txtContent02.setText("快去购买一个矿工，开始游戏吧！");
            holder.txtContent02.setVisibility(View.VISIBLE);

        }else if(ConstantString.TradeMsgTypes.Type_System.equals(tradePojo.msgType)){//系统信息
            title=tradePojo.title;
            moneyColor=Color.parseColor("#ff0d24");
            money=tradePojo.sendMine;
            setString05(holder,tradePojo);

        }
        else if(ConstantString.TradeMsgTypes.Type_Activity_Reward.equals(tradePojo.msgType)){//活动
            title=tradePojo.title;
            moneyColor=Color.parseColor("#ff0d24");
            money=tradePojo.sendMine;
            setString05(holder,tradePojo);

        } else if(ConstantString.TradeMsgTypes.Type_Replace.equals(tradePojo.msgType)){//替换
            title=tradePojo.title;
            moneyColor=Color.BLACK;
            money=-tradePojo.usedToken;
//            setString05(holder,tradePojo);
            setString08(holder,tradePojo);
        }



        holder.txtTitle.setText(title);
        holder.txtTime.setText(tradePojo.time);
        holder.txtMoney.setTextColor(moneyColor);

        if(money>=0){
            String sMoney= NumberUtil.parseENumberToNormal(money,4);
            holder.txtMoney.setText("+"+sMoney);
        }else{
            String sMoney= NumberUtil.parseENumberToNormal(money,4);
            holder.txtMoney.setText(""+sMoney);
        }

        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }






    //矿工被抢走了
    private void setString01(ViewHolderA holder, TradePojo tradePojo){
        String text01="你的矿工"+tradePojo.minerName+"被"+tradePojo.newBossName+"抢走了";


        int minerLength=tradePojo.minerName.length();
        int newBossLength=tradePojo.newBossName.length();

        SpannableString ss01 = new SpannableString(text01);
        // 可以设置字体前两个字符的颜色


        ss01.setSpan(new MyClickableSpan(color01,tradePojo.minerId,tradePojo.minerName),4,4+minerLength,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss01.setSpan(new MyClickableSpan(color02,tradePojo.newBossId,tradePojo.newBossName),5+minerLength,5+minerLength+newBossLength,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txtContent01.setText(ss01);
        holder.txtContent01.setMovementMethod(LinkMovementMethod.getInstance());



        String text02="你赚取了"+tradePojo.getFee+"蓝钻";
        SpannableString ss02 = new SpannableString(text02);
        ss02.setSpan(new ForegroundColorSpan(color03), 4,4+(tradePojo.getFee+"").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txtContent02.setText(ss02);
        holder.txtContent02.setVisibility(View.VISIBLE);


        String text03=tradePojo.minerName+"当前共挖取了"+tradePojo.openMineAdd+"蓝钻";
        SpannableString ss03 = new SpannableString(text03);
        ss03.setSpan(new MyClickableSpan(color01,tradePojo.minerId,tradePojo.minerName),0,minerLength,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss03.setSpan(new ForegroundColorSpan(color03), minerLength+6,minerLength+6+(tradePojo.openMineAdd+"").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txtContent03.setText(ss03);
        holder.txtContent03.setMovementMethod(LinkMovementMethod.getInstance());
        holder.txtContent03.setVisibility(View.VISIBLE);




        double totalFee=tradePojo.getFee+tradePojo.openMineAdd;
        String text04="总共获得收益"+totalFee+"蓝钻";
        SpannableString ss04 = new SpannableString(text04);
        ss04.setSpan(new ForegroundColorSpan(color03), 6,6+(totalFee+"").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txtContent04.setText(ss04);
        holder.txtContent04.setVisibility(View.VISIBLE);


    }









    //你被买走了
    private void setString02(ViewHolderA holder, TradePojo tradePojo){
        String text01="你被"+tradePojo.newBossName+"从"+tradePojo.oldBossName+"那里买走了";
         int oldBossLength=tradePojo.oldBossName.length();
         int newBossLength=tradePojo.newBossName.length();


        SpannableString ss01 = new SpannableString(text01);
        ss01.setSpan(new MyClickableSpan(color02,tradePojo.newBossId,tradePojo.newBossName),2,2+newBossLength,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss01.setSpan(new MyClickableSpan(color02,tradePojo.oldBossId,tradePojo.oldBossName),3+newBossLength,3+oldBossLength+newBossLength,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txtContent01.setText(ss01);
        holder.txtContent01.setMovementMethod(LinkMovementMethod.getInstance());



        String text02="你从中获取了"+tradePojo.getFee+"蓝钻";
        SpannableString ss02 = new SpannableString(text02);
        ss02.setSpan(new ForegroundColorSpan(color03), 6,6+(tradePojo.getFee+"").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txtContent02.setText(ss02);
        holder.txtContent02.setVisibility(View.VISIBLE);


        String text03="本次交易后,你的身价已经上涨为"+tradePojo.newPrice+"蓝钻";
        SpannableString ss03 = new SpannableString(text03);
        ss03.setSpan(new ForegroundColorSpan(color03), 15,15+(tradePojo.newPrice+"").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txtContent03.setText(ss03);
        holder.txtContent03.setVisibility(View.VISIBLE);


    }




    //你买了一个无主矿工
    private void setString03(ViewHolderA holder, TradePojo tradePojo){
        String text01="你从交易市场中发现了一名喜欢的矿工"+tradePojo.minerName;
         int minerLength=tradePojo.minerName.length();


        SpannableString ss01 = new SpannableString(text01);
        ss01.setSpan(new MyClickableSpan(color01,tradePojo.minerId,tradePojo.minerName),17,17+minerLength,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txtContent01.setText(ss01);
        holder.txtContent01.setMovementMethod(LinkMovementMethod.getInstance());



        String text02="见猎心喜的你直接花费了"+tradePojo.usedToken+"蓝钻将TA买下";
        SpannableString ss02 = new SpannableString(text02);
        ss02.setSpan(new ForegroundColorSpan(color03), 11,11+(tradePojo.usedToken+"").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txtContent02.setText(ss02);
        holder.txtContent02.setVisibility(View.VISIBLE);
    }



    //你买了一个别人的矿工
    private void setString04(ViewHolderA holder, TradePojo tradePojo){
        String text01="你从"+tradePojo.oldBossName+"那里买走了心仪已久的"+tradePojo.minerName;
        int oldBossLength=tradePojo.oldBossName.length();
        int minerLength=tradePojo.minerName.length();


        SpannableString ss01 = new SpannableString(text01);
        ss01.setSpan(new MyClickableSpan(color02,tradePojo.oldBossId,tradePojo.oldBossName),2,2+oldBossLength,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss01.setSpan(new MyClickableSpan(color01,tradePojo.minerId,tradePojo.minerName),12+oldBossLength,12+minerLength+oldBossLength,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.txtContent01.setText(ss01);
        holder.txtContent01.setMovementMethod(LinkMovementMethod.getInstance());



        String text02="本次交易花费了"+tradePojo.usedToken+"蓝钻";
        SpannableString ss02 = new SpannableString(text02);
        ss02.setSpan(new ForegroundColorSpan(color03), 7,7+(tradePojo.usedToken+"").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txtContent02.setText(ss02);
        holder.txtContent02.setVisibility(View.VISIBLE);
    }






    //活动提示
    private void setString05(ViewHolderA holder, TradePojo tradePojo){

        holder.txtContent01.setText(tradePojo.msgContent);

        String text02="系统赠送您"+tradePojo.sendMine+"蓝钻";
        SpannableString ss02 = new SpannableString(text02);
        ss02.setSpan(new ForegroundColorSpan(color03), 5,5+(tradePojo.sendMine+"").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txtContent02.setText(ss02);
        holder.txtContent02.setVisibility(View.VISIBLE);
    }



    //邀请成功奖励
    private void setString06(ViewHolderA holder, TradePojo tradePojo){
        String text01=tradePojo.minerName+"通过你的邀请进入了开心矿工";
        int minerLength=tradePojo.minerName.length();


        SpannableString ss01 = new SpannableString(text01);
        ss01.setSpan(new MyClickableSpan(color01,tradePojo.minerId,tradePojo.minerName),0,minerLength,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.txtContent01.setText(ss01);
        holder.txtContent01.setMovementMethod(LinkMovementMethod.getInstance());



        String text02="公测首周邀请成功一名用户,系统额外赠送"+tradePojo.openMineAdd+"蓝钻";
        SpannableString ss02 = new SpannableString(text02);
        ss02.setSpan(new ForegroundColorSpan(color03), 19,19+(tradePojo.openMineAdd+"").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txtContent02.setText(ss02);
        holder.txtContent02.setVisibility(View.VISIBLE);
    }






    //你解雇了一个矿工
    private void setString07(ViewHolderA holder, TradePojo tradePojo){
        String text01="你因为"+tradePojo.minerName+"好吃懒做而将TA解雇";
        int minerLength=tradePojo.minerName.length();

        SpannableString ss01 = new SpannableString(text01);
        ss01.setSpan(new MyClickableSpan(color01,tradePojo.minerId,tradePojo.minerName),3,3+minerLength,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txtContent01.setText(ss01);
        holder.txtContent01.setMovementMethod(LinkMovementMethod.getInstance());



        String sOpenMineAdd= NumberUtil.parseENumberToNormal(tradePojo.openMineAdd,4);
        String text02=tradePojo.minerName+"当前共挖取了"+sOpenMineAdd+"蓝钻";
        SpannableString ss02 = new SpannableString(text02);
        ss02.setSpan(new MyClickableSpan(color01,tradePojo.minerId,tradePojo.minerName), 0,minerLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.txtContent02.setText(ss02);
        holder.txtContent02.setMovementMethod(LinkMovementMethod.getInstance());
        holder.txtContent02.setVisibility(View.VISIBLE);


        String text03="当前已有空闲矿工位，快去再找一个勤劳的矿工吧！";
        holder.txtContent03.setText(text03);
        holder.txtContent03.setVisibility(View.VISIBLE);


    }










    //替换矿工
    private void setString08(ViewHolderA holder, TradePojo tradePojo){
        String text01="你用"+tradePojo.minerNameNew+"替换了原有的"+tradePojo.minerNameOld;
        int minerNewLength=tradePojo.minerNameNew.length();
        int minerOldLength=tradePojo.minerNameOld.length();


        SpannableString ss01 = new SpannableString(text01);
        ss01.setSpan(new MyClickableSpan(color02,tradePojo.minerCodeNew,tradePojo.minerNameNew),2,2+minerNewLength,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss01.setSpan(new MyClickableSpan(color02,tradePojo.minerCodeOld,tradePojo.minerNameOld),8+minerNewLength,8+minerNewLength+minerOldLength,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txtContent01.setText(ss01);
        holder.txtContent01.setMovementMethod(LinkMovementMethod.getInstance());



        String text02="此次替换操作,花费了你"+tradePojo.usedToken+"蓝钻";
        SpannableString ss02 = new SpannableString(text02);
        ss02.setSpan(new ForegroundColorSpan(color03), 11,11+(tradePojo.usedToken+"").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txtContent02.setText(ss02);
        holder.txtContent02.setVisibility(View.VISIBLE);



        String text03=tradePojo.minerNameOld+"当前共挖取了"+tradePojo.openMineAdd+"蓝钻";
        SpannableString ss03 = new SpannableString(text03);
        ss03.setSpan(new MyClickableSpan(color03,tradePojo.minerCodeOld,tradePojo.minerName),0,minerOldLength,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss03.setSpan(new ForegroundColorSpan(color03), minerOldLength+6,minerOldLength+6+(tradePojo.openMineAdd+"").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txtContent03.setText(ss03);
        holder.txtContent03.setMovementMethod(LinkMovementMethod.getInstance());
        holder.txtContent03.setVisibility(View.VISIBLE);

    }

























}
