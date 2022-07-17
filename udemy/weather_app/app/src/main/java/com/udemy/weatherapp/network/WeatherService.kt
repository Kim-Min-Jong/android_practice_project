package com.udemy.weatherapp.network

import com.udemy.weatherapp.models.WeatherResponse
import retrofit2.Call
import retrofit2.http.*

interface WeatherService {
    @GET("2.5/weather")
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("unit") units:String?,
        @Query("appid") appId: String?
    ): Call<WeatherResponse>
}