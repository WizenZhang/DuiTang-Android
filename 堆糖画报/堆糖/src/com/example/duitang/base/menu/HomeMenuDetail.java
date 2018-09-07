package com.example.duitang.base.menu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.AsyncTask.Status;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import com.dodowaterfall.Helper;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.example.duitang.DetailActivity;
import com.example.duitang.R;
import com.example.duitang.UserActivity;
import com.example.duitang.base.BaseMenuDetailpager;
import com.example.duitang.global.NetInterface;
import com.example.duitang.model.BannerData.BannerDatas;
import com.example.duitang.model.MainData;
import com.example.duitang.model.MainData.ObjectList;
import com.example.duitang.utils.NetworkUtils;
import com.example.duitang.utils.PrefUtils;
import com.google.gson.Gson;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.huewu.pla.lib.internal.PLA_AdapterView.OnItemClickListener;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

public class HomeMenuDetail extends BaseMenuDetailpager implements IXListViewListener,OnPageChangeListener{
	
	private MainData mMainData;
	private ArrayList<BannerDatas> mTopData;
//	private ArrayList<ObjectList> mObjectListData;
	private LinkedList<ObjectList> mObjectListData;
	
	@ViewInject(R.id.tv_title)
	private TextView tvTitle;//�ֲ�����
	
	@ViewInject(R.id.list)
	private XListView xListView;//�б�
	
	@ViewInject(R.id.vp_banner)
	private ViewPager mViewPager;
	
	@ViewInject(R.id.indicator)
	private CirclePageIndicator mIndicator;//�ֲ�λ��ָʾ��
	
	private Handler mHandler;
	
	private XListAdapter mListAdapter;
	private int currentPage = 0;
	private ImageFetcher mImagesFetcher;

	ContentTask task = new ContentTask(mActivity, 2);
	
	public HomeMenuDetail(Activity activity, ArrayList<BannerDatas> data) {
		super(activity);
		mTopData = data;
//		Log.i("tag", "���ݽ��:"+ mTopData);
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
		    		if (mMainData.status == 1) {
		    			
		    			duitangs = mMainData.data.object_list;
		    			// ���û���
//						CacheUtils.setCache(url,
//								json, mActivity);		    			
					}	
//		    		Log.d("tag", "json:" + duitangs);
            }
            return duitangs;
        }
    }

	/**
     * �������
     * 
     * @param pageindex
     * @param type
     * 1Ϊ����ˢ�� 2Ϊ���ظ���
     */
    private void AddItemToContainer(int type,int pageindex) {
    	 if (NetworkUtils.isNetworkAvailable(mActivity)){
	        if (task.getStatus() != Status.RUNNING) {
	            String url = NetInterface.MAIN + pageindex;
	           
            	 ContentTask task = new ContentTask(mActivity, type);
//               Log.i("tag", "url:" + url);
//               String cache = CacheUtils.getCache(url,
//       				mActivity);
//       		Log.i("tag", "������:"+cache);
//       		parseData(cache);
               task.execute(url);
		    }
        }
    }

	/**
	 * �б��������
	 * @author Wizen
	 *
	 */
	class XListAdapter extends BaseAdapter{
        private BitmapUtils utilsPhoto;
		private BitmapUtils utilsAvatar;
		
        public XListAdapter(Context context, XListView xListView) {
            mObjectListData = new LinkedList<ObjectList>();
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
		public Object getItem(int position) {

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
	   			    
	   			 String ids = PrefUtils.getString(mActivity, "read_ids", "");
//	   			Log.i("tag", "getString" + ids);
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
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.home_activity, null);

		//����ͷ����
		View headerView = View.inflate(mActivity, R.layout.home_header, null);
		
		ViewUtils.inject(this,view);
		ViewUtils.inject(this,headerView);
		
		//��ͷ���ֵ���ʽ���ظ�listView
		mViewPager.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Log.i("tag", "�����:" + arg0);
			}
		});

		xListView.addHeaderView(headerView);
		xListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(PLA_AdapterView<?> parent, View view,
					int position, long id) {
//				Log.i("tag", "�����:" + position);
				// �ڱ��ؼ�¼�Ѷ�״̬2
				String ids = PrefUtils.getString(mActivity, "read_ids", "");
				String readId = mObjectListData.get(position).id;
				if (!ids.contains(readId)) {
					ids = ids + readId + ",";
//					Log.i("tag", "setString" + ids);
					PrefUtils.setString(mActivity, "read_ids", ids);
				}
				
				// mNewsAdapter.notifyDataSetChanged();
				changeReadState(view);// ʵ�־ֲ�����ˢ��, ���view���Ǳ������item���ֶ���
				
				//��ת����ҳ
				Intent intent = new Intent();
				intent.setClass(mActivity, DetailActivity.class);
				intent.putExtra("Back","��ҳ");
				intent.putExtra("ID", mObjectListData.get(position).id);
				mActivity.startActivity(intent);
				//�����л����������ұ߽��룬����˳� 
				mActivity.overridePendingTransition(com.example.duitang.R.anim.slide_right_in,com.example.duitang.R.anim.slide_left_out);
			}
		});
		return view;
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
	
	@Override
	public void initData() {
		
		xListView.setPullLoadEnable(true);
		xListView.setXListViewListener(this);
      
		mListAdapter = new XListAdapter(mActivity, xListView);
		xListView.setAdapter(mListAdapter);
//        
        mImagesFetcher = new ImageFetcher(mActivity, 240);
        mImagesFetcher.setLoadingImage(R.drawable.empty_photo);
        mImagesFetcher.setExitTasksEarly(false);
        
        if (mTopData!= null) {
            mViewPager.setAdapter(new TopNewsAdapter());
            mIndicator.setViewPager(mViewPager);
    		mIndicator.setSnap(true);// ֧�ֿ�����ʾ
            mIndicator.setOnPageChangeListener(this);
            mIndicator.onPageSelected(0);// ��ָʾ�����¶�λ����һ����	
            
 		    autoPlay();// �Զ��ֲ�����ʾ
 		}
       
        AddItemToContainer(2,0);
	}

	private void autoPlay() {
        
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
	 * HeadView�ֲ���������
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
			image.setScaleType(ScaleType.FIT_XY);//���ڿؼ���С���ͼƬ
			
			BannerDatas bannerData = mTopData.get(position);
			utils.display(image, bannerData.image_url);//����imageView�����ͼƬ��ַ
			
			container.addView(image);
			
			String str= bannerData.target;
			final String newID = str.substring(str.indexOf("id=")+3,str.length());
			
//			image.setOnTouchListener(new TopTouchListener());//���ô�������
			image.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
//					   Log.i("tag", "id:" + newID);
					   //��ת����ҳ
						Intent intent = new Intent();
						intent.setClass(mActivity, UserActivity.class);
						intent.putExtra("Back","��ҳ");
						intent.putExtra("ID", newID);
						mActivity.startActivity(intent);
						//�����л����������ұ߽��룬����˳� 
						mActivity.overridePendingTransition(com.example.duitang.R.anim.slide_right_in,com.example.duitang.R.anim.slide_left_out);
				}
			});
			return image;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}
	
	/**
	 * ͷ�����ŵĴ�������
	 * 
	 * @author Kevin
	 * 
	 */
	class TopTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				System.out.println("����");
				mHandler.removeCallbacksAndMessages(null);// ɾ��Handler�е�������Ϣ

				break;
			case MotionEvent.ACTION_CANCEL:
				System.out.println("�¼�ȡ��");
				mHandler.sendEmptyMessageDelayed(0, 3000);
				break;
			case MotionEvent.ACTION_UP:
				System.out.println("̧��");
				mHandler.sendEmptyMessageDelayed(0, 3000);
				break;

			default:
				break;
			}

			return true;
		}

	}

	@Override
	public void onRefresh() {
		
		AddItemToContainer(1,0);
	}


	@Override
	public void onLoadMore() {
		if (NetworkUtils.isNetworkAvailable(mActivity))
        {
			if (mMainData.status == 1) {
				if (currentPage <mMainData.data.total) {
					AddItemToContainer(2,currentPage+=24);			
				}else{
					Toast.makeText(mActivity, "û�и�����", Toast.LENGTH_SHORT).show();
					return;
				}
			}	
        }
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

		
	}

	@Override
	public void onPageSelected(int position) {
		BannerDatas bannerData = mTopData.get(position);
		tvTitle.setText(bannerData.description);
		
	}
	
}
