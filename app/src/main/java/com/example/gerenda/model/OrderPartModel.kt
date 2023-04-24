package com.example.gerenda.model

import com.example.gerenda.database.DatabaseTransformable
import java.io.Serializable
import java.sql.ResultSet

class OrderPartModel(val id:Int,val type:String,val cutDone:Boolean,val recycleDone:Boolean):Serializable {

    companion object:DatabaseTransformable<OrderPartModel>{

        override fun  TransformData(set: ResultSet): OrderPartModel {
            return OrderPartModel(set.getInt("rager_sz"),set.getString("tc2_nev"), getCutDone(set),
                getRecycleDone(set))
        }
        fun getCutDone(set:ResultSet):Boolean{
            return (set.getString("rager_jel_vagva")=="I" || set.getString("rager_jel_vagva")=="K")
        }
        fun getRecycleDone(set:ResultSet):Boolean{
            return (set.getString("rager_jel_marad")=="I" || set.getString("rager_jel_marad")=="K")
        }

        fun getQuery(orderModel: OrderModel):String{
            return  """select "rager_sz","tc2_nev","rager_jel_vagva","rager_jel_marad" FROM 
            "rager_f" INNER JOIN "tercs_2" ON "tercs_2"."tc2_kod"="rager_f"."tc2_kod" where "rager_0"=${orderModel.orderId}                
            """
        }
    }
}