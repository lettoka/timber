package com.example.gerenda.model

import com.example.gerenda.database.DatabaseTransformable
import java.sql.ResultSet


//SELECT SUM("uzemi"."uzemi_menny") AS "menny","uzemi"."ter_kod"
//,"termek"."ter_nev"
//from "uzemi"
//LEFT OUTER JOIN "termek" on "uzemi"."ter_kod" = "termek"."ter_kod"
//where "uzemi"."uzemi_kod" = 1   group by "uzemi"."ter_kod" ,"termek"."ter_nev"
data class TrackingItem(
    val processID : String,
    val processName : String,
    val amount : Double
){
  companion object : DatabaseTransformable<TrackingItem>{
      override fun TransformData(set: ResultSet): TrackingItem {
          return TrackingItem(set.getString("ter_kod"),set.getString("ter_nev"),set.getDouble("menny"))
      }

      fun getQuery(trackingID : Int) : String{
         return """
      
SELECT SUM("uzemi"."uzemi_menny") AS "menny","uzemi"."ter_kod","termek"."ter_nev"
from "uzemi"
LEFT OUTER JOIN "termek" on "uzemi"."ter_kod" = "termek"."ter_kod"
where "uzemi"."uzemi_kod" = $trackingID   group by "uzemi"."ter_kod" ,"termek"."ter_nev"
          """
      }
  }
}