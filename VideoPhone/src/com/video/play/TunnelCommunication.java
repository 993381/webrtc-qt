package com.video.play;

public class TunnelCommunication {

	//�������ؽӿ�
	private static native int naInitialize(String classPath);
	private static native int naTerminate();
	private static native int naOpenTunnel(String peerId);
	private static native int naCloseTunnel(String peerId);
	private static native int naAskMediaData(String peerId);

	static {
		System.loadLibrary("gnustl_shared");
		System.loadLibrary("VideoPhone");
	}

	/**
	 * ��ʼ��ͨ��
	 */
	public static int tunnelInitialize(String classPath) {
		return naInitialize(classPath);
	}

	/**
	 * ��ֹʹ��ͨ��
	 */
	public static int tunnelTerminate() {
		return naTerminate();
	}

	/**
	 * ��ͨ��
	 */
	public static int openTunnel(String peerId) {
		return naOpenTunnel(peerId);
	}

	/**
	 * �ر�ͨ��
	 */
	public static int closeTunnel(String peerId) {
		return naCloseTunnel(peerId);
	}

	/**
	 * �����ý������
	 */
	public static int askMediaData(String peerId) {
		return naAskMediaData(peerId);
	}

	public static void SendToPeer(String arg1, String arg2) {
		System.out.println("MyDebug: 1��SendToPeer()");
	}

	public static void RecvVideoData(String arg1) {
		System.out.println("MyDebug: 2��RecvVideoData()");
	}
	
	public static void RecvAudioData(String arg1) {
		System.out.println("MyDebug: 3��RecvAudioData()");
	}
}
