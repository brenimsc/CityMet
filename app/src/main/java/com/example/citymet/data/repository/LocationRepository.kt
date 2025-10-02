package com.example.citymet.data.repository

import android.location.Location

interface LocationRepository {

    suspend fun gesLastLatLong(): Location?
}