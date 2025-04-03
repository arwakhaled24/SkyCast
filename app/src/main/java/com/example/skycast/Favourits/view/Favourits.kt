package com.example.skycast.Favourits.view

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skycast.Favourits.viewModel.FavouritsViewModel
import com.example.skycast.R
import com.example.skycast.data.RespondStatus
import com.example.skycast.data.dataClasses.LocationDataClass
import com.example.skycast.home.view.Home
import com.example.skycast.home.viewModel.WeatherViewModel
import com.example.skycast.ui.theme.BluePeriwinkle
import com.example.skycast.ui.theme.CloudWhite
import com.example.skycast.ui.theme.PrimaryContainer
import com.example.skycast.ui.theme.SilverGray
import com.example.skycast.utils.NetworkConnectivityObserver
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Favourits(
    favViewModel: FavouritsViewModel,
    weatherViewModel: WeatherViewModel,
) {
    favViewModel.getAllFav()
    val showMap = remember { mutableStateOf(false) }
    val showDetails = remember { mutableStateOf(false) }
    val favLocations = favViewModel.favouritLocationList.collectAsState().value
    val selectedLocation = remember { mutableStateOf<LocationDataClass?>(null) }
    var context = LocalContext.current
    var connectivityObserver = NetworkConnectivityObserver(context)
    var isConnected = connectivityObserver.observe().collectAsState(initial = true)


    Box(modifier = Modifier.fillMaxSize()) {
        if (showMap.value) {
            Map(
                favViewModel = favViewModel,
                onLocationAdded = { showMap.value = false }
            )
        } else if (showDetails.value && selectedLocation.value != null) {
            selectedLocation.value?.let {
                Home(
                    it.latitude,
                    it.longitude,
                    weatherViewModel,
                )
            }
        } else {
            when (favLocations) {
                is RespondStatus.Loading -> OnLoading()
                is RespondStatus.Error -> OnError(favLocations.error)
                is RespondStatus.Success -> OnSuccess(

                    locations = favLocations.result,
                    favViewModel = favViewModel,
                    onFabClick = {
                        if (isConnected.value) {
                            showMap.value = true
                        }},
                    onItemClick = { location ->
                        if (isConnected.value){
                            selectedLocation.value = location
                            weatherViewModel.getCurrentWeather(
                                lat = location.latitude,
                                lon = location.longitude
                            )
                            showDetails.value = true
                        }
                    },
                    isConnected = isConnected.value
                )
            }
        }
    }
}




