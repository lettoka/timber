package com.example.gerenda.model

import com.example.gerenda.database.DatabaseTransformable
import com.example.gerenda.types.StockType
import java.sql.ResultSet

data class ProductionTrackingOrder(
    val id : String,
    val trackingID : Int,//uzemi_kod
    val ordererName : String

){
    companion object: DatabaseTransformable<ProductionTrackingOrder> {
            override fun  TransformData(set: ResultSet):ProductionTrackingOrder{
                return ProductionTrackingOrder(set.getString("ara_szam"),set.getInt("uzemi_kod"),
                    kotlin.runCatching {   set.getString("vevo_rnev")}.getOrNull() ?: "")

            }

        fun getListForProcess(processID: Int): String{
            return  """
                select "arajanl_f"."ara_szam","arajanl_f"."uzemi_kod","vevok"."vevo_rnev"
 from "arajanl_f"
 LEFT OUTER JOIN "vevok" on "arajanl_f"."vevo_kod" = "vevok"."vevo_kod" --be kell majd joinolni ay uzemi_f-et es szurni h indult
where
("arajanl_f"."uzemi_kod" IS NOT NULL
AND (SELECT COUNT(*) from "uzemi"   where "uzemi"."ter_kod"  = $processID AND "uzemi"."uzemi_kod" = "arajanl_f"."uzemi_kod"  )   > 0)
            """
        }
        fun getQuery(userID : Int) : String{
//            return """
//                 select "ara_szam","uzemi_kod" from "arajanl_f" where "uzemi_kod" IS NOT NULL
//            """
            return """
            

 select "arajanl_f"."ara_szam","arajanl_f"."uzemi_kod","vevok"."vevo_rnev"
 from "arajanl_f"
 LEFT OUTER JOIN "vevok" on "arajanl_f"."vevo_kod" = "vevok"."vevo_kod"
where
("arajanl_f"."uzemi_kod" IS NOT NULL
AND (SELECT COUNT(*) from "uzemi" LEFT OUTER JOIN "uzemi_jog" on "uzemi"."ter_kod" = "uzemi_jog"."ter_kod"  where "uzemi_jog"."jel_kod" = $userID AND "uzemi"."uzemi_kod"  = "arajanl_f"."uzemi_kod" AND "uzemi_jog"."uzemi_j" > 1  )   > 0)
        """
        }

    }
}
