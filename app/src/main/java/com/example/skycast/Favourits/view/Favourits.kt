package com.example.skycast.Favourits.view

import android.location.Geocoder
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skycast.R
import com.example.skycast.data.RespondStatus
import com.example.skycast.data.dataClasses.LocationDataClass

@Composable
fun Favourits(favLocations: RespondStatus<List<LocationDataClass>>) {
    /*   when (favLocations) {
           is RespondStatus.Loading -> OnLoading()
           is RespondStatus.Error -> OnError(favLocations.error)
           is RespondStatus.Success -> OnSuccess(favLocations.result)
       }*/

    OnSuccess(
        listOf(
          /*  LocationDataClass(
                longitude = "30.907670",
                latitude = "29.989723",
                CityName = "kom hamada",
            ),
            LocationDataClass(
                longitude = "30.907670",
                latitude = "29.989723",
                CityName = "kom hamada",
            ),
            LocationDataClass(
                longitude = "30.907670",
                latitude = "29.989723",
                CityName = "kom hamada",
            )*/
        )
    )
}

@Composable
fun OnSuccess(favLocations: List<LocationDataClass>) {


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Red)
    ) {
        if (favLocations.isEmpty()) {
            Column ( Modifier
                .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally){
                Image(
                    painter = painterResource(id = R.drawable.empity_box_backgroundless),
                    contentDescription = "Empity List",
                    Modifier.size(300.dp)
                )

                Text(text = stringResource(R.string.no_favourite_location_yet), fontSize = 30.sp)

            }
        }else{
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp, top = 40.dp, end = 10.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                itemsIndexed(favLocations) { index, location ->
                    FavItem(location)
                }
            }
        }

        FloatingActionButton(
            onClick = { /* Handle click */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 120.dp, end = 35.dp)
                .width(50.dp)
                .height(50.dp),
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "FAB")
        }
    }
    /*FloatingActionButton(
        onClick = {  },
        modifier = Modifier
            .width(50.dp)
            .height(50.dp),
        shape = RoundedCornerShape(20.dp)

        ) {
    }*/
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

@Composable
fun FavItem(locationDataClass: LocationDataClass) {
    val context = LocalContext.current
    var addresses = Geocoder(context).getFromLocation(
        locationDataClass.latitude.toDouble(),
        locationDataClass.longitude.toDouble(),
        1
    )

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
            .padding(15.dp)
            .background(
                color = Color.Transparent
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = locationDataClass.CityName,
            fontSize = 22.sp,
            color = Color.White
        )

        Spacer(Modifier.width(20.dp))
        Text(
            text = locationDataClass.CityName,
            fontSize = 16.sp, color = Color.White
        )
        Spacer(Modifier.width(80.dp))
        Icon(
            Icons.Filled.KeyboardArrowRight,
            contentDescription = "Item Fav icon",
            modifier = Modifier.size(25.dp)
        )
        /*
                Text(text = addresses?.firstOrNull()?.getAddressLine(0) ?: "......")
        */
    }

}