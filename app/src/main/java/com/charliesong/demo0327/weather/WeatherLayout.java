package com.charliesong.demo0327.weather;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.charliesong.demo0327.R;

public class WeatherLayout extends RelativeLayout {

	public WeatherLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		screenWidth = context.getResources().getDisplayMetrics().widthPixels;
		screenHeight = context.getResources().getDisplayMetrics().heightPixels;
		mScroller=new Scroller(context);
		mVelocityTracker=VelocityTracker.obtain();
	}

	private int screenWidth;
	private int screenHeight;
	View layout1, layout2, layout3, layout4,layout_show;
	private float oldY;
	private Scroller mScroller;
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if(isInEditMode()) {
			return;
		}
			layout1 = findViewById(R.id.layout111);
			layout2 = findViewById(R.id.layout222);
			layout3 = findViewById(R.id.layout333);
			layout4 = findViewById(R.id.layout444);
			layout_show=findViewById(R.id.layout_show);
		
		
		layout4.layout(l, b, r, b+layout4.getMeasuredHeight());
		layout3.layout(l, b-layout3.getMeasuredHeight(), r, b);
		layout2.layout(l, b-layout2.getMeasuredHeight()-layout_show.getMeasuredHeight(), r, b-layout_show.getMeasuredHeight());
		layout1.layout(l, t, r, layout2.getTop());
	}
	int startScrollY=0;
	private VelocityTracker mVelocityTracker;
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startScrollY=getScrollY();
			oldY = event.getY();
			mVelocityTracker.clear();
			mVelocityTracker.addMovement(event);
			break;
		case MotionEvent.ACTION_MOVE:
			mVelocityTracker.addMovement(event);
			// break;
			float currentY = event.getY();
			float moveFloat = currentY - oldY;
			int move=(int) moveFloat;
			if (Math.abs(move) > 3) {
				handleMove(move);
				oldY = currentY;
			}
			break;
			case MotionEvent.ACTION_UP:
				mVelocityTracker.addMovement(event);
				mVelocityTracker.computeCurrentVelocity(1000);
				float pysec = mVelocityTracker.getYVelocity();
				System.out.println("pysec=========="+pysec);
				handleMove((int) (pysec/100));
//				if(pysec>600){
//
//				}else if(pysec<-600){
//
//				}else{
//
//				}
				//https://blog.csdn.net/jiqiangshan/article/details/56066668
				int dScrollY=getScrollY()-startScrollY;
				System.out.println("========="+startScrollY+"/"+getScrollY()+"="+dScrollY);
//				if (dScrollY > 0) {
//					//向上滑动dScrollY为正值
//					if (dScrollY < mScreenHeight / 3) {
//						mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
//					} else {
//						mScroller.startScroll(0, getScrollY(), 0, (mScreenHeight - dScrollY));
//					}
//				} else {
//					//向下滑动dScrollY为负值
//					if (-dScrollY < mScreenHeight / 3) {
//						mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
//					} else {
//						mScroller.startScroll(0, getScrollY(), 0, (-mScreenHeight - dScrollY));
//					}
//				}
				break;
		}
		return true;
	}
	private boolean handleMove(int move){
		if (move > 0) {
			System.err.println("top2=="+layout2.getTop()+" bottom1="+layout1.getBottom()+" bottom2="+layout2.getBottom()+" height2="+layout2.getHeight());
			if(layout2.getTop()>=layout1.getBottom()) {
				return true;
			}
			if(layout4.getTop()<getHeight()) {
				translateLayout(layout4, move);
			}
			if(layout3.getBottom()<getHeight()) {
				translateLayout(layout3, move);
			}
			translateLayout(layout2, move);
		} else {
			if(layout4.getBottom()<=getHeight()) {
				return true;
			}
			int bottom2 = layout2.getBottom();
			System.err.println(layout2.getTop()+"==bottom2==========" + bottom2 + "=========top3=" + layout3.getTop()+"=====height2=="+layout2.getHeight());
			if (bottom2 > layout3.getTop()) {
				translateLayout(layout2, move);
			} else {
				translateLayout(layout2, move);
				translateLayout(layout3, move);
				System.err.println("bottom3==" + layout3.getBottom() + " height==" + getHeight());
				if (layout3.getBottom() < getHeight()) {
					translateLayout(layout4, move);
					System.err.println(layout4.getTop() + "=====layout4=======" + layout4.getBottom());
				}
			}

		}
		return true;
	}
	private void translateLayout(View layout, int move) {
		layout.setTop( (layout.getTop() + move));
		layout.setBottom((layout.getBottom() + move));
	}
	@Override
	public void computeScroll() {
		super.computeScroll();
		Log.d("TAG", "mScroller.getCurrY() " + mScroller.getCurrY());
		if (mScroller.computeScrollOffset()) {//是否已经滚动完成
			scrollTo(0, mScroller.getCurrY());//获取当前值，startScroll（）初始化后，调用就能获取区间值
			postInvalidate();
		}
	}
}
