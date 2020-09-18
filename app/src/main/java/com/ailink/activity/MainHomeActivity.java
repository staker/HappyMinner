package com.ailink.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ailink.constants.ConstantString;
import com.ailink.constants.WebUrls;
import com.ailink.db.ApplicationData;
import com.ailink.db.Configuration;
import com.ailink.fragment.BaseFragment;
import com.ailink.fragment.HomeRankFragment;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.i.ResultListener;
import com.ailink.logic.DialogUtil;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.pojo.UserPojo;
import com.ailink.pojo.VersionPojo;
import com.ailink.util.BroadcastUtil;
import com.ailink.util.Logg;
import com.ailink.util.NumberUtil;
import com.ailink.util.PhoneDeviceUtil;
import com.ailink.util.StakerUtil;
import com.ailink.view.AlwaysMarqueeTextView;
import com.ailink.view.PaoView;
import com.ailink.view.RaiseNumberAnimTextView;
import com.ailink.view.RoundImageView;
import com.ailink.view.util.StatusBarUtil;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;
import libs.banner.BannerPojo;
import libs.banner.BannerUtil;
import libs.glide.GlideUtil;

/**
 * 这是最新首页的主界面
 */
public class MainHomeActivity extends BaseActivity {
    private Banner banner;
    private ArrayList<UserPojo> listMyMiner,listBang;
    private AlwaysMarqueeTextView txtNotice;
    private RelativeLayout layoutSpeedBuff;
    private TextView txtSpeeBuff,txtMinerCount;
    private TextView txtMsgRed;
    private RaiseNumberAnimTextView txtAsset;
    private PaoView paoView01,paoView02,paoView03,paoView04,paoView05,paoView06,paoView07;
    private RoundImageView imgHead;



    HomeRankFragment homeRankFragment;
    ResultListener resultListener;
    UserPojo userSelf;
    ArrayList<BannerPojo> listBanner;
    boolean isRequesting=false;
    BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(ConstantString.BroadcastActions.Action_Exit_App.equals(action)){
                finish();
            }
        }
    };
    @Override
    protected void initViews(Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarFullScreem(this);
        setContentView(R.layout.activity_main_home);
        banner=(Banner) findViewById(R.id.banner);

        imgHead=(RoundImageView) findViewById(R.id.img_head);
        txtMinerCount=(TextView)findViewById(R.id.txt_miner_count);
        txtAsset=(RaiseNumberAnimTextView)findViewById(R.id.txt_asset);
        txtNotice=(AlwaysMarqueeTextView)findViewById(R.id.txt_notice);
        txtSpeeBuff=(TextView) findViewById(R.id.txt_speed_buff);
        txtMsgRed=(TextView) findViewById(R.id.txt_msg_red);
        layoutSpeedBuff=(RelativeLayout) findViewById(R.id.layout_speed_buff);
        paoView01=(PaoView) findViewById(R.id.paoview01);
        paoView02=(PaoView) findViewById(R.id.paoview02);
        paoView03=(PaoView) findViewById(R.id.paoview03);
        paoView04=(PaoView) findViewById(R.id.paoview04);
        paoView05=(PaoView) findViewById(R.id.paoview05);
        paoView06=(PaoView) findViewById(R.id.paoview06);
        paoView07=(PaoView) findViewById(R.id.paoview07);
        initViewsClickLinstener(imgHead);
        initIdsClickLinstener(R.id.layout_asset,R.id.layout_home01,R.id.layout_home02,R.id.layout_home03,
                R.id.layout_home04,R.id.layout_home05,R.id.layout_home06,R.id.layout_message);
        init();
        initHomeBangFragment();
        refreshBannerAndNotice();
        refreshData();
    }
    private void init(){

        BroadcastUtil.registerReceiver1(mContext,receiver, ConstantString.BroadcastActions.Action_Exit_App);
        listMyMiner=new ArrayList<UserPojo>();
        listBang=new ArrayList<UserPojo>();
        userSelf= Configuration.getInstance(this).getUserPojo();
        if(userSelf==null){
            userSelf=new UserPojo();
        }
        txtAsset.setText("资产:"+ NumberUtil.parseENumberToNormal(userSelf.totalAsset,4));
        resultListener=new ResultListener() {
            @Override
            public void onSucess(Object object) {
                refreshData();
            }
        };

        if("yes".equals(ApplicationData.getInstance().signStatusShow)){
            ApplicationData.getInstance().signStatusShow="no";
            JumpActivityUtil.showNormalActivity(this, QianDaoActivity.class);
        }

        ServerUtil.getAppUpdate(new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                showAppUpdateDialog(object);
            }
        });
    }
    private void initHomeBangFragment() {
        try {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            homeRankFragment=new HomeRankFragment();
            transaction.add(R.id.frame_bang, homeRankFragment);
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    private void refreshData(){

        isRequesting=true;
        ServerUtil.getHomeMinerList(userSelf, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                isRequesting=false;
                ArrayList<UserPojo> temp=(ArrayList<UserPojo>)object;
                if(temp!=null){
                    listMyMiner.clear();
                    listMyMiner.addAll(temp);
                }
                if(ApplicationData.getInstance().homeSpeedBuff !=null&&!"".equals(ApplicationData.getInstance().homeSpeedBuff)){
                    txtSpeeBuff.setText(ApplicationData.getInstance().homeSpeedBuff);
                    layoutSpeedBuff.setVisibility(View.VISIBLE);
                }else{
                    layoutSpeedBuff.setVisibility(View.GONE);
                }
                if(ApplicationData.getInstance().homeMsgShow){
                    txtMsgRed.setVisibility(View.VISIBLE);
                }else {
                    txtMsgRed.setVisibility(View.GONE);
                }

                initFreezeAccount();
                initPaoView();

            }
            @Override
            public void onDataError(String dataError) {
                super.onDataError(dataError);
                isRequesting=false;
            }
        });
        ServerUtil.getMyInfo(new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                UserPojo temp = (UserPojo) object;
                if (temp != null) {
                    userSelf.totalAsset=temp.totalAsset;
                    userSelf.avatarUrl=temp.avatarUrl;
                    GlideUtil.getInstance().setImage(imgHead,temp.avatarUrl);
                    startAnimation();
                }
            }
        });
    }
    private void startAnimation(){
        String sAsset=txtAsset.getText().toString().substring(3);
        double oldAsset=Double.parseDouble(sAsset);
        double newAsset=userSelf.totalAsset;
        if(newAsset-oldAsset>0.0001){
            txtAsset.setDuration(1500);
            txtAsset.setTextNumberWithAnim("资产:",oldAsset,newAsset);
        }else{
            txtAsset.setText("资产:"+ NumberUtil.parseENumberToNormal(userSelf.totalAsset,4));
        }
    }
    private void refreshBannerAndNotice() {
        ServerUtil.getNoticeMessage(txtNotice);
        ServerUtil.getBannerList(new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                listBanner=(ArrayList<BannerPojo>)object;
                initBanner();
            }
        });
    }
    private void initBanner() {
        BannerUtil.initBanner(banner, listBanner, new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {
                BannerPojo bannerPojo=listBanner.get(position-1);
                String url="";
                if(!"null".equals(bannerPojo.url)&&bannerPojo.url!=null&&!"".equals(bannerPojo.url)){
                    url=bannerPojo.url;
                }else{
                    url= bannerPojo.pageUrl+Configuration.getInstance(mContext).getEncodeUserToken();
                }
                JumpActivityUtil.showWebActivity(mContext,bannerPojo.titleName,url,true);
            }
        });
    }
    private void initFreezeAccount(){
        if(ApplicationData.getInstance().isFreeze){
            UserPojo userPojo=Configuration.getInstance(mContext).getUserPojo();
            DialogUtil.showDialogTips(mContext,"提示","您的账号("+userPojo.userId+")已被冻结，请联系客服微信号ailink001进行处理！",new DialogUtil.OnIndexButtonListener() {
                @Override
                public void onClickIndex(int buttonIndex) {
                    mContext.finish();
                }
            });
        }
    }
    private void  initPaoView(){
        int size=listMyMiner.size();
        txtMinerCount.setText(size+"/7");
        boolean isFull=false;
    if(userSelf.minerPkgCount==7){
        isFull=true;
    }
        switch (userSelf.minerPkgCount){
            case 1:
                paoView01.setVisibility(View.VISIBLE);
                paoView02.setVisibility(View.GONE);
                paoView03.setVisibility(View.GONE);
                paoView04.setVisibility(View.GONE);
                paoView05.setVisibility(View.GONE);
                paoView06.setVisibility(View.GONE);
                break;
            case 2:
                paoView01.setVisibility(View.VISIBLE);
                paoView02.setVisibility(View.VISIBLE);
                paoView03.setVisibility(View.GONE);
                paoView04.setVisibility(View.GONE);
                paoView05.setVisibility(View.GONE);
                paoView06.setVisibility(View.GONE);
                break;
            case 3:
                paoView01.setVisibility(View.VISIBLE);
                paoView02.setVisibility(View.VISIBLE);
                paoView03.setVisibility(View.VISIBLE);
                paoView04.setVisibility(View.GONE);
                paoView05.setVisibility(View.GONE);
                paoView06.setVisibility(View.GONE);
                break;
            case 4:
                paoView01.setVisibility(View.VISIBLE);
                paoView02.setVisibility(View.VISIBLE);
                paoView03.setVisibility(View.VISIBLE);
                paoView04.setVisibility(View.VISIBLE);
                paoView05.setVisibility(View.GONE);
                paoView06.setVisibility(View.GONE);
                break;
            case 5:
                paoView01.setVisibility(View.VISIBLE);
                paoView02.setVisibility(View.VISIBLE);
                paoView03.setVisibility(View.VISIBLE);
                paoView04.setVisibility(View.VISIBLE);
                paoView05.setVisibility(View.VISIBLE);
                paoView06.setVisibility(View.GONE);
                break;
            case 6:
                paoView01.setVisibility(View.VISIBLE);
                paoView02.setVisibility(View.VISIBLE);
                paoView03.setVisibility(View.VISIBLE);
                paoView04.setVisibility(View.VISIBLE);
                paoView05.setVisibility(View.VISIBLE);
                paoView06.setVisibility(View.VISIBLE);
                break;
            case 7:
                paoView01.setVisibility(View.VISIBLE);
                paoView02.setVisibility(View.VISIBLE);
                paoView03.setVisibility(View.VISIBLE);
                paoView04.setVisibility(View.VISIBLE);
                paoView05.setVisibility(View.VISIBLE);
                paoView06.setVisibility(View.VISIBLE);
                isFull=true;
                break;
        }
        paoView07.setVisibility(View.VISIBLE);

        if(!isFull){
            paoView07.setPaoUser(new UserPojo(),resultListener);
        } else if (isFull&&size!=7){
            paoView07.setPaoUser(null,resultListener);
        }else {
        }
        switch (size){
            case 0:
                paoView01.setPaoUser(null,resultListener);
                paoView02.setPaoUser(null,resultListener);
                paoView03.setPaoUser(null,resultListener);
                paoView04.setPaoUser(null,resultListener);
                paoView05.setPaoUser(null,resultListener);
                paoView06.setPaoUser(null,resultListener);
                break;
            case 1:
                paoView01.setPaoUser(listMyMiner.get(0),resultListener);
                paoView02.setPaoUser(null,resultListener);
                paoView03.setPaoUser(null,resultListener);
                paoView04.setPaoUser(null,resultListener);
                paoView05.setPaoUser(null,resultListener);
                paoView06.setPaoUser(null,resultListener);

                break;
            case 2:
                paoView01.setPaoUser(listMyMiner.get(0),resultListener);
                paoView02.setPaoUser(listMyMiner.get(1),resultListener);
                paoView03.setPaoUser(null,resultListener);
                paoView04.setPaoUser(null,resultListener);
                paoView05.setPaoUser(null,resultListener);
                paoView06.setPaoUser(null,resultListener);

                break;
            case 3:
                paoView01.setPaoUser(listMyMiner.get(0),resultListener);
                paoView02.setPaoUser(listMyMiner.get(1),resultListener);
                paoView03.setPaoUser(listMyMiner.get(2),resultListener);
                paoView04.setPaoUser(null,resultListener);
                paoView05.setPaoUser(null,resultListener);
                paoView06.setPaoUser(null,resultListener);

                break;
            case 4:
                paoView01.setPaoUser(listMyMiner.get(0),resultListener);
                paoView02.setPaoUser(listMyMiner.get(1),resultListener);
                paoView03.setPaoUser(listMyMiner.get(2),resultListener);
                paoView04.setPaoUser(listMyMiner.get(3),resultListener);
                paoView05.setPaoUser(null,resultListener);
                paoView06.setPaoUser(null,resultListener);

                break;
            case 5:
                paoView01.setPaoUser(listMyMiner.get(0),resultListener);
                paoView02.setPaoUser(listMyMiner.get(1),resultListener);
                paoView03.setPaoUser(listMyMiner.get(2),resultListener);
                paoView04.setPaoUser(listMyMiner.get(3),resultListener);
                paoView05.setPaoUser(listMyMiner.get(4),resultListener);
                paoView06.setPaoUser(null,resultListener);
                break;
            case 6:
                paoView01.setPaoUser(listMyMiner.get(0),resultListener);
                paoView02.setPaoUser(listMyMiner.get(1),resultListener);
                paoView03.setPaoUser(listMyMiner.get(2),resultListener);
                paoView04.setPaoUser(listMyMiner.get(3),resultListener);
                paoView05.setPaoUser(listMyMiner.get(4),resultListener);
                paoView06.setPaoUser(listMyMiner.get(5),resultListener);
                break;
            case  7:
                paoView01.setPaoUser(listMyMiner.get(0),resultListener);
                paoView02.setPaoUser(listMyMiner.get(1),resultListener);
                paoView03.setPaoUser(listMyMiner.get(2),resultListener);
                paoView04.setPaoUser(listMyMiner.get(3),resultListener);
                paoView05.setPaoUser(listMyMiner.get(4),resultListener);
                paoView06.setPaoUser(listMyMiner.get(5),resultListener);
                paoView07.setPaoUser(listMyMiner.get(6),resultListener);
                break;
        }


    }




    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){

            case R.id.img_head:
                JumpActivityUtil.showNormalActivity(mContext, MeActivity.class);

                break;
            case R.id.layout_message:
                JumpActivityUtil.showNormalActivity(mContext, AutoOpenKuangActivity.class);
//                String url00= WebUrls.App_News+Configuration.getInstance(mContext).getEncodeUserToken();
//                JumpActivityUtil.showWebActivity(mContext,"消息中心",url00,true);
                break;
            case R.id.layout_asset:
                String url01= WebUrls.App_Wallet+Configuration.getInstance(mContext).getEncodeUserToken();
                JumpActivityUtil.showWebActivity(mContext,"钱包",url01,true);
                break;
            case R.id.layout_home01:
                JumpActivityUtil.showNormalActivity(mContext, ContractActivity.class);
                break;
            case R.id.layout_home02:
                String url02= WebUrls.App_QiangKuang+Configuration.getInstance(mContext).getEncodeUserToken();
                JumpActivityUtil.showWebActivity(mContext,"抢矿",url02,true);
                break;
            case R.id.layout_home03:
                JumpActivityUtil.showNormalActivity(mContext, MissionActivity.class);
                break;
            case R.id.layout_home04:
                JumpActivityUtil.showNormalActivity(mContext, PoolActivity.class);
                break;
            case R.id.layout_home05:
                JumpActivityUtil.showMinerActivity(mContext,1);
                break;
            case R.id.layout_home06:
                String url06= WebUrls.App_Bang+Configuration.getInstance(mContext).getEncodeUserToken();
                JumpActivityUtil.showTotalRankActivity(mContext,"排行榜",url06);
                break;


        }
    }












































    private void showAppUpdateDialog(Object object){
        final VersionPojo versionPojo=(VersionPojo)object;
        if(versionPojo!=null){
            int appCode= PhoneDeviceUtil.getVersionCode(this);
            if(appCode>=versionPojo.versionCode){
                return;
            }
            DialogUtil.showDialogNormal(this, versionPojo.versionLog, "取消", "更新", new DialogUtil.OnIndexButtonListener() {
                @Override
                public void onClickIndex(int buttonIndex) {
                    if(buttonIndex==2){
                        StakerUtil.showWebView(mContext,versionPojo.url);
                        if(versionPojo.state==1){
                            finish();
                        }
                    }else{
                        if(versionPojo.state==1){
                            finish();
                        }
                    }
                }
            });


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
