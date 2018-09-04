package com.example.duitang;

import com.example.duitang.global.NetInterface;
import com.example.duitang.model.MainDetailData;
import com.example.duitang.model.MainDetailData.Data;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class DetailActivity extends Activity implements OnClickListener{

	private MainDetailData mMainDetailData;
	private ImageButton btnBack;
	private ImageButton btnShare;
	private String detailUrl;
	private ListView listView;
	private Data data;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_detail);

		btnBack = (ImageButton) findViewById(R.id.btn_back);
		btnShare = (ImageButton) findViewById(R.id.btn_share);
		listView = (ListView) findViewById(R.id.lv_list);
		
		btnBack.setOnClickListener(this);
		btnShare.setOnClickListener(this);

		detailUrl = NetInterface.MAINDETAIL + getIntent().getStringExtra("ID");
//		Log.i("tag", "url:" + detailUrl);
		getDataFromServer();
	    
		listView.setAdapter(new ListAdapter());
	}
	
	/**
	 * 新闻列表的适配器
	 * @author Wizen
	 *
	 */
	class ListAdapter extends BaseAdapter{

//		private BitmapUtils utils;
//	
//		public ListAdapter(){
//			utils = new BitmapUtils(null);
//        	utils.configDefaultLoadingImage(R.drawable.image_default);
//		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 3;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return 2;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			ViewHolderFirst holderFirst = null;
			ViewHolderSecond holderSecond = null;
			ViewHolderThird holderThird = null;
			
//			if (convertView == null) {
				
				LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
				switch (position) {
				case 0:
					convertView = layoutInflator.inflate(R.layout.home_detail_first, null);
//					holderFirst = new ViewHolderFirst();
//					holderFirst.ivPhoto =  (ImageView) convertView.findViewById(R.id.iv_d_photo);
//					holderFirst.tvMsg =  (TextView) convertView.findViewById(R.id.tv_d_msg);
//					holderFirst.tvData = (TextView) convertView.findViewById(R.id.tv_d_data);
//					holderFirst.ivAvatar =  (ImageView) convertView.findViewById(R.id.iv_d_avatar);
//					holderFirst.tvName =  (TextView) convertView.findViewById(R.id.tv_d_name);
//					holderFirst.tvUserName =  (TextView) convertView.findViewById(R.id.tv_d_username);
//	                
//					convertView.setTag(holderFirst);
	                
////	                utils.display(holderFirst.ivPhoto, item.photo.path);
//	                holderFirst.tvMsg.setText(item.msg);
//	                holderFirst.tvData.setText(item.add_datetime_pretty);
////	                utils.display(holderFirst.ivAvatar, item.sender.avatar);
//	                holderFirst.tvName.setText(item.album.name);
//	                holderFirst.tvUserName.setText("by:" + item.sender.username);
	                
					break;
		        case 1:
		        	convertView = layoutInflator.inflate(R.layout.home_detail_second, null);
//		        	holderSecond = new ViewHolderSecond();
//		        	holderSecond.tvLikeCount =  (TextView) convertView.findViewById(R.id.tv_like_count);
//		        	
//		        	convertView.setTag(holderSecond);
//		        	
//		        	 holderSecond.tvLikeCount.setText(item.like_count);
		        	 
		        	break;
		        case 2:
		        	convertView = layoutInflator.inflate(R.layout.home_detail_third, null);
//		        	holderThird = new ViewHolderThird();
//		        	holderThird.tvFavoriteCount =  (TextView) convertView.findViewById(R.id.tv_favorite_count);
//		        
//		        	convertView.setTag(holderThird);
//		        	 
//		        	holderThird.tvFavoriteCount.setText(item.favorite_count);
		        	 
		        	break;
				default:
					break;
				}
//            } 
//			Data item = (Data) getItem(position);
			Log.i("tag", "结果:"+getItem(position));
////            utils.display(holderFirst.ivPhoto, item.photo.path);
//            holderFirst.tvMsg.setText(item.msg);
//            holderFirst.tvData.setText(item.add_datetime_pretty);
////            utils.display(holderFirst.ivAvatar, item.sender.avatar);
//            holderFirst.tvName.setText(item.album.name);
//            holderFirst.tvUserName.setText("by:" + item.sender.username);
			return convertView;
		}
		
	}
	
	 class ViewHolderFirst {
			public ImageView ivPhoto;
			public TextView tvMsg;
			public TextView tvData;
			public ImageView ivAvatar;
			public TextView tvName;
			public TextView tvUserName;
		}
	 class ViewHolderSecond {
			public TextView tvLikeCount;
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

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(com.example.duitang.R.anim.slide_left_in,com.example.duitang.R.anim.slide_right_out);
	}
}
