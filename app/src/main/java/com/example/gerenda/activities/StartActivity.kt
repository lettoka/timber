package com.example.gerenda.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gerenda.composable.TimberMenuButton

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
    Column(verticalArrangement = Arrangement.SpaceAround, modifier = Modifier
        .background(Color.White)
        .fillMaxSize()
        .padding(horizontal = 16.dp)) {

        TimberMenuButton("Szabászat"){goToCutting()}
        TimberMenuButton(text = "Gyártás követés") { goToProcess() }

    }
}
@Preview
@Composable
fun StartMenuPreview(){
    StartMenu(goToCutting = {}, goToProcess = {})
}