package com.example.gerenda.activities

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gerenda.R
import com.example.gerenda.adapter.OrderAdapter
import com.example.gerenda.database.KotlinDatabase
import com.example.gerenda.databinding.ActivityOrderBinding
import com.example.gerenda.model.LoadingModel
import com.example.gerenda.types.OrderItemOrdering
import com.example.gerenda.types.OrderType
import com.example.gerenda.model.OrderModel
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderActivity : DatabaseActivity() {

    var lastDataOrdering= OrderItemOrdering.ORDERER_NAME
    lateinit var type: OrderType


    override fun onDBConnSuccess() {
        getListData()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = setContentView<ActivityOrderBinding>(this,R.layout.activity_order)
        binding.act= this
        binding.loading=loadingIndicator
        type  = intent.getSerializableExtra("type") as OrderType
        title_order.text = type.title
        order_button_back.setOnClickListener { onBackPressed() }
        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        order_list.layoutManager = layoutManager
        order_list.itemAnimator = DefaultItemAnimator()
        order_swipe_refresh.setOnRefreshListener { getListData() }

    }

    override fun onResume() {
        super.onResume()
         getListData()
    }

    fun getListData() {
        if(isQueryInProgress)
            return
        //dataLoading.loading=true
        isQueryInProgress=true
        lifecycleScope.launch(Dispatchers.IO)  {
            val plusOrder= if(type==OrderType.CLOSED) " \"feldolgozva\", " else ""
            val q="${type.query} ORDER BY $plusOrder ${lastDataOrdering.order}"
            KotlinDatabase.executeRawQuery<OrderModel>(OrderModel, q) { list ->
                runOnUiThread {
                    order_list.adapter = OrderAdapter(list) { orderModel ->
                        showDetails(orderModel)
                    }
                    //dataLoading.loading=false
                    order_swipe_refresh.isRefreshing = false
                    isQueryInProgress = false
                }
            }
        }
    }
    fun onOrderingChange(neworder: OrderItemOrdering){
        lastDataOrdering=neworder
        getListData()
    }
    fun showDetails(orderModel: OrderModel) {
        val intent= Intent(this,OrderPartActivity::class.java)
        intent.putExtra("ordermodel",orderModel)
        intent.putExtra("type",type)
        startActivity(intent)
    }
}