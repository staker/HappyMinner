/**
 * 
 */
package com.ailink.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.ailink.adapter.base.ComplexViewPagerAdapter;
import com.ailink.fragment.BaseFragment;

import java.util.ArrayList;

import juhuiwan.ailink.android.R;


/**
 * @author 
 * 14-12-26   新建的新闻控件，只需要传数组的Fragment和数字的String即可组成类似新闻的导航
 */
public class NewsLayout extends LinearLayout implements View.OnClickListener{

	private Context context;
	private ViewPager pager;
	private HorizontalScrollView horView;
	private LinearLayout layoutContent;
	private ImageView imgLine;

	private FragmentManager fm;

	private ArrayList<BaseFragment> listFg;
	private ArrayList<String> listTitle;
	private ArrayList<TextView> listTxtview;
	
	
	private int screenW = 0;// 屏幕的宽度
	private int itemWidth;

	private int endPosition;//
	private int startPosition;//
	private int currentFragmentIndex;//当前滑动到的viewpager位置
	private boolean isScrollEnd;// 是否滑动结束
	
	private int unSelectedColor=Color.BLACK;
	private int  selectedColor=Color.parseColor("#df3118");
	private int itemCount=4;

	/**
	 * @param context
	 * @param attrs
	 */
	public NewsLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public NewsLayout(Context context) {
		this(context, null);
		init();
	}

	private void init() {
		LayoutInflater.from(context).inflate(R.layout.layout_news, this, true);
		horView = (HorizontalScrollView) findViewById(R.id.hor_view);
		layoutContent = (LinearLayout) findViewById(R.id.layout_content);
		imgLine = (ImageView) findViewById(R.id.img_line);
		pager = (ViewPager) findViewById(R.id.pager);

		WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		screenW = dm.widthPixels;

		itemWidth = (int) ((screenW / itemCount + 0.5f));// 设置每一个title的宽度为屏幕的1/4
		imgLine.getLayoutParams().width = itemWidth;// 滑动线条也即是没一个item的宽度
	}

	public void setItemCount(int count){
		this.itemCount=count;
		itemWidth = (int) ((screenW / count + 0.5f));// 设置每一个title的宽度为屏幕的1/count
		imgLine.getLayoutParams().width = itemWidth;
	}

	/**
	 * 直接设置下划线的宽度
	 * @param rate
	 */
	public void setItemLineWidthRate(float rate){
		imgLine.getLayoutParams().width=(int)(screenW*rate);
	}

	/**
	 * 设置对应的title，和对应的fragment
	 * 
	 * @param listFg
	 * @param listTitle
	 * @param fm
	 *            为了兼顾能在4.0版本下面使用Fragmengt，所以使用外面传过来的FragmentManager
	 */
	public void setTitleAndFragment(ArrayList<BaseFragment> listFg, ArrayList<String> listTitle, FragmentManager fm) {
		this.listFg = listFg;
		this.listTitle = listTitle;
		if(listTitle==null||listFg==null||(listFg.size()!=listTitle.size())){
			return;
		}
		this.fm = fm;
		if(listFg!=null&&listTitle!=null){
			initViewPager();
			initTitle();
		}	
		if(imgLine!=null){
			imgLine.setBackgroundColor(selectedColor);
		}
		if(listTxtview!=null&&listTxtview.size()>0){
			listTxtview.get(0).setTextColor(selectedColor);
		}
	}

	public void setTitleColor(int unSelectedColor,int selectedColor){
		this.unSelectedColor=unSelectedColor;
		this.selectedColor=selectedColor;
		if(imgLine!=null){
			imgLine.setBackgroundColor(selectedColor);
		}
		if(listTxtview!=null&&listTxtview.size()>0){
			listTxtview.get(0).setTextColor(selectedColor);
		}
	}
	
	private void initTitle() {
		int size=listTitle.size();
		listTxtview=new ArrayList<TextView>();
		for (int i = 0 ; i < size ; i++) {
			RelativeLayout layout = new RelativeLayout(context);
			TextView view = new TextView(context);
			view.setEllipsize(TextUtils.TruncateAt.END);
			view.setText(listTitle.get(i)+"");
			view.setTextColor(Color.BLACK);
			listTxtview.add(view);
			RelativeLayout.LayoutParams params =  new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			layout.addView(view, params);
			layoutContent.addView(layout, (int)(screenW/itemCount + 0.5f), 60);
			layout.setOnClickListener(this);
			layout.setTag(i);
		}

	}

	private void initViewPager() {
		ComplexViewPagerAdapter adapter = new ComplexViewPagerAdapter(fm, pager, listFg);
//		MyFragmentPagerAdapter adapter=new MyFragmentPagerAdapter(fm,null,listFg);
		pager.setAdapter(adapter);
		pager.setCurrentItem(0);

		pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				int size=listTxtview.size();
				for(int i = 0 ; i < size ; i++){
					if(i==position){
						listTxtview.get(i).setTextColor(selectedColor);
					}else{
						listTxtview.get(i).setTextColor(unSelectedColor);
					}
				}
				Animation animation = new TranslateAnimation(endPosition, position * itemWidth, 0, 0);
				startPosition = position * itemWidth;
				currentFragmentIndex = position;
				if (animation != null) {
					animation.setFillAfter(true);
					animation.setDuration(0);
					imgLine.startAnimation(animation);
					horView.smoothScrollTo((currentFragmentIndex - 1) * itemWidth, 0);
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				if (!isScrollEnd) {
					if (currentFragmentIndex == position) {
						endPosition = itemWidth * currentFragmentIndex + (int) (itemWidth * positionOffset);
					}
					if (currentFragmentIndex == position + 1) {
						endPosition = itemWidth * currentFragmentIndex - (int) (itemWidth * (1 - positionOffset));
					}

					Animation mAnimation = new TranslateAnimation(startPosition, endPosition, 0, 0);
					mAnimation.setFillAfter(true);
					mAnimation.setDuration(0);
					imgLine.startAnimation(mAnimation);
					horView.invalidate();
					startPosition = endPosition;
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				if (state == ViewPager.SCROLL_STATE_DRAGGING) {
					isScrollEnd = false;
				} else if (state == ViewPager.SCROLL_STATE_SETTLING) {
					isScrollEnd = true;
					startPosition = currentFragmentIndex * itemWidth;
					if (pager.getCurrentItem() == currentFragmentIndex) {
						// 未跳入下一个页面
						imgLine.clearAnimation();
						Animation animation = null;
						// 恢复位置
						animation = new TranslateAnimation(endPosition, currentFragmentIndex * itemWidth, 0, 0);
						animation.setFillAfter(true);
						animation.setDuration(1);
						imgLine.startAnimation(animation);
						horView.invalidate();
						endPosition = currentFragmentIndex * itemWidth;
					}
				}
			}
		});

	}


	@Override
	public void onClick(View v) {
		pager.setCurrentItem((Integer)v.getTag());	
	}

}
