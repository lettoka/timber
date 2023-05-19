package com.example.gerenda.model

import com.example.gerenda.database.DatabaseTransformable
import java.sql.ResultSet

//termek tablabol lekert uzemi folyamat
data class ProductProcess (
    val id:Int,
    val name : String
        ){
    companion object: DatabaseTransformable<ProductProcess> {
        override fun TransformData(set: ResultSet): ProductProcess {
            return ProductProcess(
                set.getInt("ter_kod"),
                set.getString("ter_nev"),
            )

        }

        fun getListQuery(): String {
            return """
                 select "termek"."ter_kod","termek"."ter_nev"
FROM "termek" where "ter_uzemi" = 'I'
            """
        }
    }
}