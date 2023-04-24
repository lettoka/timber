package com.example.gerenda.types

enum class StockType(val title:String,val query:String) {
    BSH("BSH Készlet","SELECT \"tc2_kod\",\"tc2_mer2\",\"tc2_mer3\",\"tc2_bshkvh\" FROM \"tercs_2\" where \"tc2_bshkvh\"='B' ORDER BY \"tc2_mer2\",\"tc2_mer3\" "),
    KVH("KVH Készlet","SELECT \"tc2_kod\",\"tc2_mer2\",\"tc2_mer3\",\"tc2_bshkvh\" FROM \"tercs_2\" where \"tc2_bshkvh\"='K' ORDER BY \"tc2_mer2\",\"tc2_mer3\" "),
    KRH("KRH Készlet","SELECT \"tc2_kod\",\"tc2_mer2\",\"tc2_mer3\",\"tc2_bshkvh\" FROM \"tercs_2\" where \"tc2_bshkvh\"='R' ORDER BY \"tc2_mer2\",\"tc2_mer3\" ");
    companion object{
        fun getTypeFromDatabase(data:String): StockType {
             when (data) {
                 "B"-> return BSH
                 "K"-> return KVH
                 "R"-> return KRH
             }
        return BSH
    }
    }

}