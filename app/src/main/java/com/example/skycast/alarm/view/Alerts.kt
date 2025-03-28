package com.example.skycast.alarm.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skycast.R


@Composable
fun Alert() {
    OnSuccess()
}

@Composable
fun OnSuccess() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Red)
    ) {
        if (false) {
            Column(
                Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.empity_box_backgroundless),
                    contentDescription = "Empity List",
                    Modifier.size(300.dp)
                )

                Text(text = stringResource(R.string.no_favourite_location_yet), fontSize = 30.sp)

            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp, top = 40.dp, end = 10.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                /*    itemsIndexed(favLocations) { index, location ->
                        FavItem(location,onDelete)*/
                item(15) { Text("hiiiiiiiiii") }
            }
        }
    FloatingActionButton(
        onClick = { /* Handle click and open map */ },
        modifier = Modifier
            .padding(bottom = 120.dp, end = 35.dp)
            .align(Alignment.BottomEnd)
            .width(50.dp)
            .height(50.dp),
        shape = RoundedCornerShape(20.dp),
        containerColor = Color.White
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "FAB")
    }
}}