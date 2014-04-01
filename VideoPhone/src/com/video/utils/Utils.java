package com.video.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

public class Utils {
	
	public static int screenWidth;
	public static int screenHeight;
	private static final char HEX_DIGITS[] = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'}; 
	
	
	/**
	 * 连接网络异常时获得异常原因
	 * @param num 错误码
	 * @return 返回异常原因的字符串
	 */
	public static String getErrorReason(int num) {
		String result = "";
		switch (num) {
			case 1:
				result = "Block!";
				break;
			case 2:
				result = "账号或密码错误！";
				break;
			case 3:
				result = "服务器端查询数据库时错误！";
				break;
			case 4:
				result = "账号已存在！";
				break;
			case 5:
				result = "账号不存在！";
				break;
			case 6:
				result = "该设备已被绑定！";
				break;
			case 7:
				result = "账号和邮箱不匹配！";
				break;
			default: 
				result = "不明原因！";
				break;
		}
		return result;
	}
	
	/**
	 * 判断网络连接是否可用
	 * @param context 上下文
	 * @return true:可用  false:不可用
	 */
	public static boolean isNetworkAvailable(Context context) {   
		ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);   
		if (connectivity == null) {    
			return false;   
		} else {   
			NetworkInfo[] info = connectivity.getAllNetworkInfo();   
			if (info != null) {   
				for (int i = 0; i < info.length; i++) {   
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;   
					}   
				}   
			}   
		}   
		return false;   
	}
	
	/**
	 * 转换字符串的编码格式
	 * @param src 字符串
	 * @param oldEncode 原编码格式
	 * @param newEncode 新编码格式
	 * @return 返回新编码格式的字符串
	 */
	public static String changeEncodeing(String src, String oldEncode, String newEncode) {
		if (src != null) {
			byte[] byteArray = null;
			try {
				byteArray = src.getBytes();
				return new String(byteArray, newEncode);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 将字节数组转化为Hex字符串
	 * @param src 字节数组
	 * @return 返回字符串
	 */
	public static String BytesToHexString(byte[] src) {  
		StringBuilder sb = new StringBuilder(src.length * 2);  
		
		for (int i=0; i<src.length; i++) {    
			sb.append(HEX_DIGITS[(src[i] & 0xF0) >>> 4]);    
			sb.append(HEX_DIGITS[src[i] & 0x0F]);    
		}    
		return sb.toString();    
	}
	
	/**
	 * 生成32位随机的UUID
	 * @return 返回不带"-"的32位UUID字符串
	 */
	public static String getRandomUUID(){ 
        String s = UUID.randomUUID().toString(); 
        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
    }
	
	/**
	 * 生成MD5的加密密码
	 * @param pwd 密码
	 * @return 返回加密密码
	 */
	public static String CreateMD5Pwd(String pwd) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(pwd.getBytes());
			
			byte messageDigest[] = digest.digest();
			return BytesToHexString(messageDigest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 判断SD卡是否存在
	 * @return 存在返回true 不存在返回false
	 */
	public static boolean checkSDCard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			return true;
		return false;
	}
	
	/**
	 * 判断是否为图片格式的文件
	 * @param fileName 文件
	 * @return true:是 false:不是
	 */
	public static boolean isImageFile(String fileName) {
	    String end = fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()).toLowerCase(); 
	    
	    if(end.equals("jpg")||end.equals("png")||end.equals("jpeg")||end.equals("bmp")||
	       end.equals("JPG")||end.equals("PNG")||end.equals("JPEG")||end.equals("BMP")) {
	    	return true;
	    }
	    return false; 
	}
	
	/**
	 * 将字符串转换成日期类型
	 * @param date_string 日期字符串
	 * @param format_string 要转换的日期格式
	 * @return 返回日期类型数据
	 */
	public static Date StringToDate(String date_string, String format_string) {

		SimpleDateFormat dateFormat = new SimpleDateFormat(format_string);
		Date date = null;
		try {
			date = dateFormat.parse(date_string);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 将日期类型数据转换成字符串类型
	 * @param date 日期类型数据
	 * @param format_string 要转换的日期格式
	 * @return 返回字符串类型的日期数据
	 */
	public static String DateToString(Date date, String format_string) {

		SimpleDateFormat dateFormat = new SimpleDateFormat(format_string);
		String date_string = dateFormat.format(date);
		return date_string;
	}
	
	/**
	 * 获得当前的时间
	 * @param format_string 要获得日期的格式
	 * @return 返回当前时间
	 */
	public static String getNowTime(String format_string){
	    return DateToString(new Date(), format_string);
	}
	
	/**
	 * 冒泡排序从大到小
	 * @param intArray 排序前的整型字符串
	 * @return 返回排序后的整型字符串
	 */
	public static int[] bubbleSortIntArray(int[] intArray) {
		for (int i = 0; i < intArray.length - 1; i++) {
			for (int j = i + 1; j < intArray.length; j++) {
				int temp;
				if (intArray[i] < intArray[j]) {
					temp = intArray[j];
					intArray[j] = intArray[i];
					intArray[i] = temp;
				}
			}
		}
		return intArray;
	}
	
	/**
	 * ArrayList集合冒泡排序从大到小
	 * @param arrayList 排序前的ArrayList集合
	 * @return 返回排序后的ArrayList集合
	 */
	public static ArrayList<Integer> bubbleSortArrayList(ArrayList<Integer> arrayList) {
		int size = arrayList.size();
		
		for (int i=0; i<size-1; i++) {
			for (int j=1; j< size-i; j++) {
				Integer temp;
				if ((arrayList.get(j-1)).compareTo(arrayList.get(j)) < 0) {
					temp = arrayList.get(j - 1);
					arrayList.set((j-1), arrayList.get(j));
					arrayList.set(j, temp);
				}
			}
		}
		return arrayList;
	}
	
	/**
	 * 牺牲图片的质量防止加载的图片OOM(内存溢出)
	 */
	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
		int roundedSize;
		
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}
	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
}
