package com.example.apppronsticoclima.Presentation.ciudades

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.apppronsticoclima.Repository.Repositorio
import com.example.apppronsticoclima.Repository.UserPreferences
import com.example.apppronsticoclima.Repository.modelos.Ciudad
import kotlinx.coroutines.launch

class CiudadesViewModel(
    val repositorio: Repositorio,
    val navController: NavController,
    private val userPreferences: UserPreferences
) : ViewModel(){

    var uiState by mutableStateOf<CiudadesEstado>(CiudadesEstado.Vacio)
    var ciudades : List<Ciudad> = emptyList()

    fun ejecutar(intencion: CiudadesIntencion){
        when(intencion){
            is CiudadesIntencion.Buscar -> buscar(nombre = intencion.nombre)
            is CiudadesIntencion.Seleccionar -> seleccionar(ciudad = intencion.ciudad)
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

        val lat = ciudad.lat
        val lon = ciudad.lon
        val nombre = ciudad.name

        navController.navigate("VistaClima/$lat/$lon/$nombre")
    }

    private fun setDefault(){
        uiState = CiudadesEstado.Vacio
    }
}


class CiudadesViewModelFactory(
    private val repositorio: Repositorio,
    private val navController: NavController,
    private val userPreferences: UserPreferences
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CiudadesViewModel::class.java)) {
            return CiudadesViewModel(repositorio,navController, userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}