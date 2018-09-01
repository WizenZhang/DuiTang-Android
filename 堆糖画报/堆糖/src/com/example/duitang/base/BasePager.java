package com.example.duitang.base;


import com.example.duitang.R;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class BasePager {
public Activity mActivity;
	
	public View mRootview;//对象布局
	
	public TextView tvTitle;//标题对象
	
	public FrameLayout flContent;//内容
	
	public ImageButton btnMenu;//菜单按钮
	
	public BasePager (Activity activity) {
		mActivity=activity;
		initViews();
	}
	/**
	 * 初始化布局
	 */
	public void initViews() {
		
		mRootview = View.inflate(mActivity, R.layout.navigation_bar, null);
	
		tvTitle = (TextView) mRootview.findViewById(R.id.tv_title);
		
		flContent = (FrameLayout) mRootview.findViewById(R.id.fl_content);
	
		btnMenu = (ImageButton)mRootview.findViewById(R.id.btn_menu); 
	}
	/**
	 * 初始化数据
	 */
	public void initData() {
		
		
	}
}
