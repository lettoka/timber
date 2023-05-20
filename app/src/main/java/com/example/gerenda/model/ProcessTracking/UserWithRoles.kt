package com.example.gerenda.model

import com.example.gerenda.database.DatabaseTransformable
import java.sql.ResultSet
import java.time.format.DateTimeFormatter


//select "termek"."ter_kod","termek"."ter_nev","jelszo"."jel_kod","jelszo"."felhaszn","jelszo"."nev","uzemi_jog"."uzemi_j" from "jelszo"
//LEFT OUTER JOIN "uzemi_jog" on "uzemi_jog"."jel_kod" = "jelszo"."jel_kod"
//LEFT JOIN "termek" ON ("ter_uzemi" = 'I' AND "uzemi_jog"."ter_kod" = "termek"."ter_kod")
//WHERE "jelszo"."felhaszn" = 'ANCSI'
data class UserRoleResponse(
    val id : Int,
    val name : String,
    val roleID : Int,
    val userID : Int,
    val username: String,
    val userDisplayName : String
){

        companion object: DatabaseTransformable<UserRoleResponse>
    {
        override fun  TransformData(set: ResultSet):UserRoleResponse{
            return UserRoleResponse(set.getInt("ter_kod"),set.getString("ter_nev"),set.getInt("uzemi_j"),set.getInt("jel_kod"),set.getString("felhaszn"),set.getString("nev"))

        }

        fun getQuery(username:String,password:String):String{
            return """
                 select "termek"."ter_kod","termek"."ter_nev","jelszo"."jel_kod","jelszo"."felhaszn","jelszo"."nev","uzemi_jog"."uzemi_j" from "jelszo"
LEFT OUTER JOIN "uzemi_jog" on "uzemi_jog"."jel_kod" = "jelszo"."jel_kod"
LEFT JOIN "termek" ON ("ter_uzemi" = 'I' AND "uzemi_jog"."ter_kod" = "termek"."ter_kod")
WHERE "jelszo"."felhaszn" = '$username' AND "jelszo"."jelszo_md5" = '$password' AND  "uzemi_jog"."uzemi_j" > 1
            """
        }


        fun getReloadQuery(userID: Int) : String{
            return """
                 select "termek"."ter_kod","termek"."ter_nev","jelszo"."jel_kod","jelszo"."felhaszn","jelszo"."nev","uzemi_jog"."uzemi_j" from "jelszo"
LEFT OUTER JOIN "uzemi_jog" on "uzemi_jog"."jel_kod" = "jelszo"."jel_kod"
LEFT JOIN "termek" ON ("ter_uzemi" = 'I' AND "uzemi_jog"."ter_kod" = "termek"."ter_kod")
WHERE "jelszo"."jel_kod" = $userID AND "uzemi_jog"."uzemi_j" > 1
            """
        }

    }
}

fun List<UserRoleResponse>.userData() : UserWithRoles?{
    this.firstOrNull()?.let {first ->
        return UserWithRoles(first.userID,first.username,first.userDisplayName,this.map { UserRole(it.id,it.name,it.roleID) })
    }
    return null
}

data class UserRole(
    val id : Int,
    val name : String,
    val roleID : Int
)

data class  User(
    val id : Int,
    val name : String,
    val displayName : String
) {
    companion object : DatabaseTransformable<User>{
        override fun TransformData(set: ResultSet): User {
            return User(set.getInt("jel_kod"),set.getString("felhaszn"),set.getString("nev"))
        }
        fun getUserQueryForpassword(password: String) :String{
            return """
                select "jelszo"."jel_kod","jelszo"."felhaszn","jelszo"."nev" from "jelszo" where "jelszo_md5" = '$password'
            """
        }



    }
}

data class UserWithRoles (
    val id : Int,
    val name : String,
    val displayName : String,
    val roles : List<UserRole>

        ){

}
//data class StockModel (
//    val id:Int,
//    private val type: StockType,
//    private val height: Int,
//    private val width : Int) :BaseObservable(),Serializable {
//
//
//    val crossSection : String
//        @Bindable get() = "$height x $width cm"
//
//    val title : String
//        @Bindable get() = "${type.name} $crossSection"
//
//    companion object:DatabaseTransformable<StockModel>
//    {
//        override fun  TransformData(set: ResultSet):StockModel{
//            return StockModel(set.getInt("tc2_kod"),StockType.getTypeFromDatabase(set.getString("tc2_bshkvh")),set.getInt("tc2_mer2"),set.getInt("tc2_mer3"))
//
//        }
//
//    }
//
//
//}