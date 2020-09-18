package com.ailink.util;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtil {

	public static boolean isurl(String url)
	{
		return (!TextUtils.isEmpty(url)&&url.length()>5);
	}

    public static String[] ENGLIST_LETTER = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

	public static boolean isBlank(String str) {
		if (str == null || "".equals(str)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	public static boolean countStringLength(String content, int stringNum) {
		int result = 0;
		if (content != null && !"".equals(content)) {
			char[] contentArr = content.toCharArray();
			if (contentArr != null) {
				for (int i = 0; i < contentArr.length; i++) {
					char c = contentArr[i];
					if (isChinese(c)) {
						result += 3;
					} else {
						result += 1;
					}
				}
			}
		}
		if (result > stringNum * 3) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNum(String num) {
		String check = "^(\\+|-)?[0-9]+(\\.[0-9]+)?$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(num);
		boolean isMatched = matcher.matches();
		return isMatched;
	}
	
	public static boolean isPhone(String Phone) {
		String check = "^0\\d{2,3}-\\d{5,9}|0\\d{2,3}-\\d{5,9}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(Phone);
		boolean isMatched = matcher.matches();
		return isMatched;
	}

	public static String DoubleToAmountString(Double doubleNum, int digitNum) {
		if (digitNum < 0)
			digitNum = 0;
		StringBuilder strBuilder = new StringBuilder("#");
		for (int i = 0; i < digitNum; i++) {
			if (i == 0)
				strBuilder.append(".#");
			else
				strBuilder.append("#");
		}
		DecimalFormat df = new DecimalFormat(strBuilder.toString());
		return df.format(doubleNum);
	}

	public static String getInitialAlphaEn(String str) {
		if (str == null) {
			return "#";
		}

		if (str.trim().length() == 0) {
			return "#";
		}

		char c = str.trim().substring(0, 1).charAt(0);
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase(Locale.getDefault());
		} else {
			return "#";
		}
	}

	public static String removeAllChar(String orgStr, String splitStr) {
		String[] strArray = orgStr.split(splitStr);
		String res = "";
		for (String tmp : strArray) {
			res += tmp;
		}
		return res;
	}

	public static String getEditText(EditText text) {
		if (null==text||text.getText().toString().trim().equals("")) {
			return "";
		}
		return text.getText().toString().trim();
	}

	public static String getMd5Value(String sSecret) {
		try {
			MessageDigest bmd5 = MessageDigest.getInstance("MD5");
			bmd5.update(sSecret.getBytes());
			int i;
			StringBuffer buf = new StringBuffer();
			byte[] b = bmd5.digest();
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static SpannableStringBuilder changStringColor(String content,
			int start, int end,int color) {

		SpannableStringBuilder builder = new SpannableStringBuilder(content);
		ForegroundColorSpan redSpan = new ForegroundColorSpan(color);
		builder.setSpan(redSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return builder;
	}
	
	public static SpannableStringBuilder changStringColor(String content,
			int[] start, int[] end,int color) {
		SpannableStringBuilder builder = new SpannableStringBuilder(content);
		for (int i = 0; i < end.length; i++) {
			ForegroundColorSpan redSpan = new ForegroundColorSpan(color);
			builder.setSpan(redSpan, start[i], end[i], Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		
		return builder;
	}

    public static String formBankCard(String input){  
        String result=input.replaceAll("([\\d]{4})(?=\\d)", "$1 ");  
        return result;  
    }  

    public static String test_formatFileSize(float size){  
    	NumberFormat df1 =  new DecimalFormat("##.#");
        String result= df1.format(size);
        return result;
    } 
    public static String format_number(String str){
    	if(str==null)
    	{
    		return "0.00";
    	}
    	int n=str.lastIndexOf(".");
    	if(n==-1)
    	{
    		str+=".00";
    	}
    	else
    	{
    		if(str.length()-n<=2)
    		{
	    		int len=str.length()-n-1;
	    		for (int i = 0; i < len; i++) {
					str+="0";
				}
    		}
    	}
    	return str;
    }
    
    static String[] strs={"","一","二","三","四","五","六","七","八","九","十"};
    public static String int2str(int ii) { 
    	if(ii>10)
    	{
    		return null;
    	}
    	return strs[ii];
    }
    public static String lengthformat (float number)
    {
    	
    	if(number>1000)
    	{
    		return test_formatFileSize(number/1000)+"千米";
    	}
    	else
    	{
    		return test_formatFileSize(number)+"米";
    	}
    	
    }
    public static String GetEdittextValue(EditText editText)
    {
    	if(editText==null)
    	{
    		return null;
    	}
    	return editText.getText().toString().trim();
    }
    public static String gettext(EditText et)
    {
    	if(et==null)
    	{
    		return null;
    	}
    	else
    	{
    		return et.getText().toString().trim();
    	}
    }

	public static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,17}$";

	public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";

	public static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

	public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

	public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],*$";

	public static final String REGEX_ID_CARD = "(^\\d{17}(\\d|X)$)|(^\\d{15}$)";

	public static final String REGEX_URL = "^(http|www|ftp|https)?(://)?(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*((:\\d+)?)(/(\\w+(-\\w+)*))*(\\.?(\\w)*)(\\?)?(((\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*(\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*)*(\\w*)*)$";

	public static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";

	public static boolean isUsername(String username) {
		return Pattern.matches(REGEX_USERNAME, username);
	}

	public static boolean isPassword(String password) {
		return Pattern.matches(REGEX_PASSWORD, password);
	}

	public static boolean isMobile(String mobile) {
		return Pattern.matches(REGEX_MOBILE, mobile);
	}

	public static boolean isEmail(String email) {
		return Pattern.matches(REGEX_EMAIL, email);
	}

	public static boolean isChinese(String chinese) {
		return Pattern.matches(REGEX_CHINESE, chinese);
	}

	public static boolean isIDCard(String idCard) {
		return new IdcardValidator().isValidatedAllIdcard(idCard);
	}

	public static boolean isUrl(String url) {
		if(url==null)
		{
			return false;
		}
		return Pattern.matches(REGEX_URL, url);
	}

	public static boolean isIPAddr(String ipAddr) {
		return Pattern.matches(REGEX_IP_ADDR, ipAddr);
	}
	
	public static String urlDecode(String str)
	{
		try {
			str=str.trim();
			return URLDecoder.decode(str.replaceAll("%(?![0-9a-fA-F]{2})", "%25") ,"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String urlEncode(String str)
	{
		try {
			return URLEncoder.encode(str,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "unused", "all" })
	private static class IdcardValidator {

		protected String codeAndCity[][] = { { "11", "北京" }, { "12", "天津" },
				{ "13", "河北" }, { "14", "山西" }, { "15", "内蒙古" }, { "21", "辽宁" },
				{ "22", "吉林" }, { "23", "黑龙江" }, { "31", "上海" }, { "32", "江苏" },
				{ "33", "浙江" }, { "34", "安徽" }, { "35", "福建" }, { "36", "江西" },
				{ "37", "山东" }, { "41", "河南" }, { "42", "湖北" }, { "43", "湖南" },
				{ "44", "广东" }, { "45", "广西" }, { "46", "海南" }, { "50", "重庆" },
				{ "51", "四川" }, { "52", "贵州" }, { "53", "云南" }, { "54", "西藏" },
				{ "61", "陕西" }, { "62", "甘肃" }, { "63", "青海" }, { "64", "宁夏" },
				{ "65", "新疆" }, { "71", "台湾" }, { "81", "香港" }, { "82", "澳门" },
				{ "91", "国外" } };

		private String cityCode[] = { "11", "12", "13", "14", "15", "21", "22",
				"23", "31", "32", "33", "34", "35", "36", "37", "41", "42", "43",
				"44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63",
				"64", "65", "71", "81", "82", "91" };

		private int power[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };

		private String verifyCode[] = { "1", "0", "X", "9", "8", "7", "6", "5",
				"4", "3", "2" };

		public boolean isValidatedAllIdcard(String idcard) {
			if (idcard.length() == 15) {
				idcard = this.convertIdcarBy15bit(idcard);
			}
			return this.isValidate18Idcard(idcard);
		}

		public boolean isValidate18Idcard(String idcard) {
			if (idcard.length() != 18) {
				return false;
			}
			String idcard17 = idcard.substring(0, 17);
			String idcard18Code = idcard.substring(17, 18);
			char c[] = null;
			String checkCode = "";
			if (isDigital(idcard17)) {
				c = idcard17.toCharArray();
			} else {
				return false;
			}

			if (null != c) {
				int bit[] = new int[idcard17.length()];

				bit = converCharToInt(c);

				int sum17 = 0;

				sum17 = getPowerSum(bit);

				checkCode = getCheckCodeBySum(sum17);
				if (null == checkCode) {
					return false;
				}
				if (!idcard18Code.equalsIgnoreCase(checkCode)) {
					return false;
				}
			}
			return true;
		}

		public boolean isValidate15Idcard(String idcard) {
			if (idcard.length() != 15) {
				return false;
			}

			if (isDigital(idcard)) {
				String provinceid = idcard.substring(0, 2);
				String birthday = idcard.substring(6, 12);
				int year = Integer.parseInt(idcard.substring(6, 8));
				int month = Integer.parseInt(idcard.substring(8, 10));
				int day = Integer.parseInt(idcard.substring(10, 12));

				boolean flag = false;
				for (String id : cityCode) {
					if (id.equals(provinceid)) {
						flag = true;
						break;
					}
				}
				if (!flag) {
					return false;
				}
				Date birthdate = null;
				try {
					birthdate = new SimpleDateFormat("yyMMdd").parse(birthday);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (birthdate == null || new Date().before(birthdate)) {
					return false;
				}

				GregorianCalendar curDay = new GregorianCalendar();
				int curYear = curDay.get(Calendar.YEAR);
				int year2bit = Integer.parseInt(String.valueOf(curYear)
						.substring(2));

				if ((year < 50 && year > year2bit)) {
					return false;
				}

				if (month < 1 || month > 12) {
					return false;
				}

				boolean mflag = false;
				curDay.setTime(birthdate);
				switch (month) {
				case 1:
				case 3:
				case 5:
				case 7:
				case 8:
				case 10:
				case 12:
					mflag = (day >= 1 && day <= 31);
					break;
				case 2:
					if (curDay.isLeapYear(curDay.get(Calendar.YEAR))) {
						mflag = (day >= 1 && day <= 29);
					} else {
						mflag = (day >= 1 && day <= 28);
					}
					break;
				case 4:
				case 6:
				case 9:
				case 11:
					mflag = (day >= 1 && day <= 30);
					break;
				}
				if (!mflag) {
					return false;
				}
			} else {
				return false;
			}
			return true;
		}

		public String convertIdcarBy15bit(String idcard) {
			String idcard17 = null;
			if (idcard.length() != 15) {
				return null;
			}

			if (isDigital(idcard)) {
				String birthday = idcard.substring(6, 12);
				Date birthdate = null;
				try {
					birthdate = new SimpleDateFormat("yyMMdd").parse(birthday);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Calendar cday = Calendar.getInstance();
				cday.setTime(birthdate);
				String year = String.valueOf(cday.get(Calendar.YEAR));

				idcard17 = idcard.substring(0, 6) + year + idcard.substring(8);

				char c[] = idcard17.toCharArray();
				String checkCode = "";

				if (null != c) {
					int bit[] = new int[idcard17.length()];

					bit = converCharToInt(c);
					int sum17 = 0;
					sum17 = getPowerSum(bit);

					checkCode = getCheckCodeBySum(sum17);
					if (null == checkCode) {
						return null;
					}

					idcard17 += checkCode;
				}
			} else {
				return null;
			}
			return idcard17;
		}

		public boolean isIdcard(String idcard) {
			return idcard == null || "".equals(idcard) ? false : Pattern.matches(
					"(^\\d{15}$)|(\\d{17}(?:\\d|x|X)$)", idcard);
		}

		public boolean is15Idcard(String idcard) {
			return idcard == null || "".equals(idcard) ? false : Pattern.matches(
					"^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$",
					idcard);
		}

		public boolean is18Idcard(String idcard) {
			return Pattern
					.matches(
							"^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([\\d|x|X]{1})$",
							idcard);
		}

		public boolean isDigital(String str) {
			return str == null || "".equals(str) ? false : str.matches("^[0-9]*$");
		}

		public int getPowerSum(int[] bit) {

			int sum = 0;

			if (power.length != bit.length) {
				return sum;
			}

			for (int i = 0; i < bit.length; i++) {
				for (int j = 0; j < power.length; j++) {
					if (i == j) {
						sum = sum + bit[i] * power[j];
					}
				}
			}
			return sum;
		}

		public String getCheckCodeBySum(int sum17) {
			String checkCode = null;
			switch (sum17 % 11) {
			case 10:
				checkCode = "2";
				break;
			case 9:
				checkCode = "3";
				break;
			case 8:
				checkCode = "4";
				break;
			case 7:
				checkCode = "5";
				break;
			case 6:
				checkCode = "6";
				break;
			case 5:
				checkCode = "7";
				break;
			case 4:
				checkCode = "8";
				break;
			case 3:
				checkCode = "9";
				break;
			case 2:
				checkCode = "x";
				break;
			case 1:
				checkCode = "0";
				break;
			case 0:
				checkCode = "1";
				break;
			}
			return checkCode;
		}

		public int[] converCharToInt(char[] c) throws NumberFormatException {
			int[] a = new int[c.length];
			int k = 0;
			for (char temp : c) {
				a[k++] = Integer.parseInt(String.valueOf(temp));
			}
			return a;
		}

		public static void main(String[] args) throws Exception {
			
			String idcard15 = "142431199001145";//15位
			String idcard18 = "121212121212121212";//18位
			IdcardValidator iv = new IdcardValidator();
			System.out.println(iv.isValidatedAllIdcard(idcard15));
			System.out.println(iv.isValidatedAllIdcard(idcard18));
		}
	}

	public static String trim(String str){
		String value = new String(str);
		value.trim();
		while (value.endsWith("/n") || str.startsWith("/n")){
			if(value.endsWith("/n")){
				value.substring(0,str.length()-2);
			}else{
				value.substring(2,str.length());
			}
		}
		return value;
	}
}