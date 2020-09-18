package com.ailink.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NumberUtil {
	/**
	 * 根据传进来的double值，以及保留的位数 截取一个特定位数的double值
	 * @param value  double值
	 * @param decimalCount 保留的位数
	 * @return  double
	 */
	public static double keepDecimalDouble(double value,int decimalCount){
		String sValue=""+value;
		double newValue=0.0;
		int index=sValue.indexOf(".");
		if(index+decimalCount+1<sValue.length()){
			newValue=Double.parseDouble(sValue.substring(0, index+decimalCount+1));
		}else{
			return value;
		}
		return newValue;		
	}
	/**
	 * 根据传进来的double值，以及保留的位数 截取一个特定位数的String值
	 * @param value  double值
	 * @param decimalCount 保留的位数
	 * @return  String 返回指定位数的String
	 */
	public static String keepDecimalString(double value,int decimalCount){
		String sValue=""+value;
		int index=sValue.indexOf(".");
		if(index+decimalCount+1<sValue.length()){
			return sValue.substring(0, index+decimalCount+1);
		}else{
			return sValue;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
		}	
	}


	/**
	 * 保留1位小数点
	 * @param value
	 * @return
	 */
	public static String keep1DecimalString(double value){
		DecimalFormat df = new DecimalFormat("0.0");
		String str=df.format(value);
		return str;
	}
	public static String keep2DecimalString(double value){
		DecimalFormat df = new DecimalFormat("0.00");
		String str=df.format(value);
		return str;
	}
	public static String keep3DecimalString(double value){
		DecimalFormat df = new DecimalFormat("0.000");
		String str=df.format(value);
		return str;
	}
	public static String keep4DecimalString(double value){
		DecimalFormat df = new DecimalFormat("0.0000");
		String str=df.format(value);
		return str;
	}



	/**
	 * 根据传进来的String类型的double值，以及保留的位数 截取一个特定位数的String值
	 * @param value  double值
	 * @param decimalCount 保留的位数
	 * @return  String 返回指定位数的String
	 */
	public static String keepDecimalString(String value,int decimalCount){
		String sValue=value;
		int index=sValue.indexOf(".");
		if(index+decimalCount+1<sValue.length()){
			return sValue.substring(0, index+decimalCount+1);
		}else{
			return sValue;
		}	
	}
	
	
	/**
	 * 将一个科学记数法的数字，转化成普通的数字去掉中间的E  如2E9  则会返回200000000
	 * -2.0E-4  则会返回0.0002
	 * @param value   需要处理的科学记数法的数值  
	 * @param decimalCount  需要保留的小数点位数
	 * @return String
	 */
	public static String parseENumberToNormal(double value,int decimalCount){
		String temp=value+"";
		if(!temp.contains("E")){
			return temp;
		}
		NumberFormat nf=NumberFormat.getInstance();
		nf.setMinimumFractionDigits(decimalCount);//设置最大的小数点的位数
		nf.setGroupingUsed(false);//设置之后，大数字就不会出现逗号间隔了，true的话就会出现1,200,544这种格式的数字
		String sValue=nf.format(value);
		return sValue;
	}
	
	
	
	/**
	 * 根据传进来的double值，以及保留的位数 截取一个特定位数的String值，此外如果大于10万的话，则直接除以1万  后面用万来表示
	 * @param value  double值
	 * @param decimalCount 保留的位数
	 * @return  String 返回指定位数的String
	 */
	public static String keepDecimalStringWan(double value,int decimalCount){
		String wan="";
		if(value>=100000||value<=-100000){
			value=value/10000f;
			wan="万";
		}
		String sValue=""+value;
		int index=sValue.indexOf(".");
		if(index+decimalCount+1<sValue.length()){
			return sValue.substring(0, index+decimalCount+1)+wan;
		}else{
			return sValue+wan;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
		}	
	}
	
	public static String keepDecimalStringWan(long value){
		String wan="";
		if(value>=100000||value<=-100000){
			value=value/10000;
			wan="万";
		}
		return value+wan; 	
	}
}
