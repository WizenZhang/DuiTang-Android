package com.example.duitang.base.impl;

import java.util.ArrayList;

import com.example.duitang.base.BaseMenuDetailpager;
import com.example.duitang.base.BasePager;

import com.example.duitang.base.menu.StoreMenuDetail;
import com.example.duitang.global.NetInterface;
import com.example.duitang.model.BannerData;
import com.example.duitang.utils.CacheUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 收藏
 * @author Wizen
 *
 */
public class StorePager extends BasePager {

	private ArrayList<BaseMenuDetailpager>mPagers;
	private BannerData mBannerData;

	public StorePager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initData() {
		tvTitle.setText("收藏");
		btnMenu.setVisibility(View.GONE);//隐藏菜单按钮
//
//		
//		TextView text = new TextView(mActivity);
//		text.setText("首页");
//		text.setTextColor(Color.RED);
//		text.setTextSize(25);
//		text.setGravity(Gravity.CENTER);
//		
//		//向FrameLayout中动态添加布局
//		flContent.addView(text);
		String cache = CacheUtils.getCache(NetInterface.BANNER,
				mActivity);
		if (!TextUtils.isEmpty(cache)) {// 如果缓存存在,直接解析数据, 无需访问网路
			parseData(cache);
		}
		getDataFromServer();
	}
	
	/**
	 * 从服务器获取数据
	 */
	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		
		//使用xUtils发送请求
		utils.send(HttpMethod.GET, NetInterface.BANNER, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				
//				Log.i("tag", "返回结果："+result);
				parseData(result);
				// 设置缓存
				CacheUtils.setCache(NetInterface.BANNER,
						result, mActivity);
			}

			

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				error.printStackTrace();
				
			}
		});
	}
	
	/**
	 * 解析网络数据
	 * @param result
	 */
	private void parseData(String result) {

		Gson gson = new Gson();
		mBannerData = gson.fromJson(result,BannerData.class);
		
//		BannerDatas data = mBannerData.data.get(0);

//		Log.i("tag", "解析结果:"+data.description);
		
		mPagers = new ArrayList<BaseMenuDetailpager>();
		mPagers.add(new StoreMenuDetail(mActivity,mBannerData.data));
		setCurrentMenuDetailPager(0);
	}

	private void setCurrentMenuDetailPager(int position) {
		BaseMenuDetailpager pager = mPagers.get(position);//获得当前要显示的菜单详情页
		flContent.removeAllViews();//清除之前的布局
		flContent.addView(pager.mRootView);//将布局文件设置帧布局
		
		pager.initData();//初始化当前页数据
	}
}
