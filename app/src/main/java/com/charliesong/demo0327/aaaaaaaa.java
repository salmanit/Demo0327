package com.charliesong.demo0327;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.view.ViewGroup;

import com.charliesong.demo0327.base.BaseRvHolder;
import com.charliesong.demo0327.kt.Student;

/**
 * Created by charlie.song on 2018/5/29.
 */

public class aaaaaaaa {

    private void test(){

        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0, 1);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0, 1);

        ObjectAnimator valueAnimator = ObjectAnimator.ofPropertyValuesHolder((Object) null, new PropertyValuesHolder[]{scaleX, scaleY});
//        PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", 0, 1);
//        PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", 0, 1);
//        PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt("right", 0, 1);
//        PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom", 0, 1);
//        PropertyValuesHolder pvhScrollX = PropertyValuesHolder.ofInt("scrollX", 0, 1);
//        PropertyValuesHolder pvhScrollY = PropertyValuesHolder.ofInt("scrollY", 0, 1);
//        ObjectAnimator defaultChangeIn = ObjectAnimator.ofPropertyValuesHolder((Object) null,
//                pvhLeft, pvhTop, pvhRight, pvhBottom, pvhScrollX, pvhScrollY);
        ObjectAnimator.ofFloat(null, "alpha", 0f, 1f);

        int a=(2222*33333)>>8;
    }

    class daa<T> extends PagedListAdapter<T,BaseRvHolder>{
        daa(DiffUtil.ItemCallback<T> diffCallback){
            super(diffCallback);
        }
        @NonNull
        @Override
        public BaseRvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull BaseRvHolder holder, int position) {
        test();
        }
    }


}
