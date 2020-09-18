package com.ailink.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ailink.adapter.base.BaseRecyclerAdapter;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.i.ResultListener;
import com.ailink.logic.DialogConfirmContract;
import com.ailink.pojo.ContractPojo;
import com.ailink.pojo.UserPojo;
import com.ailink.view.RoundImageView;
import com.ailink.view.util.ToastUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;
import libs.OneDrawable.OneDrawableUtil;
import libs.glide.GlideUtil;


/**
 这是 首页产品报价的适配器
 */
public class AdapterContract extends BaseRecyclerAdapter<AdapterContract.ViewHolderA> {
    private Context mContext;
    private ArrayList<ContractPojo> list;

    public AdapterContract(Context mContext, ArrayList<ContractPojo> list) {
        this.mContext = mContext;
        this.list=list;

    }
    /**
     * 由系统来决定是否调用这个方法
     */
    @Override
    public ViewHolderA onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_contract, parent, false);
        ViewHolderA holderA = new ViewHolderA(view);
        AutoUtils.auto(holderA.itemView);
        return holderA;
    }


    public static class ViewHolderA extends RecyclerView.ViewHolder {
        TextView txtName,txtDeal,txtLan,txtSpeed;
        RoundImageView imgHead;

        public ViewHolderA(View view){
            super(view);
            txtName = (TextView) view.findViewById(R.id.txt_name);
            txtDeal = (TextView) view.findViewById(R.id.txt_deal);
//            OneDrawableUtil.setBackgroundDrawable((Activity)mContext,txtDeal,R.drawable.icon_contract_press);
            imgHead= (RoundImageView) view.findViewById(R.id.img_head);
            imgHead.setRectAdius(15);
            txtLan = (TextView) view.findViewById(R.id.txt_lan);
            txtSpeed = (TextView) view.findViewById(R.id.txt_speed);
        }
    }


    /**
     * 相当于以前的getview，只是这个方法已经是系统判断好了，系统会重用已经写好的holder，来决定
     * 创建新的holder还是用已经存在的holder
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolderA holder, final int position) {

        final ContractPojo contractPojo=list.get(position);
        holder.txtName.setText(contractPojo.userName);
        holder.txtLan.setText(""+contractPojo.lan);
        holder.txtSpeed.setText(""+contractPojo.speed);
        GlideUtil.getInstance().setImage(holder.imgHead,contractPojo.avatarUrl);
        holder.txtDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog(contractPojo,position);
            }
        });
        super.onBindViewHolder(holder,position);
    }
    private void showConfirmDialog(ContractPojo contractPojo, final int index){
        new DialogConfirmContract((Activity) mContext,contractPojo,new ResultListener(){
            @Override
            public void onSucess(Object object) {
                list.remove(index);
                notifyDataSetChanged();
            }
            public void onDataError(String dataError){

            }
        }).showDialog();
    }
//    private  void confirmContract(ContractPojo contractPojo, final int index){
//        ServerUtil.sendConfirmContract(contractPojo.contractId, new HttpObjectListener() {
//            public void onSucess(Object object) {
//                ToastUtil.showToast("合约交易成功！",(Activity) mContext);
//                list.remove(index);
//                notifyDataSetChanged();
//            }
//            @Override
//            public void onDataError(String dataError) {
//                super.onDataError(dataError);
//                ToastUtil.showToast(dataError,(Activity) mContext);
//            }
//        });
//    }


    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

}
