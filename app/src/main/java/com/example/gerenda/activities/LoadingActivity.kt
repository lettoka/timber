package com.example.gerenda.activities

import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import com.example.gerenda.R
import kotlinx.android.synthetic.main.activity_loading.*

class LoadingActivity : DatabaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
    }

    override fun onResume() {
        super.onResume()
        loading_act.scaleX=0.1f;loading_act.scaleY=0.1f;loading_act.alpha=0.1f
        loading_act.animate().scaleX(1f).scaleY(1f).alpha(1f)
            .setInterpolator(AccelerateDecelerateInterpolator()).setDuration(600)
            .withEndAction {testDatabaseReachable(false)  }

    }

    override fun onDBConnSuccess() {
        gotoMain()
    }
    fun gotoMain() {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}