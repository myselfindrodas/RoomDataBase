package com.example.weatherlogger.modelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherlogger.repository.MainRepository
import com.example.weatherlogger.retrofit.ApiHelper
import com.example.weatherlogger.viewmodel.WeatherViewModel

class WeathermodelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        WeatherViewModel(MainRepository(apiHelper)) as T

}