package com.example.citymet.data.remote

import com.example.citymet.data.remote.response.CityLatResponse
import com.example.citymet.data.remote.response.CityResponse
import com.example.citymet.data.remote.response.WeatherResponse

interface RemoteDataSource {

    suspend fun getWeatherResponse(lat: Float, long: Float): WeatherResponse
    suspend fun getLatLongCity(city: String): ArrayList<CityLatResponse>
    suspend fun getCities(): ArrayList<CityResponse>
}