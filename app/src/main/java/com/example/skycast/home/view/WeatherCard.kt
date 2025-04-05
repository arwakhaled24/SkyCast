package com.example.skycast.home.view

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WeatherSubCard(title: String, value: String) {
    OutlinedCard(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(4.dp)
            .size(120.dp, 100.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Log.i("TAG", "WeatherSubCard: $value")
            Text(text = value, fontSize = 14.sp, color = Color.White)
        }
    }
}