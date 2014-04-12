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
	 * ���������쳣ʱ����쳣ԭ��
	 * @param num ������
	 * @return �����쳣ԭ����ַ���
	 */
	public static String getErrorReason(int num) {
		String result = "";
		switch (num) {
			case 1:
				result = "Block!";
				break;
			case 2:
				result = "�˺Ż��������";
				break;
			case 3:
				result = "�������˲�ѯ���ݿ�ʱ����";
				break;
			case 4:
				result = "�˺��Ѵ��ڣ�";
				break;
			case 5:
				result = "�˺Ų����ڣ�";
				break;
			case 6:
				result = "���豸�ѱ��󶨣�";
				break;
			case 7:
				result = "�˺ź����䲻ƥ�䣡";
				break;
			case 9:
				result = "δ��¼�����豸�б�";
				break;
			default: 
				result = "����ԭ��";
				break;
		}
		return result;
	}
	
	/**
	 * �ж����������Ƿ����
	 * @param context ������
	 * @return true:����  false:������
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
	 * ת���ַ����ı����ʽ
	 * @param src �ַ���
	 * @param oldEncode ԭ�����ʽ
	 * @param newEncode �±����ʽ
	 * @return �����±����ʽ���ַ���
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
	 * ���ֽ�����ת��ΪHex�ַ���
	 * @param src �ֽ�����
	 * @return �����ַ���
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
	 * ����32λ�����UUID
	 * @return ���ز���"-"��32λUUID�ַ���
	 */
	public static String getRandomUUID(){ 
        String s = UUID.randomUUID().toString(); 
        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
    }
	
	/**
	 * ����MD5�ļ�������
	 * @param pwd ����
	 * @return ���ؼ�������
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
	 * �ж�SD���Ƿ����
	 * @return ���ڷ���true �����ڷ���false
	 */
	public static boolean checkSDCard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			return true;
		return false;
	}
	
	/**
	 * �ж��Ƿ�ΪͼƬ��ʽ���ļ�
	 * @param fileName �ļ�
	 * @return true:�� false:����
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
	 * ���ַ���ת������������
	 * @param date_string �����ַ���
	 * @param format_string Ҫת�������ڸ�ʽ
	 * @return ����������������
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
	 * ��������������ת�����ַ�������
	 * @param date ������������
	 * @param format_string Ҫת�������ڸ�ʽ
	 * @return �����ַ������͵���������
	 */
	public static String DateToString(Date date, String format_string) {

		SimpleDateFormat dateFormat = new SimpleDateFormat(format_string);
		String date_string = dateFormat.format(date);
		return date_string;
	}
	
	/**
	 * ��õ�ǰ��ʱ��
	 * @param format_string Ҫ������ڵĸ�ʽ
	 * @return ���ص�ǰʱ��
	 */
	public static String getNowTime(String format_string){
	    return DateToString(new Date(), format_string);
	}
	
	/**
	 * ð������Ӵ�С
	 * @param intArray ����ǰ�������ַ���
	 * @return ���������������ַ���
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
	 * ArrayList����ð������Ӵ�С
	 * @param arrayList ����ǰ��ArrayList����
	 * @return ����������ArrayList����
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
	 * ����ͼƬ��������ֹ���ص�ͼƬOOM(�ڴ����)
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
