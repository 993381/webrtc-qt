package com.video.main;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qrcode.view.CaptureActivity;
import com.video.R;
import com.video.data.PreferData;
import com.video.data.Value;
import com.video.socket.HandlerApplication;
import com.video.socket.ZmqHandler;
import com.video.socket.ZmqThread;
import com.video.utils.Utils;

public class ModifyDeviceNameActivity extends Activity implements OnClickListener {

	private Context mContext;
	private PreferData preferData = null;
	
	private EditText et_name;
	private ProgressDialog progressDialog;
	
	private String userName = "";
	private String termMac = "";
	private String termName = "";
	
	private final int IS_ADDING = 1;
	private final int ADD_TIMEOUT = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_device_name);
		
		initView();
		initData();
	}

	private void initView() {
		ImageButton button_back = (ImageButton) this.findViewById(R.id.btn_modify_device_back);
		button_back.setOnClickListener(this);
		Button button_ok = (Button) this.findViewById(R.id.btn_modify_device_ok);
		button_ok.setOnClickListener(this);
		
		et_name = (EditText) this.findViewById(R.id.et_modify_device_name);
	}
	
	private void initData() {
		mContext = ModifyDeviceNameActivity.this;
		ZmqHandler.setHandler(handler);
		preferData = new PreferData(mContext);
		
		if (preferData.isExist("UserName")) {
			userName = preferData.readString("UserName");
		}
	}
	
	/**
	 * ����JSON�������ն������ַ���
	 */
	private String generateModifyTermNameJson(String mDeviceName, String mDeviceId) {
		String result = "";
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("type", "Client_ModifyTerm");
			jsonObj.put("TermName", mDeviceName);
			jsonObj.put("MAC", mDeviceId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		result = jsonObj.toString();
		return result;
	}
	
	/**
	 * ��ʾ�����Ľ�����
	 */
	private void showProgressDialog(String info) {
		progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage(info); 
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);  
        progressDialog.setIndeterminate(false);     
        progressDialog.setCancelable(false); 
        progressDialog.show(); 
	}
	
	/**
	 * ��ʾ��������ʾ
	 */
	private void showHandleDialog(String info) {
		AlertDialog aboutDialog = new AlertDialog.Builder(mContext)
				.setTitle("��ܰ��ʾ")
				.setMessage(info)
				.setCancelable(false)
				.setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								dialog.dismiss();
							}
						}).create();
		aboutDialog.show();
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
				case IS_ADDING:
					showProgressDialog("�������... ");
					break;
				case ADD_TIMEOUT:
					if (progressDialog != null)
						progressDialog.dismiss();
					showHandleDialog("����ն�ʧ�ܣ����糬ʱ��");
					if (handler.hasMessages(ADD_TIMEOUT)) {
						handler.removeMessages(ADD_TIMEOUT);
					}
					break;
				case R.id.add_device_id:
					if (handler.hasMessages(ADD_TIMEOUT)) {
						handler.removeMessages(ADD_TIMEOUT);
						int resultCode = msg.arg1;
						if (resultCode == 0) {
							String dealerName = (String)msg.obj;
							if (progressDialog != null)
								progressDialog.dismiss();
							showHandleDialog("��ϲ��������ն˳ɹ���\n�����ַ: "+dealerName);
						} else {
							if (progressDialog != null)
								progressDialog.dismiss();
							showHandleDialog("����ն�ʧ�ܣ�"+Utils.getErrorReason(resultCode));
						}
					} else {
						handler.removeMessages(R.id.add_device_id);
					}
					break;
			}
		}
	};
	
	/**
	 * ����Handler��Ϣ
	 */
	private void sendHandlerMsg(int what) {
		Message msg = new Message();
		msg.what = what;
		handler.sendMessage(msg);
	}
	private void sendHandlerMsg(int what, int timeout) {
		Message msg = new Message();
		msg.what = what;
		handler.sendMessageDelayed(msg, timeout);
	}
	private void sendHandlerMsg(Handler handler, int what, String obj) {
		Message msg = new Message();
		msg.what = what;
		msg.obj = obj;
		handler.sendMessage(msg);
	}
	
	public void clickAddDeviceEvent() {
		if (Utils.isNetworkAvailable(mContext)) {
//			Handler sendHandler = HandlerApplication.getInstance().getMyHandler();
//			String data = generateModifyTermNameJson();
//			sendHandlerMsg(IS_REQUESTING, "�����ύ�޸�...");
//			sendHandlerMsg(REQUEST_TIMEOUT, "�޸��ն�����ʧ��", Value.requestTimeout);
//			sendHandlerMsg(sendHandler, R.id.zmq_send_data_id, data);
		} else {
			Toast.makeText(mContext, "û�п��õ��������ӣ���ȷ�Ϻ����ԣ�", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_add_device_back:
				finish();
				break;
			case R.id.btn_add_device_ok:
				clickAddDeviceEvent();
				break;
		}
	}
	
	/**
	 * @return true:ע����Ϣ��ʽ��ȷ  false:ע����Ϣ��ʽ����
	 */
	private boolean checkModifyDeviceData() {
		boolean resultFlag = false;
		
		//��ȡEditText�������ַ���
		termName = et_name.getText().toString().trim();
		
		if (termName.equals("")) {
			resultFlag = false;
			et_name.setError("�������豸���ƣ�");
		}
		else if ((userName.length()<2) || (userName.length()>20)) {
			resultFlag = false;
			et_name.setError("�豸���Ƴ��ȷ�Χ2~20��");
		} else {
			resultFlag = true;
		}
		return resultFlag;
	}
}
