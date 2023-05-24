package com.example.gerenda.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.gerenda.extension.TimberBrown
import com.example.gerenda.extension.dpToSp

@Composable
fun DoneButton(isDone : Boolean,onClick:(undo:Boolean,onDone:()->Unit)->Unit){
    val isLoading = remember{ mutableStateOf(false) }


    Box {
        if (isLoading.value){
            CircularProgressIndicator()
        }else{
            if(isDone) {
                Icon(
                    Icons.Filled.CheckCircle,

                    contentDescription = "user",
                    modifier = Modifier.size(40.dp).clickable {
                        isLoading.value = true
                        onClick(true){isLoading.value = false}
                    },
                    tint = Color.TimberBrown
                )
            }else{
                TimberButton(text = "Kész", fontSize = dpToSp(dp = 20.dp)) {
                    isLoading.value = true
                    onClick(false){ isLoading.value = false }
                }
            }
        }

    }

}