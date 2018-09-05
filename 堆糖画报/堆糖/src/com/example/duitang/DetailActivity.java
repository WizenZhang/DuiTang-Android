package com.example.duitang;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import me.maxwin.view.XListView;
import com.example.duitang.global.NetInterface;
import com.example.duitang.model.MainDetailData;
import com.example.duitang.model.BannerData.BannerDatas;
import com.example.duitang.model.MainData.ObjectList;
import com.example.duitang.model.MainDetailData.Data;
import com.example.duitang.model.MainDetailData.LikeUser;
import com.example.duitang.view.RoundImageView;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;


public class DetailActivity extends Activity implements OnClickListener{

	private MainDetailData mMainDetailData;
	private RadioButton btnBack;
	private ImageButton btnShare;
	private String detailUrl;
	private ListView listView;
	private Data data;
	private ImageLoader mAvatarLoader;
	private ImageLoader mLikeLoader;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_detail);

		btnBack = (RadioButton) findViewById(R.id.btn_back);
		btnShare = (ImageButton) findViewById(R.id.btn_share);
		listView = (ListView) findViewById(R.id.lv_list);
		
		
		btnBack.setOnClickListener(this);
		btnShare.setOnClickListener(this);

		detailUrl = NetInterface.MAINDETAIL + getIntent().getStringExtra("ID");
//		Log.i("tag", "url:" + detailUrl);
		getDataFromServer();
	}
	
	/**
	 * 列表的适配器
	 * @author Wizen
	 *
	 */
	class ListAdapter extends BaseAdapter{
		private Context mContext;
        private ListView mListView;
		private BitmapUtils utils;
		private LinearLayout like_users;
		private LinearLayout related_albums;
		
		public ListAdapter(Context context, ListView listView){
			mContext = context;
            mListView = listView;
			utils = new BitmapUtils(mContext);
        	utils.configDefaultLoadingImage(R.drawable.image_default);
        	mAvatarLoader = initImageLoader(mContext, mAvatarLoader, "Avatar");
        	mLikeLoader = initImageLoader(mContext, mLikeLoader, "Like");
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 3;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, final ViewGroup parent) {
			
			ViewHolderFirst holderFirst = null;
			ViewHolderSecond holderSecond = null;
			ViewHolderThird holderThird = null;
			Data item = (Data) getItem(position);
//			if (convertView == null) {
			
				LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
				switch (position) {
				case 0:
					convertView = layoutInflator.inflate(R.layout.home_detail_first, null);
					holderFirst = new ViewHolderFirst();
					holderFirst.ivPhoto =  (ImageView) convertView.findViewById(R.id.iv_d_photo);
					holderFirst.tvMsg =  (TextView) convertView.findViewById(R.id.tv_d_msg);
					holderFirst.tvData = (TextView) convertView.findViewById(R.id.tv_d_data);
					holderFirst.ivAvatar =  (RoundImageView) convertView.findViewById(R.id.iv_d_avatar);
					holderFirst.tvName =  (TextView) convertView.findViewById(R.id.tv_d_name);
					holderFirst.tvUserName =  (TextView) convertView.findViewById(R.id.tv_d_username);
                    holderFirst.btDetail = (Button) convertView.findViewById(R.id.bt_detail);
					convertView.setTag(holderFirst);
	                
	                utils.display(holderFirst.ivPhoto, item.photo.path);
	                holderFirst.tvMsg.setText(item.msg);
	                holderFirst.tvData.setText(item.add_datetime_pretty);
//	                utils.display(holderFirst.ivAvatar, item.sender.avatar);
	                
	                mAvatarLoader.displayImage(item.sender.avatar,holderFirst.ivAvatar);
	                holderFirst.tvName.setText("收藏到 " + item.album.name);
	                holderFirst.tvUserName.setText(item.sender.username);
	                holderFirst.btDetail.setOnClickListener(new OnClickListener() {
					
                	//设置图片点击监听
 					final String id_detail = data.album.id;
						@Override
						public void onClick(View arg0) {
//							Log.i("tag", "点击了btDetail");
							//跳转详情页
								Intent intent = new Intent();
								intent.setClass(DetailActivity.this, UserActivity.class);
								intent.putExtra("ID", id_detail);
								startActivity(intent);
								//设置切换动画，从右边进入，左边退出 
								overridePendingTransition(com.example.duitang.R.anim.slide_right_in,com.example.duitang.R.anim.slide_left_out);			
						}
					});
					break;
		        case 1:
		        	convertView = layoutInflator.inflate(R.layout.home_detail_second, null);
		        	holderSecond = new ViewHolderSecond();
		        	holderSecond.tvLikeCount =  (TextView) convertView.findViewById(R.id.tv_like_count);
		        	holderSecond.btCollection = (Button) convertView.findViewById(R.id.bt_collection);
		        	convertView.setTag(holderSecond);
		        	
		        	holderSecond.tvLikeCount.setText("赞  " + item.top_like_users.size());
		        	
		        	like_users = (LinearLayout) convertView.findViewById(R.id.ll_like_users);
		        	holderSecond.btCollection.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							Log.i("tag", "btCollection");
						}
					});
		 	    	if (data.top_like_users!= null) {
		 	    		for (int i = 0; i < data.top_like_users.size(); i++) {
		 					View like_user_item = LayoutInflater.from(parent.getContext()).inflate(
		 							R.layout.like_user_item, null);
		 					RoundImageView icon = (RoundImageView) like_user_item
		 							.findViewById(R.id.iv_like_user);// 拿个这行的icon 就可以设置图片
//		 					utils.display(icon, data.top_like_users.get(i).avatar);
		 					
		 					mLikeLoader.displayImage(data.top_like_users.get(i).avatar,icon);
		 					like_users.addView(like_user_item);
		 				}
					}
		 	    	
		        	break;
		        case 2:
		        	convertView = layoutInflator.inflate(R.layout.home_detail_third, null);
		        	holderThird = new ViewHolderThird();
		        	holderThird.tvFavoriteCount =  (TextView) convertView.findViewById(R.id.tv_favorite_count);
		        
		        	convertView.setTag(holderThird);
		        	 
		        	holderThird.tvFavoriteCount.setText(item.favorite_count);
		        	
		        	related_albums = (LinearLayout) convertView.findViewById(R.id.ll_related_albums);
		        	
		 	    	if (data.related_albums!= null) {
		 	    		for (int i = 0; i < data.related_albums.size(); i++) {
		 					View related_album_item = LayoutInflater.from(parent.getContext()).inflate(
		 							R.layout.relate_album_item, null);
		 					ImageView image = (ImageView) related_album_item
		 							.findViewById(R.id.iv_related_album);
		 					TextView first =  (TextView) related_album_item
		 							.findViewById(R.id.tv_first);
		 					TextView name =  (TextView) related_album_item
		 							.findViewById(R.id.tv_name);
		 					TextView username =  (TextView) related_album_item
		 							.findViewById(R.id.tv_username);
		 					
		 					String url = (String) data.related_albums.get(i).covers.get(0);
		 					utils.display(image,url);
		 					if (i == 0) {
		 						first.setVisibility(1);
							}
		 					name.setText(data.related_albums.get(i).name);
		 					username.setText("by:" + data.related_albums.get(i).user.username);
		 					
		 					//设置图片点击监听
		 					final String id = data.related_albums.get(i).id;
		 					
		 					if (!TextUtils.isEmpty(id)) {
		 						related_album_item.setOnClickListener(new OnClickListener() {// 每个item的点击事件加在这里

		 									@Override
		 									public void onClick(View v) {
		 										//跳转详情页
		 										Intent intent = new Intent();
		 										intent.setClass(DetailActivity.this, UserActivity.class);
		 										intent.putExtra("ID", id);
		 										startActivity(intent);
		 										//设置切换动画，从右边进入，左边退出 
		 										overridePendingTransition(com.example.duitang.R.anim.slide_right_in,com.example.duitang.R.anim.slide_left_out);
		 									}
		 								});
		 					}
		 					related_albums.addView(related_album_item);
		 				}
					}
		 	    	
		        	break;
				default:
					break;
				}

			return convertView;
		}
		
	}
	
	 class ViewHolderFirst {
			public ImageView ivPhoto;
			public TextView tvMsg;
			public TextView tvData;
			public RoundImageView ivAvatar;
			public TextView tvName;
			public TextView tvUserName;
			public Button btDetail;
		}
	 class ViewHolderSecond {
			public TextView tvLikeCount;
			public Button btCollection;
		}
	 class ViewHolderThird {
			public TextView tvFavoriteCount;
		}
	/**
	 * 从服务器获取数据
	 */
	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, detailUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = (String) responseInfo.result;
//				Log.i("tag", "返回结果："+result);
				 parseData(result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				
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
		mMainDetailData = gson.fromJson(result,MainDetailData.class);
	
		data = mMainDetailData.data;
		
		if (data!= null) {
	    	listView.setAdapter(new ListAdapter(this, listView));
		}
//		Log.i("tag", "解析结果:"+data.msg);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			overridePendingTransition(com.example.duitang.R.anim.slide_left_in,com.example.duitang.R.anim.slide_right_out);
			break;
		case R.id.btn_share:
//			showShare();
			break;

		default:
			break;
		}
		
	}
	
	/**
	 * 初始化图片下载器，图片缓存地址<i>("/Android/data/[app_package_name]/cache/dirName")</i>
	 */
	public ImageLoader initImageLoader(Context context,
			ImageLoader imageLoader, String dirName) {
		imageLoader = ImageLoader.getInstance();
		if (imageLoader.isInited()) {
			// 重新初始化ImageLoader时,需要释放资源.
			imageLoader.destroy();
		}
		imageLoader.init(initImageLoaderConfig(context, dirName));
		return imageLoader;
	}

	/**
	 * 配置图片下载器
	 * 
	 * @param dirName
	 *            文件名
	 */
	private ImageLoaderConfiguration initImageLoaderConfig(
			Context context, String dirName) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.threadPoolSize(3).memoryCacheSize(getMemoryCacheSize(context))
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.discCache(new UnlimitedDiscCache(new File(dirName)))
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		return config;
	}

	private int getMemoryCacheSize(Context context) {
		int memoryCacheSize;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
			int memClass = ((ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE))
					.getMemoryClass();
			memoryCacheSize = (memClass / 8) * 1024 * 1024; // 1/8 of app memory
															// limit
		} else {
			memoryCacheSize = 2 * 1024 * 1024;
		}
		return memoryCacheSize;
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(com.example.duitang.R.anim.slide_left_in,com.example.duitang.R.anim.slide_right_out);
	}
	
}
