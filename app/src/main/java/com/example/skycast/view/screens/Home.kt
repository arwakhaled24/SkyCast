package com.example.skycast.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Home(paddingValues: PaddingValues) {
    Column(Modifier
        .fillMaxSize()
        .padding(paddingValues)) {
        Text(text = "Home")


    }
}