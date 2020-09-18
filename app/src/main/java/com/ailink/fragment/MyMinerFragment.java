package com.ailink.fragment;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ailink.activity.InviteFriendActivity;
import com.ailink.activity.MainActivity;
import com.ailink.adapter.AdapterMyMiner;
import com.ailink.adapter.base.BaseRecyclerAdapter;
import com.ailink.constants.ConstantString;
import com.ailink.constants.WebUrls;
import com.ailink.db.Configuration;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.logic.DialogUtil;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.pojo.MissionPojo;
import com.ailink.pojo.UserPojo;
import com.ailink.util.BroadcastUtil;
import com.ailink.util.Logg;
import com.ailink.view.LoadingDialog;
import com.ailink.view.util.ToastUtil;
import com.ailink.view.util.TopBarLayoutUtil;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;


/**
 *这是交易界面，界面顶部的  交易所，下面对应切换为此交易所的产品
 */
public class MyMinerFragment extends BaseFragment{
    private RecyclerView recycler;
    private TextView txtMission01,txtMission02,txtMission03,txtMission04;
    private TextView txtMissionState01,txtMissionState02,txtMissionState03,txtMissionState04,txtMissionState05,txtMissionState06;
    private TextView txtPay01,txtPay02,txtPay03,txtPay04,txtPay05;
    private CardView cardViewZhaomu01,cardViewZhaomu02,cardViewZhaomu03,cardViewZhaomu04,cardViewZhaomu05,cardViewZhaomu06,cardViewZhaomu07;
    private CardView cardMission01,cardMission02,cardMission03,cardMission04,cardMission05,cardMission06;

    UserPojo userSelf;
    ArrayList<UserPojo> listMiner;
    ArrayList<MissionPojo> listMission;
    AdapterMyMiner adapter;
    private LoadingDialog loadingDialog;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_miner;
    }
    @Override
    public void initViews(View view) {
        recycler = (RecyclerView)findViewById(R.id.recycler_my_miner);
        cardViewZhaomu01=(CardView)findViewById(R.id.card_zhaomu_01);
        cardViewZhaomu02=(CardView)findViewById(R.id.card_zhaomu_02);
        cardViewZhaomu03=(CardView)findViewById(R.id.card_zhaomu_03);
        cardViewZhaomu04=(CardView)findViewById(R.id.card_zhaomu_04);
        cardViewZhaomu05=(CardView)findViewById(R.id.card_zhaomu_05);
        cardViewZhaomu06=(CardView)findViewById(R.id.card_zhaomu_06);
        cardViewZhaomu07=(CardView)findViewById(R.id.card_zhaomu_07);
        cardMission01=(CardView)findViewById(R.id.card_mission01);
        cardMission02=(CardView)findViewById(R.id.card_mission02);
        cardMission03=(CardView)findViewById(R.id.card_mission03);
        cardMission04=(CardView)findViewById(R.id.card_mission04);
        cardMission05=(CardView)findViewById(R.id.card_mission05);
        cardMission06=(CardView)findViewById(R.id.card_mission06);

        txtMission01= (TextView)findViewById(R.id.txt_mission01);
        txtMission02= (TextView)findViewById(R.id.txt_mission02);
        txtMission03= (TextView)findViewById(R.id.txt_mission03);
        txtMission04= (TextView)findViewById(R.id.txt_mission04);

        txtPay01= (TextView)findViewById(R.id.txt_pay01);
        txtPay02= (TextView)findViewById(R.id.txt_pay02);
        txtPay03= (TextView)findViewById(R.id.txt_pay03);
        txtPay04= (TextView)findViewById(R.id.txt_pay04);
        txtPay05= (TextView)findViewById(R.id.txt_pay05);

        txtMissionState01= (TextView)findViewById(R.id.txt_mission_state01);
        txtMissionState02= (TextView)findViewById(R.id.txt_mission_state02);
        txtMissionState03= (TextView)findViewById(R.id.txt_mission_state03);
        txtMissionState04= (TextView)findViewById(R.id.txt_mission_state04);
        txtMissionState05= (TextView)findViewById(R.id.txt_mission_state05);
        txtMissionState06= (TextView)findViewById(R.id.txt_mission_state06);

        initViewsClickLinstener(txtPay01,txtPay02,txtPay03,txtPay04,txtPay05);
        txtMission01.setOnClickListener(this);
        txtMission02.setOnClickListener(this);
        txtMission03.setOnClickListener(this);
        txtMission04.setOnClickListener(this);
        findViewById(R.id.txt_find_miner01).setOnClickListener(this);
        findViewById(R.id.txt_find_miner02).setOnClickListener(this);
        findViewById(R.id.txt_find_miner03).setOnClickListener(this);
        findViewById(R.id.txt_find_miner04).setOnClickListener(this);
        findViewById(R.id.txt_find_miner05).setOnClickListener(this);
        findViewById(R.id.txt_find_miner06).setOnClickListener(this);
        findViewById(R.id.txt_find_miner07).setOnClickListener(this);
        init();
        initRecyclerMyMiner();
    }
    private void init() {
        loadingDialog=LoadingDialog.getInstance();
        userSelf= Configuration.getInstance(getActivity()).getUserPojo();
        if(userSelf==null){
            userSelf=new UserPojo();
        }
        listMiner=new ArrayList<UserPojo>();
        refreshData();
    }


    private void refreshData(){
        ServerUtil.getHomeMinerList(userSelf, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                ArrayList<UserPojo> temp=(ArrayList<UserPojo>)object;
                if(temp!=null&&temp.size()!=0){
                    Logg.e("temp.size()="+temp.size());
                    listMiner.clear();
                    listMiner.addAll(temp);
                    adapter.notifyDataSetChanged();
                }
                updateZhaoMuCard();
            }
        });

        ServerUtil.getMissionInfo(new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                listMission=(ArrayList<MissionPojo>) object;
                updateMissionState();
            }
        });

    }

    private void payBuyPoolPkg(final MissionPojo missionPojo){
        DialogUtil.showDialogNormal(getActivity(), "此矿工位任务,需要花费"+missionPojo.price+"蓝钻\n是否付费并开启矿工位?", "我再想想", "开启", new DialogUtil.OnIndexButtonListener() {
            @Override
            public void onClickIndex(int buttonIndex) {
                Logg.e("buttonIndex="+buttonIndex);
                if(buttonIndex==2){
                    buyPackage(missionPojo);
                }
            }
        });
    }
    private void buyPackage(MissionPojo missionPojo){
        loadingDialog.showDialogLoading(true, getActivity(), null);
        ServerUtil.sendBuyPackage(missionPojo.id,new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                loadingDialog.showDialogLoading(false,getActivity(),null);
                ToastUtil.showToast("矿工位置开启成功",getActivity());
                refreshData();
            }
            @Override
            public void onDataError(String dataError) {
                super.onDataError(dataError);
                loadingDialog.showDialogLoading(false,getActivity(),null);
                ToastUtil.showToast(dataError,getActivity());
            }

            @Override
            public void onException(Exception e) {
                super.onException(e);
                loadingDialog.showDialogLoading(false,getActivity(),null);
            }
        });

    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){




            case R.id.txt_pay01:
                payBuyPoolPkg(listMission.get(0));
                break;
            case R.id.txt_pay02:
                payBuyPoolPkg(listMission.get(1));
                break;
            case R.id.txt_pay03:
                payBuyPoolPkg(listMission.get(2));
                break;
            case R.id.txt_pay04:
                payBuyPoolPkg(listMission.get(3));
                break;
            case R.id.txt_pay05:
                payBuyPoolPkg(listMission.get(4));
                break;

            case R.id.txt_find_miner01:
            case R.id.txt_find_miner02:
            case R.id.txt_find_miner03:
            case R.id.txt_find_miner04:
            case R.id.txt_find_miner05:
            case R.id.txt_find_miner06:
            case R.id.txt_find_miner07:
            case R.id.txt_mission01:
                BroadcastUtil.sendBroadcast(getActivity(),ConstantString.BroadcastActions.Action_Home_Jump_To_Buy_Miner);
                break;
            case R.id.txt_mission02:
                JumpActivityUtil.showNormalActivity(getActivity(), InviteFriendActivity.class);
                break;
            case R.id.txt_mission03:
                JumpActivityUtil.showNormalActivity(getActivity(), InviteFriendActivity.class);
                break;

            case R.id.txt_mission04:
                String url= WebUrls.App_Wallet+Configuration.getInstance(getActivity()).getEncodeUserToken();
                JumpActivityUtil.showWebActivity(getActivity(),"钱包",url,true);
                break;
        }
    }

    private void initRecyclerMyMiner() {
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setHasFixedSize( true);
        recycler.setNestedScrollingEnabled(false);
        adapter =new AdapterMyMiner(getActivity(), listMiner);
        recycler.setAdapter(adapter);
        adapter.setOnItemClickLitener(new BaseRecyclerAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                UserPojo userPojo=listMiner.get(position);
                JumpActivityUtil.showMinerHomeActivity(getActivity(),userPojo.userId,userPojo.userName,userPojo.avatarUrl);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    private void updateMissionState(){
        if(listMission==null){
            return;
        }
        int size=listMission.size();
        for (int i=0;i<size;i++){
            MissionPojo missionPojo=listMission.get(i);
            switch (missionPojo.id){
                case 2:
                    if("yes".equals(missionPojo.state)){
                        cardMission02.setVisibility(View.GONE);
                    }else{
                        txtMissionState02.setText(missionPojo.name);
                        cardMission02.setVisibility(View.VISIBLE);
                    }
                    break;
                case 3:
                    if("yes".equals(missionPojo.state)){
                        cardMission03.setVisibility(View.GONE);
                    }else{
                        txtMissionState03.setText(missionPojo.name);

                        cardMission03.setVisibility(View.VISIBLE);
                    }
                    break;
                case 4:
                    if("yes".equals(missionPojo.state)){
                        cardMission01.setVisibility(View.GONE);
                    }else{
                        txtMissionState01.setText(missionPojo.name);

                        cardMission01.setVisibility(View.VISIBLE);
                    }
                case 5:
                    if("yes".equals(missionPojo.state)){
                        cardMission04.setVisibility(View.GONE);
                    }else{
                        txtMissionState04.setText(missionPojo.name);
                        cardMission04.setVisibility(View.VISIBLE);
                    }
                    break;
                case 6:
                    if("yes".equals(missionPojo.state)){
                        cardMission05.setVisibility(View.GONE);
                    }else{
                        txtMissionState05.setText(missionPojo.name);
                        cardMission05.setVisibility(View.VISIBLE);
                    }
                    break;
                case 7:
                    if("yes".equals(missionPojo.state)){
                        cardMission06.setVisibility(View.GONE);
                    }else{
                        txtMissionState06.setText(missionPojo.name);
                        cardMission06.setVisibility(View.VISIBLE);
                    }
                    break;

            }
        }


    }











    public void updateZhaoMuCard(){
        int size=listMiner.size();
//        topBar.setRightText("矿工位 "+size+"/"+userSelf.minerPkgCount);

        int count=userSelf.minerPkgCount-size;
        switch (count){
            case 1:
                cardViewZhaomu01.setVisibility(View.VISIBLE);
                cardViewZhaomu02.setVisibility(View.GONE);
                cardViewZhaomu03.setVisibility(View.GONE);
                cardViewZhaomu04.setVisibility(View.GONE);
                cardViewZhaomu05.setVisibility(View.GONE);
                cardViewZhaomu06.setVisibility(View.GONE);
                cardViewZhaomu07.setVisibility(View.GONE);
                break;
            case 2:
                cardViewZhaomu01.setVisibility(View.VISIBLE);
                cardViewZhaomu02.setVisibility(View.VISIBLE);
                cardViewZhaomu03.setVisibility(View.GONE);
                cardViewZhaomu04.setVisibility(View.GONE);
                cardViewZhaomu05.setVisibility(View.GONE);
                cardViewZhaomu06.setVisibility(View.GONE);
                cardViewZhaomu07.setVisibility(View.GONE);
                break;
            case 3:
                cardViewZhaomu01.setVisibility(View.VISIBLE);
                cardViewZhaomu02.setVisibility(View.VISIBLE);
                cardViewZhaomu03.setVisibility(View.VISIBLE);
                cardViewZhaomu04.setVisibility(View.GONE);
                cardViewZhaomu05.setVisibility(View.GONE);
                cardViewZhaomu06.setVisibility(View.GONE);
                cardViewZhaomu07.setVisibility(View.GONE);
                break;
            case 4:
                cardViewZhaomu01.setVisibility(View.VISIBLE);
                cardViewZhaomu02.setVisibility(View.VISIBLE);
                cardViewZhaomu03.setVisibility(View.VISIBLE);
                cardViewZhaomu04.setVisibility(View.VISIBLE);
                cardViewZhaomu05.setVisibility(View.GONE);
                cardViewZhaomu06.setVisibility(View.GONE);
                cardViewZhaomu07.setVisibility(View.GONE);
                break;
            case 5:
                cardViewZhaomu01.setVisibility(View.VISIBLE);
                cardViewZhaomu02.setVisibility(View.VISIBLE);
                cardViewZhaomu03.setVisibility(View.VISIBLE);
                cardViewZhaomu04.setVisibility(View.VISIBLE);
                cardViewZhaomu05.setVisibility(View.VISIBLE);
                cardViewZhaomu06.setVisibility(View.GONE);
                cardViewZhaomu07.setVisibility(View.GONE);
                break;
            case 6:
                cardViewZhaomu01.setVisibility(View.VISIBLE);
                cardViewZhaomu02.setVisibility(View.VISIBLE);
                cardViewZhaomu03.setVisibility(View.VISIBLE);
                cardViewZhaomu04.setVisibility(View.VISIBLE);
                cardViewZhaomu05.setVisibility(View.VISIBLE);
                cardViewZhaomu06.setVisibility(View.VISIBLE);
                cardViewZhaomu07.setVisibility(View.GONE);
                break;
            case 7:
                cardViewZhaomu01.setVisibility(View.VISIBLE);
                cardViewZhaomu02.setVisibility(View.VISIBLE);
                cardViewZhaomu03.setVisibility(View.VISIBLE);
                cardViewZhaomu04.setVisibility(View.VISIBLE);
                cardViewZhaomu05.setVisibility(View.VISIBLE);
                cardViewZhaomu06.setVisibility(View.VISIBLE);
                cardViewZhaomu07.setVisibility(View.VISIBLE);
                break;

        }


    }

}
