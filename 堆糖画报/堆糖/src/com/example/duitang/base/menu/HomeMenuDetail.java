package com.example.duitang.base.menu;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;

import com.example.duitang.R;
import com.example.duitang.base.BaseMenuDetailpager;
import com.example.duitang.model.BannerData;
import com.example.duitang.model.BannerData.BannerDatas;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

public class HomeMenuDetail extends BaseMenuDetailpager implements OnPageChangeListener{
	
	@ViewInject(R.id.vp_banner)
	private ViewPager mViewPager;
	
	private BitmapUtils utils;
	private BannerData mBannerData;
	private ArrayList<BannerDatas> mTopData;
	
	@ViewInject(R.id.tv_title)
	private TextView tvTitle;//轮播标题
	
	@ViewInject(R.id.indicator)
	private CirclePageIndicator mIndicator;//轮播位置指示器
	
	@ViewInject(R.id.lv_list)
	private ListView lvList;//列表
	
	private Handler mHandler;
	
	private ListAdapter mListAdapter;
	
	public HomeMenuDetail(Activity activity, ArrayList<BannerDatas> data) {
		super(activity);
		mTopData = data;
//		Log.i("tag", "传递结果:"+ mTopData);
	}

	@Override
	public View initViews() {
		
		View view = View.inflate(mActivity, R.layout.home_list, null);
		//加载头布局
		View headerView = View.inflate(mActivity, R.layout.home_header, null);
		
		ViewUtils.inject(this,view);
		ViewUtils.inject(this,headerView);
		
		//以头布局的形式加载给listView
		lvList.addHeaderView(headerView);
//		mViewPager.setOnPageChangeListener(this);
		return view;
	}
	
	@Override
	public void initData() {
		
//		if (mTopData != null) {
			mListAdapter = new ListAdapter();
			lvList.setAdapter(mListAdapter);
//		}
//		Log.i("tag", "传递结果:"+ mTopData);
		mViewPager.setAdapter(new TopNewsAdapter());
		
		autoPlay();// 自动轮播条显示
	}
	private void autoPlay() {
		mIndicator.setViewPager(mViewPager);
		mIndicator.setSnap(true);// 支持快照显示
        mIndicator.setOnPageChangeListener(this);
        mIndicator.onPageSelected(0);// 让指示器重新定位到第一个点
        
		if (mHandler == null) {
			mHandler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					int currentItem = mViewPager.getCurrentItem();

					if (currentItem < mTopData.size() - 1) {
						currentItem++;
					} else {
						currentItem = 0;
					}

					mViewPager.setCurrentItem(currentItem);// 切换到下一个页面
					mHandler.sendEmptyMessageDelayed(0, 3000);// 继续延时3秒发消息,
																// 形成循环
				};
			};

			mHandler.sendEmptyMessageDelayed(0, 3000);// 延时3秒后发消息
		}
	}
	/**
	 * 头条新闻适配器
	 * @author Wizen
	 *
	 */
	class TopNewsAdapter extends PagerAdapter{

		public TopNewsAdapter () {
			utils = new BitmapUtils(mActivity);
			utils.configDefaultLoadingImage(R.drawable.image_default);
		}
		@Override
		public int getCount() {
			return mTopData.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view ==object;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView image = new ImageView(mActivity);
			image.setImageResource(R.drawable.image_default);
			image.setScaleType(ScaleType.FIT_XY);//基于控件大小填充图片
			
			BannerDatas bannerData = mTopData.get(position);
			utils.display(image, bannerData.image_url);//传递imageView对象和图片地址
			
			container.addView(image);
			
			return image;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}
	
	/**
	 * 新闻列表的适配器
	 * @author Wizen
	 *
	 */
	class ListAdapter extends BaseAdapter{

		private BitmapUtils utils;
		
		 public ListAdapter(){
	        	utils = new BitmapUtils(mActivity);
	        	utils.configDefaultLoadingImage(R.drawable.image_default);
	        }
		@Override
		public int getCount() {
			return mTopData.size();
		}

		@Override
		public Object getItem(int position) {
			return mTopData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = View.inflate(mActivity, R.layout.list_news_item, null);
			return convertView;
		}
		
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// TODO Auto-generated method stub
		
	}

	@Override       
	public void onPageSelected(int position) {
		BannerDatas bannerData = mTopData.get(position);
		tvTitle.setText(bannerData.description);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub
		
	}
}
