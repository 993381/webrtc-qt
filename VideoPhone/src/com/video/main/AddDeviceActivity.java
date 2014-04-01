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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.qrcode.view.CaptureActivity;
import com.video.R;
import com.video.data.PreferData;
import com.video.data.Value;
import com.video.socket.ZmqHandler;
import com.video.socket.ZmqThread;
import com.video.utils.PopupWindowAdapter;
import com.video.utils.Utils;

public class AddDeviceActivity extends Activity implements OnClickListener, OnTouchListener {

	private Context mContext;
	private PreferData preferData = null;
	
	private ImageButton button_title_more;
	private EditText et_name;
	private EditText et_id;
	private RelativeLayout add_title;
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
		setContentView(R.layout.add);
		
		initView();
		initData();
	}

	private void initView() {
		add_title = (RelativeLayout) this.findViewById(R.id.add_device_title);
		button_title_more = (ImageButton) this.findViewById(R.id.btn_add_device_more);
		button_title_more.setOnClickListener(this);
		button_title_more.setOnTouchListener(this);
		
		ImageButton button_back = (ImageButton) this.findViewById(R.id.btn_add_device_back);
		button_back.setOnClickListener(this);
		Button button_ok = (Button) this.findViewById(R.id.btn_add_device_ok);
		button_ok.setOnClickListener(this);
		
		et_id = (EditText) this.findViewById(R.id.et_add_device_id);
		et_name = (EditText) this.findViewById(R.id.et_add_device_name);
	}
	
	private void initData() {
		mContext = AddDeviceActivity.this;
		ZmqHandler.setHandler(handler);
		preferData = new PreferData(mContext);
		
		if (preferData.isExist("UserName")) {
			userName = preferData.readString("UserName");
		}
	}
	
	/**
	 * ����JSON�ĵ�¼�ַ���
	 */
	private String generateAddDeviceJson(String username, String mac, String termname) {
		String result = "";
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("type", "Client_AddTerm");
			jsonObj.put("UserName", username);
			jsonObj.put("MAC", mac);
			jsonObj.put("TermName", termname);
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
			if (checkAddDeviceData()) {
				Handler sendHandler = ZmqThread.zmqThreadHandler;
				String data = generateAddDeviceJson(userName, termMac, termName);
				sendHandlerMsg(IS_ADDING);
				sendHandlerMsg(ADD_TIMEOUT, Value.requestTimeout);
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
			case R.id.btn_add_device_back:
				finish();
				break;
			case R.id.btn_add_device_more:
				showPopupWindow();
				break;
			case R.id.btn_add_device_ok:
				clickAddDeviceEvent();
				break;
		}
	}
	
	/**
	 * @return true:ע����Ϣ��ʽ��ȷ  false:ע����Ϣ��ʽ����
	 */
	private boolean checkAddDeviceData() {
		boolean resultFlag = false;
		
		//��ȡEditText�������ַ���
		termName = et_name.getText().toString().trim();
		termMac = et_id.getText().toString().trim();
		
		if (termName.equals("")) {
			resultFlag = false;
			et_name.setError("�������豸���ƣ�");
		}
		else if ((userName.length()<2) || (userName.length()>20)) {
			resultFlag = false;
			et_name.setError("�豸���Ƴ��ȷ�Χ2~20��");
		} else {
			resultFlag = true;
			if (termMac.equals("")) {
				resultFlag = false;
				et_id.setError("�������豸ID��������ͨ��ɨ���ά��������豸�����豸ID��");
			}
			else if ((termMac.length()<6) || (termMac.length()>20)) {
				resultFlag = false;
				et_id.setError("�豸ID���ȷ�Χ6~20��");
			} else {
				resultFlag = true;
			}
		}
		return resultFlag;
	}
	
	private void showPopupWindow() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View pop_view = inflater.inflate(R.layout.pop_main, null);
		ListView pop_listView = (ListView)pop_view.findViewById(R.id.pop_list);
		
		List<String> item_list = new ArrayList<String>();
		item_list.add("ɨ���ά��");
		item_list.add("�����豸");
		PopupWindowAdapter popAdapter = new PopupWindowAdapter(mContext, item_list);
		pop_listView.setAdapter(popAdapter);
		
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		final PopupWindow mPopupWindow = new PopupWindow(pop_view, screenWidth/2, 190, true);
		mPopupWindow.setHeight(LayoutParams.WRAP_CONTENT); 
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setOutsideTouchable(true);
		
		int X_position = screenWidth - mPopupWindow.getWidth() - 10;
		mPopupWindow.setAnimationStyle(R.style.PopupAnimationTop);
		mPopupWindow.showAsDropDown(add_title, X_position, 0);
		mPopupWindow.update();

		pop_listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
					case 0:
						if (mPopupWindow.isShowing()) {
							mPopupWindow.dismiss();
						}
						startActivityForResult(new Intent(AddDeviceActivity.this, CaptureActivity.class), 0);
						break;
					case 1:
						if (mPopupWindow.isShowing()) {
							mPopupWindow.dismiss();
						}
						break;
					default : break;
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String qrcode_string = bundle.getString("qrcode");
			et_id.setText(qrcode_string);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_add_device_more:
			if (MotionEvent.ACTION_DOWN == event.getAction()) {
				button_title_more.setImageResource(R.drawable.title_more_selected);
			} else if (MotionEvent.ACTION_UP == event.getAction()) {
				button_title_more.setImageResource(R.drawable.title_more_unselected);
			}
			break;
		}
		return false;
	}
}
