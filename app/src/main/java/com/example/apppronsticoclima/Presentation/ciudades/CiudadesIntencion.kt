package com.example.apppronsticoclima.Presentation.ciudades

import com.example.apppronsticoclima.Repository.modelos.Ciudad

sealed class CiudadesIntencion {
    data class Buscar( val nombre:String ) : CiudadesIntencion()
    data class Seleccionar(val ciudad: Ciudad) : CiudadesIntencion()
    object SetDefault : CiudadesIntencion()
}