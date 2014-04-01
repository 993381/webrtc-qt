package com.video.main;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.video.R;
import com.video.utils.ViewPagerAdapter;

public class HelpActivity extends Activity implements OnPageChangeListener  {

	private ViewPager mViewPager = null;
	private List<View> pageList = null;
	private static View help_page1;
	private static View help_page2;
	private static View help_page3;
	private static View help_page4;
	private static View help_page5;
	
	private static ImageView cur_imageView;
	private static ImageView imageView_help1;
	private static ImageView imageView_help2;
	private static ImageView imageView_help3;
	private static ImageView imageView_help4;
	private static ImageView imageView_help5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		
		initView();
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		initData();
		initViewPageView();
	}
	
	private void initView() {
		imageView_help1 = (ImageView)super.findViewById(R.id.iv_help1);
		imageView_help2 = (ImageView)super.findViewById(R.id.iv_help2);
		imageView_help3 = (ImageView)super.findViewById(R.id.iv_help3);
		imageView_help4 = (ImageView)super.findViewById(R.id.iv_help4);
		imageView_help5 = (ImageView)super.findViewById(R.id.iv_help5);
		cur_imageView = imageView_help1;
	}
	
	private void initData() {
		pageList = new ArrayList<View>();
	}
	
	/**
	 * 初始化该界面下要滑动的页面
	 */
	private void initViewPageView() {
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		help_page1 = inflater.inflate(R.layout.help_introduce1, null);
		help_page2 = inflater.inflate(R.layout.help_introduce2, null);
		help_page3 = inflater.inflate(R.layout.help_introduce3, null);
		help_page4 = inflater.inflate(R.layout.help_introduce4, null);
		help_page5 = inflater.inflate(R.layout.help_introduce5, null);
		pageList.add(help_page1);
		pageList.add(help_page2);
		pageList.add(help_page3);
		pageList.add(help_page4);
		pageList.add(help_page5);
		mViewPager = (ViewPager)super.findViewById(R.id.viewpager_help);
		mViewPager.setAdapter(new ViewPagerAdapter(pageList));
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setCurrentItem(0);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		switch (arg0) {
			case 0:
				cur_imageView.setBackgroundResource(R.drawable.image_unfocused);
				imageView_help1.setBackgroundResource(R.drawable.image_focused);
				cur_imageView = imageView_help1;
				break;
			case 1:
				cur_imageView.setBackgroundResource(R.drawable.image_unfocused);
				imageView_help2.setBackgroundResource(R.drawable.image_focused);
				cur_imageView = imageView_help2;
				break;
			case 2:
				cur_imageView.setBackgroundResource(R.drawable.image_unfocused);
				imageView_help3.setBackgroundResource(R.drawable.image_focused);
				cur_imageView = imageView_help3;
				break;
			case 3:
				cur_imageView.setBackgroundResource(R.drawable.image_unfocused);
				imageView_help4.setBackgroundResource(R.drawable.image_focused);
				cur_imageView = imageView_help4;
				break;
			case 4:
				cur_imageView.setBackgroundResource(R.drawable.image_unfocused);
				imageView_help5.setBackgroundResource(R.drawable.image_focused);
				cur_imageView = imageView_help5;
				break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (!pageList.isEmpty()) {
			pageList.clear();
			pageList = null;
		}
		if (mViewPager != null) {
			mViewPager.removeAllViews();
			mViewPager = null;
		}
	}
}
