package com.example.skycast.Favourits.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.skycast.data.RespondStatus
import com.example.skycast.data.dataClasses.LocationDataClass
import com.example.skycast.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavouritsViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _favouritLocationList = MutableStateFlow<RespondStatus<List<LocationDataClass>>>(RespondStatus.Loading)
    val favouritLocationList = _favouritLocationList.asStateFlow()

    private val _msg = mutableStateOf("")
    val msg = _msg

    init {
        getAllFav()
    }

    fun getAllFav() {
        viewModelScope.launch {
           val favLocations = repository.getAllFav()
            favLocations.catch {
                _favouritLocationList.value = RespondStatus.Error(it)
            }.collect{
                _favouritLocationList.value=RespondStatus.Success(it)
            }
        }
    }

    fun deleteFavLocation(locationDataClass: LocationDataClass) {
        var favLocationResult: Int = 1
        viewModelScope.launch {
            try {
                favLocationResult = repository.deleteFavLocation(locationDataClass)
                _msg.value = "Location deleted "
            } catch (e: Throwable) {
                if (favLocationResult == 0) {
                    _msg.value = "Location Was Not deleted"
                }
                _msg.value = "something went wrong"
            }
        }
    }

    fun addFavLocation(locationDataClass: LocationDataClass) {
        var favLocationResult: Int = 1
        viewModelScope.launch {
            try {
                val favLocations = repository.addFavLocation(locationDataClass)
                _msg.value = "Location added "
            } catch (e: Throwable) {
                if (favLocationResult == -1) {
                    _msg.value = "Location Was Not Added"
                }
                _msg.value = "something went wrong"
            }
        }
    }


}

class MyFavFactory(val repository: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavouritsViewModel(repository) as T
    }
}