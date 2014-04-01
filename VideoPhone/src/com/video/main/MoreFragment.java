package com.video.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.video.R;
import com.video.data.PreferData;
import com.video.user.LoginActivity;
import com.video.user.ModifyPwdActivity;

public class MoreFragment extends Fragment implements OnClickListener {

	private FragmentActivity mActivity;
	private View mView;
	
	Button button_logout;
	private PreferData preferData = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		return inflater.inflate(R.layout.more, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mActivity = getActivity();
		mView = getView();
		
		initView();
		initData();
	}
	
	private void initView() {
		Button button_modify_pwd = (Button)mView.findViewById(R.id.btn_modify_pwd);
		button_modify_pwd.setOnClickListener(this);
		
		Button button_help = (Button)mView.findViewById(R.id.btn_help);
		button_help.setOnClickListener(this);
		
		Button button_about = (Button)mView.findViewById(R.id.btn_about);
		button_about.setOnClickListener(this);
		
		button_logout = (Button)mView.findViewById(R.id.btn_logout);
		button_logout.setOnClickListener(this);
 	}
	
	private void initData() {
		preferData = new PreferData(mActivity);
	}
	
	/**
	 * ��ʾ��������ʾ
	 */
	private void showHandleDialog() {
		AlertDialog aboutDialog = new AlertDialog.Builder(mActivity)
				.setTitle("��ܰ��ʾ")
				.setMessage("ȷ���˳���ǰ�˺ŵĵ�¼��")
				.setCancelable(false)
				.setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								dialog.dismiss();
								ExitLogoutAPP();
							}
						})
				.setNegativeButton("ȡ��",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).create();
		aboutDialog.show();
	}
	
	/**
	 * �˳���ǰ�˺ŵ�¼�Ĵ���
	 */
	private void ExitLogoutAPP() {
		if (preferData.isExist("UserPwd")) {
			preferData.deleteItem("UserPwd");
		}
		if (preferData.isExist("AutoLogin")) {
			preferData.deleteItem("AutoLogin");
		}
		startActivity(new Intent(mActivity, LoginActivity.class));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_modify_pwd:
				startActivity(new Intent(mActivity, ModifyPwdActivity.class));
				break;
			case R.id.btn_help:
				startActivity(new Intent(mActivity, HelpActivity.class));
				break;
			case R.id.btn_about:
				startActivity(new Intent(mActivity, AboutActivity.class));
				break;
			case R.id.btn_logout:
				showHandleDialog();
				break;
		}
	}
}

