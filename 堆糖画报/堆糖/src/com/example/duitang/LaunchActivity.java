package com.example.duitang;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

public class LaunchActivity extends Activity {

	RelativeLayout rlRoot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        
        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
        startAnim();
    }


    /**
     * ��������
     */
    private void startAnim() {
		// TODO Auto-generated method stub
        //��������
    	AnimationSet set= new AnimationSet(false);
    	
    	//��ת����
    	RotateAnimation rotate=new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    	rotate.setDuration(1000);//����ʱ��
    	rotate.setFillAfter(true);//���ֶ���״̬
    	
    	//���Ŷ���
    	ScaleAnimation scale=new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    	scale.setDuration(1000);//����ʱ��
    	scale.setFillAfter(true);//���ֶ���״̬
    	
    	//���䶯��
    	AlphaAnimation alpha = new AlphaAnimation(0, 1);
    	alpha.setDuration(2000);//����ʱ��
    	alpha.setFillAfter(true);//���ֶ���״̬
    	
    	set.addAnimation(rotate);
    	set.addAnimation(scale);
    	set.addAnimation(alpha);
    	
    	//���ö�������
    	set.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				jumpNextpage();
			}
		});
    	rlRoot.startAnimation(set);
	}
    
    /**
     * ��ת��һ��ҳ��
     */
   private void jumpNextpage(){

		// ��ת����ҳ��
		startActivity(new Intent(LaunchActivity.this,MainActivity.class));
	
	    finish();
   }
}
