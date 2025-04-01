package com.example.skycast.alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.skycast.data.dataClasses.NotificationDataClass
import com.example.skycast.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class AlarmViewModel(val repo: WeatherRepository) : ViewModel() {

    private var _notificationList = MutableStateFlow<List<NotificationDataClass>>(emptyList())
    var notificationLis = _notificationList

    fun getAllNotification() {
        viewModelScope.launch {
            repo.getAllNotification().collect {
                _notificationList.emit(it)
            }
        }
    }

    fun deleteNotification(notificationId: UUID) {
        viewModelScope.launch {
            repo.deleteNotification(notificationId)
        }
    }

    fun addNotidication(notification: NotificationDataClass) {
        viewModelScope.launch {
            repo.addNotification(notification)
        }
    }
}

class AlarmFactory(val repo: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlarmViewModel(repo) as T
    }
}