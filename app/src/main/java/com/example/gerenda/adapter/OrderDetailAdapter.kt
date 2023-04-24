package com.example.gerenda.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gerenda.databinding.OrderDetailItemBinding
import com.example.gerenda.model.OrderDetailModel

class OrderDetailAdapter(private val items: List<OrderDetailModel>,val clickEnabled:Boolean
,private val cutStateChange: (orderDetail:OrderDetailModel,state:Boolean)->Unit,private val showCuts: (orderDetail:OrderDetailModel)->Unit) : RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        //val view = inflater.inflate(R.layout.stock_item, parent, false)
        //return ViewHolder(view)
        val binding = OrderDetailItemBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position],position)

    inner class ViewHolder(val binding: OrderDetailItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderDetailModel,position: Int) {
            binding.item = item
            binding.itempos=position
            binding.clickable=clickEnabled
            if(!clickEnabled)
                item.done=true

            binding.odetailDone.setOnClickListener{
                cutStateChange(item,binding.odetailDone.isChecked)
            }

            binding.odetailCut.setOnClickListener{
                showCuts(item)
            }

            binding.executePendingBindings()
        }
    }
    fun isAllDone():Boolean{
        for(item in items)
            if(!item.done)
                return false
        return true
    }
}