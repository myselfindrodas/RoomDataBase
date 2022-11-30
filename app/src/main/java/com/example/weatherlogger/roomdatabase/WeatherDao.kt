package com.example.weatherlogger.roomdatabase

import androidx.room.*


@Dao
interface WeatherDao {
    @Query("Select * from weather")
    open fun getWeatherList(): MutableList<Weather?>?

    @Insert
    fun insertWeather(weather: Weather?)

    @Update
    fun updateWeather(weather: Weather?)

    @Delete
    fun deleteWeather(weather: Weather?)
}