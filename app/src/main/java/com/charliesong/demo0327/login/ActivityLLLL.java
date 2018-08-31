package com.charliesong.demo0327.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import com.charliesong.demo0327.R;
import com.charliesong.demo0327.base.BaseActivity;

public class ActivityLLLL extends BaseActivity {

    TextView tvtitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tvtitle = findViewById(R.id.tv_title);
        tvtitle.setAlpha(0);
        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                startUIAnimator(true);
            }

            @Override
            public void keyBoardHide(int height) {
                startUIAnimator(false);
            }
        });
    tvtitle.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            tvtitle.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            tvtitle.setHeight(dp2px(60)+getStatusHeight(getBaseContext()));
        }
    });
    }

    private void startUIAnimator(final boolean softinputShow) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1).setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                if (!softinputShow) {
                    //隐藏键盘的话是反过来的，从1到0
                    value = 1 - value;
                }
                float transHeight = dp2px(300 - 60);//这里自己改，就是你的rl_login_top 420的高度减去 那个title的高度
                findViewById(R.id.rv_root).setTranslationY(-transHeight*value);
                //如果需要启动透明度渐变的动画自己这里加。。
                tvtitle.setAlpha(value);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (softinputShow) {
//                    tvtitle.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (!softinputShow) {
//                    tvtitle.setVisibility(View.GONE);
                }
            }
        });
        animator.start();
    }

    private int dp2px(int dp){
        DisplayMetrics outMertic=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMertic);
        return (int) (outMertic.density*dp);
    }
}
