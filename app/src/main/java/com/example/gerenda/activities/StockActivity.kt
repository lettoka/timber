package com.example.gerenda.activities

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gerenda.R
import com.example.gerenda.adapter.StockAdapter
import com.example.gerenda.database.KotlinDatabase
import com.example.gerenda.databinding.ActivityStockBinding
import com.example.gerenda.model.LoadingModel
import com.example.gerenda.types.StockType
import com.example.gerenda.model.StockModel
import kotlinx.android.synthetic.main.activity_stock.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StockActivity : DatabaseActivity() {
    lateinit var type: StockType


    override fun onDBConnSuccess() {
        getListData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityStockBinding>(this,R.layout.activity_stock)
        binding.loading=loadingIndicator
        type  = intent.getSerializableExtra("type") as StockType
        stockTitle.text=type.title
        backButton.setOnClickListener { onBackPressed() }
        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        stock_list.layoutManager = layoutManager
        stock_list.itemAnimator = DefaultItemAnimator()
        swipe_refresh.setOnRefreshListener { getListData() }
        getListData()
    }
    fun getListData() {
        if(isQueryInProgress)
            return
        isQueryInProgress=true
        lifecycleScope.launch(Dispatchers.IO)  {
            KotlinDatabase.executeRawQuery<StockModel>(StockModel, type.query) { list ->
                runOnUiThread {
                    stock_list.adapter = StockAdapter(list) { stockModel ->
                        showDetails(stockModel)
                    }
                    swipe_refresh.isRefreshing = false
                    isQueryInProgress = false
                }
            }
        }
    }
    fun showDetails(stockModel: StockModel) {
        val intent= Intent(this,StockDetailActivity::class.java)
        intent.putExtra("stockmodel",stockModel)
        startActivity(intent)
    }

}