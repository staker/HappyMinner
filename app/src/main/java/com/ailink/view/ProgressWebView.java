package com.ailink.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ailink.util.UIUtil;

import juhuiwan.ailink.android.R;

@SuppressWarnings("deprecation")
public class ProgressWebView extends WebView {
	private ProgressBar progressbar;
	private OnUrlLoadListener onUrlLoadListener;

	public ProgressWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		progressbar = new ProgressBar(context, null,
				android.R.attr.progressBarStyleHorizontal);
		progressbar.setProgressDrawable(getResources().getDrawable(R.drawable.webview_progress));
		progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				UIUtil.dp2px(context, 3), 0, 0));
		addView(progressbar);
		// setWebViewClient(new WebViewClient(){});
		setWebChromeClient(new WebChromeClient());
		setWebViewClient(new WebViewClient());
	}
	
	public ProgressWebView(Context context) {
		this(context,null);
	}


	public class WebChromeClient extends android.webkit.WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			setProgress(newProgress);
			super.onProgressChanged(view, newProgress);
		}

		@Override
		public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
			return super.onJsAlert(view, url, message, result);
		}

		@Override
		public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
			return super.onJsConfirm(view, url, message, result);
		}

		@Override
		public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
			return super.onJsPrompt(view, url, message, defaultValue, result);
		}

	}

	public OnUrlLoadListener getOnUrlLoadListener() {
		return onUrlLoadListener;
	}

	public void setOnUrlLoadListener(OnUrlLoadListener onUrlLoadListener) {
		this.onUrlLoadListener = onUrlLoadListener;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
		lp.x = l;
		lp.y = t;
		progressbar.setLayoutParams(lp);
		super.onScrollChanged(l, t, oldl, oldt);
	}

	public interface OnUrlLoadListener {
		void onStart();

		void onComplate();

		void onLoading(int Progress);

		void onFailure();
	}

	public void setProgress(int newProgress){
		if(progressbar != null){
			if (newProgress == 100) {
				progressbar.setVisibility(GONE);
				if (onUrlLoadListener != null) {
					onUrlLoadListener.onComplate();
				}
			} else {
				if (progressbar.getVisibility() == GONE)
					progressbar.setVisibility(VISIBLE);
				progressbar.setProgress(newProgress);
			}
			if (newProgress == 0) {
				if (onUrlLoadListener != null) {
					onUrlLoadListener.onStart();
				}
			}
			if (onUrlLoadListener != null) {
				onUrlLoadListener.onLoading(newProgress);
			}
		}
	}
}
