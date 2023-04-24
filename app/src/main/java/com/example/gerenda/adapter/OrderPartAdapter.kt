package com.example.gerenda.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gerenda.databinding.OrderPartItemBinding
import com.example.gerenda.model.OrderPartModel

class OrderPartAdapter(private val items: List<OrderPartModel>,private val showDetails: (orderModel:OrderPartModel)->Unit) : RecyclerView.Adapter<OrderPartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        //val view = inflater.inflate(R.layout.stock_item, parent, false)
        //return ViewHolder(view)
        val binding = OrderPartItemBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position],position)

    inner class ViewHolder(val binding: OrderPartItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderPartModel,position: Int) {
            binding.item = item
            binding.itempos=position
            binding.root.setOnClickListener { showDetails(item) }

            binding.executePendingBindings()
        }
    }
}