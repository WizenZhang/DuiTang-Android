package com.example.duitang.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.duitang.R;

import android.R.mipmap;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * ����ˢ�µ�ListView
 * @author Wizen
 *
 */
public class RefreshListView extends ListView implements OnScrollListener,android.widget.AdapterView.OnItemClickListener{

	private static final int STATE_PULL_REFRESH = 0;//����ˢ��
	private static final int STATE_RELEASE_REFRESH = 1;//�ɿ�ˢ��
	private static final int STATE_REFRESHING = 2;//����ˢ��
	
	private View mHeaderView;
	private int startY=-1;//�������Y����
	private int mHeaderViewHeight;
	
	private int mCurrentState = STATE_PULL_REFRESH;//��ǰ״̬
	private TextView tvTitle;
	private TextView tvTime;
	private ImageView ivArrow;
	private ProgressBar pbProgress;
	private RotateAnimation animUp;
	private RotateAnimation animDown;
	
	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initHeaderView();
//		initFooterView();
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderView();
//		initFooterView();
	}

	public RefreshListView(Context context) {
		super(context);
		initHeaderView();
//		initFooterView();
	}

	/**
	 * ��ʼ��ͷ����
	 */
    private void initHeaderView() {
		mHeaderView = View.inflate(getContext(), R.layout.refresh_header, null);
	
		this.addHeaderView(mHeaderView);
		
		tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
		tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
		ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arr);
	    pbProgress = (ProgressBar) mHeaderView.findViewById(R.id.pb_progress);
		
		mHeaderView.measure(0, 0);
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);//����ͷ����
		
		initArrowAnim();

		tvTime.setText("���ˢ��ʱ��:" + getCurrentTime());
    }
    /*
	 * ��ʼ���Ų���
	 */
	private void initFooterView() {
		mFooterView = View.inflate(getContext(),
				R.layout.refresh_footer, null);
		this.addFooterView(mFooterView);

		mFooterView.measure(0, 0);
		mFooterViewHeight = mFooterView.getMeasuredHeight();

		mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);// ����

		this.setOnScrollListener((OnScrollListener) this);
	}

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
    	switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getRawY();
			break;
        case MotionEvent.ACTION_MOVE:
			if (startY == -1) {//ȷ��startY��Ч
				startY = (int) ev.getRawY();
			}
			if (mCurrentState == STATE_REFRESHING) {//����ˢ�²�������
				break;
			}
			
			int endY = (int) ev.getRawY();
			int dy = endY - startY;//�ƶ�ƫ����
//			Log.i("tag", String.valueOf(mCurrentState));
			if (dy >0 && getFirstVisiblePosition() == 0 ) {//ֻ���������ҵ�ǰ�ǵ�һ��item,���������� 
				int padding = dy - mHeaderViewHeight;//����padding
				mHeaderView.setPadding(0, padding, 0, 0);
//				Log.i("tag", String.valueOf(padding));
				if (padding>0 && mCurrentState!= STATE_RELEASE_REFRESH) {//״̬��Ϊ�ɿ�ˢ��
					mCurrentState = STATE_RELEASE_REFRESH;
					
					refreshState();
				}else if (padding <0 && mCurrentState != STATE_PULL_REFRESH) {//״̬��Ϊ����ˢ��
					mCurrentState = STATE_PULL_REFRESH;
					refreshState();
				}
				return true;
				
			}
			break;
        case MotionEvent.ACTION_UP:
			startY = -1;//����
			
			if (mCurrentState == STATE_RELEASE_REFRESH) {
				mCurrentState = STATE_REFRESHING;//����ˢ��
				mHeaderView.setPadding(0, 0, 0, 0);//��ʾ
			    refreshState();
			}else if (mCurrentState == STATE_PULL_REFRESH) {
				mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);//����ͷ����
			}
			break;
		default:
			break;
		}
    	return super.onTouchEvent(ev);
    }

    /**
     * ˢ���¿ؼ��Ĳ���
     */
	private void refreshState() {
		switch (mCurrentState) {
		case STATE_PULL_REFRESH:
//			Log.i("tag", "����ˢ��");
			tvTitle.setText("����ˢ��");
			ivArrow.setVisibility(View.VISIBLE);
        	pbProgress.setVisibility(View.INVISIBLE);
        	ivArrow.startAnimation(animDown);
			break;
        case STATE_RELEASE_REFRESH:
//        	Log.i("tag", "�ɿ�ˢ��");
        	tvTitle.setText("�ɿ�ˢ��");
        	ivArrow.setVisibility(View.VISIBLE);
        	pbProgress.setVisibility(View.INVISIBLE);
        	ivArrow.startAnimation(animUp);
			break;
        case STATE_REFRESHING:
//        	Log.i("tag", "����ˢ��");
        	tvTitle.setText("����ˢ��");
        	ivArrow.clearAnimation();// �������������,��������
        	ivArrow.setVisibility(View.INVISIBLE);
        	pbProgress.setVisibility(View.VISIBLE);
        	
        	if (mListener != null) {
				mListener.onRefresh();
			}
        	
	        break;
		default:
			break;
		}
		
	}
	
	/**
	 * ��ʼ����ͷ����
	 */
	private void initArrowAnim() {
		// ��ͷ���϶���
		animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		animUp.setDuration(200);
		animUp.setFillAfter(true);

		// ��ͷ���¶���
		animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animDown.setDuration(200);
		animDown.setFillAfter(true);

	}

	OnRefreshListener mListener;
	private View mFooterView;
	private int mFooterViewHeight;

	public void setOnRefreshListener(OnRefreshListener listener) {
		mListener = listener;
	}
	public interface OnRefreshListener {
		public void onRefresh();

		public void onLoadMore();// ������һҳ����
	}
	
	/*
	 * ��������ˢ�µĿؼ�
	 */
	public void onRefreshComplete(boolean success) {
		if (isLoadingMore) {// ���ڼ��ظ���...
			mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);// ���ؽŲ���
			isLoadingMore = false;
		} else {
			mCurrentState = STATE_PULL_REFRESH;
			tvTitle.setText("����ˢ��");
			ivArrow.setVisibility(View.VISIBLE);
			pbProgress.setVisibility(View.INVISIBLE);

			mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);// ����

			if (success) {
				tvTime.setText("���ˢ��ʱ��:" + getCurrentTime());
			}
		}
	}
	
	/**
	 * ��ȡ��ǰʱ��
	 */
	public String getCurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
		return format.format(new Date());
	}

	private boolean isLoadingMore;


	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE
				|| scrollState == SCROLL_STATE_FLING) {
	
			if (getLastVisiblePosition() == getCount() - 1 && !isLoadingMore) {// ���������
				System.out.println("������.....");
				mFooterView.setPadding(0, 0, 0, 0);// ��ʾ
				setSelection(getCount() - 1);// �ı�listview��ʾλ��
	
				isLoadingMore = true;
	
				if (mListener != null) {
					mListener.onLoadMore();
				}
			}
		}
		
	}
	
	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}
	
	OnItemClickListener mItemClickListener;
	@Override
	public void setOnItemClickListener(
			android.widget.AdapterView.OnItemClickListener listener) {
		// TODO Auto-generated method stub
		super.setOnItemClickListener(this);
		
		mItemClickListener = listener;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		if (mItemClickListener!=null) {
			mItemClickListener.onItemClick(parent, view, position-getHeaderViewsCount(), id);
		}
	}

}
