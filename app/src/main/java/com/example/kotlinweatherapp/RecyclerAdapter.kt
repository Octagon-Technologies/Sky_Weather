package com.example.kotlinweatherapp

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinweatherapp.databinding.RecyclerItemBinding
import com.example.kotlinweatherapp.network.All
import com.example.kotlinweatherapp.network.Weather

class RecyclerAdapter(val clickListener: RecyclerListener): ListAdapter<All, RecyclerAdapter.ViewHolder>(DiffCallback){
    class ViewHolder(private val binding: RecyclerItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(all: All, weather: Weather){
            binding.all = all
            binding.weather = weather
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RecyclerItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val all = getItem(position)

        holder.itemView.setOnClickListener {
            clickListener.onClick(all)
            Log.d("RecyclerAdapter", "holder.itemview.setOnClickListener has been called")
        }

        Log.i("RecyclerAdapter", "position is $position")
        val weather = all.weather[0]
       Log.i("RecyclerAdapter", "weather.description = ${weather.description}")

        holder.bind(all, weather)
    }

}

object DiffCallback: DiffUtil.ItemCallback<All>() {
    override fun areItemsTheSame(oldItem: All, newItem: All): Boolean {
        return oldItem.dt == newItem.dt
    }

    override fun areContentsTheSame(oldItem: All, newItem: All): Boolean {
        return oldItem == newItem
    }
}

class RecyclerListener(val clickListenerLambda: (eachAll: All) -> Unit){
    fun onClick(eachAll: All) = clickListenerLambda(eachAll)
}
