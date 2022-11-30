package com.example.weatherlogger.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.weatherlogger.repository.MainRepository
import com.example.weatherlogger.retrofit.Resource
import kotlinx.coroutines.Dispatchers

class WeatherViewModel(private val mainRepository: MainRepository) : ViewModel() {

    fun weather(q: String, appid:String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.weather(q, appid)))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error Occurred!"))
        }
    }

}