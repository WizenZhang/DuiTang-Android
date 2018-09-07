package com.example.duitang;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import com.dodowaterfall.Helper;
import com.example.duitang.global.NetInterface;
import com.example.duitang.model.CategoryDetail;
import com.example.duitang.model.CategoryDetail.Sub_Cates;
import com.example.duitang.model.MainData;
import com.example.duitang.model.MainData.ObjectList;
import com.example.duitang.utils.NetworkUtils;
import com.example.duitang.utils.PrefUtils;
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
import android.widget.GridView;
import android.widget.ImageView;

import android.widget.RadioButton;

import android.widget.TextView;
import android.widget.Toast;

public class CategoryActivity extends Activity implements OnClickListener,IXListViewListener{

	private CategoryDetail mCategoryDetail;
	private TextView tvTitle;

	private RadioButton btnBack;
	private String CategoryUpUrl;

	private MainData mMainData;
	private XListView xListView;//�б�
	private GridView gridView;//�б�
	private LinkedList<ObjectList> mObjectListData;
    private XListAdapter mListAdapter;
    private int currentPage = 0;

    private GridAdapter mGridAdapter;
	
	ContentTask task = new ContentTask(this, 2);
	private View headerView;
	
//	private ImageView iv_background;
	
//	Handler handler = new Handler(){
//    	//�˷��������߳��е��ã���������ˢ��UI
//    	public void handleMessage(android.os.Message msg) {
//    		iv_background.setImageBitmap((Bitmap)msg.obj);
//    	};
//    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_activity);
		
		tvTitle =(TextView)findViewById(R.id.tv_title);
		btnBack = (RadioButton) findViewById(R.id.btn_user_back);
		xListView = (XListView) findViewById(R.id.xlist);

		
		tvTitle.setText(getIntent().getStringExtra("Title"));
		btnBack.setText("����");
		
		btnBack.setOnClickListener(this);
			
		CategoryUpUrl = NetInterface.CATEGORYDETAILUP + getIntent().getStringExtra("Key");
		
//		Log.i("tag", "title:" + getIntent().getStringExtra("Name"));
		
		xListView();
		initHeadView();
		
		AddItemToContainer(2,0);	
		
	}
	
	private void xListView() {
		xListView.setPullLoadEnable(true);
		xListView.setXListViewListener(this);
//      //����ͷ����
		headerView = View.inflate(this, R.layout.category_header, null);
		ViewUtils.inject(this,headerView);
		gridView = (GridView) headerView.findViewById(R.id.gv_header);
		
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
					PrefUtils.setString(parent.getContext(), "read_ids", ids);
				}
				
				// mNewsAdapter.notifyDataSetChanged();
				changeReadState(view);// ʵ�־ֲ�����ˢ��, ���view���Ǳ������item���ֶ���
				
				//��ת����ҳ
				Intent intent = new Intent();
				intent.setClass(CategoryActivity.this, DetailActivity.class);
				intent.putExtra("ID", mObjectListData.get(position).id);
				intent.putExtra("Back",getIntent().getStringExtra("Title"));
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

//		iv_background = (ImageView) headerView.findViewById(R.id.iv_header);
		getDataFromServer(CategoryUpUrl);	
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
				Toast.makeText(CategoryActivity.this, "No network connection found.", Toast.LENGTH_SHORT).show();
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
		mCategoryDetail = gson.fromJson(result,CategoryDetail.class);
//		Log.i("tag", "url:" + mCategoryDetail.data.sub_cates.get(0).name);
		if (null != mCategoryDetail) {
//			Log.i("tag", "size:" + mCategoryDetail.data.sub_cates.size());
			mGridAdapter = new GridAdapter();
			gridView.setAdapter(mGridAdapter);
//		     Log.i("tag", "size:" + String.valueOf(tvItem.getMeasuredHeight()));
			//�Զ���gridView�߶�
			ViewGroup.LayoutParams params = gridView.getLayoutParams();
			//dp��px��ϵ��dp=px/�豸�ܶ�
			//��ȡ�豸�ܶȣ��ͷֱ����й�
			float density = getResources().getDisplayMetrics().density;
//			Toast.makeText(this, String.valueOf(density), Toast.LENGTH_SHORT).show();
			int row = mCategoryDetail.data.sub_cates.size()/3;
			int column= mCategoryDetail.data.sub_cates.size()%3;
			if (column == 0) {
				params.height =  (int) (30*row*density);
			}else{
				params.height =  (int) (30*(row+1)*density);
			}
			gridView.setLayoutParams(params);
			
		}
    	
	}
	/**
	 * �б��������
	 * @author Wizen
	 *
	 */
	class GridAdapter extends BaseAdapter{

		@Override
		public int getCount() {

			return mCategoryDetail.data.sub_cates.size();
		}

		@Override
		public Object getItem(int position) {

			return mCategoryDetail.data.sub_cates.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(final int position, View convertView,ViewGroup parent) {
			HeadViewHolder holder;
			if (convertView == null) {
				holder = new HeadViewHolder();
				convertView = View.inflate(parent.getContext(), R.layout.category_header_item, null);
			    holder.tvCategore = (TextView) convertView.findViewById(R.id.tv_category);
			    convertView.setTag(holder);
			}else{
				holder = (HeadViewHolder) convertView.getTag();
			}
			final Sub_Cates item = (Sub_Cates) getItem(position);
			holder.tvCategore.setText("#" + item.name);
			holder.tvCategore.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String str;
					String strUTF8 = null;
					if (item.theme_name.contains("Heap_")) {
						str = mCategoryDetail.data.name + "_" + item.name;	
					}else{
						str = item.theme_name;	
					}
					
					try {
						 strUTF8 = URLEncoder.encode(str, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					
					//��ת����ҳ
					Intent intent = new Intent();
					intent.setClass(CategoryActivity.this, CategoryUpDetail.class);
					intent.putExtra("strUTF8", strUTF8);
					intent.putExtra("Back", getIntent().getStringExtra("Title"));
					intent.putExtra("Title", item.name);
//					intent.putExtra("Key", Key);
					startActivity(intent);
					//�����л����������ұ߽��룬����˳� 
					overridePendingTransition(com.example.duitang.R.anim.slide_right_in,com.example.duitang.R.anim.slide_left_out);
				}
			});
			return convertView;
		}
		
	}

	static class HeadViewHolder {
		public TextView tvCategore;
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
    private void AddItemToContainer(int type,int pageindex) {
    	if (NetworkUtils.isNetworkAvailable(this)){
        if (task.getStatus() != Status.RUNNING) {
        	String DownUrl = NetInterface.CATEGORYDETAIL + pageindex+"&platform_name=Android&locale=zh&app_code=nayutas&cate_key="+getIntent().getStringExtra("Key");
            ContentTask task = new ContentTask(this, type);
            task.execute(DownUrl);
          }
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
		    		
//		    		final String Path=mMainData.data.object_list.get(0).photo.path;
		    		if (mMainData.status == 1) {
		    			duitangs = mMainData.data.object_list;
		    			//ģ��ͼƬ
//		    			Thread t = new Thread(){
//		                	@Override
//		                	public void run() {
//		                	Bitmap blurBitmap = FastBlurUtil.GetUrlBitmap(Path, 10);          	
//		                	Message msg = new Message();
//		                	//��Ϣ�������Я������
//		                	msg.obj = blurBitmap;
//		                	//����Ϣ���������̵߳���Ϣ����
//		                	handler.sendMessage(msg);
//		                	}
//		                };
//		                t.start();
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
//        private XListView mListView;
        private BitmapUtils utilsPhoto;
		private BitmapUtils utilsAvatar;
		
        public XListAdapter(Context context, XListView xListView) {
            mContext = context;
            mObjectListData = new LinkedList<ObjectList>();
//            mListView = xListView;
            utilsPhoto= new BitmapUtils(mContext);
		    utilsPhoto.configDefaultLoadingImage(R.drawable.image_default);
		    utilsAvatar= new BitmapUtils(mContext);
		    utilsAvatar.configDefaultLoadingImage(R.drawable.image_default);

        }
		@Override
		public int getCount() {

			return mObjectListData.size();
		}

		@Override
		public Object getItem(int positoin) {

			return mObjectListData.get(positoin);
		}

		@Override
		public long getItemId(int positoin) {

			return positoin;
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

		super.onBackPressed();
		finish();
		overridePendingTransition(com.example.duitang.R.anim.slide_left_in,com.example.duitang.R.anim.slide_right_out);
	}

	@Override
	public void onRefresh() {
		
		AddItemToContainer(1,0);
		
	}

	@Override
	public void onLoadMore() {
		if (NetworkUtils.isNetworkAvailable(this))
        {
			if (mMainData.status == 1) {
				if (currentPage <mMainData.data.total) {
					AddItemToContainer(2,currentPage+=24);			
				}else{
					Toast.makeText(this, "û�и�����", Toast.LENGTH_SHORT).show();
					return;
				}
			}	
        }
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
