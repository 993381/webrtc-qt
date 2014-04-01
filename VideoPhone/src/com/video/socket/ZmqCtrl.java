package com.video.socket;

public class ZmqCtrl {
	
	private static ZmqCtrl zmqCtrlInstance = null;
	private ZmqSocket zmqSocketInstance = null;
	
	/**
	 * ʵ����ZmqCtrl�ĵ�������
	 */
	synchronized public static ZmqCtrl getInstance() {
		if (zmqCtrlInstance == null) {
			zmqCtrlInstance = new ZmqCtrl();
			System.out.println("MyDebug: ��ʵ����ZmqCtrl����");
		}
		return zmqCtrlInstance;
	}
	
	/**
	 * ��ʼ��ZMQ, ��������߳�
	 */
	public void init() {
		if (zmqSocketInstance == null) {
			zmqSocketInstance = new ZmqSocket();
		}
	}
	
	/**
	 * �ر�ZMQ���Ӿ��
	 */
	public void exit() {
		if (zmqSocketInstance != null) {
			zmqSocketInstance.zmqExit();
			zmqSocketInstance = null;
		}
		if (zmqCtrlInstance != null) {
			zmqCtrlInstance = null;
		}
	}
}
