package com.ailink.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ailink.activity.AboutActivity;
import com.ailink.activity.FeedBackActivity;
import com.ailink.activity.InviteFriendActivity;
import com.ailink.activity.MissionActivity;
import com.ailink.activity.MissionActivity3;
import com.ailink.activity.MyMinerActivity;
import com.ailink.adapter.base.BaseRecyclerAdapter;
import com.ailink.constants.WebUrls;
import com.ailink.db.ApplicationData;
import com.ailink.db.Configuration;
import com.ailink.logic.JumpActivityUtil;

import juhuiwan.ailink.android.R;


/**
 */
public class AdapterFunction extends BaseRecyclerAdapter<AdapterFunction.ViewHolderA> {
    private Context mContext;
    private boolean isShowIcon=true;
    private String[] titles = {"钱包","任务","我的矿工","邀请好友", "规则说明", "安全中心","关于"};//,"意见反馈"
    private int[] icons = {
            R.drawable.icon_mine_qianbao,
            R.drawable.icon_mine_mission,
            R.drawable.icon_mine_miner,
            R.drawable.icon_mine_friend,
            R.drawable.icon_mine_rules,
            R.drawable.icon_mine_safe,
            R.drawable.icon_mine_about,
            R.drawable.icon_mine_feedback

    };



    public static class ViewHolderA extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView txtName;
        TextView txtDesc;
        TextView txtRedTips;

        public ViewHolderA(View view){
            super(view);
            txtName = (TextView) view.findViewById(R.id.txt_name);
            imgIcon = (ImageView) view.findViewById(R.id.img_icon);
            txtDesc = (TextView) view.findViewById(R.id.txt_desc);
            txtRedTips = (TextView) view.findViewById(R.id.txt_red_tips);
        }
    }

    public AdapterFunction(Context mContext) {
        this.mContext = mContext;
        setOnItemClickLitener(null);
    }
    /**
     * 由系统来决定是否调用这个方法
     */
    @Override
    public ViewHolderA onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_funtion, parent, false);
        ViewHolderA holderA = new ViewHolderA(view);
        return holderA;
    }

    /**
     * 相当于以前的getview，只是这个方法已经是系统判断好了，系统会重用已经写好的holder，来决定
     * 创建新的holder还是用已经存在的holder
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolderA holder, int position) {
        super.onBindViewHolder(holder,position);
        holder.txtName.setText(titles[position]);
        if(isShowIcon){
            holder.imgIcon.setVisibility(View.VISIBLE);
            holder.imgIcon.setBackgroundResource(icons[position]);
        }
        if("任务".equals(titles[position])){
            if(ApplicationData.getInstance().missionTipsCount>0){
                holder.txtRedTips.setVisibility(View.VISIBLE);
                if(ApplicationData.getInstance().missionTipsCount==1){
                    holder.txtRedTips.setText("");
                }else{
                    holder.txtRedTips.setText(""+(ApplicationData.getInstance().missionTipsCount-1));

                }

            }else{
                holder.txtRedTips.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    @Override
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {

        mOnItemClickLitener=new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                clickIndex(titles[position],position);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        };
        super.setOnItemClickLitener(mOnItemClickLitener);
    }
    private void clickIndex(String title,int index){
        if(title.equals("我的矿工")){
//            JumpActivityUtil.showNormalActivity((Activity)mContext, MyMinerActivity.class);
            JumpActivityUtil.showMinerActivity((Activity)mContext,1);
        }else if(title.equals("邀请好友")){
            JumpActivityUtil.showNormalActivity((Activity)mContext, InviteFriendActivity.class);
        }else if(title.equals("规则说明")){
//            JumpActivityUtil.showNormalActivity((Activity)mContext, BuyWishPoolActivity.class);
            JumpActivityUtil.showWebActivity((Activity)mContext,"规则说明", WebUrls.App_Rules+Configuration.getInstance(mContext).getEncodeUserToken(),false);

        }else if(title.equals("意见反馈")){
        JumpActivityUtil.showNormalActivity((Activity)mContext, FeedBackActivity.class);

         }else if(title.equals("关于")){
            JumpActivityUtil.showNormalActivity((Activity)mContext, AboutActivity.class);

        }else if(title.equals("任务")){
            JumpActivityUtil.showNormalActivity((Activity)mContext, MissionActivity.class);

        }else if(title.equals("钱包")){
            String url2= WebUrls.App_Wallet+Configuration.getInstance((Activity)mContext).getEncodeUserToken();
            JumpActivityUtil.showWebActivity((Activity)mContext,"钱包",url2,true);
        }else if(title.equals("安全中心")){
            String url= WebUrls.App_Safe+Configuration.getInstance(mContext).getEncodeUserToken();
            JumpActivityUtil.showWebActivity((Activity) mContext,"安全中心",url,true);

        }

    }
}
