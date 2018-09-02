package com.example.duitang.base.menu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.AsyncTask.Status;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import com.dodowaterfall.Helper;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.example.duitang.R;
import com.example.duitang.base.BaseMenuDetailpager;
import com.example.duitang.global.NetInterface;
import com.example.duitang.model.BannerData.BannerDatas;
import com.example.duitang.model.MainData;
import com.example.duitang.model.MainData.ObjectList;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

public class HomeMenuDetail extends BaseMenuDetailpager implements IXListViewListener,OnPageChangeListener{
	
	private MainData mMainData;
	private ArrayList<BannerDatas> mTopData;
//	private ArrayList<ObjectList> mObjectListData;
	private LinkedList<ObjectList> mObjectListData;
	
	@ViewInject(R.id.list)
	private XListView xListView;//列表
	
	@ViewInject(R.id.vp_banner)
	private ViewPager mViewPager;
	
	@ViewInject(R.id.indicator)
	private CirclePageIndicator mIndicator;//轮播位置指示器
	
	private Handler mHandler;
	
	private XListAdapter mListAdapter;
	
	private ImageFetcher mImagesFetcher;

	ContentTask task = new ContentTask(mActivity, 2);
	
	public HomeMenuDetail(Activity activity, ArrayList<BannerDatas> data) {
		super(activity);
		mTopData = data;
//		Log.i("tag", "传递结果:"+ mTopData);
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
		    		duitangs = mMainData.data.object_list;
//		    		Log.d("tag", "json:" + duitangs);
            }
            return duitangs;
        }
    }

	
	/**
     * 添加内容
     * 
     * @param pageindex
     * @param type
     * 1为下拉刷新 2为加载更多
     */
    private void AddItemToContainer(int type) {
        if (task.getStatus() != Status.RUNNING) {
//            String url = "http://www.duitang.com/album/1733789/masn/p/" + pageindex + "/24/";
            ContentTask task = new ContentTask(mActivity, type);
            task.execute(NetInterface.MAIN);

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
            utilsPhoto= new BitmapUtils(mActivity);
		    utilsPhoto.configDefaultLoadingImage(R.drawable.image_default);
		    utilsAvatar= new BitmapUtils(mActivity);
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
	                convertView = layoutInflator.inflate(R.layout.list_item, null);
	                
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
                   ObjectList item = mObjectListData.get(position);

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

        class ViewHolder {
			public ImageView ivPhoto;
			public TextView tvMsg;
			public Button btReply;
			public Button btLike;
			public Button btFavorite;
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
		View view = View.inflate(mActivity, R.layout.home_list, null);

		//加载头布局
		View headerView = View.inflate(mActivity, R.layout.home_header, null);
		
		ViewUtils.inject(this,view);
		ViewUtils.inject(this,headerView);
		
		//以头布局的形式加载给listView
		
		xListView.addHeaderView(headerView);
		
		return view;
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
    		mIndicator.setSnap(true);// 支持快照显示
            mIndicator.setOnPageChangeListener(this);
            mIndicator.onPageSelected(0);// 让指示器重新定位到第一个点	
            
 		    autoPlay();// 自动轮播条显示
 		}
        
        AddItemToContainer(2);
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

					mViewPager.setCurrentItem(currentItem);// 切换到下一个页面
					mHandler.sendEmptyMessageDelayed(0, 3000);// 继续延时3秒发消息,
																// 形成循环
				};
			};

			mHandler.sendEmptyMessageDelayed(0, 3000);// 延时3秒后发消息
		}
	}
	
	/**
	 * HeadView轮播条适配器
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
	@Override
	public void onRefresh() {
		AddItemToContainer(1);
	}


	@Override
	public void onLoadMore() {
		AddItemToContainer(2);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
