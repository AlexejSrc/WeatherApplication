package com.example.weatherapplication.view.fragment.bottomsheetdialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapplication.R
import com.example.weatherapplication.model.SearchAdapterItem
import com.example.weatherapplication.model.convertAdapterItemToCityDataItem
import com.example.weatherapplication.view.adapter.SearchDialogAdapter
import com.example.weatherapplication.view.adapter.SearchDialogAdapterCallback
import com.example.weatherapplication.view.fragment.DESTROYED_BY_SYSTEM
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.search_bottom_sheet_layout.view.*


class SearchBottomSheetDialog : BaseBottomSheetDialog(){

    override lateinit var mBehavior: BottomSheetBehavior<*>
    lateinit var adapter: SearchDialogAdapter
    lateinit var dialogView: View

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialogView = View.inflate(context, R.layout.search_bottom_sheet_layout, null)
        val root = dialogView.findViewById<ConstraintLayout>(R.id.search_sheet_root)
        root.layoutParams = root.layoutParams.apply {
            height = getScreenHeight()
        }
        checkIfDestroyedBySystem(savedInstanceState)
        initRecycler()
        setDefaultState()
        setObservers()
        setUpEditTextListener()
        dialog.setContentView(dialogView)
        mBehavior = BottomSheetBehavior.from(dialogView.parent as View)
        return dialog
    }

    private fun setObservers(){
        viewModel.getWeatherAfterQueryList().observe(this, Observer {
            setFragmentState(it)
        })
        viewModel.getWeatherAfterQueryStateAction().observe(this, Observer {
            setFragmentState(it)
        })
    }

    private fun initFocus(){
        dialogView.search_sheet_edit_text.requestFocus()
        val imm: InputMethodManager? = getSystemService(dialogView.context, InputMethodManager::class.java)
        imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    private fun hideKeyboard(){
        dialogView.search_sheet_edit_text.clearFocus()
        val imm: InputMethodManager? = getSystemService(dialogView.context, InputMethodManager::class.java)
        imm?.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    private fun initRecycler(){
        dialogView.search_sheet_recycler_view.layoutManager = LinearLayoutManager(context)
        adapter = SearchDialogAdapter(
                object: SearchDialogAdapterCallback{
                    override fun onDeleteItem(item: SearchAdapterItem) {
                        viewModel.removeFromFavouritesFromSearch(item) { adapter.notifyDataSetChanged()}
                    }

                    override fun onAddItem(item: SearchAdapterItem) {
                        viewModel.addToFavouritesFromSearch(item) {adapter.notifyDataSetChanged()}
                    }

                    override fun onOpenItem(item: SearchAdapterItem) {
                        viewModel.updateCurrentWeather(convertAdapterItemToCityDataItem(item))
                        dismiss()
                    }
                }
        )
        dialogView.search_sheet_recycler_view.adapter = adapter
    }

    private fun setUpEditTextListener() {
        dialogView.search_sheet_edit_text.doOnTextChanged { s,_,_,_->
            s?.let {
                viewModel.getQueryWithDelay(s.toString())
            }
        }
    }

    private fun checkIfDestroyedBySystem(bundle: Bundle?){
        if (bundle?.getBoolean(DESTROYED_BY_SYSTEM) != true){
            viewModel.clearSearchData()
            initFocus()
        }
    }

    private fun setFragmentState(state: SearchBottomSheetStates?){
        when (state){
            is SearchBottomSheetStates.GotResults -> stateViewItems( state.result)
            SearchBottomSheetStates.NoConnection-> setNoConnectionState()
            SearchBottomSheetStates.NoData-> setEmptyListState()
            SearchBottomSheetStates.Loading-> showLoading()
            SearchBottomSheetStates.DefaultState-> setDefaultState()
            SearchBottomSheetStates.CantAddItemToFavouritesToast-> showCantAddItem()
            SearchBottomSheetStates.ShowNoConnectionToast-> showNoConnection()
            null -> hideLoading()
        }
    }

    private fun stateViewItems(items: List<SearchAdapterItem>){
        dialogView.search_sheet_recycler_view.visibility = View.VISIBLE
        dialogView.search_sheet_info.visibility = View.GONE
        adapter.submitList(items)
    }

    private fun setNoConnectionState(){
        dialogView.search_sheet_recycler_view.visibility = View.INVISIBLE
        dialogView.search_sheet_info.visibility = View.VISIBLE
        dialogView.search_sheet_info.text = resources.getString(R.string.no_connection)
    }

    private fun setEmptyListState(){
        adapter.submitList(emptyList())
        dialogView.search_sheet_recycler_view.visibility = View.VISIBLE
        dialogView.search_sheet_info.visibility = View.VISIBLE
        dialogView.search_sheet_info.text = resources.getString(R.string.no_data)
    }

    private fun setDefaultState(){
        adapter.submitList(emptyList())
        dialogView.search_sheet_recycler_view.visibility = View.VISIBLE
        dialogView.search_sheet_info.visibility = View.VISIBLE
        dialogView.search_sheet_info.text = resources.getString(R.string.enter_your_query)
    }

    private fun showLoading(){
        dialogView.search_sheet_progress_bar.visibility = View.VISIBLE
        dialogView.search_sheet_recycler_view.visibility = View.INVISIBLE
        dialogView.search_sheet_info.visibility = View.INVISIBLE
    }

    private fun hideLoading(){
        dialogView.search_sheet_progress_bar.visibility = View.GONE
    }

    private fun showCantAddItem(){
        Toast.makeText(dialogView.context, getString(R.string.unable_to_add), Toast.LENGTH_SHORT).show()
    }

    private fun showNoConnection(){
        Toast.makeText(dialogView.context, getString(R.string.no_connection), Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        hideKeyboard()
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(DESTROYED_BY_SYSTEM, true)
        super.onSaveInstanceState(outState)
    }
}