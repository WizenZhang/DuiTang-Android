package com.example.duitang.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ͷ�����ŵĵ�ViewPager
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
     * �¼��ַ������󸸿ؼ������ڿؼ��Ƿ������¼�
     * 1.�һ��������ǵ�һ��ҳ�棬��Ҫ���ؼ�����
     * 2.�󻬣����������һ��ҳ�棬��Ҫ���ؼ�����
     * 3.���»�������Ҫ���ؼ�����
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

    	switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			getParent().requestDisallowInterceptTouchEvent(true);//��getParentȥ���󣬲�����,�Ա�֤ACTION_MOVE�ܱ�����
			
			startX = (int)ev.getRawX();
			startY = (int)ev.getRawY();
			
			break;
        case MotionEvent.ACTION_MOVE:
			
        	int endX = (int)ev.getRawX();
        	int endY = (int)ev.getRawY();
        	
        	if (Math.abs(endX - startX) > Math.abs(endY - startY)) {//���һ���
				if (endX > startX) {//�һ�
					if (getCurrentItem() == 0) {//��һ��ҳ�棬��Ҫ���ؼ�����
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				} else {//��
					if (getCurrentItem() == getAdapter().getCount()-1) {//���һ��ҳ�棬��Ҫ���ؼ�����
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}
			} else {//���»���
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
