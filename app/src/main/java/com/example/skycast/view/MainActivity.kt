package com.example.skycast.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.skycast.data.LocalData.WeatherLocalDataSourse
import com.example.skycast.data.remoteData.WearherRemoreDataSourse
import com.example.skycast.data.repository.WeatherRepository
import com.example.skycast.ui.theme.SkyCastTheme
import com.example.skycast.viewModel.CurrentWeatherViewModel
import com.example.skycast.viewModel.MyFactory

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        var myFactory = MyFactory(
            WeatherRepository(
                WearherRemoreDataSourse(),
                WeatherLocalDataSourse()
            )
        )
        var currentViewModel =
            ViewModelProvider(this, myFactory).get(CurrentWeatherViewModel::class.java)
        setContent {
            SkyCastTheme {
            }
            initial(currentViewModel)
        }
    }
}


@Composable
private fun initial(viewModel: CurrentWeatherViewModel) {
    viewModel.getCurrentWeather("44", "44")

    val currentWeather = viewModel.currentWeather.observeAsState()
    val msg = viewModel.message.observeAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar= {

        }

    ) { pading ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(pading)
        ) {
            Text(currentWeather.value?.base ?: "")
        }
    }


}