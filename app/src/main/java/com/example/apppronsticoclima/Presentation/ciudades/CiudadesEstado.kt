package com.example.apppronsticoclima.Presentation.ciudades

import com.example.apppronsticoclima.Repository.modelos.Ciudad

sealed class CiudadesEstado {
    data object Vacio: CiudadesEstado()
    data object Cargando: CiudadesEstado()
    data class Resultado( val ciudades : List<Ciudad> ) : CiudadesEstado()
    data class Error(val mensaje: String): CiudadesEstado()
}