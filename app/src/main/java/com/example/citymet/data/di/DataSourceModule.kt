package com.example.citymet.data.di

import com.example.citymet.data.remote.RemoteDataSource
import com.example.citymet.data.remote.RemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Binds
    @Singleton
    fun bindRemoteDataSource(remoteDataSource: RemoteDataSourceImpl): RemoteDataSource
}