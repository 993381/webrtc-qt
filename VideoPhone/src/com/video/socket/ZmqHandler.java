package com.video.socket;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;

import com.video.R;
import com.video.data.PreferData;
import com.video.data.Value;
import com.video.main.MainActivity;
import com.video.main.OwnFragment;

public class ZmqHandler extends Handler {
	
	private static Handler mHandler = null;
	
	public static void setHandler(Handler handler) {
		mHandler = handler;
	}
	
	/**
	 * ת����õ�״̬����int����תΪString����
	 */
	private String getState(int state) {
	    String isActiveString = "false";
	    if (state == 1) {
	    	isActiveString = "true";
	    } else {
	    	isActiveString = "false";
	    }
	    return isActiveString;
	}
	
	/**
	 * ������õ������ն��б�JSONArray����
	 */
	private ArrayList<HashMap<String, String>> getReqTermList(JSONArray jsonArray) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> item = null;
		int len = jsonArray.length();
		  
	    try {
	    	for (int i=0; i<len; i++) { 
		    	JSONObject obj = (JSONObject) jsonArray.get(i); 
		    	item = new HashMap<String, String>();
			    item.put("deviceName", obj.getString("TermName")); 
			    item.put("deviceID", obj.getString("MAC"));
			    item.put("isOnline", getState(obj.getInt("Active")));
				item.put("dealerName", obj.getString("DealerName"));
				list.add(item);
	    	}
	    	return list;
		} catch (JSONException e) {
			e.printStackTrace();
			System.out.println("MyDebug: getReqTermList()�쳣��");
		}
		return null;
	}
	
	/**
	 * ������õ����󱨾�����JSONArray����
	 */
	private ArrayList<HashMap<String, String>> getReqAlarmList(JSONArray jsonArray) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> item = null;
		int len = jsonArray.length();
		  
	    try {
	    	for (int i=0; i<len; i++) { 
		    	JSONObject obj = (JSONObject) jsonArray.get(i); 
		    	item = new HashMap<String, String>();
			    item.put("msgMAC", "����: "+obj.getString("MAC")); 
			    item.put("msgEvent", "�¼�: "+obj.getString("AlarmInfo"));
				item.put("msgTime", obj.getString("DateTime"));
				item.put("imageURL", obj.getString("PictureURL"));
				item.put("msgID", obj.getString("ID"));
				item.put("isReaded", getState(obj.getInt("IsUsed")));
				list.add(item);
	    	}
	    	return list;
		} catch (JSONException e) {
			e.printStackTrace();
			System.out.println("MyDebug: getReqAlarmList()�쳣��");
		}
		return null;
	}
	
	@Override
	public void handleMessage(Message msg) {
		String recvData = (String)msg.obj;
		JSONObject obj = null;
		try {
			obj = new JSONObject(recvData);
			String type = obj.getString("type");
			//������Ϣ����
			if (type.equals("Backstage_message")) {
				Value.isNeedReqAlarmListFlag = true;
				Value.requstAlarmCount++;
				PreferData preferData = new PreferData(MainActivity.mContext);
				if (preferData.isExist("AlarmCount")) {
					int alarmCount = preferData.readInt("AlarmCount");
					alarmCount++;
					preferData.writeData("AlarmCount", alarmCount);
					MainActivity.mainHandler.obtainMessage(0, alarmCount, 0).sendToTarget();
				}
			}
			//�ն���������Ϣ����
			else if (type.equals("Backstage_TermActive")) {
				HashMap<String, String> item = new HashMap<String, String>();
			    item.put("deviceID", obj.getString("MAC"));
			    item.put("isOnline", getState(obj.getInt("Active")));
				item.put("dealerName", obj.getString("DealerName"));
				OwnFragment.ownHandler.obtainMessage(0, item).sendToTarget();
			}
			//�ͻ��˺ͷ�����������
			else if (type.equals("Client_BeatHeart")) {
				int resultCode = obj.getInt("Result");
				if (resultCode != 0) {
					Value.isLoginSuccess = true;
				}
			}
			//���������µ�handler����
			else if (mHandler != null) {
				//ע��
				if (type.equals("Client_Registration")) {
					int resultCode = obj.getInt("Result");
					mHandler.obtainMessage(R.id.register_id, resultCode, 0).sendToTarget();
				}
				//��¼
				else if (type.equals("Client_Login")) {
					int resultCode = obj.getInt("Result");
					mHandler.obtainMessage(R.id.login_id, resultCode, 0).sendToTarget();
				}
				//��������
				else if (type.equals("Client_ResetPwd")) {
					int resultCode = obj.getInt("Result");
					mHandler.obtainMessage(R.id.find_pwd_id, resultCode, 0).sendToTarget();
				}
				//�޸�����
				else if (type.equals("Client_ChangePwd")) {
					int resultCode = obj.getInt("Result");
					mHandler.obtainMessage(R.id.modify_pwd_id, resultCode, 0).sendToTarget();
				}
				//����ն�
				else if (type.equals("Client_AddTerm")) {
					int resultCode = obj.getInt("Result");
					if (resultCode == 0) {
						String dealerName = obj.getString("DealerName");
						mHandler.obtainMessage(R.id.add_device_id, resultCode, 0, dealerName).sendToTarget();
					} else {
						mHandler.obtainMessage(R.id.add_device_id, resultCode, 0).sendToTarget();
					}
				}
				//�����ն��б�
				else if (type.equals("Client_ReqTermList")) {
					int resultCode = obj.getInt("Result");
					if (resultCode == 0) {
						JSONArray jsonArray = obj.getJSONArray("Terminal");
						mHandler.obtainMessage(R.id.request_terminal_list_id, resultCode, 0, getReqTermList(jsonArray)).sendToTarget();
					} else {
						mHandler.obtainMessage(R.id.request_terminal_list_id, resultCode, 0, "�����ն��б�ʧ��").sendToTarget();
					}
				}
				//�޸��ն�����
				else if (type.equals("Client_ModifyTerm")) {
					int resultCode = obj.getInt("Result");
					mHandler.obtainMessage(R.id.modify_device_name_id, resultCode, 0).sendToTarget();
				}
				//ɾ���ն˰�
				else if (type.equals("Client_DelTerm")) {
					int resultCode = obj.getInt("Result");
					mHandler.obtainMessage(R.id.delete_device_item_id, resultCode, 0).sendToTarget();
				}
				//���󱨾�����
				else if (type.equals("Client_ReqAlarm")) {
					int resultCode = obj.getInt("Result");
					if (resultCode == 0) {
						JSONArray jsonArray = obj.getJSONArray("AlarmData");
						mHandler.obtainMessage(R.id.request_alarm_id, resultCode, 0, getReqAlarmList(jsonArray)).sendToTarget();
					} else {
						mHandler.obtainMessage(R.id.request_alarm_id, resultCode, 0, "���󱨾�����ʧ��").sendToTarget();
					}
				}
				//ɾ����������
				else if (type.equals("Client_DelAlarm")) {
					int resultCode = obj.getInt("Result");
					if (resultCode == 0) {
						mHandler.obtainMessage(R.id.request_alarm_id, resultCode, 0).sendToTarget();
					} else {
						mHandler.obtainMessage(R.id.request_alarm_id, resultCode, 0, "ɾ����������ʧ��").sendToTarget();
					}
				}
				//ɾ����ǰȫ������
				else if (type.equals("Client_DelSelectAlarm")) {
					int resultCode = obj.getInt("Result");
					if (resultCode == 0) {
						mHandler.obtainMessage(R.id.request_alarm_id, resultCode, 0).sendToTarget();
					} else {
						mHandler.obtainMessage(R.id.request_alarm_id, resultCode, 0, "ɾ����ǰȫ������ʧ��").sendToTarget();
					}
				}
				//��Ǹ�������
				else if (type.equals("Client_MarkAlarm")) {
					int resultCode = obj.getInt("Result");
					if (resultCode == 0) {
						mHandler.obtainMessage(R.id.request_alarm_id, resultCode, 0).sendToTarget();
					} else {
						mHandler.obtainMessage(R.id.request_alarm_id, resultCode, 0, "��Ǹ�������ʧ��").sendToTarget();
					}
				}
				//��ǵ�ǰȫ������
				else if (type.equals("Client_MarkSelectAlarm")) {
					int resultCode = obj.getInt("Result");
					if (resultCode == 0) {
						mHandler.obtainMessage(R.id.request_alarm_id, resultCode, 0).sendToTarget();
					} else {
						mHandler.obtainMessage(R.id.request_alarm_id, resultCode, 0, "��ǵ�ǰȫ������ʧ��").sendToTarget();
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
