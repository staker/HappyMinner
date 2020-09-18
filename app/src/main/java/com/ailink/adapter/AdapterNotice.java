package com.ailink.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ailink.adapter.base.BaseRecyclerAdapter;
import com.ailink.pojo.NoticePojo;
import com.ailink.util.DateUtil;
import com.ailink.util.Logg;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;


public class AdapterNotice extends BaseRecyclerAdapter<AdapterNotice.ViewHolderA> {
    private Context mContext;
    private ArrayList<NoticePojo> list;

    public AdapterNotice(Context mContext, ArrayList<NoticePojo> list) {
        this.mContext = mContext;
        this.list = list;
    }

    /**
     * 由系统来决定是否调用这个方法
     */
    @Override
    public ViewHolderA onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_notice, parent, false);
        ViewHolderA holderA = new ViewHolderA(view);
        return holderA;
    }


    public static class ViewHolderA extends RecyclerView.ViewHolder {
        TextView txtTitle, txtTime, txtStatus;

        public ViewHolderA(View view) {
            super(view);
            txtTitle = (TextView) view.findViewById(R.id.txt_title);
            txtTime = (TextView) view.findViewById(R.id.txt_time);
            txtStatus = (TextView) view.findViewById(R.id.txt_status);
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
        NoticePojo noticePojo = list.get(position);
        holder.txtTitle.setText(noticePojo.name);
        holder.txtTime.setText(DateUtil.toDescribeTime(noticePojo.time));
        if ("unread".equals(noticePojo.status)) {
            holder.txtStatus.setVisibility(View.VISIBLE);
        } else {
            holder.txtStatus.setVisibility(View.GONE);
        }
        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


}
