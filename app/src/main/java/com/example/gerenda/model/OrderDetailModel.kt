package com.example.gerenda.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.gerenda.BR
import com.example.gerenda.database.DatabaseTransformable
import java.sql.ResultSet


/**
 *rager_0-nak megfelelő rekordok
 */
class OrderDetailModel(val groupId:Int,val ordNumber:Int,val name:String,
                       val cut:String,val rest:Int,val waste:Int,isDone:Boolean,val broken:Boolean):BaseObservable() {

    var done:Boolean=isDone
    @Bindable get
    set(value) {
        if(field==value)return
        field=value;notifyPropertyChanged(BR.done)
    }



    companion object: DatabaseTransformable<OrderDetailModel> {
        override fun  TransformData(set: ResultSet): OrderDetailModel {
            return OrderDetailModel(set.getInt("rager_sz"),set.getInt("rag0_sorsz"),
                getName(set),set.getString("vagasok"),
                set.getInt("marad"),set.getInt("hull"), getIsDone(set), getIsBroken(set))

        }
        private fun getName(set:ResultSet):String{
            return "${set.getInt("meret1")} x ${set.getInt("meret2")}  ${set.getInt("hossz")} cm"
        }
        private fun getIsDone(set: ResultSet):Boolean{
            val c=set.getString("vagva")
            if (c=="K" || c=="I")return true
            return false
        }
        private fun getIsBroken(set:ResultSet):Boolean{
            return set.getString("ter_serult") == "I"
        }
        fun getQuery(orderPart:OrderPartModel):String{
            /*return """select
"tercs_2"."tc2_mer2" as "meret1",
"tercs_2"."tc2_mer3" as "meret2",
"rager_0"."rag0_hossz" as "hossz",
"rager_0"."rag0_marad" as "marad",
"rager_0"."rag0_hull" as "hull",
"rager_0"."rag0_rvagva" as "vagva",
"rager_1"."rager_sz",
"rager_1"."rag0_sorsz",
LIST("rager_1"."rag1_hossz",',') as "vagasok"
from "rager_1" LEFT JOIN "rager_0" ON ("rager_1"."rager_sz"="rager_0"."rager_sz" and "rager_1"."rag0_sorsz"="rager_0"."rag0_sorsz")
LEFT JOIN "rager_f" ON "rager_0"."rager_sz" = "rager_f"."rager_sz"
LEFT JOIN "tercs_2" ON "rager_f"."tc2_kod"="tercs_2"."tc2_kod"
where "rager_f"."rager_sz"=${orderPart.id}
group by "rager_1"."rager_sz","rager_1"."rag0_sorsz","hossz","marad","hull","vagva","meret1","meret2""""*/
            return """ select
"tercs_2"."tc2_mer2" as "meret1",
"tercs_2"."tc2_mer3" as "meret2",
"rager_0"."rag0_hossz" as "hossz",
"rager_0"."rag0_marad" as "marad",
"rager_0"."rag0_hull" as "hull",
"rager_0"."rag0_rvagva" as "vagva",
"rager_1"."rager_sz",
"rager_1"."rag0_sorsz",
"termek"."ter_serult",
LIST((CASE WHEN ("rager_1"."rag1_hossz" = "rager_1"."rag1_hossz0") THEN ("rager_1"."rag1_hossz") ELSE ("rager_1"."rag1_hossz"||'('||"rager_1"."rag1_hossz0"||')') END),', ') as vagasok
from
  "rager_1"
  LEFT JOIN "rager_0" ON ("rager_1"."rager_sz"="rager_0"."rager_sz" and "rager_1"."rag0_sorsz"="rager_0"."rag0_sorsz")
  LEFT JOIN "rager_f" ON "rager_0"."rager_sz" = "rager_f"."rager_sz"
  LEFT JOIN "tercs_2" ON "rager_f"."tc2_kod"="tercs_2"."tc2_kod"
  LEFT JOIN "termek" ON "rager_0"."ter_kod" = "termek"."ter_kod"
where
  "rager_f"."rager_sz"=${orderPart.id}
group by
  "rager_1"."rager_sz",
  "rager_1"."rag0_sorsz",
  "hossz",
  "marad",
  "hull",
  "vagva",
  "meret1",
  "meret2",
  "ter_serult"
ORDER BY
  "vagva" DESC, 
  "meret1" DESC,
  "meret2" DESC,
  "hossz" DESC """
        }
    }

}