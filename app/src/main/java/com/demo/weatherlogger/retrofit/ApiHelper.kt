package com.example.weatherlogger.retrofit

class ApiHelper(private val apiInterface: ApiInterface) {


    suspend fun weather(lat: String, lon: String, appid:String) = apiInterface.weather(lat, lon, appid)


}