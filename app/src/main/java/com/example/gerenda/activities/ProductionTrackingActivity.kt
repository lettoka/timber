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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gerenda.composable.LoginCard
import com.example.gerenda.composable.PasswordCard
import com.example.gerenda.composable.TimberButton
import com.example.gerenda.composable.TrackingOrderCell
import com.example.gerenda.extension.TimberBrown
import com.example.gerenda.extension.dpToSp
import com.example.gerenda.viewmodel.ProductionTrackingViewModel

class ProductionTrackingActivity : DatabaseActivity() {
    override fun onDBConnSuccess() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProductionTracking()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductionTracking(viewModel : ProductionTrackingViewModel = viewModel()) {
    val pullRefreshState =
        rememberPullRefreshState(viewModel.isPullRefreshing, { viewModel.pullRefresh() })

    Box(contentAlignment = Alignment.Center,modifier = Modifier

        .fillMaxSize()
        .background(Color(0xFFF3DCCC))
        .pullRefresh(pullRefreshState)
    ) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp),modifier = Modifier.fillMaxSize()) {
       ProductTrackingHeader()

        Column(verticalArrangement = Arrangement.spacedBy(10.dp),modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 10.dp)) {
            viewModel.orders.value.forEach{
                TrackingOrderCell(order = it)
            }
        }

    }
        if (viewModel.loginShown.value){
            LoginCard()

        }

        if (viewModel.orders.value.isEmpty() && viewModel.selectedProcessGroup.value != null && !viewModel.isPullRefreshing){
            Column(verticalArrangement = Arrangement.spacedBy(30.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Nincs elérhető megrendelés.", fontSize = dpToSp(dp = 30.dp))
                TimberButton(text = "Frissítés") {
                    viewModel.pullRefresh()
                }
            }


        }

        viewModel.onGotPassword?.let {
            PasswordCard()
        }

        PullRefreshIndicator(viewModel.isPullRefreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))
        if (viewModel.isPullRefreshing){
            Box(contentAlignment = Alignment.Center, modifier = Modifier
                .background(Color.White.copy(alpha = 0.2f))
                .fillMaxSize()) {
                CircularProgressIndicator()
            }

        }
    }
}

@Composable
fun ProductTrackingHeader(viewModel : ProductionTrackingViewModel = viewModel()){
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
            .background(Color.White)
            .fillMaxWidth()
            .padding(20.dp)) {
        Text("Gyártáskövetés",fontSize = dpToSp(dp = 30.dp), fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.SpaceBetween,modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {
                viewModel.dropDownExpanded.value = true;viewModel.loadProcessGroups()
            })
            .background(
                Color.TimberBrown.copy(alpha = 0.2f),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(all = 10.dp)
        ){
            Text(viewModel.selectedProcessGroup.value?.name ?: "Válasszon üzemi területet",fontSize = dpToSp(dp = 30.dp))
                                Icon(
                        Icons.Filled.ArrowDropDown,
                        tint = Color.Black,
                        contentDescription = "user",
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { viewModel.logout() }
                    )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(20.dp),modifier = Modifier.verticalScroll(rememberScrollState())) {
            viewModel.selectedProcessGroup.value?.processNames?.forEach {
                Text(text = it, fontSize = dpToSp(dp = 26.dp))
            }
        }

        DropdownMenu(
            expanded = viewModel.dropDownExpanded.value,
            onDismissRequest = { viewModel.dropDownExpanded.value = false;viewModel.processGroups.value = listOf() },
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.White
                )
                .shadow(10.dp)
                .padding(all = 10.dp)
                .clip(RoundedCornerShape(10.dp))

        ) {
            if (viewModel.isDropDownRefreshing){
                Box( contentAlignment = Alignment.Center,modifier = Modifier
                    .background(Color.White)
                    .height(80.dp)
                    .fillMaxWidth()
                    .padding(all = 20.dp)){
                    CircularProgressIndicator()
                }
            }
            viewModel.processGroups.value.forEach { processGroup ->
                DropdownMenuItem(onClick = {
                    viewModel.selectProcess(processGroup)
                }, text = {

                    Text(text = processGroup.name,fontSize = dpToSp(dp = 30.dp))
                })
            }
        }


//        viewModel.user.value?.let { user ->
//            Row(horizontalArrangement = Arrangement.SpaceBetween,modifier = Modifier.fillMaxWidth()) {
//                Icon(
//                    Icons.Rounded.AccountCircle,
//
//                    contentDescription = "user",
//                    modifier = Modifier.size(40.dp)
//                )
//
//                Text(text = user.displayName,fontSize = dpToSp(dp = 30.dp))
//
//
//                    Icon(
//                        Icons.Rounded.Close,
//                        tint = Color.Red,
//                        contentDescription = "user",
//                        modifier = Modifier
//                            .size(40.dp)
//                            .clickable { viewModel.logout() }
//                    )
//
//
//
//            }
//            Row(horizontalArrangement = Arrangement.spacedBy(20.dp),modifier = Modifier.verticalScroll(rememberScrollState())) {
//                user.roles.forEach {
//                    Text(text = it.name, fontSize = dpToSp(dp = 26.dp))
//                }
//            }
//
//        }
//        if (viewModel.user.value == null) {
//            Text(text = "Jelentkezzen be a folytatáshoz", fontSize = 20.sp)
//            Button(onClick = { viewModel.loginShown.value = true }, modifier = Modifier.padding(top = 10.dp)) {
//                Text("Bejelentkezés")
//            }
//        }
    }
}




@Preview
@Composable
fun ProductionTrackingPreview(){
    ProductionTracking()
}