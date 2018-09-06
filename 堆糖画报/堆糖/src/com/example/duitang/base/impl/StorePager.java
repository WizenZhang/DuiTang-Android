package com.example.duitang.base.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.duitang.base.BaseMenuDetailpager;
import com.example.duitang.base.BasePager;
import com.example.duitang.base.menu.StoreMenuDetail;
import com.example.duitang.db.Collection;
import com.example.duitang.db.DatabaseUtil;
import android.app.Activity;
import android.view.View;
import android.widget.Toast;

/**
 * �ղ�
 * @author Wizen
 *
 */
public class StorePager extends BasePager {

	private ArrayList<BaseMenuDetailpager>mPagers;

    private DatabaseUtil mUtil;
	
	private List<Collection> list = null;
	
	public StorePager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		tvTitle.setText("�ҵ��ղ�");
		btnMenu.setVisibility(View.GONE);//���ز˵���ť
//		Toast.makeText(mActivity, "�����У������ڴ�...", Toast.LENGTH_SHORT).show();	
		//��ȡ���ݿ�
		mUtil = new DatabaseUtil(mActivity);
		list = mUtil.queryAll();
		List<Map<String, Object>> templist = new ArrayList<Map<String,Object>>();
		if(list.size() != 0){	
			
			for(Collection collection:list){
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("_id", collection.getId());
				map.put("name", collection.getName());
				map.put("path", collection.getPath());
				templist.add(map);
			 }
			
//						Log.i("tag", "���ݽ��:"+templist);
//						Log.i("tag", "���ݽ��:"+templist.get(0).get("name"));
           }else{
	             Toast.makeText(mActivity, "�����ղأ����ղأ�", Toast.LENGTH_SHORT).show();
                }
		mPagers = new ArrayList<BaseMenuDetailpager>();
		mPagers.add(new StoreMenuDetail(mActivity,templist));
		setCurrentMenuDetailPager(0);
	}
	

	private void setCurrentMenuDetailPager(int position) {
		BaseMenuDetailpager pager = mPagers.get(position);//��õ�ǰҪ��ʾ�Ĳ˵�����ҳ
		flContent.removeAllViews();//���֮ǰ�Ĳ���
		flContent.addView(pager.mRootView);//�������ļ�����֡����
		
		pager.initData();//��ʼ����ǰҳ����
	}
}
