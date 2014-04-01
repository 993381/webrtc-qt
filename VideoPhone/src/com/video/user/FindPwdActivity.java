package com.video.user;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.video.R;
import com.video.data.Value;
import com.video.socket.ZmqHandler;
import com.video.socket.ZmqThread;
import com.video.utils.Utils;

public class FindPwdActivity extends Activity implements OnClickListener {

	private Context mContext;
	
	private EditText et_name;
	private EditText et_email;
	private Button btn_submit;
	private ProgressDialog progressDialog;
	
	private String userName = "";
	private String userEmail = "";
	
	private final int IS_SUBMITTING = 1;
	private final int SUBMIT_TIMEOUT = 2;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_pwd);
		
		initView();
		initData();
	}

	private void initView() {
		et_name = (EditText)super.findViewById(R.id.et_find_pwd_name);
		et_email = (EditText)super.findViewById(R.id.et_find_pwd_email);
		
		btn_submit = (Button)super.findViewById(R.id.btn_find_pwd_submit);
		btn_submit.setOnClickListener(this);
	}

	private void initData() {
		mContext = FindPwdActivity.this;
		ZmqHandler.setHandler(handler);
	}
	
	/**
	 * ����JSON��ע���ַ���
	 */
	private String generateFindPwdJson(String username, String email) {
		String result = "";
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("type", "Client_ResetPwd");
			jsonObj.put("UserName", username);
			jsonObj.put("Email", email);
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
				case IS_SUBMITTING:
					showProgressDialog("�һ�������... ");
					break;
				case SUBMIT_TIMEOUT:
					if (progressDialog != null)
						progressDialog.dismiss();
					showHandleDialog("�һ�����ʧ�ܣ����糬ʱ��");
					if (handler.hasMessages(SUBMIT_TIMEOUT)) {
						handler.removeMessages(SUBMIT_TIMEOUT);
					}
					break;
				case R.id.find_pwd_id:
					if (handler.hasMessages(SUBMIT_TIMEOUT)) {
						handler.removeMessages(SUBMIT_TIMEOUT);
						int resultCode = msg.arg1;
						if (resultCode == 0) {
							if (progressDialog != null)
								progressDialog.dismiss();
							showHandleDialog("��ϲ�����һ�����ɹ��������ѱ�����Ϊ��123456����");
						} else {
							if (progressDialog != null)
								progressDialog.dismiss();
							showHandleDialog("�һ�����ʧ�ܣ�"+Utils.getErrorReason(resultCode));
						}
					} else {
						handler.removeMessages(R.id.find_pwd_id);
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
	
	private void clickFindPwdEvent() {
		if (Utils.isNetworkAvailable(mContext)) {
			if (checkFindPwdData()) {
				Handler sendHandler = ZmqThread.zmqThreadHandler;
				String data = generateFindPwdJson(userName, userEmail);
				sendHandlerMsg(IS_SUBMITTING);
				sendHandlerMsg(SUBMIT_TIMEOUT, Value.requestTimeout);
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
			case R.id.btn_find_pwd_submit:
				clickFindPwdEvent();
				break;
		}
	}
	
	/**
	 * @return true:ע����Ϣ��ʽ��ȷ  false:ע����Ϣ��ʽ����
	 */
	private boolean checkFindPwdData() {
		boolean resultFlag = false;
		
		//��ȡEditText�������ַ���
		userName = et_name.getText().toString().trim();
		userEmail = et_email.getText().toString().trim();
		
		if (userName.equals("")) {
			resultFlag = false;
			et_name.setError("�������û�����");
		}
		else if ((userName.length()<3) || (userName.length()>20)) {
			resultFlag = false;
			et_name.setError("�û������ȷ�Χ3~20��");
		} else {
			resultFlag = true;
			if (userEmail.equals("")) {
				resultFlag = false;
				et_email.setError("������������䣡");
			}
			else if ((userEmail.length()<6) || (userEmail.length()>20)) {
				resultFlag = false;
				et_email.setError("�������䳤�ȷ�Χ6~20��");
			}
			else if (userEmail.indexOf("@")<=0) {
				resultFlag = false;
				et_email.setError("�����ʽ����ȷ��");
			}
			else if (userEmail.indexOf(".")<=0) {
				resultFlag = false;
				et_email.setError("�����ʽ����ȷ��");
			} else {
				resultFlag = true;
			}
		}
		return resultFlag;
	}
}
