package com.ailink.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ailink.activity.BaseActivity;
import com.ailink.adapter.base.BaseRecyclerAdapter;
import com.ailink.constants.ConstantString;
import com.ailink.db.ApplicationData;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.i.ResultListener;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.pojo.PoolPojo;
import com.ailink.pojo.RequirePojo;
import com.ailink.pojo.UserPojo;
import com.ailink.util.BroadcastUtil;
import com.ailink.util.Logg;
import com.ailink.view.LoadingDialog;
import com.ailink.view.util.ToastUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;
import libs.glide.GlideUtil;


/**
 这是 首页产品报价的适配器
 */
public class AdapterPool extends BaseRecyclerAdapter<AdapterPool.ViewHolderA> {
    private Context mContext;
    private ArrayList<PoolPojo> list;
    int colorRed= Color.parseColor("#F23249");
    int colorGreen= Color.parseColor("#1DAF76");
    int colorYellow= Color.parseColor("#FEAD56");

    LoadingDialog loadingDialog;
    ResultListener resultListener;
    public AdapterPool(Context mContext, ArrayList<PoolPojo> list, ResultListener resultListener) {
        this.mContext = mContext;
        this.list=list;
        this.resultListener=resultListener;
        loadingDialog=LoadingDialog.getInstance();

    }
    /**
     * 由系统来决定是否调用这个方法
     */
    @Override
    public ViewHolderA onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_pool,parent, false);
        ViewHolderA holderA = new ViewHolderA(view);
        AutoUtils.auto(holderA.itemView);
        return holderA;
    }


    public static class ViewHolderA extends RecyclerView.ViewHolder {
        TextView txtPoolName, txtUserCount, txtMainOutput, txtStatusTime, txtMineSum, txtMineLeave, txtActionMiner,txtRequire,txtBasePoolNoMiner,txtPlaceRequire;
        LinearLayout layoutDetail,layoutOutput,layoutRequire,layoutPlaceRequire;
        RelativeLayout layoutPoolEnd;
        RecyclerView recyclerView;
        ImageView imgPoolLogo,imgPoolEnd;

        public ViewHolderA(View view){
            super(view);
            txtPoolName = (TextView) view.findViewById(R.id.txt_pool_name);
            txtBasePoolNoMiner = (TextView) view.findViewById(R.id.txt_base_pool_no_miner);
            txtUserCount = (TextView) view.findViewById(R.id.txt_user_count);
            txtMainOutput = (TextView) view.findViewById(R.id.txt_main_output);
            txtStatusTime = (TextView) view.findViewById(R.id.txt_status_time);
            txtMineSum = (TextView) view.findViewById(R.id.txt_mineSum);
            txtPlaceRequire=(TextView) view.findViewById(R.id.txt_place_require);
            txtMineLeave = (TextView) view.findViewById(R.id.txt_mineLeave);
            txtActionMiner = (TextView) view.findViewById(R.id.txt_action_miner);
            txtRequire= (TextView) view.findViewById(R.id.txt_require);
            imgPoolLogo=(ImageView) view.findViewById(R.id.img_pool_logo);
            imgPoolEnd=(ImageView) view.findViewById(R.id.img_pool_end);

            layoutDetail=(LinearLayout) view.findViewById(R.id.layout_detail);
            layoutOutput=(LinearLayout) view.findViewById(R.id.layout_output);
            layoutRequire=(LinearLayout) view.findViewById(R.id.layout_require);
            layoutPlaceRequire=(LinearLayout) view.findViewById(R.id.layout_place_require);
            layoutPoolEnd=(RelativeLayout) view.findViewById(R.id.layout_pool_end);
            recyclerView=(RecyclerView) view.findViewById(R.id.recycler_miner);
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

        PoolPojo poolPojo=list.get(position);
        holder.txtPoolName.setText(poolPojo.poolName+"矿池");
        holder.txtBasePoolNoMiner.setVisibility(View.GONE);
        holder.layoutPlaceRequire.setVisibility(View.GONE);
        GlideUtil.getInstance().setImage(holder.imgPoolLogo,poolPojo.poolLogo);

        if(poolPojo.poolId==1){
            //说明是基础矿池
            showBasePool(holder,poolPojo);
            super.onBindViewHolder(holder,position);
            return;
        }



            if("end".equals(poolPojo.status)){
                holder.txtUserCount.setVisibility(View.GONE);
                holder.layoutDetail.setVisibility(View.GONE);
                holder.txtStatusTime.setVisibility(View.GONE);
                holder.layoutPoolEnd.setVisibility(View.VISIBLE);
                holder.txtMainOutput.setText("主要产出:"+poolPojo.mainOutput);
        }else{
            holder.layoutPoolEnd.setVisibility(View.GONE);
            holder.layoutDetail.setVisibility(View.VISIBLE);
            holder.layoutOutput.setVisibility(View.VISIBLE);
            holder.txtMainOutput.setText("主要产出:"+poolPojo.mainOutput);


            if("open".equals(poolPojo.status)){
                showStatusOpen(holder,poolPojo);
            }else if("begin".equals(poolPojo.status)){
                showStatusBegin(holder,poolPojo);
            }else{
                showStatusQueue(holder,poolPojo);
            }

        }


        showPlaceRequire(holder,poolPojo);
        setActionMinerListener(holder.txtActionMiner,poolPojo);
        super.onBindViewHolder(holder,position);
    }



private void showStatusOpen(ViewHolderA holder,PoolPojo poolPojo){
    holder.txtStatusTime.setVisibility(View.VISIBLE);
    holder.txtUserCount.setVisibility(View.VISIBLE);
    holder.txtMineSum.setVisibility(View.VISIBLE);
    holder.txtMineLeave.setVisibility(View.VISIBLE);
    holder.txtMineSum.setText("产出总量:"+poolPojo.mineSum+poolPojo.mainOutput);
    holder.txtMineLeave.setText("剩余产量:"+poolPojo.mineLeave+poolPojo.mainOutput);
//    holder.txtUserCount.setText("人数:"+poolPojo.online);

//    holder.txtUserCount.setText("总算力:"+poolPojo.ratio);
    holder.txtUserCount.setText(""+poolPojo.ratio);
    holder.txtStatusTime.setText("关闭时间:"+poolPojo.endTime);

    if(poolPojo.open){
        //用户已经开启矿池
        showRecyclerViewMiner(holder,poolPojo);
    }else{
        showRequire(holder,poolPojo);
    }


}

    private void showStatusBegin(ViewHolderA holder,PoolPojo poolPojo){
        holder.txtStatusTime.setVisibility(View.VISIBLE);
        holder.txtUserCount.setVisibility(View.GONE);
        holder.txtMineSum.setVisibility(View.VISIBLE);
        holder.txtMineLeave.setVisibility(View.GONE);
        holder.txtMineSum.setText("产出总量:"+poolPojo.mineSum+poolPojo.mainOutput);
        holder.txtStatusTime.setText("开启时间:"+poolPojo.beginTime);
        holder.txtActionMiner.setVisibility(View.GONE);
        if(!poolPojo.open){
            showRequire(holder,poolPojo);
        }

    }
    private void showStatusQueue(ViewHolderA holder,PoolPojo poolPojo){
        holder.txtStatusTime.setVisibility(View.VISIBLE);
        holder.txtUserCount.setVisibility(View.GONE);
        holder.txtMineSum.setVisibility(View.VISIBLE);
        holder.txtMineLeave.setVisibility(View.GONE);
        holder.txtUserCount.setVisibility(View.VISIBLE);
//        holder.txtUserCount.setText("人数:"+poolPojo.online);
//    holder.txtUserCount.setText("总算力:"+poolPojo.ratio);
        holder.txtUserCount.setText(""+poolPojo.ratio);
        holder.txtMineSum.setText("产出总量:"+poolPojo.mineSum+poolPojo.mainOutput);
        holder.txtStatusTime.setText("开启时间:"+poolPojo.beginTime);
        holder.txtActionMiner.setVisibility(View.GONE);
        if(poolPojo.open){
            showRecyclerViewMiner(holder,poolPojo);
        }else{
            showRequire(holder,poolPojo);
        }
    }


    /**
     * 这里显示放置的矿工，
     * @param holder
     * @param poolPojo
     */
    private void showRecyclerViewMiner(ViewHolderA holder,PoolPojo poolPojo){
        holder.recyclerView.setVisibility(View.VISIBLE);
        holder.layoutRequire.setVisibility(View.GONE);
        holder.txtRequire.setVisibility(View.GONE);
        if(poolPojo.listMiner!=null&&poolPojo.listMiner.size()>0&&poolPojo.listMiner.get(0).userId!=0){
            holder.txtActionMiner.setVisibility(View.VISIBLE);
            holder.txtActionMiner.setText("更换矿工");
            initRecyclerView(holder,poolPojo);
        }else{
            holder.txtActionMiner.setVisibility(View.VISIBLE);
            holder.txtActionMiner.setText("放置矿工");
            if(poolPojo.poolId==1&&poolPojo.emptyCount==0){
                holder.txtBasePoolNoMiner.setVisibility(View.VISIBLE);
                holder.recyclerView.setVisibility(View.GONE);
            }
            if(poolPojo.emptyCount>0){
                //放置笑脸的个数
                initRecyclerView(holder,poolPojo);
            }
        }
        if(poolPojo.poolId==1){
            holder.txtActionMiner.setVisibility(View.GONE);
        }
    }
    private void initRecyclerView(ViewHolderA holder, final PoolPojo poolPojo){

        int emptyCount=poolPojo.emptyCount;
        int minerSize=0;
        if(poolPojo.listMiner!=null){

            for(int i=0;i<poolPojo.listMiner.size();i++){
                if(poolPojo.listMiner.get(i).userId==0){
                    poolPojo.listMiner.remove(i);
                    i=0;
                }
            }
            minerSize=poolPojo.listMiner.size();
        }else{
            poolPojo.listMiner=new ArrayList<UserPojo>();
        }
        if(emptyCount==0&&minerSize==0){
            return;
        }
        for(int i=0;i<emptyCount;i++){
            UserPojo userPojo=new UserPojo();
            userPojo.userId=0;
            poolPojo.listMiner.add(userPojo);
        }

        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.recyclerView.setLayoutManager(linearLayoutManager);
        holder.recyclerView.setHasFixedSize( true);
        holder.recyclerView.setNestedScrollingEnabled(false);
        AdapterPoolMiner adapter =new AdapterPoolMiner(mContext, poolPojo.listMiner);
        holder.recyclerView.setAdapter(adapter);
        adapter.setOnItemClickLitener(new BaseRecyclerAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                UserPojo userPojo=poolPojo.listMiner.get(position);
                if(userPojo.userId==0){
                    if(poolPojo.poolId==1){
                        BroadcastUtil.sendBroadcast1(mContext,"3", ConstantString.BroadcastActions.Action_Change_Bottom_Tab);
                        BroadcastUtil.sendBroadcast(mContext,ConstantString.BroadcastActions.Action_Home_Jump_To_Buy_Miner);
                        ApplicationData.getInstance().isShowRecommendMiner=true;
                    }else{
                            //非基础矿池点击的时候弹出 放置矿工
                        JumpActivityUtil.showFangzhiMinerActivity((BaseActivity)mContext,0,poolPojo.poolId);
                    }
                }
            }
            @Override
            public void onItemLongClick(View view, int position) {

            }
        });


    }



    private void showPlaceRequire(ViewHolderA holder,PoolPojo poolPojo){
        if(poolPojo.listPlaceRequire==null||poolPojo.listPlaceRequire.size()==0){
            return;
        }
        holder.layoutPlaceRequire.setVisibility(View.VISIBLE);
       int size=poolPojo.listPlaceRequire.size();
            String requires="";
            for(int i=0;i<size;i++){
                RequirePojo requirePojo=poolPojo.listPlaceRequire.get(i);
                requires=requires+requirePojo.name+"\n";
            }
            SpannableString ss = new SpannableString(requires);
            for(int i=0;i<size;i++){
                RequirePojo requirePojo=poolPojo.listPlaceRequire.get(i);
                int startLength=0;
                for(int j=0;j<i;j++){
                    startLength=startLength+poolPojo.listPlaceRequire.get(j).name.length()+1;
                }
                if("yes".equals(requirePojo.status)){
                    ss.setSpan(new ForegroundColorSpan(colorGreen), startLength,startLength+requirePojo.name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }else{
                    ss.setSpan(new ForegroundColorSpan(colorYellow), startLength,startLength+requirePojo.name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            holder.txtPlaceRequire.setText(ss);

    }





    private void showRequire(ViewHolderA holder,PoolPojo poolPojo){
        holder.recyclerView.setVisibility(View.GONE);
        holder.layoutRequire.setVisibility(View.VISIBLE);

        if("open".equals(poolPojo.status)){
            holder.txtActionMiner.setText("开启矿池");
            holder.txtActionMiner.setVisibility(View.VISIBLE);
        }else if("begin".equals(poolPojo.status)){
            holder.txtActionMiner.setVisibility(View.GONE);
        }else{
            holder.txtActionMiner.setText("开启矿池");
            holder.txtActionMiner.setVisibility(View.VISIBLE);
        }

        if(poolPojo.listRequire!=null&&poolPojo.listRequire.size()>0){
            int size=poolPojo.listRequire.size();
            String requires="";
            for(int i=0;i<size;i++){
                RequirePojo requirePojo=poolPojo.listRequire.get(i);
                requires=requires+requirePojo.name+"\n";
            }
            holder.txtRequire.setVisibility(View.VISIBLE);
            SpannableString ss = new SpannableString(requires);
            for(int i=0;i<size;i++){
                RequirePojo requirePojo=poolPojo.listRequire.get(i);
                int startLength=0;
                for(int j=0;j<i;j++){
                    startLength=startLength+poolPojo.listRequire.get(j).name.length()+1;
                }
                if("yes".equals(requirePojo.status)){
                    ss.setSpan(new ForegroundColorSpan(colorGreen), startLength,startLength+requirePojo.name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }else{
                    ss.setSpan(new ForegroundColorSpan(colorRed), startLength,startLength+requirePojo.name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            holder.txtRequire.setText(ss);
        }else{
            holder.recyclerView.setVisibility(View.VISIBLE);
            holder.layoutRequire.setVisibility(View.GONE);
        }
    }


    /**
     * 这里给底部的 操作按钮设置监听
     * @param textView
     */
    private void setActionMinerListener(final TextView textView,final PoolPojo poolPojo){
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content=textView.getText().toString().trim();
                Logg.e("content="+content);
                if("开启矿池".equals(content)){
                    openPool(poolPojo);
                }else  if("放置矿工".equals(content)){
                    JumpActivityUtil.showFangzhiMinerActivity((BaseActivity)mContext,0,poolPojo.poolId);
                }else  if("更换矿工".equals(content)){
                    JumpActivityUtil.showFangzhiMinerActivity((BaseActivity)mContext,poolPojo.listMiner.get(0).userId,poolPojo.poolId);
                }
            }
        });
    }


    private void openPool(PoolPojo poolPojo){
        loadingDialog.showDialogLoading(true,(BaseActivity)mContext,null);
        ServerUtil.sendOpenPool(poolPojo.poolId, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                loadingDialog.showDialogLoading(false,(BaseActivity)mContext,null);
                ToastUtil.showToast("开启成功",(BaseActivity)mContext);
                if(resultListener!=null){
                    resultListener.onSucess(null);
                }
            }

            @Override
            public void onDataError(String dataError) {
                super.onDataError(dataError);
                ToastUtil.showToast(dataError,(BaseActivity)mContext);
                loadingDialog.showDialogLoading(false,(BaseActivity)mContext,null);
            }
        });
    }


    private void showBasePool(ViewHolderA holder,PoolPojo poolPojo){
        holder.txtStatusTime.setVisibility(View.GONE);
        holder.txtUserCount.setVisibility(View.GONE);
        holder.txtMineSum.setVisibility(View.GONE);
        holder.txtMineLeave.setVisibility(View.GONE);
        holder.layoutOutput.setVisibility(View.GONE);
        holder.layoutPoolEnd.setVisibility(View.GONE);
        holder.txtActionMiner.setVisibility(View.GONE);
        holder.txtMainOutput.setText("主要产出:"+poolPojo.mainOutput);
        holder.txtUserCount.setVisibility(View.VISIBLE);
//        holder.txtUserCount.setText("人数:"+poolPojo.online);
        //    holder.txtUserCount.setText("总算力:"+poolPojo.ratio);
        holder.txtUserCount.setText(""+poolPojo.ratio);
        showRecyclerViewMiner(holder,poolPojo);
    }
    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

}
