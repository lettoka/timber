package com.example.gerenda.database

import android.content.Context
import android.content.SharedPreferences
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.gerenda.extension.isValidIp

object DatabaseCredentials:BaseObservable() {
    private const val PREF_KEY="GERENDA"
    private const val USERNAME_KEY="USERNAME"
    private const val PASSWORD_KEY="PASSWORD"
    private const val PATH_KEY="PATH"
    private const val IP_KEY="IP"
    private const val WAREHOUSE_KEY="WAREHOUSE"

    private lateinit var prefs:SharedPreferences

    var username:String
    // get() =  prefs.getString(USERNAME_KEY,"") ?: ""
    get() = "ANCSI"//"SYSDBA"/"ANCSI"
    set(value) {
        with(prefs.edit()){
            putString(USERNAME_KEY,value)
            apply()
        }
    }
    var password:String
    //get() =  prefs.getString(PASSWORD_KEY,"") ?: ""
    get() =  "afaf"//"masterkey"/"afaf"
    set(value) {
        with(prefs.edit()){
            putString(PASSWORD_KEY,value)
            apply()
        }
    }
    var path:String
    @Bindable get() =  prefs.getString(PATH_KEY,"") ?: ""
    set(value) {
        with(prefs.edit()){
            putString(PATH_KEY,value)
            apply()
        }
    }
    var ip:String
    @Bindable get() =  prefs.getString(IP_KEY,"") ?: ""
    set(value) {
        if(!value.isValidIp())
            return
        with(prefs.edit()){
            putString(IP_KEY,value)
            apply()
        }
    }
    var warehouse:String
    @Bindable get() =  prefs.getString(WAREHOUSE_KEY,"") ?: ""
    set(value) {
        with(prefs.edit()){
            putString(WAREHOUSE_KEY,value)
            apply()
        }
    }

    val databaseurl:String
    get() = "jdbc:firebirdsql://$ip/$path\\DATA.GDB?encoding=ISO8859_1"




    fun init(context: Context){
        if (::prefs.isInitialized)
            return
        prefs=context.getSharedPreferences(PREF_KEY,Context.MODE_PRIVATE)
    }
}