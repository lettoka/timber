package com.example.gerenda.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gerenda.R
import com.example.gerenda.types.StockType
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        BSH_button.setOnClickListener { ShowStockActivity(StockType.BSH) }
        KVH_button.setOnClickListener { ShowStockActivity(StockType.KVH) }
        KRH_button.setOnClickListener { ShowStockActivity(StockType.KRH) }
        order_button.setOnClickListener { ShowOrderActivity() }
    }
    fun ShowStockActivity(type: StockType) {
        val intent = Intent(this,StockActivity::class.java)
        intent.putExtra("type",type)
        startActivity(intent)
    }
    fun ShowOrderActivity() {
        val intent = Intent(this,OrderMenuActivity::class.java)
        startActivity(intent)
    }
}