package com.example.gerenda.model.ProcessTracking

import com.example.gerenda.database.DatabaseTransformable
import java.sql.ResultSet

//uzemi_teerul
data class ProcessGroup(
    val id : Int,
    val name : String,
    val processNames : List<String>
){
    companion object : DatabaseTransformable<ProcessGroup>{
        override fun TransformData(set: ResultSet): ProcessGroup {
            return ProcessGroup(set.getInt("uzemt_kod"),set.getString("uzemt_nev"),
                getProcessNames(set)
            )
        }

        fun getProcessNames(set: ResultSet):List<String>{
            val fullString = set.getString("uzemilist")
            return  kotlin.runCatching {  fullString.split(";")}.getOrNull() ?: listOf()
        }

        fun getListQuery() : String{
            return """
select "uzemi_terul"."uzemt_kod","uzemt_nev", LIST("termek"."ter_asz",';') "uzemilist" from "uzemi_terul"
LEFT OUTER JOIN "termek" on "uzemi_terul"."uzemt_kod" = "termek"."uzemt_kod"  
  GROUP BY "uzemi_terul"."uzemt_kod","uzemi_terul"."uzemt_nev"    
            """
            //HAVING  LIST("termek"."ter_asz") IS NOT NULL
        }

    }
}
