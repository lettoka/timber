package com.example.gerenda.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowRight
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gerenda.model.ProductionTrackingOrder
import com.example.gerenda.model.TrackingItem
import com.example.gerenda.viewmodel.LoadingState
import com.example.gerenda.viewmodel.TrackingOrderViewModel

@Composable
fun TrackingOrderCell(order : ProductionTrackingOrder,viewModel: TrackingOrderViewModel = androidx.lifecycle.viewmodel.compose.viewModel()){
    val isOpen = remember { mutableStateOf(false) }
    val items = remember{ mutableStateOf<List<TrackingItem>>(listOf()) }
    val loadingState = remember{ mutableStateOf(LoadingState.IDLE) }
    val arrowRotation = animateFloatAsState(targetValue = if (isOpen.value)90f else 0f)

    fun expandClicked(){
        if (loadingState.value == LoadingState.LOADING){ return}
        if (isOpen.value){
            loadingState.value = LoadingState.IDLE
            items.value = listOf()
        }else{
            loadingState.value = LoadingState.LOADING
            viewModel.loadItems(order.trackingID, onError = {
                items.value = listOf()
                isOpen.value = false
            }, onSuccess = {
                items.value = it
                loadingState.value = LoadingState.LOADED
            })
        }
        isOpen.value = !isOpen.value
    }

    Column(modifier = Modifier
        .clip(RoundedCornerShape(10.dp))
        .background(Color.White)
        .fillMaxWidth()
        .padding(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expandClicked() }
        ) {
            Text(order.id, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Icon(
                Icons.Rounded.ArrowRight,
                contentDescription = "user",
                modifier = Modifier
                    .size(40.dp)
                    .rotate(arrowRotation.value)

            )
        }
        AnimatedVisibility(visible = isOpen.value) {
            Box(contentAlignment = Alignment.Center) {
                Column() {
                    

                items.value.forEach{
                    Row(){
                        Text(text = it.processName)
                        Text(text = String.format("%.1f ",it.amount))
                    }
                }
                }
            if (loadingState.value == LoadingState.LOADING) {
                CircularProgressIndicator()
            }
            }
        }

    }


}

@Preview
@Composable
fun TrackingOrderCellPreview(){
    TrackingOrderCell(ProductionTrackingOrder("tesztorder",1))
}