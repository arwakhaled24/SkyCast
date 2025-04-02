package com.example.skycast.home.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.skycast.data.RespondStatus
import com.example.skycast.data.dataClasses.currentWeather.CurrentWeatherRespond
import com.example.skycast.data.dataClasses.forecastRespond.ForecasteRespond
import com.example.skycast.data.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    private var _currentWeather = MutableStateFlow<RespondStatus<CurrentWeatherRespond>>(RespondStatus.Loading)
    var currentWeather: MutableStateFlow<RespondStatus<CurrentWeatherRespond>> = _currentWeather

    private var _forecast = MutableStateFlow<RespondStatus<ForecasteRespond>>(RespondStatus.Loading)
    var forecast:MutableStateFlow<RespondStatus<ForecasteRespond>> = _forecast

    private var _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun getCurrentWeather(
        lat: String,
        lon: String,
        language: String = "en",
        unit: String = "metric"
    ) {
        Log.i("TAG", "getCurrentWeather: ooh")
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getCurrentWeather(lat, lon, language, unit)
            result.catch {
                _currentWeather.value = RespondStatus.Error(it)
            }.collect {it
                if (it!= null){
                    _currentWeather.value = RespondStatus.Success<CurrentWeatherRespond>(it)
                    Log.i("TAG", "getCurrentWeather: ooh2")
                }
            }
        }
    }

    fun getForecast(
        lat: String,
        lon: String,
        language: String = "en",
        unit: String = "metric"
    ) {
        Log.i("TAG", "getCurrentWeather: ooh3")
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getForecastgetCurrentWeather(lat, lon, language, unit)
            result.catch {
                _forecast.value = RespondStatus.Loading
            }.collect{
                if (it != null){
                    _forecast.value= RespondStatus.Success<ForecasteRespond>(it)
                    Log.i("TAG", "getCurrentWeather: ooh4")
                }
            }
        }
    }


}


class MyFactory(private val repository: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return WeatherViewModel(repository) as T
    }
}