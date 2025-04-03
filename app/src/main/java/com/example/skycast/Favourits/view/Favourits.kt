package com.example.skycast.Favourits.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.skycast.Favourits.viewModel.FavouritsViewModel
import com.example.skycast.R
import com.example.skycast.data.RespondStatus
import com.example.skycast.data.dataClasses.LocationDataClass
import com.example.skycast.home.view.Home
import com.example.skycast.home.viewModel.WeatherViewModel
import com.example.skycast.utils.NetworkConnectivityObserver


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
    val context = LocalContext.current
    val connectivityObserver = remember { NetworkConnectivityObserver(context) }
    val isConnected by connectivityObserver.networkStatus.collectAsState(initial = true)


    Box(modifier = Modifier.fillMaxSize()) {
        if (showMap.value) {
            Map(
                onLocationAdded = { showMap.value = false },
                onPlaceSelected = { locationDataClass ->
                    favViewModel.addFavLocation(
                        locationDataClass
                    )
                },
                buttontext = context.getString(R.string.add_to_favorites),
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
                        if (isConnected) {
                            showMap.value = true
                        }},
                    onItemClick = { location ->
                        if (isConnected){
                            selectedLocation.value = location
                            weatherViewModel.getCurrentWeather(
                                lat = location.latitude,
                                lon = location.longitude
                            )
                            showDetails.value = true
                        }
                    },
                    isConnected = isConnected
                )
            }
        }
    }
}




