package cn.hnist.lib.android.hnistbook.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密
 * @author fox-life-one
 *
 */
public class MD5 {

	public static String getMD5(String val) throws NoSuchAlgorithmException{
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(val.getBytes());
		byte[] md5Bytes = md5.digest();
		return getString(md5Bytes);
	}
	
	private static String getString(byte[] bytes){
		StringBuffer mStringBuffer = new StringBuffer();
		for (byte b : bytes) {
			//每个byte转换为两位16进制数
			mStringBuffer.append(String.format("%02x", b&0xff));
		}
		return mStringBuffer.toString();
	}
}
