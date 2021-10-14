package com.imanfz.simpleweather.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imanfz.simpleweather.data.remote.response.DailyItem
import com.imanfz.simpleweather.databinding.ItemRowWeatherBinding
import com.imanfz.simpleweather.utils.getDay
import com.imanfz.simpleweather.utils.loadImageFromUrl
import com.imanfz.simpleweather.utils.toCelsius

class MainAdapter(
    private val listener: OnClickListener
) : RecyclerView.Adapter<MainAdapter.ItemViewHolder>() {
    private var list = ArrayList<DailyItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<DailyItem>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ItemRowWeatherBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener {
            listener.onClick(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ItemViewHolder(
        private val binding: ItemRowWeatherBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DailyItem) {
            with(binding) {
                item.weather?.get(0)?.icon?.let { ivIcon.loadImageFromUrl(it) }
                tvDaily.text = item.dt?.getDay()
                tvTemp.text = (item.temp?.day ?: 0.0).toCelsius()
                tvTempMin.text = (item.temp?.min ?: 0.0).toCelsius()
                tvWeatherStatus.text = item.weather?.get(0)?.main
            }
        }
    }

    interface OnClickListener {
        fun onClick(data: DailyItem)
    }
}