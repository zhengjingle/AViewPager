package com.example.aviewpager;


import java.util.LinkedList;

import com.zjl.customview.AViewPager;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	AViewPager aViewPager;
	RadioButton rb_h,rb_v,rb_none;
	Button button1,button2,button3,button4;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		aViewPager=(AViewPager)findViewById(R.id.aViewPager1);
		initViews();
		aViewPager.setCurrentItem(0);
		aViewPager.setOrientation(AViewPager.HORIZONTAL);
		
		rb_h=(RadioButton)findViewById(R.id.radio0);
		rb_v=(RadioButton)findViewById(R.id.radio1);
		rb_none=(RadioButton)findViewById(R.id.radio2);
		rb_h.setOnClickListener(this);
		rb_v.setOnClickListener(this);
		rb_none.setOnClickListener(this);
		
		button1=(Button) findViewById(R.id.button1);
		button2=(Button) findViewById(R.id.button2);
		button3=(Button) findViewById(R.id.button3);
		button4=(Button) findViewById(R.id.button4);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		button4.setOnClickListener(this);
		
		//模拟插入
		new Thread(){
			public void run(){
				for(int i=0;i<10;i++){
					h_add_thread.sendMessage(Message.obtain());
					SystemClock.sleep(5000);
				}
				
			}
		}.start();
		
		//模拟删除
		new Thread(){
			public void run(){
				for(int i=0;i<10;i++){
					h_remove_thread.sendMessage(Message.obtain());
					SystemClock.sleep(5000);
				}
				
			}
		}.start();
	}
	
	Handler h_add_thread=new Handler(){
		public void handleMessage(Message msg){
			aViewPager.addItem(0, View.inflate(MainActivity.this, R.layout.view1, null));
			Toast.makeText(MainActivity.this, "addItem", Toast.LENGTH_SHORT).show();
		}
	};
	
	Handler h_remove_thread=new Handler(){
		public void handleMessage(Message msg){
			aViewPager.removeItem(0);
			Toast.makeText(MainActivity.this, "removeItem", Toast.LENGTH_SHORT).show();
		}
	};
	
	private void initViews(){
		View view1=View.inflate(this, R.layout.view1, null);
		View view2=View.inflate(this, R.layout.view2, null);
		View view3=View.inflate(this, R.layout.view3, null);
		View view4=View.inflate(this, R.layout.view4, null);
		
		LinkedList<View> viewList=new LinkedList<View>();
		viewList.add(view1);
		viewList.add(view2);
		viewList.add(view3);
		viewList.add(view4);
		aViewPager.addItemList(viewList);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch(v.getId()){
		case R.id.radio0:
			aViewPager.setOrientation(AViewPager.HORIZONTAL);
			break;
		case R.id.radio1:
			aViewPager.setOrientation(AViewPager.VERTICAL);
			break;
		case R.id.radio2:
			aViewPager.setOrientation(AViewPager.NONE);
			break;
		case R.id.button1:
			aViewPager.setCurrentItem(0);
			break;
		case R.id.button2:
			aViewPager.setCurrentItem(1);
			break;
		case R.id.button3:
			aViewPager.setCurrentItem(2, R.anim.out_to_left, R.anim.in_from_right);
			break;
		case R.id.button4:
			aViewPager.setCurrentItem(3);
			break;
		}
	}
}
