package com.example.skycast.Favourits.view

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.widget.addTextChangedListener
import com.example.skycast.Favourits.viewModel.FavouritsViewModel
import com.example.skycast.data.dataClasses.LocationDataClass
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Map(
    favViewModel: FavouritsViewModel,
    onLocationAdded: () -> Unit,
) {
    val context = LocalContext.current
    var markerPosition = remember { mutableStateOf<LatLng?>(null) }
    val geocoder = remember { Geocoder(context) }
    val city = remember { mutableStateOf("") }
    val country = remember { mutableStateOf("") }
    val address = remember { mutableStateOf("No Selected Location yet") }
    val quertText = remember { mutableStateOf("") }
    val query = remember { mutableStateOf("") }
    val activ = remember { mutableStateOf(false) }
    val names = listOf("arwa", "sara", "ahmed", "mohamed", "khaled", "bosy")// to be country list
    val searchFlow = remember { MutableSharedFlow<List<String>>(replay = 1) }
    var searchResults = remember { mutableStateOf(emptyList<String>()) }
    val selectedLocation = remember { mutableStateOf<LatLng?>(null) }
    val scope = rememberCoroutineScope()

    Column {
        Spacer(modifier = Modifier.height(18.dp))


        SearchBar(
            onPlaceSelected = { place ->

                selectLocation(place, context, markerPosition, address)
            }

        )



        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                onMapClick = { latLng ->
                    markerPosition.value = latLng
                    updateLocaionAddress(latLng, geocoder, address)

                }

            )
            {
                markerPosition.value?.let { position ->
                    Marker(state = MarkerState(position = position))
                }
            }

            scope.launch() {
                searchFlow.collect { result ->
                    searchResults.value = result
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
                        .background(Color(0xFF2196F3), RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = address.value,
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    if (address.value != "No Selected Location yet") {
                        Button(
                            onClick = {
                                favViewModel.addFavLocation(
                                    LocationDataClass(
                                        longitude = markerPosition.value!!.longitude.toString(),
                                        latitude = markerPosition.value!!.latitude.toString(),
                                        CityName = address.value
                                    )
                                )
                                onLocationAdded()
                            },
                            modifier = Modifier.fillMaxWidth(0.8f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("ADD TO FAVORITES", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}


fun selectLocation(
    selectedPlace: String,
    context: Context,
    markerPosition: MutableState<LatLng?>,
    address: MutableState<String>
) {
    runBlocking {
        launch {
            val geocoder = Geocoder(context)
            val addresses = withContext(Dispatchers.IO) {
                geocoder.getFromLocationName(selectedPlace, 1)
            }
            if (!addresses.isNullOrEmpty()) {
                val addressVar = addresses[0]
                val latLng = LatLng(addressVar.latitude, addressVar.longitude)
                markerPosition.value = latLng
                updateLocaionAddress(
                    latLng,
                    geocoder,
                    address
                )

            } else {


            }
        }
    }

}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onPlaceSelected: (String) -> Unit
) {
    val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    AndroidView(
        factory = { context ->
            AutoCompleteTextView(context).apply {
                hint = "Search for a place"

                setTextColor(textColor.toArgb())
                setHintTextColor(
                    textColor.copy(alpha = 0.6f).toArgb()
                )

                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                val autocompleteAdapter =
                    ArrayAdapter<String>(context, R.layout.simple_dropdown_item_1line)
                val placesClient = Places.createClient(context)
                val autocompleteSessionToken = AutocompleteSessionToken.newInstance()


                addTextChangedListener { editable ->
                    val query = editable?.toString() ?: ""
                    if (query.isNotEmpty()) {

                        val request = FindAutocompletePredictionsRequest.builder()
                            .setSessionToken(autocompleteSessionToken)
                            .setQuery(query)
                            .build()

                        placesClient.findAutocompletePredictions(request)
                            .addOnSuccessListener { response ->
                                autocompleteAdapter.clear()
                                response.autocompletePredictions.forEach { prediction ->

                                    autocompleteAdapter.add(prediction.getFullText(null).toString())
                                }
                                autocompleteAdapter.notifyDataSetChanged()
                            }
                    }
                }


                setAdapter(autocompleteAdapter)


                setOnItemClickListener { _, _, position, _ ->
                    val selectedPlace =
                        autocompleteAdapter.getItem(position) ?: return@setOnItemClickListener
                    onPlaceSelected(selectedPlace)
                }
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

fun updateLocaionAddress(latLng: LatLng, geocoder: Geocoder, address: MutableState<String>) {

    runBlocking {
        launch {
            val addresses = geocoder.getFromLocation(
                latLng.latitude,
                latLng.longitude,
                1
            )
            addresses?.firstOrNull()?.let {
                val city = it.locality ?: ""
                val country = it.countryName ?: ""
                address.value = "${city}, ${country}"
            }
        }
    }

}