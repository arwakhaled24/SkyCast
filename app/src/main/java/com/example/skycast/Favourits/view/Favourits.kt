package com.example.skycast.Favourits.view

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skycast.Favourits.viewModel.FavouritsViewModel
import com.example.skycast.R
import com.example.skycast.data.RespondStatus
import com.example.skycast.data.dataClasses.LocationDataClass
import com.example.skycast.home.view.Home
import com.example.skycast.home.viewModel.WeatherViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Favourits(
    favViewModel: FavouritsViewModel,
    weatherViewModel: WeatherViewModel,
    onNavigateToMap: () -> Unit,
    onNavigateToDetails: (latitude: String, longitude: String, locationName: String) -> Unit
) {
    favViewModel.getAllFav()
    val showMap = remember { mutableStateOf(false) }
    val showDetails = remember { mutableStateOf(false) }
    val favLocations = favViewModel.favouritLocationList.collectAsState().value
    val selectedLocation = remember { mutableStateOf<LocationDataClass?>(null) }



    Box(modifier = Modifier.fillMaxSize()) {
        if (showMap.value) {
            Map(
                favViewModel = favViewModel,
                onLocationAdded = { showMap.value = false }
            )
        } else if (showDetails.value && selectedLocation.value != null) {
            selectedLocation.value?.let {
                Home(
                    weatherViewModel,
                    it.latitude, it.longitude
                )
            }
        } else {
            when (favLocations) {
                is RespondStatus.Loading -> OnLoading()
                is RespondStatus.Error -> OnError(favLocations.error)
                is RespondStatus.Success -> OnSuccess(
                    locations = favLocations.result,
                    favViewModel = favViewModel,
                    onFabClick = { showMap.value = true },
                    onItemClick = { location ->
                        // Set the selected location and show details
                        selectedLocation.value = location
                        // Call the weather API with the selected location data
                        weatherViewModel.getCurrentWeather(
                            lat = location.latitude,
                            lon = location.longitude
                        )
                        showDetails.value = true
                    }
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OnSuccess(
    locations: List<LocationDataClass>,
    favViewModel: FavouritsViewModel,
    onFabClick: () -> Unit,
    onItemClick: (LocationDataClass) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFA5BFCC)),

    ) {
        if (locations.isEmpty()) {
            Column(
                Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.empity_box_backgroundless),
                    contentDescription = "Empty List",
                    Modifier.size(300.dp)
                )

                Text(text = "No Selected Location yet", fontSize = 30.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp, top = 40.dp, end = 10.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                itemsIndexed(locations) { index, location ->
                    FavItem(location, favViewModel, { onItemClick(location) }
                    )
                }
            }
        }
        FloatingActionButton(
            onClick = onFabClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 120.dp, end = 35.dp)
                .width(50.dp)
                .height(50.dp),
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Location")
        }
    }
}


@Composable
fun OnError(e: Throwable) {
    val context = LocalContext.current
    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
}

@Composable
fun OnLoading() {
    Column(
        Modifier.fillMaxSize(),
        Arrangement.Center,
        Alignment.CenterHorizontally

    ) { CircularProgressIndicator() }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FavItem(
    locationDataClass: LocationDataClass,
    favViewModel: FavouritsViewModel,
    onItemClick: () -> Unit
) {
    val context = LocalContext.current
    val parts = locationDataClass.CityName.split(", ")
    val city = parts.getOrNull(0) ?: ""
    val country = parts.getOrNull(1) ?: ""

    Row(
        modifier = Modifier
            .fillMaxHeight(fraction = .8F)
            .fillMaxWidth()
            .background(color = Color.Transparent)
            .border(
                3.dp,
                Color.White,
                shape = RoundedCornerShape(25.dp)
            )
            .clickable {
                onItemClick.invoke()
            }
            .padding(15.dp)
            .background(
                color = Color.Transparent
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,

        ) {

        Text(
            text = city,
            fontSize = 20.sp,
            color = Color.White
        )
        Spacer(Modifier.width(40.dp))
        Text(
            text = country,
            fontSize = 16.sp,
            color = Color.White
        )
        Spacer(Modifier.width(80.dp))
        Icon(
            Icons.Filled.Delete,
            contentDescription = "Item Fav icon",
            modifier = Modifier
                .size(25.dp)
                .clickable {
                    favViewModel.deleteFavLocation(locationDataClass)
                },
        )

    }

}

fun onRowClick(long: String, lat: String, viewModel: WeatherViewModel) {
}