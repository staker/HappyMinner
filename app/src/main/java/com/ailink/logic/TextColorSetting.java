package com.ailink.logic;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

import com.ailink.util.Logg;

public class TextColorSetting {


	/**
	 * 给两个String设置颜色
	 * @param string01
	 * @param string02
	 */
	public static void addTextColor2String(TextView txt,String string01, String string02, int color01,int color02) {
		if(string01==null||string02==null){
			return;
		}
		SpannableString ss = new SpannableString(string01+string02);
		// 可以设置字体前两个字符的颜色
		ss.setSpan(new ForegroundColorSpan(color01), 0,string01.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss.setSpan(new ForegroundColorSpan(color02), string01.length(),string01.length()+string02.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		txt.setText(ss);
	}

	/**
	 * 给两个String设置颜色
	 * @param string01
	 * @param string02
	 */
	public static void addTextColor2String(TextView txt,String string01, String string02, String color01,String color02) {
		if(string01==null||string02==null){
			return;
		}
		SpannableString ss = new SpannableString(string01+string02);
		// 可以设置字体前两个字符的颜色
		ss.setSpan(new ForegroundColorSpan(Color.parseColor(color01)), 0,string01.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss.setSpan(new ForegroundColorSpan(Color.parseColor(color02)), string01.length(),string01.length()+string02.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		txt.setText(ss);
	}



	/**
	 *给3个String设置颜色
	 */
	public static void addTextColor3String(TextView txt,String string01, String string02,String string03
			, int color01,int color02,int color03) {
		if(string01==null||string02==null||string03==null){
			return;
		}
		SpannableString ss = new SpannableString(string01+string02+string03);

		// 可以设置字体前两个字符的颜色
		ss.setSpan(new ForegroundColorSpan(color01), 0,string01.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss.setSpan(new ForegroundColorSpan(color02), string01.length(),string01.length()+string02.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss.setSpan(new ForegroundColorSpan(color03), string01.length()+string02.length(),string01.length()+string02.length()+string03.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		txt.setText(ss);
	}




	public static void addTextColorGreen4(String string, TextView v) {
		SpannableString ss = new SpannableString(string);

		
		ss.setSpan(new ForegroundColorSpan(Color.argb(255, 20, 150, 20)), 0, 4,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		v.setText(ss);

	}
	
	
	
	/**
	 * 设置字符串最后几个字符的颜色为绿色
	 * @param string 字符串
	 * @param endCount 最后需要设置颜色的字符个数
	 * @param v TextView
	 */
	public static void addTextColorGreenEnd(String string,int endCount, TextView v) {
		if(string==null){
			return;
		}
		SpannableString ss = new SpannableString(string);
		int length=string.length();
		if(length-endCount<0){
			return;
		}
		// 可以设置字体后两个字符的颜色
		ss.setSpan(new ForegroundColorSpan(Color.argb(255, 20, 150, 20)), length-endCount, length,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		v.setText(ss);
	}
	/**
	 * 设置字符串最后几个字符的颜色为红色
	 * @param string 字符串
	 * @param endCount 最后需要设置颜色的字符个数
	 * @param v TextView
	 */
	public static void addTextColorRedEnd(String string, int endCount,TextView v) {
		if(string==null){
			return;
		}
		SpannableString ss = new SpannableString(string);
		int length=string.length();
		if(length-endCount<0){
			return;
		}
		// 可以设置字体后两个字符的颜色
		ss.setSpan(new ForegroundColorSpan(Color.argb(255, 150, 20, 20)), length-endCount, length,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		v.setText(ss);
	}

	/**
	 * 如果字符串中包含url连接，则给连接加上颜色，并且可以点击
	 *  url的格式必须以http://开头，此外结尾处必须有空格，否则就无法正常解析
	 * @param s
	 *            字符串
	 * @param v
	 *            需要设置的textview
	 */
	public static void addUrlAutoLink(String s, TextView v) {

		int length = s.length();
		// 这里面代表的回调的Activity，设置在娜段文字上面，那么哪段文字被点击之后就会跳转到这个Activity
		SpannableString ss = new SpannableString(s);
		for (int i = 0; i < length; i++) {
			if (s.charAt(i) == 'h') {
				try {
					if (s.substring(i, i + 7).equals("http://")) {

						StringBuffer url = new StringBuffer();
						url.append(s.substring(i, i + 7));
						int count = 7;
						for (int j = i + 1; j < length; j++) {
							if (s.charAt(j) != ' ') {
								url.append(s.charAt(j));
								count++;
							} else {
								break;
							}
						}
						ss.setSpan(new URLSpan(url.toString()), i, i + count,
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		}
		v.setText(ss);
		v.setMovementMethod(LinkMovementMethod.getInstance());

	}
}
