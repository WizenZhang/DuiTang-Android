package com.example.duitang;

import java.io.File;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import com.example.duitang.db.Collection;
import com.example.duitang.db.DatabaseUtil;
import com.example.duitang.global.NetInterface;
import com.example.duitang.model.MainDetailData;
import com.example.duitang.model.MainDetailData.Data;
import com.example.duitang.utils.NetworkUtils;
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class DetailActivity extends Activity implements OnClickListener{

	private MainDetailData mMainDetailData;
	private RadioButton btnBack;
	private ImageButton btnShare;
	private String detailUrl;
	private ListView listView;
	private Data data;
	private ImageLoader mAvatarLoader;
	private ImageLoader mLikeLoader;

	private DatabaseUtil mDBUtil;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_detail);

		btnBack = (RadioButton) findViewById(R.id.btn_back);
		btnShare = (ImageButton) findViewById(R.id.btn_share);
		listView = (ListView) findViewById(R.id.lv_list);
		btnBack.setText(getIntent().getStringExtra("Back"));
		
		
		btnBack.setOnClickListener(this);
		btnShare.setOnClickListener(this);

		detailUrl = NetInterface.MAINDETAIL + getIntent().getStringExtra("ID");

		getDataFromServer();
	
	}
	
    
	/**
	 * 列表的适配器
	 * @author Wizen
	 *
	 */
	class ListAdapter extends BaseAdapter{
		private Context mContext;
		private BitmapUtils utils;
		private LinearLayout like_users;
		private LinearLayout related_albums;
		private boolean checked;
		
		public ListAdapter(Context context, ListView listView){
			mContext = context;
			utils = new BitmapUtils(mContext);
        	utils.configDefaultLoadingImage(R.drawable.image_default);
        	mAvatarLoader = initImageLoader(mContext, mAvatarLoader, "Avatar");
        	mLikeLoader = initImageLoader(mContext, mLikeLoader, "Like");
		}
		@Override
		public int getCount() {

			return 3;
		}

		@Override
		public Object getItem(int position) {

			return data;
		}

		@Override
		public long getItemId(int position) {

			return 0;
		}

		@Override
		public View getView(int position, View convertView, final ViewGroup parent) {
			
			ViewHolderFirst holderFirst = null;
			ViewHolderSecond holderSecond = null;
			ViewHolderThird holderThird = null;
			final Data item = (Data) getItem(position);
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
                    holderFirst.rlDetail = (RelativeLayout) convertView.findViewById(R.id.rl_detail);
					convertView.setTag(holderFirst);
	                
	                utils.display(holderFirst.ivPhoto, item.photo.path);
	                holderFirst.tvMsg.setText(item.msg);
	                holderFirst.tvData.setText(item.add_datetime_pretty);
//	                utils.display(holderFirst.ivAvatar, item.sender.avatar);
	                
	                mAvatarLoader.displayImage(item.sender.avatar,holderFirst.ivAvatar);
	                holderFirst.tvName.setText("收藏到 " + item.album.name);
	                holderFirst.tvUserName.setText(item.sender.username);
	                holderFirst.rlDetail.setOnClickListener(new OnClickListener() {
					
                	//设置图片点击监听
 					final String id_detail = data.album.id;
						@Override
						public void onClick(View arg0) {
//							Log.i("tag", "点击了btDetail");
							//跳转详情页
								Intent intent = new Intent();
								intent.setClass(DetailActivity.this, UserActivity.class);
								intent.putExtra("ID", id_detail);
								intent.putExtra("Back","详情");
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
		        	holderSecond.btCollection = (RadioButton) convertView.findViewById(R.id.bt_collection);
		        	
		        	convertView.setTag(holderSecond);
		        	
		        	holderSecond.tvLikeCount.setText("赞  " + item.top_like_users.size());
//		        	Log.i("tag", "btCollection.isChecked:"+String.valueOf(holderSecond.btCollection.isChecked()));
		        	
		        	like_users = (LinearLayout) convertView.findViewById(R.id.ll_like_users);
		        	
		        	//获取数据库
		    		mDBUtil = new DatabaseUtil(mContext);
		    		checked = mDBUtil.queryByid(item.id);
		    		holderSecond.btCollection.setChecked(mDBUtil.queryByid(item.id));
//		    		checked = holderSecond.btCollection.isChecked();
//					Log.i("tag", "数据结果:"+checked);
		        	holderSecond.btCollection.setOnClickListener(new OnClickListener() {	
						@Override
						public void onClick(View v) {											
							checked=!checked;
//							Log.i("tag", "checked:"+String.valueOf(item.id));
							((CompoundButton) v).setChecked(checked);
							if (checked == true) {
								Collection collection = new Collection();
								collection.setId(item.id);
								collection.setName(item.sender.username);
								collection.setPath(item.photo.path);
								if(mDBUtil.Insert(collection)){
									Toast.makeText(mContext, "收藏成功", Toast.LENGTH_SHORT).show();
								}
							} else {
								mDBUtil.Delete(item.id);
								Toast.makeText(mContext, "收藏已取消", Toast.LENGTH_SHORT).show();
							}
							
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
		 										intent.putExtra("Back","详情");
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
			public RelativeLayout rlDetail;
		}
	 class ViewHolderSecond {
			public TextView tvLikeCount;
			public RadioButton btCollection;
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
				// 设置缓存
//				CacheUtils.setCache(NetInterface.BANNER,
//						result, mActivity);
//				Log.i("tag", "返回结果："+result);
				
				 parseData(result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(DetailActivity.this, "No network connection found.", Toast.LENGTH_SHORT).show();
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
			if (NetworkUtils.isNetworkAvailable(this)){
			showShare();
			}else {
			Toast.makeText(this, "No network connection found.", Toast.LENGTH_SHORT).show();	
			}
			break;

		default:
			break;
		}
		
	}
	
	private void showShare() {
		 ShareSDK.initSDK(this);
		 OnekeyShare oks = new OnekeyShare();
		 //关闭sso授权
		 oks.disableSSOWhenAuthorize(); 

		// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
		 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		 oks.setTitle(data.album.name);
		 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		 oks.setTitleUrl(data.photo.path);
		 // text是分享文本，所有平台都需要这个字段
		 oks.setText(data.msg);
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		 //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		 // url仅在微信（包括好友和朋友圈）中使用
		 oks.setUrl("http://sharesdk.cn");
		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		 oks.setComment("我是测试评论文本");
		 // site是分享此内容的网站名称，仅在QQ空间使用
		 oks.setSite(getString(R.string.app_name));
		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		 oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		 oks.show(this);
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

		super.onBackPressed();
		finish();
		overridePendingTransition(com.example.duitang.R.anim.slide_left_in,com.example.duitang.R.anim.slide_right_out);
	}
	
}
