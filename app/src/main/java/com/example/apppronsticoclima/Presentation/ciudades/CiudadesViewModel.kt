package com.example.apppronsticoclima.Presentation.ciudades

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.apppronsticoclima.Repository.Repositorio
import com.example.apppronsticoclima.Repository.UserPreferences
import com.example.apppronsticoclima.Repository.modelos.Ciudad
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

sealed interface CiudadesEfecto {
    data class NavegarAClima(val lat: Float, val lon: Float, val nombre: String) : CiudadesEfecto
    object PedirPermisoUbicacion : CiudadesEfecto
}

class CiudadesViewModel(
    val repositorio: Repositorio,
    private val userPreferences: UserPreferences
) : ViewModel(){

    var uiState by mutableStateOf<CiudadesEstado>(CiudadesEstado.Vacio)
    var ciudades : List<Ciudad> = emptyList()

    // Variable privada para guardar el estado antes de la geolocalización
    private var stateBeforeGeoloc: CiudadesEstado? = null

    private val _efecto = Channel<CiudadesEfecto>()
    val efecto = _efecto.receiveAsFlow()

    fun ejecutar(intencion: CiudadesIntencion){
        when(intencion){
            is CiudadesIntencion.Buscar -> buscar(nombre = intencion.nombre)
            is CiudadesIntencion.Seleccionar -> seleccionar(ciudad = intencion.ciudad)
            is CiudadesIntencion.UsarGeolocalizacion -> {
                // Guardamos el estado actual y luego mostramos el indicador de carga.
                stateBeforeGeoloc = uiState
                uiState = CiudadesEstado.Cargando
                viewModelScope.launch {
                    _efecto.send(CiudadesEfecto.PedirPermisoUbicacion)
                }
            }
            is CiudadesIntencion.FalloDeGeolocalizacion -> {
                // Si falla, restauramos el estado anterior.
                uiState = stateBeforeGeoloc ?: CiudadesEstado.Error("No se pudo obtener la ubicación. Asegúrate de que la localización esté activada.")
                stateBeforeGeoloc = null
            }
            is CiudadesIntencion.NavegarAClimaConUbicacion -> {
                viewModelScope.launch {
                    _efecto.send(CiudadesEfecto.NavegarAClima(intencion.lat, intencion.lon, "Mi Ubicación"))
                }
                // Al navegar, restauramos el estado anterior para que la lista no se borre.
                uiState = stateBeforeGeoloc ?: CiudadesEstado.Vacio
                stateBeforeGeoloc = null
            }
            CiudadesIntencion.SetDefault -> setDefault()
        }
    }

    private fun buscar( nombre: String){

        uiState = CiudadesEstado.Cargando
        viewModelScope.launch {
            try {
                ciudades = repositorio.buscarCiudad(nombre)
                if (ciudades.isEmpty()) {
                    uiState = CiudadesEstado.Vacio
                } else {
                    uiState = CiudadesEstado.Resultado(ciudades)
                }
            } catch (exeption: Exception){
                uiState = CiudadesEstado.Error(exeption.message ?: "error desconocido")
            }
        }
    }

    private fun seleccionar(ciudad: Ciudad){
        userPreferences.guardarCiudadSeleccionada(ciudad)
        viewModelScope.launch {
            _efecto.send(CiudadesEfecto.NavegarAClima(
                lat = ciudad.lat,
                lon = ciudad.lon,
                nombre = ciudad.name
            ))
        }
    }

    private fun setDefault(){
        uiState = CiudadesEstado.Vacio
    }
}


class CiudadesViewModelFactory(
    private val repositorio: Repositorio,
    private val userPreferences: UserPreferences
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CiudadesViewModel::class.java)) {
            return CiudadesViewModel(repositorio, userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}