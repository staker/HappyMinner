package com.ailink.logic;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import juhuiwan.ailink.android.R;

public class DialogUtil {
	/**
	 * 专门用户接口点击之后用的
	 */
	public interface IndexNumber {
		public static int Index_OutSide = 0;
		public static int Index_1 = 1;
		public static int Index_2 = 2;
		public static int Index_3 = 3;
		public static int Index_4 = 4;
		public static int Index_5 = 5;
	}

	public interface OnIndexButtonListener {
		public void onClickIndex(int buttonIndex);
	}

	/**
	 * 修改输入框内容之后的监听接口
	 *
	 * @author staker
	 */
	public interface OnEditEndListener {
		public void onClickIndex(int buttonIndex, String editContent);
	}


	/**
	 * 弹出一个普通的dialog样式，
	 *
	 * @param context         上下文
	 * @param content         dialog的内容
	 * @param btnName01       默认名字为取消
	 * @param btnName02       默认名字为确定
	 * @param onClickListener 监听器
	 */
	public static void showDialogNormal(Context context, String content,String btnName01, String btnName02, final OnIndexButtonListener onClickListener) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialog_view_normal, null);
		TextView txtContent = null;
		Button btn01, btn02;
		txtContent = (TextView) v.findViewById(R.id.txt_content);
		if (txtContent != null) {
			txtContent.setText(content);
		}
		final Dialog dialog = new Dialog(context, R.style.help_dialog);
		if (onClickListener != null) {
			btn01 = (Button) v.findViewById(R.id.btn_01);
			btn02 = (Button) v.findViewById(R.id.btn_02);
			if (btnName01 != null) {
				btn01.setText(btnName01);
			}
			if (btnName02 != null) {
				btn02.setText(btnName02);
			}
			btn01.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onClickListener.onClickIndex(1);
					if (dialog != null && dialog.isShowing()) {
						dialog.dismiss();
					}
				}
			});
			btn02.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onClickListener.onClickIndex(2);
					if (dialog != null && dialog.isShowing()) {
						dialog.dismiss();
					}
				}
			});
		}
		dialog.setContentView(v);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				onClickListener.onClickIndex(3);
			}
		});
		dialog.show();
	}


	public static void showDialogTips(Context context,String title, String content,final OnIndexButtonListener onClickListener) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialog_view_tips, null);
		TextView txtContent = (TextView) v.findViewById(R.id.txt_content);
		if (txtContent != null) {
			txtContent.setText(content);
		}
		TextView txtTitle = (TextView) v.findViewById(R.id.txt_title);
		if (txtTitle != null) {
			txtTitle.setText(title);
		}
		Button btnSure=(Button) v.findViewById(R.id.btn_sure);

		final Dialog dialog = new Dialog(context, R.style.help_dialog);
		btnSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(onClickListener!=null){
					onClickListener.onClickIndex(1);
				}

				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
			}
		});
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				onClickListener.onClickIndex(3);
			}
		});
		dialog.setContentView(v);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}


















//	public static void showDialogFireMiner(Context context,final OnIndexButtonListener onClickListener) {
//		LayoutInflater inflater = LayoutInflater.from(context);
//		View v = inflater.inflate(R.layout.dialog_view_fire_miner, null);
//		Button btn01, btn02;
//		final Dialog dialog = new Dialog(context, R.style.help_dialog);
//		if (onClickListener != null) {
//			btn01 = (Button) v.findViewById(R.id.btn_01);
//			btn02 = (Button) v.findViewById(R.id.btn_02);
//			btn01.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					onClickListener.onClickIndex(1);
//					if (dialog != null && dialog.isShowing()) {
//						dialog.dismiss();
//					}
//				}
//			});
//			btn02.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					onClickListener.onClickIndex(2);
//					if (dialog != null && dialog.isShowing()) {
//						dialog.dismiss();
//					}
//				}
//			});
//		}
//		dialog.setContentView(v);
//		dialog.setCanceledOnTouchOutside(true);
//		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//			@Override
//			public void onCancel(DialogInterface dialog) {
//				onClickListener.onClickIndex(3);
//			}
//		});
//		dialog.show();
//	}


















































}
