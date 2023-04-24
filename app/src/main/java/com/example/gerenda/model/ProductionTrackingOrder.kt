package com.example.gerenda.model

import com.example.gerenda.database.DatabaseTransformable
import com.example.gerenda.types.StockType
import java.sql.ResultSet

data class ProductionTrackingOrder(
    val id : String,
    val trackingID : Int//uzemi_kod

){
    companion object: DatabaseTransformable<ProductionTrackingOrder> {
            override fun  TransformData(set: ResultSet):ProductionTrackingOrder{
                return ProductionTrackingOrder(set.getString("ara_szam"),set.getInt("uzemi_kod"))

            }

        fun getQuery() : String{
            return """
                 select "ara_szam","uzemi_kod" from "arajanl_f" where "uzemi_kod" IS NOT NULL
            """
        }

    }
}
