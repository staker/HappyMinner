package com.ailink.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ailink.adapter.base.BaseRecyclerAdapter;
import com.ailink.logic.TextColorSetting;
import com.ailink.pojo.InvitePojo;
import com.ailink.pojo.UserPojo;
import com.ailink.view.RoundImageView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;
import libs.glide.GlideUtil;


/**
 这是 首页产品报价的适配器
 */
public class AdapterInviteReward extends BaseRecyclerAdapter<AdapterInviteReward.ViewHolderA> {
    private Context mContext;
    private ArrayList<InvitePojo> list;

    public AdapterInviteReward(Context mContext, ArrayList<InvitePojo> list) {
        this.mContext = mContext;
        this.list=list;

    }
    /**
     * 由系统来决定是否调用这个方法
     */
    @Override
    public ViewHolderA onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_invite_reward, parent, false);
        ViewHolderA holderA = new ViewHolderA(view);
        return holderA;
    }


    public static class ViewHolderA extends RecyclerView.ViewHolder {
        TextView txtLevel,txtSingleReward,txtCount,txtTatalReward;

        public ViewHolderA(View view){
            super(view);
            txtLevel = (TextView) view.findViewById(R.id.txt_level);
            txtSingleReward = (TextView) view.findViewById(R.id.txt_single_reward);
            txtCount = (TextView) view.findViewById(R.id.txt_count);
            txtTatalReward = (TextView) view.findViewById(R.id.txt_total_reward);

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

        InvitePojo invitePojo=list.get(position);
        holder.txtLevel.setText(getTextByLevel(position+1));
        TextColorSetting.addTextColor2String(holder.txtSingleReward,invitePojo.inviteSingleReward+"","蓝钻/人", Color.parseColor("#333333"),Color.parseColor("#999999"));
        if(invitePojo.inviteCount>100){
            invitePojo.inviteCount=100;
        }
        if(invitePojo.inviteCount>0){
            holder.txtCount.setText(invitePojo.inviteCount+"人");
        }else{
            holder.txtCount.setText("0");
        }

        holder.txtTatalReward.setText(invitePojo.inviteCount*invitePojo.inviteSingleReward+"");


        super.onBindViewHolder(holder,position);
    }



    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    private String getTextByLevel(int level){
        switch (level){
            case 1:
                return "一级邀请";
            case 2:
                return "二级邀请";
            case 3:
                return "三级邀请";
            case 4:
                return "四级邀请";
            case 5:
                return "五级邀请";
            case 6:
                return "六级邀请";
            case 7:
                return "七级邀请";
            case 8:
                return "八级邀请";
            case 9:
                return "九级邀请";
        }
        return "一级邀请";
    }
}
