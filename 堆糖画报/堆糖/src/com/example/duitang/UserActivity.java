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
	private XListView xListView;//�б�
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
	private ImageView iv_background;
	
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
//      //����ͷ����
		headerView = View.inflate(this, R.layout.user_header, null);
		ViewUtils.inject(this,headerView);
		xListView.addHeaderView(headerView);
		xListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(PLA_AdapterView<?> parent, View view,
					int position, long id) {
				if (position > -1) {
//				Log.i("tag", "�����:" + position);
				// �ڱ��ؼ�¼�Ѷ�״̬2
				String ids = PrefUtils.getString(parent.getContext(), "read_ids", "");
				String Id = mObjectListData.get(position).id;
				if (!ids.contains(Id)) {
					ids = ids + Id +",";
					PrefUtils.setString(parent.getContext(), "resd_ids", ids);
				}
				
				// mNewsAdapter.notifyDataSetChanged();
				changeReadState(view);// ʵ�־ֲ�����ˢ��, ���view���Ǳ������item���ֶ���
				
				//��ת����ҳ
				Intent intent = new Intent();
				intent.setClass(UserActivity.this, DetailActivity.class);
				intent.putExtra("ID", mObjectListData.get(position).id);
				startActivity(intent);
				//�����л����������ұ߽��룬����˳� 
				overridePendingTransition(com.example.duitang.R.anim.slide_right_in,com.example.duitang.R.anim.slide_left_out);
			
				}
			}
		});

		mListAdapter = new XListAdapter(this, xListView);
		xListView.setAdapter(mListAdapter);
	}
	
	private void initHeadView() {	
		tv_name = (TextView) headerView.findViewById(R.id.tv_user_name);
		tv_count = (TextView) headerView.findViewById(R.id.tv_user_count);
		iv_avatar = (RoundImageView) headerView.findViewById(R.id.iv_user_avatar);
		tv_username = (TextView) headerView.findViewById(R.id.tv_user_username);
		iv_background = (ImageView) headerView.findViewById(R.id.iv_background);
		getDataFromServer(userUpUrl);	
	}
	/**
	 * �ӷ�������ȡ����
	 */
	private void getDataFromServer(String UpUrl) {
		HttpUtils utils = new HttpUtils();
//		Log.i("tag", "url��"+UpUrl);
		utils.send(HttpMethod.GET,UpUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = (String) responseInfo.result;
//				Log.i("tag", "���ؽ����"+result);
				 parseData(result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				
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
		mBannerDetailData = gson.fromJson(result,BannerDetailData.class);
        data = mBannerDetailData.data;
		if (null != data) {
			tv_name.setText(data.name);
			tv_count.setText(data.count + "��ͼƬ" + "��" + data.like_count+"���ղ�" );
//			BitmapUtils utils = new BitmapUtils(this);
//			utils.configDefaultLoadingImage(R.drawable.image_default);
//			utils.display(iv_avatar, data.user.avatar);
			mImageLoader = initImageLoader(this, mImageLoader, "ImageLoader");
			mImageLoader.displayImage(data.user.avatar,iv_avatar);
			tv_username.setText("by:" + data.user.username);
		    BitmapUtils utils = new BitmapUtils(this);
			utils.display(iv_background,(String)data.covers.get(0));
		}
    	
	}
	
	/**
	 * �ı��Ѷ����ŵ���ɫ
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
     * �������
     * 
     * @param pageindex
     * @param type
     * 1Ϊ����ˢ�� 2Ϊ���ظ���
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
            
            //�ӷ�������ȡ����
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
            //������������
            if (null != json) {
		            Gson gson = new Gson();
		    		mMainData = gson.fromJson(json,MainData.class);
//		    		Log.d("tag", "json:" + mMainData.data.object_list.get(0).photo.path);
		    		if (mMainData.status == 1) {
		    			duitangs = mMainData.data.object_list;
//					}else{
//						Toast.makeText(mContext, "��ר��������", Toast.LENGTH_SHORT).show();
					}
            }
            return duitangs;
        }
    }
	
	/**
	 * �б��������
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
//		    BitmapUtils utils = new BitmapUtils(mContext);
//			utils.configDefaultLoadingImage(R.drawable.image_default);
//			
//			utils.display(iv_background,mObjectListData.get(0).photo.path);
//    		mBgImageLoader = initImageLoader(mContext, mBgImageLoader, "BgImageLoader");
//    		mBgImageLoader.displayImage(mMainData.data.object_list.get(0).photo.path,iv_background);
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
	 * ��ʼ��ͼƬ��������ͼƬ�����ַ<i>("/Android/data/[app_package_name]/cache/dirName")</i>
	 */
	public ImageLoader initImageLoader(Context context,
			ImageLoader imageLoader, String dirName) {
		imageLoader = ImageLoader.getInstance();
		if (imageLoader.isInited()) {
			// ���³�ʼ��ImageLoaderʱ,��Ҫ�ͷ���Դ.
			imageLoader.destroy();
		}
		imageLoader.init(initImageLoaderConfig(context, dirName));
		return imageLoader;
	}

	/**
	 * ����ͼƬ������
	 * 
	 * @param dirName
	 *            �ļ���
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
