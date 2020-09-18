package com.ailink.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ailink.adapter.base.BaseRecyclerAdapter;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.pojo.UserPojo;
import com.ailink.view.LoadingDialog;
import com.ailink.view.RoundImageView;
import com.ailink.view.util.ToastUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;
import libs.glide.GlideUtil;


/**
 这是 首页产品报价的适配器
 */
public class AdapterFangzhiMiner extends BaseRecyclerAdapter<AdapterFangzhiMiner.ViewHolderA> {
    private Activity mContext;
    private ArrayList<UserPojo> list;
    private long removeUserId;
    int poolId;
    LoadingDialog loadingDialog;
    public AdapterFangzhiMiner(Activity mContext, long removeUserId,int poolId, ArrayList<UserPojo> list) {
        this.mContext = mContext;
        this.list=list;
        this.removeUserId=removeUserId;
        this.poolId=poolId;
        loadingDialog=LoadingDialog.getInstance();
    }
    /**
     * 由系统来决定是否调用这个方法
     */
    @Override
    public ViewHolderA onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_fangzhi_miner ,parent, false);
        ViewHolderA holderA = new ViewHolderA(view);
        AutoUtils.auto(holderA.itemView);
        return holderA;
    }


    public static class ViewHolderA extends RecyclerView.ViewHolder {
        TextView txtName,txtNewPrice,txtSpeed,txtMinerState, txtFangzhi,txtCurPool;
        RoundImageView imgHead;
        public ViewHolderA(View view){
            super(view);
            txtName = (TextView) view.findViewById(R.id.txt_name);
            txtCurPool = (TextView) view.findViewById(R.id.txt_cur_pool);
            txtNewPrice = (TextView) view.findViewById(R.id.txt_shenjia);
            txtSpeed = (TextView) view.findViewById(R.id.txt_suanli);
            txtMinerState = (TextView) view.findViewById(R.id.txt_miner_state);
            txtFangzhi = (TextView) view.findViewById(R.id.txt_fire_miner);
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
        final UserPojo user=list.get(position);
        holder.txtName.setText(user.userName);
        GlideUtil.getInstance().setImage(holder.imgHead,user.avatarUrl);

        holder.txtNewPrice.setText("身价 "+ (int)user.priceNew);
        holder.txtSpeed.setText("算力 "+user.speed);
        setFangzhiClickListener(holder.txtFangzhi,user);
        if(user.poolId==1){
            holder.txtCurPool.setText("当前位置：基础矿池");
        }else{
            holder.txtCurPool.setText("当前位置："+user.poolName+"矿池");
        }
        super.onBindViewHolder(holder,position);
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    private void setFangzhiClickListener(TextView textView, final UserPojo user){
        if(user.userId==removeUserId){
            textView.setBackgroundResource(R.drawable.selector_color_gradient_red_55);
            textView.setText("撤回");
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getBack(user);
                }
            });
        }else{
            if(user.canPlace){
                textView.setText("放置");
                textView.setBackgroundResource(R.drawable.selector_shape_gradient_blue_55);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startFangZhi(user);
                    }
                });
            }else {
                textView.setText("未达标");
                textView.setBackgroundResource(R.drawable.selector_shape_gray_55);
            }

        }

    }
    private void getBack(final UserPojo user){
        loadingDialog.showDialogLoading(true,mContext,null);
        ServerUtil.sendPoolInto(1,user.userId, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                loadingDialog.showDialogLoading(false,mContext,null);
                ToastUtil.showToast("撤回成功",mContext);
                mContext.finish();
            }
            @Override
            public void onDataError(String dataError) {
                super.onDataError(dataError);
                ToastUtil.showToast(dataError,mContext);
                loadingDialog.showDialogLoading(false,mContext,null);
            }
        });
    }

    private void startFangZhi(final UserPojo user){
        loadingDialog.showDialogLoading(true,mContext,null);
        ServerUtil.sendPoolInto(poolId,user.userId, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                loadingDialog.showDialogLoading(false,mContext,null);
                ToastUtil.showToast("放置成功",mContext);
                mContext.finish();
            }

            @Override
            public void onDataError(String dataError) {
                super.onDataError(dataError);
                ToastUtil.showToast(dataError,mContext);
                loadingDialog.showDialogLoading(false,mContext,null);
            }
        });
    }












}
