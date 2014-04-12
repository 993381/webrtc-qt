package com.video.data;

import com.video.utils.Tools;

public class ByteCache {

	private byte[] mLock = new byte[0];
	private byte[] mBuffer = null;
	private int mBufferLen;
	private int inPtr;
	private int outPtr;

	/**
	 * �жϻ������Ƿ�Ϊ��
	 * @return true:������Ϊ��  false:��������Ϊ��
	 */
	public boolean isEmpty() {
		return (inPtr == outPtr) ? true : false;
	}
	
	/**
	 * ��ջ�����
	 */
	public void clearBuffer() {
		synchronized (mLock) {
			inPtr = 0;
			outPtr = 0;
		}
	}

	/**
	 * ���û������Ĵ�С
	 * @param size ��������С
	 */
	public void setBufferLen(int size) {
		mBufferLen = size;
		mBuffer = new byte[mBufferLen];
		clearBuffer();
	}

	/**
	 * ���������д�������
	 * @return ����������д�������
	 */
	public int getMaxWriteLen() {
		int result = 0;

		if (mBuffer == null) {
			return -1;
		}

		if (inPtr == outPtr) {
			result = mBufferLen;
		} else if (outPtr > inPtr) {
			result = outPtr - inPtr;
		} else {
			result = mBufferLen - inPtr + outPtr;
		}
		
		return result;
	}

	/**
	 * ��������Զ�ȡ������
	 * @return ���������Զ�ȡ������
	 */
	private int getMaxReadLen() {
		int result = 0;

		if (mBuffer == null) {
			return -1;
		}

		if (inPtr == outPtr) {
			result = 0;
		} else if (outPtr < inPtr) {
			result = inPtr - outPtr;
		} else if (outPtr > inPtr) {
			result = mBufferLen - outPtr + inPtr;
		}
		
		return result;
	}

	/**
	 * ������ѹ�뻺����
	 * @param buf Ҫѹ�������
	 * @param len ѹ�����ݵĳ���
	 * @return 0:ѹ��ɹ�  <0ѹ��ʧ��
	 */
	public int push(byte[] buf, int len) {
		int result = -1;

		synchronized (mLock) {
			int maxPushLen = 0;
			int pushLen = len;
			int firstChunkSize = 0;
			int secondChunkSize = 0;
			
			maxPushLen = getMaxWriteLen();
			if (pushLen >= maxPushLen) {
				return -2;
			}

			if (inPtr + pushLen < mBufferLen) {
				Tools.CopyByteArray(mBuffer, inPtr, buf, 0, pushLen);
				inPtr += pushLen;
			} else {
				firstChunkSize = mBufferLen - inPtr;
				secondChunkSize = pushLen - firstChunkSize;
				Tools.CopyByteArray(mBuffer, inPtr, buf, 0, firstChunkSize);
				Tools.CopyByteArray(mBuffer, 0, buf, firstChunkSize, secondChunkSize);
				inPtr = secondChunkSize;
			}
			result = 0;
		}
		return result;
	}
	
	/**
	 * ����������������
	 * @param naluData ����������
	 * @return ���ص������ݵĴ�С
	 */
	public int pop(byte[] naluData) {
		int len = -1;
		
		synchronized (mLock) {
			int maxPopLen = 0;
			int popLen = 0;

			int firstChunkSize = 0;
			int secondChunkSize = 0;
			maxPopLen = getMaxReadLen();

			if (maxPopLen <= 0) {
				return -2;
			}

			popLen = getPopLen();
			len = popLen;
			popLen += 4;

			if (popLen == 0 || popLen > mBufferLen || popLen > maxPopLen) {
				return -2;
			}

			if (outPtr + popLen < mBufferLen) {
				Tools.CopyByteArray(naluData, 0, mBuffer, outPtr + 4, popLen-4);
				outPtr += popLen;
			} else {
				firstChunkSize = mBufferLen - outPtr;
				secondChunkSize = popLen - firstChunkSize;

				if (firstChunkSize >= 4) {
					if ((firstChunkSize - 4) != 0) {
						Tools.CopyByteArray(naluData, 0, mBuffer, outPtr+4, firstChunkSize-4);
					}
					Tools.CopyByteArray(naluData, firstChunkSize, mBuffer, 0, secondChunkSize);
				} else {
					Tools.CopyByteArray(naluData, 0, mBuffer, outPtr, firstChunkSize);
					Tools.CopyByteArray(naluData, firstChunkSize, mBuffer, 4-firstChunkSize, secondChunkSize-4+firstChunkSize);
				}
				outPtr = secondChunkSize;
			}
		}

		return len;
	}
	
	/**
	 * ���õ������ݵĴ�С
	 * @return ���ص������ݵĴ�С
	 */
	private int getPopLen() {
		byte[] result = new byte[6];

		if (outPtr > inPtr) {
			int len1, len2;
			len1 = mBufferLen - outPtr;
			if (len1 < 4) {
				byte[] cLen = new byte[6];
				Tools.CopyByteArray(cLen, 0, mBuffer, outPtr, len1);
				len2 = 4 - len1;
				Tools.CopyByteArray(cLen, len1, mBuffer, 0, len2);
				Tools.CopyByteArray(result, 0, cLen, 0, 4);
			} else {
				Tools.CopyByteArray(result, 0, mBuffer, outPtr, 4);
			}
		} else {
			Tools.CopyByteArray(result, 0, mBuffer, outPtr, 4);
		}

		return Tools.getIntValue(result, 0);
	}
}
