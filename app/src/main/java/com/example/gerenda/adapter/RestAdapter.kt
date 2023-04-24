package com.example.gerenda.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gerenda.databinding.RestItemBinding
import com.example.gerenda.model.RestModel

class RestAdapter(private val items: List<RestModel>,val clickEnabled:Boolean
,val updateData: (orderDetail:RestModel)->Unit) : RecyclerView.Adapter<RestAdapter.ViewHolder>() {
var updateBlocked=false //before assigning new adapter to the recyclerview block updates to prevent focuschange trigger
    var selectedRest : RestModel?=null
    set(value) {
        if(field==value || updateBlocked)return
        if(field!=null && field!!.trueRest!=lastValue) {
            updateBlocked = true
            updateData(field!!)
        }
        field=value
        lastValue=value!!.trueRest
    }
    var lastValue:Int=0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        //val view = inflater.inflate(R.layout.stock_item, parent, false)
        //return ViewHolder(view)
        val binding = RestItemBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position],position)

    inner class ViewHolder(val binding: RestItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RestModel,position: Int) {
            binding.item = item
            binding.itempos=position
            binding.clickable=clickEnabled
            //binding.clickable=clickEnabled

            /*binding.odetailDone.setOnClickListener{
                cutStateChange(item,binding.odetailDone.isChecked)
            }*/
            binding.restBroken.setOnClickListener {
                it.requestFocusFromTouch()
                if(!updateBlocked){
                    updateBlocked=true
                    updateData(item)
                }
            }
            binding.trueRestInput.setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus){
                    selectedRest=binding.item
                    return@setOnFocusChangeListener
                }
               /* if(!updateBlocked){
                    updateBlocked=true
                    updateData(item)
                }*/
            }
            binding.executePendingBindings()
        }
    }

}