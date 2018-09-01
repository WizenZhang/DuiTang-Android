package com.example.duitang.base;


import com.example.duitang.R;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class BasePager {
public Activity mActivity;
	
	public View mRootview;//���󲼾�
	
	public TextView tvTitle;//�������
	
	public FrameLayout flContent;//����
	
	public ImageButton btnMenu;//�˵���ť
	
	public BasePager (Activity activity) {
		mActivity=activity;
		initViews();
	}
	/**
	 * ��ʼ������
	 */
	public void initViews() {
		
		mRootview = View.inflate(mActivity, R.layout.navigation_bar, null);
	
		tvTitle = (TextView) mRootview.findViewById(R.id.tv_title);
		
		flContent = (FrameLayout) mRootview.findViewById(R.id.fl_content);
	
		btnMenu = (ImageButton)mRootview.findViewById(R.id.btn_menu); 
	}
	/**
	 * ��ʼ������
	 */
	public void initData() {
		
		
	}
}
