package com.example.weatherlogger.apimodel

import com.google.gson.annotations.SerializedName

data class WeatherResponse(

    @field:SerializedName("main")
    val data: main? = null,
)

data class main(
    @field:SerializedName("temp")
    val temp: String? = null,
)