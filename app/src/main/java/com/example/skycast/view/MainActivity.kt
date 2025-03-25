package com.example.skycast.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.skycast.Favourits.view.Favourits
import com.example.skycast.data.LocalData.LocalDataSource
import com.example.skycast.data.LocalData.room.MyDatabase
import com.example.skycast.data.dataClasses.NavItem
import com.example.skycast.data.remoteData.WearherRemoreDataSourse
import com.example.skycast.data.repository.WeatherRepository
import com.example.skycast.home.view.Home
import com.example.skycast.home.viewModel.HomeViewModel
import com.example.skycast.home.viewModel.MyFactory
import com.example.skycast.view.screens.Alert
import com.example.skycast.view.screens.Setting
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

private const val LOCATION_PERMISSION_CODE = 1

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    /*
        lateinit var locationState: MutableState<Location>
    */
    lateinit var weatherViewModel: HomeViewModel

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


        var myFactory = MyFactory(
            WeatherRepository(
                WearherRemoreDataSourse(),
                LocalDataSource(MyDatabase.getInstance(context = this).getDao())
            )
        )

        weatherViewModel = ViewModelProvider(this, myFactory)
            .get(HomeViewModel::class.java)

        setContent {
            MainScreen(weatherViewModel)


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
                    Log.i("TAG", "onLocationResult: ${locationResult.lastLocation}")
                    /* locationState.value =
                         locationResult.lastLocation ?: Location(LocationManager.GPS_PROVIDER)*/
                    val location =
                        locationResult.lastLocation ?: Location(LocationManager.GPS_PROVIDER)
                    weatherViewModel.getCurrentWeather(
                        location.latitude.toString(),
                        location.longitude.toString()
                    )
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


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(viewModel: HomeViewModel) {
    var navController = rememberNavController()
    val   selectedIndex = remember { mutableStateOf(0) }
    val navItems = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Favorites", Icons.Default.Favorite),
        NavItem("Notification", Icons.Default.Notifications),
        NavItem("Settings", Icons.Default.List)
    )
    /* val currentWeather = viewModel.currentWeather.collectAsState().value
     val forecasteRespond = viewModel.forecast.collectAsState().value*/
    ////do when and check the types error -> load-> suc


    /*
        Log.i("TAG", "viewmodel: ${currentWeather.value?.name?:"www"}")
    */

    val msg = viewModel.message.observeAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

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
        ContentScreen(selectedIndex.value)
    }

}

@Composable
fun BottomBar(navController: NavHostController) {
  val   selectedIndex = remember { mutableStateOf(0) }
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
                selected = if (selectedIndex.value == index) true else false,
            )
        })
    }
}


@Composable
fun ContentScreen(index: Int) {
    when (index) {
        0 -> Home()
        1 -> Favourits()
        2 -> Alert()
        3 -> Setting()
    }

}

/*
@Composable
fun RowScope.AddItem(
    screen: NavItem,
    currentDestination: NavDestination?,
    navController: NavHostController,
    selectedIndex: Int
) {
    NavigationBarItem(
        label = { screen.name },

        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "navigation icon"
            )
        },
        */
/*  selected = selectedIndes== screen.index? true :false  ,*//*


        onClick = { selectedIndes = screen.index },
        selected = TODO(),

        )

}
*/
