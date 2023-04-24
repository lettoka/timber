package com.example.gerenda.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gerenda.databinding.StockDetailItemBinding
import com.example.gerenda.databinding.StockItemBinding
import com.example.gerenda.model.StockDetailModel
import com.example.gerenda.model.StockModel

class StockDetailAdapter(private val items: List<StockDetailModel>) : RecyclerView.Adapter<StockDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        //val view = inflater.inflate(R.layout.stock_item, parent, false)
        //return ViewHolder(view)
        val binding = StockDetailItemBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position],position)

    inner class ViewHolder(val binding: StockDetailItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StockDetailModel,position: Int) {
            binding.item = item
            binding.itempos= position
            binding.executePendingBindings()

        }
    }
}