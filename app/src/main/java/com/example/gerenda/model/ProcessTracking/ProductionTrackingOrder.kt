package com.example.gerenda.model.ProcessTracking

import com.example.gerenda.database.DatabaseTransformable
import java.sql.ResultSet

data class ProductionTrackingOrder(
    val id : String,
    val trackingID : Int,//uzemi_kod
    val ordererName : String,
    val creatorName : String,
    val startDate: String,
    val supervisorName : String

){
    companion object: DatabaseTransformable<ProductionTrackingOrder> {
            override fun  TransformData(set: ResultSet): ProductionTrackingOrder {
                return ProductionTrackingOrder(set.getString("ara_szam"),set.getInt("uzemi_kod"),
                    kotlin.runCatching {   set.getString("vevo_rnev")}.getOrNull() ?: "",set.getString("nev"),set.getString("uzemi_sdate"),
                    kotlin.runCatching {  set.getString("felelos_nev")}.getOrNull() ?: "-")


            }

        fun getListForProcessGroup(processGroupID: Int,startDate : String): String {
            return  """
                select "arajanl_f"."ara_szam","arajanl_f"."uzemi_kod","vevok"."vevo_rnev","uzemi_f"."uzemi_start","uzemi_f"."uzemi_sdate","jelszo"."nev","felelos"."nev" "felelos_nev"
 from "arajanl_f"
 LEFT OUTER JOIN "vevok" on "arajanl_f"."vevo_kod" = "vevok"."vevo_kod"
 LEFT outer JOIN "uzemi_f" on "arajanl_f"."uzemi_kod" = "uzemi_f"."uzemi_kod"
 LEFT outer JOIN "jelszo" on "arajanl_f"."jel_kod" = "jelszo"."jel_kod" 
 LEFT outer JOIN "jelszo" "felelos" on "uzemi_f"."uzemi_felel" = "felelos"."jel_kod"
where
("arajanl_f"."uzemi_kod" IS NOT NULL
AND "uzemi_f"."uzemi_start" = 'I' AND "uzemi_f"."uzemi_sdate"  <= '$startDate'
AND (SELECT COUNT(*) from "uzemi" LEFT OUTER JOIN "termek" on "termek"."ter_kod" = "uzemi"."ter_kod"   where "termek"."uzemt_kod"  = $processGroupID AND "uzemi"."uzemi_kod" = "arajanl_f"."uzemi_kod"  )   > 0
)
            """
        }

//        fun getListForProcess(processID: Int,startDate : String): String{
//            return  """
//                select "arajanl_f"."ara_szam","arajanl_f"."uzemi_kod","vevok"."vevo_rnev","uzemi_f"."uzemi_start","uzemi_f"."uzemi_sdate"
// from "arajanl_f"
// LEFT OUTER JOIN "vevok" on "arajanl_f"."vevo_kod" = "vevok"."vevo_kod"
// LEFT outer JOIN "uzemi_f" on "arajanl_f"."uzemi_kod" = "uzemi_f"."uzemi_kod"
//where
//("arajanl_f"."uzemi_kod" IS NOT NULL
//AND "uzemi_f"."uzemi_start" = 'I' AND "uzemi_f"."uzemi_sdate"  < '$startDate'
//AND (SELECT COUNT(*) from "uzemi"   where "uzemi"."ter_kod"  = $processID AND "uzemi"."uzemi_kod" = "arajanl_f"."uzemi_kod"  )   > 0
//)
//            """
//        }
//        fun getQuery(userID : Int) : String{
//            return """
//
//
// select "arajanl_f"."ara_szam","arajanl_f"."uzemi_kod","vevok"."vevo_rnev"
// from "arajanl_f"
// LEFT OUTER JOIN "vevok" on "arajanl_f"."vevo_kod" = "vevok"."vevo_kod"
//where
//("arajanl_f"."uzemi_kod" IS NOT NULL
//AND (SELECT COUNT(*) from "uzemi" LEFT OUTER JOIN "uzemi_jog" on "uzemi"."ter_kod" = "uzemi_jog"."ter_kod"  where "uzemi_jog"."jel_kod" = $userID AND "uzemi"."uzemi_kod"  = "arajanl_f"."uzemi_kod" AND "uzemi_jog"."uzemi_j" > 1  )   > 0)
//        """
//        }

    }
}
