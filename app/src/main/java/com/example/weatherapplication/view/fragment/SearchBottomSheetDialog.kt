package com.example.weatherapplication.view.fragment

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.example.weatherapplication.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.search_bottom_sheet_layout.*

class SearchBottomSheetDialog : BottomSheetDialogFragment(){
    private var mBehavior: BottomSheetBehavior<*>? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view: View = View.inflate(context, R.layout.search_bottom_sheet_layout, null)
        val root = view.findViewById<LinearLayout>(R.id.search_sheet_root)
        root.layoutParams = root.layoutParams.apply {
            height = getScreenHeight()
        }
        dialog.setContentView(view)
        mBehavior = BottomSheetBehavior.from(view.parent as View)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        mBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }
}