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
		ViewUtils.inject(this,view);//ע��view���¼�
		return view;
	}
    
	public void initData() {
		rgGroup.check(R.id.rb_home);//Ĭ�Ϲ�ѡ��ҳ
		//��ʼ�������ҳ��
		mPagerList = new ArrayList<BasePager>();

		mPagerList.add(new HomePager(mActivity));
		mPagerList.add(new CategoryPager(mActivity));
		mPagerList.add(new StorePager(mActivity));
		
		mViewPager.setAdapter(new ContentAdapter());
		
		//����RadioGroup��ѡ���¼�
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
				mPagerList.get(position).initData();//��ȡ��ǰ��ѡ�е�ҳ�棬��ʼ����ҳ������
				
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
		mPagerList.get(0).initData();//��ʼ����ҳ����
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
//			pager.initData();//��ʼ�����ݡ�������Ҫ�ڴ˴���ʼ�����ݣ�����������һ������
			return pager.mRootview;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View)object);
		}
	}

}
