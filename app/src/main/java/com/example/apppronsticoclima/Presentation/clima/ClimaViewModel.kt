package com.example.apppronsticoclima.Presentation.Clima

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.apppronsticoclima.Repository.Repositorio
import com.example.apppronsticoclima.Repository.modelos.Clima
import com.example.apppronsticoclima.Repository.modelos.ListForecast
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

class ClimaViewModel(
    val repositorio: Repositorio,
    val lat: Float,
    val lon: Float,
    val nombre: String
) : ViewModel() {

    var uiState by mutableStateOf<ClimaEstado>(ClimaEstado.Vacio)

    fun ejecutar(intencion: ClimaIntencion) {
        when (intencion) {
            is ClimaIntencion.CargarClima -> traerClimaYPronostico()
            is ClimaIntencion.CompartirPronostico -> {
                // TODO: Lógica para compartir
            }
            else -> {
            }
        }
    }

    private fun traerClimaYPronostico() {
        uiState = ClimaEstado.Cargando
        viewModelScope.launch {
            try {
                val clima = repositorio.traerClima(lat = lat, lon = lon)
                val pronostico = repositorio.traerPronostico(nombre = nombre)
                uiState = mapClimaAEstado(clima, pronostico)
            } catch (exception: Exception) {
                uiState = ClimaEstado.Error(exception.message ?: "Error desconocido")
            }
        }
    }

    private fun mapClimaAEstado(clima: Clima, pronostico: List<ListForecast>): ClimaEstado.Exitoso {


        val descripcionNubosidad = obtenerDescripcionNubosidad(clima.clouds.all)
        val descripcionVisibilidad = obtenerDescripcionVisibilidad(clima.visibility)

        return ClimaEstado.Exitoso(
            ciudad = "${clima.name}, ${clima.sys.country}",
            fecha = "Hoy, ${formatearFecha(Date())}",
            temperatura = "${clima.main.temp.roundToInt()}°",
            descripcion = clima.weather.firstOrNull()?.description?.capitalize() ?: "N/A",
            sensacionTermica = "Sensación térmica: ${clima.main.feels_like.roundToInt()}°C",
            humedad = "${clima.main.humidity}%",
            viento = "${clima.wind.speed.roundToInt()} km/h",

            visibilidad = "${clima.visibility / 1000} km",
            visibilidadDesc = descripcionVisibilidad,

            presion = "${clima.main.pressure} hPa",
            presionDesc = "Presión atmosférica",

            nubosidad = "${clima.clouds.all}%",
            nubosidadDesc = descripcionNubosidad,

            amanecer = formatearHora(clima.sys.sunrise),
            atardecer = formatearHora(clima.sys.sunset),
            tempMaxHoy = "Máx: ${clima.main.temp_max.roundToInt()}°",
            tempMinHoy = "Mín: ${clima.main.temp_min.roundToInt()}°",
            pronostico = mapPronosticoAEstado(pronostico)
        )
    }

    private fun mapPronosticoAEstado(pronostico: List<ListForecast>): List<ForecastDay> {
        return pronostico.distinctBy {
            formatearFecha(Date(it.dt * 1000L), "dd/MM")
        }.mapIndexed { index, forecast ->
            val fecha = Date(forecast.dt * 1000L)
            ForecastDay(
                id = index,
                dia = formatearFecha(fecha, "EEE"),
                fecha = formatearFecha(fecha, "dd MMM"),
                descripcion = forecast.weather.firstOrNull()?.description?.capitalize() ?: "N/A",
                tempMax = "${forecast.main.temp_max.roundToInt()}°",
                tempMin = "${forecast.main.temp_min.roundToInt()}°"
            )
        }.take(5)
    }

    private fun formatearFecha(fecha: Date, formato: String = "EEEE, dd 'de' MMMM"): String {
        return SimpleDateFormat(formato, Locale("es", "ES")).format(fecha).capitalize()
    }
    private fun formatearHora(timestamp: Long): String {
        return SimpleDateFormat("hh:mm a", Locale("es", "ES")).format(Date(timestamp * 1000L))
    }
    private fun String.capitalize(): String {
        return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }



    private fun obtenerDescripcionNubosidad(porcentaje: Int): String {
        return when (porcentaje) {
            0 -> "Despejado"
            in 1..10 -> "Mayormente despejado"
            in 11..40 -> "Parcialmente nublado"
            in 41..80 -> "Mayormente nublado"
            in 81..100 -> "Cubierto"
            else -> "N/A"
        }
    }

    private fun obtenerDescripcionVisibilidad(metros: Int): String {
        return when (metros) {
            in 9000..10000 -> "Excelente"
            in 7000..8999 -> "Buena"
            in 4000..6999 -> "Moderada"
            in 1000..3999 -> "Reducida"
            else -> "Niebla"
        }
    }
}


class ClimaViewModelFactory(
    private val repositorio: Repositorio,
    private val lat: Float,
    private val lon: Float,
    private val nombre: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClimaViewModel::class.java)) {
            return ClimaViewModel(repositorio, lat, lon, nombre) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}