package com.example.apppronsticoclima.Repository.modelos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// --- Modelos para traerClima() ---
@Serializable
data class Clima(
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind,
    val clouds: Clouds,
    val sys: Sys,
    val visibility: Int,
    val name: String
)
@Serializable
data class Main(
    val temp: Float,
    @SerialName("feels_like")
    val feels_like: Float,
    @SerialName("temp_min")
    val temp_min: Float,
    @SerialName("temp_max")
    val temp_max: Float,
    val pressure: Int,
    val humidity: Int
)
@Serializable
data class Weather(
    val main: String,
    val description: String,
    val icon: String
)
@Serializable
data class Wind(
    val speed: Float
)
@Serializable
data class Clouds(
    val all: Int
)
@Serializable
data class Sys(
    val country: String? = null, // Hecho opcional para evitar el crash
    val sunrise: Long,
    val sunset: Long
)
// --- Modelos para buscarCiudad() ---
@Serializable
data class Ciudad(
    val name: String,
    val lat: Float,
    val lon: Float,
    val country: String,
    val state: String? = null
)
// --- Modelos para traerPronostico() ---
@Serializable
data class ForecastDTO(
    val list: List<ListForecast>
)
@Serializable
data class ListForecast(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>
)