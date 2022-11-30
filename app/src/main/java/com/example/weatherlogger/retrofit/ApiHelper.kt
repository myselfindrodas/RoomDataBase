package com.example.weatherlogger.retrofit

class ApiHelper(private val apiInterface: ApiInterface) {


    suspend fun weather(q: String, appid:String) = apiInterface.weather(q, appid)


}