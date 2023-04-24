package com.example.gerenda.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gerenda.R
import com.example.gerenda.types.OrderType
import kotlinx.android.synthetic.main.activity_order_menu.*

class OrderMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_menu)

        backButton.setOnClickListener { onBackPressed() }
        new_orders.setOnClickListener { showOrderActivity(OrderType.NEW) }
        ongoing_orders.setOnClickListener { showOrderActivity(OrderType.INPROGRESS) }
        closed_orders.setOnClickListener { showOrderActivity(OrderType.CLOSED) }

    }
    fun showOrderActivity(type: OrderType){
        val intent = Intent(this,OrderActivity::class.java)
        intent.putExtra("type",type)
        startActivity(intent)
    }
}