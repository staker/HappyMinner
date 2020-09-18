package com.ailink.fragment;


import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ailink.activity.BaseActivity;
import com.ailink.activity.ContractActivity;
import com.ailink.activity.MyMinerActivity;
import com.ailink.activity.QianDaoActivity;
import com.ailink.activity.ShowLiveActivity;
import com.ailink.constants.WebUrls;
import com.ailink.db.ApplicationData;
import com.ailink.db.Configuration;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.i.ResultListener;
import com.ailink.logic.DialogUtil;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.pojo.UserPojo;
import com.ailink.util.Logg;
import com.ailink.util.NumberUtil;
import com.ailink.view.AlwaysMarqueeTextView;
import com.ailink.view.PaoView;
import com.ailink.view.util.StatusBarUtil;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;
import libs.OneDrawable.OneDrawableUtil;
import libs.banner.BannerPojo;
import libs.banner.BannerUtil;

/**
 * cd D:\D\AppS\木木模拟器\emulator\nemu\vmonitor\bin
 * cd D:\apps\MuMu\emulator\nemu\vmonitor\bin
 * <p>
 *     如果进不来这个磁盘  直接 d:  就可以，因为cd D：\  不一定有用
 * adb_server.exe connect 127.0.0.1:7555
 */
public class HomeFragment01 extends BaseFragment {

    private Banner banner;
    private ArrayList<UserPojo> listMyMiner;
    private Button btnMessage;
    private AlwaysMarqueeTextView txtNotice;
    private TextView txtAsset,txtAddition,txtSpeeBuff;
    private RelativeLayout layoutMyMiner,layoutAddition,layoutSpeedBuff;
    private ImageView imgMinerCount,imgPkgCount;
    private PaoView paoView01,paoView02,paoView03,paoView04,paoView05,paoView06,paoView07;
    private Button btn01,btn02,btn03,btn04;

    ArrayList<BannerPojo> listBanner;
    UserPojo userSelf;
    ResultListener resultListener;
    boolean isRequesting=false;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_home01;
    }
    @Override
    public void initViews(View view) {

        StatusBarUtil.setStatusBarFullScreem((BaseActivity) getActivity());
        btnMessage =findViewById(R.id.btn_message);
        btnMessage.setOnClickListener(this);
        btn01=findViewById(R.id.btn_01);

        initIdsOnClickLinsener(R.id.btn_01,R.id.btn_02,R.id.btn_03,R.id.btn_04,R.id.btn_show_live);
        OneDrawableUtil.setBackgroundDrawable(getActivity(),btn01,R.drawable.icon_contract_press);
        banner=findViewById(R.id.banner);
        layoutMyMiner=findViewById(R.id.layout_my_miner);
        layoutMyMiner .setOnClickListener(this);

        imgMinerCount=findViewById(R.id.img_miner_count);
        imgPkgCount=findViewById(R.id.img_pkg_count);
        layoutAddition=findViewById(R.id.layout_addition);
        layoutAddition.setOnClickListener(this);
        layoutSpeedBuff=findViewById(R.id.layout_speed_buff);
        layoutSpeedBuff.setOnClickListener(this);

        txtNotice=findViewById(R.id.txt_notice);
        txtAsset=findViewById(R.id.txt_zichan);
        txtAddition=findViewById(R.id.txt_add_profit);
        txtSpeeBuff=findViewById(R.id.txt_speed_buff);
        txtAsset.setOnClickListener(this);
        paoView01=findViewById(R.id.paoview01);
        paoView02=findViewById(R.id.paoview02);
        paoView03=findViewById(R.id.paoview03);
        paoView04=findViewById(R.id.paoview04);
        paoView05=findViewById(R.id.paoview05);
        paoView06=findViewById(R.id.paoview06);
        paoView07=findViewById(R.id.paoview07);
        init();
        refreshBanner();
        refreshData();

    }
    @Override
    public void onResumeSelf() {
        super.onResumeSelf();
        StatusBarUtil.setStatusBarFullScreem((BaseActivity) getActivity());
        Logg.e("onResumeSelf home01 准备请求数据");
        if(isRequesting){
            return;
        }
        Logg.e("onResumeSelf home01 开始请求数据");
        refreshData();

    }
    private void init(){
        listMyMiner=new ArrayList<UserPojo>();
        userSelf= Configuration.getInstance(getActivity()).getUserPojo();
        if(userSelf==null){
            userSelf=new UserPojo();
        }
        resultListener=new ResultListener() {
            @Override
            public void onSucess(Object object) {
                refreshData();
            }
        };


        if("yes".equals(ApplicationData.getInstance().signStatusShow)){
            ApplicationData.getInstance().signStatusShow="no";
            JumpActivityUtil.showNormalActivity(getActivity(), QianDaoActivity.class);
        }
    }

    private void refreshData(){
        ServerUtil.getNoticeMessage(txtNotice);
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

                if(ApplicationData.getInstance().waKuangBuff !=null&&!"".equals(ApplicationData.getInstance().waKuangBuff)){
                    txtAddition.setText(ApplicationData.getInstance().waKuangBuff);
                    layoutAddition.setVisibility(View.VISIBLE);
                }else{
                    layoutAddition.setVisibility(View.GONE);
                }

                if(ApplicationData.getInstance().topSpeedBuff !=null&&!"".equals(ApplicationData.getInstance().topSpeedBuff)){
                    txtSpeeBuff.setText(ApplicationData.getInstance().topSpeedBuff);
                    layoutSpeedBuff.setVisibility(View.VISIBLE);
                }else{
                    layoutSpeedBuff.setVisibility(View.GONE);
                }



               initFreezeAccount();
                initPaoView();
                txtAsset.setText("资产:"+ NumberUtil.parseENumberToNormal(userSelf.totalAsset,4));
            }
            @Override
            public void onDataError(String dataError) {
                super.onDataError(dataError);
                isRequesting=false;
            }
        });

    }
    private void refreshBanner() {
        ServerUtil.getBannerList(new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                listBanner=(ArrayList<BannerPojo>)object;
                initBanner();
            }
        });
    }
    private void initFreezeAccount(){
        if(ApplicationData.getInstance().isFreeze){
            UserPojo userPojo=Configuration.getInstance(getActivity()).getUserPojo();
            DialogUtil.showDialogTips(getActivity(),"提示","您的账号("+userPojo.userId+")已被冻结，请联系客服微信号ailink001进行处理！",new DialogUtil.OnIndexButtonListener() {
                @Override
                public void onClickIndex(int buttonIndex) {
                    getActivity().finish();
                }
            });
        }
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
                    url= bannerPojo.pageUrl+Configuration.getInstance(getActivity()).getEncodeUserToken();
                }
                JumpActivityUtil.showWebActivity(getActivity(),bannerPojo.titleName,url,true);
            }
        });
    }
    private void  initPaoView(){
        int size=listMyMiner.size();
        imgPkgCount.setBackgroundResource(R.drawable.home_pkg_7);
        imgMinerCount.setBackgroundResource(getMinerPngId(size));
        switch (userSelf.minerPkgCount){
            case 1:
                paoView01.setVisibility(View.VISIBLE);
                paoView02.setVisibility(View.GONE);
                paoView03.setVisibility(View.GONE);
                paoView04.setVisibility(View.GONE);
                paoView05.setVisibility(View.GONE);
                paoView06.setVisibility(View.GONE);
                paoView07.setVisibility(View.GONE);
                break;
            case 2:
                paoView01.setVisibility(View.VISIBLE);
                paoView02.setVisibility(View.VISIBLE);
                paoView03.setVisibility(View.GONE);
                paoView04.setVisibility(View.GONE);
                paoView05.setVisibility(View.GONE);
                paoView06.setVisibility(View.GONE);
                paoView07.setVisibility(View.GONE);
                break;
            case 3:
                paoView01.setVisibility(View.VISIBLE);
                paoView02.setVisibility(View.VISIBLE);
                paoView03.setVisibility(View.VISIBLE);
                paoView04.setVisibility(View.GONE);
                paoView05.setVisibility(View.GONE);
                paoView06.setVisibility(View.GONE);
                paoView07.setVisibility(View.GONE);

                break;
            case 4:
                paoView01.setVisibility(View.VISIBLE);
                paoView02.setVisibility(View.VISIBLE);
                paoView03.setVisibility(View.VISIBLE);
                paoView04.setVisibility(View.VISIBLE);
                paoView05.setVisibility(View.GONE);
                paoView06.setVisibility(View.GONE);
                paoView07.setVisibility(View.GONE);
                break;
            case 5:
                paoView01.setVisibility(View.VISIBLE);
                paoView02.setVisibility(View.VISIBLE);
                paoView03.setVisibility(View.VISIBLE);
                paoView04.setVisibility(View.VISIBLE);
                paoView05.setVisibility(View.VISIBLE);
                paoView06.setVisibility(View.GONE);
                paoView07.setVisibility(View.GONE);
                break;
            case 6:
                paoView01.setVisibility(View.VISIBLE);
                paoView02.setVisibility(View.VISIBLE);
                paoView03.setVisibility(View.VISIBLE);
                paoView04.setVisibility(View.VISIBLE);
                paoView05.setVisibility(View.VISIBLE);
                paoView06.setVisibility(View.VISIBLE);
                paoView07.setVisibility(View.GONE);
                break;
            case 7:
                paoView01.setVisibility(View.VISIBLE);
                paoView02.setVisibility(View.VISIBLE);
                paoView03.setVisibility(View.VISIBLE);
                paoView04.setVisibility(View.VISIBLE);
                paoView05.setVisibility(View.VISIBLE);
                paoView06.setVisibility(View.VISIBLE);
                paoView07.setVisibility(View.VISIBLE);
                break;
        }
        switch (size){
            case 0:
                paoView01.setPaoUser(null,resultListener);
                paoView02.setPaoUser(null,resultListener);
                paoView03.setPaoUser(null,resultListener);
                paoView04.setPaoUser(null,resultListener);
                paoView05.setPaoUser(null,resultListener);
                paoView06.setPaoUser(null,resultListener);
                paoView07.setPaoUser(null,resultListener);
                break;
            case 1:
                paoView01.setPaoUser(listMyMiner.get(0),resultListener);
                paoView02.setPaoUser(null,resultListener);
                paoView03.setPaoUser(null,resultListener);
                paoView04.setPaoUser(null,resultListener);
                paoView05.setPaoUser(null,resultListener);
                paoView06.setPaoUser(null,resultListener);
                paoView07.setPaoUser(null,resultListener);
                break;
            case 2:
                paoView01.setPaoUser(listMyMiner.get(0),resultListener);
                paoView02.setPaoUser(listMyMiner.get(1),resultListener);
                paoView03.setPaoUser(null,resultListener);
                paoView04.setPaoUser(null,resultListener);
                paoView05.setPaoUser(null,resultListener);
                paoView06.setPaoUser(null,resultListener);
                paoView07.setPaoUser(null,resultListener);
                break;
            case 3:
                paoView01.setPaoUser(listMyMiner.get(0),resultListener);
                paoView02.setPaoUser(listMyMiner.get(1),resultListener);
                paoView03.setPaoUser(listMyMiner.get(2),resultListener);
                paoView04.setPaoUser(null,resultListener);
                paoView05.setPaoUser(null,resultListener);
                paoView06.setPaoUser(null,resultListener);
                paoView07.setPaoUser(null,resultListener);
                break;
            case 4:
                paoView01.setPaoUser(listMyMiner.get(0),resultListener);
                paoView02.setPaoUser(listMyMiner.get(1),resultListener);
                paoView03.setPaoUser(listMyMiner.get(2),resultListener);
                paoView04.setPaoUser(listMyMiner.get(3),resultListener);
                paoView05.setPaoUser(null,resultListener);
                paoView06.setPaoUser(null,resultListener);
                paoView07.setPaoUser(null,resultListener);
                break;
            case 5:
                paoView01.setPaoUser(listMyMiner.get(0),resultListener);
                paoView02.setPaoUser(listMyMiner.get(1),resultListener);
                paoView03.setPaoUser(listMyMiner.get(2),resultListener);
                paoView04.setPaoUser(listMyMiner.get(3),resultListener);
                paoView05.setPaoUser(listMyMiner.get(4),resultListener);
                paoView06.setPaoUser(null,resultListener);
                paoView07.setPaoUser(null,resultListener);
                break;
            case 6:
                paoView01.setPaoUser(listMyMiner.get(0),resultListener);
                paoView02.setPaoUser(listMyMiner.get(1),resultListener);
                paoView03.setPaoUser(listMyMiner.get(2),resultListener);
                paoView04.setPaoUser(listMyMiner.get(3),resultListener);
                paoView05.setPaoUser(listMyMiner.get(4),resultListener);
                paoView06.setPaoUser(listMyMiner.get(5),resultListener);
                paoView07.setPaoUser(null,resultListener);
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


    private int getMinerPngId(int minerCount){
        switch (minerCount){
            case 0:
                return R.drawable.home_miner_0;
            case 1:
                return R.drawable.home_miner_1;
            case 2:
                return R.drawable.home_miner_2;
            case 3:
                return R.drawable.home_miner_3;
            case 4:
                return R.drawable.home_miner_4;
            case 5:
                return R.drawable.home_miner_5;
            case 6:
                return R.drawable.home_miner_6;
            case 7:
                return R.drawable.home_miner_7;
        }
        return R.drawable.home_miner_0;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_message:
                String url3= WebUrls.App_News+Configuration.getInstance(getActivity()).getEncodeUserToken();
                JumpActivityUtil.showWebActivity(getActivity(),"消息中心",url3,true);
                break;
            case R.id.layout_my_miner:
                JumpActivityUtil.showNormalActivity(getActivity(), MyMinerActivity.class);
//                JumpActivityUtil.showNormalActivity(getActivity(), AutoOpenKuangActivity.class);
                break;
            case R.id.txt_zichan:
                String url2= WebUrls.App_Wallet+Configuration.getInstance(getActivity()).getEncodeUserToken();
                JumpActivityUtil.showWebActivity(getActivity(),"钱包",url2,true);
                break;
            case R.id.btn_01:
                JumpActivityUtil.showNormalActivity(getActivity(), ContractActivity.class);
                break;
            case R.id.btn_02:
                String url= WebUrls.App_Wallet+Configuration.getInstance(getActivity()).getEncodeUserToken();
                JumpActivityUtil.showWebActivity(getActivity(),"钱包",url,true);
                break;
            case R.id.btn_03:
                String url03= WebUrls.App_QiangKuang+Configuration.getInstance(getActivity()).getEncodeUserToken();
                JumpActivityUtil.showWebActivity(getActivity(),"抢矿",url03,true);
                break;
            case R.id.btn_04:
                String url01= WebUrls.App_Bang+Configuration.getInstance(getActivity()).getEncodeUserToken();
                JumpActivityUtil.showTotalRankActivity(getActivity(),"排行榜",url01);
                break;
            case R.id.layout_addition:
                String speedUrl= WebUrls.App_Speed+Configuration.getInstance(getActivity()).getEncodeUserToken();
                JumpActivityUtil.showWebActivity(getActivity(),"算力特权",speedUrl,true);
                break;

            case R.id.btn_show_live:
                JumpActivityUtil.showNormalActivity(getActivity(), ShowLiveActivity.class);
                break;




        }
    }
}
