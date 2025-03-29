package com.example.skycast.Favourits

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.skycast.Favourits.view.Favourits
import com.example.skycast.Favourits.view.Map
import com.example.skycast.Favourits.viewModel.FavouritsViewModel
import com.example.skycast.home.view.Home
import com.example.skycast.home.viewModel.WeatherViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    navController: NavHostController,
    favViewModel: FavouritsViewModel,
    weatherViewModel: WeatherViewModel,
    startDestination: String = AppDestinations.FAVOURITES_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = AppDestinations.FAVOURITES_ROUTE) {
            Favourits(
                favViewModel = favViewModel,
                weatherViewModel = weatherViewModel,
                onNavigateToMap = {
                    navController.navigate(AppDestinations.MAP_ROUTE)
                },
                onNavigateToDetails =
                { latitude, longitude, locationName ->

                    weatherViewModel.getCurrentWeather(latitude, longitude)
                    navController.navigate(
                        AppDestinations.homeRouteWithArgs(
                            latitude = latitude,
                            longitude = longitude,
                            locationName = locationName.replace("/", "-")
                        )
                    )
                }
            )
        }
        composable(route = AppDestinations.MAP_ROUTE) {
            Map(
                favViewModel = favViewModel,
                onLocationAdded = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = AppDestinations.HOME_ROUTE_WITH_ARGS,
            arguments = listOf(
                navArgument(AppDestinations.LATITUDE_ARG) { type = NavType.StringType },
                navArgument(AppDestinations.LONGITUDE_ARG) { type = NavType.StringType },
                navArgument(AppDestinations.LOCATION_NAME_ARG) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val latitude = backStackEntry.arguments?.getString(AppDestinations.LATITUDE_ARG) ?: ""
            val longitude = backStackEntry.arguments?.getString(AppDestinations.LONGITUDE_ARG) ?: ""
            val locationName = backStackEntry.arguments?.getString(AppDestinations.LOCATION_NAME_ARG)?.replace("-", "/") ?: ""

            Home(
                viewModel = weatherViewModel,
                lat = latitude,
                long = longitude,
            )
        }
    }
}


object AppDestinations {
    const val FAVOURITES_ROUTE = "favourites"
    const val HOME_ROUTE = "home"
    const val MAP_ROUTE = "map"

    const val LATITUDE_ARG = "latitude"
    const val LONGITUDE_ARG = "longitude"
    const val LOCATION_NAME_ARG = "locationName"

    const val HOME_ROUTE_WITH_ARGS = "$HOME_ROUTE/{$LATITUDE_ARG}/{$LONGITUDE_ARG}/{$LOCATION_NAME_ARG}"

    fun homeRouteWithArgs(latitude: String, longitude: String, locationName: String): String {
        return "$HOME_ROUTE/$latitude/$longitude/$locationName"
    }
}
