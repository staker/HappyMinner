package com.ailink.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 这是封装的一个最简单的BaseRecyclerAdapter适配器，里面仅仅是加了对item的点击事件处理
 * 没有其他东西
 * 此外，要点击事件可用，子类必须在onBindViewHolder里面，调用super方法，否则无效
 *
 *
 *
 *
 *
 *
 * 注意：横向的RecyclerView  在xml里面设置360px 不一定是屏幕的一半，一定要在Adapter里面再适配一次才行
 * 方法如下：
 *      public ViewHolderA onCreateViewHolder(ViewGroup parent, int viewType) {
                 View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_xau_price, parent, false);
                 ViewHolderA holderA = new ViewHolderA(view);
                AutoUtils.auto(holderA.itemView);//重要的是这一句代码
                return holderA;
         }
 */

public abstract class BaseRecyclerAdapter< VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>  {

    private OnItemClickLitener mOnItemClickLitener;
    public interface OnItemClickLitener {
        public void onItemClick(View view, int position);
        public void onItemLongClick(View view, int position);
    }
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
    public void onBindViewHolder(final  VH holder, final int position) {
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

//
//    /**
//     * 带动画的添加item方法
//     * @param position  添加item的位置
//     * @param object  对应的实体类
//     */
//    public void addObject(int position,Object object){
//
//    }
//    public abstract ArrayList getListData();
}

