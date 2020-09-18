/**
 * 
 */
package com.ailink.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailink.constants.ConstantString;
import com.ailink.db.ApplicationData;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.i.ResultListener;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.pojo.PkgInfoPojo;
import com.ailink.pojo.UserPojo;
import com.ailink.util.BroadcastUtil;
import com.ailink.util.Logg;
import com.ailink.util.NumberUtil;
import com.ailink.view.util.ToastUtil;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import juhuiwan.ailink.android.R;
import android.os.Handler;
import libs.glide.GlideUtil;

/**
 * @author 小木桩 14-07-24
 */
public class PaoView extends LinearLayout implements View.OnClickListener{

	private Context context;

	private RoundImageView imgHead,imgPool;
	private ImageView imgHeadBg;
	private TextView txtState,txtAddAsset;

	UserPojo userPojo;
	ResultListener resultListener;
	boolean isAnimationEnd=true;
    private Timer timer;
    private TimerTask task;
    boolean flag=true;

	private android.os.Handler  handler=new android.os.Handler(){
		public void handleMessage(android.os.Message msg) {
			if(txtState==null||userPojo==null){
				return;
			}
			if(flag){
				txtState.setText(userPojo.userName);
				flag=false;
			}else{
				if("stop".equals(userPojo.minerStatus)){
					txtState.setText("已满");
				}else{
					txtState.setText("挖矿中");
				}
				flag=true;
			}
		}
	};

	public PaoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public PaoView(Context context) {
		this(context, null);
		init();
	}

	private void init() {
		LayoutInflater.from(context).inflate(R.layout.layout_pao_view, this, true);
		imgHeadBg = (ImageView) findViewById(R.id.img_head_bg);

		imgHead = (RoundImageView) findViewById(R.id.img_head);
		imgPool= (RoundImageView) findViewById(R.id.img_pool);
		imgHeadBg.setOnClickListener(this);
		imgHead.setOnClickListener(this);
		txtState=(TextView) findViewById(R.id.txt_state);
		txtAddAsset=(TextView) findViewById(R.id.txt_add_asset);
	}

	public void setPaoUser(UserPojo userPojo,ResultListener resultListener){
		this.userPojo=userPojo;
		this.resultListener=resultListener;
		if(userPojo==null){
            stopTimer();
			txtState.setText("招募矿工");
			imgHead.setVisibility(View.GONE);
			imgHeadBg.setBackgroundResource(R.drawable.head_pao_null_bg);
			imgPool.setVisibility(View.GONE);
			return;
		}else if(userPojo.userId==0){
			stopTimer();
			txtState.setText("开启矿工位");
			imgHead.setVisibility(View.GONE);
			imgHeadBg.setBackgroundResource(R.drawable.head_pao_suo);
			imgPool.setVisibility(View.GONE);
			return;
		}
		Animation alphaAnimation = AnimationUtils.loadAnimation(context, R.anim.alpha_1_0_1);
		// 步骤2:创建 动画对象 并传入设置的动画效果xml文件
		txtState.startAnimation(alphaAnimation);

        initTimerTask();
		imgHeadBg.setBackgroundResource(R.drawable.head_pao_bg);
		imgHead.setVisibility(View.VISIBLE);
		imgPool.setVisibility(View.VISIBLE);
		GlideUtil.getInstance().setImage(imgHead,userPojo.avatarUrl);
		if(userPojo.poolId==1){
			imgPool.setVisibility(View.GONE);
		}else{
			imgPool.setVisibility(View.VISIBLE);
			GlideUtil.getInstance().setImage(imgPool,userPojo.poolLogo);
		}



		txtState.setText(userPojo.userName);
	}

    private void initTimerTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new Timer(true);
        if (task != null) {
            task.cancel();
            task = null;
        }
        task = new TimerTask() {
            @Override
            public void run() {
             	handler.sendEmptyMessage(0);
            }
        };
        timer.schedule(task, 0, 5000);
    }
    public  void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }



	@Override
	public void onClick(View v) {
		if(userPojo==null){
			JumpActivityUtil.showMinerActivity((Activity) context,2);
		}else if(userPojo.userId==0){
			JumpActivityUtil.showMinerActivity((Activity) context,1);
		}else{
            //收矿
			openMinerPkg();
		}
	}
	private void openMinerPkg(){
		if(!isAnimationEnd){
			return;
		}
		isAnimationEnd=false;
		ServerUtil.getMinerPkg(userPojo.pkgId, new HttpObjectListener() {
			@Override
			public void onSucess(Object object) {
                ArrayList<PkgInfoPojo> listPkg=(ArrayList<PkgInfoPojo>)object;
				showPkgAnimation(listPkg);
			}
		});
	}



	private void showPkgAnimation(final ArrayList<PkgInfoPojo> listPkg){
		if(listPkg==null||listPkg.size()==0){
			ToastUtil.showToast("请稍后尝试",(Activity) context);
			isAnimationEnd=true;
			return;
		}
		int size=listPkg.size();
		if(size==0){
			ToastUtil.showToast("请稍后尝试",(Activity) context);
			isAnimationEnd=true;
			return;
		}else if(size==1){
			//收矿成功
			userPojo.minerStatus="minering";
			txtAddAsset.setVisibility(View.VISIBLE);
//			if(listPkg.get(0).poolId==1){
//				txtAddAsset.setText(listPkg.get(0).addToken);
//			}else{
//				txtAddAsset.setText(listPkg.get(0).addToken+listPkg.get(0).tokenType);
//			}
			txtAddAsset.setText(listPkg.get(0).addToken+listPkg.get(0).tokenType);
			Animation alphaAnimation = AnimationUtils.loadAnimation(context, R.anim.alpha_move_to_top);
			// 步骤2:创建 动画对象 并传入设置的动画效果xml文件
			txtAddAsset.startAnimation(alphaAnimation);
			alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}
				@Override
				public void onAnimationEnd(Animation animation) {
					txtAddAsset.setVisibility(View.GONE);
					isAnimationEnd=true;
				}
				@Override
				public void onAnimationRepeat(Animation animation) {
				}

			});

		}else{
			//收矿成功
			userPojo.minerStatus="minering";
			txtAddAsset.setVisibility(View.VISIBLE);
//			if(listPkg.get(0).poolId==1){
//				txtAddAsset.setText(listPkg.get(0).addToken);
//			}else{
//				txtAddAsset.setText(listPkg.get(0).addToken+listPkg.get(0).tokenType);
//			}
			txtAddAsset.setText(listPkg.get(0).addToken+listPkg.get(0).tokenType);
			Animation alphaAnimation = AnimationUtils.loadAnimation(context, R.anim.alpha_move_to_top);
			// 步骤2:创建 动画对象 并传入设置的动画效果xml文件
			txtAddAsset.startAnimation(alphaAnimation);
			alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					txtAddAsset.setVisibility(View.GONE);
					isAnimationEnd=true;
					showSecondAnimation(listPkg.get(1));
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

			});



		}

		if(resultListener!=null){
			resultListener.onSucess(null);
		}

	}

	private void showSecondAnimation(PkgInfoPojo pkgInfoPojo){
		//收矿成功
		userPojo.minerStatus="minering";
		txtAddAsset.setVisibility(View.VISIBLE);
		txtAddAsset.setText(pkgInfoPojo.addToken);
		Animation alphaAnimation = AnimationUtils.loadAnimation(context, R.anim.alpha_move_to_top);
		// 步骤2:创建 动画对象 并传入设置的动画效果xml文件
		txtAddAsset.startAnimation(alphaAnimation);
		alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				txtAddAsset.setVisibility(View.GONE);
				isAnimationEnd=true;
			}
			@Override
			public void onAnimationRepeat(Animation animation) {

			}

		});
	}
}
