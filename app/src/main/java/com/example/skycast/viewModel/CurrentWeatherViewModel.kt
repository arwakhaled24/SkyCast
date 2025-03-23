package com.example.skycast.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.skycast.data.dataClasses.currentWeather.CurrentWeatherRespond
import com.example.skycast.data.dataClasses.forecastRespond.ForecasteRespond
import com.example.skycast.data.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrentWeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    private var _currentWeather = MutableLiveData<CurrentWeatherRespond>()
    var currentWeather: LiveData<CurrentWeatherRespond> = _currentWeather
    private var _forecast = MutableLiveData<ForecasteRespond>()
    var forecast: LiveData<ForecasteRespond> = _forecast

    private var _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun getCurrentWeather(
        lat: String,
        lon: String,
        language: String = "en",
        unit: String = "standard"
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _currentWeather.postValue(repository.getCurrentWeather(lat, lon, language, unit))
        }
    }

    fun getForecast(
        lat: String,
        lon: String,
        language: String = "en",
        unit: String = "standard"
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _forecast.postValue(repository.getForecastgetCurrentWeather(lat, lon, language, unit))
        }
    }


}


class MyFactory(private val repository: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return CurrentWeatherViewModel(repository) as T
    }
}