package com.example.citymet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.citymet.data.repository.LocationRepository
import com.example.citymet.data.repository.WeatherRepository
import com.example.citymet.ui.feature.WeatherUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _weatherUiState = MutableStateFlow(WeatherUiState())
    val weatherUiState = _weatherUiState.asStateFlow()

    fun loadInitialData() {

        viewModelScope.launch {
            val location = locationRepository.gesLastLatLong()

            _weatherUiState.update { it.copy(isLoading = true) }

            try {
                val cities = repository.getCities()

                val lat = location?.latitude?.toFloat() ?: -23.533773f
                val long = location?.longitude?.toFloat() ?: -46.625290f

                val weather = repository.getWeather(lat, long)

                _weatherUiState.update {
                    it.copy(
                        cities = cities,
                        weatherInfo = weather,
                        selectedCity = weather.locationName,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _weatherUiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun getLatLongCity(city: String) {
        viewModelScope.launch {
            _weatherUiState.update {
                it.copy(
                    isLoading = true,
                    selectedCity = city,
                    weatherInfo = null,
                    error = null
                )
            }

            try {
                val response = repository.getLatLongCity(city)
                val lat = response?.lat?.toFloat() ?: -23.533773f
                val lon = response?.lon?.toFloat() ?: -46.625290f

                val weatherInfo = repository.getWeather(lat, lon)

                _weatherUiState.update {
                    it.copy(weatherInfo = weatherInfo, isLoading = false)
                }
            } catch (e: Exception) {
                _weatherUiState.update {
                    it.copy(error = e.message, isLoading = false)
                }
            }
        }
    }
}