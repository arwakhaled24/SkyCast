package com.example.skycast.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.skycast.view.screens.Favourits
import com.example.skycast.view.screens.Setting
import com.example.skycast.view.screens.Weather
@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.HomeScreen.route,
        modifier = Modifier
    ) {
        composable(route = NavigationRoutes.HomeScreen.route) { /*navigationBackStack->*/
      /*      val data = navigationBackStack.toRoute<NavigationRoutes.HomeScreen>()
            val currentWeatherRespond = data.currentWeatherRespond
            val forecasteRespond = data.forecastRespond*/
            Weather(/*currentWeatherRespond,forecasteRespond*/)
        }
        composable(route = NavigationRoutes.SettingScreen.route) {
            Setting()
        }
        composable(route = NavigationRoutes.FavouritScreen.route) {
            Favourits()
        }
    }

}