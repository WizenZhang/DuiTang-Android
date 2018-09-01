package com.example.duitang.base;

import android.app.Activity;
import android.view.View;

/**
 * �˵�����ҳ����
 * @author Wizen
 *
 */
public abstract class BaseMenuDetailpager {
	
    public Activity mActivity;
	
	public View mRootView;//�����ֶ���
	
	public BaseMenuDetailpager(Activity activity){
		mActivity = activity;
		mRootView = initViews();
	}
	
	/**
	 * ��ʼ������
	 * @return 
	 */
	public abstract View initViews();
	
	/**
	 * ��ʼ������
	 */
	public void initData() {
		
		
	}
}