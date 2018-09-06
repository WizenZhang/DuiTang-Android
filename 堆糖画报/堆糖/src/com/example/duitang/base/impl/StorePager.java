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
 * 收藏
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
		tvTitle.setText("我的收藏");
		btnMenu.setVisibility(View.GONE);//隐藏菜单按钮
//		Toast.makeText(mActivity, "开发中，敬请期待...", Toast.LENGTH_SHORT).show();	
		//获取数据库
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
			
//						Log.i("tag", "数据结果:"+templist);
//						Log.i("tag", "数据结果:"+templist.get(0).get("name"));
           }else{
	             Toast.makeText(mActivity, "暂无收藏，请收藏！", Toast.LENGTH_SHORT).show();
                }
		mPagers = new ArrayList<BaseMenuDetailpager>();
		mPagers.add(new StoreMenuDetail(mActivity,templist));
		setCurrentMenuDetailPager(0);
	}
	

	private void setCurrentMenuDetailPager(int position) {
		BaseMenuDetailpager pager = mPagers.get(position);//获得当前要显示的菜单详情页
		flContent.removeAllViews();//清除之前的布局
		flContent.addView(pager.mRootView);//将布局文件设置帧布局
		
		pager.initData();//初始化当前页数据
	}
}
