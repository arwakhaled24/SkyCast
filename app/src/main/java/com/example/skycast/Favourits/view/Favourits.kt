package com.example.skycast.Favourits.view

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import com.example.skycast.home.viewModel.HomeViewModel
import com.example.skycast.home.viewModel.WeatherViewModel
import com.example.skycast.utils.NetworkConnectivityObserver
import com.example.skycast.utils.SharedPreferences


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
    val isConnected by connectivityObserver.networkStatus.collectAsState(initial = false)

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = showMap.value,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            if (showMap.value) {
                Map(
                    onLocationAdded = { showMap.value = false },
                    onPlaceSelected = { locationDataClass ->
                        favViewModel.addFavLocation(locationDataClass)
                    },
                    buttonText = context.getString(R.string.add_to_favorites),
                )
            }
        }

        AnimatedVisibility(
            visible = !showMap.value && showDetails.value && selectedLocation.value != null,
            enter = fadeIn() + slideInHorizontally(),
            exit = fadeOut() + slideOutHorizontally()
        ) {
            if (!showMap.value && showDetails.value && selectedLocation.value != null) {
                selectedLocation.value?.let {
                    Home(
                        it.latitude,
                        it.longitude,
                        weatherViewModel,
                        HomeViewModel()
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = !showMap.value && !showDetails.value,
        ) {
            if (!showMap.value && !showDetails.value) {
                when (favLocations) {
                    is RespondStatus.Loading -> OnLoading()
                    is RespondStatus.Error -> OnError(favLocations.error)
                    is RespondStatus.Success -> OnSuccess(
                        locations = favLocations.result,
                        favViewModel = favViewModel,
                        onFabClick = {
                            if (isConnected) {
                                showMap.value = true
                            }
                        },
                        onItemClick = { location ->
                            if (isConnected) {
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

    BackHandler(
        enabled = showDetails.value ||showMap.value,
        onBack = {
            showDetails.value = false
            showMap.value=false
            selectedLocation.value = null
            weatherViewModel.getForecast(
                lat = SharedPreferences.getInstance(context).getLat().toString(),
                lon = SharedPreferences.getInstance(context).getLong().toString()
            )
        }
    )
}




