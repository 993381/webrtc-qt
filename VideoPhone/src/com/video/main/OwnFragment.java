package com.video.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.video.R;
import com.video.data.PreferData;
import com.video.data.Value;
import com.video.data.XmlDevice;
import com.video.socket.ZmqHandler;
import com.video.socket.ZmqThread;
import com.video.utils.DeviceItemAdapter;
import com.video.utils.PopupWindowAdapter;
import com.video.utils.Utils;

public class OwnFragment extends Fragment implements OnClickListener {
	
	private FragmentActivity mActivity;
	private View mView;
	private static XmlDevice xmlData;
	private PreferData preferData = null;
	private String userName = null;
	//终端列表项
	private String mDeviceName = null;
	private String mDeviceId = null;
	private int listPosition = 0;
	private static int listSize = 0;
	
	private ImageButton button_add;
	private PopupWindow mPopupWindow;
	private ProgressDialog progressDialog;
	
	private static ArrayList<HashMap<String, String>> deviceList = null;
	private static DeviceItemAdapter deviceAdapter = null;
	private ListView lv_list;
	
	private final int IS_REQUESTING = 1;
	private final int REQUEST_TIMEOUT = 2;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.own, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mActivity = getActivity();
		mView = getView();
		
		initView();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
	}
	
	private void initView () {
		button_add = (ImageButton)mView.findViewById(R.id.btn_add_device);
		button_add.setOnClickListener(this);
		
		lv_list = (ListView) mView.findViewById(R.id.device_list);
		lv_list.setOnItemLongClickListener(new OnItemLongClickListenerImpl());
	}
	
	private void initData() {
		//初始化Activity要使用的参数
		ZmqHandler.setHandler(handler);
		xmlData = new XmlDevice(mActivity);
		preferData = new PreferData(mActivity);
		if (preferData.isExist("UserName")) {
			userName = preferData.readString("UserName");
		}
		//初始化终端列表的显示
		if (Value.isNeedReqTermListFlag) {
			reqTermListEvent();
		}
		deviceList = xmlData.readXml();
		if (deviceList != null) {
			listSize = deviceList.size();
			deviceAdapter = new DeviceItemAdapter(mActivity, deviceList);
			lv_list.setAdapter(deviceAdapter);
		}
	}
	
	/**
	 * 获得一个设备项Item
	 * @param isOnline 设备是否在线
	 * @param deviceName 设备名称
	 * @param deviceID 设备的MAC
	 * @return 返回一个设备项Item
	 */
	@SuppressWarnings("unused")
	private HashMap<String, String> getDeviceItem(boolean isOnline, String deviceName, String deviceID, String dealerName) {
		
		HashMap<String, String> item = new HashMap<String, String>();
		String state = "false";
		if (isOnline) {
			state = "true";
		} else {
			state = "false";
		}
		item.put("isOnline", state);
		item.put("deviceName", deviceName);
		item.put("deviceID", deviceID);
		item.put("dealerName", dealerName);
		return item;
	}
	
	/**
	 * 生成JSON的请求设备列表字符串
	 */
	private String generateReqTermListJson() {
		String result = "";
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("type", "Client_ReqTermList");
			jsonObj.put("UserName", userName);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		result = jsonObj.toString();
		return result;
	}
	
	/**
	 * 生成JSON的删除终端绑定字符串
	 */
	private String generateDelTermItemJson(String mac) {
		String result = "";
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("type", "Client_DelTerm");
			jsonObj.put("UserName", userName);
			jsonObj.put("MAC", mac);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		result = jsonObj.toString();
		return result;
	}
	
	/**
	 * 显示操作的进度条
	 */
	private void showProgressDialog(String info) {
		progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage(info); 
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);  
        progressDialog.setIndeterminate(false);     
        progressDialog.setCancelable(false); 
        progressDialog.show(); 
	}
	
	private Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
				case IS_REQUESTING:
					showProgressDialog((String) msg.obj);
					break;
				case REQUEST_TIMEOUT:
					if (progressDialog != null)
						progressDialog.dismiss();
					if (handler.hasMessages(REQUEST_TIMEOUT)) {
						handler.removeMessages(REQUEST_TIMEOUT);
					}
					Value.isNeedReqTermListFlag = false;
					Toast.makeText(mActivity, ""+msg.obj, Toast.LENGTH_SHORT).show();
					break;
				//请求终端列表
				case R.id.request_terminal_list_id:
					if (handler.hasMessages(REQUEST_TIMEOUT)) {
						handler.removeMessages(REQUEST_TIMEOUT);
						if (progressDialog != null)
							progressDialog.dismiss();
						int resultCode = msg.arg1;
						if (resultCode == 0) {
							deviceList = (ArrayList<HashMap<String, String>>) msg.obj;
							if (deviceList != null) {
								Value.isNeedReqTermListFlag = false;
								deviceAdapter = new DeviceItemAdapter(mActivity, deviceList);
								lv_list.setAdapter(deviceAdapter);
								xmlData.updateList(deviceList);
							}
							listSize = xmlData.getListSize();
						} else {
							Toast.makeText(mActivity, msg.obj+"，"+Utils.getErrorReason(resultCode), Toast.LENGTH_SHORT).show();
						}
					} else {
						handler.removeMessages(R.id.request_terminal_list_id);
					}
					break;
				//删除终端绑定
				case R.id.delete_device_item_id:
					if (handler.hasMessages(REQUEST_TIMEOUT)) {
						handler.removeMessages(REQUEST_TIMEOUT);
						if (progressDialog != null)
							progressDialog.dismiss();
						int resultCode = msg.arg1;
						if (resultCode == 0) {
							Toast.makeText(mActivity, "删除终端绑定成功！", Toast.LENGTH_SHORT).show();
							xmlData.deleteItem(mDeviceId);
							deviceList.remove(listPosition);
							deviceAdapter.notifyDataSetChanged();
							listSize = xmlData.getListSize();
						} else {
							Toast.makeText(mActivity, "删除终端绑定失败，"+Utils.getErrorReason(resultCode), Toast.LENGTH_SHORT).show();
						}
					} else {
						handler.removeMessages(R.id.request_terminal_list_id);
					}
					break;
			}
		}
	};
	
	public static Handler ownHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 0) {
				HashMap<String, String> item = (HashMap<String, String>)msg.obj;
				String mac = item.get("deviceID");
				for (int i=0; i<listSize; i++) {
					if (deviceList.get(i).get("deviceID").equals(mac)) {
						item.put("deviceName", deviceList.get(i).get("deviceName"));
						deviceList.get(i).put("isOnline", item.get("isOnline"));
						deviceList.get(i).put("dealerName", item.get("dealerName"));
						break;
					}
				}
				deviceAdapter.notifyDataSetChanged();
				xmlData.updateItem(item);
			}
		}
	};
	
	/**
	 * 发送Handler消息
	 */
	private void sendHandlerMsg(int what, String obj) {
		Message msg = new Message();
		msg.what = what;
		msg.obj = obj;
		handler.sendMessage(msg);
	}
	private void sendHandlerMsg(int what, String obj, int timeout) {
		Message msg = new Message();
		msg.what = what;
		msg.obj = obj;
		handler.sendMessageDelayed(msg, timeout);
	}
	private void sendHandlerMsg(Handler handler, int what, String obj) {
		Message msg = new Message();
		msg.what = what;
		msg.obj = obj;
		handler.sendMessage(msg);
	}
	
	/**
	 * 请求终端列表的网络操作
	 */
	public void reqTermListEvent() {
		if (Utils.isNetworkAvailable(mActivity)) {
			Handler sendHandler = ZmqThread.zmqThreadHandler;
			String data = generateReqTermListJson();
			sendHandlerMsg(IS_REQUESTING, "正在请求终端列表...");
			sendHandlerMsg(REQUEST_TIMEOUT, "请求终端课表失败，网络超时！", Value.requestTimeout);
			sendHandlerMsg(sendHandler, R.id.zmq_send_data_id, data);
		} else {
			Toast.makeText(mActivity, "没有可用的网络连接，请确认后重试！", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 删除终端绑定的网络操作
	 */
	public void delTermItemEvent(String id) {
		if (Utils.isNetworkAvailable(mActivity)) {
			Handler sendHandler = ZmqThread.zmqThreadHandler;
			String data = generateDelTermItemJson(id);
			sendHandlerMsg(IS_REQUESTING, "正在删除终端绑定...");
			sendHandlerMsg(REQUEST_TIMEOUT, "删除终端绑定失败，网络超时！", Value.requestTimeout);
			sendHandlerMsg(sendHandler, R.id.zmq_send_data_id, data);
		} else {
			Toast.makeText(mActivity, "没有可用的网络连接，请确认后重试！", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_add_device:
				startActivity(new Intent(mActivity, AddDeviceActivity.class));
				break;
		}
	}
	
	/**
	 * 设备项ListView的长点击事件
	 */
	private class OnItemLongClickListenerImpl implements OnItemLongClickListener {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			listPosition = position;
			showPopupWindow(lv_list);
			return false;
		}
	}

	/**
	 * 设备项ListView的长按键的PopupWindow选项
	 */
	public void showPopupWindow(View view) {
		LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View pop_view = inflater.inflate(R.layout.pop_event_main, null);
		ListView pop_listView = (ListView)pop_view.findViewById(R.id.pop_list);
		
		List<String> item_list = new ArrayList<String>();
		item_list.add("修改终端名称");
		item_list.add("删除终端绑定");
		item_list.add("设置背景图片");
		item_list.add("删除背景图片");
		PopupWindowAdapter popAdapter = new PopupWindowAdapter(mActivity, item_list);
		pop_listView.setAdapter(popAdapter);
		
		mPopupWindow = new PopupWindow(pop_view, Utils.screenWidth, 200, true);
		mPopupWindow.setHeight(LayoutParams.WRAP_CONTENT); 
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setOutsideTouchable(true);
		
		mPopupWindow.setAnimationStyle(R.style.PopupAnimationBottom);
		mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
		mPopupWindow.update();

		pop_listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				HashMap<String, String> item = deviceList.get(listPosition);
				mDeviceName = item.get("deviceName");
				mDeviceId = item.get("deviceID");
				switch (position) {
					case 0:
						Intent intent = new Intent(mActivity, ModifyDeviceNameActivity.class);
						intent.putExtra("deviceName", mDeviceName);
						intent.putExtra("deviceID", mDeviceId);
						startActivity(intent);
						mActivity.overridePendingTransition(R.anim.down_in, 0);
						break;
					case 1:
						delTermItemEvent(mDeviceId);
						break;
				}
				if (mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
				}
			}
		});
	}
}
