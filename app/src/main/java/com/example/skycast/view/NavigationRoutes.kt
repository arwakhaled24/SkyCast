package com.example.skycast.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationRoutes(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object HomeScreen :
        NavigationRoutes(route = "home", title = "Home",icon = Icons.Default.Home)

    object  FavouritScreen :
        NavigationRoutes(route = "favourites", title = "Favourites", icon = Icons.Default.Favorite)

    object  SettingScreen  :
        NavigationRoutes(route = "settings", title = "Settings", icon = Icons.Default.Settings)
}