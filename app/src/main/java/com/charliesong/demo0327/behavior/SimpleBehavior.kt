package com.charliesong.demo0327.behavior

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.charliesong.demo0327.R

class SimpleBehavior:CoordinatorLayout.Behavior<TextView>{
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun onLayoutChild(parent: CoordinatorLayout?, child: TextView?, layoutDirection: Int): Boolean {
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout?, child: TextView, dependency: View): Boolean {
        child.translationX=dependency.translationX
        println("onDependentViewChanged===============${child.getTag()}====${dependency.getTag()}")
        return false
    }

    override fun onDependentViewRemoved(parent: CoordinatorLayout?, child: TextView?, dependency: View?) {
        super.onDependentViewRemoved(parent, child, dependency)
    }

    override fun layoutDependsOn(parent: CoordinatorLayout?, child: TextView, dependency: View): Boolean {
//        println("layoutDependsOn===============${dependency.getTag()}===${dependency}")

        return super.layoutDependsOn(parent, child, dependency)
    }
}