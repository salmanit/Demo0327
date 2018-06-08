package com.charliesong.demo0327.widget.copy;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import java.util.UUID;

/**
 * Created by willy on 2017/5/5.
 */

public class AnimationRatingBar extends BaseRatingBar {

//    protected Handler mHandler;
    protected Runnable mRunnable;
    protected String mRunnableToken = UUID.randomUUID().toString();

    protected AnimationRatingBar(Context context) {
        super(context);
        init();
    }

    protected AnimationRatingBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected AnimationRatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        mHandler = new Handler();
    }

    protected void postRunnable(Runnable runnable, long ANIMATION_DELAY) {
//        if (mHandler == null) {
//            mHandler = new Handler();
//        }

        long timeMillis = SystemClock.uptimeMillis() + ANIMATION_DELAY;
        postDelayed(runnable,ANIMATION_DELAY);
    }

}

