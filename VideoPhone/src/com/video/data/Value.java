package com.video.data;

public class Value {

	/**
	 * �豸����������
	 */
	public final static String DeviceBackstageName = "Backstage";
	
	/**
	 * ��������������
	 */
	public final static String AlarmBackstageName = "Alarmstage";
	
	/**
	 * ������ID��Port
	 */
	public final static String BackstageIPPort = "tcp://192.168.40.191:5555";
//	public final static String BackstageIPPort = "tcp://218.56.11.182:5555";
	
	/**
	 * ��ʱʱ��
	 */
	public final static int requestTimeout = 5000;
	
	/**
	 * �Ƿ���Ҫ���󱨾���¼�ı�־
	 */
	public static boolean isLoginSuccess = false;
	
	/**
	 * �Ƿ���Ҫ�����ն��б�ı�־
	 */
	public static boolean isNeedReqTermListFlag = true;
	
	/**
	 * �Ƿ���Ҫ���󱨾���¼�ı�־
	 */
	public static boolean isNeedReqAlarmListFlag = true;
	
	/**
	 * ���󱨾���¼������
	 */
	public static int requstAlarmCount = 5;
	
	/**
	 * ��ʼ��Ӧ�ó����ȫ�ֱ���
	 */
	public static void resetValues() {
		isLoginSuccess = false;
		isNeedReqTermListFlag = true;
		isNeedReqAlarmListFlag = true;
	}
}
