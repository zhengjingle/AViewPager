package com.zjl.customview;

import java.util.LinkedList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

public class AViewPager extends FrameLayout{

	private LinkedList<View> viewList=new LinkedList<View>();
	private Context context;
	
	public final static int HORIZONTAL=1;
	public final static int VERTICAL=2;
	public final static int NONE=3;
	
	private int orientation=HORIZONTAL;
	
	public AViewPager(Context context) {
		super(context);
		this.context=context;
	}

	public AViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
	}

	public AViewPager(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context=context;
	}
	
	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}
	
	public void addItemList(LinkedList<View> viewList){
		if(viewList==null)return;
		
		synchronized(this.viewList){
			this.viewList.addAll(viewList);
		}
	}
	
	public void setItem(int position,View view){
		if (position < 0 || position >= viewList.size()){
			return;
		}
		
		synchronized(viewList){
     		viewList.set(position,view);
     		
     		if(this.position==position){
     			setCurrentItem(position);
     		}
		}
	}
	
	public void addItem(int position,View view){
		if (position < 0 || position > viewList.size()){
			return;
		}
		
		synchronized(viewList){
    		viewList.add(position, view);
    		
    		if(this.position==position){
     			setCurrentItem(position);
     		}
		}
	}
	
	public void removeItem(int position){
		if (position < 0 || position >= viewList.size()){
			return;
		}
		
		synchronized(viewList){
	    	viewList.remove(position);
	    	
	    	if(this.position==position){
	    		if(viewList.size()==0){
	    			removeAllViewsInLayout();
	    			invalidate();
	    		}else{
	    			setCurrentItem(position);
	    		}
	    	}
		}
	}

	int willSelectedPosition=-1;
	int position=-1;
	float[] downPoint=new float[2];
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if(viewList==null || viewList.size()==0)return false;
		
		synchronized (viewList) {
			int action = event.getAction();
			if (action == MotionEvent.ACTION_DOWN) {
				downPoint[0] = event.getX();
				downPoint[1] = event.getY();
				
			} else if (action == MotionEvent.ACTION_UP) {
				downPoint[0] = 0;
				downPoint[1] = 0;
				
				if (willSelectedPosition > -1) {
					setCurrentItem(willSelectedPosition);
					willSelectedPosition = -1;
				}
				
				if(onPageChangeListener!=null){
	    			onPageChangeListener.onPageScrollStateChanged(OnPageChangeListener.SCROLL_STATE_SETTLING);
				}
			} else if (action == MotionEvent.ACTION_MOVE) {

				if (orientation == HORIZONTAL) {
					double xMove = event.getX() - downPoint[0];
					hScroll(xMove);
				} else if (orientation == VERTICAL) {
					double yMove = event.getY() - downPoint[1];
					vScroll(yMove);
				} else {

				}
				
				if(onPageChangeListener!=null){
					onPageChangeListener.onPageScrollStateChanged(OnPageChangeListener.SCROLL_STATE_DRAGGING);
				}
			}else{
				if(onPageChangeListener!=null){
	        		onPageChangeListener.onPageScrollStateChanged(OnPageChangeListener.SCROLL_STATE_IDLE);
	    		}
			}
			
		}
		return true;
	}
	
	private void vScroll(double yMove){
		
		View currentView=viewList.get(position);
		View upView=null;
		View downView=null;
		if(position!=0){
			upView=viewList.get(position-1);
		}
		if(position!=viewList.size()-1){
			downView=viewList.get(position+1);
		}
		
		
		if(yMove>0){
			if(upView!=null){
				
				ViewGroup.MarginLayoutParams currentParams=getMarginLayoutParams();
				currentParams.topMargin=(int)yMove;
				removeAllViewsInLayout();
				addView(currentView, currentParams);
				
				int height=getHeight();
				ViewGroup.MarginLayoutParams upParams=getMarginLayoutParams();
				upParams.bottomMargin=height-(int)yMove;
				addView(upView, upParams);
				
				if(currentParams.topMargin<upParams.bottomMargin){
					willSelectedPosition=position;
				}else{
					willSelectedPosition=position-1;
				}
				
				if(onPageChangeListener!=null){
					float rate=currentParams.topMargin/(float)getHeight();
					onPageChangeListener.onPageScrolled(position, rate, currentParams.topMargin);
				}
			}
		}else if(yMove<0){
			if(downView!=null){
				
				ViewGroup.MarginLayoutParams currentParams=getMarginLayoutParams();
				currentParams.bottomMargin=Math.abs((int)yMove);
				removeAllViewsInLayout();
				addView(currentView, currentParams);
				
				int height=getHeight();
				ViewGroup.MarginLayoutParams bottomParams=getMarginLayoutParams();
				bottomParams.topMargin=height-Math.abs((int)yMove);
				addView(downView, bottomParams);
				
				if(currentParams.bottomMargin<bottomParams.topMargin){
					willSelectedPosition=position;
				}else{
					willSelectedPosition=position+1;
				}
				
				if(onPageChangeListener!=null){
					float rate=currentParams.bottomMargin/(float)getHeight();
					onPageChangeListener.onPageScrolled(position, rate, currentParams.bottomMargin);
				}
			}
			
			
		}
	}
	
	private void hScroll(double xMove){
		
		View currentView=viewList.get(position);
		View leftView=null;
		View rightView=null;
		if(position!=0){
			leftView=viewList.get(position-1);
		}
		if(position!=viewList.size()-1){
			rightView=viewList.get(position+1);
		}
		
		
		if(xMove>0){
			if(leftView!=null){
				
				ViewGroup.MarginLayoutParams currentParams=getMarginLayoutParams();
				currentParams.leftMargin=(int)xMove;
				removeAllViewsInLayout();
				addView(currentView, currentParams);
				
				int width=getWidth();
				ViewGroup.MarginLayoutParams leftParams=getMarginLayoutParams();
				leftParams.rightMargin=width-(int)xMove;
				addView(leftView, leftParams);
				
				if(currentParams.leftMargin<leftParams.rightMargin){
					willSelectedPosition=position;
				}else{
					willSelectedPosition=position-1;
				}
				
				if(onPageChangeListener!=null){
					float rate=currentParams.leftMargin/(float)getWidth();
					onPageChangeListener.onPageScrolled(position, rate, currentParams.leftMargin);
				}
			}
		}else if(xMove<0){
			if(rightView!=null){
				
				ViewGroup.MarginLayoutParams currentParams=getMarginLayoutParams();
				currentParams.rightMargin=Math.abs((int)xMove);
				removeAllViewsInLayout();
				addView(currentView, currentParams);
				
				int width=getWidth();
				ViewGroup.MarginLayoutParams rightParams=getMarginLayoutParams();
				rightParams.leftMargin=width-Math.abs((int)xMove);
				addView(rightView, rightParams);
				
				if(currentParams.rightMargin<rightParams.leftMargin){
					willSelectedPosition=position;
				}else{
					willSelectedPosition=position+1;
				}
				
				if(onPageChangeListener!=null){
					float rate=currentParams.rightMargin/(float)getWidth();
					onPageChangeListener.onPageScrolled(position, rate, currentParams.rightMargin);
				}
			}
			
			
		}
	}
	
	public void setCurrentItem(int position) {
		if (position < 0 || position > viewList.size() - 1) {

		} else {
			synchronized (viewList) {
				removeAllViewsInLayout();

				View view = viewList.get(position);
				view.setLayoutParams(getMarginLayoutParams());
				addView(view);

				this.position = position;
				
				if(onPageChangeListener!=null){
					onPageChangeListener.onPageSelected(position);
				}
			}
		}
	}
	
	public void setCurrentItem(int position, int outAnim, int inAnim){
		
		if (position < 0 || position > viewList.size() - 1) {

		} else {
			synchronized (viewList) {
				View outView = viewList.get(this.position);
				View inView = viewList.get(position);

				outView.startAnimation(AnimationUtils.loadAnimation(context,
						outAnim));
				inView.startAnimation(AnimationUtils.loadAnimation(context,
						inAnim));

				removeAllViewsInLayout();
				inView.setLayoutParams(getMarginLayoutParams());
				addView(inView);

				this.position = position;
				
				if(onPageChangeListener!=null){
					onPageChangeListener.onPageSelected(position);
				}
			}
		}
	}
	
	private ViewGroup.MarginLayoutParams getMarginLayoutParams(){
		return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);
	}
	
	OnPageChangeListener onPageChangeListener;
	public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener){
		this.onPageChangeListener=onPageChangeListener;
	}
	
	public interface OnPageChangeListener{
		//0(END) 1(PRESS) 2(UP) 
		/**
	     * Indicates that the pager is in an idle, settled state. The current page
	     * is fully in view and no animation is in progress.
	     */
	    public static final int SCROLL_STATE_IDLE = 0;
	    
	    /**
	     * Indicates that the pager is currently being dragged by the user.
	     */
	    public static final int SCROLL_STATE_DRAGGING = 1;
	    
	    /**
	     * Indicates that the pager is in the process of settling to a final position.
	     */
	    public static final int SCROLL_STATE_SETTLING = 2;
		
		public void onPageSelected(int position);
		
		public void onPageScrolled(int position,float positionOffset, int positionOffsetPixels);
		
		public void onPageScrollStateChanged(int state);
	}
}
