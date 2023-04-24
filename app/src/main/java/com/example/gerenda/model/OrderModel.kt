package com.example.gerenda.model

import androidx.databinding.BaseObservable
import com.example.gerenda.adapter.StockAdapter
import com.example.gerenda.database.DatabaseTransformable
import com.example.gerenda.types.OrderType
import java.io.Serializable
import java.lang.Exception
import java.sql.ResultSet

class OrderModel(val orderId:Int,val orderNumber:String,val ordererName:String,val deadline:String,val isProcessing:Boolean=false):BaseObservable(),Serializable {


    companion object:DatabaseTransformable<OrderModel>{

        override fun  TransformData(set: ResultSet): OrderModel {
            return OrderModel(set.getInt("rager_0"),set.getString("ara_szam"),
                set.getString("jo_vevo")?:"",set.getString("jo_hatarido"),
            getIsProcessing(set))
        }
        fun getIsProcessing(set:ResultSet):Boolean{
            return try {
                set.getString("feldolgozva")=="N"
            } catch (e:Exception){
                return false
            }
        }
    }
}