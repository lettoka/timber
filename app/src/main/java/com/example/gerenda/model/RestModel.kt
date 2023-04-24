package com.example.gerenda.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.example.gerenda.database.DatabaseTransformable
import java.sql.ResultSet

class RestModel(val groupId:Int,val ordNumber:Int,val name:String,
                       val calculatedRest:Int,var trueRest:Int,brokn:Boolean) :BaseObservable(){
    var broken:Boolean=brokn
    @Bindable get
    set(value) {field=value;notifyPropertyChanged(BR.broken)}

    var trueRestString:String
    @Bindable get() = trueRest.toString()
    set(value) {
        val intVal= value.toIntOrNull()?:0
        if(intVal==trueRest)return
        trueRest=intVal
        notifyPropertyChanged(BR.trueRestString)
    }

    companion object:DatabaseTransformable<RestModel>{
        override fun  TransformData(set: ResultSet): RestModel {
            return RestModel(set.getInt("rager_sz"),set.getInt("rag0_sorsz"),set.getString("nev"),
            set.getInt("rag0_marad"),set.getInt("rag0_marad2"), getIsBroken(set))
        }
        fun getIsBroken(set: ResultSet):Boolean{
            return set.getString("rag0_serult2") == "I"

        }
        fun getQuery(orderPartModel: OrderPartModel):String{
            return """SELECT "rager_0"."rager_sz","rag0_sorsz","rag0_marad","rag0_marad2","rag0_serult2",
                 ("tercs_2"."tc2_nev" || ' ' ||  "rager_0"."rag0_hossz" || 'M') as nev
                FROM "rager_0" LEFT JOIN "rager_f" ON "rager_f"."rager_sz"="rager_0"."rager_sz"
                LEFT JOIN "tercs_2" ON "rager_f"."tc2_kod"="tercs_2"."tc2_kod"
                WHERE "rag0_marad">0 AND "rager_0"."rager_sz"=${orderPartModel.id} 
            """
        }

    }
}