package com.example.gerenda.activities

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BaseObservable
import androidx.lifecycle.lifecycleScope
import com.example.gerenda.database.DatabaseCredentials
import com.example.gerenda.database.KotlinDatabase
import com.example.gerenda.model.LoadingModel
import com.example.gerenda.view.CredentialsDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class DatabaseActivity : AppCompatActivity() {

    /**
     *Variable for only one query at the same time
     */
    var isQueryInProgress=false
    set(value) {
        field=value
        loadingIndicator.loading=value
    }

    val loadingIndicator=LoadingModel()

    abstract fun onDBConnSuccess()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DatabaseCredentials.init(applicationContext)
        CredentialsDialog.activity=this
    }

    override fun onResume() {
        super.onResume()
        CredentialsDialog.activity=this
    }




    fun showCredWindow(){
        CredentialsDialog.showb()
    }
    fun testDatabaseReachable(showLoading:Boolean=true){
        if(showLoading)
            1+1//TODO
        lifecycleScope.launch(Dispatchers.IO)  {
            val reachable=KotlinDatabase.DatabaseIsReachable()
            //TODO hide loading
            if(reachable)
                onDBConnSuccess()
            /*else
                showCredWindow()*/
        }
    }
}