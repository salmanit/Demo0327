package com.charliesong.demo0327.base;

import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by charlie.song on 2018/5/8.
 */

public class RvItemTouchListener extends RecyclerView.SimpleOnItemTouchListener {
    GestureDetector gestureDetector;
    RecyclerView rv;

    public RvItemTouchListener(RecyclerView rv) {
        this.rv = rv;
        gestureDetector = new GestureDetector(rv.getContext(), gestureListener);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return gestureDetector.onTouchEvent(e);
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        super.onTouchEvent(rv, e);
        gestureDetector.onTouchEvent(e);
    }

    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                int position = rv.getChildAdapterPosition(child);
                if (listener != null) {
                    listener.singleTab(position, rv.getChildViewHolder(child));
                }
            }
            return super.onSingleTapUp(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
        }
    };
    public RvItemClickListener listener;

    public interface RvItemClickListener {
         void singleTab(int position, RecyclerView.ViewHolder viewHolder);
         void longPress(int position);
    }
}
