package com.example.apppronsticoclima.Presentation.Clima

/**
 * Define las acciones que el usuario puede realizar.
 */
sealed class ClimaIntencion {
    data object CargarClima : ClimaIntencion()
    data object VolverAtras : ClimaIntencion()
    data object CompartirPronostico : ClimaIntencion()
}