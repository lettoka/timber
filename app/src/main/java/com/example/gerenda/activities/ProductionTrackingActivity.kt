package com.example.gerenda.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gerenda.composable.TrackingOrderCell
import com.example.gerenda.viewmodel.ProductionTrackingViewModel

class ProductionTrackingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProductionTracking()
        }
    }
}

@Composable
fun ProductionTracking(viewModel : ProductionTrackingViewModel = viewModel()) {
    Box(modifier = Modifier
        .background(Color(0xFFF3DCCC))
        .fillMaxSize()) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp),modifier = Modifier.fillMaxSize()) {
       ProductTrackingHeader()
        Column(verticalArrangement = Arrangement.spacedBy(10.dp),modifier = Modifier.verticalScroll(rememberScrollState()).padding(horizontal = 10.dp)) {
            viewModel.orders.value.forEach{
                TrackingOrderCell(order = it)
            }
        }

    }
        if (viewModel.isLoading.value){
        CircularProgressIndicator()
        }
    }
}

@Composable
fun ProductTrackingHeader(viewModel : ProductionTrackingViewModel = viewModel()){
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
            .background(Color.White)
            .fillMaxWidth()
            .padding(20.dp)) {
        viewModel.user.value?.let { user ->
            Row(horizontalArrangement = Arrangement.SpaceBetween,modifier = Modifier.fillMaxWidth()) {
                Icon(
                    Icons.Rounded.AccountCircle,

                    contentDescription = "user",
                    modifier = Modifier.size(40.dp)
                )

                Text(text = user.displayName,fontSize = 20.sp)


                    Icon(
                        Icons.Rounded.Close,
                        tint = Color.Red,
                        contentDescription = "user",
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { viewModel.logout() }
                    )



            }
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp),modifier = Modifier.verticalScroll(rememberScrollState())) {
                user.roles.forEach {
                    Text(text = it.name)
                }
            }

        }
        if (viewModel.user.value == null) {
            Text(text = "Jelentkezzen be a folytatáshoz", fontSize = 20.sp)
            Button(onClick = { viewModel.reloadUser() }, modifier = Modifier.padding(top = 10.dp)) {
                Text("Bejelentkezés")
            }
        }
    }
}

@Preview
@Composable
fun ProductionTrackingPreview(){
    ProductionTracking()
}