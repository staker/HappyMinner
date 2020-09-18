package com.ailink.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailink.activity.BaseActivity;
import com.ailink.activity.BindPhoneActivity;
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
public class AdapterDailyMission extends BaseRecyclerAdapter<AdapterDailyMission.ViewHolderA> {
    private Context mContext;
    private ArrayList<DailyMissionPojo> list;
    private boolean isBindPhone=false;

    public AdapterDailyMission(Context mContext, ArrayList<DailyMissionPojo> list) {
        this.mContext = mContext;
        this.list=list;
        setOnItemClickLitener(null);

    }
    /**
     * 由系统来决定是否调用这个方法
     */
    @Override
    public ViewHolderA onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_daily_mission, parent, false);
        ViewHolderA holderA = new ViewHolderA(view);
        AutoUtils.auto(holderA.itemView);
        return holderA;
    }


    public static class ViewHolderA extends RecyclerView.ViewHolder {
        TextView txtName,txtStatus,txtTips;
        ImageView imgIcon;
        LinearLayout layoutStatus;

        public ViewHolderA(View view){
            super(view);
            txtName = (TextView) view.findViewById(R.id.txt_name);
            imgIcon= (ImageView) view.findViewById(R.id.img_icon);
            txtStatus = (TextView) view.findViewById(R.id.txt_status);
            txtTips= (TextView) view.findViewById(R.id.txt_tips);
            layoutStatus = (LinearLayout) view.findViewById(R.id.layout_status);
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

        DailyMissionPojo dailyMissionPojo=list.get(position);
        holder.txtName.setText(dailyMissionPojo.name);
        GlideUtil.getInstance().setImage(holder.imgIcon,dailyMissionPojo.iconUrl);
        if("yes".equals(dailyMissionPojo.status)){
            holder.layoutStatus.setVisibility(View.VISIBLE);
            holder.txtStatus.setVisibility(View.GONE);
            if("手机绑定".equals(dailyMissionPojo.name)){
                isBindPhone=true;
            }
        }else if("no".equals(dailyMissionPojo.status)){
            holder.layoutStatus.setVisibility(View.GONE);
            holder.txtStatus.setVisibility(View.VISIBLE);
            holder.txtStatus.setBackgroundResource(R.drawable.shape_full_blue_55);
            holder.txtStatus.setText("+"+dailyMissionPojo.addToken+"蓝钻");

        }else{
            holder.layoutStatus.setVisibility(View.GONE);
            holder.txtStatus.setVisibility(View.VISIBLE);
            holder.txtStatus.setText("即将开启");
            holder.txtStatus.setBackgroundResource(R.drawable.shape_full_gray_55);
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
        if("speedAddition".equals(dailyMissionPojo.path)){
            holder.txtStatus.setText("+挖矿收成");
        }
        super.onBindViewHolder(holder,position);
    }

    @Override
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        mOnItemClickLitener=new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if("yes".equals(list.get(position).status)){
                    return;
                }
                clickIndex(list.get(position));
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        };
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
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

}
