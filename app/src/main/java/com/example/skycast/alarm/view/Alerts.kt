package com.example.skycast.alarm.view

import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.skycast.Favourits.viewModel.FavouritsViewModel
import com.example.skycast.R
import com.example.skycast.data.dataClasses.LocationDataClass
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@Composable
fun Alert() {


}

@Composable
fun Map1(favViewModel: FavouritsViewModel) {
    val context = LocalContext.current
    val markerPosition = remember { mutableStateOf<LatLng?>(null) }
    val geocoder = Geocoder(context)
    val city = remember { mutableStateOf("") }
    val country = remember { mutableStateOf("") }
    val addresse= remember { mutableStateOf("No Selected Location yet") }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            onMyLocationClick = { location ->
                Log.i("TAG", "MountainMap: ${location.longitude.toString()}")
            },
            onMyLocationButtonClick = { -> true },
            onMapClick = { latlong ->
                markerPosition.value = latlong
                runBlocking {
                    launch() {
                        addresse.value =
                            geocoder.getFromLocation(
                                markerPosition.value!!.latitude,
                                markerPosition.value!!.longitude,
                                1
                            )?.firstOrNull()?.getAddressLine(0)?:"  "
                        val temp =  geocoder.getFromLocation(
                            markerPosition.value!!.latitude,
                            markerPosition.value!!.longitude,
                            1
                        )?.firstOrNull()

                        city.value= temp?.featureName?:""
                        country.value=temp?.countryName?:""
/*
                        addresse.value= "${city.value}/ ${country.value}"
*/

                    }
                }
            }) {
            markerPosition.value?.let { position ->
                Marker(
                    state = MarkerState(position = position),
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 80.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF2196F3),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = addresse.value,
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                if (addresse.value != "No Selected Location yet") {
                    Button(
                        onClick = {
                            favViewModel.addFavLocation(
                                LocationDataClass(
                                longitude = markerPosition.value?.longitude.toString(),
                                latitude = markerPosition.value?.latitude.toString(),
                                    CityName = "${city.value}, ${country.value}" ,
                            )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.8f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "ADD TO FAVORITES",
                            modifier = Modifier.padding(start = 8.dp),
                            color = Color.White
                        )
                    }
                }
            }

        }
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
    }
}}

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