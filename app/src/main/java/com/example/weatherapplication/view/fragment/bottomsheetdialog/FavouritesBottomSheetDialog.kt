package com.example.weatherapplication.view.fragment.bottomsheetdialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapplication.R
import com.example.weatherapplication.model.SearchAdapterItem
import com.example.weatherapplication.model.convertEntityToCityDataItem
import com.example.weatherapplication.model.room.WeatherInCityEntity
import com.example.weatherapplication.view.adapter.FavouritesDialogAdapter
import com.example.weatherapplication.view.adapter.SearchDialogAdapter
import com.example.weatherapplication.view.adapter.SearchDialogAdapterCallback
import com.example.weatherapplication.view.adapter.SwipeCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.favourites_bottom_sheet_layout.view.*
import kotlinx.android.synthetic.main.fragment_weather_display_layout.*
import kotlinx.android.synthetic.main.fragment_weather_display_layout.view.*
import kotlinx.android.synthetic.main.search_bottom_sheet_layout.view.*

class FavouritesBottomSheetDialog : BaseBottomSheetDialog(){
    override lateinit var mBehavior: BottomSheetBehavior<*>
    lateinit var adapter: FavouritesDialogAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view: View = View.inflate(context, R.layout.favourites_bottom_sheet_layout, null)
        val root = view.findViewById<ConstraintLayout>(R.id.favourites_root)
        root.layoutParams = root.layoutParams.apply {
            height = getScreenHeight()
        }
        initRecycler(view)
        viewModel.getWeatherList().observe(this, Observer {
            adapter.submitList(it)
            setState(view, it)
        })
        dialog.setContentView(view)
        mBehavior = BottomSheetBehavior.from(view.parent as View)
        return dialog
    }

    private fun initRecycler(view: View){
        view.favourites_sheet_recycler_view.layoutManager = LinearLayoutManager(context)
        adapter = FavouritesDialogAdapter {
            viewModel.updateCurrentWeather(convertEntityToCityDataItem(it))
            dismiss()
        }
        view.favourites_sheet_recycler_view.adapter = adapter
        val callback = SwipeCallback{ viewModel.removeFromFavourites(convertEntityToCityDataItem(it)) }
        ItemTouchHelper(callback).attachToRecyclerView(view.favourites_sheet_recycler_view)
    }

    private fun setState(view: View, list: List<WeatherInCityEntity?>){
        if (list.isEmpty()){
            setEmptyState(view)
        }
        else{
            setDefaultState(view)
        }
    }

    fun setEmptyState(view: View){
        view.favourites_sheet_info.visibility = View.VISIBLE
    }
    private fun setDefaultState(view: View){
        view.favourites_sheet_info.visibility = View.GONE
    }

}