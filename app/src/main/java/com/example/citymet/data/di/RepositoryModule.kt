package com.example.citymet.data.di

import com.example.citymet.data.repository.LocationRepository
import com.example.citymet.data.repository.LocationRepositoryImpl
import com.example.citymet.data.repository.WeatherRepository
import com.example.citymet.data.repository.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {

    @Binds
    fun bindsWeatherRepository(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository

    @Binds
    fun bindWeatherLocationRepository(locationRepositoryImpl: LocationRepositoryImpl): LocationRepository
}