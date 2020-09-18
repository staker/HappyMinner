package com.ailink.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ailink.adapter.base.BaseRecyclerAdapter;
import com.ailink.pojo.TitlePojo;
import com.ailink.pojo.UserPojo;
import com.ailink.view.RoundImageView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;
import libs.glide.GlideUtil;


/**
 这是 首页产品报价的适配器
 */
public class AdapterTitle extends BaseRecyclerAdapter<AdapterTitle.ViewHolderA> {
    private Context mContext;
    private ArrayList<TitlePojo> list;


    public AdapterTitle(Context mContext, ArrayList<TitlePojo> list) {
        this.mContext = mContext;
        this.list=list;

    }
    /**
     * 由系统来决定是否调用这个方法
     */
    @Override
    public ViewHolderA onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_select_title,parent, false);
        ViewHolderA holderA = new ViewHolderA(view);
        AutoUtils.auto(holderA.itemView);
        return holderA;
    }


    public static class ViewHolderA extends RecyclerView.ViewHolder {
        TextView txtTitle,txtDesc;
        ImageView imgSelect;

        public ViewHolderA(View view){
            super(view);
            txtTitle = (TextView) view.findViewById(R.id.txt_name);
            txtDesc = (TextView) view.findViewById(R.id.txt_desc);
            imgSelect=(ImageView) view.findViewById(R.id.img_select);
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

        TitlePojo titlePojo=list.get(position);
        holder.txtTitle.setText(titlePojo.titleName);
        holder.txtDesc.setText(titlePojo.titleDesc+"");

        if("select".equals(titlePojo.status)){
            holder.imgSelect.setVisibility(View.VISIBLE);
        }else{

            holder.imgSelect.setVisibility(View.GONE);
        }
        super.onBindViewHolder(holder,position);
    }


    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

}
