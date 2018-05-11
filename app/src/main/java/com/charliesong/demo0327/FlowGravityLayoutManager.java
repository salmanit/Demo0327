package com.charliesong.demo0327;

import android.graphics.Rect;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by charlie.song on 2018/5/8.
 */

public class FlowGravityLayoutManager extends RecyclerView.LayoutManager {

    private int mVerticalOffset;//竖直偏移量 每次换行时，要根据这个offset判断
    private int mFirstVisiPos;//屏幕可见的第一个View的Position
    private int mLastVisiPos;//屏幕可见的最后一个View的Position
    private OrientationHelper orientationHelper;//系统自带的类，这里选择的是垂直方向的，用来获取top,bottom，以及child的宽高等
    private SparseArray<Rect> mItemRects;//key 是position，保存的是view的bound属性
    private boolean horizontalCenter=false;//同一行的控件是否居中显示
    public FlowGravityLayoutManager() {
        orientationHelper = OrientationHelper.createOrientationHelper(this, OrientationHelper.VERTICAL);
        mItemRects = new SparseArray<>();
        setAutoMeasureEnabled(true);
    }
    public void setHorizontalCenter(boolean horizontalCenter) {
        this.horizontalCenter = horizontalCenter;
    }
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        //我们要根据recyclerView的高度是固定还是不固定的来处理，如果是固定高度，比如给个准确的值200dp或者match_parent,那么这里就得设置为false，防止layoutManager来计算高度。
        //如果是warp_content,那么就需要layoutManager自己来计算高度了，这里就得为true了
//        setAutoMeasureEnabled(View.MeasureSpec.getMode(heightSpec)!=View.MeasureSpec.EXACTLY);
//        System.out.println("40============="+View.MeasureSpec.getMode(heightSpec)+"/"+View.MeasureSpec.EXACTLY);
        super.onMeasure(recycler, state, widthSpec, heightSpec);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0) {
            detachAndScrapAttachedViews(recycler);
            oneRowViews.clear();
            return;
        }
        if (getChildCount() == 0 && state.isPreLayout()) {
            return;
        }
        detachAndScrapAttachedViews(recycler);
        mVerticalOffset = 0;
        mFirstVisiPos = 0;
        mLastVisiPos = getItemCount() ;
        fill(recycler, state);
    }

    private void fill(RecyclerView.Recycler recycler, RecyclerView.State state) {
        fill(recycler, state, 0);
    }
    int leftOffset = getPaddingLeft();
    int lineMaxHeight = 0;//每行的最大高度，因为同一行的控件可能高度不一样，取最大值，好决定下一行的位置
    int topOffset;
    SparseArray<View> oneRowViews=new SparseArray<>();//保存在同一行的view，方便在这一行控件加载完以后平移，只对居中对齐的情况下处理
    /**
     * 填充childView的核心方法,应该先填充，再移动。
     * 在填充时，预先计算dy的在内，如果View越界，回收掉。
     * 一般情况是返回dy，如果出现View数量不足，则返回修正后的dy.
     *
     * @param dy RecyclerView给我们的位移量,+,显示底端， -，显示头部,手指往上滑是正的，往下滑是负的
     * @return 修正以后真正的dy（可能剩余空间不够移动那么多了 所以return <|dy|）
     */
    private int fill(RecyclerView.Recycler recycler, RecyclerView.State state, int dy) {
         topOffset = orientationHelper.getStartAfterPadding();
        //回收越界子View
        if (getChildCount() > 0) {//滑动时进来的
            for (int i = getChildCount() - 1; i >= 0; i--) {
                View child = getChildAt(i);
                if (dy > 0) {//上滑，顶部的view可能消失
                    if (getDecoratedBottom(child) - dy < topOffset) {
                        removeAndRecycleView(child, recycler);
                        mFirstVisiPos++;
                    }
                } else if (dy < 0) {//手指往下滑，底部的view 可能消失
                    if (getDecoratedTop(child) - dy > orientationHelper.getEndAfterPadding()) {
                        removeAndRecycleView(child, recycler);
                        mLastVisiPos--;
                    }
                }
            }
        }
        //
        leftOffset = getPaddingLeft();
         lineMaxHeight = 0;
        //布局子View阶段
        if (dy >= 0) {
            int minPos = mFirstVisiPos;
            mLastVisiPos = getItemCount() - 1;
            if (getChildCount() > 0) {
                View lastVisibleView = getChildAt(getChildCount() - 1);
                minPos = getPosition(lastVisibleView) + 1;
                topOffset = getDecoratedTop(lastVisibleView);
                leftOffset = getDecoratedRight(lastVisibleView);
                lineMaxHeight = Math.max(lineMaxHeight, orientationHelper.getDecoratedMeasurement(lastVisibleView));
            }
            for (int i = minPos; i <=mLastVisiPos; i++) {
            //找recycler要一个childItemView,我们不管它是从scrap里取，还是从RecyclerViewPool里取，亦或是onCreateViewHolder里拿。
                View child = recycler.getViewForPosition(i);
                addView(child);
                measureChildWithMargins(child, 0, 0);
                if (leftOffset + orientationHelper.getDecoratedMeasurementInOther(child) <= getWidth() - getPaddingRight()) {//这一行可以装的下，不用换行
                    layoutChild(child,i);
                } else {
                    transViewX();
                    leftOffset = getPaddingLeft();
                    topOffset += lineMaxHeight;
                    lineMaxHeight = 0;
                    //新起一行的时候要判断一下边界
                    if (topOffset - dy > orientationHelper.getEndAfterPadding()) {
                        removeAndRecycleView(child, recycler);
                        mLastVisiPos=i-1;//已经跑到recyclerview最底部，不可见了，修改mLastVisPos,循环结束
                    } else {
                        layoutChild(child,i);
                    }
                }
            }
            System.out.println("130=========mLastVisiPos="+mLastVisiPos+"=======getChildCount="+getChildCount());
            //添加完后，判断是否已经没有更多的ItemView，并且此时屏幕仍有空白，则需要修正dy
            View lastVisibleView = getChildAt(getChildCount() - 1);
            int lastPosition = getPosition(lastVisibleView);
            if (lastPosition == getItemCount() - 1) {
                int gap = orientationHelper.getEndAfterPadding() - getDecoratedBottom(lastVisibleView);
                if (gap > 0) {
                    dy -= gap;
                }
            }
        } else {

            int maxPosition = getItemCount() - 1;
            mFirstVisiPos = 0;
            if (getChildCount() > 0) {
                View firstView = getChildAt(0);
                maxPosition = getPosition(firstView) - 1;
            }
            for (int i = maxPosition; i >= mFirstVisiPos; i--) {
                Rect rect = mItemRects.get(i);
                if (rect.bottom - mVerticalOffset - dy < getPaddingTop()) {
                    mFirstVisiPos = i + 1;
                    break;
                } else {
                    View child = recycler.getViewForPosition(i);
                    addView(child, 0);//将View添加至RecyclerView中，childIndex为1，但是View的位置还是由layout的位置决定
                    measureChildWithMargins(child, 0, 0);
                    layoutDecoratedWithMargins(child, rect.left, rect.top - mVerticalOffset, rect.right, rect.bottom - mVerticalOffset);
                }
            }

        }
        return  dy;
    }


    //换行或者add最后一个view的时候，平移下最终的位置，
    private void transViewX(){
        if(!horizontalCenter){
            return;
        }
        int transX=(getWidth()-getPaddingRight()-leftOffset)/2;
        for(int i=0;i<oneRowViews.size();i++){
            int key=oneRowViews.keyAt(i);
            View transChild= oneRowViews.get(key);
            transChild.offsetLeftAndRight(transX);
            Rect rect = mItemRects.get(key);
            rect.offset(transX,0);
        }
        oneRowViews.clear();
    }

    private void layoutChild(View child,int i){
        oneRowViews.put(i,child);
        layoutDecoratedWithMargins(child, leftOffset, topOffset, leftOffset + orientationHelper.getDecoratedMeasurementInOther(child),
                topOffset + orientationHelper.getDecoratedMeasurement(child));
        //保存Rect供逆序layout用
        Rect rect = new Rect(leftOffset, topOffset + mVerticalOffset, leftOffset + orientationHelper.getDecoratedMeasurementInOther(child),
                topOffset + orientationHelper.getDecoratedMeasurement(child) + mVerticalOffset);
        mItemRects.put(i, rect);

        leftOffset += orientationHelper.getDecoratedMeasurementInOther(child);
        lineMaxHeight = Math.max(lineMaxHeight, orientationHelper.getDecoratedMeasurement(child));
        if(i==mLastVisiPos){
            transViewX();
        }
    }
    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (dy == 0 || getChildCount() == 0) {
            return 0;
        }
        int realOffset = dy;
        //边界修复代码
        if (mVerticalOffset + realOffset < 0) {//上边界
            realOffset = -mVerticalOffset;
        }else if (realOffset > 0){
            //利用最后一个子View比较修正
            View lastView=getChildAt(getChildCount()-1);
            if(getPosition(lastView)==getItemCount()-1){
                int gap = getHeight() - getPaddingBottom() - getDecoratedBottom(lastView);
                if (gap > 0) {
                    realOffset = -gap;
                } else if (gap == 0) {
                    realOffset = 0;
                } else {
                    realOffset = Math.min(realOffset, -gap);
                }
            }
        }

        realOffset= fill(recycler, state, realOffset);//先填充，再位移。

        mVerticalOffset += realOffset;//累加实际滑动距离

        offsetChildrenVertical(-realOffset);//滑动
        return realOffset;
    }

}
