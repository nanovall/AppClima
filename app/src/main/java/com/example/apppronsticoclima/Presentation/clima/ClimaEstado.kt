package com.example.apppronsticoclima.Presentation.Clima

/**
 * Define todos los posibles estados de la pantalla de Clima.
 */
sealed class ClimaEstado {
    data object Vacio : ClimaEstado()
    data object Cargando : ClimaEstado()
    data class Error(val mensaje: String) : ClimaEstado()

    data class Exitoso(
        val ciudad: String,
        val fecha: String,
        val temperatura: String,
        val descripcion: String,
        val sensacionTermica: String,
        val humedad: String,
        val viento: String,
        val visibilidad: String,
        val visibilidadDesc: String,
        val presion: String,
        val presionDesc: String,
        val nubosidad: String,
        val nubosidadDesc: String,
        val amanecer: String,
        val atardecer: String,
        val tempMaxHoy: String,
        val tempMinHoy: String,
        val pronostico: List<ForecastDay>

    ) : ClimaEstado()
}

/**
 * Modelo de datos simple para un día en la lista de pronóstico.
 */

data class ForecastDay(
    val id: Int,
    val dia: String,
    val fecha: String,
    val descripcion: String,
    val tempMax: String,
    val tempMin: String
)