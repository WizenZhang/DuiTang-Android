package com.example.duitang.base;

import android.app.Activity;
import android.view.View;

/**
 * 菜单详情页基类
 * @author Wizen
 *
 */
public abstract class BaseMenuDetailpager {
	
    public Activity mActivity;
	
	public View mRootView;//根布局对象
	
	public BaseMenuDetailpager(Activity activity){
		mActivity = activity;
		mRootView = initViews();
	}
	
	/**
	 * 初始化布局
	 * @return 
	 */
	public abstract View initViews();
	
	/**
	 * 初始化数据
	 */
	public void initData() {
		
		
	}
}