package com.example.apppronsticoclima.Presentation.startup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.apppronsticoclima.Repository.UserPreferences
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

//Acá >> toda la lógica de decisión de inicio.
sealed interface StartupEfecto {
    data class NavegarAClima(val lat: Float, val lon: Float, val nombre: String) : StartupEfecto
    object NavegarACiudades : StartupEfecto
}

class StartupViewModel(
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _efecto = Channel<StartupEfecto>()
    val efecto = _efecto.receiveAsFlow()

    fun decidirRutaInicial() {
        viewModelScope.launch {
            val ciudadGuardada = userPreferences.obtenerCiudadSeleccionada()
            if (ciudadGuardada != null) {
                _efecto.send(
                    StartupEfecto.NavegarAClima(
                        lat = ciudadGuardada.lat,
                        lon = ciudadGuardada.lon,
                        nombre = ciudadGuardada.name
                    )
                )
            } else {
                _efecto.send(StartupEfecto.NavegarACiudades)
            }
        }
    }
}

class StartupViewModelFactory(
    private val userPreferences: UserPreferences
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StartupViewModel::class.java)) {
            return StartupViewModel(userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
