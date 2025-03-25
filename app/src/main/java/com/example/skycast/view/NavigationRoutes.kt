package com.example.skycast.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.skycast.data.dataClasses.currentWeather.CurrentWeatherRespond
import com.example.skycast.data.dataClasses.forecastRespond.ForecasteRespond
import kotlinx.serialization.Serializable


sealed class NavigationRoutes(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
  object /* data class*/ HomeScreen/*(val currentWeatherRespond: CurrentWeatherRespond?, val forecastRespond: ForecasteRespond?)*/ :
        NavigationRoutes(route = "home", title = "Home",icon = Icons.Default.Home) {
    }

    object  FavouritScreen :
        NavigationRoutes(route = "favourites", title = "Favourites", icon = Icons.Default.Favorite)

    object  SettingScreen  :
        NavigationRoutes(route = "settings", title = "Settings", icon = Icons.Default.Settings)

    
}