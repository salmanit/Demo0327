package com.charliesong.demo0327.login;

import android.app.Activity;
import android.arch.lifecycle.GenericLifecycleObserver;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class SoftKeyBoardListener {
    public interface OnSoftKeyBoardChangeListener {
        void keyBoardShow(int height);

        void keyBoardHide(int height);
    }
    private View rootView;//activity的根视图
    private int screenBottom;//纪录根视图的显示高度
    private OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener;
    boolean isShow=false;//软键盘是否显示
    private ViewTreeObserver.OnGlobalLayoutListener listener;
    public SoftKeyBoardListener(Activity activity) {

//获取activity的根视图
        rootView = activity.getWindow().getDecorView();
        screenBottom=activity.getWindowManager().getDefaultDisplay().getHeight();
//监听视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变
        listener=new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override

            public void onGlobalLayout() {

                //获取当前根视图在屏幕上显示的大小
                Rect r = new Rect();

                rootView.getWindowVisibleDisplayFrame(r);

                //根视图显示高度变小超过300，可以看作软键盘显示了
                System.out.println("rect============"+r.toShortString()+"==="+screenBottom);
                if (!isShow&& screenBottom >r.bottom) {
                    isShow=true;
                    if (onSoftKeyBoardChangeListener != null) {
                        onSoftKeyBoardChangeListener.keyBoardShow(screenBottom - r.bottom);
                    }
                    return;
                }

                //根视图显示高度变大超过300，可以看作软键盘隐藏了

                if (isShow&&r.bottom>= screenBottom) {
                    isShow=false;
                    if (onSoftKeyBoardChangeListener != null) {
                        onSoftKeyBoardChangeListener.keyBoardHide(0);
                    }

                    return;

                }

            }

        };
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(listener);
        addLifeObServer(activity);
    }

    private void setOnSoftKeyBoardChangeListener(OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {

        this.onSoftKeyBoardChangeListener = onSoftKeyBoardChangeListener;

    }

    public static void setListener(Activity activity, OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {

        SoftKeyBoardListener softKeyBoardListener = new SoftKeyBoardListener(activity);

        softKeyBoardListener.setOnSoftKeyBoardChangeListener(onSoftKeyBoardChangeListener);

    }

    public static void closeKeybord(EditText mEditText, Context mContext) {

        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);

        mEditText.setFocusable(false);

    }

    public void addLifeObServer(Activity activity){
        if(activity instanceof LifecycleOwner){
            LifecycleOwner owner= (LifecycleOwner) activity;
            owner.getLifecycle().addObserver(new GenericLifecycleObserver() {
                @Override
                public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
                    if(event==Lifecycle.Event.ON_DESTROY){
                        if(rootView!=null)
                            rootView.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
                    }
                }
            });
        }
    }
}

