package com.example.gerenda.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StartMenu(goToCutting = {
                gotoCutting()
            }, goToProcess = {
                gotoProcess()
            })
        }
    }

    fun gotoCutting() {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    fun gotoProcess() {
        val intent = Intent(this,ProductionTrackingActivity::class.java)
        startActivity(intent)
    }
}
@Composable
fun StartMenu(goToCutting : ()->Unit,goToProcess : ()->Unit){
    Column() {
        Text(text = "készlet", modifier = Modifier.clickable {goToCutting()  })
        Text(text = "gyártáskövetés", modifier = Modifier.clickable {goToProcess()  })
    }
}
@Preview
@Composable
fun StartMenuPreview(){
    StartMenu(goToCutting = {}, goToProcess = {})
}