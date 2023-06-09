package com.example.gerenda.composable

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ArrowRight
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gerenda.extension.dpToSp
import com.example.gerenda.model.ProcessTracking.ProductionTrackingOrder
import com.example.gerenda.model.ProcessTracking.TrackingItem
import com.example.gerenda.viewmodel.LoadingState
import com.example.gerenda.viewmodel.ProductionTrackingViewModel
import com.example.gerenda.viewmodel.TrackingOrderViewModel

@Composable
fun TrackingOrderCell(
    order: ProductionTrackingOrder,
    viewModel: TrackingOrderViewModel = viewModel(),
    pageViewModel: ProductionTrackingViewModel = viewModel()
) {
    val isOpen = remember { mutableStateOf(false) }
    val items = remember { mutableStateOf<List<TrackingItem>>(listOf()) }
    val loadingState = remember { mutableStateOf(LoadingState.IDLE) }
    val arrowRotation = animateFloatAsState(targetValue = if (isOpen.value) 90f else 0f)
    val context = LocalContext.current

    fun expandClicked() {
        if (loadingState.value == LoadingState.LOADING) {
            return
        }
        if (isOpen.value) {
            loadingState.value = LoadingState.IDLE
            items.value = listOf()
        } else {
            loadingState.value = LoadingState.LOADING
//            pageViewModel.user.value?.id?.let {userID ->
//                viewModel.loadItems(order.id,userID, onError = {
//                    items.value = listOf()
//                    isOpen.value = false
//                    loadingState.value = LoadingState.IDLE
//                }, onSuccess = {
//                    items.value = it
//                    loadingState.value = LoadingState.LOADED
//                })
//            }
            pageViewModel.selectedProcessGroup.value?.let { processGroup ->
                viewModel.loadItemsForProcessGroup(
                    order.id,
                    processGroupID = processGroup.id,
                    onError = {
                        items.value = listOf()
                        isOpen.value = false
                        loadingState.value = LoadingState.IDLE
                    },
                    onSuccess = {
                        items.value = it
                        loadingState.value = LoadingState.IDLE
                    })
            }
        }
        isOpen.value = !isOpen.value
    }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expandClicked() }
        ) {
            Text(order.id, fontSize = 36.sp, fontWeight = FontWeight.Bold)
            Text(text = order.ordererName, fontSize = dpToSp(dp = 20.dp))
            Icon(
                Icons.Rounded.ArrowRight,
                contentDescription = "user",
                modifier = Modifier
                    .size(40.dp)
                    .rotate(arrowRotation.value)

            )
        }
        AnimatedVisibility(
            visible = isOpen.value,
            enter = expandVertically(animationSpec = tween(1000)) + fadeIn(
                animationSpec = tween(
                    500
                )
            ),
            exit = shrinkVertically(animationSpec = tween(1000))

        ) {

                Column(verticalArrangement = Arrangement.Center) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {


                        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                            Icon(
                                Icons.Rounded.AccountCircle,

                                contentDescription = "user",
                                modifier = Modifier.size(30.dp)
                            )
                            Text(text = order.creatorName, fontSize = dpToSp(dp = 26.dp))

                        }


                        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                            Text(text = "Felelős:", fontSize = dpToSp(dp = 26.dp))
                            Text(text = order.supervisorName, fontSize = dpToSp(dp = 26.dp))

                        }
                    }
                    Box(contentAlignment = Alignment.Center) {
                    //items.value.groupingBy { it.processName }
                        Column() {


                            items.value.groupBy { it.processName }.forEach { group ->
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                ) {
                                    Text(
                                        text = group.key,
                                        fontSize = dpToSp(dp = 26.dp),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = String.format(
                                            "%.1f m³",
                                            group.value.sumOf { it.amount }),
                                        fontSize = dpToSp(
                                            dp = 20.dp
                                        )
                                    )
                                }


                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {


                                    group.value.forEach {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                //Text(text = it.processName)
                                                Text(it.productName, fontSize = dpToSp(dp = 20.dp))
                                                Text(
                                                    text = String.format("%.1f m³", it.amount),
                                                    fontSize = dpToSp(dp = 20.dp)
                                                )


                                            }
                                            DoneButton(
                                                isDone = it.isDone,
                                                onClick = { isUndoing, onDone ->
                                                    pageViewModel.askForPassword(isUndoing) { passwordResult ->
                                                        passwordResult?.let { password ->
                                                            viewModel.checkPassword(
                                                                isUndoing,
                                                                order.id,
                                                                password = password,
                                                                it,
                                                                onSuccess = { newItems ->
                                                                    items.value = newItems
                                                                    onDone()
                                                                },
                                                                onError = { errorMessage ->
                                                                    Toast.makeText(
                                                                        context,
                                                                        errorMessage,
                                                                        Toast.LENGTH_LONG
                                                                    ).show()
                                                                    onDone()
                                                                })
                                                            return@askForPassword
                                                        }
                                                        onDone()//the process was cancelled
                                                    }
                                                })
                                        }
                                    }
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


}

@Preview
@Composable
fun TrackingOrderCellPreview() {
    TrackingOrderCell(
        ProductionTrackingOrder(
            "tesztorder",
            1,
            "nev",
            "felhasznalo",
            "2022.11.12",
            "Felelosnev"
        )
    )
}


