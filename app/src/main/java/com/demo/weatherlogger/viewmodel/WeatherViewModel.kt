package com.example.weatherlogger.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.weatherlogger.repository.MainRepository
import com.example.weatherlogger.retrofit.ApiHelper
import com.example.weatherlogger.retrofit.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel(private val mainRepository: MainRepository) : ViewModel() {

    fun weather(lat: String, lon: String, appid:String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.weather(lat, lon, appid)))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error Occurred!"))
        }
    }

}