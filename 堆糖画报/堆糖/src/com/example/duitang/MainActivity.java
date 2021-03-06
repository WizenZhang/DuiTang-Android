package com.example.duitang;



import com.example.duitang.fragment.ContentFragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Window;

public class MainActivity extends Activity {
	
	private static final String FRAGMENT_CONTENT = "fragment_content";
	
    @Override
	public void onCreate(Bundle savedInstanceState) {

   	super.onCreate(savedInstanceState);
   	requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
   	
   	setContentView(R.layout.activity_main);
   	
       initFragment();
    }
    
    /**
     * 初始化fragment，将fragment数据填充给布局文件
     */
    private void initFragment() {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();//开启事务
		
		transaction.replace(R.id.fl_content, new ContentFragment(),FRAGMENT_CONTENT);//用fragment替换framlayout
		
		transaction.commit();//提交事务
		
	}
}
