package com.example.citymet.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CityResponse(
    val nome: String,
    @SerialName("codigo_ibge") val codigoIbge: String
)

@Serializable
data class CityLatResponse(
    val name: String,
    val lat: String,
    val lon: String
)