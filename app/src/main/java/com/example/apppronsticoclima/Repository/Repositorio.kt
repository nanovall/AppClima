package com.example.apppronsticoclima.Repository

import com.example.apppronsticoclima.Repository.modelos.Ciudad
import com.example.apppronsticoclima.Repository.modelos.Clima
import com.example.apppronsticoclima.Repository.modelos.ListForecast

interface Repositorio {
    suspend fun buscarCiudad(ciudad: String): List<Ciudad>
    suspend fun traerClima(lat: Float, lon: Float) : Clima
    suspend fun traerPronostico(nombre: String) : List<ListForecast>
    suspend fun traerPronostico(lat: Float, lon: Float) : List<ListForecast>
}