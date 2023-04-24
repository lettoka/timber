package com.example.gerenda.model

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.example.gerenda.database.DatabaseTransformable
import java.sql.ResultSet

class StockDetailModel(val length:Int,val stock:Int,val reserved:Int,val broken:Boolean,val recycled:Boolean) {

    val stateString:SpannableString
    get() {
        var str=""
        if(broken)str+=" "
        if (recycled)str+=""
        val spannable=SpannableString(str)
        if(broken){
            spannable.setSpan(ForegroundColorSpan(Color.RED),
            0,1,Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        }
        if(recycled){
            val start=if(broken)2 else 0
            spannable.setSpan(ForegroundColorSpan(Color.parseColor("#7E9E4B")),
            start,start+1,Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        }
        return spannable
    }

    companion object: DatabaseTransformable<StockDetailModel> {
        override fun  TransformData(set: ResultSet): StockDetailModel {
            return StockDetailModel(set.getInt("ter_mer3"),
                set.getInt("jo_keszlet"),set.getInt("jo_foglal")?:0,
                boolFromStr(set.getString("ter_serult")),boolFromStr(set.getString("jo_maradek")))

        }
        fun boolFromStr(str:String?="N"):Boolean{
            return str!="N"
        }
    }

}