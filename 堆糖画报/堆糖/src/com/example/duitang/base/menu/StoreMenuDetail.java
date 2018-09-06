package com.example.duitang.base.menu;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duitang.DetailActivity;
import com.example.duitang.R;
import com.example.duitang.base.BaseMenuDetailpager;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;


public class StoreMenuDetail extends BaseMenuDetailpager { 


	private List<Map<String, Object>> mStoreData;
	@ViewInject(R.id.gv_list)
	private GridView gridView;//列表

	public StoreMenuDetail(Activity activity, List<Map<String, Object>> data) {
		super(activity);
		mStoreData = data;
//		Log.i("tag", "传递结果:"+ mTopData);
	}

	@Override
	public void initData() {
		gridView.setAdapter(new GridViewAdapter(mActivity));
	}
	
	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.collection_activity, null);
		
		ViewUtils.inject(this,view);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				Log.i("tag", (String)mStoreData.get(position).get("_id"));
				String Id = String.valueOf(mStoreData.get(position).get("_id")) ;
				//跳转详情页
				Intent intent = new Intent();
				intent.setClass(mActivity, DetailActivity.class);
				intent.putExtra("ID", Id);
				intent.putExtra("Back","我的收藏");
				mActivity.startActivity(intent);
				//设置切换动画，从右边进入，左边退出 
				mActivity.overridePendingTransition(com.example.duitang.R.anim.slide_right_in,com.example.duitang.R.anim.slide_left_out);
			}
			
		});
		return view;
	}
	
	/**
	 * 列表的适配器
	 * @author Wizen
	 *
	 */
	class GridViewAdapter extends BaseAdapter{

		private Context mContext;

        private BitmapUtils utilsPhoto;

		
        public GridViewAdapter(Context context) {
            mContext = context;
            utilsPhoto= new BitmapUtils(mActivity);
		    utilsPhoto.configDefaultLoadingImage(R.drawable.image_default);
        }
		@Override
		public int getCount() {

			return mStoreData.size();
		}

		@Override
		public Object getItem(int position) {

			return mStoreData.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
	            ViewHolder holder;

	            if (convertView == null) {
	                LayoutInflater layoutInflator = LayoutInflater.from(mContext);
	                convertView = layoutInflator.inflate(R.layout.collection_item, null);
              
	                holder = new ViewHolder();
	                holder.ivPhoto =  (ImageView) convertView.findViewById(R.id.iv_photo);
	                holder.tvName =  (TextView) convertView.findViewById(R.id.tv_name);
               
	                convertView.setTag(holder);
	            } else {
					holder = (ViewHolder) convertView.getTag();
				}
                   Map<String,Object> item = mStoreData.get(position);
                   utilsPhoto.display(holder.ivPhoto,  (String)item.get("path"));
	   			    holder.tvName.setText( (String)item.get("name"));

	            return convertView;
		}

        class ViewHolder {
			public ImageView ivPhoto;
			public TextView tvName;
		}
		
	}

}
