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

        fun getQuery(userID : Int) : String{
//            return """
//                 select "ara_szam","uzemi_kod" from "arajanl_f" where "uzemi_kod" IS NOT NULL
//            """
            return """
            select "arajanl_f"."ara_szam","arajanl_f"."uzemi_kod" from "arajanl_f"
            where ("arajanl_f"."uzemi_kod" IS NOT NULL AND (SELECT DISTINCT "uzemi_jog"."uzemi_j" from "uzemi" LEFT OUTER JOIN "uzemi_jog" on "uzemi"."ter_kod" = "uzemi_jog"."ter_kod"  where "uzemi_jog"."jel_kod" = $userID AND "uzemi"."uzemi_kod"  = "arajanl_f"."uzemi_kod"   )  IS NOT NULL)
        """
        }

    }
}
