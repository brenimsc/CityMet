package com.example.citymet.data.model

data class WeatherInfo(
    val locationName: String,
    val conditionIcon: String,
    val condition: String,
    val temperature: Int,
    val dayOffWeek: String
) {
    val isDay = conditionIcon.last() == 'd'
}
