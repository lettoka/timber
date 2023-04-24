package com.example.gerenda.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gerenda.R
import com.example.gerenda.adapter.RestAdapter
import com.example.gerenda.database.KotlinDatabase
import com.example.gerenda.databinding.ActivityRecycleBinding
import com.example.gerenda.databinding.PopupRecycledoneBinding
import com.example.gerenda.model.OrderModel
import com.example.gerenda.model.OrderPartModel
import com.example.gerenda.model.RestModel
import com.example.gerenda.types.OrderType
import kotlinx.android.synthetic.main.activity_recycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecycleActivity : DatabaseActivity() {

    lateinit var orderPartModel: OrderPartModel
    lateinit var orderModel:OrderModel
    lateinit var type:OrderType

    override fun onDBConnSuccess() {
        getListData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityRecycleBinding>(this,R.layout.activity_recycle)
        orderModel = intent.getSerializableExtra("ordermodel") as OrderModel
        orderPartModel = intent.getSerializableExtra("orderpartmodel") as OrderPartModel
        type  = intent.getSerializableExtra("type") as OrderType
        binding.order= orderModel
        binding.loading=loadingIndicator
        binding.orderpart=orderPartModel
        binding.clickable=isEditingEnabled()
        recycle_button_back.setOnClickListener { onBackPressed() }
        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recycle_list.layoutManager=layoutManager
        recycle_list.itemAnimator= DefaultItemAnimator()
        recycle_swipe_refresh.setOnRefreshListener { getListData() }
        recycle_done_button.setOnClickListener { showConfirmRecycle() }
        recycle_calculated_button.setOnClickListener { copyCalculated() }
        goto_odetail_button.setOnClickListener {
            val intent= Intent(this,OrderDetailActivity::class.java)
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
            KotlinDatabase.executeRawQuery(RestModel, RestModel.getQuery(orderPartModel)) { list ->
                runOnUiThread {
                    (recycle_list.adapter as? RestAdapter)?.updateBlocked=true
                    recycle_list.adapter = RestAdapter(list,isEditingEnabled(),
                        ::updateInDatabase)
                    recycle_swipe_refresh.isRefreshing = false
                    isQueryInProgress = false
                }
            }
        }
    }
    fun isEditingEnabled():Boolean{
        return !orderPartModel.recycleDone && type!=OrderType.CLOSED
    }
    fun showConfirmRecycle() {
        val popup = AlertDialog.Builder(this)
        val binding = PopupRecycledoneBinding.inflate(this.layoutInflater)
        binding.orderpart = orderPartModel
        val restToUpdate=(recycle_list.adapter as? RestAdapter)?.selectedRest
        if(restToUpdate!=null)
            updateInDatabase(restToUpdate){
                runOnUiThread {
                    binding.recycledoneSubmit.isEnabled = true
                }
            }
        else
            binding.recycledoneSubmit.isEnabled=true
        popup.setView(binding.root)
        popup.setCancelable(false)
        val finalpopup = popup.create()
        finalpopup.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.recycledoneSubmit.setOnClickListener {
            recycleDoneForPart()
            finalpopup.dismiss()
        }
        binding.recycledoneCancel.setOnClickListener {
            finalpopup.dismiss()
            getListData()
        }
        finalpopup.show()
        binding.root.scaleY = 0.1f
        binding.root.scaleX = 0.1f
        binding.root.animate().scaleY(1f).scaleX(1f)
            .setInterpolator(AccelerateDecelerateInterpolator()).duration = 600
    }
    fun updateInDatabase(restModel: RestModel,callback:(()->Unit)=::getListData){
        val broken= if (restModel.broken) "I" else "N"
        lifecycleScope.launch(Dispatchers.IO)  {
            KotlinDatabase.executeUpdate("""UPDATE "rager_0" 
                set "rag0_marad2"=${restModel.trueRest} ,"rag0_serult2"='${broken}' 
                where "rager_sz"=${restModel.groupId} and "rag0_sorsz"=${restModel.ordNumber} """)
            callback.invoke()
        }
    }
    fun recycleDoneForPart(){
        lifecycleScope.launch(Dispatchers.IO)  {
            KotlinDatabase.executeUpdate("""UPDATE "rager_f" 
                set "rager_jel_marad"='K'  
                where "rager_sz"=${orderPartModel.id} """)
            runOnUiThread { onBackPressed() }
        }

    }
    fun copyCalculated(){
        lifecycleScope.launch(Dispatchers.IO)  {
            KotlinDatabase.executeUpdate("""UPDATE "rager_0" 
                set "rag0_marad2"="rag0_marad"
                where "rager_sz"=${orderPartModel.id} """)
            getListData()
        }
    }

}