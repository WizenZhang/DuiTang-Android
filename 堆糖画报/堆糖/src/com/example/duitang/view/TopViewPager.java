package com.example.duitang.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 头条新闻的的ViewPager
 * @author Wizen
 *
 */
public class TopViewPager extends ViewPager {

	private int startX;
	private int startY;

	public TopViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

	public TopViewPager(Context context) {
		super(context);
		
	}

	/**
     * 事件分发，请求父控件及祖宗控件是否拦截事件
     * 1.右滑，而且是第一个页面，需要父控件拦截
     * 2.左滑，而且是最后一个页面，需要父控件拦截
     * 3.上下滑动，需要父控件拦截
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

    	switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			getParent().requestDisallowInterceptTouchEvent(true);//用getParent去请求，不拦截,以保证ACTION_MOVE能被调用
			
			startX = (int)ev.getRawX();
			startY = (int)ev.getRawY();
			
			break;
        case MotionEvent.ACTION_MOVE:
			
        	int endX = (int)ev.getRawX();
        	int endY = (int)ev.getRawY();
        	
        	if (Math.abs(endX - startX) > Math.abs(endY - startY)) {//左右滑动
				if (endX > startX) {//右滑
					if (getCurrentItem() == 0) {//第一个页面，需要父控件拦截
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				} else {//左滑
					if (getCurrentItem() == getAdapter().getCount()-1) {//最后一个页面，需要父控件拦截
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}
			} else {//上下滑动
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			break;
		default:
			
			getParent().requestDisallowInterceptTouchEvent(true);
			
			break;
		}
    	return super.dispatchTouchEvent(ev);
    }
}
