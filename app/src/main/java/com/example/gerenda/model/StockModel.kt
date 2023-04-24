package com.example.gerenda.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.gerenda.database.DatabaseTransformable
import com.example.gerenda.types.StockType
import java.io.Serializable
import java.sql.ResultSet

data class StockModel (
     val id:Int,
    private val type: StockType,
    private val height: Int,
    private val width : Int) :BaseObservable(),Serializable {


    val crossSection : String
    @Bindable get() = "$height x $width cm"

    val title : String
    @Bindable get() = "${type.name} $crossSection"

    companion object:DatabaseTransformable<StockModel>
     {
         override fun  TransformData(set: ResultSet):StockModel{
             return StockModel(set.getInt("tc2_kod"),StockType.getTypeFromDatabase(set.getString("tc2_bshkvh")),set.getInt("tc2_mer2"),set.getInt("tc2_mer3"))

         }

    }


}