package com.ailink.view.util;


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
	public interface IndexNumber{
		public static int Index_OutSide=0;
		public static int Index_1=1;
		public static int Index_2=2;
		public static int Index_3=3;
		public static int Index_4=4;
		public static int Index_5=5;
	}
	public interface OnIndexButtonListener{
		public void  onClickIndex(int buttonIndex);
	}

	/**
	 * 修改输入框内容之后的监听接口
	 * @author staker
	 */
	public interface OnEditEndListener{
		public void  onClickIndex(int buttonIndex, String editContent);
	}

//	/**
//	 * 默认创建一个  有修改和删除按钮的dialog，此时nameBtn01和nameBtn02可以传null进来
//	 * @param context 上下文
//	 * @param dialogTitle dialog的title
//	 * @param nameBtn01 第一个按钮的名字  默认为修改
//	 * @param nameBtn02 第二个按钮的名字  默认为删除
//	 * @param onClickListener 传进来的回调监听器
//	 */
//	public static void showDialogEdit2(Context context,String dialogTitle,String nameBtn01,String nameBtn02,final OnIndexButtonListener onClickListener) {
//		LayoutInflater inflater = LayoutInflater.from(context);
//		View v = inflater.inflate(R.layout.dialog_view_edit2, null);
//		TextView txtTile=null,txt01=null,txt02=null;
//		txtTile=(TextView) v.findViewById(R.id.txt_title);
//		txtTile.setText(dialogTitle);
//		final Dialog dialog = new Dialog(context, R.style.help_dialog);
//		if(onClickListener!=null){
//			txt01=(TextView) v.findViewById(R.id.txt_01);
//			if(nameBtn01!=null){
//				txt01.setText(nameBtn01);
//			}
//			txt01.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					onClickListener.onClickIndex(1);
//					if(dialog!=null&&dialog.isShowing()){
//						dialog.dismiss();
//					}
//				}
//			});
//			txt02=(TextView) v.findViewById(R.id.txt_02);
//			if(nameBtn02!=null){
//				txt02.setText(nameBtn02);
//			}
//			txt02.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					onClickListener.onClickIndex(2);
//					if(dialog!=null&&dialog.isShowing()){
//						dialog.dismiss();
//					}
//				}
//			});
//		}
//		dialog.setContentView(v);
//		dialog.setCanceledOnTouchOutside(true);
//		dialog.show();
//	}
//
	/**
	 * 弹出一个dialog  一个title列，和三个可以点击的TextView列
	 * @param context
	 * @param dialogTitle  dialog的标题
	 * @param nameBtn01    	 第1个textview的名字
	 * @param nameBtn02  	第2个textview的名字
	 * @param nameBtn03 	第3个textview的名字
	 * @param onClickListener
	 */
	public static void showDialogEditBtn3(Context context,String dialogTitle,String nameBtn01,String nameBtn02,
			String nameBtn03,final OnIndexButtonListener onClickListener) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialog_view_edit3, null);
		TextView txtTile=null,txt01=null,txt02=null,txt03=null;
		txtTile=(TextView) v.findViewById(R.id.txt_title);
		txtTile.setText(dialogTitle);
		final Dialog dialog = new Dialog(context, R.style.help_dialog);
		if(onClickListener!=null){
			txt01=(TextView) v.findViewById(R.id.txt_01);
			if(nameBtn01!=null){
				txt01.setText(nameBtn01);
			}
			txt01.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onClickListener.onClickIndex(1);
					if(dialog!=null&&dialog.isShowing()){
						dialog.dismiss();
					}
				}
			});
			txt02=(TextView) v.findViewById(R.id.txt_02);
			if(nameBtn02!=null){
				txt02.setText(nameBtn02);
			}
			txt02.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onClickListener.onClickIndex(2);
					if(dialog!=null&&dialog.isShowing()){
						dialog.dismiss();
					}
				}
			});
			txt03=(TextView) v.findViewById(R.id.txt_03);
			if(nameBtn03!=null){
				txt03.setText(nameBtn03);
			}
			txt03.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onClickListener.onClickIndex(3);
					if(dialog!=null&&dialog.isShowing()){
						dialog.dismiss();
					}
				}
			});
		}
		dialog.setContentView(v);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}



	/**
	 * 弹出一个普通的dialog样式，
	 * @param context  上下文
	 * @param content  dialog的内容
	 * @param btnName01  默认名字为取消
	 * @param btnName02 默认名字为确定
	 * @param onClickListener  监听器
	 */
	public static void showDialogNormal(Context context,String content,
			String btnName01,String btnName02,final OnIndexButtonListener onClickListener) {		
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialog_view_normal, null);
		TextView txtContent=null;
		Button btn01,btn02;
		txtContent=(TextView) v.findViewById(R.id.txt_content);
		if(txtContent!=null){
			txtContent.setText(content);
		}
		final Dialog dialog = new Dialog(context, R.style.help_dialog);	
		if(onClickListener!=null){						
			btn01=(Button) v.findViewById(R.id.btn_01);
			btn02=(Button) v.findViewById(R.id.btn_02);	
			if(btnName01!=null){
				btn01.setText(btnName01);
			}
			if(btnName02!=null){
				btn02.setText(btnName02);
			}
			btn01.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onClickListener.onClickIndex(1);
					if(dialog!=null&&dialog.isShowing()){
						dialog.dismiss();
					}
				}
			});		
			btn02.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onClickListener.onClickIndex(2);
					if(dialog!=null&&dialog.isShowing()){
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
//	/**
//	 * 弹出一个普通的提示dialog样式，无需监听，点击确定之后，则会把dialog消失
//	 * @param context  上下文
//	 * @param dialogTitle  dialog的title
//	 * @param content  dialog的内容
//	 * @param btnName 默认名字为确定
//	 */
//	public static void showDialogTips(Context context,String dialogTitle,String content,String btnName,final OnIndexButtonListener onClickListener) {
//		if(context==null){
//			return;
//		}
//		LayoutInflater inflater = LayoutInflater.from(context);
//		View v = inflater.inflate(R.layout.dialog_view_tips, null);
//		TextView txtTitle=null,txtContent=null;
//		txtTitle=(TextView) v.findViewById(R.id.txt_title);
//		if(dialogTitle!=null){
//			txtTitle.setText(dialogTitle);
//		}
//		txtContent=(TextView) v.findViewById(R.id.txt_content);
//		if(txtContent!=null){
//			txtContent.setText(content);
//		}
//		Button btnSure = (Button) v.findViewById(R.id.btn_sure);
//		if (btnName != null) {
//			btnSure.setText(btnName);
//		}
//		final Dialog dialog = new Dialog(context, R.style.help_dialog);
//		btnSure.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(onClickListener!=null){
//					onClickListener.onClickIndex(0);
//				}
//				if (dialog != null && dialog.isShowing()) {
//					dialog.dismiss();
//				}
//			}
//		});
//		dialog.setContentView(v);
//		dialog.setCanceledOnTouchOutside(true);
//		dialog.show();
//	}
	/**
	 * 弹出一个修改内容的对话框
	 * @param context  上下文
	 * @param dialogTitle  dialog的title
	 * @param editContent  dialog的默认的输入框内容
	 * @param btnName01  默认名字为取消
	 * @param btnName02 默认名字为确定
	 * @param listener  监听器
	 */
	public static void showDialogEditText(Context context,String dialogTitle,String editContent,
			String btnName01,String btnName02,final OnEditEndListener listener){

		LayoutInflater inflater = LayoutInflater.from(context);
		View v2 = inflater.inflate(R.layout.dialog_edit_content, null);

		Button btn01 = (Button) v2.findViewById(R.id.btn_01);
		Button btn02 = (Button) v2.findViewById(R.id.btn_02);
		TextView txtTitle=(TextView) v2.findViewById(R.id.txt_title);
		if(dialogTitle!=null){
			txtTitle.setText(dialogTitle);
		}

		final EditText edtText = (EditText) v2.findViewById(R.id.edt_content);
		if(editContent!=null){
			edtText.setText(editContent);
			edtText.setSelection(edtText.getText().length());
		}
		if(btnName01!=null){
			btn01.setText(btnName01);
		}
		if(btnName02!=null){
			btn02.setText(btnName02);
		}
		final Dialog dialog = new Dialog(context, R.style.help_dialog);
		if(listener!=null){
			btn01.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(dialog!=null&&dialog.isShowing()){
						dialog.dismiss();
					}
				}
			});
			btn02.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onClickIndex(2, edtText.getText().toString());
					if(dialog!=null&&dialog.isShowing()){
						dialog.dismiss();
					}
				}
			});
		}
		dialog.setContentView(v2);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}


}
