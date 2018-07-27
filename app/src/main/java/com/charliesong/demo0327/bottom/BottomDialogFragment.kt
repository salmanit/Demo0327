package com.charliesong.demo0327.bottom

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import com.charliesong.demo0327.R

class BottomDialogFragment : BottomSheetDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val v = LayoutInflater.from(activity).inflate(R.layout.activity_weather2, null)
        dialog.setContentView(v)
        behavior = BottomSheetBehavior.from(v.parent as View)

        return dialog
    }

    lateinit var behavior: BottomSheetBehavior<View>
    override fun onStart() {
        super.onStart()
        if (behavior != null) {
//            behavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        }
    }
}