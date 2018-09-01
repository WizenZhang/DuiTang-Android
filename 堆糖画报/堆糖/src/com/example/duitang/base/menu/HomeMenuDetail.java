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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;

import com.example.duitang.R;
import com.example.duitang.base.BaseMenuDetailpager;
import com.example.duitang.global.NetInterface;
import com.example.duitang.model.BannerData;
import com.example.duitang.model.BannerData.BannerDatas;
import com.example.duitang.model.MainData;
import com.example.duitang.model.MainData.ObjectList;
import com.example.duitang.view.GridViewWithHeaderAndFooter;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

public class HomeMenuDetail extends BaseMenuDetailpager implements OnPageChangeListener{
	
	@ViewInject(R.id.vp_banner)
	private ViewPager mViewPager;

	private BannerData mBannerData;
	private ArrayList<BannerDatas> mTopData;
	
	private ArrayList<ObjectList> mObjectListData;
	
	private MainData mMainData;
	@ViewInject(R.id.tv_title)
	private TextView tvTitle;//轮播标题
	
	@ViewInject(R.id.indicator)
	private CirclePageIndicator mIndicator;//轮播位置指示器
	
	@ViewInject(R.id.gv_list)
	private GridViewWithHeaderAndFooter gridView;//列表
	
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
		gridView.addHeaderView(headerView); 
//		mViewPager.setOnPageChangeListener(this);
		return view;
	}
	
	@Override
	public void initData() {
		

//		Log.i("tag", "传递结果:"+ mTopData);
		mViewPager.setAdapter(new TopNewsAdapter());
		
		autoPlay();// 自动轮播条显示
		
		getDataFromServer();
		
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
	 * 从服务器获取数据
	 */
	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		
		//使用xUtils发送请求
		utils.send(HttpMethod.GET, NetInterface.MAIN, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				
//				Log.i("tag", "返回结果："+result);
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
		mMainData = gson.fromJson(result,MainData.class);
		
		mObjectListData = mMainData.data.object_list;

		if (mObjectListData != null) {
			mListAdapter = new ListAdapter();
			gridView.setAdapter(mListAdapter);
		}
//		Log.i("tag", "解析结果:"+ mObjectListData.size());
		
	}
	/**
	 * 头条新闻适配器
	 * @author Wizen
	 *
	 */
	class TopNewsAdapter extends PagerAdapter{
		private BitmapUtils utils;
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
	 * 列表的适配器
	 * @author Wizen
	 *
	 */
	class ListAdapter extends BaseAdapter{

		private BitmapUtils utilsPhoto;
		private BitmapUtils utilsAvatar;
		
		 public ListAdapter(){
			    utilsPhoto= new BitmapUtils(mActivity);
			    utilsPhoto.configDefaultLoadingImage(R.drawable.image_default);
			    utilsAvatar= new BitmapUtils(mActivity);
			    utilsAvatar.configDefaultLoadingImage(R.drawable.image_default);
	        }
		@Override
		public int getCount() {
			return mObjectListData.size();
		}

		@Override
		public ObjectList getItem(int position) {
			return mObjectListData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.list_item, null);

				holder = new ViewHolder();
				holder.ivPhoto =  (ImageView) convertView.findViewById(R.id.iv_photo);
				holder.tvMsg =  (TextView) convertView.findViewById(R.id.tv_msg);
                holder.btReply = (Button) convertView.findViewById(R.id.bt_reply);
                holder.btLike = (Button) convertView.findViewById(R.id.bt_like);
                holder.btFavorite = (Button) convertView.findViewById(R.id.bt_favorite);
                holder.ivAvatar =  (ImageView) convertView.findViewById(R.id.iv_avatar);
                holder.tvName =  (TextView) convertView.findViewById(R.id.tv_name);
                holder.tvAuthor =  (TextView) convertView.findViewById(R.id.tv_author);
                
                convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			    ObjectList item = getItem(position);
			    utilsPhoto.display(holder.ivPhoto, item.photo.path);
			    holder.tvMsg.setText(item.msg);
			    holder.btReply.setText(item.reply_count);
			    holder.btLike.setText(item.like_count);
			    holder.btFavorite.setText(item.favorite_count);
			    utilsAvatar.display(holder.ivAvatar, item.sender.avatar);
			    holder.tvName.setText(item.album.name);
			    holder.tvAuthor.setText("by:" + item.sender.username);
			    
			return convertView;
		}
		
	}

	static class ViewHolder {
		public ImageView ivPhoto;
		public TextView tvMsg;
		public Button btReply;
		public Button btLike;
		public Button btFavorite;
		public ImageView ivAvatar;
		public TextView tvName;
		public TextView tvAuthor;
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
