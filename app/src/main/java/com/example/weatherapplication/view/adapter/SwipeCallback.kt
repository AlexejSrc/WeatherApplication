package com.example.weatherapplication.view.adapter

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.model.room.WeatherInCityEntity
import javax.inject.Inject

class SwipeCallback(private val deleteItemCallback: (WeatherInCityEntity)->Unit) : ItemTouchHelper.SimpleCallback(0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    @Inject
    lateinit var trashBitmap: Bitmap
    override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (direction == ItemTouchHelper.RIGHT){
            deleteItemCallback((viewHolder as FavouritesDialogAdapter.ItemViewHolder).item)
        }
    }

    var swipeBack = false
    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = false
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(
                c, recyclerView, viewHolder, dX,
                dY, actionState, isCurrentlyActive
        )
        val itemView = viewHolder.itemView
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val paint = Paint().apply { color = Color.RED }
            paint.textSize = 40F
            paint.textAlign = Paint.Align.CENTER
            val height = itemView.top + itemView.height / 2
            if (dX>0 && dX<itemView.right){
                c.drawBitmap(trashBitmap, 150F, (height-(paint.textSize/2)-1), paint)
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }



}