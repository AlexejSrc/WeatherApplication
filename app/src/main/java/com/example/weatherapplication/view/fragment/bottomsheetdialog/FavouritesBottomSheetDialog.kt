package com.example.weatherapplication.view.fragment.bottomsheetdialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapplication.MyApplication
import com.example.weatherapplication.R
import com.example.weatherapplication.model.convertEntityToCityDataItem
import com.example.weatherapplication.model.room.WeatherInCityEntity
import com.example.weatherapplication.view.adapter.FavouritesDialogAdapter
import com.example.weatherapplication.view.adapter.SwipeCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.favourites_bottom_sheet_layout.view.*

class FavouritesBottomSheetDialog : BaseBottomSheetDialog(){
    override lateinit var mBehavior: BottomSheetBehavior<*>
    lateinit var adapter: FavouritesDialogAdapter
    lateinit var dialogView: View
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialogView = View.inflate(context, R.layout.favourites_bottom_sheet_layout, null)
        val root = dialogView.findViewById<ConstraintLayout>(R.id.favourites_root)
        root.layoutParams = root.layoutParams.apply {
            height = getScreenHeight()
        }
        initRecycler()
        setObservers()
        dialog.setContentView(dialogView)
        mBehavior = BottomSheetBehavior.from(dialogView.parent as View)
        return dialog
    }

    private fun initRecycler(){
        dialogView.favourites_sheet_recycler_view.layoutManager = LinearLayoutManager(context)
        adapter = FavouritesDialogAdapter {
            viewModel.updateCurrentWeather(convertEntityToCityDataItem(it))
            dismiss()
        }
        dialogView.favourites_sheet_recycler_view.adapter = adapter
        val callback = SwipeCallback{ viewModel.removeFromFavourites(convertEntityToCityDataItem(it)) }
        MyApplication.component.inject(callback)
        ItemTouchHelper(callback).attachToRecyclerView(dialogView.favourites_sheet_recycler_view)
    }

    private fun setState(state: FavouritesBottomSheetStates?){
        when(state){
            FavouritesBottomSheetStates.Loading-> showLoading()
            is FavouritesBottomSheetStates.GotResult-> setDefaultState(state.data)
            FavouritesBottomSheetStates.EmptyList-> setEmptyState()
            null-> hideLoading()
        }
    }

    fun setObservers(){
        viewModel.getWeatherFavouritesState().observe(this, Observer {
            setState(it)
        })
        viewModel.getWeatherFavouritesAction().observe(this, Observer {
            setState(it)
        })
    }

    private fun setEmptyState(){
        dialogView.favourites_sheet_info.visibility = View.VISIBLE
    }
    private fun setDefaultState(list: List<WeatherInCityEntity?>){
        dialogView.favourites_sheet_info.visibility = View.GONE
        adapter.submitList(list)
    }

    private fun showLoading(){
        dialogView.fragment_favourites_progress_bar.visibility = View.VISIBLE
    }

    private fun hideLoading(){
        dialogView.fragment_favourites_progress_bar.visibility = View.GONE
    }

}