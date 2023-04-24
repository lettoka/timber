package com.example.gerenda.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gerenda.databinding.OrderItemBinding
import com.example.gerenda.model.OrderModel

class OrderAdapter(private val items: List<OrderModel>,private val showDetails: (orderModel:OrderModel)->Unit) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        //val view = inflater.inflate(R.layout.stock_item, parent, false)
        //return ViewHolder(view)
        val binding = OrderItemBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position],position)

    inner class ViewHolder(val binding: OrderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderModel,position: Int) {
            binding.item = item
            binding.itempos=position
            binding.executePendingBindings()
            binding.root.setOnClickListener { showDetails(item)
            }
        }
    }
}