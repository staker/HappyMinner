package com.ailink.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ailink.adapter.base.BaseRecyclerAdapter;
import com.ailink.db.ApplicationData;
import com.ailink.pojo.UserPojo;

import com.ailink.util.Logg;
import com.ailink.util.StakerUtil;
import com.ailink.view.RoundImageView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;
import libs.glide.GlideUtil;


/**
 这是 首页产品报价的适配器
 */
public class AdapterRecommendMiner extends BaseRecyclerAdapter<AdapterRecommendMiner.ViewHolderA> {
    private Context mContext;
    private ArrayList<UserPojo> list;

    public AdapterRecommendMiner(Context mContext, ArrayList<UserPojo> list) {
        this.mContext = mContext;
        this.list=list;
    }
    /**
     * 由系统来决定是否调用这个方法
     */
    @Override
    public ViewHolderA onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_recommend_minner, parent, false);
        ViewHolderA holderA = new ViewHolderA(view);
        AutoUtils.auto(holderA.itemView);
        return holderA;
    }


    public static class ViewHolderA extends RecyclerView.ViewHolder {
        TextView txtName,txtShenjia;
        RoundImageView imgHead;
        Button btnBuy;

        public ViewHolderA(View view){
            super(view);
            txtName = (TextView) view.findViewById(R.id.txt_name);
            txtShenjia = (TextView) view.findViewById(R.id.txt_shenjia);
            imgHead= (RoundImageView) view.findViewById(R.id.img_head);
            btnBuy= (Button) view.findViewById(R.id.btn_buy);
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
        StakerUtil.reSizeTextView(holder.txtShenjia,"身价:"+user.totalAsset,206);
        GlideUtil.getInstance().setImage(holder.imgHead,user.avatarUrl);
        super.onBindViewHolder(holder,position);
    }



    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

}
