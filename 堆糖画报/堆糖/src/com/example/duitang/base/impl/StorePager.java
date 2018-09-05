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
 * �ղ�
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
		tvTitle.setText("�ղ�");
		btnMenu.setVisibility(View.GONE);//���ز˵���ť
//
//		
//		TextView text = new TextView(mActivity);
//		text.setText("��ҳ");
//		text.setTextColor(Color.RED);
//		text.setTextSize(25);
//		text.setGravity(Gravity.CENTER);
//		
//		//��FrameLayout�ж�̬��Ӳ���
//		flContent.addView(text);
		String cache = CacheUtils.getCache(NetInterface.BANNER,
				mActivity);
		if (!TextUtils.isEmpty(cache)) {// ����������,ֱ�ӽ�������, ���������·
			parseData(cache);
		}
		getDataFromServer();
	}
	
	/**
	 * �ӷ�������ȡ����
	 */
	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		
		//ʹ��xUtils��������
		utils.send(HttpMethod.GET, NetInterface.BANNER, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				
//				Log.i("tag", "���ؽ����"+result);
				parseData(result);
				// ���û���
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
	 * ������������
	 * @param result
	 */
	private void parseData(String result) {

		Gson gson = new Gson();
		mBannerData = gson.fromJson(result,BannerData.class);
		
//		BannerDatas data = mBannerData.data.get(0);

//		Log.i("tag", "�������:"+data.description);
		
		mPagers = new ArrayList<BaseMenuDetailpager>();
		mPagers.add(new StoreMenuDetail(mActivity,mBannerData.data));
		setCurrentMenuDetailPager(0);
	}

	private void setCurrentMenuDetailPager(int position) {
		BaseMenuDetailpager pager = mPagers.get(position);//��õ�ǰҪ��ʾ�Ĳ˵�����ҳ
		flContent.removeAllViews();//���֮ǰ�Ĳ���
		flContent.addView(pager.mRootView);//�������ļ�����֡����
		
		pager.initData();//��ʼ����ǰҳ����
	}
}
