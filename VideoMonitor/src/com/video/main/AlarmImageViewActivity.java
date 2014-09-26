package com.video.main;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.video.R;

public class AlarmImageViewActivity extends Activity implements OnClickListener {
	
	private ImageView imageView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_imageview);
		
		initView();
		initData();
	}
	
	private void initView() {
		imageView = (ImageView) findViewById(R.id.alarm_imageView);
		imageView.setOnClickListener(this);
		ImageButton button_back = (ImageButton) this.findViewById(R.id.ib_alarm_back);
		button_back.setOnClickListener(this);
	}
	
	private void initData() {
		Intent intent = this.getIntent();
		File imageFile = new File((String) intent.getCharSequenceExtra("imagePath"));
		System.out.println("MyDebug: the image_Path："+intent.getCharSequenceExtra("imagePath"));
		if (imageFile.exists()) {
			Uri UriPath = Uri.fromFile(imageFile);
			imageView.setImageURI(UriPath);
		} else {
			imageView.setImageResource(R.drawable.message_image_default);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.ib_alarm_back:
				finish();
				overridePendingTransition(R.anim.fragment_nochange, R.anim.right_out);
				break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK  && event.getRepeatCount() == 0) {
			finish();
			overridePendingTransition(R.anim.fragment_nochange, R.anim.right_out);
		}
		return false;
	}
}