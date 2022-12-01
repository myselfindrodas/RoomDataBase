package com.example.weatherlogger.repository

import androidx.lifecycle.LiveData
import com.example.weatherlogger.retrofit.ApiHelper


class MainRepository(private val apiHelper: ApiHelper) {


    suspend fun weather(lat: String, lon: String, appid:String) = apiHelper.weather(lat, lon, appid)


}