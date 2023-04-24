package com.example.gerenda.activities

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gerenda.R
import com.example.gerenda.adapter.OrderPartAdapter
import com.example.gerenda.database.KotlinDatabase
import com.example.gerenda.databinding.ActivityOrderPartBinding
import com.example.gerenda.model.OrderModel
import com.example.gerenda.model.OrderPartModel
import com.example.gerenda.types.OrderType
import kotlinx.android.synthetic.main.activity_order_part.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderPartActivity : DatabaseActivity() {
    lateinit var orderModel: OrderModel
    lateinit var type:OrderType


    override fun onDBConnSuccess() {
        getListData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityOrderPartBinding>(
            this,
            R.layout.activity_order_part
        )
        orderModel = intent.getSerializableExtra("ordermodel") as OrderModel
        type  = intent.getSerializableExtra("type") as OrderType
        binding.order = orderModel
        binding.loading = loadingIndicator
        opart_button_back.setOnClickListener { onBackPressed() }
        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        opart_list.layoutManager = layoutManager
        opart_list.itemAnimator = DefaultItemAnimator()
        opart_swipe_refresh.setOnRefreshListener { getListData() }

    }

    override fun onResume() {
        super.onResume()
        getListData()
    }

    fun showDetails(orderPartModel: OrderPartModel,dtype:DetailType?=null){
        var act= if(orderPartModel.cutDone )
                    RecycleActivity::class.java
                else OrderDetailActivity::class.java
        if(dtype!=null){
            act = if (dtype==DetailType.RECYCLE)
                RecycleActivity::class.java
            else OrderDetailActivity::class.java
        }
        val intent= Intent(this,act)
        intent.putExtra("ordermodel",orderModel)
        intent.putExtra("orderpartmodel",orderPartModel)
        intent.putExtra("type",type)
        startActivity(intent)
    }

    fun getListData() {
        if (isQueryInProgress)
            return
        isQueryInProgress = true
        lifecycleScope.launch(Dispatchers.IO) {
            KotlinDatabase.executeRawQuery(
                OrderPartModel,
                OrderPartModel.getQuery(orderModel)
            ) { list ->
                runOnUiThread {
                    opart_list.adapter = OrderPartAdapter(list){
                        showDetails(it)
                    }
                    opart_swipe_refresh.isRefreshing = false
                    isQueryInProgress = false

                }
            }
        }
    }
    public enum class DetailType(){
        RECYCLE,ORDERDETAIl
    }
}