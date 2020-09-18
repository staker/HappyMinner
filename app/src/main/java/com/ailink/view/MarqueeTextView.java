package com.ailink.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 *
 *
 * txtNotice.startScroll(); 需要在代码里面手动调用开启
 * txtNotice.stopScroll();需要在代码里面手动调用关闭
 */
public class MarqueeTextView extends AppCompatTextView implements Runnable {
	private int currentScrollX;
	private boolean isStop = false;
	private int textWidth;
	private boolean isMeasure = false;

	public MarqueeTextView(Context context) {
		super(context);
	}

	public MarqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		currentScrollX = this.getWidth();
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (!isMeasure) {
			getTextWidth();// ���ֿ��ֻ��Ҫ��ȡһ�ξͿ�����
			isMeasure = true;
		}
	}

	private void getTextWidth() {
		Paint paint = this.getPaint();
		String str = this.getText().toString();
		textWidth = (int) paint.measureText(str);
	}

	@Override
	/*
	 * public void run() { currentScrollX-=2;//�����ٶ�.+�ű�ʾ�����-
	 * scrollTo(currentScrollX,0); if(isStop){ return; }
	 * if(getScrollX()<=-(this.getWidth())){ scrollTo(textWidth,0);
	 * currentScrollX=textWidth; } postDelayed(this, 5); }
	 */
	public void run() {
		currentScrollX += 2;// �����ٶ�.+�ű�ʾ�����-
		scrollTo(currentScrollX, 0);
		if (isStop) {
			return;
		}
		if (getScrollX() >= (textWidth)) {
			currentScrollX = -(this.getWidth());// ��ǰ���ֵ�λ��
		}
		postDelayed(this, 1);
	}

	// ��ʼ����
	public void startScroll() {
		isStop = false;
		this.removeCallbacks(this);
		post(this);
	}

	// ֹͣ����
	public void stopScroll() {
		isStop = true;
	}

	// ��ͷ��ʼ����
	public void startFromHead() {
		currentScrollX = 0;
		startScroll();
	}

}
