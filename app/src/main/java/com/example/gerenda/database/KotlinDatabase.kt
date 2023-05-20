package com.example.gerenda.database

import android.util.Log
import com.example.gerenda.view.CredentialsDialog
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement
import java.util.*


object KotlinDatabase {


    private fun getConnection():Connection?{
        return try {
            //Class.forName("com.mysql.jdbc.Driver").newInstance()
            Class.forName ("org.firebirdsql.jdbc.FBDriver")
            DriverManager.setLoginTimeout(5)
            DriverManager.getConnection(DatabaseCredentials.databaseurl,DatabaseCredentials.username,DatabaseCredentials.password)
        }catch (e:Exception){
           // Log.w("DB CONNECTION ERROR: ",e.stackTrace)
               e.printStackTrace()
            CredentialsDialog.showb()
            null
        }
    }

     fun DatabaseIsReachable():Boolean{
        return getConnection()!=null
    }


    fun <T> executeRawQuery(dt: DatabaseTransformable<T>, q:String,mustReturnOneRow : Boolean = false,onError:((Exception)->Unit)? = null,onSuccess : ((List<T>)->Unit)) {
        val conn = getConnection()
        if (conn == null) { onError?.invoke(Exception("Sikertelen csatlakozás"));return}
        try {
            val st: Statement = conn.createStatement()
            Log.w("lekerdezes:",q)
            val rs = st.executeQuery(q)
            val list: MutableList<T> = ArrayList()
            while (rs.next()) {
               // println("lekerdezett:  ${rs.getString("name")}")
                list.add(dt.TransformData(rs))
            }
            if (mustReturnOneRow && list.isEmpty()){onError?.invoke(Exception("Üres lista")) }
            onSuccess(list)
        } catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
            onError?.invoke(ex)
        } catch (ex: Exception) {
            // handle any errors
            ex.printStackTrace()
            onError?.invoke(ex)
        }
        finally {
            conn.close()
        }
    }
    fun executeUpdate(query:String):Boolean{
        val conn = getConnection() ?: return false
        try {
            val st: Statement = conn.createStatement()
            val rs = st.executeUpdate(query)
            return true
        } catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
            return false
        } catch (ex: Exception) {
            // handle any errors
            ex.printStackTrace()
            return false
        }
        finally {
            conn.close()
        }
    }

}