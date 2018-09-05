package com.example.duitang.base.impl;

import java.util.ArrayList;

import com.example.duitang.R;
import com.example.duitang.base.BaseMenuDetailpager;
import com.example.duitang.base.BasePager;
import com.example.duitang.base.menu.HomeMenuDetail;
import com.example.duitang.global.CategoryImageUrl;
import com.example.duitang.global.NetInterface;
import com.example.duitang.model.BannerData;
import com.example.duitang.model.CategoryData;
import com.example.duitang.model.CategoryData.Datas;
import com.example.duitang.model.CategoryData.Group_Items;
import com.example.duitang.utils.CacheUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 分类
 * @author Wizen
 *
 */
public class CategoryPager extends BasePager{
	
	private CategoryData mCategoryData;
	
	@ViewInject(R.id.lv_category_list)
	private ListView cateListView;
	
	
	public CategoryPager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initData() {
		tvTitle.setText("分类");
		btnMenu.setVisibility(View.GONE);//隐藏菜单按钮
		View view = View.inflate(mActivity, R.layout.activity_category, null);
		ViewUtils.inject(this,view);
		//向FrameLayout中动态添加布局
		flContent.removeAllViews();//清除之前的布局
		flContent.addView(view);
		getDataFromServer();
	}

	/**
	 * 从服务器获取数据
	 */
	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		
		//使用xUtils发送请求
		utils.send(HttpMethod.GET, NetInterface.CATEGORY, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				parseData(result);
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
		mCategoryData = gson.fromJson(result,CategoryData .class);
		if (mCategoryData.status == 1) {
			cateListView.setAdapter(new CateListAdapter());
		}
	}
	/**
	 * 列表的适配器
	 * @author Wizen
	 *
	 */
	class CateListAdapter extends BaseAdapter{
		private BitmapUtils utils;
		
		public CateListAdapter(){
        	utils = new BitmapUtils(mActivity);
        	utils.configDefaultLoadingImage(R.drawable.image_default);
        }
		@Override
		public int getCount() {

			return mCategoryData.data.size()-1;
		}

		@Override
		public Object getItem(int position) {
	
			return mCategoryData.data;
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView==null) {
				convertView = View.inflate(mActivity, R.layout.category_list_item, null);
				holder =new ViewHolder();
				holder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_group_name);
			    holder.tv_name11 = (TextView) convertView.findViewById(R.id.tv_name11);
			    holder.tv_name12 = (TextView) convertView.findViewById(R.id.tv_name12);
			    holder.tv_name21 = (TextView) convertView.findViewById(R.id.tv_name21);
			    holder.tv_name22 = (TextView) convertView.findViewById(R.id.tv_name22);
			    holder.iv_name11 = (ImageView) convertView.findViewById(R.id.iv_name11);
			    holder.iv_name12 = (ImageView) convertView.findViewById(R.id.iv_name12);
			    holder.iv_name21 = (ImageView) convertView.findViewById(R.id.iv_name21);
			    holder.iv_name22 = (ImageView) convertView.findViewById(R.id.iv_name22);
			    convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			Datas item = mCategoryData.data.get(position+1);
			holder.tv_group_name.setText(item.group_name);
			holder.tv_name11.setText(item.group_items.get(0).name);
			holder.tv_name12.setText(item.group_items.get(1).name);
			holder.tv_name21.setText(item.group_items.get(2).name);
			holder.tv_name22.setText(item.group_items.get(3).name);
			switch (position) {
			case 0:
				utils.display(holder.iv_name11,CategoryImageUrl.FUZHUANG);
				utils.display(holder.iv_name12,CategoryImageUrl.XIEBAOPEISHI);
				utils.display(holder.iv_name21,CategoryImageUrl.DAPEI);
				utils.display(holder.iv_name22,CategoryImageUrl.JIAJURIZA);
				break;
			case 1:
				utils.display(holder.iv_name11,CategoryImageUrl.SHISHANGJIEPAI);
				utils.display(holder.iv_name12,CategoryImageUrl.MEIFAZAOXING);
				utils.display(holder.iv_name21,CategoryImageUrl.MEIRONGMEIZHUANG);
				utils.display(holder.iv_name22,CategoryImageUrl.HUNSHAHUNLI);
				break;
			case 2:
				utils.display(holder.iv_name11,CategoryImageUrl.JIAJUSHENGHUO);
				utils.display(holder.iv_name12,CategoryImageUrl.SHOUGONGDIY);
				utils.display(holder.iv_name21,CategoryImageUrl.MEISHICAIPU);
				utils.display(holder.iv_name22,CategoryImageUrl.CHAHUAHUIHUA);
				break;
			case 3:
				utils.display(holder.iv_name11,CategoryImageUrl.MENGCHONG);
				utils.display(holder.iv_name12,CategoryImageUrl.TOUXIANGBIAOQING);
				utils.display(holder.iv_name21,CategoryImageUrl.BIZHI);
				utils.display(holder.iv_name22,CategoryImageUrl.MEITUSHEYING);
				break;
			case 4:
				utils.display(holder.iv_name11,CategoryImageUrl.YINGYINSHU);
				utils.display(holder.iv_name12,CategoryImageUrl.WENZIJUZI);
				utils.display(holder.iv_name21,CategoryImageUrl.LVXING);
				utils.display(holder.iv_name22,CategoryImageUrl.SHEJI);
				break;
			default:
				break;
			}
			
			holder.tv_name11.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Log.i("tag", "点击了"+ position +v.getTag());
					
				}
			});
            holder.tv_name12.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Log.i("tag", "点击了"+ position +v.getTag());
					
				}
			});
            holder.tv_name21.setOnClickListener(new OnClickListener() {
	
	             @Override
	             public void onClick(View v) {
		
		         Log.i("tag", "点击了"+ position +v.getTag());
		
				}
			});
			holder.tv_name22.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Log.i("tag", "点击了"+ position +v.getTag());
					
				}
			});
			return convertView;
		}
		
	}
	static class ViewHolder{
		public TextView tv_group_name;
		public TextView tv_name11;
		public TextView tv_name12;
		public TextView tv_name21;
		public TextView tv_name22;
		public ImageView iv_name11;
		public ImageView iv_name12;
		public ImageView iv_name21;
		public ImageView iv_name22;
		
		}

}
