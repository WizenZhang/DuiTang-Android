package com.example.duitang;



import com.example.duitang.fragmen.ContentFragment;
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
   	requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ������
   	
   	setContentView(R.layout.activity_main);
   	
       initFragment();
    }
    
    /**
     * ��ʼ��fragment����fragment�������������ļ�
     */
    private void initFragment() {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();//��������
		
		transaction.replace(R.id.fl_content, new ContentFragment(),FRAGMENT_CONTENT);//��fragment�滻framlayout
		
		transaction.commit();//�ύ����
		
	}
}
