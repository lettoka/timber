package com.example.gerenda.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gerenda.R
import com.example.gerenda.adapter.StockDetailAdapter
import com.example.gerenda.database.KotlinDatabase
import com.example.gerenda.databinding.ActivityStockDetailBinding
import com.example.gerenda.model.OrderPartModel
import com.example.gerenda.model.StockDetailModel
import com.example.gerenda.model.StockModel
import kotlinx.android.synthetic.main.activity_stock_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StockDetailActivity : DatabaseActivity() {
    lateinit var stockModel:StockModel
    override fun onDBConnSuccess() {
        getListData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_stock_detail)
        val binding = DataBindingUtil.setContentView<ActivityStockDetailBinding>(this,R.layout.activity_stock_detail)
        binding.loading=loadingIndicator
        stockModel = intent.getSerializableExtra("stockmodel") as StockModel
        detail_title.text=stockModel.title
        detail_button_back.setOnClickListener { onBackPressed() }
        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        detail_list.layoutManager=layoutManager
        detail_list.itemAnimator=DefaultItemAnimator()
        detail_swipe_refresh.setOnRefreshListener { getListData() }
        getListData()
    }

    fun getListData() {
        if(isQueryInProgress)
            return
        isQueryInProgress=true
        lifecycleScope.launch(Dispatchers.IO)  {
            KotlinDatabase.executeRawQuery<StockDetailModel>(StockDetailModel, """SELECT
  "termek"."ter_asz",
  "termek"."tc2_kod",
  "termek"."ter_mer3",
  "termek"."ter_serult",
  (CASE
    WHEN ("termek"."ter_szin" = 6)
    THEN 'I'
    ELSE 'N'
  END) AS "jo_maradek",
  (SELECT
    SUM("mozg_t"."mt_menny" - "mozg_t"."mt_fogy")
  FROM
    "mozg_t"
  WHERE
    ("mozg_t"."ter_kod" = "termek"."ter_kod") AND
    ("mozg_t"."mt_kesz" = '3')
  ) AS "jo_keszlet",
  (SELECT
    SUM("foglal"."fog_menny")
  FROM
    "foglal"
  WHERE
    ("foglal"."ter_kod" = "termek"."ter_kod")
  ) AS "jo_foglal"
FROM
  "termek"
WHERE
  ("termek"."tc2_kod" = ${stockModel.id}) AND
  ("termek"."ter_kell" = 'I') AND
  ("termek"."ter_keszl" = 'I')
ORDER BY
  "termek"."ter_mer3" DESC""") { list ->
                runOnUiThread {
                    detail_list.adapter = StockDetailAdapter(list)
                    detail_swipe_refresh.isRefreshing = false
                    isQueryInProgress = false
                }
            }
        }
    }
}