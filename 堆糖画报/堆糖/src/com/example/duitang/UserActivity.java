package com.example.duitang;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import com.dodowaterfall.Helper;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.example.duitang.DetailActivity.ListAdapter;
import com.example.duitang.global.NetInterface;
import com.example.duitang.model.BannerDetailData;
import com.example.duitang.model.BannerDetailData.Data;
import com.example.duitang.model.MainData;
import com.example.duitang.model.MainDetailData;
import com.example.duitang.model.MainData.ObjectList;
import com.example.duitang.utils.PrefUtils;
import com.example.duitang.view.RoundImageView;
import com.google.gson.Gson;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.huewu.pla.lib.internal.PLA_AdapterView.OnItemClickListener;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class UserActivity extends Activity implements OnClickListener,IXListViewListener{

	private BannerDetailData mBannerDetailData;
	private RadioButton btnBack;
	private String userUpUrl;
	private String userDownUrl;
	private MainData mMainData;
	private XListView xListView;//列表
	private LinkedList<ObjectList> mObjectListData;
    private XListAdapter mListAdapter;
	
	private ImageFetcher mImagesFetcher;

	ContentTask task = new ContentTask(this, 2);
	private View headerView;
	
	private Data data;
	private TextView tv_name;
	private TextView tv_count;
	private RoundImageView iv_avatar;
	private TextView tv_username;
	
	private ImageLoader mImageLoader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_activity);
		
		btnBack = (RadioButton) findViewById(R.id.btn_user_back);
		xListView = (XListView) findViewById(R.id.xlist);
		btnBack.setOnClickListener(this);
			
		userUpUrl = NetInterface.BANNERDETAILUP + getIntent().getStringExtra("ID");
		userDownUrl = NetInterface.BANNERDETAILDOWN + getIntent().getStringExtra("ID");
//		Log.i("tag", "url:" + getIntent().getStringExtra("name"));
		
		xListView();
		initHeadView();
		
		AddItemToContainer(2,userDownUrl);	
		
	}
	
	private void xListView() {
		xListView.setPullLoadEnable(true);
		xListView.setXListViewListener(this);
//      //加载头布局
		headerView = View.inflate(this, R.layout.user_header, null);
		ViewUtils.inject(this,headerView);
		xListView.addHeaderView(headerView);
		xListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(PLA_AdapterView<?> parent, View view,
					int position, long id) {
//				Log.i("tag", "被点击:" + position);
				// 在本地记录已读状态2
				String ids = PrefUtils.getString(parent.getContext(), "read_ids", "");
				String Id = mObjectListData.get(position).id;
				if (!ids.contains(Id)) {
					ids = ids + Id +",";
					PrefUtils.setString(parent.getContext(), "resd_ids", ids);
				}
				
				// mNewsAdapter.notifyDataSetChanged();
				changeReadState(view);// 实现局部界面刷新, 这个view就是被点击的item布局对象
				
				//跳转详情页
				Intent intent = new Intent();
				intent.setClass(UserActivity.this, DetailActivity.class);
				intent.putExtra("ID", mObjectListData.get(position).id);
				startActivity(intent);
				//设置切换动画，从右边进入，左边退出 
				overridePendingTransition(com.example.duitang.R.anim.slide_right_in,com.example.duitang.R.anim.slide_left_out);
			}
		});

		mListAdapter = new XListAdapter(this, xListView);
		xListView.setAdapter(mListAdapter);
	}
	
	private void initHeadView() {
		String name= getIntent().getStringExtra("name");
		String count= getIntent().getStringExtra("count");
		String like_count= getIntent().getStringExtra("like_count");
		String avatar= getIntent().getStringExtra("avatar");
		String username= getIntent().getStringExtra("username");
		
		tv_name= (TextView) headerView.findViewById(R.id.tv_user_name);
		tv_count= (TextView) headerView.findViewById(R.id.tv_user_count);
		iv_avatar= (RoundImageView) headerView.findViewById(R.id.iv_user_avatar);
		tv_username= (TextView) headerView.findViewById(R.id.tv_user_username);
		
		if (name == null) {
			getDataFromServer(userUpUrl);
		} else {
			tv_name.setText(name);
			tv_count.setText(count + "张图片" + "·" + like_count+"人收藏" );
//			BitmapUtils utils = new BitmapUtils(this);
//			utils.configDefaultLoadingImage(R.drawable.image_default);
//			utils.display(iv_avatar, avatar);
			 mImageLoader = initImageLoader(this, mImageLoader, "test");
			 mImageLoader.displayImage(avatar,iv_avatar);
			tv_username.setText("by:" + username);
		}
		
		
	}
	/**
	 * 从服务器获取数据
	 */
	private void getDataFromServer(String UpUrl) {
		HttpUtils utils = new HttpUtils();
//		Log.i("tag", "url："+UpUrl);
		utils.send(HttpMethod.GET,UpUrl, new RequestCallBack<String>() {

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
		mBannerDetailData = gson.fromJson(result,BannerDetailData.class);
        data = mBannerDetailData.data;
		if (null != data) {
			tv_name.setText(data.name);
			tv_count.setText(data.count + "张图片" + "·" + data.like_count+"人收藏" );
//			BitmapUtils utils = new BitmapUtils(this);
//			utils.configDefaultLoadingImage(R.drawable.image_default);
//			utils.display(iv_avatar, data.user.avatar);
			mImageLoader = initImageLoader(this, mImageLoader, "test");
			mImageLoader.displayImage(data.user.avatar,iv_avatar);
			tv_username.setText("by:" + data.user.username);
		}
    	
	}
	
	/**
	 * 改变已读新闻的颜色
	 */
	private void changeReadState(View view) {
		TextView tvMsg = (TextView) view.findViewById(R.id.tv_msg);
		TextView tvReply = (TextView) view.findViewById(R.id.bt_reply);
		TextView tvLike = (TextView) view.findViewById(R.id.bt_like);
		TextView tvFavorite = (TextView) view.findViewById(R.id.bt_favorite);
		TextView tvName =  (TextView) view.findViewById(R.id.tv_name);
		 
		tvMsg.setTextColor(Color.GRAY);
		tvReply.setTextColor(Color.GRAY);
		tvLike.setTextColor(Color.GRAY);
		tvFavorite.setTextColor(Color.GRAY);
		tvName.setTextColor(Color.GRAY);
	}
	/**
     * 添加内容
     * 
     * @param pageindex
     * @param type
     * 1为下拉刷新 2为加载更多
     */
    private void AddItemToContainer(int type,String url) {
        if (task.getStatus() != Status.RUNNING) {
//            String Url = "http://www.duitang.com/napi/blog/list/by_album/?include_fields=sender%2Calbum%2Cicon_url%2Creply_count%2Clike_count%2Ctop_comments%2Ctop_like_users&platform_version=4.1.2&device_platform=8295&screen_width=540&screen_height=960&start=0&app_version=57&album_id=64054110";
            ContentTask task = new ContentTask(this, type);
//            Log.i("tag", "url:" + url);
            task.execute(url);
        }
    }
    
	private class ContentTask extends AsyncTask<String, Integer, List<ObjectList>> {

        private Context mContext;
        private int mType = 1;

        public ContentTask(Context context, int type) {
            super();
            mContext = context;
            mType = type;
        }

        @Override
        protected List<ObjectList> doInBackground(String... params) {
            try {
                return parseNewsJSON(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<ObjectList> result) {
            if (mType == 1) {

            	mListAdapter.addItemTop(result);
            	mListAdapter.notifyDataSetChanged();
            	xListView.stopRefresh();

            } else if (mType == 2) {
            	xListView.stopLoadMore();
                mListAdapter.addItemLast(result);
                mListAdapter.notifyDataSetChanged();
            }

        }

        @Override
        protected void onPreExecute() {
        }

        public List<ObjectList> parseNewsJSON(String url) throws IOException {
            List<ObjectList> duitangs = new ArrayList<ObjectList>();
            String json = "";
            
            //从服务器获取数据
            if (Helper.checkConnection(mContext)) {
                try {
                    json = Helper.getStringFromUrl(url);
                } catch (IOException e) {
                    Log.e("IOException is : ", e.toString());
                    e.printStackTrace();
                    return duitangs;
                }
            }               
//            Log.d("tag", "json:" + json);
            //解析网络数据
            if (null != json) {
		            Gson gson = new Gson();
		 
		    		mMainData = gson.fromJson(json,MainData.class);
//		    		Log.d("tag", "json:" + mMainData.status);
		    		if (mMainData.status == 1) {
		    			duitangs = mMainData.data.object_list;
//					}else{
//						Toast.makeText(mContext, "该专辑不存在", Toast.LENGTH_SHORT).show();
					}
            }
            return duitangs;
        }
    }
	
	/**
	 * 列表的适配器
	 * @author Wizen
	 *
	 */
	class XListAdapter extends BaseAdapter{

		private Context mContext;
        private XListView mListView;
        private BitmapUtils utilsPhoto;
		private BitmapUtils utilsAvatar;
		
        public XListAdapter(Context context, XListView xListView) {
            mContext = context;
            mObjectListData = new LinkedList<ObjectList>();
            mListView = xListView;
            utilsPhoto= new BitmapUtils(mContext);
		    utilsPhoto.configDefaultLoadingImage(R.drawable.image_default);
		    utilsAvatar= new BitmapUtils(mContext);
		    utilsAvatar.configDefaultLoadingImage(R.drawable.image_default);
        }
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mObjectListData.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mObjectListData.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
	            ViewHolder holder;

	            if (convertView == null) {
	                LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
	                convertView = layoutInflator.inflate(R.layout.home_list_item, null);
	                
	                holder = new ViewHolder();
	                holder.ivPhoto =  (ImageView) convertView.findViewById(R.id.iv_photo);
					holder.tvMsg =  (TextView) convertView.findViewById(R.id.tv_msg);
	                holder.tvReply = (TextView) convertView.findViewById(R.id.bt_reply);
	                holder.tvLike = (TextView) convertView.findViewById(R.id.bt_like);
	                holder.tvFavorite = (TextView) convertView.findViewById(R.id.bt_favorite);
	                holder.ivAvatar =  (ImageView) convertView.findViewById(R.id.iv_avatar);
	                holder.tvName =  (TextView) convertView.findViewById(R.id.tv_name);
	                holder.tvAuthor =  (TextView) convertView.findViewById(R.id.tv_author);
	                
	                convertView.setTag(holder);
	            } else {
					holder = (ViewHolder) convertView.getTag();
				}
                   ObjectList item = mObjectListData.get(position);
       
                    utilsPhoto.display(holder.ivPhoto, item.photo.path);
	   			    holder.tvMsg.setText(item.msg);
	   			    holder.tvReply.setText(item.reply_count);
	   			    holder.tvLike.setText(item.like_count);
	   			    holder.tvFavorite.setText(item.favorite_count);
	   			    utilsAvatar.display(holder.ivAvatar, item.sender.avatar);
	   			    holder.tvName.setText(item.album.name);
	   			    holder.tvAuthor.setText("by:" + item.sender.username);
	   			    
	   			 String ids = PrefUtils.getString(mContext, "read_ids", "");
		 			if (ids.contains(mObjectListData.get(position).id)) {
		 				holder.tvMsg.setTextColor(Color.GRAY);
		 				holder.tvReply.setTextColor(Color.GRAY);
		 				holder.tvLike.setTextColor(Color.GRAY);
		 				holder.tvFavorite.setTextColor(Color.GRAY);
		 				holder.tvName.setTextColor(Color.GRAY);
		 			}else{
		 				holder.tvMsg.setTextColor(Color.BLACK);
		 				holder.tvReply.setTextColor(Color.BLACK);
		 				holder.tvLike.setTextColor(Color.BLACK);
		 				holder.tvFavorite.setTextColor(Color.BLACK);
		 				holder.tvName.setTextColor(Color.BLACK);
		 			}
	            return convertView;
		}

        class ViewHolder {
			public ImageView ivPhoto;
			public TextView tvMsg;
			public TextView tvReply;
			public TextView tvLike;
			public TextView tvFavorite;
			public ImageView ivAvatar;
			public TextView tvName;
			public TextView tvAuthor;
		}
		
        public void addItemLast(List<ObjectList> datas) {
        	mObjectListData.addAll(datas);
        }

        public void addItemTop(List<ObjectList> datas) {
            for (ObjectList info : datas) {
            	mObjectListData.addFirst(info);
            }
        }
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_user_back:
			finish();
			overridePendingTransition(com.example.duitang.R.anim.slide_left_in,com.example.duitang.R.anim.slide_right_out);
			break;
		default:
			break;
		}
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(com.example.duitang.R.anim.slide_left_in,com.example.duitang.R.anim.slide_right_out);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
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
	
}
