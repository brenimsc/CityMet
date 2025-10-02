package com.example.citymet.data.remote

import com.example.citymet.BuildConfig
import com.example.citymet.data.remote.response.CityLatResponse
import com.example.citymet.data.remote.response.CityResponse
import com.example.citymet.data.remote.response.WeatherResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient
) : RemoteDataSource {

    companion object {
        private const val BASE_URL = "https://api.openweathermap.org"

        private const val API_KEY = BuildConfig.API_KEY
        private const val BASE_URL_BRASIL = "https://brasilapi.com.br"
    }

    override suspend fun getWeatherResponse(
        lat: Float,
        long: Float
    ): WeatherResponse {
        return httpClient.get("$BASE_URL/data/2.5/weather?lat=$lat&lon=$long&appid=$API_KEY&units=metric")
            .body()
    }

    override suspend fun getLatLongCity(city: String): ArrayList<CityLatResponse> {
        return httpClient.get("$BASE_URL/geo/1.0/direct?q=$city&limit=1&appid=$API_KEY").body()
    }

    override suspend fun getCities(): ArrayList<CityResponse> =
        httpClient.get("${BASE_URL_BRASIL}/api/ibge/municipios/v1/SP").body()
}