package com.example.citymet.ui.feature

import com.example.citymet.data.model.WeatherInfo

data class WeatherUiState(
    val weatherInfo: WeatherInfo? = null,
    val cities: List<String> = emptyList(),
    val selectedCity: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
