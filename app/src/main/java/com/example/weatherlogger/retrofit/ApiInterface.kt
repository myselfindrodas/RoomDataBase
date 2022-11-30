package com.example.weatherlogger.retrofit

import com.example.weatherlogger.apimodel.WeatherResponse
import retrofit2.http.*

interface ApiInterface {



    @GET("weather")
    suspend fun weather(
        @Query("q") place:String,
        @Query("appid") appid:String,
    ): WeatherResponse



}