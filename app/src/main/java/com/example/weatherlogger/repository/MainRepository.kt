package com.example.weatherlogger.repository

import com.example.weatherlogger.retrofit.ApiHelper


class MainRepository(private val apiHelper: ApiHelper) {

    suspend fun weather(q: String, appid:String) = apiHelper.weather(q, appid)

}