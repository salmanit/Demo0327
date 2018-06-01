package com.charliesong.demo0327;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;

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
    }
}
