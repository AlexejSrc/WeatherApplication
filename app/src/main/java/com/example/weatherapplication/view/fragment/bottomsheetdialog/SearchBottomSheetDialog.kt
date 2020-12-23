package com.example.weatherapplication.view.fragment.bottomsheetdialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapplication.R
import com.example.weatherapplication.model.SearchAdapterItem
import com.example.weatherapplication.model.convertAdapterItemToCityDataItem
import com.example.weatherapplication.view.adapter.SearchDialogAdapter
import com.example.weatherapplication.view.adapter.SearchDialogAdapterCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.search_bottom_sheet_layout.*
import kotlinx.android.synthetic.main.search_bottom_sheet_layout.view.*


class SearchBottomSheetDialog : BaseBottomSheetDialog(){

    override lateinit var mBehavior: BottomSheetBehavior<*>
    lateinit var adapter: SearchDialogAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view: View = View.inflate(context, R.layout.search_bottom_sheet_layout, null)
        val root = view.findViewById<ConstraintLayout>(R.id.search_sheet_root)
        root.layoutParams = root.layoutParams.apply {
            height = getScreenHeight()
        }
        initRecycler(view)
        viewModel.getWeatherAfterQueryList().observe(this, Observer {
            setFragmentState(it, view)

        })
        setUpEditTextListener(view)
        dialog.setContentView(view)
        mBehavior = BottomSheetBehavior.from(view.parent as View)
        return dialog
    }

    private fun initRecycler(view: View){
        view.search_sheet_recycler_view.layoutManager = LinearLayoutManager(context)
        adapter = SearchDialogAdapter(
                object: SearchDialogAdapterCallback{
                    override fun onDeleteItem(item: SearchAdapterItem) {
                        viewModel.removeFromFavourites(convertAdapterItemToCityDataItem(item))
                    }

                    override fun onAddItem(item: SearchAdapterItem) {
                        viewModel.addToFavourites(convertAdapterItemToCityDataItem(item))
                    }

                    override fun onOpenItem(item: SearchAdapterItem) {
                        viewModel.updateCurrentWeather(convertAdapterItemToCityDataItem(item))
                        dismiss()
                    }
                }
        )
        view.search_sheet_recycler_view.adapter = adapter
    }

    private fun setUpEditTextListener(view: View) {
        view.search_sheet_edit_text.doOnTextChanged { s,_,_,_->
            s?.let {
                viewModel.getQueryWithDelay(s.toString())
            }
        }
    }

    private fun setFragmentState(list: List<SearchAdapterItem>?, view: View){
        list?.let {
            adapter.submitList(list)
            if (list.isEmpty()){
                setEmptyListState(view)
            }
            else{
                setDefaultState(view)
            }
        } ?: setNoConnectionState(view)
    }

    private fun setNoConnectionState(view: View){
        view.search_sheet_recycler_view.visibility = View.INVISIBLE
        view.search_sheet_info.visibility = View.VISIBLE
        view.search_sheet_info.text = resources.getString(R.string.no_connection)
    }

    private fun setEmptyListState(view: View){
        view.search_sheet_recycler_view.visibility = View.VISIBLE
        view.search_sheet_info.visibility = View.VISIBLE
        view.search_sheet_info.text = resources.getString(R.string.no_data)
    }

    fun setDefaultState(view: View){
        view.search_sheet_recycler_view.visibility = View.VISIBLE
        view.search_sheet_info.visibility = View.GONE
    }

    override fun onDestroy() {
        viewModel.clearSearchData()
        super.onDestroy()
    }
}