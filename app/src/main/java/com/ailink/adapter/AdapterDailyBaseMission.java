package com.ailink.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailink.activity.BaseActivity;
import com.ailink.activity.BindPhoneActivity;
import com.ailink.activity.MissionQiangGiftActivity;
import com.ailink.activity.MissionSendGiftActivity;
import com.ailink.activity.QianDaoActivity;
import com.ailink.adapter.base.BaseRecyclerAdapter;
import com.ailink.constants.WebUrls;
import com.ailink.db.Configuration;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.pojo.DailyMissionPojo;
import com.ailink.pojo.UserPojo;
import com.ailink.view.util.ToastUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;
import libs.glide.GlideUtil;
import libs.util.umeng.UmengShareUtil;


/**
 这是 首页产品报价的适配器
 */
public class AdapterDailyBaseMission extends BaseRecyclerAdapter<AdapterDailyBaseMission.ViewHolderA> {
    private Context mContext;
    private ArrayList<DailyMissionPojo> list;
    private boolean isBindPhone=false;

    public AdapterDailyBaseMission(Context mContext, ArrayList<DailyMissionPojo> list) {
        this.mContext = mContext;
        this.list=list;
        setOnItemClickLitener(null);

    }
    /**
     * 由系统来决定是否调用这个方法
     */
    @Override
    public ViewHolderA onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_daily_base_mission, parent, false);
        ViewHolderA holderA = new ViewHolderA(view);
        AutoUtils.auto(holderA.itemView);
        return holderA;
    }


    public static class ViewHolderA extends RecyclerView.ViewHolder {
        TextView txtName,txtDesc,txtStatus,txtTips;
        ImageView imgIcon;

        public ViewHolderA(View view){
            super(view);
            txtName = (TextView) view.findViewById(R.id.txt_name);
            imgIcon= (ImageView) view.findViewById(R.id.img_icon);
            txtStatus = (TextView) view.findViewById(R.id.txt_status);
            txtTips= (TextView) view.findViewById(R.id.txt_tips);
            txtDesc= (TextView) view.findViewById(R.id.txt_desc);
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

        final DailyMissionPojo dailyMissionPojo=list.get(position);
        holder.txtName.setText(dailyMissionPojo.name);
        holder.txtDesc.setText(dailyMissionPojo.desc);
        GlideUtil.getInstance().setImage(holder.imgIcon,dailyMissionPojo.iconUrl);


        if("yes".equals(dailyMissionPojo.status)){
            holder.txtStatus.setText("已完成");
            holder.txtStatus.setBackgroundResource(R.drawable.shape_empty_green01_55);
            holder.txtStatus.setTextColor(Color.parseColor("#3BCB9D"));
            if("手机绑定".equals(dailyMissionPojo.name)){
                isBindPhone=true;
            }
        }else{
            holder.txtStatus.setText("去完成");
            holder.txtStatus.setBackgroundResource(R.drawable.selector_color_gradient_blue01_55);
            holder.txtStatus.setTextColor(Color.WHITE);
        }
        if("yes".equals(dailyMissionPojo.click)){
            holder.txtStatus.setEnabled(true);
            holder.txtStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickIndex(dailyMissionPojo);
                }
            });
        }else{
            holder.txtStatus.setEnabled(false);
        }


        if(dailyMissionPojo.showTips){
            holder.txtTips.setVisibility(View.VISIBLE);
            if(dailyMissionPojo.tipsNumber>0){
                holder.txtTips.setText(""+dailyMissionPojo.tipsNumber);
            }else {
                holder.txtTips.setText("");
            }
        }else {
            holder.txtTips.setVisibility(View.GONE);
        }

        super.onBindViewHolder(holder,position);
    }

    @Override
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
//        mOnItemClickLitener=new OnItemClickLitener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                if("yes".equals(list.get(position).status)){
//                    return;
//                }
//                clickIndex(list.get(position));
//            }
//
//            @Override
//            public void onItemLongClick(View view, int position) {
//
//            }
//        };
        super.setOnItemClickLitener(mOnItemClickLitener);
    }



    private void clickIndex(DailyMissionPojo missionPojo){

        if(missionPojo.name.equals("每日分享")||missionPojo.path.equals("share")){

            UserPojo userPojo=Configuration.getInstance(mContext).getUserPojo();
            UmengShareUtil.share((BaseActivity) mContext,userPojo.userId,userPojo.userName,(int)userPojo.priceNew,userPojo.speed);


        }else if(missionPojo.name.equals("手机绑定")){
            JumpActivityUtil.showNormalActivity((Activity) mContext, BindPhoneActivity.class);
        }else if(missionPojo.name.equals("实名认证")){
            if(!isBindPhone){
                ToastUtil.showToast("请先绑定手机号",(Activity)mContext);
                return;
            }
            String url= WebUrls.App_Safe+Configuration.getInstance(mContext).getEncodeUserToken();
            JumpActivityUtil.showWebActivity((Activity) mContext,"实名认证",url,true);
        }else if(missionPojo.name.equals("绑定钱包")){
            String url= WebUrls.App_Wallet+Configuration.getInstance(mContext).getEncodeUserToken();
            JumpActivityUtil.showWebActivity((Activity) mContext,"钱包",url,true);

        }else if(missionPojo.path.equals("speedAddition")){//进入算力加成
            String url= WebUrls.App_Speed+Configuration.getInstance(mContext).getEncodeUserToken();
            JumpActivityUtil.showWebActivity((Activity) mContext,"算力特权",url,true);
        }
        else if(missionPojo.name.equals("每日签到")||missionPojo.path.equals("sign")){//进入算力加成
            JumpActivityUtil.showNormalActivity((Activity) mContext, QianDaoActivity.class);
        }
        else if(missionPojo.name.equals("每日抢矿")||missionPojo.path.equals("lottery")){//进入每日抢矿
            JumpActivityUtil.showNormalActivity((Activity) mContext, MissionQiangGiftActivity.class);
        }
        else if(missionPojo.name.equals("每日送礼")||missionPojo.path.equals("gift")){//
            JumpActivityUtil.showNormalActivity((Activity) mContext, MissionSendGiftActivity.class);
        }

    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

}
