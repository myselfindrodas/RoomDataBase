package com.app.hcsassist.retrofit

import com.example.weatherlogger.retrofit.ApiInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"


    private fun getRetrofit(): Retrofit {
        val httpClient =
            OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        httpClient.addInterceptor(interceptor)
        return Retrofit.Builder()
            .baseUrl(BASE_URL).client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build() //Doesn't require the adapter
    }


    val apiService: ApiInterface = getRetrofit().create(ApiInterface::class.java)

}