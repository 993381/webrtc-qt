package com.video.user;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.video.R;
import com.video.data.PreferData;
import com.video.data.Value;
import com.video.socket.ZmqHandler;
import com.video.socket.ZmqThread;
import com.video.utils.Utils;

public class LoginActivity extends Activity implements OnClickListener {

	private Context mContext;
	private PreferData preferData = null;
	
	private EditText et_name;
	private EditText et_pwd;
	private CheckBox cb_auto_login;
	private ProgressDialog progressDialog;
	private boolean isAutoLogin = false;
	
	private String userName = "";
	private String userPwd = "";
	
	private final int IS_LOGINNING = 1;
	private final int LOGIN_TIMEOUT = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		initView();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
	}
	
	private void initView() {
		et_name = (EditText) super.findViewById(R.id.et_name);
		et_pwd = (EditText) super.findViewById(R.id.et_pwd);
		cb_auto_login = (CheckBox) super.findViewById(R.id.cb_auto_login);
		cb_auto_login.setOnCheckedChangeListener(new onCheckedChangeListenerImpl());
		Button button_login = (Button) super.findViewById(R.id.btn_login);
		button_login.setOnClickListener(this);
		TextView text_register = (TextView)super.findViewById(R.id.tv_register);
		text_register.setOnClickListener(this);
		TextView text_find_pwd = (TextView)super.findViewById(R.id.tv_find_pwd);
		text_find_pwd.setOnClickListener(this);
	}
	
	private void initData() {
		mContext = LoginActivity.this;
		ZmqHandler.setHandler(handler);
		preferData = new PreferData(mContext);
		
		if (preferData.isExist("AutoLogin")) {
			isAutoLogin = preferData.readBoolean("AutoLogin");
		}
		
		if (isAutoLogin) {
			cb_auto_login.setChecked(true);
		} else {
			cb_auto_login.setChecked(false);
		}
		
		if (preferData.isExist("UserName")) {
			userName = preferData.readString("UserName");
			et_name.setText(userName);
		}
		
		if (preferData.isExist("UserPwd")) {
			userPwd = preferData.readString("UserPwd");
			et_pwd.setText(userPwd);
		}
	}
	
	/**
	 * ����JSON�ĵ�¼�ַ���
	 */
	private String generateLoginJson(String username, String pwd) {
		String result = "";
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("type", "Client_Login");
			jsonObj.put("UserName", username);
			jsonObj.put("Pwd", Utils.CreateMD5Pwd(pwd));
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
				case IS_LOGINNING:
					showProgressDialog("���ڵ�¼... ");
					break;
				case LOGIN_TIMEOUT:
					if (progressDialog != null)
						progressDialog.dismiss();
					showHandleDialog("��¼ʧ�ܣ����糬ʱ��");
					if (handler.hasMessages(LOGIN_TIMEOUT)) {
						handler.removeMessages(LOGIN_TIMEOUT);
					}
					break;
				case R.id.login_id:
					if (handler.hasMessages(LOGIN_TIMEOUT)) {
						handler.removeMessages(LOGIN_TIMEOUT);
						int resultCode = msg.arg1;
						if (resultCode == 0) {
							if (progressDialog != null)
								progressDialog.dismiss();
							showHandleDialog("��ϲ������¼�ɹ���");
						} else {
							if (progressDialog != null)
								progressDialog.dismiss();
							showHandleDialog("��¼ʧ�ܣ�"+Utils.getErrorReason(resultCode));
						}
					} else {
						handler.removeMessages(R.id.login_id);
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
	
	private class onCheckedChangeListenerImpl implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			preferData.writeData("AutoLogin", isChecked);
		}
	}
	
	public void clickLoginEvent() {
		if (Utils.isNetworkAvailable(mContext)) {
			if (checkRegisterData()) {
				if (preferData.isExist("UserName")) {
					preferData.deleteItem("UserName");
					preferData.writeData("UserName", userName);
				} else {
					preferData.writeData("UserName", userName);
				}
				
				if (preferData.isExist("UserPwd")) {
					preferData.deleteItem("UserPwd");
					preferData.writeData("UserPwd", userPwd);
				} else {
					preferData.writeData("UserPwd", userPwd);
				}
				Handler sendHandler = ZmqThread.zmqThreadHandler;
				String data = generateLoginJson(userName, userPwd);
				sendHandlerMsg(IS_LOGINNING);
				sendHandlerMsg(LOGIN_TIMEOUT, Value.requestTimeout);
				sendHandlerMsg(sendHandler, R.id.zmq_send_data_id, data);
			}
		} else {
			showHandleDialog("û�п��õ��������ӣ���ȷ�Ϻ����ԣ�");
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_login:
				clickLoginEvent();
				break;
			case R.id.tv_register:
				startActivity(new Intent(this, RegisterActivity.class));
				break;
			case R.id.tv_find_pwd:
				startActivity(new Intent(this, FindPwdActivity.class));
				break;
		}
	}
	
	/**
	 * @return true:ע����Ϣ��ʽ��ȷ  false:ע����Ϣ��ʽ����
	 */
	private boolean checkRegisterData() {
		boolean resultFlag = false;
		
		//��ȡEditText�������ַ���
		userName = et_name.getText().toString().trim();
		userPwd = et_pwd.getText().toString().trim();
		
		if (userName.equals("")) {
			resultFlag = false;
			et_name.setError("�������û�����");
		}
		else if ((userName.length()<3) || (userName.length()>20)) {
			resultFlag = false;
			et_name.setError("�û������ȷ�Χ3~20��");
		} else {
			resultFlag = true;
			if (userPwd.equals("")) {
				resultFlag = false;
				et_pwd.setError("���������룡");
			}
			else if ((userPwd.length()<6) || (userPwd.length()>20)) {
				resultFlag = false;
				et_pwd.setError("���볤�ȷ�Χ6~20��");
			} else {
				resultFlag = true;
			}
		}
		return resultFlag;
	}
}
