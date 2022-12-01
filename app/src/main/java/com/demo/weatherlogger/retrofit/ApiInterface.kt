package com.example.weatherlogger.retrofit

import com.example.weatherlogger.apimodel.WeatherResponse
import retrofit2.http.*

interface ApiInterface {



    @GET("weather")
    suspend fun weather(
        @Query("lat") lat:String,
        @Query("lon") lon:String,
        @Query("appid") appid:String,
    ): WeatherResponse



}