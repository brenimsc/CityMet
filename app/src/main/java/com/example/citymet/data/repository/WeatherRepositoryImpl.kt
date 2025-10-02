package com.example.citymet.data.repository

import com.example.citymet.data.model.WeatherInfo
import com.example.citymet.data.remote.RemoteDataSource
import com.example.citymet.data.remote.response.CityLatResponse
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject
import kotlin.math.roundToInt

class WeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : WeatherRepository {

    override suspend fun getWeather(
        lat: Float,
        long: Float
    ): WeatherInfo {
        val response = remoteDataSource.getWeatherResponse(lat, long)
        val weather = response.weather[0]

        return WeatherInfo(
            locationName = response.name.ifBlank { "Sem nome" },
            conditionIcon = weather.icon,
            condition = weather.main,
            temperature = response.main.temp.roundToInt(),
            dayOffWeek = LocalDate.now().dayOfWeek.getDisplayName(
                TextStyle.FULL,
                Locale.getDefault()
            )
        )
    }

    override suspend fun getLatLongCity(city: String): CityLatResponse? {
        return remoteDataSource.getLatLongCity(city).firstOrNull()
    }

    override suspend fun getCities(): List<String> {
        return remoteDataSource.getCities().map {
            it.nome
        }
    }
}