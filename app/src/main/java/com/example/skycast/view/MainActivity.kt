package com.example.skycast.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.skycast.Favourits.AppDestinations
import com.example.skycast.Favourits.view.Favourits
import com.example.skycast.Favourits.viewModel.FavouritsViewModel
import com.example.skycast.Favourits.viewModel.MyFavFactory
import com.example.skycast.alarm.view.Alert
import com.example.skycast.data.LocalData.LocalDataSource
import com.example.skycast.data.LocalData.room.MyDatabase
import com.example.skycast.data.dataClasses.NavItem
import com.example.skycast.data.remoteData.WearherRemoreDataSourse
import com.example.skycast.data.remoteData.retrofit.RetrofitHelper
import com.example.skycast.data.repository.WeatherRepository
import com.example.skycast.home.view.Home
import com.example.skycast.home.viewModel.MyFactory
import com.example.skycast.home.viewModel.WeatherViewModel
import com.example.skycast.ui.theme.SkyCastTheme
import com.example.skycast.utils.getMetaDataValue
import com.example.skycast.view.screens.Setting
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.libraries.places.api.Places

private const val LOCATION_PERMISSION_CODE = 1

class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var weatherViewModel: WeatherViewModel
    lateinit var favViewModel: FavouritsViewModel
    lateinit var currentLocation: MutableState<Location>

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        var myFactory = MyFactory(
            WeatherRepository.getInstance(
                WearherRemoreDataSourse(RetrofitHelper(context = this)),
                LocalDataSource(MyDatabase.getInstance(context = this).getDao())
            )
        )
        var myFavFactory = MyFavFactory(
            WeatherRepository.getInstance(
                WearherRemoreDataSourse(RetrofitHelper(context = this)),
                LocalDataSource(MyDatabase.getInstance(context = this).getDao())
            )
        )
        Log.i("TAG", "onCreate: ${getMetaDataValue(this)} ")
        val apiKey = getMetaDataValue(this)
        if (!Places.isInitialized() && apiKey != null) {
            Places.initialize(applicationContext, apiKey)
        }
        setContent {
            weatherViewModel = viewModel(factory = myFactory)
            favViewModel = viewModel(factory = myFavFactory)
            currentLocation = remember { mutableStateOf(Location(LocationManager.GPS_PROVIDER)) }
            SkyCastTheme {
                MainScreen(weatherViewModel, favViewModel, currentLocation.value)

            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                getCurrentLocation()
            } else {
                enableLocationServices()
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                LOCATION_PERMISSION_CODE
            )
        }
    }

    private fun checkPermissions(): Boolean {
        var result = false
        if ((ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            result = true
        }
        return result
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.Builder(3600000).apply {
                setPriority(Priority.PRIORITY_LOW_POWER)
            }.build(),
            object :
                LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    currentLocation.value =
                        locationResult.lastLocation ?: Location(LocationManager.GPS_PROVIDER)

                }
            },
            Looper.myLooper()
        )
    }

    private fun enableLocationServices() {
        Toast.makeText(this, "enable location", Toast.LENGTH_SHORT).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getCurrentLocation()
            else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    LOCATION_PERMISSION_CODE
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    weatherViewModel: WeatherViewModel,
    favViewModel: FavouritsViewModel,
    currentLocation: Location,
    navController: NavHostController = rememberNavController()
) {
    val selectedIndex = remember { mutableStateOf(0) }
    val msg = weatherViewModel.message.observeAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val navItems = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Favorites", Icons.Default.Favorite),
        NavItem("Notification", Icons.Default.Notifications),
        NavItem("Settings", Icons.Default.List)
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar {
                navItems.forEachIndexed({ index, screen ->
                    NavigationBarItem(
                        label = { screen.name },
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = "navigation icon"
                            )
                        },
                        onClick = { selectedIndex.value = index },
                        selected = if (selectedIndex.value == index) true else false,
                    )
                })
            }
        }
    ) {
        when (selectedIndex.value) {
            0 -> Home(
                weatherViewModel,
                lat = currentLocation.latitude.toString(),
                long = currentLocation.longitude.toString(),
            )

            1 -> Favourits(
                favViewModel,
                weatherViewModel,
                { navController.navigate(AppDestinations.MAP_ROUTE) },
                onNavigateToDetails = { latitude, longitude, locationName ->
                    weatherViewModel.getCurrentWeather(latitude, longitude)
                    navController.navigate(
                        AppDestinations.homeRouteWithArgs(
                            latitude = latitude,
                            longitude = longitude,
                            locationName = locationName.replace("/", "-")
                        )
                    )})
                    2 -> Alert()
                    3 -> Setting()
                }
        }

    }

    @Composable
    fun BottomBar(navController: NavHostController) {
        val selectedIndex = remember { mutableStateOf(0) }
        val navItems = listOf(
            NavItem("Home", Icons.Default.Home),
            NavItem("Favorites", Icons.Default.Favorite),
            NavItem("Notification", Icons.Default.Notifications),
            NavItem("Settings", Icons.Default.List)
        )

        NavigationBar {
            navItems.forEachIndexed({ index, screen ->
                NavigationBarItem(
                    label = { screen.name },
                    icon = {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = "navigation icon"
                        )
                    },
                    onClick = { selectedIndex.value = index },
                    selected = selectedIndex.value == index,
                )
            })
        }
    }

