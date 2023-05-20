package com.example.gerenda.composable

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gerenda.extension.dpToSp
import com.example.gerenda.model.ProcessTracking.ProcessTask
import com.example.gerenda.viewmodel.ProductionTrackingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginCard(viewModel : ProductionTrackingViewModel = viewModel()){
    val username = remember{ mutableStateOf("") }
    val password = remember{ mutableStateOf("") }

    Column(horizontalAlignment = Alignment.CenterHorizontally,verticalArrangement = Arrangement.spacedBy(20.dp),modifier = Modifier
        .padding(horizontal = 16.dp)
        .clip(RoundedCornerShape(20.dp))
        .background(Color.White)
        .fillMaxWidth()
        .padding(20.dp)) {
        Text("Bejelentkezés", fontSize = dpToSp(dp = 40.dp))
        TextField(value = username.value, onValueChange = {username.value = it},label = { Text(text = "Felhasználónév") })
        TextField(value = password.value, onValueChange = {password.value = it},label = { Text(text = "Jelszó") })
        
        TimberButton(text = "Bejelentkezés") {
            viewModel.login(username = username.value,password=password.value)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordCard(viewModel : ProductionTrackingViewModel = viewModel(),onGotPassword : (String)->Unit){
    val password = remember{ mutableStateOf("") }
    BackHandler() {
        viewModel.processPassword(null)
    }
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray.copy(alpha = 0.1f))
            .clickable {
                viewModel.processPassword(null)//dissmiss the password prompt
            }) {


    Column(horizontalAlignment = Alignment.CenterHorizontally,verticalArrangement = Arrangement.spacedBy(20.dp),modifier = Modifier
        .padding(horizontal = 16.dp)
        .clip(RoundedCornerShape(20.dp))
        .background(Color.White)
        .fillMaxWidth()
        .padding(20.dp)) {
        Text("Adja meg a jelszavát", fontSize = dpToSp(dp = 40.dp))
        TextField(value = password.value, onValueChange = {password.value = it},label = { Text(text = "Jelszó") })

        TimberButton(text = "Kész") {
            viewModel.processPassword(password.value)
        }
    }
    }
}