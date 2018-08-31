package com.example.duitang.base.impl;

import com.example.duitang.base.BasePager;

import android.app.Activity;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * 分类
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
		tvTitle.setText("分类");
//		btnMenu.setVisibility(View.GONE);//隐藏菜单按钮
		
		TextView text = new TextView(mActivity);
		text.setText("分类");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		
		//向FrameLayout中动态添加布局
		flContent.addView(text);
	}
}
