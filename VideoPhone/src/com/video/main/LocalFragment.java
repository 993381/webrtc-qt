package com.video.main;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.video.R;
import com.video.local.ImageViewFileItem;
import com.video.local.ListViewAdapter;
import com.video.local.ViewLocalImageActivity;
import com.video.utils.Utils;
import com.video.utils.ViewPagerAdapter;

public class LocalFragment extends Fragment implements OnClickListener, OnPageChangeListener {

	private FragmentActivity mActivity;
	private View mView;
	
	private TextView viewpage_video;
	private TextView viewpage_image;
	
	private ViewPager mViewPager;
	private List<View> pageList;
	private View video_page;
	private View image_page;
	
	String SD_path = "";
	File currentFile;
	File[] currentFiles;
	
	//����¼���ʼ��
	
	//ץ��ͼƬ��ʼ��
	private final int INIT_LOCAL_VIDEO_FINISH = 1;
	private final int INIT_LOCAL_IMAGE_FINISH = 2;
	private ListView mListView;
	private ListViewAdapter mListViewAdapter;
	private List<ImageViewFileItem> mFileAll;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.local, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		mActivity = getActivity();
		mView = getView();
		
		initViewPageView();
		initView();
		initData();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (ViewLocalImageActivity.isDeleteImage) {
			ViewLocalImageActivity.isDeleteImage = false;
			initData();
		}
	}
	
	private void initView() {
		viewpage_video = (TextView)mView.findViewById(R.id.tv_vp_video);
		viewpage_image = (TextView)mView.findViewById(R.id.tv_vp_image);
		//����¼���ʼ��
		
		//ץ��ͼƬ��ʼ��
		mListView = (ListView)mView.findViewById(R.id.local_image_listView);
		
		viewpage_video.setOnClickListener(this);
		viewpage_image.setOnClickListener(this);
	}
	
	private void initData() {
		//����¼���ʼ��
		
		//ץ��ͼƬ��ʼ��
		new LocalImageThread().start();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.tv_vp_image:
				viewpage_image.setBackgroundResource(R.drawable.viewpage_selected);
				viewpage_video.setBackgroundResource(R.drawable.viewpage_unselected);
				mViewPager.setCurrentItem(0);
				break;
			case R.id.tv_vp_video:
				viewpage_video.setBackgroundResource(R.drawable.viewpage_selected);
				viewpage_image.setBackgroundResource(R.drawable.viewpage_unselected);
				mViewPager.setCurrentItem(1);
				break;
		}
	}
	
	/**
	 * ��ʼ���ý�����Ҫ������ҳ��
	 */
	private void initViewPageView() {
		LayoutInflater inflater = LayoutInflater.from(mActivity);
		image_page = inflater.inflate(R.layout.local_image, null);
		video_page = inflater.inflate(R.layout.local_video, null);
		pageList = new ArrayList<View>();
		pageList.add(image_page);
		pageList.add(video_page);
		mViewPager = (ViewPager)mView.findViewById(R.id.local_viewpager);
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setAdapter(new ViewPagerAdapter(pageList));
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
				viewpage_image.setBackgroundResource(R.drawable.viewpage_selected);
				viewpage_video.setBackgroundResource(R.drawable.viewpage_unselected);
				mViewPager.setCurrentItem(0);
				break;
			case 1:
				viewpage_video.setBackgroundResource(R.drawable.viewpage_selected);
				viewpage_image.setBackgroundResource(R.drawable.viewpage_unselected);
				mViewPager.setCurrentItem(1);
				break;
		}
	}
	
	/**
	 * �����̷߳���Handler��Ϣ(��̫����)
	 * @param what ��Ϣ����
	 */
	public void sendHandlerMsg(int what) {
		Message msg = new Message();
		msg.what = what;
		handler.sendMessage(msg);
	}
	public void sendHandlerMsg(int what, int arg1) {
		Message msg = new Message();
		msg.what = what;
		msg.arg1 = arg1;
		handler.sendMessage(msg);
	}
	
	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
				case INIT_LOCAL_VIDEO_FINISH:
					
					break;
				case INIT_LOCAL_IMAGE_FINISH:
					mListViewAdapter = new ListViewAdapter(mActivity, mFileAll);
					if (mListViewAdapter != null) {
						mListView.setAdapter(mListViewAdapter);
					}
					break;
				default: break;
			}
		}
	};
	
	//-----------------------------------------------------------------------------------
	//����¼����
	//-----------------------------------------------------------------------------------
	
	
	//-----------------------------------------------------------------------------------
	//ץ��ͼƬ����
	//-----------------------------------------------------------------------------------
	
	private class LocalImageThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			initLocalImageData();
		}
	}
	
	private void initLocalImageData() {
		
		mFileAll = new ArrayList<ImageViewFileItem>();
		ImageViewFileItem fileItem = null;
		
		//ץ��ͼƬ��ʼ��
		if (Utils.checkSDCard()) {
			SD_path = Environment.getExternalStorageDirectory().getAbsolutePath();
			File rootPath = new File(SD_path);
			currentFile = rootPath;
			currentFiles = rootPath.listFiles();
			
			if (isDirectoryExist(currentFiles, "KaerVideo")) {
				if (isDirectoryExist(currentFiles, "image")) {
					
					int imageFileCount = currentFiles.length;
					ArrayList<String> imageFileArrayString = handleImageFileName(currentFiles);
					
					if (imageFileArrayString == null) {
						Toast.makeText(mActivity, "û�б���ץ��ͼƬ", Toast.LENGTH_SHORT).show();
						return ;
					}
					
					for (int i=0; i<imageFileCount; i++) {
						fileItem = new ImageViewFileItem();
						//�ļ����µ�ͼƬ
						String imageFilePath = currentFile.getPath()+File.separator+imageFileArrayString.get(i);
						File file = new File(imageFilePath);
						File[] files = file.listFiles();
						ArrayList<HashMap<String, Object>> fileImages = listAllImageViews(files);
						if (fileImages == null) {
							if (file.exists())
								file.delete();
							continue;
						}
						fileItem.imageViews = fileImages;
						//�ļ�����
						fileItem.fileName = imageFileArrayString.get(i);
						mFileAll.add(fileItem);
					}
					sendHandlerMsg(INIT_LOCAL_IMAGE_FINISH);
				}
			}
		}
	}
	
	/**
	 * ���ƶ��ļ����µ�ͼƬȫ���г���
	 * @param files �ļ����
	 * @return ����ͼƬ��ʽ���ļ����
	 */
	private ArrayList<HashMap<String, Object>> listAllImageViews(File[] files) {
		int count = files.length;
		if (count == 0)
			return null;
		ArrayList<HashMap<String, Object>> fileImages = new ArrayList<HashMap<String, Object>>();;
		HashMap<String, Object> imageItem = null;
		
		for (int i=0; i<count; i++) {
			if (!Utils.isImageFile(files[i].getName()))
				continue;
			imageItem = new HashMap<String, Object>();
			imageItem.put("imageInfo", files[i].getPath());
			fileImages.add(imageItem);
		}
		return fileImages;
	}
	
	/**
	 * �����ļ��м��ϣ����ڴӴ�С
	 */
	private ArrayList<Integer> sortImageFileName(File[] fileArray) {
		int fileCount = fileArray.length;
		ArrayList<Integer> arrayInteger = new ArrayList<Integer>();
		
		for (int i=0; i<fileCount; i++) {
			Date date = Utils.StringToDate(currentFiles[i].getName(), "yyyy-MM-dd");
			String fileString = Utils.DateToString(date, "yyyyMMdd");
			int fileInt = Integer.parseInt(fileString);
			arrayInteger.add(fileInt);
		}
		arrayInteger = Utils.bubbleSortArrayList(arrayInteger);
		return arrayInteger; 
	}
	
	/**
	 * ����ͼƬ�ļ������ּ��ϣ��������ڴӴ�С���ַ�������
	 */
	private ArrayList<String> handleImageFileName(File[] fileArray) {
		int fileCount = fileArray.length;
		if (fileCount == 0)
			return null;
		ArrayList<String> arrayString = new ArrayList<String>();
		ArrayList<Integer> arrayInteger = sortImageFileName(fileArray);
		
		for (int i=0; i<fileCount; i++) {
			String fileString = arrayInteger.get(i).toString();
			Date date = Utils.StringToDate(fileString, "yyyyMMdd");
			fileString = Utils.DateToString(date, "yyyy-MM-dd");
			arrayString.add(fileString);
		}
		return arrayString;
	}

	/**
	 * �����ļ��м������Ƿ�������ļ���
	 * @param files �ļ��м���
	 * @param fileName Ҫ���ҵ��ļ���
	 * @return true:�� false:û��
	 */
	private boolean isDirectoryExist(File[] files, String fileName) {
		for (int i=0; i<files.length; i++) {
			if (files[i].getName().equals(fileName)) {
				currentFile = files[i];
				currentFiles = currentFile.listFiles();
				return true;
			}
		}
		return false;
	}
}
