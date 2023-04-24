package com.example.gerenda.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gerenda.R
import com.example.gerenda.databinding.StockItemBinding
import com.example.gerenda.model.StockModel

class StockAdapter(private val items: List<StockModel>,private val showDetails: (stockModel:StockModel)->Unit) : RecyclerView.Adapter<StockAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        //val view = inflater.inflate(R.layout.stock_item, parent, false)
        //return ViewHolder(view)
        val binding = StockItemBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position],position)

    inner class ViewHolder(val binding: StockItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StockModel,position: Int) {
            binding.item = item
            binding.itempos = position
            binding.executePendingBindings()
            binding.root.setOnClickListener { showDetails(item)
            }
        }
    }
}