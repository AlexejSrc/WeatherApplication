package com.example.weatherapplication.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.R
import com.example.weatherapplication.model.SearchAdapterItem
import kotlinx.android.synthetic.main.search_item_layout.view.*

class SearchDialogAdapter(val callback: SearchDialogAdapterCallback) : ListAdapter<SearchAdapterItem, SearchDialogAdapter.ItemViewHolder>(DIFF_CALLBACK) {

    companion object{
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<SearchAdapterItem> =
                object : DiffUtil.ItemCallback<SearchAdapterItem>() {
                    override fun areItemsTheSame(oldItem: SearchAdapterItem, newItem: SearchAdapterItem): Boolean {
                        return oldItem.city==newItem.city && oldItem.countryCode == newItem.countryCode
                    }

                    override fun areContentsTheSame(oldItem: SearchAdapterItem, newItem: SearchAdapterItem): Boolean {
                        return oldItem.city==newItem.city && oldItem.countryCode == newItem.countryCode && oldItem.isInFavourites == newItem.isInFavourites
                    }
                }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var item: SearchAdapterItem
        init {
            itemView.search_item_star_button.setOnClickListener {
                if(item.isInFavourites){
                    callback.onDeleteItem(item)
                }
                else{
                    callback.onAddItem(item)
                }
            }
            itemView.setOnClickListener {
                callback.onOpenItem(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.search_item_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.item = item
        holder.itemView.search_item_city_name.text = item.city
        holder.itemView.search_item_country_name.text = holder.itemView.context.getString(R.string.country, item.countryCode)
        holder.itemView.search_item_star_button.setImageDrawable(
                ContextCompat.getDrawable(holder.itemView.context,
                        if(item.isInFavourites) R.drawable.in_favourites_icon else R.drawable.not_in_favourites_icon))
    }
}