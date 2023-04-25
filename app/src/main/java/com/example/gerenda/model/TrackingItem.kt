package com.example.gerenda.model

import com.example.gerenda.database.DatabaseTransformable
import java.sql.ResultSet


//SELECT SUM("uzemi"."uzemi_menny") AS "menny","uzemi"."ter_kod"
//,"termek"."ter_nev"
//from "uzemi"
//LEFT OUTER JOIN "termek" on "uzemi"."ter_kod" = "termek"."ter_kod"
//where "uzemi"."uzemi_kod" = 1   group by "uzemi"."ter_kod" ,"termek"."ter_nev"
data class TrackingItem(
    val processID : Int,
    val processName : String,
    val amount : Double,
    val producID : Int,
    val productName : String
){
  companion object : DatabaseTransformable<TrackingItem>{
      override fun TransformData(set: ResultSet): TrackingItem {
          return TrackingItem(set.getInt("folyamat_ter_kod"),set.getString("folyamat_nev"),set.getDouble("uzemi_menny"),set.getInt("ter_kod"),set.getString("anyag_nev"))
      }

      fun getQuery(orderID : String) : String{
//         return """
//
//SELECT "uzemi"."uzemi_menny","uzemi"."ter_kod","termek"."ter_nev"
//from "uzemi"
//LEFT OUTER JOIN "termek" on "uzemi"."ter_kod" = "termek"."ter_kod"
//where "uzemi"."uzemi_kod" = $trackingID
//          """
          return """
              SELECT "arajanl_f"."ara_szam","uzemi"."uzemi_menny","uzemi"."ter_kod" "folyamat_ter_kod","folyamat"."ter_nev" "folyamat_nev","arajanl_t"."ter_kod","anyag"."ter_nev" "anyag_nev","uzemi"."uzemi_kesz"
from "arajanl_f"
LEFT OUTER JOIN "uzemi" on "arajanl_f"."uzemi_kod" = "uzemi"."uzemi_kod"
LEFT OUTER JOIN "termek" "folyamat" on "uzemi"."ter_kod" = "folyamat"."ter_kod"
LEFT OUTER JOIN "arajanl_t" on ("arajanl_f"."ara_szam" = "arajanl_t"."ara_szam" AND "uzemi"."uzemi_sorsz" = "arajanl_t"."uzemi_sorsz" )
LEFT OUTER JOIN "termek" as "anyag" on ("arajanl_t"."ter_kod" = "anyag"."ter_kod")
where    "arajanl_f"."ara_szam" = '$orderID'
          """
      }
  }
}