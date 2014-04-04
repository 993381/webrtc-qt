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
import android.widget.ImageButton;

import com.video.R;
import com.video.data.PreferData;
import com.video.data.Value;
import com.video.socket.ZmqHandler;
import com.video.socket.ZmqThread;
import com.video.utils.Utils;

public class ModifyPwdActivity extends Activity implements OnClickListener {

	private Context mContext;
	private PreferData preferData = null;
	
	private EditText et_old_pwd;
	private EditText et_new_pwd;
	private EditText et_new_repwd;
	private ProgressDialog progressDialog;
	
	private String userName = "";
	private String userPwd = "";
	private String userOldPwd = "";
	private String userNewPwd = "";
	private String userNewRepwd = "";
	
	private final int IS_MODIFYING = 1;
	private final int MODIFY_TIMEOUT = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_pwd);
		
		initView();
		initData();
	}

	private void initView() {
		et_old_pwd = (EditText)super.findViewById(R.id.et_modify_old_pwd);
		et_new_pwd = (EditText)super.findViewById(R.id.et_modify_new_pwd);
		et_new_repwd = (EditText)super.findViewById(R.id.et_modify_new_repwd);
		
		ImageButton button_back = (ImageButton) super.findViewById(R.id.ib_modify_pwd_back);
		button_back.setOnClickListener(this);
		Button button_submit = (Button) super.findViewById(R.id.btn_modify_pwd_submit);
		button_submit.setOnClickListener(this);
	}
	
	private void initData() {
		mContext = ModifyPwdActivity.this;
		ZmqHandler.setHandler(handler);
		preferData = new PreferData(mContext);
		if (preferData.isExist("UserName")) {
			userName = preferData.readString("UserName");
		}
		
		if (preferData.isExist("UserPwd")) {
			userPwd = preferData.readString("UserPwd");
		}
	}
	
	/**
	 * ����JSON��ע���ַ���
	 */
	private String generateModifyPwdJson(String username, String oldpwd, String newpwd) {
		String result = "";
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("type", "Client_ChangePwd");
			jsonObj.put("UserName", username);
			jsonObj.put("OldPwd", Utils.CreateMD5Pwd(oldpwd));
			jsonObj.put("NewPwd", Utils.CreateMD5Pwd(newpwd));
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
				case IS_MODIFYING:
					showProgressDialog("�����ύ... ");
					break;
				case MODIFY_TIMEOUT:
					if (progressDialog != null)
						progressDialog.dismiss();
					showHandleDialog("�޸�����ʧ�ܣ����糬ʱ��");
					if (handler.hasMessages(MODIFY_TIMEOUT)) {
						handler.removeMessages(MODIFY_TIMEOUT);
					}
					break;
				case R.id.modify_pwd_id:
					if (handler.hasMessages(MODIFY_TIMEOUT)) {
						handler.removeMessages(MODIFY_TIMEOUT);
						int resultCode = msg.arg1;
						if (resultCode == 0) {
							if (progressDialog != null)
								progressDialog.dismiss();
							showHandleDialog("��ϲ�����޸�����ɹ���");
						} else {
							if (progressDialog != null)
								progressDialog.dismiss();
							showHandleDialog("�޸�����ʧ�ܣ�"+Utils.getErrorReason(resultCode));
						}
					} else {
						handler.removeMessages(R.id.modify_pwd_id);
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
	
	private void clickModifyPwdEvent() {
		if (Utils.isNetworkAvailable(mContext)) {
			if (checkModifyPwdData()) {
				Handler sendHandler = ZmqThread.zmqThreadHandler;
				String data = generateModifyPwdJson(userName, userOldPwd, userNewPwd);
				sendHandlerMsg(IS_MODIFYING);
				sendHandlerMsg(MODIFY_TIMEOUT, Value.requestTimeout);
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
			case R.id.ib_modify_pwd_back:
				finish();
				break;
			case R.id.btn_modify_pwd_submit:
				clickModifyPwdEvent();
				break;
		}
	}
	
	/**
	 * @return true:�޸������ʽ��ȷ  false:�޸������ʽ����
	 */
	private boolean checkModifyPwdData() {
		boolean resultFlag = false;
		
		//��ȡEditText�������ַ���
		userOldPwd = et_old_pwd.getText().toString().trim();
		userNewPwd = et_new_pwd.getText().toString().trim();
		userNewRepwd = et_new_repwd.getText().toString().trim();
		
		if (userOldPwd.equals("")) {
			resultFlag = false;
			et_old_pwd.setError("����������룡");
		}
		else if (!userOldPwd.equals(userPwd)) {
			resultFlag = false;
			et_old_pwd.setError("��������ԭ���벻ƥ�䣡");
		} else {
			resultFlag = true;
			if (userNewPwd.equals("")) {
				resultFlag = false;
				et_new_pwd.setError("�����������룡");
			}
			else if ((userNewPwd.length()<6) || (userNewPwd.length()>20)) {
				resultFlag = false;
				et_new_pwd.setError("���볤�ȷ�Χ6~20��");
			} else {
				resultFlag = true;
				if (userNewRepwd.equals("")) {
					resultFlag = false;
					et_new_repwd.setError("���ٴ����������룡");
				}
				else if ((userNewRepwd.length()<6) || (userNewRepwd.length()>20)) {
					resultFlag = false;
					et_new_repwd.setError("ȷ�����볤�ȷ�Χ6~20��");
				}
				else if (!userNewPwd.equals(userNewRepwd)) {
					resultFlag = false;
					et_new_repwd.setError("������������벻һ�£�");
				} else {
					resultFlag = true;
				}
			}
		}
		return resultFlag;
	}
}
