package com.example.gerenda.activities

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gerenda.R
import com.example.gerenda.adapter.OrderDetailAdapter
import com.example.gerenda.database.KotlinDatabase
import com.example.gerenda.databinding.ActivityOrderDetailBinding
import com.example.gerenda.databinding.PopupCutDetailsBinding
import com.example.gerenda.databinding.PopupCutdoneBinding
import com.example.gerenda.model.LoadingModel
import com.example.gerenda.model.OrderDetailModel
import com.example.gerenda.model.OrderModel
import com.example.gerenda.model.OrderPartModel
import com.example.gerenda.types.OrderType
import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.popup_cutdone.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderDetailActivity : DatabaseActivity() {
    lateinit var orderModel:OrderModel
    lateinit var type:OrderType

    lateinit var orderPartModel: OrderPartModel

    override fun onDBConnSuccess() {
        getListData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityOrderDetailBinding>(this,R.layout.activity_order_detail)
        orderModel = intent.getSerializableExtra("ordermodel") as OrderModel
        orderPartModel = intent.getSerializableExtra("orderpartmodel") as OrderPartModel
        type  = intent.getSerializableExtra("type") as OrderType
        binding.order= orderModel
        binding.loading=loadingIndicator
        binding.orderpart=orderPartModel
        binding.clickable=isEditingEnabled()
        odetail_button_back.setOnClickListener { onBackPressed() }
        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        odetail_list.layoutManager=layoutManager
        odetail_list.itemAnimator= DefaultItemAnimator()
        odetail_swipe_refresh.setOnRefreshListener { getListData() }
        goto_recycle_button.setOnClickListener {
            val intent= Intent(this,RecycleActivity::class.java)
        intent.putExtra("ordermodel",orderModel)
        intent.putExtra("orderpartmodel",orderPartModel)
        intent.putExtra("type",type)
        startActivity(intent)
            onBackPressed()
        }
       // odetail_swipe_refresh.setDistanceToTriggerSync(600)
        getListData()
    }
    fun getListData() {
        if(isQueryInProgress)
            return
        isQueryInProgress=true
        lifecycleScope.launch(Dispatchers.IO)  {
            KotlinDatabase.executeRawQuery(OrderDetailModel, OrderDetailModel.getQuery(orderPartModel)) { list ->
                runOnUiThread {
                    odetail_list.adapter = OrderDetailAdapter(list,isEditingEnabled(),::cutUpdate,::showCuts )
                    odetail_swipe_refresh.isRefreshing = false
                    isQueryInProgress = false
                }
            }
        }
    }
    fun isEditingEnabled():Boolean{
        return !orderPartModel.recycleDone && type!=OrderType.CLOSED
    }

    fun showCuts(orderDetailModel: OrderDetailModel){
        val popup = AlertDialog.Builder(this)
        val binding = PopupCutDetailsBinding.inflate(this.layoutInflater)
        popup.setView(binding.root)
        popup.setCancelable(true)
        val finalpopup = popup.create();
        finalpopup.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        binding.cutDetailBody.text = orderDetailModel.cut
        finalpopup.show()
        binding.root.scaleY = 0.1f
        binding.root.scaleX = 0.1f
        binding.root.animate().scaleY(1f).scaleX(1f)
            .setInterpolator(AccelerateDecelerateInterpolator()).duration = 600
    }

    fun showConfirmCut(orderDetailModel: OrderDetailModel,cutDone:Boolean) {
        val popup = AlertDialog.Builder(this)
        val binding = PopupCutdoneBinding.inflate(this.layoutInflater)
        binding.orderpart = orderPartModel
        popup.setView(binding.root)
        popup.setCancelable(false)
        val finalpopup = popup.create();
        finalpopup.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        binding.cutdoneSubmit.setOnClickListener {
            updateInDatabase(orderDetailModel, cutDone,true) {
                runOnUiThread { onBackPressed() }
            }
            finalpopup.dismiss()
        }
        binding.cutdoneCancel.setOnClickListener {
            finalpopup.dismiss()
            getListData()
        }
        finalpopup.show()
        binding.root.scaleY = 0.1f
        binding.root.scaleX = 0.1f
        binding.root.animate().scaleY(1f).scaleX(1f)
            .setInterpolator(AccelerateDecelerateInterpolator()).duration = 600
    }
    fun cutUpdate(orderDetailModel: OrderDetailModel,cutDone:Boolean){
        if((odetail_list.adapter as OrderDetailAdapter).isAllDone()) {
            showConfirmCut(orderDetailModel, cutDone);return
        }
        updateInDatabase(orderDetailModel,cutDone,false,::getListData)
    }
    fun updateInDatabase(orderDetailModel: OrderDetailModel,cutDone:Boolean,fullCutDone:Boolean=false,onSuccess:(()->Unit)?=null){
        val state= if (cutDone) "K" else "N"
        lifecycleScope.launch(Dispatchers.IO)  {
            KotlinDatabase.executeUpdate("""UPDATE "rager_0" set "rag0_rvagva"='$state' where "rager_sz"=${orderDetailModel.groupId} and "rag0_sorsz"=${orderDetailModel.ordNumber} """)
            if(fullCutDone) {
                KotlinDatabase.executeUpdate(
                    """UPDATE "rager_f" set "rager_jel_vagva"= 
                 (CASE 
                    WHEN ((SELECT DISTINCT "rager_0"."rag0_rvagva" 
                        FROM "rager_0" INNER JOIN "rager_f" ON "rager_0"."rager_sz"="rager_f"."rager_sz" 
                        WHERE 
                        ("rager_f"."rager_sz" = ${orderDetailModel.groupId}) AND 
                        ("rager_0"."rag0_rvagva" = 'N') 
                        ) IS NULL) 
                    THEN ('K') 
                    ELSE ('N') 
                 END) 
            where "rager_sz"=${orderDetailModel.groupId} """
                )

                KotlinDatabase.executeUpdate(
                    """UPDATE "rager_f" set "rager_jel_marad"= 
                 (CASE 
                    WHEN ((SELECT DISTINCT "rager_0"."rag0_marad" 
                        FROM "rager_0" INNER JOIN "rager_f" ON "rager_0"."rager_sz"="rager_f"."rager_sz" 
                        WHERE 
                        ("rager_f"."rager_sz" = ${orderDetailModel.groupId}) AND 
                        ("rager_0"."rag0_marad" > 0) 
                        ) IS NOT NULL) 
                    THEN ('N') 
                    ELSE ('K') 
                 END) 
            where "rager_sz"=${orderDetailModel.groupId} """)
            }
            //getListData()
            onSuccess?.invoke()
        }
    }
}