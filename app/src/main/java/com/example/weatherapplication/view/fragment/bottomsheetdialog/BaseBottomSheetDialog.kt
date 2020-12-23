package com.example.weatherapplication.view.fragment.bottomsheetdialog

import android.content.res.Resources
import androidx.fragment.app.activityViewModels
import com.example.weatherapplication.viewmodel.CurrentWeatherViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialog : BottomSheetDialogFragment(){

    abstract var mBehavior: BottomSheetBehavior<*>
    protected val viewModel: CurrentWeatherViewModel by activityViewModels()

    override fun onStart() {
        super.onStart()
        mBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    protected fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }
}