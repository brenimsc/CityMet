package com.example.citymet.ui.viewmodel

import app.cash.turbine.test
import com.example.citymet.MainDispatcherRule
import com.example.citymet.data.model.WeatherInfo
import com.example.citymet.data.remote.response.CityLatResponse
import com.example.citymet.data.repository.LocationRepository
import com.example.citymet.data.repository.WeatherRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class WeatherViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: WeatherViewModel

    @RelaxedMockK
    private lateinit var repository: WeatherRepository

    @RelaxedMockK
    private lateinit var locationRepository: LocationRepository

    private val mockCities = listOf("São Paulo", "Rio de Janeiro", "Belo Horizonte")
    private val mockWeatherInfo = WeatherInfo(
        locationName = "São Paulo",
        dayOffWeek = "Segunda-feira",
        conditionIcon = "sunny",
        condition = "Ensolarado",
        temperature = 25
    )


    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = WeatherViewModel(repository = repository, locationRepository = locationRepository)

        coEvery { repository.getCities() } returns mockCities
        coEvery { repository.getWeather(any(), any()) } returns mockWeatherInfo
    }

    @Test
    fun `getLatLongCity should work with any coordinates`() = runTest {
        val cityName = "São Paulo"
        val mockCity = CityLatResponse(lat = "-23.5", lon = "-46.6", name = "")

        coEvery { viewModel.getLatLongCity(cityName) } coAnswers { mockCity }

        viewModel.weatherUiState.test {
            viewModel.getLatLongCity(cityName)

            val initialState = awaitItem()

            val loadingState = awaitItem()

            assertThat(loadingState.isLoading).isTrue()
            assertThat(loadingState.selectedCity).isEqualTo(cityName)
            assertThat(loadingState.weatherInfo).isNull()
            assertThat(loadingState.error).isNull()

            cancelAndIgnoreRemainingEvents()
        }
    }
}