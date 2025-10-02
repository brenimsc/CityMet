package com.example.citymet.data.repository

import com.example.citymet.data.model.WeatherInfo
import com.example.citymet.data.remote.response.CityLatResponse

interface WeatherRepository {

    suspend fun getWeather(lat: Float, long: Float): WeatherInfo

    suspend fun getLatLongCity(city: String): CityLatResponse?
    suspend fun getCities(): List<String>
}