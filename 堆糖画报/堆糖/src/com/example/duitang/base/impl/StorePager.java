package com.example.duitang.base.impl;

import com.example.duitang.base.BasePager;

import android.app.Activity;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * 收藏
 * @author Wizen
 *
 */
public class StorePager extends BasePager {

	public StorePager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initData() {
		tvTitle.setText("收藏");
		btnMenu.setVisibility(View.GONE);//隐藏菜单按钮

		
		TextView text = new TextView(mActivity);
		text.setText("收藏");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		
		//向FrameLayout中动态添加布局
		flContent.addView(text);
	}
}
