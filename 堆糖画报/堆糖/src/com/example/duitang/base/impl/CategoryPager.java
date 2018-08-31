package com.example.duitang.base.impl;

import com.example.duitang.base.BasePager;

import android.app.Activity;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * ����
 * @author Wizen
 *
 */
public class CategoryPager extends BasePager {

	public CategoryPager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initData() {
		tvTitle.setText("����");
//		btnMenu.setVisibility(View.GONE);//���ز˵���ť
		
		TextView text = new TextView(mActivity);
		text.setText("����");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		
		//��FrameLayout�ж�̬��Ӳ���
		flContent.addView(text);
	}
}
