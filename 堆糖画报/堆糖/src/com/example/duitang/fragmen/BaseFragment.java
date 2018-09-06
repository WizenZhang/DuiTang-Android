package com.example.duitang.fragmen;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * fragement����
 * @author Wizen
 *
 */
public abstract class BaseFragment extends Fragment{
	
	public Activity mActivity;
	
	//fragment����
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mActivity = getActivity();
	}
	
	//����fragment�Ĳ���
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return initViews();
	}

	//������Activity�������
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		
		initData();
	}
	//�������ʵ�ֳ�ʼ�����ֵķ���
	public abstract View initViews();
	
	//��ʼ�����ݣ����Բ�ʵ��
	public void initData() {
		
	}
	
}
