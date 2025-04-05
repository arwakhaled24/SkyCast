package com.example.skycast

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
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Left
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Right
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.skycast.Favourits.view.Favourits
import com.example.skycast.Favourits.viewModel.FavouritsViewModel
import com.example.skycast.Favourits.viewModel.MyFavFactory
import com.example.skycast.alarm.AlarmFactory
import com.example.skycast.alarm.AlarmViewModel
import com.example.skycast.alarm.view.Alert
import com.example.skycast.data.LocalData.LocalDataSource
import com.example.skycast.data.LocalData.room.MyDatabase
import com.example.skycast.data.RespondStatus
import com.example.skycast.data.dataClasses.NavItem
import com.example.skycast.data.remoteData.WearherRemoreDataSourse
import com.example.skycast.data.remoteData.retrofit.RetrofitHelper
import com.example.skycast.data.repository.WeatherRepository
import com.example.skycast.home.view.Home
import com.example.skycast.home.viewModel.HomeViewModel
import com.example.skycast.home.viewModel.MyFactory
import com.example.skycast.home.viewModel.WeatherViewModel
import com.example.skycast.settings.Setting
import com.example.skycast.ui.theme.PrimaryContainer
import com.example.skycast.ui.theme.SkyCastTheme
import com.example.skycast.utils.SharedPreferences
import com.example.skycast.utils.getMetaDataValue
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
    lateinit var alarmViewModel: AlarmViewModel
    lateinit var currentLocation: MutableState<Location>
    lateinit var selectedLong: MutableState<Double>
    lateinit var selectedLat: MutableState<Double>
    lateinit var homeViewModel: HomeViewModel


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        var myFactory = MyFactory(
            WeatherRepository.getInstance(
                WearherRemoreDataSourse(RetrofitHelper(context = this)),
                LocalDataSource(
                    MyDatabase.getInstance(context = this).getDao(),
                )
            ), this
        )
        var myFavFactory = MyFavFactory(
            WeatherRepository.getInstance(
                WearherRemoreDataSourse(RetrofitHelper(context = this)),
                LocalDataSource(MyDatabase.getInstance(context = this).getDao())
            )
        )
        var MyalarmFactory = AlarmFactory(
            WeatherRepository.getInstance(
                WearherRemoreDataSourse(RetrofitHelper(context = this)),
                LocalDataSource(MyDatabase.getInstance(context = this).getDao())
            )
        )

        val apiKey = getMetaDataValue(this)
        if (!Places.isInitialized() && apiKey != null) {
            Places.initialize(applicationContext, apiKey)
        }
        setContent {
            selectedLat = remember { mutableStateOf(0.0) }
            selectedLong = remember { mutableStateOf(0.0) }
            weatherViewModel = viewModel(factory = myFactory)
            favViewModel = viewModel(factory = myFavFactory)
            alarmViewModel = viewModel(factory = MyalarmFactory)
            currentLocation = remember { mutableStateOf(Location(LocationManager.GPS_PROVIDER)) }
            homeViewModel= viewModel()
            SkyCastTheme {
                MainScreen(
                    weatherViewModel,
                    favViewModel,
                    alarmViewModel,
                    selectedLong.value,
                    selectedLat.value,
                    homeViewModel
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (SharedPreferences.getInstance(this).isSelectedLocation() == false) {
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
                Log.i("TAG", "onStart:fromLocation ")
            }
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
                    selectedLat.value =
                        locationResult.lastLocation?.latitude
                            ?: Location(LocationManager.GPS_PROVIDER).latitude
                    selectedLong.value =
                        locationResult.lastLocation?.longitude
                            ?: Location(LocationManager.GPS_PROVIDER).longitude
                }
            },
            Looper.myLooper()
        )
    }

    private fun enableLocationServices() {
        Toast.makeText(this, " please enable location", Toast.LENGTH_SHORT).show()
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
@OptIn(ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    weatherViewModel: WeatherViewModel,
    favViewModel: FavouritsViewModel,
    alarmViewModel: AlarmViewModel,
    selectedLong: Double,
    selectedLat: Double,
    homeViewModel: HomeViewModel
) {

    val selectedIndex = remember { mutableStateOf(0) }
    val navItems = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Favorites", Icons.Default.Favorite),
        NavItem("Notification", Icons.Default.Notifications),
        NavItem("Settings", Icons.Default.List)
    )
    var selectedLo = selectedLong
    var selectedLa = selectedLat
    val context = LocalContext.current
    if (SharedPreferences.getInstance(context).isSelectedLocation()) {
        selectedLa = SharedPreferences.getInstance(context).getLat().toDouble()
        selectedLo = SharedPreferences.getInstance(context).getLong().toDouble()
    }
    weatherViewModel.getForecast(lat = selectedLa.toString(), lon = selectedLo.toString())
    weatherViewModel.getCurrentWeather(lat = selectedLa.toString(), lon = selectedLo.toString(),)
    val lottie= rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.stars))
    val forecastState = weatherViewModel.forecast.collectAsState().value
    var source= getDayNightBackground(weatherViewModel)


    Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            containerColor = Color.Gray.copy(alpha = 0.8f),
            bottomBar = {
                NavigationBar(containerColor = Color.Gray.copy(alpha = .8f)) {
                    navItems.forEachIndexed({ index, screen ->
                        NavigationBarItem(
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                unselectedIconColor = Color.White.copy(alpha = 0.6f),
                                indicatorColor = Color.Gray.copy(alpha = 1.2f)
                            ),
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
            AnimatedContent(targetState = selectedIndex.value, transitionSpec = {
                slideIntoContainer(
                    animationSpec = tween(500, easing = EaseIn),
                    towards = Left
                ).with(
                    slideOutOfContainer(
                        animationSpec = tween(500, easing = EaseOut),
                        towards = Right
                    )
                )
            }) { targetState ->
                weatherViewModel.updateLanguage(context)
                Box {
                    Image(
                        painter = painterResource(source?:R.drawable.ihone_packgound),
                        contentDescription = "Background",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    LottieAnimation( composition = lottie.value,
                        modifier = Modifier.fillMaxSize(),
                        iterations = LottieConstants.IterateForever,
                        contentScale = ContentScale.FillBounds )

                    when (targetState) {

                        0 -> Home(
                            lat = selectedLo.toString(),
                            long = selectedLa.toString(),
                            weatherViewModel,
                            homeViewModel
                        )

                        1 -> Favourits(
                            favViewModel,
                            weatherViewModel,
                        )

                        2 -> Alert(
                            alarmViewModel,
                            currenTocationLat = selectedLa.toString(),
                            currenTocationLong = selectedLo.toString(),
                        )

                        3 -> Setting()
                    }
                }
            }
        }
}

@Composable
fun getDayNightBackground(weatherViewModel: WeatherViewModel): Int? {
    val forecastState = weatherViewModel.forecast.collectAsState().value

    return when {
        forecastState is RespondStatus.Loading -> null
        forecastState is RespondStatus.Error -> null
        else -> {
            val iconCode = (forecastState as? RespondStatus.Success)?.result
                ?.list
                ?.firstOrNull()
                ?.weather
                ?.firstOrNull()
                ?.icon
                ?: "01d"

            if (iconCode.endsWith("n")) {
                    R.drawable.ihone_packgound

            } else {
                PrimaryContainer= Color(0x98D8EF)
                R.drawable.day
            }
        }
    }
}


