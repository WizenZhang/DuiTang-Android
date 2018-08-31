package com.example.duitang.base.impl;

import com.example.duitang.base.BasePager;

import android.app.Activity;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * ��ҳ
 * @author Wizen
 *
 */
public class HomePager extends BasePager {

	public HomePager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initData() {
		tvTitle.setText("��ҳ");
		btnMenu.setVisibility(View.GONE);//���ز˵���ť

		
		TextView text = new TextView(mActivity);
		text.setText("��ҳ");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		
		//��FrameLayout�ж�̬��Ӳ���
		flContent.addView(text);
	}
}
