package com.example.duitang.fragmen;

import java.util.ArrayList;

import com.example.duitang.R;
import com.example.duitang.base.BasePager;
import com.example.duitang.base.impl.CategoryPager;
import com.example.duitang.base.impl.HomePager;
import com.example.duitang.base.impl.StorePager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ContentFragment extends BaseFragment {

	@ViewInject(R.id.rg_group)

	private RadioGroup rgGroup;
    
    @ViewInject(R.id.vp_content)
    
    private ViewPager mViewPager;
    
    private ArrayList<BasePager>mPagerList;
    
    
    
	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.fragment_content, null);
//		rgGroup = (RadioGroup) view.findViewById(R.id.rg_group);
		ViewUtils.inject(this,view);//注入view和事件
		return view;
	}
    
	public void initData() {
		rgGroup.check(R.id.rb_home);//默认勾选首页
		//初始化五个子页面
		mPagerList = new ArrayList<BasePager>();

		mPagerList.add(new HomePager(mActivity));
		mPagerList.add(new CategoryPager(mActivity));
		mPagerList.add(new StorePager(mActivity));
		
		mViewPager.setAdapter(new ContentAdapter());
		
		//监听RadioGroup的选择事件
		rgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_home:
//					mViewPager.setCurrentItem(0);
					mViewPager.setCurrentItem(0, false);
					break;
				case R.id.rb_category:
					mViewPager.setCurrentItem(1, false);
					break;
				case R.id.rb_store:
					mViewPager.setCurrentItem(2, false);
					break;
				default:
					break;
				}
				
			}
			

		});
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				mPagerList.get(position).initData();//获取当前被选中的页面，初始化该页面数据
				
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				
			}
		});
		mPagerList.get(0).initData();//初始化首页数据
	}
	
	class ContentAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mPagerList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view==object;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager pager = mPagerList.get(position);
			container.addView(pager.mRootview);
//			pager.initData();//初始化数据。。。不要在此处初始化数据，否则会加载下一个数据
			return pager.mRootview;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View)object);
		}
	}

}
