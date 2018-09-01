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
	private TextView tvTitle;//�ֲ�����
	
	@ViewInject(R.id.indicator)
	private CirclePageIndicator mIndicator;//�ֲ�λ��ָʾ��
	
	@ViewInject(R.id.lv_list)
	private ListView lvList;//�б�
	
	private Handler mHandler;
	
	private ListAdapter mListAdapter;
	
	public HomeMenuDetail(Activity activity, ArrayList<BannerDatas> data) {
		super(activity);
		mTopData = data;
//		Log.i("tag", "���ݽ��:"+ mTopData);
	}

	@Override
	public View initViews() {
		
		View view = View.inflate(mActivity, R.layout.home_list, null);
		//����ͷ����
		View headerView = View.inflate(mActivity, R.layout.home_header, null);
		
		ViewUtils.inject(this,view);
		ViewUtils.inject(this,headerView);
		
		//��ͷ���ֵ���ʽ���ظ�listView
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
//		Log.i("tag", "���ݽ��:"+ mTopData);
		mViewPager.setAdapter(new TopNewsAdapter());
		
		autoPlay();// �Զ��ֲ�����ʾ
	}
	private void autoPlay() {
		mIndicator.setViewPager(mViewPager);
		mIndicator.setSnap(true);// ֧�ֿ�����ʾ
        mIndicator.setOnPageChangeListener(this);
        mIndicator.onPageSelected(0);// ��ָʾ�����¶�λ����һ����
        
		if (mHandler == null) {
			mHandler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					int currentItem = mViewPager.getCurrentItem();

					if (currentItem < mTopData.size() - 1) {
						currentItem++;
					} else {
						currentItem = 0;
					}

					mViewPager.setCurrentItem(currentItem);// �л�����һ��ҳ��
					mHandler.sendEmptyMessageDelayed(0, 3000);// ������ʱ3�뷢��Ϣ,
																// �γ�ѭ��
				};
			};

			mHandler.sendEmptyMessageDelayed(0, 3000);// ��ʱ3�����Ϣ
		}
	}
	/**
	 * ͷ������������
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
			image.setScaleType(ScaleType.FIT_XY);//���ڿؼ���С���ͼƬ
			
			BannerDatas bannerData = mTopData.get(position);
			utils.display(image, bannerData.image_url);//����imageView�����ͼƬ��ַ
			
			container.addView(image);
			
			return image;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}
	
	/**
	 * �����б��������
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
