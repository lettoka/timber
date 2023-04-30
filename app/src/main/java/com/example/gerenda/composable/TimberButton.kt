package com.example.gerenda.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.gerenda.extension.TimberBrown
import com.example.gerenda.extension.dpToSp

@Composable
fun TimberMenuButton(text:String, action : ()->Unit){
    Box( contentAlignment = Alignment.Center,modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .clip(RoundedCornerShape(15.dp))
        .background(Color(0xFFB88E71))
        .clickable { action() }) {
        Text(text = text, fontSize = dpToSp(
            dp = 40.dp
        ), color = Color.White
        )
    }

}

@Composable
fun TimberButton(text:String,fontSize : TextUnit = dpToSp(dp = 40.dp), action : ()->Unit){
    Box( contentAlignment = Alignment.Center,modifier = Modifier

        .clip(RoundedCornerShape(15.dp))

        .background(Color.TimberBrown)
        .padding(all = 10.dp)
        .clickable { action() }) {
        Text(text = text, fontSize = fontSize, color = Color.White
        )
    }

}