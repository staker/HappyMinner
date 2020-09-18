package com.ailink.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5算法工具
 * 
 * @since 2009-8-7 14:46:27
 */

public class MD5Util {

	/**
	 * MD5单向加密，32位
	 * 
	 * @param str
	 * @return String加密后点字符串
	 */
	public static String md5String(String str) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException ne) {
			Logg.e("NoSuchAlgorithmException: md5", ne);
			return "";

		}
		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];
		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

}
