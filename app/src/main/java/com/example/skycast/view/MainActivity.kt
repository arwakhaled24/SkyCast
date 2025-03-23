package com.example.skycast.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.skycast.data.LocalData.WeatherLocalDataSourse
import com.example.skycast.data.remoteData.WearherRemoreDataSourse
import com.example.skycast.data.repository.WeatherRepository
import com.example.skycast.viewModel.CurrentWeatherViewModel
import com.example.skycast.viewModel.MyFactory


class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var myFactory = MyFactory(
            WeatherRepository(
                WearherRemoreDataSourse(), WeatherLocalDataSourse()
            )
        )
        var weatherViewModel =
            ViewModelProvider(this, myFactory).get(CurrentWeatherViewModel::class.java)


        setContent {
            MainScreen(weatherViewModel)

        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(viewModel: CurrentWeatherViewModel) {
    var navController = rememberNavController()
    viewModel.getCurrentWeather("44", "44")

    val currentWeather = viewModel.currentWeather.observeAsState()
    val msg = viewModel.message.observeAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {BottomBar(navController)}
    ) {
        NavigationGraph(navController)
    }

}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        NavigationRoutes.HomeScreen,
        NavigationRoutes.SettingScreen,
        NavigationRoutes.FavouritScreen
    )
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    NavigationBar {
        screens.forEach({ screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        })
    }
}

@Composable
fun RowScope.AddItem(
    screen: NavigationRoutes,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    NavigationBarItem(
        label = { screen.title },

        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "navigation icon"
            )
        },

        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,

        onClick = { navController.navigate(screen.route) }
    )

}
