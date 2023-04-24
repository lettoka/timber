package com.example.gerenda.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.example.gerenda.R
import com.example.gerenda.activities.DatabaseActivity
import com.example.gerenda.database.DatabaseCredentials
import com.example.gerenda.database.KotlinDatabase
import com.example.gerenda.databinding.CredentialsPopupBinding
import kotlinx.android.synthetic.main.credentials_popup.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object CredentialsDialog {

    lateinit var activity:DatabaseActivity


    fun showb() {
        activity.runOnUiThread {
            val shake = AnimationUtils.loadAnimation(activity, R.anim.shake);
        val popup = AlertDialog.Builder(activity)
        val binding = CredentialsPopupBinding.inflate(activity.layoutInflater)
        binding.creds=DatabaseCredentials
        popup.setView(binding.root)
        popup.setCancelable(false)
        val finalpopup = popup.create();
        finalpopup.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        binding.okButton.setOnClickListener {
            //binding.okButton.isEnabled=false
            finalpopup.dismiss()
            activity.testDatabaseReachable()
            /*activity.lifecycleScope.launch(Dispatchers.IO)  {
            if(KotlinDatabase.DatabaseIsReachable()) {
                finalpopup.dismiss()
                activity.onDBConnSuccess()
            }
            else
                binding.okButton.isEnabled=true

            }*/

        }
        finalpopup.show()
            binding.root.startAnimation(shake)
    }
    }
}