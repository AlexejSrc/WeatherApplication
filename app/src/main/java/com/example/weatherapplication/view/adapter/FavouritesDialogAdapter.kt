package com.example.weatherapplication.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.R
import com.example.weatherapplication.model.room.WeatherInCityEntity
import kotlinx.android.synthetic.main.search_item_layout.view.*

class FavouritesDialogAdapter(private val openWeatherCallback: (WeatherInCityEntity)->Unit) : ListAdapter<WeatherInCityEntity, FavouritesDialogAdapter.ItemViewHolder>(DIFF_CALLBACK) {

    companion object{
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<WeatherInCityEntity> =
                object : DiffUtil.ItemCallback<WeatherInCityEntity>() {
                    override fun areItemsTheSame(oldItem: WeatherInCityEntity, newItem: WeatherInCityEntity): Boolean {
                        return oldItem.city==newItem.city && oldItem.countryCode == newItem.countryCode
                    }

                    override fun areContentsTheSame(oldItem: WeatherInCityEntity, newItem: WeatherInCityEntity): Boolean {
                        return oldItem.city==newItem.city && oldItem.countryCode == newItem.countryCode && oldItem.temperature == newItem.temperature
                    }
                }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var item: WeatherInCityEntity
        init {
            itemView.setOnClickListener {
                openWeatherCallback(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.search_item_layout, parent, false)
        ).apply { itemView.search_item_star_button.visibility = View.GONE }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.item = item
        holder.itemView.search_item_city_name.text = item.city
        holder.itemView.search_item_country_name.text = holder.itemView.context.getString(R.string.country, item.countryCode)
    }
}