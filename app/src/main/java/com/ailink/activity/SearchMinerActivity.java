package com.ailink.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailink.adapter.AdapterRecommend;
import com.ailink.adapter.AdapterSearchList;
import com.ailink.adapter.base.BaseRecyclerAdapter;
import com.ailink.constants.ConstantString;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.pojo.UserPojo;
import com.ailink.util.BroadcastUtil;
import com.ailink.util.Logg;
import com.ailink.util.StakerUtil;
import com.ailink.view.LoadingDialog;
import com.ailink.view.util.SpringViewUtil;
import com.ailink.view.util.ToastUtil;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;


/**
 *
 */
public class SearchMinerActivity extends BaseActivity {
    private EditText edtSearch;
    private LinearLayout layoutResult;
    ArrayList<UserPojo> list;
    SpringViewUtil springViewUtil;
    AdapterSearchList adapter;
    LoadingDialog loadingDialog;
    int page =1;//获取第几页的数据
    boolean isRequesting=false;
    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search_miner);
        findViewById(R.id.txt_cancel).setOnClickListener(this);
        edtSearch=(EditText)findViewById(R.id.edt_search);
        layoutResult=(LinearLayout)findViewById(R.id.layout_no_result);
        springViewUtil=new SpringViewUtil(this,false);
        list=new ArrayList<UserPojo>();
        loadingDialog=LoadingDialog.getInstance();
        initRecyclerView();
        initEditSearch();

    }
    private void initEditSearch() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                refreshData();
            }
        });

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    isRequesting=false;
                    loadingDialog.showDialogLoading(true,mContext,null);
                    refreshData();
                    return true;
                }
                return false;
            }
        });
    }
    private void initRecyclerView() {
        adapter=new AdapterSearchList(this,list);
        springViewUtil.recyclerView.setAdapter(adapter);
        springViewUtil.initDefaultHeader(this);
        springViewUtil.initDefaultFooter(this);
        springViewUtil.springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
            @Override
            public void onLoadmore() {
                loadMoreData();
            }
        });
        adapter.setOnItemClickLitener(new BaseRecyclerAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                UserPojo userPojo=list.get(position);
                JumpActivityUtil.showMinerHomeActivity(mContext,userPojo.userId,userPojo.userName,userPojo.avatarUrl);
            }
            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }




    private void refreshData(){
        String key=edtSearch.getText().toString().trim();
        if(key.length()==0){
            loadingDialog.showDialogLoading(false,mContext,null);
            return;
        }
        if (isRequesting){
            return;
        }
        isRequesting=true;
        page =1;
        ServerUtil.getSearchList(key,page, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                springViewUtil.setFinishRefresh();
                ArrayList<UserPojo> temp=(ArrayList<UserPojo>)object;
                if(temp!=null){
                    if(temp.size()==0){
                        layoutResult.setVisibility(View.VISIBLE);
                    }else{
                        layoutResult.setVisibility(View.GONE);
                    }
                    list.clear();
                    list.addAll(temp);
                    adapter.notifyDataSetChanged();
                }
                isRequesting=false;
                loadingDialog.showDialogLoading(false,mContext,null);

            }
            public void onDataError(String dataError) {
                isRequesting=false;
                springViewUtil.setFinishRefresh();
                loadingDialog.showDialogLoading(false,mContext,null);
            }
            public void onException(Exception e) {
                isRequesting=false;
                springViewUtil.setFinishRefresh();
                loadingDialog.showDialogLoading(false,mContext,null);
            }
        });
    }




    private void loadMoreData() {
        String key=edtSearch.getText().toString().trim();
        if(key.length()==0){
            return;
        }
        page++;
        ServerUtil.getSearchList(key,page, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                springViewUtil.setFinishRefresh();
                ArrayList<UserPojo> temp=(ArrayList<UserPojo>)object;
                if(temp==null||temp.size()==0){
                    page--;
                    return;
                }
                list.addAll(temp);
                adapter.notifyDataSetChanged();
            }
            public void onDataError(String dataError)
            {
                page--;
                springViewUtil.setFinishRefresh();
            }
            public void onException(Exception e) {
                page--;
                springViewUtil.setFinishRefresh();
            }
        });
    }





    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.txt_cancel:
                if(StakerUtil.isSoftShowing(this)){
                    StakerUtil.hidenKeyboard(this,edtSearch);
                }else{
                    finish();
                }
                break;

        }
    }
}
